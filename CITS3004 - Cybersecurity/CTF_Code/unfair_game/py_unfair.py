#!/usr/bin/env python

from pwn import *
from subprocess import *
import os

# Connect to CTF site
c = remote('cits4projtg.cybernemosyne.xyz', 1001)

# Receive initial printout with instructions etc. 
c.recvlines(numlines=17)
# P.S - Don't need to send initial wrong one

# Compile C program automatically (ensure latest exec being used)
os.system("gcc -o c_unfair c_unfair.c")

# Execute C program to get numbers
exit_code = os.system('./c_unfair')

# For every number in file
for line in open("nums.txt", "r").readlines():

    try:

        # Send number
        c.sendline(bytes(line[:-1], 'utf-8'))

        # Get output
        line1 = str(c.recvline(), 'UTF-8')
        line2 = str(c.recvline(), 'UTF-8')
        line3 = str(c.recvline(), 'UTF-8')
        line4 = str(c.recvline(), 'UTF-8')
        line5 = str(c.recvline(), 'UTF-8')
        # Note: 5 is maximum we can get here

        # Print output
        print("1: ", line1, end='')  
        print("2: ", line2, end='')     
        print("3: ", line3, end='')    
        print("4: ", line4, end='')    
        print("5: ", line5, end='')    

        # Get combined
        full = str(line1+line2+line3+line4+line5)

        # Check if 100th round reached (winning round)
        if("100" in full):
            print("WON!!\n\n\n\n")
            # Print/receive more lines so that answer is printed out
            print(c.recvline())
            print(c.recvline())
            print(c.recvline())
            print(c.recvline())
            print(c.recvline())
            print(c.recvline())
            print(c.recvline())
            print(c.recvline())
            print(c.recvline())
            print(c.recvline())

    except Exception as e:
        # Hide EOF and ConnError exceptions
        pass


