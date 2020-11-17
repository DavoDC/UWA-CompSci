/*
 * Write a function named my_strcmp() to determine if
 * one string is (lexicographically, or alphabetically)
 * less than, equal to, or greater than another string.
 * Your function should accept
 * the two character arrays as its arguments,
 * and return either: -1 if the first string is less than the second string,
 * 0 if the two strings are equal, or 1 if the first string is greater than the
 * second string.
 * Call your function from the main() function with the code: my_strcmp(argv[1], argv[2]).
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

int my_strcmp(char* str1, char* str2)
{
    // Length check
    int len1 = strlen(str1);
    int len2 = strlen(str2);
    if (len1 > len2) {
        return 1;
    } else if (len2 > len1) {
        return -1;
    }

    // Holder
    int diff = 0;

    // Compare all chars
    for (int i = 0; i < len1; i++) {
        diff = str1[i] - str2[i];
    }

    // Return result
    return diff;
}

void strCmpDriver(char* argvalue[])
{
    // Header
    printf("\n\n\n#### Results ####\n");

    // Get strings
    char* str1 = argvalue[1];
    char* str2 = argvalue[2];
    printf("\n S1: %s", str1);
    printf("\n S2: %s", str2);
    printf("\n");

    // Run comparisons
    printf("\n strcmp gave: %i", strcmp(str1, str2));
    printf("\n");
    printf("\n my_strcmp gave: %i", my_strcmp(str1, str2));
    printf("\n  1 if the first is greater");
    printf("\n  0 if equal");
    printf("\n  -1 if the first is less");
    printf("\n  Otherwise, same length, diff chars");


    // Space
    printf("\n\n");
}

/*
 * Entry
 */
int main(int argcount, char* argvalue[])
{
    // Info
    printf("\n++++ Lab3 - Q3 - String Comparison \n");
    printf("\n Arg1: String1");
    printf("\n Arg2: String2");

    // Check
    if (argcount != 3) {
        printf("\n\nWrong argument count\n\n");
        exit(EXIT_FAILURE);
    }

    // Actual
    strCmpDriver(argvalue);

    // Exit
    exit(EXIT_SUCCESS);
}