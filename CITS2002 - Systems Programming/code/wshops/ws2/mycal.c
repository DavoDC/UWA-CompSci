/* 
 * File:   mycal.c
 * Author: David
 *
 * Created on August 10, 2019, 7:09 AM
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

// Month array - static???
char* months[12] = {"January", "February", "March", "April",
    "May", "June", "July", "August",
    "September", "October", "November", "December"};

/*
 * Returns a pointer to a specified part of a string
 */
char* substring(char *string, int position, int length)
{
    // Make pointer and allocate memory
    char *pointer;
    pointer = malloc(sizeof(char) * length + 1);

    // Add characters
    int c;
    for (c = 0; c < length; c++) {
        *(pointer + c) = *(string + position - 1);
        string++;
    }

    // Add null to close string
    *(pointer + c) = '\0';

    // Return result
    return pointer;
}

/*
 * Turns 3 letter month into full version
 */
char* makeFull(char *mon)
{
    // Return correct string
    for (int i = 0; i < 12; i++) {
        // Get 3 letter version of full
        char* curMon = substring(months[i], 1, 3);

        // Check for match
        if (strcmp(curMon, mon) == 0) {
            return months[i];
        }
    }

    return "Error";
}

/*
 * Generate day numbers on rightmost side
 */
int* generateFinalDays(int start)
{
    // Make pointer
    int* finals;
    finals = malloc(sizeof(int) * 5);

    // Add first value
    finals[0] = start;

    // Generate values
    int mult = 0;
    for (int i = 1; i < 5; i++) {
        // Get previous value and add 7
        finals[i] = finals[i - 1] + 7;
        mult++;
    }

    // Return array
    return finals;
}

/*
 * Print calendar
 */
void printCal(void)
{
    // FEATURES TO ADD
    // - GET DAYS
    // - GET EMPTY START DAYS
    // - GET FIRST TOP RIGHT
    // - MONTH AS ARGUMENT 
    // - CURRENT DAY HIGHLIGHT (if input month = current month)

    // Get time information
    time_t t = time(NULL);
    char* time = asctime(localtime(&t));

    // Print month and year
    printf("\n    ");
    char* month = substring(time, 5, 3);
    month = makeFull(month);
    char* year = substring(time, 21, 4);
    printf("%s %s", month, year);


    // Print day name header
    printf("\nSu Mo Tu We Th Fr Sa\n");

    // Print month-specific spaces
    int emptyStartDays = 4;
    while (emptyStartDays != 0) {
        printf("   ");
        emptyStartDays--;
    }

    // Get days in month
    int totalDays = 31;
    // CODE FOR GETTING DAYS
    //    if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12 )
    //  {
    //  	printf("\n 31 Days in this Month");  	
    //  }
    //  else if ( month == 4 || month == 6 || month == 9 || month == 11 )
    //  {
    //  	printf("\n 30 Days in this Month");  	
    //  }  
    //  else if ( month == 2 )
    //  {
    //  	printf("\n Either 28 or 29 Days in this Month"); 

    // Generate final days in month (right most side)
    int firstTopRight = 3; // Get first top right day in month
    int* finalDays = generateFinalDays(firstTopRight);

    // Print day numbers
    int i;
    for (i = 1; i <= totalDays; i++) {
        // Add space for single digits
        if (i < 10) {
            printf(" ");
        }

        // Print day number
        printf("%i ", i);

        // Go to next line on final days 
        for (int j = 0; j < 5; j++) {
            if (finalDays[j] == i) {
                printf("\n");
            }
        }
    }

    // Space
    printf("\n");
}

/*
 * Main function
 */
int main(int argc, char** argv)
{
    // Info
    printf("\n++++ Workshop 2 - 'cal' simulator ++++ \n\n");
    printf("\nArg = Month number or nothing for current");

    // process input
    //Enter 

    // Actual function
    printCal();

    // Exit
    return(EXIT_SUCCESS);
}

