/*
CITS 3004
Name: Crypto Library
Authors: Other UWA Lecturers, Alvaro Monsalve
Last Update: 08 JUL 2021

Notes:  Openssl version 1.1.1
        https://www.openssl.org/docs/man1.1.1/man3/EVP_EncryptInit.html
        You can modify the below code to get started, or you are welcome to write from scratch
*/
#include <evp.h>
#include <stdio.h>
#include <string.h>

void KeyGen(char* in, char* out)
{
	int i = 0;
	char* temp = malloc(17);
    strncpy(temp, in, 17);
    int len = strlen(temp);
    if (len < 17) {
        for (i = len; i < 17; i++) {
            temp[i] = 0x20;
        }
    }
    memcpy(out, temp, 17);
	
	//Your code goes here
}


int do_crypt(char *outfile, char* keystring)
{
    unsigned char outbuf[1024];
	int outlen, tmplen;
    /* Bogus key and IV: we'd normally set these from
    * another source.
    */
    unsigned char key[17]; //set key to be 17
	KeyGen(keystring, key); //generate key

	// Initialize the iv here
    unsigned char iv[16] = {0};//you need to edit this line //iv all set to zero
    char intext[] = "This is a top secret.";
    EVP_CIPHER_CTX *ctx;
    FILE *out;

    ctx = EVP_CIPHER_CTX_new();
	
	//Initialise the encryption
    EVP_EncryptInit_ex(ctx, EVP_aes_128_cbc(), NULL, key, iv);//you need to populate the variables

    if (!EVP_EncryptUpdate(ctx, outbuf, &outlen, intext, strlen(intext))) {
        /* Error */
        EVP_CIPHER_CTX_free(ctx);
        return 0;
    }
    /*
    * Buffer passed to EVP_EncryptFinal() must be after data just
    * encrypted to avoid overwriting it.
    */
    if (!EVP_EncryptFinal_ex(ctx, outbuf + outlen, &tmplen)) {
        /* Error */
        EVP_CIPHER_CTX_free(ctx);
        return 0;
    }
    outlen += tmplen;
    EVP_CIPHER_CTX_free(ctx);
    /*
    * Need binary mode for fopen because encrypted data is
    * binary data. Also cannot use strlen() on it because
    * it won't be NUL terminated and may contain embedded
    * NULs.
    */

    int i; //loop index
	int rightFlag = 0; //flag  for exact match
	
	//fill out the expected cipher text
	unsigned char result[32] = {0x8d, 0x20, 0xe5, 0x05, 0x6a, 0x8d, 0x24, 0xd0, 0x46, 0x2c, 0xe7, 0x4e, 0x49, 0x04, 0xc1, 0xb5, 0x13, 0xe1, 0x0d, 0x1d, 0xf4, 0xa2, 0xef, 0x2a, 0xd4, 0x54, 0x0f, 0xae, 0x1c, 0xa0, 0xaa, 0xf9};

		
	//write a test to check whether the cipertext matches with the result (hint: compare individual bytes using for-loop)
	//your code goes here
    for (i = 0; i < outlen; i++) {
        if (result[i] == outbuf[i]) {
            rightFlag = 1;
        } else {
            rightFlag = 0;
            break;
        }
    }

	if (rightFlag == 0) {
	    return 0;
	} else {
        out = fopen(outfile, "wb");
        fwrite(outbuf, 1, outlen, out);
        fclose(out);
        return 1;
    }
}

int main()
{
	//create the main function
	//create a char length 17 (our limit)
	//open a file containing the list of words
	//for each word, use it as a key to see if the ciphertext matches
	//once you found a word that matches the ciphertext, print

	//your code goes here
	
	unsigned char nextWord[50];
	FILE* wordFile;
	char* answerFile = "answer.txt";
	wordFile = fopen("words.txt", "r");
	
	while (fscanf(wordFile, "%s\n", nextWord) != EOF) {
	    if (do_crypt(answerFile, nextWord)) {
	        printf("The key is %s\n", nextWord);
	        break;
	    }
	}
	
	return 0;
}

