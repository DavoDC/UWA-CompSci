#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

void printSeq(int num, char* unit)
{
    while (num != 0) {
        printf(unit);
        num--;
    }
}

void printTree(char* type, int size)
{
    // Space
    printf("\n\n");

    // Print a tree type according to input
    if (strcmp(type, "A") == 0) {
        // Type A
        // *   - print 1 star
        // **  - print 2 stars
        // *** - print 3 stars
        // From 0 to Size 
        for (int i = 0; i <= size; i++) {
            // Stars
            printSeq(i, "*");
            // New line
            printf("\n");
        }
    } else if (strcmp(type, "B") == 0) {
        // Type B
        // XX* - 2 space + 1 star
        // X** - 1 space + 2 star
        // *** - 3 stars
        // Go from 0 to Size
        for (int i = 0; i <= size; i++) {
            // Print spaces (diff)
            printSeq(size - i, " ");
            // Print stars (normal)
            printSeq(i, "*");
            // New line 
            printf("\n");
        }
    } else if (strcmp(type, "C") == 0) {
        // Type C
        // *** - 3 stars
        // **  - 2 stars
        // *   - 1 star
        // Go from Size to 0
        for (int i = size; i != 0; i--) {
            printSeq(i, "*");
            printf("\n");
        }
    } else if (strcmp(type, "D") == 0) {
        // Type D
        // XX*XX  - 2 spaces, 1 star  (-1 +2)
        // X***X   - 1 space, 3 stars (1 + 2)
        // *****    - 0 spaces, 5 stars (3 + 2)
        // Go from 0 to Size
        int stars = -1;
        for (int i = 0; i < size; i++) {
            // Spaces
            printSeq(size - i - 1, " ");
            // Stars
            stars += 2;
            printSeq(stars, "*");
            // Next line
            printf("\n");
        }
    } else {
        // If other letters are inputted, just do Type A
        printTree("A", size);
    }

    // Space
    printf("\n");
}

/*
 * Entry
 */
int main(int argcount, char *argvalue[])
{
    // Info
    printf("\n++++ Lab2 - Q6 - Star Trees \n");
    printf("\n Arg1: Tree Type (A,B,C,D)");
    printf("\n Arg2: Size");

    // Check
    if (argcount != 3) {
        printf("\n\nWrong argument count\n\n");
        exit(EXIT_FAILURE);
    }

    // Actual
    char* type = argvalue[1];
    int size = atoi(argvalue[2]);
    printTree(type, size);

    // Exit
    exit(EXIT_SUCCESS);
}