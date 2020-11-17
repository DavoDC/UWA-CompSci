
#ifndef HELPER_H
#define HELPER_H

// Type definition
typedef unsigned char BYTE;

// Declarations
int64_t microseconds(void);
int checksum_internet(unsigned char *ptr, int nbytes);
unsigned short checksum_crc16(unsigned char *in, int length);
unsigned short checksum_ccitt(unsigned char * in, int length);
void corrupt_frame(BYTE frame[], int length);


#endif /* HELPER_H */

