#define _GNU_SOURCE 1 // Enables strdup to be used
#include "sifs-internal.h"
#include "helper.h"

// Helper function declarations
bool isDefragged(void);
void defragBlock(SIFS_BLOCKID);
void shiftAndUpdateBM(SIFS_BLOCKID, SIFS_BLOCKID);

/*
 * Attempts to coalesce all unused blocks into a single contiguous area,
 * that starts at the end of the used blocks.
 * This will possibly enable new large files to be added to a volume that previously suffered from
 * much external-fragmentation.
 */
int SIFS_defrag(const char* volumename)
{
    // Start the call (default mode)
    if (startCall(volumename, "rb+", "/", false, false) == SIFS_FAIL) {
        return SIFS_FAIL;
    }

    // If the volume is already defragged
    if (isDefragged()) {

        // There is no need to go further
        // Finish successfully
        return SIFS_SUCCESS;
    }

    // Start the defrag process
    defragBlock(0);


    // Finish successfully
    return finishCall();
}

/*
 * Check if a volume is fragmented
 * @Return True if a defrag is needed, and false otherwise
 */
bool isDefragged(void)
{
    // Find the first unused block ID
    SIFS_BLOCKID firstUID = getNextUnusedID();

    // The largest contiguous block should be
    // the length of the rest of the volume
    long largeCB = sum.bsize * (sum.bnum - firstUID);

    // If that contiguous block exists
    SIFS_BLOCKID start;
    SIFS_BLOCKID end;
    if (getContigSeries(largeCB, &start, &end) == SIFS_SUCCESS) {

        // The volume is already defragged
        return true;
    }

    // Otherwise, the volume is fragmented
    return false;
}

/*
 * Defrag all directories in the volume
 * @param blockID The ID of the block to defrag
 */
void defragBlock(SIFS_BLOCKID parbID)
{
    // Get the block
    SIFS_DIRBLOCK parDir = getDirBlock(parbID);

    // For each child of the parent
    for (int e = 0; e < parDir.nentries; e++) {

        // Get the current child block ID
        SIFS_BLOCKID curChildbID = parDir.entries[e].blockID;

        // Seek to the child block
        seekToBID(curChildbID);

        // Find the next unused block ID
        SIFS_BLOCKID nextUbID = getNextUnusedID();

        // If the child block is a directory block
        if (sum.bitmap[curChildbID] == SIFS_DIR) {

            // If the next unused block is less than the current block
            if (nextUbID < curChildbID) {

                // The block should be moved (leftward) to the unused ID

                // Update the child entry of the parent to its new block ID
                parDir.entries[e].blockID = nextUbID;

                // Write the updated parent directory to volume
                seekToBID(parbID);
                fwrite(&parDir, sizeof parDir, 1, sum.volfile);

                // Shift the actual block and update the bitmap
                shiftAndUpdateBM(nextUbID, curChildbID);

                // Defrag the child block
                defragBlock(nextUbID);
            }
        }
    }
}

/**
 * Shift a block from one spot to another
 * Update the bitmap to reflect this change
 * @param dest The destination block ID
 * @param src The source block ID
 */
void shiftAndUpdateBM(SIFS_BLOCKID dest, SIFS_BLOCKID src)
{
    // If the destination is holding something else
    if (sum.bitmap[dest] != SIFS_UNUSED) {

        // Do not process further
        return;
    }

    // Make the destination into the source in the bitmap
    updateBitmap(dest, sum.bitmap[src]);

    // Make the source unused in the bitmap
    updateBitmap(src, SIFS_UNUSED);

    // Make a block holder
    char block[sum.bsize];
    memset(block, 0, sizeof block);

    // Read in the source block
    seekToBID(src);
    fread(block, sum.bsize, 1, sum.volfile);

    // Clear the source block
    clearBlock(src);

    // Write the holder to the destination
    seekToBID(dest);
    fwrite(block, sum.bsize, 1, sum.volfile);
}




