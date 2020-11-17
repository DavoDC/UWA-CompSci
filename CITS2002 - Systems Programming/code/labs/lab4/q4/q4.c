/*
 * Write a C program, named listints, 
 * which will print the integers requested via a single command-line argument. 
 * The list of integers is to appear in strictly increasing order, with each
 * requested integer appearing once and only once 
 * (even if duplicated in the request).
 * Simple examples of its use are:
 * 
 * prompt> listints 8
 * will print: 8
 * 
 * prompt> listints 3,5,9
 * will print: 3 5 9
 * 
 * prompt> listints 1-10
 * will print: 1 2 3 4 5 6 7 8 9 10
 * 
 * prompt> listints 1-10,6
 * will print: 1 2 3 4 5 6 7 8 9 10 
 * 
 * And some much more difficult examples:
 * prompt> listints 2000-2020,40-50
 * prompt> listints 1-10,2010-2020,3000000-3000010
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <time.h>

#define MAXSIZE 2500

// Numbers
int numbers[MAXSIZE];

// Amount of numbers
int numCount;

/*
 * Get part of a string
 */
char* getSubStr(char str[], int start, int end)
{
    // Make pointer
    char* sub = malloc(strlen(str) + 1);

    // Check allocation
    if (sub == NULL) {
        printf("\n ALLOCATION FAILED! \n");
    }

    // Index for sub
    int subInd = 0;

    // From start pos to end pos, add characters
    for (int i = start; i < end + 1; i++) {
        sub[subInd] = str[i];
        subInd++;
    }

    // Return result
    return sub;
}

/*
 * Get the position of the specified char in the given string
 * Return -1 if it is not found
 */
int getCharPos(char str[], char c)
{
    // Get string length
    int len = strlen(str);

    // For all characters
    for (int i = 0; i < len; i++) {
        // If one matches with the char
        if (str[i] == c) {

            // Return the position
            return i;

            // Stop searching
            break;
        }
    }

    // Otherwise, return -1
    return -1;
}

/*
 * Convert the desired integers into an array of ints
 */
void parseString(char str[])
{
    // Split the string by commas
    char* split = strtok(str, ",");

    // While there is a next comma-separated part
    while (split != NULL) {

        // Get the position of the dash character,
        // or -1 if nothing was found
        int dashPos = getCharPos(split, '-');

        // If this part has no dash
        if (dashPos == -1) {

            // Simply convert it to an int, and add it
            numbers[numCount] = atoi(split);
            numCount++;

        } else {
            // Else if this part does have a dash

            // Find the length of part
            int len = strlen(split);

            // Get numbers on either side of dash
            int first = atoi(getSubStr(split, 0, dashPos - 1));
            int second = atoi(getSubStr(split, dashPos + 1, len));

            // Add numbers from first number to second number
            for (int i = first; i < second + 1; i++) {

                // Add to int array
                numbers[numCount] = i;
                numCount++;
            }
        }

        // Move to next part
        split = strtok(NULL, ",");
    }
}

/*
 * Sort numbers using bubble sort
 */
void sortNumbers(void)
{
    for (int c = 0; c < numCount - 1; c++) {
        for (int d = 0; d < numCount - c - 1; d++) {
            if (numbers[d] > numbers[d + 1]) {
                int swap = numbers[d];
                numbers[d] = numbers[d + 1];
                numbers[d + 1] = swap;
            }
        }
    }
}

/*
 * Print unique numbers
 */
void printUniques(void)
{
    // List of printed numbers
    int printed[MAXSIZE];

    // Number of printed numbers
    int printedNums = 0;

    // For all numbers
    for (int i = 0; i < numCount; i++) {
        // Get current number
        int curNum = numbers[i];

        // Check if has been printed
        bool hasPrinted = false;
        for (int i = 0; i < printedNums; i++) {
            // If printed number matches current number
            if (printed[i] == curNum) {
                // It has been printed
                hasPrinted = true;
            }

        }

        // If the number has not been printed 
        if (!hasPrinted) {
            // Print it
            printf(" %i", curNum);

            // Add it to the array
            printed[printedNums] = curNum;

            // Increase number of printed numbers
            printedNums++;
        }
    }
}

/*
 * List the unique integers wanted, in order
 */
void listInts(char str[])
{
    // Header
    printf("\n Output: ");
    printf("\n");

    // Convert the string into a list of numbers
    parseString(str);

    // Sort the list
    sortNumbers();

    // Print unique integers
    printUniques();
}

/*
 * Main function
 */
int main(int argcount, char *argvalue[])
{
    // Info
    printf("\n++++ Lab3 - Q4 - Listing Numbers\n");
    printf("\n Arg Examples:  ");
    printf("\n ./q4 8");
    printf("\n ./q4 3,5,9");
    printf("\n ./q4 1-10");
    printf("\n ./q4 1-10,6");
    printf("\n ./q4 2000-2020,40-50");
    printf("\n ./q4 1-10,2010-2020,3000000-3000010");
    printf("\n");

    // Check input
    if (!(argcount >= 2)) {
        printf("\n Please check example arguments \n\n\n");
        exit(EXIT_FAILURE);
    }

    // Run function
    listInts(argvalue[1]);

    // Space 
    printf("\n\n");

    // Exit
    exit(EXIT_SUCCESS);
}
