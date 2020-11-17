/* 
 * File:   main.c
 * Author: David
 * 
 * An demonstration of C99 programming concepts
 * Functions come first, then main method is at bottom
 *
 * Created on July 11, 2019
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

// Import header
#include "demoh.h"

/*
 * A function demonstration looping and printing
 */
void loopAndPrintDemo(void)
{
    // Start message
    printf("Loop and Print Demo");
    printf("\n");

    // Initialize local int
    int num = 2;

    // Loop three times
    for (int i = 0; i < 3; i++) {

        // Print 1st parameter, fill in using 2nd onwards
        printf("Num: %d, String: %s, Float: %f", num, "Gold", 1.5);

        // Multiply num by 2
        num *= 2;

        // Make space
        printf("\n");
    }

    // Make space
    printf("\n");
}

/*
 * Demonstrates simple pointer use
 */
void pointerDemo(void)
{
    // Start message
    printf("Simple Pointer Demo");
    printf("\n");

    // Declare variables of type 'pointer to int'
    // Note: Don't do = int* A,B,C;
    int* A;
    int* B;
    int* C;

    // Declare an int
    int X;

    // Allocate memory the size of an int to the pointers (essential)
    A = malloc(sizeof(int));
    B = malloc(sizeof(int));
    C = malloc(sizeof(int));

    // Dereference the pointer (access its value) and set its value
    *A = 225;
    *B = 70;
    *C = 65;

    // Set the int's value
    X = 4;

    // Make the value of B equal to the value of A
    *B = *A;

    // Get the address of int X and assign it to the C pointer
    C = &X;

    // Directly assign 5 to C, which points to X, changing X also
    *C = 5;

    // Print out values
    printf("A:%d \n", *A);
    printf("B:%d (was 70, became value of A) \n", *B);
    printf("C:%d (was 65, now points to X, 5 was directly assigned) \n", *C);
    printf("X:%d \n", X);

    // Free the memory associated with the pointer 
    free(A);
    free(B);
    // free(C); We cannot free after assigning via & ??

    // Make space
    printf("\n");
}

/*
 * Demonstrates the use of arrays
 */
void arrayDemo(void)
{
    // Start message
    printf("Array Demo");
    printf("\n");

    // First way = 'Literal array, Java way'
    // Initialization
    int myArray[4] = {0, 25, 50, 100};
    // Use sizeof trick to get length = Total memory/Size of one 
    int len = sizeof myArray / sizeof myArray[0];
    // Iteration 
    for (int i = 0; i < len; i++) {
        printf("A1: %d", myArray[i]);
        printf("\n");
    }

    printf("\n");

    // Second Way = Using a pointer
    // State length
    int LENGTH = 3;
    // Create variable of type 'pointer to long'
    long* ap;
    // Assign how much mem is needed = One cell x Length
    ap = malloc((sizeof(long)) * LENGTH);
    // Initialize values
    ap[0] = 12;
    ap[1] = 3;
    ap[2] = 36;
    // Iterate
    for (int i = 0; i < LENGTH; i++) {
        printf("A2: %ld", ap[i]);
        printf("\n");
    }

    // Free mem
    free(ap);

    // Make space
    printf("\n");

}

/**
 * Get user input from console
 */
void scanDemo()
{
    // Start message
    printf("Scan Demo");
    printf("\n");

    // Make space for input
    int i;

    // First part = Data type format strings
    // Second part = Give address to input
    printf("Enter: ");
    scanf("%d", &i);

    // Return value
    printf("Your value ^2 is: %d", i * i);

    // Make space
    printf("\n");
    printf("\n");

}

/**
 * Type definition test
 * They are like renamed versions of existing data types
 */
void typeDefDemo(void)
{
    // Start message
    printf("TypeDef Demo");
    printf("\n");

    typedef int dog;
    int a = 2;
    dog d = 6;

    // Print result
    printf("Dog + int: %d", d + a);

    // Make space
    printf("\n");
    printf("\n");

}

void stringDemo()
{
    // Start message
    printf("String Demo");
    printf("\n");

    // Make string
    char thanos[10] = "THANOS";
    printf("\nString is: %s", thanos);

    // Print length
    printf("\n Len: %zd", strlen(thanos));
    printf("\n");

    // Compare string
    printf("\nString compared to STEVER: ");
    printf("\n ASCIIDiff: %d", strcmp(thanos, "A"));
    printf("\n");

    // Concatenate string
    printf("\nString concatenation: ");
    printf("\n Conc: %s", strcat(thanos, " + Stones"));
    printf("\n");

    // Copy string (RIGHT TO LEFT)
    printf("\nString copy: Gauntlet <- Thanos");
    char gauntlet[15] = "GAUNTLET";
    strcpy(gauntlet, thanos);
    printf("\n Gauntlet is now: %s", gauntlet);
    printf("\n");

    // Functions not in C99
    // strrev
    // strupr
    // strlwr

    // Make space
    printf("\n");
    printf("\n");
}

void specialDemo(void)
{
    // True is 1
    printf("\n");
    if (true == 1) {
        printf("true is true");
    }

    // Char
    printf("\n");
    for (char ch = 'a'; ch <= 'd'; ch = ch + 1) {
        printf("\nChar is: %c", ch);
    }

    // Space
    printf("\n");
    printf("\n");
}

/*
 * Main function
 */
int main(void)
{
    loopAndPrintDemo();
    pointerDemo();
    arrayDemo();
    typeDefDemo();
    stringDemo();
    scanDemo();
    specialDemo();

    exit(EXIT_SUCCESS);
}