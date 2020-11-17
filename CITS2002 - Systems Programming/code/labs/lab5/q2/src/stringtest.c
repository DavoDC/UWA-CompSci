/*
 * Using the example strlen() and strcpy() functions of Lecture 15 as a guide, 
 * write pairs of functions to implement versions of the following standard C functions:
 * a. strcat
 * b. strcmp
 * c. strncmp
 * d. strchr
 * e. strrchr
 * 
 * Maintain a C source files  for the pointer-based versions 
 * (named pointers.c).
 *
 * Develop a separate header file containing the prototypes of the pointer-based functions
 * (named pointers.h).
 
 * Create a single testing program named stringtest.c, 
 * containing the only main() function for this question, 
 * that tests each of the functions you write. 
 
 * The file stringtest.c should include the header file
 *
 * Approach the writing of each function by following these steps:
 *
 * i. Firstly, read the appropriate online Unix manual entry for 
 * each function (e.g. run man strcat). Pay particular attention to the 
 * parameters of the function and its return value. Note that the
 * online documentation will frequently use the (as yet not explained) 
 * keywords const and restrict. 
 * For now, simply ignore these and do not reproduce them in your functions 
 * (ask a demonstrator if interested in what they're for).
 * 
 * ii. Next, develop a version of each function .
 * 
 * ii. Write a short test function in stringtest.c file 
 * that calls your new function several times to test its execution. 
 * Depending on the function being tested, suitable parameters to the function
 * will include simple short strings, long strings, empty strings, identical strings 
 * (when comparing them), very different strings, 
 * strings differing in only a single character, 
 * and strings containing, or otherwise, a required character (for strchr).
 * 
 */
#include "stringtest.h"

// Args
#define FUNC_COUNT 5
char funcArgs[][10] = {"cat", "cmp", "Ncmp", "chr", "Rchr"};

// Strings for testing
char empty[] = "";
char oneC[] = "A";
char shortS[] = "Dog";
char longS[] = "Supercalifornia";
char sim1S[] = "Bellboy";
char sim2S[] = "Bellsoy";

void testStrRChr(char* str, char c)
{
    // Print parameters
    char start[2000];
    sprintf(start, "%s, %c", str, c);
    printf("\n %-20s", start);

    // Get copies
    char str1[strlen(str) + 1];
    strcpy(str1, str);
    char str2[strlen(str) + 1];
    strcpy(str2, str);

    // Call ours
    printf("\n %-10s %-15s", "my_strrchr:", my_strrchr(str1, (int) c));

    // Call official
    printf("\n %-11s %-15s", "strrchr:", strrchr(str2, (int) c));

    // Space
    printf("\n");
}

void testStrChr(char* str, char c)
{
    // Print parameters
    char start[2000];
    sprintf(start, "%s, %c", str, c);
    printf("\n %-20s", start);

    // Get copies
    char str1[strlen(str) + 1];
    strcpy(str1, str);
    char str2[strlen(str) + 1];
    strcpy(str2, str);

    // Call ours
    printf("\n %-10s %-15s", "my_strchr:", my_strchr(str1, (int) c));

    // Call official
    printf("\n %-10s %-15s", "strchr:", strchr(str2, (int) c));

    // Space
    printf("\n");
}

void testStrNCmp(char* s1, char* s2, int num)
{
    // Print result along with strings used
    char start[2000];
    sprintf(start, "%s, %s -> %i", s1, s2, my_strncmp(s1, s2, num));
    printf("\n %-50s", start);

    // Check my result for correctness
    if (strncmp(s1, s2, num) == my_strncmp(s1, s2, num)) {
        printf("CORRECT!");
    } else {
        printf("DIFFERS! (Was %i)", strncmp(s1, s2, num));
    }
}

void testStrCmp(char* s1, char* s2)
{
    // Print result along with strings used
    char start[2000];
    sprintf(start, "%s, %s -> %i", s1, s2, my_strcmp(s1, s2));
    printf("\n %-50s", start);

    // Check my result for correctness
    if (strcmp(s1, s2) == my_strcmp(s1, s2)) {
        printf("CORRECT!");
    } else {
        printf("DIFFERS! (Was %i)", strcmp(s1, s2));
    }
}

void testStrCat(char* s1, char* s2)
{
    // Get copies (to preserve original strings)
    char c1[strlen(s1) + 1];
    strcpy(c1, s1);
    char c2[strlen(s2) + 1];
    strcpy(c2, s2);

    // Save the result of my function
    char result[1000];
    strcpy(result, my_strcat(c1, c2));

    // Print result along with strings used
    char start[2000];
    sprintf(start, "%s, %s -> %s", c1, c2, result);
    printf("\n %-50s", start);

    // Check my result for correctness
    if (strcmp(result, strcat(c1, c2)) == 0) {
        printf("CORRECT!");
    } else {
        printf("DIFFERS!");
    }
}

void usage(void)
{
    printf("\n\n Arg Issue - Please check usage \n\n");
}

void testFunc(char arg[])
{
    if (strcmp(arg, "all") == 0) {
        for (int i = 0; i < FUNC_COUNT; i++) {
            testFunc(funcArgs[i]);
        }
    } else if (strcmp(arg, funcArgs[0]) == 0) {

        printf("\n my_strcat tests:");
        testStrCat(shortS, sim1S);
        testStrCat(sim1S, shortS);
        testStrCat(longS, shortS);
        testStrCat(oneC, shortS);
        testStrCat(shortS, oneC);
        testStrCat(shortS, empty);

    } else if (strcmp(arg, funcArgs[1]) == 0) {

        printf("\n my_strcmp tests:");
        testStrCmp(sim2S, sim1S);
        testStrCmp(sim1S, sim2S);
        testStrCmp(shortS, sim1S);
        testStrCmp(sim1S, shortS);
        testStrCmp(longS, shortS);
        testStrCmp(shortS, oneC);
        testStrCmp(shortS, empty);

    } else if (strcmp(arg, funcArgs[2]) == 0) {

        printf("\n my_strncmp tests:");
        for (int i = 0; i < 10; i++) {
            printf("\n n=%i", i);
            testStrNCmp(sim2S, sim1S, i);
            testStrNCmp(sim1S, sim2S, i);
            testStrNCmp(shortS, sim1S, i);
            testStrNCmp(longS, shortS, i);
            testStrNCmp(shortS, oneC, i);
            testStrNCmp(shortS, empty, i);
            printf("\n");
        }

    } else if (strcmp(arg, funcArgs[3]) == 0) {

        printf("\n my_strchr tests:");
        testStrChr(longS, 'W');
        testStrChr(longS, 'S');
        testStrChr(longS, 'a');
        testStrChr(longS, 'c');
        testStrChr(shortS, 'g');

    } else if (strcmp(arg, funcArgs[4]) == 0) {

        printf("\n my_strrchr tests:");
        testStrRChr(longS, 'W');
        testStrRChr(longS, 'S');
        testStrRChr(longS, 'a');
        testStrRChr(longS, 'c');

    } else {

        // Invalid arg
        usage();
    }

    // Make space
    printf("\n");
    printf("\n");
}

/*
 * Entry point
 */
int main(int argNo, char** argval)
{
    // Info
    printf("\n++++ Lab5 - Q2 - String Functions \n");
    printf("\n Usage:");
    printf("\n ./stringtest [ all ||");
    for (int i = 0; i < FUNC_COUNT; i++) {
        printf(" %s", funcArgs[i]);
        if (i != FUNC_COUNT - 1) printf(" ||");
        else if (i == FUNC_COUNT - 1) printf(" ]");
    }
    printf("\n");

    // Check args
    if (argNo != 2) {
        usage();
        exit(EXIT_FAILURE);
    }

    // Run function
    testFunc(argval[1]);

    // Exit
    exit(EXIT_SUCCESS);
}
