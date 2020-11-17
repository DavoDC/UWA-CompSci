/*
 * Make a program which prints out the lines 
 * of a text file in reverse order - 
 * 
 * the last line is printed first, and the first line is printed last.
 * Ensure that your program can operate on files named on the command-line, 
 * and from text lines arriving via stdin.
 *
 */
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#define MAX_SIZE 1000

// Line information
char** lines;
int storedLines = 0;

/*
 * Print lines in reverse
 */
void printLines(void)
{
    // Message
    printf("\n Your lines in reverse:");

    // From max to 0
    for (int i = storedLines - 1; i >= 0; i--) {
        // Print line
        printf("\n %s", lines[i]);

        // Force lines to come out
        fflush(stdout);
    }

    // Space
    printf("\n\n");
}

/*
 * Remove line endings
 */
void removeLE(char str[])
{
    int i = 0;

    // While null byte not reached
    while (str[i] != '\0') {

        // Replace first line ending with null byte
        if (str[i] == '\r' || str[i] == '\n') {
            str[i] = '\0';
            break;
        }

        i++;
    }
}

/*
 * Get lines from a line
 */
void getLinesFromFile(char fpath[])
{
    // Try to open file
    FILE* file = fopen(fpath, "r");

    // Check for file open errors
    if (file == NULL) {
        printf("Could not open: %s \n\n", fpath);
        exit(EXIT_FAILURE);
    }

    //  Get the number of lines in the file
    int numLines = 0;
    char buf[MAX_SIZE];
    while (fgets(buf, sizeof buf, file) != NULL) numLines++;

    // Rewind file
    rewind(file);

    // Initialize lines holder
    lines = malloc(sizeof(char) * numLines * MAX_SIZE);

    // Check allocation
    if (lines == NULL) {
        printf("\n Allocation issue");
    }

    // Get lines and store them
    for (int i = 1; i <= numLines; i++) {

        // Allocate memory to current line
        lines[storedLines] = malloc(sizeof(char) * MAX_SIZE);

        // Get into current line
        fgets(lines[storedLines], MAX_SIZE, file);

        // Remove any extra chars
        removeLE(lines[storedLines]);

        // Notify
        printf("\n Stored: %s", lines[storedLines]);

        // Move to next line
        storedLines++;
    }

    // Close the file
    fclose(file);

    // Space
    printf("\n");
}

/*
 * Get lines from stdin
 */
void getInputtedLines(void)
{
    // Get number of lines wanted by user
    printf("\n Please enter how many lines you will enter: ");
    char numLineStr[BUFSIZ];
    fgets(numLineStr, sizeof numLineStr, stdin);
    int numLines = atoi(numLineStr);

    // Initialize lines holder
    lines = malloc(sizeof(char) * numLines * MAX_SIZE);

    // Check allocation
    if (lines == NULL) {
        printf("\n Allocation issue");
    }

    // Get lines and store them
    printf("\n");
    for (int i = 1; i <= numLines; i++) {

        // Print start
        printf(" Line %i: ", i);

        // Allocate memory to current line
        lines[storedLines] = malloc(sizeof(char) * MAX_SIZE);

        // Get into current line
        fgets(lines[storedLines], MAX_SIZE, stdin);

        // Remove any extra chars
        removeLE(lines[storedLines]);

        // Move to next line
        storedLines++;
    }
}

int main(int argC, char** argV)
{
    // Info
    printf("\n++++ Lab5 - Q3 - Reversed Lines \n");
    printf("\n Usage:");
    printf("\n  ./q3 <filepath>");
    printf("\n  ./q3 custom");
    printf("\n\n");

    // Check args
    if (!(argC >= 2)) {
        printf("\n Please check usage \n\n");
        exit(EXIT_FAILURE);
    }

    // Get lines
    if (strcmp(argV[1], "custom") == 0) {
        // Get user input
        getInputtedLines();
    } else {
        // Read file
        getLinesFromFile(argV[1]);
    }

    // Print lines (in reverse)
    printLines();

    // Exit
    exit(EXIT_SUCCESS);
}
