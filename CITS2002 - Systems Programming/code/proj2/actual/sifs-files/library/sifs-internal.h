
// Common C library headers
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>

// Common SIFS library header files
#include "../sifs.h"
#include "md5.h"


// CONCRETE STRUCTURES AND CONSTANTS USED THROUGHOUT THE SIFS LIBRARY.
// DO NOT CHANGE
#define SIFS_MIN_BLOCKSIZE 1024

#define SIFS_MAX_NAME_LENGTH 32 // including the NULL byte
#define SIFS_MAX_ENTRIES 24 // for both directory and file entries

#define SIFS_UNUSED  'u'
#define SIFS_DIR  'd'
#define SIFS_FILE 'f'
#define SIFS_DATABLOCK  'b'

#define SIFS_ROOTDIR_BLOCKID 0



// DEFINTION OF A VOLUME

typedef struct {
    // The second integer stores the size, in bytes, of each block
    // Typically 1KB or 4KB.
    size_t blocksize;

    // The number of blocks in the volume that may be used 
    // to store directory information, file information, or the contents of files
    uint32_t nblocks;

} SIFS_VOLUME_HEADER;


// The volume bitmap - records how each block on the volume is being used. 
// The name volume bitmap does not mean that each element occupies 
// only a single bit (this name is only historic).
// A single 1-byte character is used to record each block's use, 
// represented as one of four distinct values.
// i.e. SIFS_UNUSED, SIFS_DIR, ...
typedef char SIFS_BIT;

typedef uint32_t SIFS_BLOCKID;



// DEFINITION OF EACH DIRECTORY BLOCK - MUST FIT INSIDE A SINGLE BLOCK
// Directory blocks have a fixed format

typedef struct {
    // The name of the directory
    char name[SIFS_MAX_NAME_LENGTH];

    // The directory block's last modification time
    // time last modified <- time()
    time_t modtime;

    // The number of entries in the directory (either subdirectories or files)
    uint32_t nentries;

    // A number of entries

    struct {
        // The block number (blockID) of each subdirectory or file.
        SIFS_BLOCKID blockID;

        // The file index into a SIFS_FILEBLOCK's filenames[]
        uint32_t fileindex;

    } entries[SIFS_MAX_ENTRIES];


} SIFS_DIRBLOCK;



// DEFINITION OF EACH FILE BLOCK - MUST FIT INSIDE A SINGLE BLOCK
// File-blocks have a fixed format.

typedef struct {
    // The time the first file was added (<- time())
    time_t modtime;

    // The common length (in bytes) of every file represented by that file-block
    size_t length;

    // Stores the MD5 cryptographic digest (a summary) of the files' contents
    unsigned char md5[MD5_BYTELEN];

    // The block number (blockID) of the files' first data-block
    SIFS_BLOCKID firstblockID;

    // The number of files having the same/identical contents
    uint32_t nfiles;

    // An array of each file's name 
    char filenames[SIFS_MAX_ENTRIES][SIFS_MAX_NAME_LENGTH];

} SIFS_FILEBLOCK;

