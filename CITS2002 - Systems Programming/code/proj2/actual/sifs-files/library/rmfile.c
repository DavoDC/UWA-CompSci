#define _GNU_SOURCE 1
#include <string.h>
#include "sifs-internal.h"
#include "helper.h"

/*
 * Remove an existing file from an existing volume
 */
int SIFS_rmfile(const char *volumename, const char *pathname)
{
    // Start the call
    if (startCall(volumename, "rb+", pathname, false, true) == SIFS_FAIL) {
        return SIFS_FAIL;
    }

    // Get the fileblock
    SIFS_FILEBLOCK fileB = getFileBlock(sum.lastPart.blockID);

    // If the file block has two or more duplicates
    if (fileB.nfiles >= 2) {

        // We only need to remove an entry from the filenames

        // Find the position of the filename to remove
        //  Holder
        int remPos = 0;
        //  For all filenames
        for (int f = 0; f < fileB.nfiles; f++) {
            // If current filename matches last part filename
            if (strcmp(fileB.filenames[f], sum.lastPart.word) == 0) {
                // Save position and stop 
                remPos = f;
                break;
            }
        }

        // Remove the filename by shifting other names over
        for (int n = remPos; n < fileB.nfiles; n++) {
            strcpy(fileB.filenames[n], fileB.filenames[n + 1]);
        }

        // Decrease the number of files
        fileB.nfiles--;

        // Write the updated fileblock to the volume
        seekToBID(sum.lastPart.blockID);
        fwrite(&fileB, sizeof fileB, 1, sum.volfile);

    } else if (fileB.nfiles == 1) {

        // Else if we are removing the last file,
        // we must delete the fileblock and datablock(s)

        // Set the fileblock as unused in the bitmap
        updateBitmap(sum.lastPart.blockID, SIFS_UNUSED);

        // Clear the fileblock data
        clearBlock(sum.lastPart.blockID);

        // Get the blockID of last data block
        int endBlockID = fileB.firstblockID + getBlocksReq(fileB.length);

        // For all data blocks of the file
        for (int b = fileB.firstblockID; b < endBlockID; b++) {

            // Set as unused
            updateBitmap(b, SIFS_UNUSED);

            // Clear the data
            clearBlock(b);
        }
    }

    // Update the parent directory by 
    // removing the entry for the file
    removeLastFromParent();

    // Finish successfully
    return finishCall();
}
