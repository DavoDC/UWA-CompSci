
#ifndef DATAGRAMHANDLER_H
#define DATAGRAMHANDLER_H

#include "stringfix.h"
#include "RGProc.h"

class DatagramHandler {
public:

    // Construction
    DatagramHandler();
    DatagramHandler(const DatagramHandler& orig);
    virtual ~DatagramHandler();

    // Methods
    void init();
    void receiveDatagrams();
    void sendNeighboursInfo();
    void backtrackSG(StatGram);
    String discoverRoute(String);
    void sendStatgram(StatGram, int);
    void sendSGtoNeighbors(StatGram, bool, bool);

private:

    // Server socket file descriptor
    int serverSock;

    // Route gram processor
    RGProc rgp;

    // Get stat gram
    StatGram getStatGram();
};

#endif /* DATAGRAMHANDLER_H */

