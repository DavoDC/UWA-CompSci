/*
CITS 3004
Name: Collision Free Breaker (Brute Force Attack for Hash MD5)
Authors: Other UWA Lecturers, Alvaro Monsalve
Last Update: 14 AUG 2021

Notes:  
Openssl version 1.1.1
https://www.openssl.org/docs/man1.1.1/man3/EVP_DigestInit.html
You can modify the below code to get started, or you are welcome to write from scratch
*/


// Import
#include<stdlib.h>
#include<stdio.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<fcntl.h>
#include<openssl/evp.h>
#include<string.h>



// Function declarations
int compareAll(const char *list, const char *data, int total, int unit_len, int len);
void add(unsigned int *data, int len);
int compare(const char *a, const char *b, int len);
double avg(const int *data, int count);
void display(char *data, int len);



// Main function, entry point
int main(int argc, char** argv)
{

	// If name arg and attempt count are present
	if(argc < 2) {
		printf("Usage: cfb AttemptCount\n");
		return 0;
	}

	// Set and print bytes to match
	int bytesToMatch = 3; // Default
	if(argc == 3) {
		// Usage: cfb AttemptCount BytesToMatch
		bytesToMatch = atoi(argv[2]);
	}
	printf("bytesToMatch: %i (out of 16)", bytesToMatch);
	

	// Local variables
	srand(1);
	char data[16];
	char dataBackup[16];
	char hashHistory[1024*1024];
	char data_hash[16];
	int i, j;
	int attempt = atoi(argv[1]);
	int count = 0;
	int *statistics;
	statistics = (int *)malloc(attempt * sizeof(int));
	EVP_MD_CTX *ctx_d;


	// For all attempts
	while(count<attempt)
	{
		/*
		1. 
		Create a Random Data Message (Let's call it data)
		Outputs: data and dataBackup
		*/
		for(i=0;i<4;i++)
		{	
			((int *)dataBackup)[i] = ((int *)data)[i] = rand();
		}
	
		/*
		2. 
		Compare a certain amount of bytes of data_hash with the MD5 Hash of hashHistory 
		The while loop stops if hashHistory contains the X bytes of data_hash
		*/
		int c, index;
		// Store how many attemps has done for one specific hash value
		c = 0; 
		// Keep looping until there is a collision
		while(index=compareAll(hashHistory, data_hash, c-1, 16, bytesToMatch) < 0) 
		{
			/*
			2. 
			Save data_hash in the hashHistory
			(Note data is incremented by 1 in each while iteration)
			*/
			if (c > 0)
			{
				bcopy(data_hash, &hashHistory[16*(c-1)], 16);
				add((unsigned int*)data, 4);
			} 
			// not match, so store the previous result
			c++;

			/*
			3. 
			Hash the Data message with MD5 of 16 bytes
			Ouput: data_hash 
			*/
			int p, q, r, r_len;
			ctx_d = EVP_MD_CTX_new();
			EVP_MD_CTX_init(ctx_d);
			p = EVP_DigestInit_ex(ctx_d, EVP_md5(), NULL);
			q = EVP_DigestUpdate(ctx_d, data, 16);
			r = EVP_DigestFinal_ex(ctx_d, data_hash, &r_len);
			EVP_MD_CTX_free(ctx_d);
		}


		/*
		4. Print the total number of comparisons
		*/
		printf("\n* Hash value Match:  calculated for %d times\n", c);
		printf("   Original Message:      ");
		display(dataBackup, 16);
		printf("\n   Generated Message:     ");
		display(data, 16);
		printf("\n   Hash Value:            ");
		display(data_hash,bytesToMatch);


		statistics[count]=c;
		count++;
	}
	
	// Calculate average number
	double avg_time = avg(statistics, attempt); 
	printf("\n===================================================\n");
	printf("Tried %lf times on average for each hash value. \n", avg_time);
	return 0;
}



/*
TASK
Write a function to compare (data) and all entries in (list)'s first (len) bytes
Return the index of item matched, otherwise return -1
Hint: use a for-loop until you reach the end of the list
Hint: use the function compare already given to you to compare bytes of length (len)

(list) contains multiple history entries appended at the end without any overhead
(data) is the current entry you are comparing with the (list) items
(total) is the length of (list)
(unit_len) is the length of each entry
(len) is the length of bytes you are comparing between (list)'s entries and (data)
*/
int compareAll(const char* list, const char* data, int total, int unit_len, int len) {

	// For each item in list    ((total) is the length of (list))
	for(int i = 0; i < total; i++) {

		// If match found
		if(compare(list, data, len) == 0) {

			// Return index
			return i;
		}

		// Increment list pointer by length of each entry
		list += unit_len;
	}

	// If no match, return -1
	return -1; 
}



// Compare two strings
int compare(const char *a, const char *b, int len) {

	// Compare first len bytes of a and b
	for(int i = 0; i < len; i++) {

		// If mismatch found
		if(a[i] != b[i]) {
			return 1;
		}
	}

	// If they are the same
	return 0; 
}





// Helper function to add 1 using bit operations
void add(unsigned int *data, int len)
{
	//treat 4*len bytes memory as one large number and perform add 1
	int p, carry;
	p = len - 1;
	carry = 1;

	while(carry ==1 && p >= 0)
	{
		if(data[p]!=0xffffffff)
		{
			data[p]++;
			carry = 0;
		}
		else
		{
			data[p]=0x00000000;
			carry = 1;
			p--;
		}
	}
	return;
}


// Find average value from an array of values
double avg(const int *data, int count)
{
	//calculate count numbers' average value
	int i;
	double avg = 0;
	for(i=0;i<count;i++)
	{
		avg = avg*((double)i/(double)(i+1))+(double)(data[i])/(double)(i+1);
	}
	return avg;
}






// Print out hex value with spaces
void display(char *data, int len)
{
	//print out len bytes memory
	int i = 0;
	for(;i<len;i++)
	{
		printf("%02x ", (unsigned char)data[i]);
	}
	return;
}

