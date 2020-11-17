#include "stringtest.h"

/*
 * Appends the string pointed to by s1 to the end of the string pointed to by s2
 */
char* my_strcat(char* s1, char* s2)
{
    // Get combined len + null byte
    int combLen = strlen(s1) + strlen(s2) + 1;

    // Create string pointer
    char* res = malloc(sizeof(char) * combLen);

    // EASY WAY (WORKS)
    // Copy both strings in with sprintf
    //sprintf(res, "%s%s", s1, s2);

    // MANUAL WAY (WORKS)
    // Copy in first string
    strcpy(res, s1);
    // Copy in second string
    //  Move pointer up
    res += strlen(s1);
    //  Until second has not reached nullbyte
    while (*s2 != '\0') {
        *res = *s2;
        res++;
        s2++;
    }
    // Terminate result
    *res = '\0';
    // Rewind result
    res -= combLen - 1;

    // Return result
    return res;
}

/*
 * Takes two strings as parameters and returns an integer value 
 * based on the comparison of strings.
 * Starts comparison character by character starting 
 * from the first character until the characters 
 * in both strings are equal or a NULL character is encountered.
 * If first character in both strings are equal, 
 * then this function will check the second character, 
 * if this is also equal then it will check the third and so on...
 * This process will be continued until a character in 
 * either string is NULL or the characters are unequal.
 */
int my_strcmp(char* s1, char* s2)
{
    // Loop while:
    // - Characters are equal
    // - Null byte has not been reached
    while (*s1 == *s2 && *s1 != '\0' && *s2 != '\0') {
        // Move both strings to the next character
        s1++;
        s2++;
    }

    // Return the difference between 
    // the first pair of differing characters
    return *s1 - *s2;
}

/*
 * Same as strcmp, but has a byte limit
 * Remember one byte == one char
 */
int my_strncmp(char* s1, char* s2, int n)
{
    // Return 0 when n is 0
    if (n == 0) return 0;

    // Bytes checked
    int bytes = 0;

    // Loop while:
    // - Characters are equal
    // - Null byte has not been reached
    // - Byte limit has not been reached
    while (*s1 == *s2 && *s1 != '\0' && *s2 != '\0' && bytes != n - 1) {
        // Move both strings to the next character
        s1++;
        s2++;

        // Increase bytes checked
        bytes++;
    }

    // Return the difference pair of different character
    return *s1 - *s2;
}

/*
 * Searches for the first occurrence of the character ch
 * (an unsigned char) in the string pointed to by the argument str
 * Returns a pointer to the first occurrence of the 
 * character c in the string str, or NULL if the character is not found.
 */
char* my_strchr(char* str, int chNum)
{
    // Get character
    char ch = (char) chNum;

    // Iterate until 
    // - different character found
    // - str termination reached
    while (*str != ch && *str != '\0') {

        // Move string to the next character
        str++;
    }

    // Return null if nothing found
    if (*str == '\0') {
        return NULL;
    }

    // Otherwise return shifted string
    return str;
}

/*
 * Locates the last occurrence of a character in a string. 
 * It returns a pointer to the last occurrence in the string. 
 * The terminating null character is considered part of the C string. 
 * Therefore, it can also be located to retrieve a pointer to the end of a string.
 */
char* my_strrchr(char* str, int chNum)
{
    // Get character
    char inC = (char) chNum;

    // Get first char
    char firstC = *str;

    // Put string forward
    str += strlen(str);

    // Iterate until 
    // - different character found
    // - front of string reached
    while (*str != inC && *str != firstC) {

        // Move string BACK one
        str--;
    }

    // If finishes on inputted char
    if (*str == inC) {
        // Return as strings match
        return str;
    }

    // If finishes on first char
    if (*str == firstC) {

        // Return string as nothing found
        return NULL;
    }

    // Otherwise return shifted string
    return str;
}