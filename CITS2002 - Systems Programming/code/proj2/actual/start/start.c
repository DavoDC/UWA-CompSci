#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>
#include <sys/time.h>

/*
 * Print the time left until the project's completion date
 */
void printTimeLeft(void)
{
    // Get current time in milliseconds
    struct timeval now;
    gettimeofday(&now, NULL);
    long nowVal = now.tv_sec * 1000;

    // The due date time in milliseconds
    // (Thu 17 Oct 2019 - 20:00 - 8pm)
    long dueVal = 1571313600000;

    // Difference in milliseconds
    long diff = dueVal - nowVal;

    // End case
    if (diff <= 0) {
        printf("\nProject finished successfully and amazingly! ");
        return;
    }

    // Calculate components
    long days = diff / (24 * 60 * 60 * 1000);
    long hours = diff / (60 * 60 * 1000) % 24;
    long mins = diff / (60 * 1000) % 60;

    // Print difference
    printf("\nTime until Project Completion = ");
    printf("%ld days %ld hrs %ld mins", days, hours, mins);
}

/*
 * Entry point
 */
int main(void)
{
    // Title
    printf("\n$$$$ Welcome to Project 2 (We got 38/40) $$$$");
    printf("\n");

    // Time left message
    printTimeLeft();

    // Make space
    printf("\n\n");

    // Exit successfully
    return EXIT_SUCCESS;
}
