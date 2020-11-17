#define _GNU_SOURCE 1 
#include <string.h>
#include "sifs-internal.h"
#include "helper.h"

/*
 * Make a new directory within an existing volume
 */
int SIFS_mkdir(const char* volumename, const char* pathname)
{
    // Start the call
    if (startCall(volumename, "rb+", pathname, true, false) == SIFS_FAIL) {
        return SIFS_FAIL;
    }

    // If user is trying to overwrite/create root
    if (sum.pathJustRoot) {

        // Set error to "already exists"
        SIFS_errno = SIFS_EEXIST;

        // Finish unsuccessfully
        return SIFS_FAIL;
    }

    // Make a new directory block structure
    SIFS_DIRBLOCK newDir;
    for (int e = 0; e < SIFS_MAX_ENTRIES; e++) {
        newDir.entries[e].blockID = (SIFS_BLOCKID) 0;
        newDir.entries[e].fileindex = (uint32_t) 0;
    }
    newDir.modtime = time(NULL);
    strcpy(newDir.name, sum.lastPart.word);
    newDir.nentries = 0;

    // Get the fresh block ID given to the last part,
    // and write the new directory into the block into it
    SIFS_BLOCKID newbID = sum.lastPart.blockID;
    seekToBID(newbID);
    fwrite(&newDir, sizeof newDir, 1, sum.volfile);
    
    // Update the bitmap
    updateBitmap(newbID, SIFS_DIR);

    // Get the parent directory of the new directory
    int parbID = sum.lastParentPart.blockID;
    SIFS_DIRBLOCK parent = getDirBlock(parbID);

    // Edit the parent directory
    parent.nentries++;
    //  If the number of entries has exceeded maximum
    if (parent.nentries > SIFS_MAX_ENTRIES) {

        // Set error to "too many entries"
        SIFS_errno = SIFS_EMAXENTRY;

        // Finish unsuccessfully
        return SIFS_FAIL;
    }
    //  Add new entry
    parent.entries[parent.nentries - 1].blockID = newbID;

    //  Update modification time
    parent.modtime = time(NULL);

    // Write the updated parent directory to volume
    seekToBID(parbID);
    fwrite(&parent, sizeof parent, 1, sum.volfile);

    // Finish successfully
    return finishCall();
}
