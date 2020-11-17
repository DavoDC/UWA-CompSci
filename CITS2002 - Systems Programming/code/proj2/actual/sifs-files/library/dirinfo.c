#define _GNU_SOURCE 1 // Enables strdup to be used
#include <string.h>
#include <sys/unistd.h>
#include "sifs-internal.h"
#include "helper.h"

/*
 * Get information about a requested directory
 */
int SIFS_dirinfo(const char* volumename, const char* pathname,
        char*** entrynames, uint32_t* nentries, time_t* modtime)
{
    // Start the call
    if (startCall(volumename, "rb", pathname, false, false) == SIFS_FAIL) {
        return SIFS_FAIL;
    }

    // Allocate memory to a 'holder'
    char** entriesHolder = malloc(SIFS_MAX_ENTRIES * sizeof(char*));

    // If allocation was not successful
    if (entriesHolder == NULL) {

        // Set error to "memory allocation failed"
        SIFS_errno = SIFS_ENOMEM;

        // Exit unsuccessfully
        return SIFS_FAIL;
    }

    // The blockID of the directory to retrieve
    int dirBlockID = 0;

    // If path is not just the root directory
    if (!sum.pathJustRoot) {

        // Use the last part's blockID
        dirBlockID = sum.lastPart.blockID;
    }

    // Get the directory block structure
    SIFS_DIRBLOCK dir = getDirBlock(dirBlockID);

    // Pass back information about
    // number of entries and modification time
    *nentries = dir.nentries;
    *modtime = dir.modtime;

    // Add information about individual entries to holder
    // For all entries
    for (int e = 0; e < SIFS_MAX_ENTRIES; e++) {

        // Make individual entry holder
        char entry[SIFS_MAX_NAME_LENGTH];

        // If entry is in use
        if (*nentries != 0 && e < *nentries) {

            // Add formatted string with values to entry
            sprintf(entry, "bID=%i, fIndex=%i",
                    dir.entries[e].blockID,
                    dir.entries[e].fileindex);
        } else {

            // Else, the entry must be unused
            entry[0] = '\0';
        }

        // Make the holder point to an entry duplicate,
        // and let strdup allocate memory
        entriesHolder[e] = strdup(entry);

        // If the allocation failed
        if (entriesHolder[e] == NULL) {

            // Set error to "memory allocation failed"
            SIFS_errno = SIFS_ENOMEM;

            // Exit unsuccessfully
            return SIFS_FAIL;
        }
    }

    // Pass back the information from the entries holder
    *entrynames = entriesHolder;

    // Finish successfully
    return finishCall();
}
