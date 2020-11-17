/*
 * A computer password is often consider "safe" (i.e. hard to guess)
 * if it contains a mixture of uppercase and lowercase characters and digits.
 * Write a function named isSafe() to
 * determine if a potential password (a string)
 * has at least two uppercase characters,
 * two lowercase characters,
 * and two digits.
 * Your function should take a single character array as its
 * argument and return a Boolean value to indicate
 * whether the password is considered safe or not.
 * See the online documentation (man pages) for help with the
 * isalpha(), islower(), and isupper() functions.
 * Include the appropriate header file <ctype.h> into your program.
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>

/*
 * Check all passwords
 */
void checkPassword(char* pwStr)
{
    // Get length
    int len = strlen(pwStr);

    // Counts
    int upperCount = 0;
    int lowerCount = 0;
    int digitCount = 0;

    // Iterate over all characters
    for (int i = 0; i < len; i++) {
        // Get current string
        char curChar = pwStr[i];

        // Update counts
        if (isalpha(curChar) != 0) {
            if (isupper(curChar)) {
                upperCount++;
            } else if (islower(curChar)) {
                lowerCount++;
            }
        } else {
            digitCount++;
        }
    }

    // Print results
    printf("\nResults for password '%s' : ", pwStr);
    printf("\n Uppercase Letters: %i", upperCount);
    printf("\n Lowercase Letters: %i", lowerCount);
    printf("\n Digits: %i", digitCount);

    // Print info
    printf("\n");
    bool uCon = upperCount >= 2;
    bool lCon = lowerCount >= 2;
    bool dCon = digitCount >= 2;
    if (uCon && lCon && dCon) {
        printf("\nThe password is safe! (as it had at least 2 of each)");
    } else {
        printf("\nThe password is UNsafe!");

        // More info
        if (!uCon) {
            printf("\n Lack of uppercase letters!");
        }

        if (!lCon) {
            printf("\n Lack of lowercase letters!");
        }

        if (!dCon) {
            printf("\n Lack of digits!");
        }
    }

    printf("\n\n\n");

}

int main(int argcount, char * argvalue[])
{
    // Info
    printf("\n++++ Lab3 - Q2 - Password Checker \n");
    printf("\n Args: Pw, Pw, Pw ... \n");

    // Check
    if (argcount < 2) {
        fprintf(stderr, "\nExpected at least 1 extra argument, but there was %d\n\n", argcount - 1);
        exit(EXIT_FAILURE);
    }

    // Actual
    for (int i = 1; i < argcount; i++) {
        checkPassword(argvalue[i]);
    }

    // Exit
    exit(EXIT_SUCCESS);
}
