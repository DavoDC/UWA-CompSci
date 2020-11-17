
#include "globals.h"

/**
 * Create an route log
 *
 * @param addStart
 */
RouteLog::RouteLog(bool addStart) {

    // If start desired
    if (addStart) {

        // Add start
        stops.push_back(thisRS);

        //TEST
        //stops.push_back(RouteStop("Frog", 3));
    }
}

/**
 * Create a route log from a string representation
 *
 * @param strRep
 */
RouteLog::RouteLog(String strRep) {

    // If empty
    if (strEquals(strRep, "Empty")) {
        // Do not process further
        return;
    }

    // Split into route stop representations
    vector<String> routeStopsS = split(strRep, sep);

    // For each route stop string
    for (String stopStr : routeStopsS) {

        // Convert to route stop and add to log
        stops.push_back(RouteStop(stopStr));
    }
}

/**
 * Get string representation
 *
 * @return
 */
String RouteLog::toString() {

    // If log is empty
    if (stops.empty()) {

        // Notify
        return "Empty";
    }

    // Holder
    String rep = "";

    // Add route stops separated
    for (RouteStop curRS : stops) {
        rep += String(curRS.toString() + sep);
    }

    // Return representation
    return rep;
}

/**
 * Get underlying vector of route log
 * @return 
 */
vector<RouteStop> RouteLog::getVec() {
    return stops;
}

/**
 * Add route stop if not in log already
 * @param orig
 */
void RouteLog::addRS(RouteStop inRS) {

    // If not in log already
    if (!contains(inRS)) {

        // Add to stops
        stops.push_back(inRS);
    }
}

/**
 * Return true if the log contains the given stop
 * @param query
 * @return 
 */
bool RouteLog::contains(RouteStop query) {
    for (RouteStop curRS : stops) {
        if (curRS.equals(query)) {
            return true;
        }
    }
    return false;
}


// ------------------------------------------
// Construction -----------------------------
// ------------------------------------------

/**
 * Constructor
 */
RouteLog::RouteLog() {
}

/**
 * Special
 * @param orig
 */
RouteLog::RouteLog(const RouteLog& orig) {
}

/**
 * Destructor
 */
RouteLog::~RouteLog() {
}

