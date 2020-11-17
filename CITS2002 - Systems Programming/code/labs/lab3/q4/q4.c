/*
 * Each call to the standard C99 function rand() 
 * returns a different random integer. 
 * Running the same program multiple times 
 * results in exactly the same sequence of random integers.
 * While this is generally unexpected ("hey, they are not random"!), 
 * it is very helpful for debugging programs without the randomness.
 * We can provide each execution with a more random sequence 
 * of random numbers by seeding the random number generator
 * with the C statement srand( time(NULL) );
 * 
 * Write a simple program to fill an array of 10 integers with random numbers from rand(). 
 * Run the program several times, printing the contents of the array.
 * 
 * Now, use srand() to seed the generation of random numbers. 
 * Run the program several times, printing the contents of the array.
 * 
 * Extend your program by passing the initialised array to 
 * another function which finds and prints the largest value in the array.
 * 
 * Finally, extend the program's function to place the array's 
 * largest value into the array's first element (index position 0),
 * "pushing" all other values down in the array (0→1, 1→2, 2→3, and so on).
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <time.h>

// Global int
int size = 7;

void printArr(int* ints)
{
    printf("\n");

    for (int i = 0; i < size; i++) {
        printf("\n Pos %i = %i", i, ints[i]);
    }

    printf("\n");
}

int printLargest(int* array)
{
    // Find max
    int max = array[0];
    int maxPos = 0;
    for (int i = 1; i < size; i++) {
        int cur = array[i];
        if (cur > max) {
            max = cur;
            maxPos = i;
        }
    }

    // Print max
    printf("\n");
    printf("\nMax value of %i was at pos %i", max, maxPos);

    // Return maxPos
    return maxPos;
}

void makeLargestFirst(int maxPos, int* array)
{
    // Bring largest to front
    int index = maxPos;
    int max = array[maxPos];
    while (array[0] != max) {
        int temp = array[index - 1];
        array[index - 1] = array[index];
        array[index] = temp;
        index--;
    }

    // Print new array
    printf("\n\nNew Array after moving Max up: ");
    printArr(array);
}

void randTest(void)
{
    // Array
    int array[size];

    // Seed with time
    srand(time(NULL));

    // Fill array with random nums
    for (int i = 0; i < size; i++) {
        array[i] = rand();
    }

    // Print array info
    printArr(array);
    int maxPos = printLargest(array);
    makeLargestFirst(maxPos, array);

    // Space
    printf("\n\n\n\n");
}

int main(int argcount, char *argvalue[])
{
    // Info
    printf("\n++++ Lab3 - Q4 - Random Numbers\n");
    printf("\n Arg1: Array Size \n");

    // Check input
    if (argcount != 2) {
        printf("\n Please enter one number: array size \n\n");
        exit(EXIT_FAILURE);
    }

    // Process input
    size = atoi(argvalue[1]);

    // Run further tests
    randTest();

    // Exit
    exit(EXIT_SUCCESS);
}
