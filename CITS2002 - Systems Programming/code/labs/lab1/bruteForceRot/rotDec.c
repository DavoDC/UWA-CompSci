#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>

// Function declarations
void rotateStr(char*, int);
char rotate(char, int);

/**
 * Main
 */
int main(int argcount, char *argvalue[])
{
    printf("\n");
    printf("\n$$$$$$$ Brute Force Decryption for Caesar Cipher $$$$$$$");
    printf("\n\n");

    // Exit with an error if the number of arguments (including
    // the name of the executable) is not precisely 2
    if (argcount != 2) {
        fprintf(stderr, "%s: program expected 1 argument, but instead received %d\n",
                argvalue[0], argcount - 1);
        exit(EXIT_FAILURE);
    } else {
		
		// Test rotations 0 to 26
		for(int i = 0; i < 26; i++)
		{
			rotateStr(argvalue[1], i);
		}
		
		// Space
	    printf("\n\n\n");
	}
			
    return(EXIT_SUCCESS);
}

/*
 * Apply rotate to a string and print out the result
 */
void rotateStr(char* c, int rot)
{
    // Input length
    int len = strlen(c);

    // Print out final result after all chars rotated
    printf("\nRotation of %i produces: ", rot);
    int j;
    for (j = 0; j < len - 1; j++) {
        char new = rotate(c[j], rot);
        printf("%c", new);
    }
}

/*
 * The rotate function returns the character ROT positions further along the
 * alphabetic character sequence from c, or c if c is not lower-case
 */
char rotate(char c, int rot)
{
    // Check if c is lower-case or not
    if (islower(c)) {
        // The ciphered character is ROT positions beyond c,
        // allowing for wrap-around
        return('a' + (c - 'a' + rot) % 26);
    } else {
        return c;
    }
}



