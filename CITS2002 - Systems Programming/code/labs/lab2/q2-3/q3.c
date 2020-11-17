#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

/*
 * Find largest number from list
 */
void findMax(char * argv[])
{
    // Determine number of arguments
    int args = 0;
    while (argv[args] != NULL) {
        args++;
    }

    // Check first
    int max = atoi(argv[1]);

    // Check rest
    int i;
    for (i = 2; i < args; i++) {
        // Get current
        int cur = atoi(argv[i]);

        // Update max if cur is larger
        if (cur > max) {
            max = cur;
        }
    }

    // Print result
    printf("\nMax value was %i\n\n", max);

}

/*
 * Entry
 */
int main(int argcount, char * argvalue[])
{
    // Info
    printf("\n++++ Lab2 - Q2+3 - Get Max of Inf. List \n");
    printf("\n Args: Num, Num, Num ... \n");

    // Check
    if (argcount < 2) {
        printf("\nNot enough arguments\n\n");
        exit(EXIT_FAILURE);
    }

    // Actual
    findMax(argvalue);

    // Exit
    exit(EXIT_SUCCESS);
}
