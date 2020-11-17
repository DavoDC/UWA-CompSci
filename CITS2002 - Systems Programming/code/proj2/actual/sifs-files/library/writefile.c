#include "sifs-internal.h"
#include "helper.h"

/*
 * Add a copy of a new file to an existing volume
 */
int SIFS_writefile(const char *volumename, const char *pathname,
        void* data, size_t nbytes)
{
    // Start the call
    if (startCall(volumename, "rb+", pathname, true, false) == SIFS_FAIL) {
        return SIFS_FAIL;
    }

    // If user is trying to turn root into fileblock,
    // or file contains no data
    if (sum.pathJustRoot) {
        // Set error to 'invalid argument'
        SIFS_errno = SIFS_EINVAL;

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Get parent directory block
    SIFS_DIRBLOCK parDir = getDirBlock(sum.lastParentPart.blockID);

    // If the parent can hold no more entries
    if (parDir.nentries == SIFS_MAX_ENTRIES) {

        // Set error to 'too many entries'
        SIFS_errno = SIFS_EMAXENTRY;

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Get the MD5 digest of the input data
    unsigned char newMD5[MD5_BYTELEN];
    MD5_buffer(data, nbytes, newMD5);


    // Determine if file is unique or not
    SIFS_BLOCKID sameMD5BlockID = -1;
    // For all blocks
    for (int b = 0; b < sum.bnum; b++) {

        // If it is a file block
        if (sum.bitmap[b] == SIFS_FILE) {

            // Get the file block
            SIFS_FILEBLOCK curFB = getFileBlock(b);

            // If the current MD5 matches the input
            if (memcmp(newMD5, curFB.md5, MD5_BYTELEN) == 0) {

                // Save location
                sameMD5BlockID = b;
            }
        }
    }

    // If MD5 was different to all others
    if (sameMD5BlockID == -1) {

        // We need to make a new fileblock and datablock set

        // Update the bitmap for where the fileblock will be
        updateBitmap(sum.lastPart.blockID, SIFS_FILE);

        // The starting and ending bIDs of a contiguous
        // series of blocks that can hold the file
        SIFS_BLOCKID first = 0;
        SIFS_BLOCKID last = 0;

        // Get the contiguous block range
        if (getContigSeries(nbytes, &first, &last) == SIFS_FAIL) {
            return SIFS_FAIL;
        }

        // Write data blocks to volume
        //  Holds much data for writing is done/left
        size_t dataDone = 0;
        size_t dataLeft = nbytes;
        //  For all blocks in the range of the contiguous block found
        for (int b = first; b <= last; b++) {
            
            // If length is 0
            if(nbytes == 0)
            {
                // Do not continue further
                break;
            }

            // Seek to the block
            seekToBID(b);

            // If there is enough data to write a full block
            if (dataLeft >= sum.bsize) {
                // Write a full block
                fwrite(data, sum.bsize, 1, sum.volfile);
                dataDone += sum.bsize;
                dataLeft -= sum.bsize;

                // Move pointer forward
                data = ((char*) data + sum.bsize);

            } else {
                // Else, just write how much is left
                fwrite(data, dataLeft, 1, sum.volfile);
                dataDone += dataLeft;
                dataLeft = 0;

                // Move pointer forward
                data = ((char*) data + dataLeft);
            }

            // Update the bitmap
            updateBitmap(b, SIFS_DATABLOCK);
        }

        // Create the file block
        SIFS_FILEBLOCK newFB;
        strcpy(newFB.filenames[0], sum.lastPart.word);
        newFB.firstblockID = first;
        newFB.length = nbytes;
        memcpy(newFB.md5, newMD5, MD5_BYTELEN);
        newFB.modtime = time(NULL);
        newFB.nfiles = 1;

        // Write the fileblock to the volume
        seekToBID(sum.lastPart.blockID);
        fwrite(&newFB, sizeof newFB, 1, sum.volfile);

        // Update the parent with the last part's block ID
        //  Add new fileblock entry
        parDir.entries[parDir.nentries].blockID = sum.lastPart.blockID;
        parDir.entries[parDir.nentries].fileindex = 0;

    } else {

        // Else, there was a fileblock with the same MD5 digest
        // The filename should be added to the existing file block,
        // and the file block should be linked to the parent

        // Get the fileblock already holding the file
        SIFS_FILEBLOCK exFB = getFileBlock(sameMD5BlockID);

        // Add an entry to the parent
        parDir.entries[parDir.nentries].blockID = sameMD5BlockID;
        parDir.entries[parDir.nentries].fileindex = exFB.nfiles;

        // Update the file block (no modtime change)
        strcpy(exFB.filenames[exFB.nfiles], sum.lastPart.word);
        exFB.nfiles++;

        // Write the fileblock back to the volume
        seekToBID(sameMD5BlockID);
        fwrite(&exFB, sizeof exFB, 1, sum.volfile);
    }

    // Finish updating the parent
    //  Increase entry count
    parDir.nentries++;
    //  Update modification time
    parDir.modtime = time(NULL);
    // Write the updated parent back to the volume
    seekToBID(sum.lastParentPart.blockID);
    fwrite(&parDir, sizeof parDir, 1, sum.volfile);

    // Finish successfully
    return finishCall();
}
