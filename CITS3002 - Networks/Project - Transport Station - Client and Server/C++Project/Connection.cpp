
#include "globals.h"

/**
 * Make connection from string rep
 */
Connection::Connection(String strRep) {

    // Split line into parts
    vector<String> parts = split(strRep, sep);

    // If number of parts is not 5
    if (parts.size() != 5) {

        // Notify and exit
        throwError("Bad line", EINVAL);
    }

    // Save parts
    depart = LocalTime(parts.at(0));
    mode = parts.at(1);
    startPoint = parts.at(2);
    arrive = LocalTime(parts.at(3));
    destName = parts.at(4);
}

/**
 * Get string rep of connection
 * @return 
 */
String Connection::toString() {

    // Holder
    String rep = "";

    // Add departure time
    rep += depart.toString() + sep;

    // Add mode of transport
    rep += mode + sep;

    // Starting stop
    rep += startPoint + sep;

    // Arrival time
    rep += arrive.toString() + sep;

    // Destination name
    rep += destName;

    // Return rep
    return rep;
}

/**
 * Get human readable description of the connection
 *
 * @return
 */
String Connection::getDesc() {
    String desc = "";
    desc += "At " + depart.toString() + ",";
    desc += " catch '" + mode + "' from '";
    desc += startPoint + "', ";
    desc += "and you will arrive at " + arrive.toString();
    desc += " at '" + destName + "'";
    return desc;
}

/**
 * Print description
 */
void Connection::printDesc() {
    printD("\n" + getDesc());
}

/**
 * Get destination name
 *
 * @return
 */
String Connection::getDest() {
    return destName;
}

/**
 * Get arrival time
 *
 * @return
 */
LocalTime Connection::getArrivalTime() {
    return arrive;
}

/**
 * Get departure time
 *
 * @return
 */
LocalTime Connection::getDepTime() {
    return depart;
}

/**
 * Get start point
 *
 * @return
 */
String Connection::getStartPoint() {
    return startPoint;
}

/**
 * Get mode of transport
 *
 * @return
 */
String Connection::getMode() {
    return mode;
}

// ----
// ------------------------------------
// ----

/**
 * Default constructor
 */
Connection::Connection() {
}

/**
 * Copy constructor - enables use in vectors
 * @param orig
 */
Connection::Connection(const Connection& orig) {

    // Departure time
    depart = orig.depart;

    // Mode of transport (bus number/bus line/train line)
    mode = orig.mode;

    // Starting stop
    startPoint = orig.startPoint;

    // Arrival time
    arrive = orig.arrive;

    // Destination name
    destName = orig.destName;
}

/**
 * Assignment operator - enables use in vectors
 * @param orig
 * @return 
 */
Connection Connection::operator=(const Connection& orig) {

    // If not invalid self-assignment
    if (this != &orig) {

        // Departure time
        depart = orig.depart;

        // Mode of transport (bus number/bus line/train line)
        mode = orig.mode;

        // Starting stop
        startPoint = orig.startPoint;

        // Arrival time
        arrive = orig.arrive;

        // Destination name
        destName = orig.destName;
    }

    // Return this
    return *this;
}

/**
 * Destructor
 */
Connection::~Connection() {
}
