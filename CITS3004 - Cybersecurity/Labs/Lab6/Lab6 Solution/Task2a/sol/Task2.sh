gcc -o rawtcp rawtcp.c
gcc -o rawudp rawudp.c
gcc -o sniffex sniffexPort.c -lpcap
./sniffex

#In another terminal:

# check open ports with: 
#sudo ss -tulw

#Generate rawtcp packets
#./rawtcp 1.2.3.4 20001 5.6.7.8 20002

#Generate rawudp packets
#./rawudp 1.2.3.4 20001 5.6.7.8 20002

