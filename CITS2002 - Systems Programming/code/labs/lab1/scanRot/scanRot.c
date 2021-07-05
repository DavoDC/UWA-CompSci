#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>

// The amount to roate the characters by
int ROT;

/*
 * The rotate function returns the character ROT positions further along the
 * alphabetic character sequence from c, or c if c is not lower-case
 */
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

/*
 * Apply rotate to a string and print out the result
 */
void rotateStr(char* c)
{
    // Input length
    int len = strlen(c);

    // Print out rotated version line by line
    int i;
    for (i = 0; i < len - 1; i++) {
        char orig = c[i];
        char new = rotate(c[i]);

        // If not blank, print normally
        if (new != ' ') {
            printf("Original was %c , rotated is %c  , pos was %d \n", orig, new, i);
        } else {
            // If blank, print 'SPACE'
            printf("SPACE \n");
        }
    }

    // Print out rotated version line by line
    printf("\n\nFull version: ");
    int j;
    for (j = 0; j < len - 1; j++) {
        char new = rotate(c[j]);
        printf("%c", new);
    }

}

/*
 * Do a round of processing
 */
void askAndRotate(void)
{
    // Get string
    printf("Enter string to rotate:  ");
    char input[50];
    fgets(input, sizeof(input), stdin);

    // Get rotate value
    printf("Enter amount to rotate by:  ");
    char rotin[50];
    fgets(rotin, sizeof(rotin), stdin);

    // Process rotate value
    ROT = atoi(rotin);

    // Process string = Rotate 
    printf("\nOutput: \n");
    rotateStr(input);

    // Make space
    printf("\n");
    printf("\n");
}

/**
 * Main
 */
int main(void)
{
    printf("\n");
    printf("\n$$$$$$$ Lab 1 - String Rotator $$$$$$$");
    printf("\n\n");

    askAndRotate();

    return(EXIT_SUCCESS);
}