/*
 * Write a function named replace() 
 * that replaces all instances 
 * of one string for another string in a third string. 
 * 
 * For example:
 * 
 * prompt> ./replace red blue aredrobin
 * abluerobin
 * 
 * prompt> ./replace cat bison catocathartic
 * bisonobisonhartic
 * 
 * Ensure you have terminated your string correctly.
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

// Lengths
int pLen;
int rLen;
int sLen;
int newLen;

// Match
int matchNo;

int* findMatches(char* part, char* str)
{
    // Results
    int* res = malloc(sizeof(int) * sLen * 2);

    // Full match count
    int fullMatches = 0;

    // For every position in the full string
    for (int i = 0; i < sLen; i++) {

        // i status
        printf("\n I is now %i", i);

        // Edge condition
        // - Skip scanning of sections too small to contain string
        // - Prevent scanning beyond the string
        if ((i + pLen) > sLen) {
            printf("\n  ! No need to go further !");
            break;
        }

        // Scan sections of the larger string

        //  'Internal index' for small string
        int pIndex = 0;

        //  Number of consecutive char matches this scan
        int charMatches = 0;

        //  Start from i, Go as far as current + small len
        for (int p = i; p < (i + pLen); p++) {

            // p status
            printf("\n  P is now %i |", p);

            // Get characters
            char pChar = part[pIndex];
            char sChar = str[p];
            printf(" Chars are -> p: %c , str: %c ", pChar, sChar);

            // Compare characters
            if (pChar == sChar) {
                charMatches++;

                printf(" --> Char MATCH!");

                // charMatches = length of smaller string
                if (charMatches == pLen) {
                    printf("\n   Full Match found at pos %i !", i);

                    // Save info
                    res[fullMatches] = i;

                    // Increment
                    fullMatches++;
                }
            }
            pIndex++;
        }
    }

    // Add closing signal
    res[fullMatches] = -1;
    fullMatches++;

    // Return result
    return res;
}

int numberOfMatches(int* matchData)
{
    // Space
    printf("\n");

    // Count up to closing signal
    int matchNo = 0;
    while (matchData[matchNo] != -1) {
        matchNo++;
    }

    // Print out
    printf("\n Matches: %i", matchNo);

    // Space
    printf("\n");

    // Return
    return matchNo;
}

void calcNewLen(void)
{
    // Difference between part and rep
    int smallDiff = rLen - pLen;
    printf("\n Difference between the parts and replacement: %i", smallDiff);

    // Calculate length change
    printf("\n Number of parts: %i", matchNo);
    int lenChange = matchNo * smallDiff;
    printf("\n Change in length: matches x diff: %i", lenChange);

    // Add to str length
    newLen = sLen + lenChange;
    printf("\n\n Current length: %i", sLen);
    printf("\n Thus, new length: %i", newLen);
}

bool isReplaceable(int matchPos, int i)
{
    // Replacement conditions
    //  Can't be before match pos
    int con1 = !(i < matchPos);
    //  Can exceed/equal curMatchPos
    int con2 = (i >= matchPos);
    //  Cannot exceed curMatchPos + rep Lengths
    int con3 = !(i > matchPos + rLen - 1);

    // Return result
    return(con1 && con2 && con3);
}

void generateNew(int* matchData, int matches, char* rep, char* str)
{
    // Make holder of right size
    char newStr[newLen];

    // Space
    printf("\n");

    // Consecutive replacements
    int conReps = 0;

    // Add characters
    for (int i = 0; i < newLen; i++) {

        // Determine if position requires replacement
        // For all match positions:
        bool reqRep = false;
        for (int m = 0; m < matches; m++) {
            if (isReplaceable(matchData[m], i)) {
                printf("\nFound char for replacement: %c (as pos falls within range of a prev. disc. match)", str[i]);
                printf("\n  matchPos: %i , curPos %i", matchData[m], i);
                reqRep = true;
            }
        }

        // If requires replacement, add from replacement
        if (reqRep) {
            // Get new char 
            char newChar = rep[conReps];

            // Add
            newStr[i] = newChar;

            // Notify
            printf("\n    Added %c to str", newChar);

            // Move to next
            conReps++;

            // Reset upon one full replacement
            if (conReps == rLen) {
                conReps = 0;
            }
        } else {
            // If doesn't require replacement, add back normal character
            printf("\n\nNo replacement required, will add existing char");
            newStr[i] = str[i];
            printf("\nAdded %c to str", str[i]);

        }

        printf("\n");
    }

    // Add null byte
    newStr[newLen] = '\0';

    // Results
    printf("\n\nThe string generated was:");
    printf("\n %s", newStr);

}

void printReplace(char* part, char* rep, char* str)
{
    // Info
    printf("\n");
    printf("\n Part: %s", part);
    printf("\n Rep: %s", rep);
    printf("\n Str: %s", str);
    printf("\n\n");

    // Get all lengths
    pLen = strlen(part);
    rLen = strlen(rep);
    sLen = strlen(str);

    // Find how many 'parts' are in 'str' and where they are
    int* matchData = findMatches(part, str);

    // Find number of matches
    matchNo = numberOfMatches(matchData);

    // Find length of resultant
    calcNewLen();

    // Make new string with replaced parts
    generateNew(matchData, matchNo, rep, str);

    // Space
    printf("\n\n");

}

int main(int argcount, char* argvalue[])
{
    // Info
    printf("\n++++ Lab3 - Q5 - String Replace \n");
    printf("\n Arg1 = Part to Replace");
    printf("\n Arg2 = The Replacement");
    printf("\n Arg3 = The String");
    printf("\n\n Beware - Only works sometimes :D  \n\n");


    // Check
    if (argcount != 4) {
        printf("\n\nBad arguments\n\n\n");
        exit(EXIT_FAILURE);
    }

    // Run
    printReplace(argvalue[1], argvalue[2], argvalue[3]);

    // Exit
    exit(EXIT_SUCCESS);
}
