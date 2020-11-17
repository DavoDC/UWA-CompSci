#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

/**
 * Turn a char into an int
 * @return 
 */
int getIntFromChar(char in)
{
    // Make mini string
    char *valStr = malloc(2);

    // Add values
    valStr[0] = in;
    valStr[1] = '\0';

    // Return value
    return atoi(valStr);
}

/*
 * Return last digit of a number
 */
int getLastDigit(int num)
{
    // Convert the number to a string
    char* str = malloc(num);
    sprintf(str, "%i", num);

    // Get the string's length
    int len = strlen(str);

    //  Convert last char to number
    int lastDigit = getIntFromChar(str[len - 1]);

    // Return
    return lastDigit;
}

/*
 * Return true if the last digit matches the input
 */
bool endsIn(int num, int expEnd)
{
    // If the number is a single digit, we can just compare
    if (num < 10) {
        return(num == expEnd);
    }

    // Get last digit as number
    int lastDigit = getLastDigit(num);

    // Return true if last digit matches expected ending
    bool res = false;
    if (lastDigit == expEnd) {
        return true;
    }

    // Return default
    return res;
}

/*
 * Get the ordinal ending for a given number
 */
char* getOrdinal(int num)
{
    // Get last digit
    int lastD = getLastDigit(num);

    // Handle 'th'
    int between4n13 = (num >= 4 && num <= 13);
    int multOf10 = (num % 10 == 0);
    int lastDexceeds3 = 3 < lastD;
    if (between4n13 || multOf10 || lastDexceeds3) {
        return "th";
    }

    // Handle 'st', 'nd' and 'rd'
    // Use last digit to determine ending
    switch (lastD) {
        case 1:
            return "st";
            break;
        case 2:
            return "nd";
            break;
        case 3:
            return "rd";
        default:
            break;
    }

    // Return
    return "ERR";
}

/*
 * Print out a list of numbers with their ordinal endings
 */
void printOrdinal(int len, char* argvalue[])
{
    // Space
    printf("\n");
    printf("\n");

    // Auto mode = Generate numbers
    if (strcmp(argvalue[1], "auto") == 0) {
        // Get length
        int aLen = atoi(argvalue[2]);
        // Iterate
        for (int i = 1; i <= aLen; i++) {
            // Print number with ordinal
            printf(" %d%s ", i, getOrdinal(i));

            // Make a new line periodically
            if (i % 10 == 0) {
                printf("\n");
            }
        }
    } else {
        // Normal mode = Use inputted numbers
        // Iterate
        for (int i = 1; i < len; i++) {
            // Convert to a number
            int curNum = atoi(argvalue[i]);

            // Print number with ordinal
            printf(" %d%s ", curNum, getOrdinal(curNum));

            // Make a new line periodically
            if (i % 10 == 0) {

                printf("\n");
            }
        }
    }

    // Space
    printf("\n");
    printf("\n");
}

/*
 * Entry
 */
int main(int argcount, char* argvalue[])
{
    // Info
    printf("\n++++ Lab2 - Q5 - Ordinal Descriptions \n");
    printf("\n Args = Num, Num, Num ...");
    printf("\n OR 'auto Num' ");

    // Check
    if (argcount < 3) {
        printf("\n\nBad arguments\n\n\n");
        exit(EXIT_FAILURE);
    }

    // Run
    printOrdinal(argcount, argvalue);

    // Exit
    exit(EXIT_SUCCESS);
}
