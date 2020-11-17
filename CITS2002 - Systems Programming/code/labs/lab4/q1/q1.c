/*
 * Write a C function to convert the characters in a NULL-byte-terminated 
 * string to their numeric "meaning".
 * For example, the call to the integer function my_atoi("1234")
 * will return the integer 1234.
 * Don't forget to also support negative values!
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>

/*
 * A re-implementation of atoi (with pointers)
 */
int my_atoi_pointer(char* str)
{
    // The sign of the number
    int sign = 1;

    // The value of the number
    int value = 0;


    // Handle sign
    if (*str == '-') {
        sign = -1;
        str++;
    } else if (*str == '+') {
        str++;
    }

    // Get value
    //  While character is a digit
    while (isdigit(*str)) {

        // Multiply value by 10
        // - The first number doesn't receive this, as it should be
        // - e.g. 150 is composed of two 10x operations)
        // - Subsequent numbers get multiplied
        // - e.g. 1 becomes 10, 15 becomes 150
        value *= 10;

        // Add numeric value
        value += (int) (*str - '0');

        // Move pointer to next
        str++;
    }

    return sign * value;
}

/*
 * A re-implementation of atoi (without pointers)
 */
int my_atoi_normal(char str[])
{
    // The sign of the number
    int sign = 1;

    // The value of the number
    int value = 0;

    // The position in the string
    int index = 0;

    // Handle sign
    if (str[0] == '-') {
        sign = -1;
        index++;
    } else if (str[0] == '+') {
        index++;
    }

    // Get value
    //  While character is a digit
    while (isdigit(str[index])) {

        // Multiply value by 10
        // - The first number doesn't receive this, as it should be
        // - e.g. 150 is composed of two 10x operations)
        // - Subsequent numbers get multiplied
        // - e.g. 1 becomes 10, 15 becomes 150
        value *= 10;

        // Add numeric value
        value += (int) (str[index] - '0');

        // Move to next index
        index++;
    }

    return sign * value;
}

/*
 * Compare atoi functions for each argument
 */
void driver(int argcount, char* argvalue[])
{
    // Iterate over all arguments, skipping the first one
    for (int i = 1; i < argcount; i++) {

        // Get current string
        char* curStr = argvalue[i];

        // Info
        printf("\nString:'%s'", curStr);

        // Actual 
        printf("\n %-15s = %i", "atoi", atoi(curStr));

        // Test my ones
        printf("\n %-15s = %i", "my_atoi_normal", my_atoi_normal(curStr));
        printf("\n %-15s = %i", "my_atoi_pointer", my_atoi_pointer(curStr));

        // Space
        printf("\n");
    }

    // End space
    printf("\n");

}

int main(int argcount, char* argvalue[])
{
    // Info
    printf("\n++++ Lab4 - Q1 - Making atoi() \n");
    printf("\n Args: Num, Num, Num ... \n");

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
