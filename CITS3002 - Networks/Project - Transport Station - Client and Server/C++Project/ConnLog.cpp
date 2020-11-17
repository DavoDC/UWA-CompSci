
#include "globals.h"

/**
 * Create a connection log from a string representation
 *
 * @param strRep
 */
ConnLog::ConnLog(String strRep) {

    // If empty
    if (strEquals(strRep, "Empty")) {

        // Do not process further
        return;
    }

    // Split into connection representations
    vector<String> connsS = split(strRep, sep);

    // For each route stop string
    for (String connStr : connsS) {

        // Extract route stop and add to log
        conns.push_back(Connection(connStr));
    }
}

/**
 * Get string representation
 *
 * @return
 */
String ConnLog::toString() {

    // If log is empty
    if (conns.empty()) {

        // Notify
        return "Empty";
    }

    // Holder
    String rep = "";

    // Add route stops separated
    for (Connection curConn : conns) {

        rep += curConn.toString() + sep;
    }

    // Return representation
    return rep;
}

/**
 * Get the first connection
 *
 * @return
 */
Connection ConnLog::getFirst() {
    return conns.at(0);
}

/**
 * Get the last connection
 *
 * @return
 */
Connection ConnLog::getLast() {
    return conns.at(conns.size() - 1);
}

/**
 * Get separator
 * @return 
 */
String ConnLog::getSep() {
    return sep;
}

// ----
// ------------------------------------
// ----

/**
 * Constructor
 */
ConnLog::ConnLog() {
}

/**
 * Special
 * @param orig
 */
ConnLog::ConnLog(const ConnLog& orig) {
}

/**
 * Destructor
 */
ConnLog::~ConnLog() {
}

