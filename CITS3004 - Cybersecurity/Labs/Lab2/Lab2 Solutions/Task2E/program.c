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

int do_crypt(char *outfile)
{
    unsigned char outbuf[1024];
    int outlen, tmplen;
    /*
     * Bogus key and IV: we'd normally set these from
     * another source.
     */
    unsigned char key[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    unsigned char iv[] = {1,2,3,4,5,6,7,8};
    char intext[] = "Some Crypto Text";
    EVP_CIPHER_CTX *ctx;
    FILE *out;

    ctx = EVP_CIPHER_CTX_new();
    EVP_EncryptInit_ex(ctx, EVP_idea_cbc(), NULL, key, iv);

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
    out = fopen(outfile, "wb");
    if (out == NULL) {
        /* Error */
        return 0;
    }
    fwrite(outbuf, 1, outlen, out);
    fclose(out);
    return 1;
 }


int main()
{
    //create the main function
	//create a char length 17 (our limit)
	//open a file containing the list of words
	//for each word, use it as a key to see if the ciphertext matches
	//once you found a word that matches the ciphertext, print

	//your code goes here
	printf("The Program starts here:");

	char* outfile = "Encryption.bin";
	do_crypt(outfile);

	return 0;
}
