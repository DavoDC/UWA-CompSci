#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

/*
 * Replace instances of 'part' in 'orig' with 'rep'
 * You must free the result if result is non-NULL.
 */
char* str_replace(char* part, char* rep, char* str)
{
    // Store and calculate lengths
    int pLen;
    int wLen;
    pLen = strlen(part);
    wLen = strlen(rep);

    // The result for returning
    char* result;

    // Count the number of replacements needed
    printf("\n Counting start \n");
    //  Make insert var, init to str
    char* ins = str;
    //  Make temporary var
    char* temp;
    //  Number of replacements
    int matches;
    //  Each run: 
    //  - Temp becomes strstr, which finds 1st occurrence of 'part' in 'ins'
    //  - So temp has chars 'cut off' each time up to the next 'part' instance
    for (matches = 0; (temp = strstr(ins, part)); ++matches) {
        // Pre info
        printf("\n  Run %d , Temp is %s , Ins is %s ", matches, temp, ins);

        // Move ins to next part
        ins = temp + pLen;

        // Post info
        printf("\n  Ins = temp+pLen, ins is now: %s", ins);
        printf("\n");
    }

    // Calculate resultant length
    // Original + Difference*matches + 1
    int newLen = strlen(str) + ((wLen - pLen) * matches) + 1;

    // Allocate memory to tmp 
    temp = result = malloc(sizeof(char) * newLen);

    // Generate new string
    int len_front;
    // For each match
    while (matches--) {
        ins = strstr(str, part);
        len_front = ins - str;
        temp = strncpy(temp, str, len_front) + len_front;
        temp = strcpy(temp, rep) + wLen;
        str += len_front + pLen;
    }

    // Copies the last part of string
    strcpy(temp, str);

    return result;
}

/**
 * Print info to display the Replace function's operation
 * @param part
 * @param rep
 * @param str
 */
void printReplace(char* part, char* rep, char* str)
{
    // Info
    printf("\n");
    printf("\n Part: %s", part);
    printf("\n Rep:  %s", rep);
    printf("\n Str:  %s", str);
    printf("\n");

    // Generate
    char* newStr = str_replace(part, rep, str);

    // Print
    printf("\n Result: %s", newStr);

    // Condition
    if (strcmp(newStr, str) == 0) {
        printf("\n(No changes to original)");
    }

    // Space
    printf("\n\n");

}

/*
 * Entry
 */
int main(int argcount, char* argvalue[])
{
    // Info
    printf("\n++++ Lab3 - Q5 - String Replace \n");
    printf("\n Arg1 = Part to Replace");
    printf("\n Arg2 = The Replacement");
    printf("\n Arg3 = The String");


    // Check args
    if (argcount != 4) {
        printf("\n\nBad arguments\n\n\n");
        exit(EXIT_FAILURE);
    }

    // Run function
    printReplace(argvalue[1], argvalue[2], argvalue[3]);

    // Exit
    exit(EXIT_SUCCESS);
}