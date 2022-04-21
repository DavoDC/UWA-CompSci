#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define ROT 3

// The rotate function returns the character ROT positions further along the
// alphabetic character sequence from c, or c if c is not lower-case
// The ciphered character is ROT positions beyond c,
// allowing for wrap-around

char rotate(char c)
{
    if (isupper(c)) {
        return('A' + (c - 'A' + ROT) % 26);
    } else if (islower(c)) {
        return('a' + (c - 'a' + ROT) % 26);
    } else {
        return c;
    }
}

//  Execution of the whole program begins at the main function

int main(int argcount, char *argvalue[])
{
    // Info
    printf("\nLab1, Q1-5 - Rotates by 3 \n\n");

    // Exit with an error if the number of arguments (including
    // the name of the executable) is not precisely 2
    if (argcount != 2) {
        fprintf(stderr, "%s: program expected 1 argument, but instead received %d\n",
                argvalue[0], argcount - 1);
        exit(EXIT_FAILURE);
    } else {
        // Define a variable for a later loop
        int i;
        // Calculate the length of the first argument
        int length = strlen(argvalue[1]);

        // Loop for every character in the text
        for (i = 0; i < length; i++) {
            char oldChar = argvalue[1][i];
            char newChar = rotate(oldChar);
            printf("At pos %d , '%c' became '%c' \n", i, oldChar, newChar);
        }

        // Print one final new-line character
        printf("\n");

        // Exit indicating success
        exit(EXIT_SUCCESS);
    }
    return 0;
}
