#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>

struct TraceBlock
{
    // Lines
    char lines[10000][100];

    // Number of lines
    int lineNo;
};

void printDiffLine(char s1[], char s2[])
{
    // Get lengths
    int len1 = strlen(s1);
    int len2 = strlen(s2);

    // Get shortest
    int sLen = len1;
    if (len1 < len2) {
        sLen = len1;
    } else {
        sLen = len2;
    }

    // Make holder
    char full[(len1 + len2) + 50];

    // Iterate over chars
    int i;
    for (i = 0; i < sLen; i++) {

        // If chars match
        if (s1[i] == s2[i]) {
            full[i] = ' ';
        } else {
            // If they don't match
            full[i] = '^';
        }
    }

    // Terminate
    full[i] = '\0';

    // Print
    printf("\nD: %s", full);
}

struct TraceBlock getTraceLines(char filepath[])
{
    // Open file 
    FILE* filep = fopen(filepath, "r");

    // Exit if couldn't load
    if (filep == NULL) {
        printf("\n\n Couldn't load logs! Chop some more!");
        printf("\n Elusive file path was: %s", filepath);
        printf("\n\n");
        exit(EXIT_FAILURE);
    }

    // Make tracefile
    struct TraceBlock tblock;
    tblock.lineNo = 0;

    // Reading variables
    char curLine[BUFSIZ];

    //  Read each line until we reach the end of file marker
    int maxBytesPerLine = 100;
    while (fgets(curLine, maxBytesPerLine, filep) != NULL) {

        // If line is a time line/total process time line/best line
        if (curLine[0] == '@' || curLine[0] == 't' || curLine[0] == 'b') {

            // Copy current line into array
            strcpy(tblock.lines[tblock.lineNo], curLine);

            // Remove newline chars
            for (int i = 0; i < strlen(tblock.lines[tblock.lineNo]); i++) {
                if (tblock.lines[tblock.lineNo][i] == '\n') {
                    tblock.lines[tblock.lineNo][i] = ' ';
                }
            }

            // Progress to next line
            tblock.lineNo++;
        }
    }
    // Close file
    fclose(filep);

    // Return 
    return tblock;
}

void runCompMode(char folder[], char opt)
{
    // Header
    printf("\n$$$$ Compare to Chris");
    printf("\n");
    printf("\n $ Format");
    printf("\nO: Our notification");
    printf("\nC: Chris's notification");
    printf("\nD: Points to differences with '^'");

    // Get file paths
    char ourTFpath[100];
    sprintf(ourTFpath, "../actual/ours/%s/ours%s%c.log", folder, folder, opt);

    char chrisTFpath[100];
    sprintf(chrisTFpath, "../actual/chris/%s/chris%s%c.log", folder, folder, opt);

    // Load files as trace blocks
    struct TraceBlock oursTB = getTraceLines(ourTFpath);
    struct TraceBlock chrisTB = getTraceLines(chrisTFpath);

    // Print each pair of lines with comparison
    // Run up to smallest number of lines
    int index = 0;
    printf("\n");
    while (index < oursTB.lineNo && index < chrisTB.lineNo) {

        // If index valid, print our line
        if (index < oursTB.lineNo) {
            printf("\nO: %s", oursTB.lines[index]);
        }

        // If index valid, print Chris's line
        if (index < chrisTB.lineNo) {
            printf("\nC: %s", chrisTB.lines[index]);
        }

        // Print difference line
        printDiffLine(oursTB.lines[index], chrisTB.lines[index]);

        // Increase index
        index++;

        // Space
        printf("\n");
    }

    // Space
    printf("\n\n");
}

/*
 * Run the project with the specified params
 */
void runProj(char folder[], char opt, int start, int end, int inc)
{
    // Make parts
    // Go to directory and fix permissions (on everything in project)
    char part1[] = "cd .. && chmod -R 777 * ";

    // Compile program
    char part2[] = "&& cc -std=c99 -Wall -pedantic -Werror -o actual/besttq actual/besttq.c";
    // -fsanitize=address";

    // Run program with chosen tracefile
    char part3[1000];
    sprintf(part3, "&& actual/./besttq actual/tfs/tf%c.txt %i %i %i", opt, start, end, inc);

    // Run program with chosen tracefile and direct output to tracefile
    //  Make redirect part
    char outPath[1000];
    sprintf(outPath, " > actual/ours/%s/ours%s%c.log", folder, folder, opt);
    // Add run and redirect
    char part4[2000];
    sprintf(part4, "%s%s", part3, outPath);

    // Concatenate command parts
    char full[5000];
    sprintf(full, "%s%s%s%s", part1, part2, part3, part4);

    // Print info
    printf("\n\nCompiled 'besttq.c' to make executable");
    printf("\nRan 'besttq' with:  'tf%c.txt'  %i  %i  %i", opt, start, end, inc);
    printf("\nOutput setting: %s\n\n", outPath);

    // Run command
    system(full);
}

bool isValidOption(char optIn)
{
    // Fix permissions
    system("cd .. && chmod -R 777 *");

    // Try to open option
    char filepath[100];
    sprintf(filepath, "../actual/tfs/tf%c.txt", optIn);
    FILE* tfile = fopen(filepath, "r");

    // If its not valid if file is NULL
    if (tfile == NULL) {
        return false;
    }

    // Otherwise, its good
    return true;
}

void printInfo(void)
{
    // Title
    printf("\n$$$$ Welcome to Project 1 'Proxy' - The Ultimate Development Assistant $$$$");

    // Usage
    printf("\n\n $ Usage");
    printf("\n  Infomode:  ./proxy I");
    printf("\n  Quikmode:  ./proxy Q");
    printf("\n  Openmode:  ./proxy O");
    printf("\n  Monomode:  ./proxy M <tfopt> <TQ>");
    printf("\n  Fullmode:  ./proxy F <tfopt> <start> <end> <inc>");

    // Switches
    printf("\n\n $ Switches");
    printf("\n  Optional, put after mono/full mode args");
    printf("\n      -c     Compare to Chris");

    // Tracefile option info
    printf("\n\n $ Tracefiles Found: ");
    for (char c = 'A'; c <= 'Z'; c++) {
        if (isValidOption(c)) {
            printf(" %c", c);
        }
    }

    // Space
    printf("\n\n");
}

void runDesiredMode(int argcount, char *argvalue[])
{
    // Check argument count
    if (!(argcount == 2 || argcount >= 4)) {
        printf("Mode unknown \n\n");
        exit(EXIT_FAILURE);
    }

    // Determine mode and run it
    if (strcmp(argvalue[1], "Q") == 0) {

        // Quick mode
        printf("\n$$$$ Quick mode");
        runProj("mono", 'A', 100, 100, 100);

        // Finish
        exit(EXIT_SUCCESS);

    } else if (strcmp(argvalue[1], "O") == 0) {

        // Open mode
        printf("\n$$$$ Openmode");
        printf("\n\nOpening directory... \n\n\n");

        // Build command
        char kaliPath1[] = "C:\\Users\\d\\AppData\\Local\\Packages\\KaliLinux.54290C8133FEE_ey8k8hqnwqnmg\\LocalState";
        char kaliPath2[] = "\\rootfs\\home\\david\\cits2002\\proj1\\actual";
        char expCmd[1000];
        sprintf(expCmd, "explorer.exe /root, \"%s%s\"", kaliPath1, kaliPath2);

        // Execute command
        system(expCmd);

        // Finish
        exit(EXIT_SUCCESS);

    } else if (strcmp(argvalue[1], "I") == 0) {

        // Info mode
        printf("\n$$$$ Info mode");

        printf("\n\n $ Quikmode ");
        printf("\n  Quickly run tfA with default values. Good for compilation checking");

        printf("\n\n $ Openmode ");
        printf("\n  Open the project directory when you are too lazy to. Only works for me!");

        printf("\n\n $ Infomode ");
        printf("\n  Show this information right here homie!");

        printf("\n\n $ Monomode ");
        printf("\n  Lets you run the project for one time quantum");
        printf("\n  <tfopt> (char) = Trace file option specifier (from those found)");
        printf("\n  <TQ>    (int)  = The sole time-quantum you want to test (usecs)");

        printf("\n\n $ Full Mode ");
        printf("\n  Lets you run with project with specific variables ");
        printf("\n  <tfopt> (char) = Trace file option specifier (from those found)");
        printf("\n  <start> (int)  = Initial time-quantum (usecs)");
        printf("\n  <end>   (int)  = Final time-quantum (usecs)");
        printf("\n  <inc>   (int)  = How much time quantum is incremented by each simulation (usecs)");
        printf("\n\n");

        // Finish
        exit(EXIT_SUCCESS);
    }

    // Get option char and check it
    char opt = 'A';
    if (argcount >= 3) {
        opt = argvalue[2][0];
        if (!isValidOption(opt)) {
            printf("\n\nTracefile option exists in alternate reality. Use quantum realm :D \n\n");
            exit(EXIT_FAILURE);
        }
    }
    // Get numbers
    int start = 100;
    if (argcount >= 4) {
        start = atoi(argvalue[3]);
    }
    int end = 100;
    int inc = 100;
    if (argcount >= 5) {
        end = atoi(argvalue[4]);
    }
    if (argcount >= 6) {
        inc = atoi(argvalue[5]);
    }

    // Decide on mode
    char folder[50];
    if (strcmp(argvalue[1], "M") == 0) {

        // Mono mode
        printf("\n$$$$ Mono mode");

        // Mono mode path
        strcpy(folder, "mono");

        // Run project
        runProj(folder, opt, start, start, start);

    } else if (strcmp(argvalue[1], "F") == 0) {
        // Full mode
        printf("\n$$$$ Full mode");

        // Full mode path
        strcpy(folder, "full");

        // Run project
        runProj(folder, opt, start, end, inc);
    }

    // Check comparison switch
    if (strcmp(argvalue[argcount - 1], "-c") == 0) {
        runCompMode(folder, opt);
    }
}

/*
 * Entry point
 */
int main(int argcount, char *argvalue[])
{
    // Print information about proxy when no args entered
    if (argcount == 1) {
        printInfo();
    } else {
        // Run desired mode
        runDesiredMode(argcount, argvalue);
    }



    // Exit
    return EXIT_SUCCESS;
}
