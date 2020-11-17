#include "sifs-internal.h"
#include "helper.h"

/*
 * Remove an existing directory from an existing volume
 */
int SIFS_rmdir(const char* volumename, const char* pathname)
{
    // Start the call
    if (startCall(volumename, "rb+", pathname, false, false) == SIFS_FAIL) {
        return SIFS_FAIL;
    }

    // If directory is just the root
    if (sum.pathJustRoot) {

        // We cannot remove it
        // Set error to "invalid argument"
        SIFS_errno = SIFS_EINVAL;

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Get the directory to be removed
    SIFS_DIRBLOCK dir = getDirBlock(sum.lastPart.blockID);

    // If directory is not empty
    if (dir.nentries != 0) {

        // Set error to "directory is not empty"
        SIFS_errno = SIFS_ENOTEMPTY;

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Update the bitmap = Mark the directory block as unused
    updateBitmap(sum.lastPart.blockID, SIFS_UNUSED);

    // Reset the block's data
    clearBlock(sum.lastPart.blockID);

    // Update the parent directory by 
    // removing the entry for the directory
    removeLastFromParent();

    // Finish successfully
    return finishCall();
}