
#ifndef _HELPER_H
#define _HELPER_H

//  ----------------------------------------------------------------------
//  HEADER FILE FOR HELPER FUNCTIONS
//  ----------------------------------------------------------------------


//  ----------------------------------------------------------------------
//  GLOBAL TYPES
//  ----------------------------------------------------------------------

/*
 * Models a component of the user inputted pathname
 */
typedef struct _part {
    // The part's string value
    char word[SIFS_MAX_NAME_LENGTH];

    // The blockID the part is linked to
    int blockID;
} PART;

/*
 * A summary of information relating to a call on a volume
 */
typedef struct _summary {
    // A pointer to the volume file
    FILE* volfile;

    // The block size and number of blocks
    size_t bsize;
    uint32_t bnum;

    // The size of the header and bitmap
    long headerSize;
    long bitmapSize;

    // Where the blocks start (after the bitmap and header)
    long bstart;

    // The bitmap
    SIFS_BIT* bitmap;

    // Whether the original path is just the root
    bool pathJustRoot;

    // Whether the last part is 'new' (doesn't exist yet)
    bool lastPartNew;
    
    // The expected type of the last part (file or directory)
    bool lastIsFile;

    // The last part and it's parent
    PART lastPart;
    PART lastParentPart;

} SUMMARY;


//  ----------------------------------------------------------------------
//  GLOBAL VARIABLES
//  ----------------------------------------------------------------------

// An instance of a SUMMARY
extern SUMMARY sum;

//  ----------------------------------------------------------------------
//  GLOBAL FUNCTIONS
//  ----------------------------------------------------------------------

// Prepare for a library call
extern int startCall(const char*, const char*,
        const char*, bool, bool);

// Get a directory block
extern SIFS_DIRBLOCK getDirBlock(SIFS_BLOCKID);

// Get a file block
extern SIFS_FILEBLOCK getFileBlock(SIFS_BLOCKID);

// Seek file to a blockID
extern void seekToBID(SIFS_BLOCKID);

// Find the next unused block ID
extern SIFS_BLOCKID getNextUnusedID();

// Find a contiguous block
extern int getContigSeries(long, SIFS_BLOCKID*, SIFS_BLOCKID*);

// Get the number of the blocks required for a given file length
extern int getBlocksReq(long);

// Update bitmap
extern void updateBitmap(SIFS_BLOCKID, SIFS_BIT);

// Clear a block
extern void clearBlock(SIFS_BLOCKID);

// Get directory entries
void getDirEntries(SIFS_DIRBLOCK, SIFS_BLOCKID[][2]);

// Remove references to the last entry
extern void removeLastFromParent();

// Free a array of strings
extern void freeStrArray(char**, int);

// Finish a call successfully
extern int finishCall(void);


#endif
