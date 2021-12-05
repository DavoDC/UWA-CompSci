/*
CITS 3004
Name: Collision Free Breaker (Brute Force Attach for Hash MD5)
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
#include<string.h>

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


///You are to write this function below

int compareAll(const char *list, const char *data, int total, int unit_len, int len)
{
	//compare data and all entries in list's first len bytes, unit_len is the entry's length
	int i = 0;
	for(;i< total; i++)
	{
		int j;
		j = compare(&list[i*unit_len], data, len);
		if(j==0)
			return i; //if there is a match, return index
	}
	return -1; // no match, return -1
}

int main(int argc, char** argv)
{
	if(argc <2)
	{
		printf("Usage: cfb AttemptCount\n");
		return 0;
	}
	//srand(time(NULL));
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

	while(count<attempt)
	{
		/*
		1. Create a Random Data Message (Let's call it data)
		   Outputs: data and dataBackup
		*/
		for(i=0;i<4;i++)
		{	
			((int *)dataBackup)[i] = ((int *)data)[i] = rand();
		}
	
		/*
		2. Compare (only first 3 bytes) of data_hash with 
		   the MD5 Hash of bashHistory 
		   
		   The while loop stops if hashHistory contains the 3 bytes of data_hash
		*/
		int c, index;
		c = 0; //store how many attemps has done for one specific hash value
		
		while(index=compareAll(hashHistory, data_hash, c-1, 16, 3)<0) //keep looping until there is a collision
		{
			/*
			2. Save data_hash in the hashHistory
			   (Note data is incremented by 1 in each while iteration)
			*/
			if (c > 0)
			{
				bcopy(data_hash, &hashHistory[16*(c-1)], 16);
				add((unsigned int*)data, 4);
			} //not match, so store the previous result

			c++;

			/*
			3. Hash the Data message with MD5 of 16 bytes
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
		display(data_hash,3);

		statistics[count]=c;
		count++;
	}
	
	double avg_time = avg(statistics, attempt); //calculate average number
	printf("\n===================================================\n");
	printf("Tried %lf times on average for each hash value. \n", avg_time);
	return 0;
}
	
