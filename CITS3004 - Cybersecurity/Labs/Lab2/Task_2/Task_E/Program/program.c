/*
 * CITS3004
 * Name: Crypto Library
 * Authors: Other UWA Lecturers, Alvaro Monsalve
 * Last Update: 08 JUL 2021
 * Openssl version 1.1.1
 * https://www.openssl.org/docs/man1.1.1/man3/EVP_EncryptInit.html
 */

// Import C libraries
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

// Import special crypto library
#include <evp.h>


// ### Global Fixed Variables
// The plaintext (total 21 characters)
unsigned char *plaintext = "This is a top secret.";
int plaintext_len = 21;

// The ciphertext (in hex format)
unsigned char *ciphertext = "8d20e5056a8d24d0462ce74e4904c1b513e10d1df4a2ef2ad4540fae1ca0aaf9";

// Define the max line length
#define MAX_SIZE 1000


// ### Word Lines Information
char** lines; // Array of strings
int storedLines = 0; // Line count


// ### Function declarations
// Step 1
void readInWords(char fpath[]);
void refineLine(char str[]);
// Step 2
void convertToKey(char* word, unsigned char* key);
// Step 3
int encrypt(unsigned char *key, unsigned char *ct_out);

/**
 * Main function, entry point
 * @return 
 */
int main() {

    // Start message
    printf("\n### CITS3004 Lab 2 - Task E ###\n");

    // ### STEP 1 = GET DICTIONARY
    readInWords("../Files/words.txt");

    // For every word/line in dictionary
    for (int curLine = 0; curLine < storedLines; curLine++) {

        // ### STEP 2 - CONVERT WORD TO KEY
        char* curKey = malloc(17);
        convertToKey(lines[curLine], curKey);

        // ### STEP 3 - USE KEY TO ENCRYPT PLAINTEXT AND PRODUCE CIPHERTEXT
        unsigned char cur_ct_raw[100]; // Holder
        int cur_ct_len = encrypt(curKey, cur_ct_raw);

        // ### STEP 4 - COMPARE CIPHERTEXT PRODUCED TO GIVEN CIPHERTEXT
        // - Convert to hex
        char cur_ct_hex[50];
        for (int cti = 0; cti < cur_ct_len; cti++) {
            sprintf(cur_ct_hex + cti * 2, "%02x", cur_ct_raw[cti]);
        }
        cur_ct_hex[cur_ct_len * 2] = '\0';

        // - Compare to given ciphertext
        if (strcmp(cur_ct_hex, ciphertext) == 0) {
            printf("Key found!!!: %s\n", curKey);
            break;
        }
    }

    // Space
    printf("\n\n");

    // Exit successfully
    return EXIT_SUCCESS;
}

/**
 * Helper function for STEP 1
 * Read in the dictionary words (the potential keys)
 * @param fpath The file path to words.txt
 */
void readInWords(char fpath[]) {
    // Try to open file for reading
    FILE* file = fopen(fpath, "r");

    // Check for file open errors
    if (file == NULL) {
        printf("Could not open: %s \n\n", fpath);
        exit(EXIT_FAILURE);
    }

    //  Get the number of lines in the file
    int numLines = 0;
    char buf[MAX_SIZE];
    while (fgets(buf, sizeof buf, file) != NULL) numLines++;

    // Rewind file
    rewind(file);

    // Initialize lines holder
    lines = malloc(sizeof (char) * numLines * MAX_SIZE);

    // Check allocation
    if (lines == NULL) {
        printf("\n Allocation issue");
    }

    // Get lines and store them
    for (int i = 1; i <= numLines; i++) {

        // Allocate memory to current line
        lines[storedLines] = malloc(sizeof (char) * MAX_SIZE);

        // Get into current line
        fgets(lines[storedLines], MAX_SIZE, file);

        // Remove any extra chars
        refineLine(lines[storedLines]);

        // Move to next line
        storedLines++;
    }

    // Close the file
    fclose(file);

    // Space
    printf("\n");
}

/**
 * Helper function for STEP 1
 * Remove line endings to strip and sanitize string
 * @param str String to be modified
 */
void refineLine(char str[]) {
    // Counter
    int i = 0;

    // While null byte not reached
    while (str[i] != '\0') {

        // Replace first line ending with null byte
        if (str[i] == '\r' || str[i] == '\n') {
            str[i] = '\0';
            break;
        }
        i++;
    }
}

/**
 * Helper function for STEP 2
 * Convert a word to a key by adding padding
 * @param word Original word
 * @param out The key (The padded word)
 */
void convertToKey(char* word, unsigned char* key) {
    char* temp = malloc(17);
    strncpy(temp, word, 17);
    int len = strlen(temp);
    if (len < 17) {
        for (int i = len; i < 17; i++) {
            temp[i] = 0x20;
        }
    }
    memcpy(key, temp, 17);
}

/**
 * Helper function for STEP 3
 * Use a given key to encrypt the plaintext
 * Put the produced ciphertext in ct_out
 * @param key The current key
 * @param ct_out The ciphertext produced
 */
int encrypt(unsigned char *key, unsigned char *ct_out) {
    // Initialize cipher
    EVP_CIPHER_CTX *ctx;
    if (!(ctx = EVP_CIPHER_CTX_new())) {

        // If error happens:
        printf("Issue!: EVP_CIPHER_CTX_new");
        EVP_CIPHER_CTX_free(ctx);
        return 0;
    }

    // Initialize IV
    unsigned char iv[17];
    memset(iv, 0, 17);

    // Do AES-128-CBC encryption
    if (!EVP_EncryptInit_ex(ctx, EVP_aes_128_cbc(), NULL, key, iv)) {

        // If error happens:
        printf("Issue!: EVP_EncryptInit_ex");
        EVP_CIPHER_CTX_free(ctx);
        return 0;
    }

    // Do update
    int len;
    int ciphertext_len;
    if (!EVP_EncryptUpdate(ctx, ct_out, &len, plaintext, plaintext_len)) {

        // If error happens:
        printf("Issue!: EVP_EncryptUpdate");
        EVP_CIPHER_CTX_free(ctx);
        return 0;
    }
    ciphertext_len = len;

    // Finalize
    if (!EVP_EncryptFinal_ex(ctx, ct_out + len, &len)) {

        // If error happens:
        printf("Issue!: EVP_EncryptFinal_ex");
        EVP_CIPHER_CTX_free(ctx);
        return 0;
    }
    ciphertext_len += len;

    // Free memory
    EVP_CIPHER_CTX_free(ctx);

    // Return CT length
    return ciphertext_len;
}
