/*
 * Write a function named my_strlen() that 
 * calculates and returns the length of a string. 
 * Your function should take one argument, a character array that represents the string, 
 * and return an integer - the length of the string.
 * The calling function (the main() function) should print the integer 
 * returned by your my_strlen() function.
 * Test your function with some string constants 
 * and by passing to it some command-line arguments.
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

/*
 * A re-implementation of strlen
 */
int my_atoi(char* str)
{
    // Holder
    int len = 0;

    // Add 1 until reaching null byte
    //  The current character
    char curChar;
    while (curChar != '\0') {
        // Increase index/len
        len++;
        // Update the current character
        curChar = str[len];
    }

    // Return result
    return len;
}

/*
 * Check length of all arguments with both strlen's
 */
void driver(int len, char* argvalue[])
{
    // Iterate over all arguments, skipping the first one
    for (int i = 1; i < len; i++) {
        // Get current string
        char* curStr = argvalue[i];

        // Info
        printf("\nLength of '%s':", curStr);

        // Actual strlen
        printf("\n strlen = %zi", strlen(curStr));

        // Test redone strlen
        printf("\n my_strlen = %i", my_atoi(curStr));

        // Space
        printf("\n");
    }

    // End space
    printf("\n");

}

int main(int argcount, char* argvalue[])
{
    // Info
    printf("\n++++ Lab3 - Q1 - String Length \n");
    printf("\n Args: Str, Str, Str ... \n");

    // Check
    if (argcount < 2) {
        fprintf(stderr, "\nExpected at least 1 extra argument, but there was %d\n\n", argcount - 1);
        exit(EXIT_FAILURE);
    }

    // Check all
    driver(argcount, argvalue);

    // Exit
    exit(EXIT_SUCCESS);
}
