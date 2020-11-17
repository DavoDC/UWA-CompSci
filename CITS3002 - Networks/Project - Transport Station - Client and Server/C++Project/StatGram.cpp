
#include "globals.h"

/**
 * Make infogram
 * 
 * @param nothing 
 */
StatGram::StatGram(int nothing) {

    // Initialize type
    type = "InfoGram";

    // Initialize default status
    status = LookingForDest;

    // Initialize source stop
    srcStop = thisRS;

    // Destination is not important
    destStop = RouteStop("Unknown", -1);

    // Initialize route log
    routeLog = RouteLog(false);

    // Make avoid log
    avoidLog = RouteLog(false);

    // Initialize backtracking log
    btLog = RouteLog(false);
}

/**
 * Make route gram
 *
 * @param destName Destination name
 * @param avoidLog The avoid log
 */
StatGram::StatGram(String destName, RouteLog avoidLogIn) {

    // Save type
    type = "RouteGram";

    // Initialize default status
    status = LookingForDest;

    // Initialize source stop
    srcStop = thisRS;

    // Initialize route log
    routeLog = RouteLog(true);

    // Initialize destination
    destStop = RouteStop(destName, -1);

    // Save avoid log
    avoidLog = avoidLogIn;

    // Initialize backtracking log
    btLog = RouteLog(false);
}

/**
 * Make a statgram from string representation
 *
 * @param strRep
 */
StatGram::StatGram(String strRep) {

    // Split into parts
    vector<String> parts = split(strRep, sep);

    //    // TEST
    //    printD("Split into parts!\n");
    //
    //    // TEST
    //    for (String cur : parts) {
    //        printD(cur + "  ");
    //    }

    // Load 1 = Type of Statgram
    type = extractPart(parts, 1);

    // Load 2 = Status
    status = extractPart(parts, 2);

    // Load 3 = Source stop
    srcStop = RouteStop(extractPart(parts, 3));

    // Load 4 = Destination stop
    destStop = RouteStop(extractPart(parts, 4));

    // Load 5 = Add route log
    routeLog = RouteLog(extractPart(parts, 5));

    // Load 6 = Add log of route stops to avoid
    avoidLog = RouteLog(extractPart(parts, 6));

    // Load 7 = Add log of stops to backtrack
    btLog = RouteLog(extractPart(parts, 7));

    // Load 8 = Add connections for final result
    connLog = ConnLog(extractPart(parts, 8));
}

/**
 * Extract the value section of a data piece
 *
 * @param parts
 * @param index
 * @return
 */
String StatGram::extractPart(vector<String> parts, int index) {
    return split(parts.at(index), nameSep).at(1);
}

/**
 * Retrieve a string representation of the statgram for sending in datagram
 * packets
 *
 * @return
 */
String StatGram::toString() {
    // Holder
    String rep = "";

    // Add 0 = Protocol name
    rep += addPart("Protocol", protoName);

    // Add 1 = Type of Statgram
    rep += addPart("Type", type);

    // Add 2 = Status
    rep += addPart("Status", status);

    // Add 3 = Source stop
    rep += addPart("Source", srcStop.toString());

    // Add 4 = Destination stop
    rep += addPart("Destination", destStop.toString());

    // Add 5 = Add route log
    rep += addPart("RouteLog", routeLog.toString());

    // Add 6 = Add log of route stops to avoid
    rep += addPart("AvoidLog", avoidLog.toString());

    // Add 7 = Add log of stops to backtrack
    rep += addPart("BackTrack", btLog.toString());

    // Add 8 = Add connections for final result
    rep += addPart("ConnLog", connLog.toString());

    // Return
    return rep;
}

/**
 * Add a piece of data in the protocol format
 *
 * @param name
 * @param value
 * @return
 */
String StatGram::addPart(String name, String value) {
    return (name + nameSep + value + sep);
}

/**
 * Print out a description of this statgram
 *
 * @param action
 */
void StatGram::printDesc(String action) {

    // Say station name and type
    String msg = "\n ### " + getType() + " " + action + " by ";
    msg += stName + "!";
    printD(msg);

    // Give payload info
    vector<String> parts = split(toString(), sep);
    if (parts.empty()) {
        return;
    }
    for (int i = 0; i < parts.size(); i++) {
        printD(" " + to_string(i) + "=" + parts.at(i));
    }

    printD("");
}

/**
 * Add a port as next connection to connection log
 *
 * @param port
 */
void StatGram::addToConnLog(int port) {

    // If not an adjacent port
    if (intVecContains(adjPorts, port)) {
        String msg = "addToConnLog: " + to_string(port);
        msg += " was not adjacent";
        throwError(msg, EINVAL);
    }

    // NOT DONE YET
    throwError("addToConnLog not done yet!", ENOTSUP);

    // Convert port to name
    //    String dest = null;
    //    Set<Entry<String, Integer>> entrySet;
    //    entrySet = Globals.nbDir.entrySet();
    //    for (Entry<String, Integer> curEnt : entrySet) {
    //
    //        // If port of entry matches query
    //        if (curEnt.getValue() == port) {
    //
    //            // Save name linked
    //            dest = curEnt.getKey();
    //        }
    //    }
    //
    //    // Comparison time in time calculation
    //    LocalTime comp = null;
    //
    //    // If connection log is not empty
    //    if (!connLog.isEmpty()) {
    //
    //        // Get the last connection
    //        Connection last = connLog.getLast();
    //        comp = last.getArrivalTime();
    //    }
    //
    //    // Get best connection for that destination
    //    Connection best = Globals.timetable.getNextDepStr(dest, comp);
    //
    //    // Add to connection log
    //    connLog.add(best);
}

/**
 * Switch status
 *
 * @param status
 */
void StatGram::switchStatus() {
    if (strEquals(status, LookingForDest)) {
        status = LookingForSrc;
    } else if (strEquals(status, LookingForSrc)) {
        status = LookingForDest;
    }
    throwError("switchStatus: Bad status", EINVAL);
}

/**
 * Set destination route stop
 *
 * @param destRS
 */
void StatGram::setDestRS(RouteStop destRS) {
    destStop = destRS;
}

/**
 * Retrieve route log
 *
 * @return
 */
RouteLog StatGram::getRouteLog() {
    return routeLog;
}

/**
 * Retrieve avoid log
 *
 * @return
 */
RouteLog StatGram::getAvoidLog() {
    return avoidLog;
}

/**
 * Get backtracking log
 *
 * @return
 */
RouteLog StatGram::getBTLog() {
    return btLog;
}

/**
 * Get connection log
 *
 * @return
 */
ConnLog StatGram::getConnLog() {
    return connLog;
}

/**
 * Get the destination stop
 *
 * @return
 */
RouteStop StatGram::getDestStop() {
    return destStop;
}

/**
 * Get the source stop
 *
 * @return
 */
RouteStop StatGram::getSrcStop() {
    return srcStop;
}

/**
 * Get status
 *
 * @return
 */
String StatGram::getStatus() {
    return status;
}

/**
 * Get type
 *
 * @return
 */
String StatGram::getType() {
    return type;
}

// ----
// ------------------------------------
// ----

/**
 * Constructor
 */
StatGram::StatGram() {
}

/**
 * Copy constructor - enables use in vectors
 * @param orig
 */
StatGram::StatGram(const StatGram & orig) {

    // 1 = Type
    type = orig.type;

    // 2 = Status
    // LookingForSrc OR LookingForDest
    status = orig.status;

    // 3 = Source stop
    srcStop = orig.srcStop;

    // 4 = Destination stop
    destStop = orig.destStop;

    // 5 = Log of route stops visited
    routeLog = orig.routeLog;

    // 6 = Log of route stops to avoid
    avoidLog = orig.avoidLog;

    // 7 = Used to get back to the source when final result has been acquired
    btLog = orig.btLog;

    // 8 = When getting back, this list holds the connections of 
    // the final result in reverse order
    connLog = orig.connLog;
}

/**
 * Destructor
 */
StatGram::~StatGram() {
}

