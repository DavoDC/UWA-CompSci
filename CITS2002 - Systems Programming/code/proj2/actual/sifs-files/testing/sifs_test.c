#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <stdbool.h>
#include "../sifs.h"
#include "../library/sifs-internal.h"
#include "../library/helper.h"

// The location of volumes
#define volLoc "testing/vols/copies/vol"  

// The location of the sample files
#define fileLoc "testing/sample-files/" 

// String for perror
#define pErrorStr "\n Library Error"

// Number of testing functions
#define FUNC_NO 9

// Usage string pairs
#define comParams "<volchar> <pathname>"
char usageStr[][2][300] = {
    {"mkvol", "<volchar> <blocksize> <numblocks>"},
    {"dinfo", comParams},
    {"mkdir", comParams},
    {"rmdir", comParams},
    {"finfo", comParams},
    {"refil", comParams},
    {"rmfil", comParams},
    {"wrfil", "<volchar> <pathname> <filename>"},
    {"dfrag", "<volchar>"},
};

// Common parameter holders
//  The filename storing the SIFS volume
char* volumename;
//  The path to the directory
char* pathname;

// Switch for volume examination
bool doVolExam = false;

//  ----------------------------------------------------------------------
//  HELPER FUNCTIONS
//  ----------------------------------------------------------------------

/*
 * A Java-like proxy for strcmp()
 */
bool strEquals(char s1[], char s2[])
{
    // If no difference
    if (strcmp(s1, s2) == 0) {

        // Return true
        return true;
    }

    // Else, return false
    return false;
}

/*
 * Get the position of a 5-letter function string
 * Return -1 if it is not found
 */
int getFuncPos(char query[])
{
    // Holder
    int pos = -1;

    // For all functions
    for (int i = 0; i < FUNC_NO; i++) {

        // If query matches current function
        if (strEquals(query, usageStr[i][0])) {

            // Update position
            pos = i;

            // Stop searching
            break;
        }
    }

    // If nothing found, notify and exit
    if (pos == -1) {
        fprintf(stderr, "\n\n Your function string (%s)", query);
        fprintf(stderr, "\n is not linked to any library function \n\n");
        exit(EXIT_FAILURE);
    }

    // Return pos found
    return pos;
}

/*
 * Notify the user about how to use the program correctly
 */
void printUsage(char query[])
{
    // Header
    printf("\n\n Incorrect Usage");

    // Range of usage strings to print
    int start;
    int end;

    // If query is "all"
    if (strEquals(query, "all")) {

        // Do general 
        printf("\n\n Please see all possibilities below:");

        // Set range to full
        start = 0;
        end = FUNC_NO;

    } else {

        // Else if it is not "all",
        // Set range of usage strings to the one,specified function
        //  Get function position (and exit if invalid)
        start = getFuncPos(query);
        //  End is just pos+1
        end = start + 1;

        // Print valid function header
        printf(" of %s", query);
        printf("\n\n The correct way to use %s:", query);
    }

    // Print range of usage strings
    for (int i = start; i < end; i++) {
        printf("\n  ./testing/sifs_test ");
        printf("%s %s", usageStr[i][0], usageStr[i][1]);
    }

    // Space
    printf("\n");
}

/*
 * Check parameters and retrieve if valid
 * If invalid, notify and exit
 */
void processParams(int argNo, char** argval)
{


    // Check arguments
    bool defragReq = strEquals(argval[1], "dfrag") && argNo == 3;
    bool lengthGood = argNo >= 4;
    bool funcGood = (getFuncPos(argval[1]) != -1);

    // If arguments are fully good
    if ((lengthGood && funcGood) || defragReq) {

        // Allocate memory to volume path
        volumename = malloc(strlen(volLoc) + strlen(argval[2]) + 5);

        // Save volume with start
        sprintf(volumename, "%s%s%s", volLoc, argval[2], "cp");

        // Save pathname
        if (!defragReq) {
            pathname = argval[3];
        }
    } else if (!lengthGood && funcGood) {

        // If function is okay but length is bad,
        // print information about attempted function
        printUsage(argval[1]);

        // Exit
        exit(EXIT_FAILURE);

    } else if (!lengthGood && !funcGood) {

        // If both are bad, print full information
        printUsage("all");

        // Exit
        exit(EXIT_FAILURE);
    }
}

//  ----------------------------------------------------------------------
//  MAKE VOLUME TEST
//  ----------------------------------------------------------------------

/*
 * Test 'make volume' function
 */
void mkvol_test(int argNo, char** argval)
{
    // Local variables 
    size_t blocksize;
    uint32_t nblocks;

    // Check additional args
    // If they are good
    if (argNo == 5) {

        // Save them
        blocksize = atoi(argval[3]);
        nblocks = atoi(argval[4]);

    } else {

        // Exit as they are incorrect
        printUsage("mkvol");
        exit(EXIT_FAILURE);
    }

    // Call info
    char callInfo[1000];
    sprintf(callInfo, "\n SIFS_mkvolume(%s, %zi, %u) \n", volumename, blocksize, nblocks);

    // Run library function
    if (SIFS_mkvolume(volumename, blocksize, nblocks) == SIFS_FAIL) {
        strcat(callInfo, pErrorStr);
        SIFS_perror(callInfo);
        exit(EXIT_FAILURE);
    }

    // Print info about call
    printf(callInfo);

    // Exit successfully
    exit(EXIT_SUCCESS);
}

//  ----------------------------------------------------------------------
//  DIRECTORY FUNCTION TESTS
//  ----------------------------------------------------------------------

void printEntry(char** entrynames, int pos)
{
    // Get entry
    char curEnt[300];
    sprintf(curEnt, "%s", entrynames[pos]);

    // Print start 
    printf("\n   entries[%-2i] = ", pos);

    // If not unused
    if (strlen(curEnt) != 0) {
        printf("{%s}", curEnt);
    } else {
        printf("{unused}");
    }

}

/*
 * Test dirinfo function
 */
void dirinfo_test()
{
    // Modify args
    if (strEquals(pathname, "root")) {
        strcpy(pathname, "/");
    }

    // VARIABLES TO STORE THE INFORMATION RETURNED FROM SIFS_dirinfo()
    char** entrynames;
    uint32_t nentries = 0;
    time_t modtime = 0;

    // Call info
    char callInfo[1000];
    sprintf(callInfo, "\n sifs_dirinfo(%s, %s, ..): ", volumename, pathname);

    // PASS THE ADDRESS OF OUR VARIABLES SO THAT SIFS_dirinfo() MAY MODIFY THEM
    if (SIFS_dirinfo(volumename, pathname, &entrynames, &nentries, &modtime) == SIFS_FAIL) {
        strcat(callInfo, pErrorStr);
        SIFS_perror(callInfo);
        exit(EXIT_FAILURE);
    }

    // Print info about call
    printf(callInfo);

    // Print single value info
    printf("\n  modified: %s", ctime(&modtime));
    printf("  number of entries: %u", nentries);

    // Print out list of entries
    printf("\n  entry names: ");
    // Print valid ones
    for (int e = 0; e < nentries + 1; e++) {
        printEntry(entrynames, e);
    }

    // Print last entry
    printf("\n   ...");
    printEntry(entrynames, SIFS_MAX_ENTRIES - 1);
    printf("\n");

    // If array has been allocated to
    if (entrynames != NULL) {

        // Free cells
        for (int e = 0; e < SIFS_MAX_ENTRIES; ++e) {
            free(entrynames[e]);
        }

        // Free array
        free(entrynames);
    }
}

/*
 * Test 'make directory' function
 */
void mkdir_test()
{
    // Call info
    char callInfo[1000];
    sprintf(callInfo, "\n sifs_mkdir(%s, %s): ", volumename, pathname);

    // Run library function
    if (SIFS_mkdir(volumename, pathname) == SIFS_FAIL) {
        strcat(callInfo, pErrorStr);
        SIFS_perror(callInfo);
        exit(EXIT_FAILURE);
    }

    // Results
    printf(callInfo);
    printf("\n");
}

/*
 * Test 'remove directory' function
 */
void rmdir_test()
{
    // Call info
    char callInfo[1000];
    sprintf(callInfo, "\n sifs_rmdir(%s, %s): ", volumename, pathname);

    // Run library function
    if (SIFS_rmdir(volumename, pathname) == SIFS_FAIL) {
        strcat(callInfo, pErrorStr);
        SIFS_perror(callInfo);
        exit(EXIT_FAILURE);
    }

    // Print info about call
    printf(callInfo);

    // Exit successfully
    exit(EXIT_SUCCESS);
}

//  ----------------------------------------------------------------------
//  FILE FUNCTION TESTS
//  ----------------------------------------------------------------------

/*
 * Test 'file info' function
 */
void fileinfo_test()
{
    // Variables to modify
    size_t length = 0;
    time_t modtime = 0;

    // Call info
    char callInfo[1000];
    sprintf(callInfo, "\n sifs_fileinfo(%s, %s, ..): ", volumename, pathname);

    // Run library function
    if (SIFS_fileinfo(volumename, pathname, &length, &modtime) == SIFS_FAIL) {
        strcat(callInfo, pErrorStr);
        SIFS_perror(callInfo);
        exit(EXIT_FAILURE);
    }

    // Print info about call
    printf(callInfo);

    // Results
    printf("\n  length:   %zi", length);
    printf("\n  modified: %s", ctime(&modtime));

    // Exit successfully
    exit(EXIT_SUCCESS);
}

/*
 * Test 'remove file' function
 */
void rmfile_test()
{
    // Call info
    char callInfo[1000];
    sprintf(callInfo, "\n sifs_rmfil(%s, %s): ", volumename, pathname);

    // Run library function
    if (SIFS_rmfile(volumename, pathname) == SIFS_FAIL) {
        strcat(callInfo, pErrorStr);
        SIFS_perror(callInfo);
        exit(EXIT_FAILURE);
    }

    // Print info about call
    printf(callInfo);

    // Exit successfully
    exit(EXIT_SUCCESS);
}

/*
 * Print a character of a data
 */
void printDataChar(int pos, void* data)
{
    // Get character
    char curChar = ((char*) data)[pos];

    // Print non-empty chars
    if (curChar != '\0' && curChar != '\n') {
        printf("  %i-%c", pos, curChar);
    }

    // Spaces
    if (pos != 0 && pos % 12 == 0) {
        printf("\n");
    }
}

/*
 * Print a sample of some data
 */
void printSampleBytes(void* data, size_t nbytes)
{
    // Too small case
    if (nbytes < 250) {
        return;
    }

    printf("\n  Sample bytes:");
    printf("\n  (in the form: <bytepos>-<character>)");
    printf("\n  (skips null bytes and new line characters)");

    printf("\n\n  First 100 bytes: \n");
    for (int i = 0; i < 100; i++) {
        printDataChar(i, data);
    }

    printf("\n\n  Last 100 bytes: \n");
    for (int i = nbytes - 100; i < nbytes; i++) {
        printDataChar(i, data);
    }
}

/*
 * Test 'read file' function
 */
void readfile_test()
{
    // Variables to modify
    void* data;
    size_t nbytes = 0;

    // Call info
    char callInfo[1000];
    sprintf(callInfo, "\n sifs_readfile(%s, %s, ..): ", volumename, pathname);

    // Run library function
    if (SIFS_readfile(volumename, pathname, &data, &nbytes) == SIFS_FAIL) {
        strcat(callInfo, pErrorStr);
        SIFS_perror(callInfo);
        exit(EXIT_FAILURE);
    }

    // Print info about call
    printf(callInfo);

    // Results
    printf("\n  length: %zi", nbytes);
    printSampleBytes(data, nbytes);

    // Exit successfully
    exit(EXIT_SUCCESS);
}

/*
 * Convert a file into a data array
 */
void getFileData(char filepath[], void** data, size_t* nbytes)
{
    // Open file 
    FILE* filep = fopen(filepath, "rb");

    // Exit if couldn't load
    if (filep == NULL) {
        printf("\n\n File could not be loaded");
        printf("\n Elusive file path was: %s", filepath);
        printf("\n\n");
        exit(EXIT_FAILURE);
    }

    // Get file length
    fseek(filep, 0, SEEK_END);
    *nbytes = ftell(filep);
    fseek(filep, 0, SEEK_SET);

    // Initialize data
    *data = malloc(sizeof(char) * *nbytes + 1);

    // Add data to buffer;
    fread(*data, *nbytes, 1, filep);

    // Close file
    fclose(filep);
}

/*
 * Test 'write file' function
 */
void writefile_test(int argNo, char** argval)
{
    // Input variables
    void* data = 0;
    size_t nbytes = 0;
    char fullFilePath[1000];

    // Check additional args
    // If they are good
    if (argNo == 5) {

        // Save with start
        sprintf(fullFilePath, "%s%s", fileLoc, argval[4]);

    } else {

        // Exit as they are incorrect
        printUsage("wrfil");
        exit(EXIT_FAILURE);
    }

    // Get data
    getFileData(fullFilePath, &data, &nbytes);

    // Notify about data 
    printf("\n Opened file: %s", fullFilePath);
    printf("\n Got %zi bytes from it, for input into writefile", nbytes);
    printSampleBytes(data, nbytes);
    printf("\n");

    // Make volfilepath
    char volfpath[400];
    sprintf(volfpath, "%s/%s", pathname, argval[4]);

    // Call info
    char callInfo[1000];
    sprintf(callInfo, "\n sifs_writefile(%s, %s, ..): ", volumename, volfpath);

    // Run library function
    if (SIFS_writefile(volumename, volfpath, data, nbytes) == SIFS_FAIL) {
        strcat(callInfo, pErrorStr);
        SIFS_perror(callInfo);
        exit(EXIT_FAILURE);
    }

    // Print info about call
    printf(callInfo);

    // Print space
    printf("\n");

    // Exit successfully
    exit(EXIT_SUCCESS);
}

/*
 * Test 'defrag' function
 */
void defrag_test(void)
{
    // Call info
    char callInfo[1000];
    sprintf(callInfo, "\n sifs_defrag(%s): \n", volumename);

    // Run library function
    if (SIFS_defrag(volumename) == SIFS_FAIL) {
        strcat(callInfo, pErrorStr);
        SIFS_perror(callInfo);
        exit(EXIT_FAILURE);
    }

    // Print info about call
    printf(callInfo);

    // Exit successfully
    exit(EXIT_SUCCESS);
}

//  ----------------------------------------------------------------------
//  ARGUMENT HANDLING
//  ----------------------------------------------------------------------

/*
 * Run one of the testing functions based on the arguments
 */
void runDesired(int argNo, char** argval)
{
    // Check volume examination switch
    if (strEquals("-v", argval[argNo - 1])) {

        // Activate volume examination
        doVolExam = true;

        // Correct argNo
        argNo--;
    }

    // Process parameters
    processParams(argNo, argval);

    // Run functions
    if (strEquals(argval[1], "mkvol")) mkvol_test(argNo, argval);
    else if (strEquals(argval[1], "dinfo")) dirinfo_test();
    else if (strEquals(argval[1], "rmdir")) rmdir_test();
    else if (strEquals(argval[1], "mkdir")) mkdir_test();
    else if (strEquals(argval[1], "finfo")) fileinfo_test();
    else if (strEquals(argval[1], "refil")) readfile_test();
    else if (strEquals(argval[1], "rmfil")) rmfile_test();
    else if (strEquals(argval[1], "wrfil")) writefile_test(argNo, argval);
    else if (strEquals(argval[1], "dfrag")) defrag_test();
}

//  ----------------------------------------------------------------------
//  MAIN FUNCTION
//  ----------------------------------------------------------------------

int main(int argNo, char** argval)
{
    // If there are no args
    if (argNo == 1) {

        // Print all usage info
        printUsage("all");

    } else if (argNo >= 2) {

        // Else if args exist, attempt to choose a function
        runDesired(argNo, argval);
    }

    // Exit successfully
    exit(EXIT_SUCCESS);
}

