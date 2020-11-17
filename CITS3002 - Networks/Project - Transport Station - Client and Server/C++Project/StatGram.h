
#ifndef STATGRAM_H
#define STATGRAM_H

#include "stringfix.h"

/**
 * Models a station datagram
 */
class StatGram {
public:

    // Construction
    StatGram();
    StatGram(int nothing);
    StatGram(String strRep);
    StatGram(const StatGram& orig);
    StatGram(String destName, RouteLog avoidLogIn);
    virtual ~StatGram();

    // Methods
    String toString();
    void printDesc(String action);
    void addToConnLog(int port);
    void setDestRS(RouteStop destRS);
    void switchStatus();
    RouteLog getRouteLog();
    RouteLog getAvoidLog();
    RouteLog getBTLog();
    ConnLog getConnLog();
    RouteStop getDestStop();
    RouteStop getSrcStop();
    String getStatus();
    String getType();

private:
    // Methods
    String extractPart(vector<String>, int);
    String addPart(String name, String value);

    // Variables
    // Separator
    String sep = "&";

    // Name Separator
    String nameSep = "=";

    // 0 = Protocol name
    String protoName = "UDP_DC";

    // 1 = Type
    String type;

    // 2 = Status
    // LookingForSrc OR LookingForDest
    String status;

    // 3 = Source stop
    RouteStop srcStop;

    // 4 = Destination stop
    RouteStop destStop;

    // 5 = Log of route stops visited
    RouteLog routeLog;

    // 6 = Log of route stops to avoid
    RouteLog avoidLog;

    // 7 = Used to get back to the source when final result has been acquired
    RouteLog btLog;

    // 8 = When getting back, this list holds the connections of 
    // the final result in reverse order
    ConnLog connLog;

};

#endif /* STATGRAM_H */

