
#ifndef ROUTELOG_H
#define ROUTELOG_H

#include "stringfix.h"
#include "RouteStop.h"

/**
 * Represents a log of route stops taken
 */
class RouteLog {
public:
    // Construction
    RouteLog();
    RouteLog(bool);
    RouteLog(String);
    RouteLog(const RouteLog& orig);
    virtual ~RouteLog();

    // Methods
    String toString();
    void addRS(RouteStop);
    bool contains(RouteStop);
    vector<RouteStop> getVec();

private:
    // Variables
    // Separator
    String sep = "->";

    // Underlying list
    vector<RouteStop> stops;

};

#endif /* ROUTELOG_H */

