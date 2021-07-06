#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int main(int argcount, char *argvalue[])
{
    // welcome
    char string[200] = "I'm David!";
    printf("\nHi there! %s \n", string);
    printf("\n");
    
    // print args
    for(int i = 0; i < argcount; i++)
    {
    	printf("\n%ith arg = %s", i, argvalue[i]);
    }
    
    // space
    printf("\n");
    printf("\n");
    
    return 0;
}
