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

// Rotate arguments

void rotateStr(char *argvalue[])
{
    // Info
    printf("\nLab1, Q8 - Args = Num,Str,Num,Str ... \n");

    // Get number of rows in input
    int olen = 0;
    while (argvalue[olen] != NULL) {
        olen++;
    }

    // Print out the contents of each column 
    // For every row
    // 0th argument is program name
    int j;
    for (j = 1; j < olen; j++) {

        // Space
        printf("\n");

        // If not even = Process as number
        if (!(j % 2 == 0)) {
            ROT = atoi(argvalue[j]);
            printf("Rotation is now: %d", ROT);
        } else {
            // If even = Process as string 

            // Define a variable for a later loop
            int i;

            // Calculate the length of the string
            int length = strlen(argvalue[j]);

            // Loop for every character in the text
            for (i = 0; i < length; i++) {
                char oldChar = argvalue[j][i];
                char newChar = rotate(oldChar);
                printf("%c became %c \n", oldChar, newChar);
            }
        }

        // Space
        printf("\n");
    }

    // Final space
    printf("\n");

}

//  Execution of the whole program begins at the main function

int main(int argcount, char *argvalue[])
{
    // Run function
    rotateStr(argvalue);

    // Exit indicating success
    exit(EXIT_SUCCESS);

    return 0;
}
