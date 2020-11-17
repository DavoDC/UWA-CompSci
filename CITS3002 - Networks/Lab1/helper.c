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

// Type definition
typedef unsigned char BYTE;

// Constants
#define MIN_BURSTLENGTH         10
#define MAX_BURSTLENGTH         100
#define INIT_CCITT -1
#define POLY_CRC16 0xa001
#define INIT_CRC16 0

/* nibble table for CCITT crc */
static unsigned int ccitt_h[] = {
    0x0000, 0x1081, 0x2102, 0x3183, 0x4204, 0x5285, 0x6306, 0x7387,
    0x8408, 0x9489, 0xa50a, 0xb58b, 0xc60c, 0xd68d, 0xe70e, 0xf78f,
};

static unsigned int ccitt_l[] = {
    0x0000, 0x1189, 0x2312, 0x329b, 0x4624, 0x57ad, 0x6536, 0x74bf,
    0x8c48, 0x9dc1, 0xaf5a, 0xbed3, 0xca6c, 0xdbe5, 0xe97e, 0xf8f7,
};

/**
 * Get current time as microseconds
 * @return 
 */
int64_t microseconds(void) {
    struct timeval now;
    gettimeofday(&now, NULL);
    return now.tv_sec * 1000000 + now.tv_usec;
}

/*
    There are two popular 16 bit crc calculations.  The one I
    used is the crc16 used for DDCMP and Bisync communications.
    Its polynomial is: x^16 + x^15 + x^2 + 1, with an initial
    value of 0. I call it crc16().  The other common crc is used
    in ADCCP, HDLC and SDLC; its polynomial is: x^16 + x^12 + x^5 + 1,
    with an initial value of -1.  I call it ccitt(). If you are
    using the crc to hash you may want to play with both or invent
    your own.
 */

/*	Computing a POLY number from the crc equation.
 *	Crc's are usually expressed as an polynomial expression such as:
 *
 *		x^16 + x^12 + x^5 + 1
 *
 *	Since the highest order term is to the power 16 this is a
 *	16 bit crc.  The POLY number is determined by setting bits
 *	corresponding to the power terms in the polynomial equation
 *	in an integer.  To do this we number the bits of the integer
 *	with the most significant bit = bit 0. CAUTION: this is the
 *	opposite to the some bit numbering schemes.  This is due to
 *	the least significant bit first convention of crc calculations.
 *	The above equation becomes:
 *
 *	  1   0   0   0   0   1   0   0   0   0   0   0   1   0   0   0
 *	|___.___.___.___|___.___.___.___|___.___.___.___|___.___.___.___|
 *	  0   1   2   3   4   5   6   7   8   9   10  11  12  13  14  15
 *	msb                                                          lsb
 *
 *	Note: the "1" term is equivalent to "x^0" and the "x^16"
 *	term is ignored (it does determine the length to be 16 bits).
 *	Thus the POLY number is 0x8408 (ccitt crc).
 */

/* CCITT - ADCCP, HDLC, SDLC (16 bit)
 *	x^16 + x^12 + x^5 + 1
 */
unsigned short checksum_ccitt(unsigned char * in, int length) {
    register unsigned int n, crc;

    crc = INIT_CCITT;
    while (length-- > 0) {
        n = *in++ ^ crc;
        crc = ccitt_l[n & 0x0f] ^ ccitt_h[(n >> 4)&0x0f] ^ (crc >> 8);
    };
    return ((unsigned short) crc);
}

/* crc16 - DDCMP and Bisync (16 bit)
 *	x^16 + x^15 + x^2 + 1
 * a bit at a time 
 */
unsigned short checksum_crc16(unsigned char *in, int length) {
    register unsigned short crc, bit;
    register int i;

    crc = INIT_CRC16;
    while (length-- > 0) {
        for (i = 1; i <= 0x80; i <<= 1) {
            bit = (((*in) & i) ? 0x8000 : 0);
            if (crc & 1) bit ^= 0x8000;
            crc >>= 1;
            if (bit) crc ^= (int) (POLY_CRC16);
        }
        ++in;
    }
    return crc;
}

/* Calculate the Internet Protocol family checksum algorithm.
   This code is taken from Steven's "Unix Network Programming" pp454-455.

   The algorithm is simple, using a 32-bit accumulator (sum),
   we add sequential 16-bit words to it, and at the end, fold back
   all the carry bits from the top 16 bits into the lower 16 bits.
 */
int checksum_internet(unsigned short *ptr, int nbytes) {

    long sum;
    unsigned short oddbyte, answer;

    sum = 0L;
    while (nbytes > 1) {
        sum += *ptr++;
        nbytes -= 2;
    }

    if (nbytes == 1) { /* mop up an odd byte if necessary */
        oddbyte = 0; /* make sure that the top byte is zero */
        *((unsigned char *) &oddbyte) = *(unsigned char *) ptr; /* 1 byte only */
        sum += oddbyte;
    }

    /* Now add back carry outs from top 16 bits to lower 16 bits */
    sum = (sum >> 16) + (sum & 0xffff); /* add hi-16 to lo-16 */
    sum += (sum >> 16); /* add carry bits */
    answer = ~sum; /* one's complement, then truncate to 16 bits */

    return (answer);
}

/**
 * This is a simple function designed to test checksum algorithms.
 * The function randomly corrupts one or more bits of a given frame.
 * As corruption is random, you'll need to call srand() from somewhere
 * earlier in your program to ensure different random sequences
 * You may like to devise other forms of corruption and run tests on them.
 * @param frame
 * @param length
 */
void corrupt_frame(BYTE frame[], int length) {

    // Seed RNG
    srand(time(NULL));

    // Choose one of four corruption methods
    switch (rand() % 4) {
        case 0:
            // This is a severe/unrealistic function
            // It swaps adjacent, different chars
            while (true) {

                // Get random byte index
                int byte = (rand() % (length - 1));

                // If adjacent byte is different
                if (frame[byte] != frame[byte + 1]) {

                    // Swap bytes
                    BYTE save = frame[byte];
                    frame[byte] = frame[byte + 1];
                    frame[byte + 1] = save;
                    break;
                }
            }

            break;
        case 1:

            // Prevents "label can only be part of a statement..." error
            goto Next1;
Next1:
            ;

            // Corrupt one character by setting it to the complement of its own value
            int byte1 = rand() % length;
            frame[byte1] = ~(frame[byte1]);

            break;
        case 2:

            // Corrupt one bit of a byte by toggling its value
            // Prevents "label can only be part of a statement..." error
            goto Next2;
Next2:
            ;
            int byte2 = (rand() % length);
            int bit = (rand() % NBBY);
            frame[byte2] = (frame[byte2] ^ (1UL << bit));

            break;
        case 3:

            // Corrupt a frame with a burst error
            // Prevents "label can only be part of a statement..." error
            goto Next3;
Next3:
            ;
            int nbits = (length * NBBY);
            while (true) {
                int b0 = rand() % nbits;
                int b1 = rand() % nbits;
                int burst = b1 - b0;

                if (burst >= MIN_BURSTLENGTH && burst <= MAX_BURSTLENGTH) {
                    for (int b = b0; b < b1; ++b) {
                        int byte = b / NBBY;
                        int bit = b % NBBY;

                        frame[byte] = (frame[byte] | (1UL << bit));
                    }
                    break;
                }
            }

            break;
    }
}
