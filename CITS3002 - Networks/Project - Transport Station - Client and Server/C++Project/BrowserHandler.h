
#ifndef BROWSERHANDLER_H
#define BROWSERHANDLER_H

#include "stringfix.h"

/**
 * Handles browser interface communications (via TCP)
 */
class BrowserHandler {
public:
    // Constructors
    BrowserHandler();
    BrowserHandler(const BrowserHandler& orig);
    virtual ~BrowserHandler();

    // Methods
    void init();
    void handleComm();

private:
    // Server socket file descriptor
    int serverSock;

    // Methods
    String readLine(int);
    String getFromBrowser();
    String processResp(String);
};

#endif /* BROWSERHANDLER_H */

