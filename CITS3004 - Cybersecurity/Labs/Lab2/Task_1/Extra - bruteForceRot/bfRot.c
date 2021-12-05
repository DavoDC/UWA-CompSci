#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>

// Function declarations
void rotateStr(char *, int);
char rotate(char, int);

/**
 * Main function - entry point
 * Takes one argument = The string to encrypt/decrypt
 */
int main(int argcount, char *argvalue[]) {

    // Message heading
    printf("\n");
    printf("\n$$$$$$$ Brute Force Encryption/Decryption for Caesar Cipher $$$$$$$");
    printf("\n\n");

    // If argument amount is not 2
    // Note: First argument is program name
    if (argcount != 2) {

        // Notify
        fprintf(stderr,
                "%s: program expected 1 argument, but instead received %d\n",
                argvalue[0], argcount - 1);

        // Exit
        exit(EXIT_FAILURE);
    } else {

        // Else if there is one actual argument

        // Test rotations 0 to 26
        for (int i = 0; i < 26; i++) {
            rotateStr(argvalue[1], i);
        }

        // Space
        printf("\n\n\n");
    }

    return (EXIT_SUCCESS);
}

/*
 * Helper method. Apply rotate to a string and print out the result
 */
void rotateStr(char *str, int rot) {

    // Get length of string
    int len = strlen(str);

    // Print heading
    printf("\nRotation of %i produces: ", rot);

    // For each character
    for (int j = 0; j < len; j++) {

        // Get rotated version
        char new = rotate(str[j], rot);

        // Print character on same line
        printf("%c", new);
    }
}

/*
 * Helper method. 
 * The rotate function returns the character "rot" positions 
 * further along the alphabetic character sequence from c, 
 * or c if c is not lower-case
 */
char rotate(char c, int rot) {

    // If char is lower case
    if (islower(c)) {

        // Return the ciphered character, "rot" positions beyond c,
        // allowing for wrap-around via modulo
        return ('a' + (c - 'a' + rot) % 26);
    } else {

        // Else if upper case, return as is
        return c;
    }
}
