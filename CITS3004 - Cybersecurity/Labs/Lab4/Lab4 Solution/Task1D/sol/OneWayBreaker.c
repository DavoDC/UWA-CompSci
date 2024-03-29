/*
CITS 3004
Name: One Way Breaker (Brute Force Attach for Hash MD5)
Authors: Other UWA Lecturers, Alvaro Monsalve
Last Update: 14 AUG 2021

Notes:  Openssl version 1.1.1
        https://www.openssl.org/docs/man1.1.1/man3/EVP_DigestInit.html
        You can modify the below code to get started, or you are welcome to write from scratch
*/
#include<stdlib.h>
#include<stdio.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<fcntl.h>
#include<openssl/evp.h>

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

int compare(const char *a, const char *b, int len)
{
	int i;
	//compare first len bytes of a and b
	for(i=0; i<len; i++)
	{
		if(a[i] != b[i])
			return 1;
	}
	return 0; //if they are the same
}

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

void display(char *data, int len)
{
	//print out len bytes memory
	int i = 0;
	for(;i<len;i++)
	{
		printf("%02X ", (unsigned char)data[i]);
	}
	return;
}

int main(int argc, char** argv)
{
	if(argc <2)
	{
		printf("Usage: owb AttemptCount\n");
		return 0;
	}
	//srand(time(NULL));
	srand(1);
	char target[16];
	char data[16];
	char target_hash[16];
	char data_hash[16];
	int i, j;

	int attempt = atoi(argv[1]);
	int count = 0;
	int *statistics;
	statistics = (int *)malloc(attempt * sizeof(int));
	
	EVP_MD_CTX *ctx_t;
	EVP_MD_CTX *ctx_d;

	while(count<attempt)
	{
		/*
		1. Create a Message m (Let's call it Target)
		*/
		for(i=0;i<4;i++)
		{	
			((int *)target)[i] = rand();
			//printf("target[%d] = %d\n", i, ((int *)target)[i]);
		}
		
		/*
		2. Create a Random Data Message (Let's call it data)
		*/
		for(j=0;j<4;j++)
		{
			((int *)data)[j] = rand();
			//printf("data[%d] = %d\n", j, ((int *)data)[j]);			
		}

		/*
		3. Hash the Target message with MD5 of 16 bytes
		     Ouput: target_hash 
		*/
		int m, n, o, o_len, c;
		c = 0; //store how many attemps has done for one specific hash value
		
		ctx_t = EVP_MD_CTX_new();
		EVP_MD_CTX_init(ctx_t);
		m = EVP_DigestInit_ex(ctx_t, EVP_md5(), NULL);
		n = EVP_DigestUpdate(ctx_t, target, 16);
		o = EVP_DigestFinal_ex(ctx_t, target_hash, &o_len); //Get target hash value
		//printf("o_len=%d",o_len);
		
		/*
		4. Compare (only first 3 bytes) of target_hash with 
		   the MD5 Hash of data (Note data is incremented by 1 in each while iteration)
		   
		   The while loop stops if target_hash == data_hash
		*/
		while(compare(target_hash, data_hash, 3)) //keep looping until find a match
		{
			int p, q, r, r_len;
			ctx_d = EVP_MD_CTX_new();
			EVP_MD_CTX_init(ctx_d);
			p = EVP_DigestInit_ex(ctx_d, EVP_md5(), NULL);
			q = EVP_DigestUpdate(ctx_d, data, 16);
			r = EVP_DigestFinal_ex(ctx_d, data_hash, &r_len);
			EVP_MD_CTX_free(ctx_d);
			add((unsigned int *)data, 4);
			c++;
		}

		/*
		5. Print the total number of comparisons
		*/
		printf("\n* Hash value Match : tried for %d times\n", c);
		printf("  Original message : ");
		display(target, 16);
		printf("\n  Generated message: ");
		display(data, 16);
		//printf("\n                     ");
		//display(data+16, 16);		// if data[32]
		printf("\n  Hash Value:        ");
		display(target_hash,3);

		EVP_MD_CTX_free(ctx_t);
		statistics[count]=c;
		count++;
	}
	
	double avg_time = avg(statistics, attempt); //calculate average number
	printf("\n===================================================\n");
	printf("Tried %lf times on average for each hash value. \n", avg_time);
	return 0;
}
	
