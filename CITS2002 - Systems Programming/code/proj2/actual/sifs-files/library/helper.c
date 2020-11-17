#define _GNU_SOURCE 1 // Enables strdup to 
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "../sifs.h"
#include "sifs-internal.h"
#include "helper.h"

//  Project 2 2019
//  ----------------------------------------------------------------------
//  GLOBAL HELPER VARIABLES
//  ----------------------------------------------------------------------

// A summary of the call
SUMMARY sum;

// The components of the pathname of the call
char** pathParts;

// The blockIDs of the components
int* pathPartbIDs;

// The number of parts
int partNo = 0;

//  ----------------------------------------------------------------------
//  GENERAL HELPER FUNCTIONS
//  ----------------------------------------------------------------------

/**
 * Return the blocks required
 * @param length The length of the file
 * @return The number of blocks
 */
int getBlocksReq(long length)
{
    // Calculate the blocks required as a double
    double bReq = (double) length / (double) (sum.bsize);

    // Get result as int
    int bReqInt = (int) bReq;

    // Get decimal value
    double decVal = bReq - bReqInt;

    // If decimal value is not 0
    if (decVal != 0.0) {
        bReq++;
    }

    // Return integer value
    return((int) bReq);
}

/**
 * Finds a contiguous series of blocks for a given file
 * @param length (of the file)
 * @param start The first block ID (inclusive)
 * @param end The last block ID (inclusive)
 * @return SIFS_SUCCESS or SIFS_FAIL
 */
int getContigSeries(long length, SIFS_BLOCKID* start, SIFS_BLOCKID* end)
{
    // If length is zero
    if (length == 0) {

        // Both values are 0
        *start = 0;
        *end = 0;

        // Finish successfully
        return SIFS_SUCCESS;
    }

    // Get the number of blocks required
    int blocksReq = getBlocksReq(length);

    // For all blocks
    for (int b = 0; b < sum.bnum; b++) {

        // The current block ID
        int curbID = b;

        // While the current ID is unused, increase it
        while (sum.bitmap[curbID] == SIFS_UNUSED) {
            curbID++;
        }

        // If the consecutive number of unused blocks found
        // is equal to how many are needed
        if ((curbID - b) >= blocksReq) {

            // The contiguous series has been found,
            // so save info about it
            *start = b;
            *end = b + blocksReq - 1;

            // Finish successfully
            return SIFS_SUCCESS;
        }
    }

    // Else, no series could be found,
    // so set error to "no space on volume"
    SIFS_errno = SIFS_ENOSPC;

    // Finish unsuccessfully
    return SIFS_FAIL;
}

/**
 * Change a value of the bitmap and write it to the volume
 * @param blockID The block ID to change
 * @param newBit The new SIFS_BIT value 
 */
void updateBitmap(SIFS_BLOCKID blockID, SIFS_BIT newBit)
{
    // Change bitmap
    sum.bitmap[blockID] = newBit;

    // Seek to start of bitmap (or end of header)
    fseek(sum.volfile, sum.headerSize, SEEK_SET);

    // Write bitmap to volume
    fwrite(sum.bitmap, sum.bitmapSize, 1, sum.volfile);
}

/*
 * Get the next unused block ID
 */
SIFS_BLOCKID getNextUnusedID()
{
    // For all blocks
    for (int curbID = 0; curbID < sum.bnum; curbID++) {

        // If the bitmap indicates that the block is unused
        if (sum.bitmap[curbID] == SIFS_UNUSED) {

            // Return the block ID
            return(SIFS_BLOCKID) curbID;
        }
    }

    // Return -1 as none were found
    return -1;
}

/**
 * Seek the file to a certain bID
 * @param bID The blockID to go to
 */
void seekToBID(SIFS_BLOCKID bID)
{
    // Position of block is:
    // header size + bitmap size + (block size * blockID)
    int blockPos = sum.bstart + (sum.bsize * bID);

    // Seek to block from start of file
    fseek(sum.volfile, blockPos, SEEK_SET);
}

/**
 * Get the directory block at a given block ID
 * @param blockID The block ID of the directory
 * @return The directory block structure
 */
SIFS_DIRBLOCK getDirBlock(SIFS_BLOCKID blockID)
{
    // Seek to the directory block
    seekToBID(blockID);

    // Read the block in
    SIFS_DIRBLOCK tempDB;
    fread(&tempDB, sizeof tempDB, 1, sum.volfile);

    // Return the block
    return tempDB;
}

/**
 * Get the file block at a given block ID
 * @param blockID The block ID of the file
 * @return The file block structure
 */
SIFS_FILEBLOCK getFileBlock(SIFS_BLOCKID blockID)
{
    // Seek to the file block
    seekToBID(blockID);

    // Read the block in
    SIFS_FILEBLOCK tempFB;
    fread(&tempFB, sizeof tempFB, 1, sum.volfile);

    // Return the block
    return tempFB;
}

/*
 * Remove the entry of the last part from its parent
 */
void removeLastFromParent()
{
    // Get the parent directory block 
    SIFS_DIRBLOCK parDir = getDirBlock(sum.lastParentPart.blockID);

    // If there are multiple entries
    if (parDir.nentries >= 2) {

        // Find the position to remove
        //  Holder
        int remPos = 0;
        //  For all entries
        for (int e = 0; e < parDir.nentries; e++) {
            // If current ID matches last part ID
            if (parDir.entries[e].blockID == sum.lastPart.blockID) {
                // Save position and stop 
                remPos = e;
                break;
            }
        }

        // Remove the entry by shifting entries left
        for (int n = remPos; n < parDir.nentries; n++) {
            parDir.entries[n] = parDir.entries[n + 1];
        }
    }

    //  Decrease the number of entries
    parDir.nentries--;

    // 	Update the modification time 
    parDir.modtime = time(NULL);

    //  Write the updated parent directory to the volume
    seekToBID(sum.lastParentPart.blockID);
    fwrite(&parDir, sizeof parDir, 1, sum.volfile);
}

/**
 * Clear a block with zeroes
 * @param blockID The blockID of the block to clear
 */
void clearBlock(SIFS_BLOCKID blockID)
{
    // Create an array that simulates a block
    char block[sum.bsize];

    // Fill it with zeroes
    memset(block, 0, sizeof block);

    // Seek to the blockID
    seekToBID(blockID);

    // Write the empty block over the block
    fwrite(block, sizeof block, 1, sum.volfile);
}

//  ----------------------------------------------------------------------
//  PREPARING FOR CALLS
//  ----------------------------------------------------------------------

/**
 * Get the entries of a directory in bID-fileIndex pairs
 * @param dir
 * @param entries
 */
void getDirEntries(SIFS_DIRBLOCK dir, SIFS_BLOCKID entries[][2])
{
    // For all entries
    for (int e = 0; e < SIFS_MAX_ENTRIES; e++) {

        // If current entry holds something and entries exist
        if (e < dir.nentries && dir.nentries != 0) {

            // Add actual values
            entries[e][0] = dir.entries[e].blockID;
            entries[e][1] = dir.entries[e].fileindex;

        } else {

            // Otherwise, add zeroes
            entries[e][0] = 0;
            entries[e][1] = 0;
        }
    }
}

/**
 * Check that a given parent directory contains a given child
 * @param parDir The parent directory as a structure
 * @param childPart The child as a path component position
 * @return True if the parent is the parent of the child
 */
bool parentHasChild(SIFS_DIRBLOCK parDir, int childPart)
{
    // Get the child (entries) of the parent
    SIFS_BLOCKID children[24][2];
    getDirEntries(parDir, children);

    // For each child of the parent
    for (int e = 0; e < parDir.nentries; e++) {

        // Get the current child block ID
        int curChildbID = children[e][0];

        // Seek to the child block
        seekToBID(curChildbID);

        // If the child block is a directory block
        if (sum.bitmap[curChildbID] == SIFS_DIR) {

            // Read in the child block
            SIFS_DIRBLOCK childB = getDirBlock(curChildbID);

            // If the child block has the same name as the inputted child
            if (strcmp(childB.name, pathParts[childPart]) == 0) {

                // The child's blockID has been found
                pathPartbIDs[childPart] = curChildbID;

                // Return true as a child was found
                return true;
            }

        } else if (sum.bitmap[curChildbID] == SIFS_FILE) {

            // Else if the child is a file block

            // Read in the child file block
            SIFS_FILEBLOCK childFB = getFileBlock(curChildbID);

            // For all files in the child
            for (int f = 0; f < childFB.nfiles; f++) {

                // If there is match
                if (strcmp(childFB.filenames[f], pathParts[childPart]) == 0) {

                    // The child's blockID has been found
                    pathPartbIDs[childPart] = curChildbID;

                    // Return true as a child was found
                    return true;
                }
            }
        }
    }

    // Return false as no child was found
    return false;
}

/*
 * Verify that a parent path component holds a child path component
 * @param parPart parent path component position
 * @param childPart child path component position
 * @return True when the parent holds the child, and false otherwise
 */
bool verifyRel(int parPart, int childPart)
{
    // If either position is invalid
    if (parPart >= partNo || childPart >= partNo) {

        // Do not process further
        return false;
    }

    // Scan to the parent directory
    seekToBID(pathPartbIDs[parPart]);

    // Read in the parent directory
    SIFS_DIRBLOCK parDir;
    fread(&parDir, sizeof parDir, 1, sum.volfile);

    // The child is the last part and the last part should be new
    if (childPart == partNo - 1 && sum.lastPartNew) {

        // If the parent already has a child of the same name
        if (parentHasChild(parDir, childPart)) {

            // Set error to "volume/file/directory already exists"
            SIFS_errno = SIFS_EEXIST;

            // The wanted relationship is invalid,
            // as child names must be unique
            return false;
        }

        // The child will have the next unused bID
        pathPartbIDs[childPart] = getNextUnusedID();

        // The relationship is valid
        return true;
    }

    // If the parent has no children
    if (parDir.nentries == 0) {

        // The relationship cannot exist
        return false;
    }

    // If parent has the child
    if (parentHasChild(parDir, childPart)) {

        // The relationship is valid
        return true;
    }

    // Else, the relationship is invalid
    return false;
}

/*
 * Find the block ID of each path component
 * @return SIFS_FAIL or SIFS_SUCCESS
 */
int getPartBlockIDs(void)
{
    // Allocate memory to the partbIDs array
    pathPartbIDs = malloc(partNo * sizeof(int));

    // Check allocation
    if (pathParts == NULL) {

        // Set error to "memory allocation failed"
        SIFS_errno = SIFS_ENOMEM;

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // The first part is always the root,
    // which is always at block 0
    pathPartbIDs[0] = 0;

    // If the path is just the root directory
    if (sum.pathJustRoot) {

        // The path is valid
        return SIFS_SUCCESS;
    }

    // Attempt to verify the parent-child relationship
    // between each pair of path components,
    // saving the block IDs of the parts in the process
    int parts = 0;
    while (verifyRel(parts, parts + 1)) {
        parts++;
    }

    // If existence error was encountered
    if (SIFS_errno == SIFS_EEXIST) {

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Add one for the always-valid root directory
    parts++;

    // If parts verified equals number of parts
    if (parts == partNo) {

        // The path is valid as parts are valid
        return SIFS_SUCCESS;
    }

    // Otherwise, the path is invalid
    SIFS_errno = SIFS_ENOENT;

    // Finish unsuccessfully
    return SIFS_FAIL;
}

/*
 * Break down a slash(/)-separated path into its components
 * Can handle multiple slashes
 * @return SIFS_FAIL or SIFS_SUCCESS
 */
int getPathParts(const char* pathname)
{
    // Allocate memory
    pathParts = malloc(SIFS_MAX_ENTRIES * sizeof(char*));

    // Check allocation
    if (pathParts == NULL) {

        // Set error to "memory allocation failed"
        SIFS_errno = SIFS_ENOMEM;

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Add root directory
    pathParts[0] = "";
    partNo++;

    // If the path is just the root
    if (sum.pathJustRoot) {

        // Finish successfully
        // as there is no need to break it down
        return SIFS_SUCCESS;
    }

    // Get copy of string
    char* pathcpy = strdup(pathname);

    // Check memory allocation
    if (pathcpy == NULL) {

        // Set error to "memory allocation failed"
        SIFS_errno = SIFS_ENOMEM;

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Split the string by slashes
    pathcpy = strtok(pathcpy, "/");

    // While there is a next part
    while (pathcpy != NULL) {

        // Make holder point to entry duplicate,
        // and let strdup allocate memory
        pathParts[partNo] = pathcpy;

        // Check  memory allocation
        if (pathParts[partNo] == NULL) {

            // Set error to "memory allocation failed"
            SIFS_errno = SIFS_ENOMEM;

            // Finish unsuccessfully
            return SIFS_FAIL;
        }

        // If path part length is too large
        if (strlen(pathParts[partNo]) > SIFS_MAX_NAME_LENGTH) {

            // Set error to "invalid argument"
            SIFS_errno = SIFS_EINVAL;

            // Finish unsuccessfully
            return SIFS_FAIL;
        }

        // Move to next part and index
        pathcpy = strtok(NULL, "/");
        partNo++;
    }

    // Finish successfully
    return SIFS_SUCCESS;
}

/*
 * Check if a volume is valid
 * @return SIFS_FAIL or SIFS_SUCCESS
 */
int isVolValid(void)
{
    // Check block size and number of blocks
    bool validBSize = sum.bsize >= SIFS_MIN_BLOCKSIZE;
    bool validBNum = sum.bnum > 0;

    // Check size of whole volume
    fseek(sum.volfile, 0, SEEK_END);
    long realSize = ftell(sum.volfile);
    long calcSize = (sum.bsize * sum.bnum) + sum.bstart;
    bool validSize = calcSize == realSize;

    // Check the root directory is a directory in the bitmap
    bool rootBM = sum.bitmap[SIFS_ROOTDIR_BLOCKID] == SIFS_DIR;

    // Check bitmap
    int trueBlockNum = 0;
    // For all blocks
    for (int b = 0; b < sum.bnum; b++) {

        // If a valid SIFS BIT, increase block count
        // Otherwise, do not increase block count
        switch (sum.bitmap[b]) {
            case SIFS_UNUSED:
            case SIFS_DIR:
            case SIFS_FILE:
            case SIFS_DATABLOCK:
                trueBlockNum++;
                break;
            default:
                break;
        }
    }
    bool validBitmap = trueBlockNum == sum.bnum;

    // If all checks are okay
    if (validBSize && validBNum && validSize
            && validBitmap && rootBM) {

        // Finish successfully
        return SIFS_SUCCESS;
    }

    // Else, the volume is not valid
    // Set error to "not a volume"
    SIFS_errno = SIFS_ENOTVOL;

    // Finish unsuccessfully
    return SIFS_FAIL;
}

/*
 * Check the call's wanted type is correct
 * @return SIFS_FAIL or SIFS_SUCCESS
 */
int isDesiredType()
{
    // If the path is just the root
    if (sum.pathJustRoot) {
        // If last part is new
        if (sum.lastPartNew) {
            // No checks needed
            return SIFS_SUCCESS;
        } else {

            // Else if the last part is not new,
            // the type should the directory type
            if (!sum.lastIsFile) {
                return SIFS_SUCCESS;
            } else {
                // We are trying to read the root as file
                SIFS_errno = SIFS_ENOTFILE;
                return SIFS_FAIL;
            }
        }
    }

    // The ID of the part to check
    SIFS_BLOCKID checkbID = 0;

    // If the last part is new
    if (sum.lastPartNew) {

        // Check the parent
        checkbID = sum.lastParentPart.blockID;
    } else {

        // Else If the last part exists, 
        // check the last part 
        checkbID = sum.lastPart.blockID;
    }

    // The type of bit expected
    SIFS_BIT exp;

    // Fill type expected
    if (sum.lastIsFile) {
        exp = SIFS_FILE;
    } else {
        exp = SIFS_DIR;
    }

    // If the blockID to check is not the right type
    if (sum.bitmap[checkbID] != exp) {

        // If type should have been a directory
        if (exp == SIFS_DIR) {

            // Set error to "not a directory"
            SIFS_errno = SIFS_ENOTDIR;
        } else {

            // Else if it should have been a file
            // Set error to "not a file"
            SIFS_errno = SIFS_ENOTFILE;
        }

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Finish successfully
    return SIFS_SUCCESS;
}

/**
 * Handle slashes
 * @param pathname The original path inputted
 */
void handleSlashes(const char* pathname)
{
    // Count how many slashes there are
    int len = strlen(pathname);
    int slashCount = 0;
    int index = 0;
    while (pathname[index] != '\0' && pathname[index] == '/') {
        slashCount++;
        index++;
    }

    // If the path is just slashes,
    // the original path is just the root directory 
    if (len == slashCount || strcmp(pathname, "/") == 0) {

        // Record this in the summary
        sum.pathJustRoot = true;
    }
}

/**
 * Prepare for a SIFS library function call
 * @param volumename The name of the volume
 * @param openflags The mode in which to open the file
 * @param pathname The original inputted path (within the volume)
 * @param lastPartNew True when the last part does not exist yet
 * @param lastIsFile True when the last part should be a file
 * @return SIFS_FAIL or SIFS_SUCCESS
 */
int startCall(const char* volumename, const char* openflags,
        const char* pathname, bool lastPartNew, bool lastIsFile)
{
    // If either string is NULL
    if (volumename == NULL || pathname == NULL) {

        // Set error to "invalid argument"
        SIFS_errno = SIFS_EINVAL;

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Handle slashes in paths
    handleSlashes(pathname);

    // Save whether last part can be 'new' (non existing)
    // in the summary
    sum.lastPartNew = lastPartNew;

    // Save the type expected for the last part
    sum.lastIsFile = lastIsFile;

    // Try opening the volume in the desired mode
    sum.volfile = fopen(volumename, openflags);

    // If opening failed
    if (sum.volfile == NULL) {

        // Set error to "no such volume"
        SIFS_errno = SIFS_ENOVOL;

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Read in header
    SIFS_VOLUME_HEADER header;
    fread(&header, sizeof header, 1, sum.volfile);

    // Save header information
    sum.bsize = header.blocksize;
    sum.bnum = header.nblocks;
    sum.headerSize = ftell(sum.volfile);

    // Read in bitmap
    int size = sizeof(char) * sum.bnum;
    sum.bitmap = malloc(size);
    fread(sum.bitmap, size, 1, sum.volfile);

    // Record where the header and bitmap end
    sum.bstart = ftell(sum.volfile);

    // Record bitmap size
    sum.bitmapSize = sum.bstart - sum.headerSize;

    // If the volume is not valid
    if (isVolValid() == SIFS_FAIL) {

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // If the path cannot be broken down,
    // or has parts that cannot be linked to any block ID
    if (getPathParts(pathname) == SIFS_FAIL
            || getPartBlockIDs() == SIFS_FAIL) {

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Save information of last path component
    strcpy(sum.lastPart.word, pathParts[partNo - 1]);
    sum.lastPart.blockID = pathPartbIDs[partNo - 1];

    // If an invalid value was given to the last part
    if (sum.lastPart.blockID == -1) {

        // Then there are no more unused blocks left,
        // so set error to "No space on volume"
        SIFS_errno = SIFS_ENOSPC;

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // If the last part has a parent,
    // save its information too
    if (!sum.pathJustRoot) {
        strcpy(sum.lastParentPart.word, pathParts[partNo - 2]);
        sum.lastParentPart.blockID = pathPartbIDs[partNo - 2];
    }

    // Check that desired type is correct
    if (isDesiredType() == SIFS_FAIL) {

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Otherwise, finish successfully
    return SIFS_SUCCESS;
}

//  ----------------------------------------------------------------------
//  FINISH CALL
//  ----------------------------------------------------------------------

/*
 * End a successful library call
 */
int finishCall(void)
{
    // Free the path components
    free(pathParts);

    // Free the bitmap
    free(sum.bitmap);

    // Close the file
    fclose(sum.volfile);

    // Finish successfully
    return SIFS_SUCCESS;
}






