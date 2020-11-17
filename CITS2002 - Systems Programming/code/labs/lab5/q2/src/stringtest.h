#ifndef STRINGTEST_H
#define STRINGTEST_H
// The above prevents the header from being duplicated during compilation

// Common headers
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>


// String function declarations
// Note: There are NO variable names

// Concatenation
char* my_strcat(char*, char*);

// Compare functions
int my_strcmp(char*, char*);
int my_strncmp(char*, char*, int);

// Char 
char* my_strchr(char*, int);
char* my_strrchr(char*, int);


#endif