
# Had to add some libs
#include <stdio.h>
#include <string.h>
#include <stdlib.h>


# Compiling the programs
# cc -o static/rawudp static/rawudp.c

# cc -o static/rawtcp static/rawtcp.c


# Running commands
# Syntax:
# rawXcp [source IP] [source port] [dest IP] [dest port]

# 1st
# 1. Generate and capture a raw TCP packet 
# from source 1.2.3.4 port 20001 
# to destination 5.6.7.8 port 20002.
# sudo ./static/rawtcp 1.2.3.4 20001 5.6.7.8 20002



# 2nd
# 2. Generate and capture a raw UDP packet 
# from source 1.2.3.4 port 20001 
# to destination 5.6.7.8 port 20002.
# ./static/rawudp
sudo ./static/rawudp 1.2.3.4 20001 5.6.7.8 20002





