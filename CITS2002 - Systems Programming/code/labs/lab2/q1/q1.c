#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

/*
 * Return true if NUM is divisible by DIV
 */
bool isDivisable(int num, int div)
{
    bool result;

    // If no remainder, it is divisible
    if (num % div == 0) {
        result = true;
    } else {
        result = false;
    }

    return result;
}

/*
 * Determine if the input is a lea year
 */
void isLeap(int year)
{
    // Conditions
    bool d400 = isDivisable(year, 400);
    bool d4 = isDivisable(year, 4);
    bool id100 = !isDivisable(year, 100);

    // Leap Year condition
    bool leap = d400 || (d4 && id100);

    // Print result
    printf("\nResult: %i is", year);
    if (!leap) {
        printf(" NOT");
    }
    printf(" a leap year\n\n");

}

int main(int argcount, char *argvalue[])
{
    // Info
    printf("\n++++ Lab2 - Q1 - Leap Year Check \n");
    printf("\n Arg1:  A year \n");

    // Check
    if (argcount != 2) {
        fprintf(stderr, "\nExpected 1 extra argument, but there was %d\n\n", argcount - 1);
        exit(EXIT_FAILURE);
    }

    // Actual
    int year = atoi(argvalue[1]);
    isLeap(year);

    // Exit
    exit(EXIT_SUCCESS);
}
