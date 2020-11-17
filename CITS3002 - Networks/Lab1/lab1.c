#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdint.h>
#include <unistd.h>
#include <inttypes.h>
#include <stdbool.h>
#include <sys/param.h>
#include <time.h>
#include <sys/time.h>

#include "helper.h"

// Declarations
void question1();
void question2();

/**
 * Main method
 * @param argc
 * @param argv
 * @return 
 */
int main(int argc, char** argv) {

    // Start msg
    printf("\nRunning... \n");

    // Starting time
    int64_t start = microseconds();

    // Run function
    //question1();
    question2();

    // Print time taken
    printf("\n\nTime taken: %" PRId64 "usecs\n", microseconds() - start);

    // End
    printf("\n\n");

    // Finish
    return (EXIT_SUCCESS);
}

/**
 * Write a short program to read in the 100 integers and display them to the screen.
 * 
 * What do you observe and why?
 * > The numbers should go from -50 to 50 but display as huge values.
 * > This is because Intel uses little Endian byte ordering for integers,
 * > and SPARC uses big Endian byte ordering for intgers.
 * 
 * Does the Linux or macOS program named od offer any help (run man od)?
 * > "od -i Q1.datafile" displays it like Intel
 * > "od -i --endian=big Q1.datafile" displays it like 
 * 
 * What consequences do your observations have for computer networking?
 * > That computers running different architectures have to consider such things 
 * > as having a different "Endian" - the ordering of bytes
 * 
 * @return 
 */
void question1(void) {

    // USING MY OWN DATAFILE
    //    int  fd2 = open("MYDATA.datafile", O_CREAT|O_WRONLY, 0600);
    //
    //    if(fd2 >= 0) {        //  IFF FILE OPENED SUCCESSFULLY
    //        for(int32_t i = -50 ; i<50 ; i++) {
    //            write(fd2, &i, sizeof(i));
    //        }
    //        close(fd2);
    //    }
    //int fd = open("MYDATA.datafile", O_RDONLY, 0600);

    // Open data file
    int fd = open("Q1.datafile", O_RDONLY, 0600);


    // If file descriptor was negative/invalid
    if (fd < 0) {

        // Notify
        printf("\n File could not be opened \n");
    } else {
        // Else, notify
        printf("\nFile was opened successfully! \n");

        // For all integer
        for (int i = 0; i < 100; i++) {

            // Holder
            int32_t buffer;

            // Read in value
            read(fd, &buffer, sizeof (int32_t));

            // Print out value
            printf("%" PRId32 "  ", buffer);

            // Spaces
            if (i != 0 && i % 5 == 0) {
                printf("\n");
            }
        }

        // Close file
        close(fd);
    }
}

/**
The three industry-standard cyclic-redundancy check (CRC) algorithms discussed in Lecture-2:
- checksum_ccitt()
- checksum_crc16()
- checksum_internet()

Each of these functions accepts two parameters: 
- an array of chars/bytes (as if the bytes were a network frame transmitted/received),
- and an integer indicating how many bytes are provided in the array. 

Each of the functions then produces an integer result,
that is the result of calculating a checksum value of all bytes in the array. 
Each checksum algorithm is different and 'combines together' all bits from all bytes.

Questions:
What do CRC algorithms(in general) aim to do?
> They assign unique values to data frames

What is the purpose / use of the result of a CRC algorithm ?
> They are used to detect accidental changes in data 

What does it mean to describe a CRC algorithm as robust ?
> It always produces 

What should I try to measure ?
> The difference in checksums

What will be considered a success?
> A success is when the checksum is always different after corruption

How successful is a (naive) checksum function which simply adds 
up all bytes in a buffer to be transmitted? 
Why, or why not, might such a function be successful?
> This method would not produce an unique checksum value for each frame

 */
void question2(void) {

    // ############################
    // 1. Get frame data from a large file from your computer's disk
    // ############################
    // Open file
    int fd = open("SONGDATA.mp3", O_RDONLY);

    // If file descriptor was negative/invalid
    if (fd < 0) {

        // Notify
        printf("\n File could not be opened \n");
        exit(EXIT_FAILURE);
    } else {

        // Else, notify
        printf("\nFile was opened successfully! \n");
    }

    // Holder for file
    int flen = 500;
    BYTE frame[flen];
    size_t got;

    // Read in bytes
    read(fd, frame, sizeof frame);

    //    // Print out some bytes
    //    printf("\nBytes:\n");
    //    for (int i = 0; i < flen / 3; i++) {
    //        printf(" %c  ", (char) frame[i]);
    //    }

    // ############################
    // 2. Get initial check sums
    // ############################
    int inter1 = checksum_internet(frame, flen);
    unsigned short crc1 = checksum_crc16(frame, flen);
    unsigned short cc1 = checksum_ccitt(frame, flen);

    // ############################
    // 3. Corrupt the data with corrupt_frame() function
    // ############################
    corrupt_frame(frame, flen);

    // ############################
    // 4. Get check sums after corruption
    // ############################
    int inter2 = checksum_internet(frame, flen);
    unsigned short crc2 = checksum_crc16(frame, flen);
    unsigned short cc2 = checksum_ccitt(frame, flen);

    // ############################
    // 5. Print check sums for comparison (should be different)
    // ############################
    printf("\ninternet = before: %i  , after: %i", inter1, inter2);
    printf("\ncrc16    = before: %hu  , after: %hu", crc1, crc2);
    printf("\nccitt    = before: %hu  , after: %hu", cc1, cc2); 

    // Close file
    close(fd);
}
