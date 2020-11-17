/*
 * [PROJECT] Write a function named money that accepts a single integer parameter 
 * that represents a total number of cents, and breaks 
 * the total down into its numbers of dollars and
 * cents. Your function should calculate the number of dollars, 
 * and the number of "left-over" cents, and provide these to 
 * its calling function through its parameters. 
 * For example, a value of
 * 524 should be "returned" as 5 dollars and 24 cents.
 * Your function should have the prototype:
 * void money(int total, int *dollars, int *cents); 
 * and be called as:
 * money(524, &dollars, &cents);
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>

/*
 * Interprets money values
 */
void money(int total, int* dollars, int* cents)
{
    *dollars = (total - (total % 100)) / 100;
    *cents = total % 100;
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

        // Run money
        //  Get argument as int
        int val = atoi(curStr);
        //  Make local variables
        int dollar = 0;
        int cents = 0;
        // Call function
        money(val, &dollar, &cents);
        // Print results
        printf("\n %-10s = ", "money()");
        printf("$%i %ic", dollar, cents);

        // Space
        printf("\n");
    }

    // End space
    printf("\n");

}

int main(int argcount, char* argvalue[])
{
    // Info
    printf("\n++++ Lab5 - Q1 - Money() \n");
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
