#include <cnet.h>
#include <stdlib.h>
#include <string.h>

/*
 * This is an implementation of a stop-and-wait data link protocol.
    It is based on Tanenbaum's `protocol 4', 2nd edition, p227.
    This protocol employs only data and acknowledgement frames -
    piggybacking and negative acknowledgements are not used.

    It is currently written so that only one node (number 0) will
    generate and transmit messages and the other (number 1) will receive
    them. This restriction seems to best demonstrate the protocol to
    those unfamiliar with it.
    The restriction can easily be removed by "commenting out" the line

            if(nodeinfo.nodenumber == 0)

    in reboot_node(). Both nodes will then transmit and receive (why?).

    Note that this file only provides a reliable data-link layer for a
    network of 2 nodes.
 */

/*
 * Types of frames
 */
typedef enum {
    DL_DATA, DL_ACK
} FRAMEKIND;

/*
 * Models data
 */
typedef struct {
    char data[MAX_MESSAGE_SIZE];
} MSG;

/*
 * Frame with header then data
 */
typedef struct {
    // Header
    FRAMEKIND kind; // only ever DL_DATA or DL_ACK
    size_t len; // the length of the msg field only
    int checksum; // checksum of the whole frame
    int seq; // only ever 0 or 1, ordering/ID of Data or Ack

    // DC: Added pending DL_ACK
    int ackSeq; // -1 not a piggyback, 0 or 1 is 

    MSG msg;
} FRAME;

// Macros for sizes
#define FRAME_HEADER_SIZE  (sizeof(FRAME) - sizeof(MSG))
#define FRAME_SIZE(f)      (FRAME_HEADER_SIZE + f.len)

// Global accounting variables
MSG *lastmsg;
size_t lastlength = 0;
CnetTimerID lasttimer = NULLTIMER;
int ackexpected = 0;
int nextframetosend = 0;
int frameexpected = 0;

// DC: Piggybacking variables
CnetTimerID paTimer = NULLTIMER;
int paSeq = -1;

/**
 * Helper method for transmitting frame
 * @param msg
 * @param kind
 * @param length
 * @param seqno
 */
void transmit_frame(MSG *msg, FRAMEKIND kind, size_t length, int seqno) {

    // Create frame
    FRAME f;
    int link = 1;
    f.kind = kind;
    f.seq = seqno;
    f.checksum = 0;
    f.len = length;

    // Act based on frame type
    switch (kind) {

        case DL_ACK:
            // For ACKs, notify but don't add data
            printf("ACK transmitted, seq=%d\n", seqno);
            break;

        case DL_DATA:
        {
            // If DL_ACK pending, save in outgoing frame
            f.ackSeq = paSeq;
            CNET_stop_timer(paTimer);

            // Reset time
            CnetTime timeout;

            // Notify
            printf(" DATA transmitted, seq=%d\n", seqno);
            printf(" Piggyback seq=%d\n", paSeq);

            // Add data to frame
            memcpy(&f.msg, msg, (int) length);

            // Set timer
            timeout = FRAME_SIZE(f)*((CnetTime) 8000000 / linkinfo[link].bandwidth) +
                    linkinfo[link].propagationdelay;

            // Start timer
            lasttimer = CNET_start_timer(EV_TIMER1, 3 * timeout, 0);
            break;
        }
    }

    // For all frame types:
    // Calculate full checksum and save
    length = FRAME_SIZE(f);
    f.checksum = CNET_ccitt((unsigned char *) &f, (int) length);

    // Write frame down to physical layer
    CHECK(CNET_write_physical(link, &f, &length));

    // Reset paSeq
    paSeq = -1;
}

/**
 * When application (layer above) is ready 
 * @param 
 * @return 
 */
EVENT_HANDLER(application_ready) {

    // Get data from application
    CnetAddr destaddr;
    lastlength = sizeof (MSG);
    CHECK(CNET_read_application(&destaddr, lastmsg, &lastlength));

    // Make application stop sending data
    CNET_disable_application(ALLNODES);

    // Notify
    printf("down from application, seq=%d\n", nextframetosend);

    // Send frame down to physical
    transmit_frame(lastmsg, DL_DATA, lastlength, nextframetosend);
    nextframetosend = 1 - nextframetosend;
}

/**
 * When physical layer (layer below) is ready
 * @param 
 * @return 
 */
EVENT_HANDLER(physical_ready) {

    // Get frame 
    FRAME f;
    size_t len;
    int link, checksum;
    len = sizeof (FRAME);
    CHECK(CNET_read_physical(&link, &f, &len));

    // Get copy of checksum and reset actual
    checksum = f.checksum;
    f.checksum = 0;

    // Check checksum
    if (CNET_ccitt((unsigned char *) &f, (int) len) != checksum) {
        // Ignore frame if checksum bad, notify
        printf("\t\t\t\tBAD checksum - frame ignored\n");
        return;
    }

    // Act based on kind of frame RECEIVED
    switch (f.kind) {

            // If ACK received
        case DL_ACK:
            // If sequence is good
            if (f.seq == ackexpected) {
                // Notify ack was received
                printf("\t\t\t\tACK received, seq=%d\n", f.seq);

                // Stop timer
                CNET_stop_timer(lasttimer);

                // Decreased ackexpected
                ackexpected = 1 - ackexpected;

                // Let application send again
                CNET_enable_application(ALLNODES);
            }
            break;

            // If data received
        case DL_DATA:

            // Process piggybacked ACKs
            // If piggybacked Ack is valid
            if (f.ackSeq == ackexpected) {
                // Notify ack was received
                printf("\t\t\t\tPiggybacked ACK received, seq=%d\n", f.ackSeq);
                
                // Stop timer
                CNET_stop_timer(lasttimer);

                // Move to next ack
                ackexpected = 1 - ackexpected;

                // Let application send again
                CNET_enable_application(ALLNODES);
            }

            // Notify
            printf("\t\t\t\tDATA received, seq=%d, ", f.seq);
            // If sequence is good
            if (f.seq == frameexpected) {
                // Notify
                printf("up to application\n");
                len = f.len;
                // Write data up to application
                CHECK(CNET_write_application(&f.msg, &len));
                frameexpected = 1 - frameexpected;

                // DC:
                // Instead of sending immediately, 
                // save the pending ack sequence for later
                paSeq = f.seq;
                printf("Saved ACK seqno for later\n");

                // Start timer
                paTimer = CNET_start_timer(EV_TIMER2, 10000000, 0);

            } else {
                printf("ignored\n");
            }

            break;
    }
}

/**
 * When timeout occurs
 * @param 
 * @return 
 */
EVENT_HANDLER(timeouts) {
    // Notify
    printf("timeout, seq=%d\n", ackexpected);

    // Send frame
    transmit_frame(lastmsg, DL_DATA, lastlength, ackexpected);
}

/**
 * When timeout occurs
 * @param 
 * @return 
 */
EVENT_HANDLER(pendack) {
    // Notify
    printf("DC piggyback timeout, sent lone ACK q=%d\n", paSeq);

    // When pending ACK timer expires, send pending ACK on its own
    transmit_frame(NULL, DL_ACK, 0, paSeq);

    // Reset sequence number
    paSeq = -1;
}

/**
 * Show state
 * @param 
 * @return 
 */
EVENT_HANDLER(showstate) {
    printf(
            "\n\tackexpected\t= %d\n\tnextframetosend\t= %d\n\tframeexpected\t= %d\n",
            ackexpected, nextframetosend, frameexpected);
}

/**
 * Setup node
 * 
 * @param 
 * @return 
 */
EVENT_HANDLER(reboot_node) {
    if (nodeinfo.nodenumber > 1) {
        fprintf(stderr, "This is not a 2-node network!\n");
        exit(1);
    }

    lastmsg = calloc(1, sizeof (MSG));

    CHECK(CNET_set_handler(EV_APPLICATIONREADY, application_ready, 0));
    CHECK(CNET_set_handler(EV_PHYSICALREADY, physical_ready, 0));
    CHECK(CNET_set_handler(EV_TIMER1, timeouts, 0));
    CHECK(CNET_set_handler(EV_DEBUG0, showstate, 0));

    // DC: Added second timer
    CHECK(CNET_set_handler(EV_TIMER2, pendack, 0));

    CHECK(CNET_set_debug_string(EV_DEBUG0, "State"));

    CNET_enable_application(ALLNODES);
}
