#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>


int ROT = 13;

//  The rotate function returns the character ROT positions further along the
//  alphabetic character sequence from c, or c if c is not lower-case

char rotate(char c)
{
    // Check if c is lower-case or not
    if (islower(c)) {
        // The ciphered character is ROT positions beyond c,
        // allowing for wrap-around
        return('a' + (c - 'a' + ROT) % 26);
    } else {
        return c;
    }
}

//  Execution of the whole program begins at the main function

int main(int argcount, char *argvalue[])
{
    // Info
    printf("\nLab1, Q6+7 - Arg1 is rotation amount, rest is string\n");

    // Argument check
    if (!(argcount - 1 >= 2)) {
        fprintf(stderr, "%s: program expected 2 arguments, but instead received %d\n",
                argvalue[0], argcount - 1);
        exit(EXIT_FAILURE);
    }

    // Set rotate amount
    ROT = atoi(argvalue[1]);

    // Info
    printf("\nWill rotate by: %d\n", ROT);

    // Print out the contents of each column 
    // For every row - skipping 0 (prog) and 1(rotate)
    int j;
    for (j = 2; j < argcount; j++) {
        // Define a variable for a later loop
        int i;
        // Calculate the length of the first argument
        int length = strlen(argvalue[j]);

        // Loop for every character in the text
        for (i = 0; i < length; i++) {
            // Determine and print the ciphered character
            printf("%c", rotate(argvalue[j][i]));
        }

        // Make space
        printf("\n\n");
    }

    // Exit indicating success
    exit(EXIT_SUCCESS);
}
