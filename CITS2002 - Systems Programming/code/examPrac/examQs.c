#define _GNU_SOURCE 1
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>

#include <sys/stat.h>
#include <sys/wait.h>
#include <dirent.h>
#include <time.h>

/*
 * 2018-1 Q1
 */
bool subString(char* haystack, char* needle)
{
    // Testing message
    printf("\n subStr is looking for %-5s in %-5s", needle, haystack);

    // Check empty status of each
    bool emptyH = *haystack == '\0';
    bool emptyN = *needle == '\0';

    // If both are empty
    if (emptyH && emptyN) {

        // Return true as empty contains empty
        return true;
    } else if (emptyH || emptyN) {

        // Return false as the substring property cannot be satisified
        return false;
    }

    // Iterate over all characters in the haystack
    while (*haystack != '\0') {
        // Get copies
        char* copyH = strdup(haystack); // A section
        char* copyN = strdup(needle); // Full string

        // Number of character matches
        int matches = 0;

        // Iterate over all characters in the copies
        while (*copyN != '\0' && *copyH != '\0') {

            // Count character matches
            if (*copyN == *copyH) {
                matches++;
            }

            // Move copies ahead
            copyN++;
            copyH++;
        }

        // If there is a full match
        if (matches == strlen(needle)) {
            return true;
        }

        // Move to the next haystack character
        haystack++;
    }

    // No matches found
    return false;
}

/**
 * Print out information of an entry using stat()
 * @param entrypath The path of the entry
 * @return True if the entry was a directory
 */
void printEntryInfo(char entrypath[])
{
    // Initialize stat structure
    struct stat statBuf;
    if (stat(entrypath, &statBuf) != 0) {
        fprintf(stderr, "\n stat() failed \n\n");
        exit(EXIT_FAILURE);
    }

    // Check type of entry 
    bool isDir = S_ISDIR(statBuf.st_mode);
    bool isFile = S_ISREG(statBuf.st_mode);

    // Notify about entry type
    if (isDir) {
        printf("\n %s is a directory", entrypath);
    } else if (isFile) {
        printf("\n %s is a file", entrypath);
    } else {
        fprintf(stderr, "\n Unknown entry type found \n\n");
        fprintf(stderr, "\n Path: %s \n\n", entrypath);
        exit(EXIT_FAILURE);
    }

    // Print general entry information
    printf("\n stat structure fields:");
    printf("\n  st_mode:  %u", statBuf.st_mode);
    printf("\n  st_size:  %li", statBuf.st_size);
    printf("\n  st_mtime: %s", ctime(&statBuf.st_mtim.tv_sec));
    printf("  st_atime: %s", ctime(&statBuf.st_atim.tv_sec));
}

/*
 * 2018-2 Q6 + extra 
 */
void printDirInfo(char *dirName, int* nFiles, int* nDirs)
{
    // Open directory
    DIR* dirp = opendir(dirName);

    // If open failed
    if (dirp == NULL) {
        fprintf(stderr, "\n opendir() failed \n\n");
        exit(EXIT_FAILURE);
    }

    // File path holder
    char fullpath[PATH_MAX];

    // Directory entry holder
    struct dirent* dp;

    // While another entry exists
    while ((dp = readdir(dirp)) != NULL) {

        // Skip recursive entries
        if (strcmp(dp->d_name, ".") == 0 || strcmp(dp->d_name, "..") == 0) {
            continue;
        }

        // Get full entry path
        sprintf(fullpath, "%s/%s", dirName, dp->d_name);

        // Print entry info
        printEntryInfo(fullpath);

        // If entry is a directory
        if (dp->d_type == DT_DIR) {
            // Increase directory count
            *nDirs = *nDirs + 1;
            // Check directory
            printDirInfo(fullpath, nFiles, nDirs);

        } else if (dp->d_type == DT_REG) {
            // Else if it was a file, 
            // increase file count
            *nFiles = *nFiles + 1;
        }
    }

    // Close the directory
    closedir(dirp);
}

/*
 * 2017 Q6
 */
int totalBytes(char* dirname, time_t since)
{
    // Open directory and check
    DIR* dirp = opendir(dirname);
    if (dirp == NULL) {
        fprintf(stderr, "\n opendir() failed \n\n");
        exit(EXIT_FAILURE);
    }

    // Byte local variable
    int bytes = 0;

    // Full path
    char fullpath[PATH_MAX];

    // Iterate over directory entries
    struct dirent* dp;
    while ((dp = readdir(dirp)) != NULL) {

        // Skip recursive entries
        if (strcmp(dp->d_name, ".") == 0 || strcmp(dp->d_name, "..") == 0) {
            continue;
        }

        // Get full path
        sprintf(fullpath, "%s/%s", dirname, dp->d_name);

        // TEST
        printEntryInfo(fullpath);

        // Get attributes
        struct stat statbuf;
        stat(fullpath, &statbuf);

        // If it is a file (both ways so I remember both)
        if (S_ISREG(statbuf.st_mode) && dp->d_type == DT_REG) {
            // If modification time is greater than since
            if (statbuf.st_mtim.tv_sec >= since) {

                // Add size
                bytes += statbuf.st_size;
            }

        } else if (S_ISDIR(statbuf.st_mode) && dp-> d_type == DT_DIR) {

            // Else if it is a directory
            // Add size of lower directory
            bytes += totalBytes(fullpath, since);
        }

    }

    // Close the directory
    closedir(dirp);

    // Return bytes found
    return bytes;
}

/*
 * Return an array of tokens
 */
char** tokenize(char* str, char* sep, int* parts)
{
    // Make holder and check
    char** tokens = malloc(sizeof(char*) * strlen(str));
    if (tokens == NULL) {
        exit(EXIT_FAILURE);
    }

    // Get first token using string dup of string
    char* curToken = strtok(strdup(str), sep);

    // Iterate
    int part = 0;
    while (curToken != NULL) {

        // Save and move forward
        tokens[part] = strdup(curToken);
        part++;

        // Get next token
        curToken = strtok(NULL, sep);
    }

    // Terminate
    tokens[part] = NULL;

    // Assign to parts
    *parts = part;

    // Return holder
    return tokens;
}

/*
 * Free tokens
 */
void freeTokens(char** tokens)
{
    if (tokens != NULL) {

        while (*tokens != NULL) {
            free(*tokens);
            tokens++;
        }

        free(tokens = 0);
    }
}

/*
 * 2018-1 Q3
 */
int executeUsingPath(char* progpath, char* args[])
{
    // Get program name - the last token
    int parts = 0;
    char** tokens = tokenize(progpath, "/", &parts);
    char* progname = strdup(tokens[parts - 1]);
    freeTokens(tokens);

    // Tokenize the PATH variable
    int numpaths;
    char** paths = tokenize(getenv("PATH"), ":", &numpaths);

    // While a path exists
    while (*paths != NULL) {

        // Get full path
        char fullpath[PATH_MAX]; // MAXPATHLEN for Chris
        sprintf(fullpath, "%s/%s", *paths, progname);

        // Fork process
        switch (fork()) {
            case -1:
                // Fork failed
                printf("\n fork() failed\n");
                exit(EXIT_FAILURE);
                break;
            case 0:
                // Child actions 

                // Overlay child with new prog
                execv(fullpath, args);

                break;
            default:
            {
                // Parent actions

                // Parent is waiting on child
                int status;
                wait(&status); // Returns child PID

                // Return exit status of child
                return WEXITSTATUS(status);
                exit(EXIT_SUCCESS);
                break;
            }
        }

        // Move to next path
        paths++;
    }

    // No program found
    return -1;
}

/*
 * 2018-1 Q2
 */
typedef struct
{
    char*** grid;
    int rows;
    int cols;
} GRID;

GRID init_grid(int rows, int cols)
{
    GRID g;
    g.rows = rows;
    g.cols = cols;

    // Initialize full grid
    g.grid = calloc(rows, sizeof(char**) * cols);
    if (g.grid == NULL) {
        perror(__func__);
        exit(EXIT_FAILURE);
    }

    // Initialize row
    for (int r = 0; r < rows; r++) {
        g.grid[r] = calloc(1, sizeof(char**));
        if (g.grid[r] == NULL) {
            perror(__func__);
            exit(EXIT_FAILURE);
        }

        // TEST = Add random string
        for (int c = 0; c < cols; c++) {
            char str[100];
            sprintf(str, "str%i%i", r, c);
            g.grid[r][c] = strdup(str);
        }
    }

    return g;
}

void print_grid(GRID g)
{
    printf("\n");
    for (int r = 0; r < g.rows; r++) {
        for (int c = 0; c < g.cols; c++) {
            printf(" %s ", g.grid[r][c]);
        }
        printf("\n");
    }
}

void swap_col(GRID g, int sCol, int dCol)
{
    // Go down the columns
    for (int i = 0; i < g.rows; i++) {
        // Save destination col
        char* destEntry = g.grid[i][dCol];

        // Destination col becomes source col
        g.grid[i][dCol] = g.grid[i][sCol];

        // Source col becomes destination col
        g.grid[i][sCol] = destEntry;
    }
}

void free_grid(GRID g)
{
    for (int r = 0; r < g.rows; r++) {
        free(g.grid[r]);
    }

    free(g.grid);
}

/*
 * 2018-1 Q6
 */
int removeDir(char* dirname)
{
    // Open dir
    DIR* dirp = opendir(dirname);
    if (dirp == NULL) {
        perror(__func__);
        return -1;
    }

    // Full path holder
    char fullpath[PATH_MAX]; // MAXPATHLEN for Chris

    // Iterate over directory entries
    struct dirent* dp;
    while ((dp = readdir(dirp)) != NULL) {

        // Skip recursive directories
        if (strcmp(dp->d_name, ".") == 0 ||
                strcmp(dp->d_name, "..") == 0) {
            continue;
        }

        // Get full path
        sprintf(fullpath, "%s/%s", dirname, dp->d_name);

        // Get stat info
        struct stat statBuf;
        stat(fullpath, &statBuf);

        // If a file found 
        if (S_ISREG(statBuf.st_mode) && dp->d_type == DT_REG) {
            // Unlink file
            unlink(fullpath);

        } else if (S_ISDIR(statBuf.st_mode) && dp->d_type == DT_DIR) {
            // Else If directory found

            // Remove files then folder
            removeDir(fullpath);
            rmdir(fullpath);
        }
    }

    // Delete original directory and save result
    int result = rmdir(dirname);

    // Close directory
    closedir(dirp);

    // Return result
    return result;
}

/*
 * 2018-2 Q2
 */
char** makeSentences(char* chapter, int* numSent)
{
    // Tokenize chapter
    int tokenNum;
    char** tokens = tokenize(chapter, ". ", &tokenNum);

    // Make holders
    char** sentences = malloc((tokenNum + 1) * sizeof(char*));

    if (sentences == NULL) {
        exit(EXIT_FAILURE);
    }

    int sentCount = 0;

    // Iterate over parts
    while (*tokens != NULL) {
        // Increase number of sentences
        sentCount++;

        // Get current sentence holder (+1 for dot)
        char curSent[strlen(*tokens) + 2];

        // Fill holder
        sprintf(curSent, "%s.", *tokens);

        // Copy in pointer
        *sentences = strdup(curSent);

        // Check memory
        if (*sentences == NULL) {
            perror(__func__);
            exit(EXIT_FAILURE);
        }

        // Move to next sentence
        sentences++;

        // Move to next token
        tokens++;
    }

    // Free tokens
    freeTokens(tokens);

    // Assign num of sentence
    *numSent = sentCount;

    // Return holder
    return(sentences - sentCount);
}

char* makeChapter(char** sentences, int numSent)
{
    // Make holder
    char* chapter = strdup("");
    if (chapter == NULL) {
        return NULL;
    }

    // A running total of the chapter size
    int totalSize = strlen(chapter);

    // For all sentences
    for (int i = 0; i < numSent; i++) {

        // Add to total size (+2 for null byte and space);
        totalSize += strlen(sentences[i] + 2);

        // Expand memory for chapter by new total
        chapter = realloc(chapter, totalSize);
        if (chapter == NULL) {
            return NULL;
        }

        // Add sentence and space
        strcat(chapter, sentences[i]);
        strcat(chapter, " ");
    }

    // Return holder
    return chapter;
}

/*
 * 2018-2 Q1
 */
bool isSubset(int set1[], int len1, int set2[], int len2)
{
    // Handle empty case
    if (len1 == 0 || len2 == 0) {
        return false;
    }

    // For all spots in set1
    for (int i = 0; i < len1; i++) {

        // Indices for each set
        int ind1 = i;
        int ind2 = 0;

        // Match count
        int matches = 0;

        // While indices are valid
        while (ind1 < len1 && ind2 < len2) {

            // Test
            printf("\n s1: %i s2: %i", set1[ind1], set2[ind2]);

            // Check for a match
            if (set1[ind1] == set2[ind2]) {
                matches++;
            }

            // If a full match was found
            if (matches == len2) {
                return true;
            }

            // Move indices forward
            ind1++;
            ind2++;
        }
        
        printf("\n");
    }

    // No subset found
    return false;
}

/*
 * Entry point
 */
int main(int argcount, char *argvalue[])
{
    // Start message
    printf("\n Welcome, David, ");
    printf("\n you are so amazing that you got 4 HDs in Sem 2 2019 easily!");
    printf("\n");

    // Testing

    // Substring testing
    //        printf(" , returned %i (true == %i)",
    //                subString("superman", "supe"), true);
    //        printf(" , returned %i (true == %i)",
    //                subString("superman", "man"), true);
    //        printf(" , returned %i (true == %i)",
    //                subString("superman", "ermm"), true);

    // countEntries + printEntryInfo testing
    // Holders
    //    int nFiles;
    //    int nDirs;
    //
    //    // Run on first arg
    //    printDirInfo("../pre", &nFiles, &nDirs);
    //
    //    // Print final results
    //    printf("\n\n Files found: %i ,  Dirs found: %i  ", nFiles, nDirs);

    // totalBytes testing
    //    if (argcount >= 2) {
    //        // Run 
    //        int bytes = totalBytes(argvalue[1], 1465597839);
    //
    //        // Print results
    //        printf("\n\n Bytes: %i ", bytes);
    //    }


    // executeUsingPath testing
    //    char* args[] = { "ls", "-lha", NULL};
    //    printf(" Return value: %i", executeUsingPath("ls", args));

    // Grid testing
    //    GRID g = init_grid(3, 3);
    //    print_grid(g);
    //    swap_col(g, 0, 2);
    //    print_grid(g);
    //    free_grid(g);

    // Remove dir testing
    //    printf("\n Before: \n");
    //    fflush(stdout);
    //    system("cd .. && cp -r pre preCp && ls && ls preCp && ls preCp/demo");
    //    int retV = removeDir("../preCp");
    //    printf("\n Ran removeDir, returned: %i \n", retV);
    //    printf("\n After: \n");
    //    fflush(stdout);
    //    system("cd .. && ls && ls preCp && ls preCp/demo");
    //    system("cd .. && rm -r preCp");

    // Make Sentence testing
    //    char test[] = "Dog. Frog. Cat. ";
    //    printf("\n makeSentences(%s,...):", test);
    //    int numSent;
    //    char** sentences = makeSentences(test, &numSent);
    //    for (int i = 0; i < numSent; i++) {
    //        printf("\n %i : %s", i, sentences[i]);
    //    }
    //    // Make Chapter testing
    //    printf("\n\n makeChapter with sentences above");
    //    char* chapter = makeChapter(sentences, numSent);
    //    printf("\n Result: %s", chapter);


    // Subset testing
    int set1[] = {1, 2, 3, 4, 5};
    int set2[] = {4, 5};
    bool subC = isSubset(set1, 5, set2, 2);
    printf("\n Result: %i (true == %i)", subC, true);


    // Finish successfully
    printf("\n\n");
    exit(EXIT_SUCCESS);
}
