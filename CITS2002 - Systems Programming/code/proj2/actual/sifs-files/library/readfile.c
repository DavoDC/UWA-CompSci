#include "sifs-internal.h"
#include "helper.h"

/*
 * Read the contents of an existing file from an existing volume
 */
int SIFS_readfile(const char *volumename, const char *pathname,
        void **data, size_t *nbytes)
{
    // Start the call
    if (startCall(volumename, "rb", pathname, false, true) == SIFS_FAIL) {
        return SIFS_FAIL;
    }

    // Get the file block
    SIFS_FILEBLOCK fileB = getFileBlock(sum.lastPart.blockID);

    // Get blockID of last data block
    int endBlockID = fileB.firstblockID + getBlocksReq(fileB.length);

    // Assign info
    *nbytes = fileB.length;

    // Counts of data done and left
    size_t dataRead = 0;
    size_t dataLeft = fileB.length;

    // Allocate memory to data 
    *data = malloc(fileB.length + 1);

    // Add data
    // For all data blocks of the file
    for (int b = fileB.firstblockID; b < endBlockID; b++) {

        // Seek to the block
        seekToBID(b);

        // If there is enough data to read in a full block
        if (dataLeft >= sum.bsize) {

            // Read in data
            fread(*data, sum.bsize, 1, sum.volfile);

            // Update counts
            dataRead += sum.bsize;
            dataLeft -= sum.bsize;
        } else {

            // Else, just write how much is left
            fread(*data, dataLeft, 1, sum.volfile);

            // Update counts
            dataRead += dataLeft;
            dataLeft = 0;
        }
    }

    // Finish successfully
    return finishCall();
}
