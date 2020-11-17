#include "sifs-internal.h"
#include "helper.h"

/*
 * Get information about a requested file
 */
int SIFS_fileinfo(const char *volumename, const char *pathname,
        size_t *length, time_t *modtime)
{
    // Start the call
    if (startCall(volumename, "rb", pathname, false, true) == SIFS_FAIL) {
        return SIFS_FAIL;
    }

    // Get the file block ID of the last path component
    int filebID = sum.lastPart.blockID;

    // Get the file block
    SIFS_FILEBLOCK fileB = getFileBlock(filebID);

    // Assign info
    *length = fileB.length;
    *modtime = fileB.modtime;

    // Finish successfully
    return finishCall();
}
