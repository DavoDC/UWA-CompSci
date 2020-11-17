#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

/*
 * Printout the CCN on one line
 */
void printIntArr(int* ints)
{
    printf("\nThe full inputted number, as is: \n");
    for (int i = 0; i < 16; i++) {
        printf("%i", ints[i]);
    }

    printf("\n");
}

/**
 * Turn a char into an int
 * @return 
 */
int getIntFromChar(char in)
{
    // Make mini string
    // Valid as well: char* valStr = malloc(sizeof(char) * 2 );
    char valStr[2];

    // Add values
    valStr[0] = in;
    valStr[1] = '\0';

    // Return value
    return atoi(valStr);
}

/*
 * Turn a char array into ints
 */
int* getNumsFromString(char* strNums)
{
    // Holder
    int* nums = malloc(sizeof(int)*16);

    // Turn string into int array
    for (int i = 0; i < 16; i++) {
        nums[i] = getIntFromChar(strNums[i]);
    }

    return nums;
}

/*
 * Check a credit card in int array form
 */
void checkCCN(int* nums)
{
    // Info
    printf("\n\n$$$$ Welcome to the Luhn Checker $$$$");

    // Step 1 Info 
    printf("\n\n$$$$ S1 Acquisition $$$$");
    printIntArr(nums);
    printf("\nFor every odd digit, from R to L, we sum them \n");

    // Do Step 1 process
    int s1;
    int start1 = 15;
    for (int i = start1; i >= 0;) {
        s1 += nums[i];
        printf("\nAdded %i from pos %i, (%i from R)", nums[i], i, start1 - i + 1);
        i -= 2;
    }
    printf("\n\nS1 was found to be %i", s1);

    // Step 2 Info
    printf("\n\n$$$$ S2 Acquisition $$$$");
    printIntArr(nums);
    printf("\nFor every even digit, from R to L, we multiply by 2, and sum its digits \n");

    // Do Step 2 process
    int s2;
    int start2 = 14;
    for (int i = start2; i >= 0;) {

        int curNum = nums[i];
        printf("\nSaw num %i at pos %i, (%i from R)", curNum, i, start2 - i + 2);

        curNum *= 2;
        printf("\nDoubled it: %i", curNum);

        if (curNum < 10) {
            printf(" - it has 1 digit, added as is");
            s2 += curNum;
        } else {
            printf(" - it has 2 digits, added as");
            char result[2];
            sprintf(result, "%i", curNum);

            int digit1 = getIntFromChar(result[0]);
            int digit2 = getIntFromChar(result[1]);

            int digitSum = digit1 + digit2;
            printf(" digitsum %i", digitSum);

            s2 += digitSum;
        }

        i -= 2;

        printf("\n");
    }
    printf("\nS2 was found to be %i", s2);

    // Final step
    printf("\n\n\n$$$$ Final Sum Acquisition $$$$\n");

    int fSum = s1 + s2;
    printf("\nThe final sum was %i", fSum);

    printf("\nValid CCNs have a final sum ending in 0");
    printf("\nSo this CCN is ");
    if (fSum % 10 == 0) {
        printf("valid!");
    } else {
        printf("invalid!");
    }

    // Space
    printf("\n");
    printf("\n");
}

/*
 * Entry
 * 
 * Valid CCNs:
 * 1343980811829537
 * 8843852321856484
 * 4610088489672161
 * Source = https://www.dcode.fr/luhn-algorithm
 * 
 * Invalid CCNs:
 * 0011222233334444
 * 1234567812356789
 */
int main(int argcount, char *argvalue[])
{
    // Info
    printf("\n++++ Lab2 - Q4 - Credit Card Number Checker\n");
    printf("\n Arg1: Credit Card Number \n\n");

    // Check input
    if (argcount == 1) {
        printf("\nPlease enter a 16 digit credit card no\n\n");
        exit(EXIT_FAILURE);
    } else if (strlen(argvalue[1]) != 16) {
        printf("\nInput was not 16 digits\n\n");
        exit(EXIT_FAILURE);
    }

    // Run further tests
    checkCCN(getNumsFromString(argvalue[1]));

    // Exit
    exit(EXIT_SUCCESS);
}
