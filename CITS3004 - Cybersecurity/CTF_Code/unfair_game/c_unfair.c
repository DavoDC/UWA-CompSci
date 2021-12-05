#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

int main(int argc, char **argv) {

    // P.S - Don't need use arguments

    // Seed PRNG using time
    srand(time(NULL));

    // Open file for writing
    FILE *f = fopen("nums.txt", "w");

    // P.S - Don't call rand here otherwise it'll incremenent the PRNG
    
    // Changed limit
    for(int i = 0; i < 200; i++) {
        fprintf(f, "%d\n", rand());
    }
}

