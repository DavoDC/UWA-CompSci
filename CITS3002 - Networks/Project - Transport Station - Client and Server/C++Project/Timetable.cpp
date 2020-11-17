
#include "globals.h"

/**
 * Initialize timetable
 */
void Timetable::init() {

    // Initialize timetable file path
    fullTTpath = ttFolder + "/tt-" + stName;

    // Do initial update of connections
    updateConns();
}

/**
 * Get a route (description) from the current station to given destination
 */
String Timetable::getRoute(String destName) {

    // Get direction connections to destination
    vector<Connection> directConns = getDirConns(destName);

    // If the destination is adjacent (i.e has a direct connection)
    if (!directConns.empty()) {

        // Get next best connection
        Connection next = getNextDepStr(directConns, destName);

        // If next is invalid
        if (strContains(next.getDest(), "UNAVAIL")) {

            // No route available
            return "Sorry, no route is available today";
        } else {

            // Return the next departure using timetable information
            return next.getDesc();
        }
    } else {

        // Else if the destination is not adjacent
        // Else if destination is not adjacent,
        // communicate with other stations to find out route
        // DISABLED
        // return datagramH.discoverRoute(destName);
        return "Unsupported. Please read README";
    }
}

/**
 * Find the closest next departure connection to a given destination
 *
 * @param dest
 * @param comp Comparison time
 * @return
 */
Connection Timetable::getNextDepStr(vector<Connection> directConns,
        String destName) {

    // Make comparison time
    LocalTime compTime;

    // If current time wanted
    if (useCurrentTime) {

        // Get current time
        time_t timeStruct;
        time(&timeStruct);
        struct tm* tmStruct = localtime(&timeStruct);
        int currHour = tmStruct->tm_hour;
        int currMin = tmStruct ->tm_min;

        // Initialize
        compTime = LocalTime(currHour, currMin);

    } else {

        // If current time is not wanted, 
        // use testing hour
        compTime = LocalTime(testHour, 0);
    }

    // Difference holders
    vector<LocalTime> diffs;
    diffs.reserve(directConns.size() + 1);
    vector<Connection> matchingConns;

    // For all direct connections
    for (Connection curConn : directConns) {

        // Extract departure time from connection
        LocalTime depTime = curConn.getDepTime();

        // Calculate difference between dep time and comp time
        LocalTime curDiff = compTime.getDiffBetween(depTime, compTime);

        // TEST
        printD("compTime: " + compTime.toString());
        printD("depTime: " + depTime.toString());
        printD("diff: " + curDiff.toString());
        printD("");

        // If difference is not negative
        if (!curDiff.isNegative()) {

            // Add matching connection
            matchingConns.push_back(curConn);

            // Add duration
            diffs.push_back(curDiff);
        }
    }


    // If no durations were added
    if (diffs.empty()) {

        // All connections are negative,
        // so no routes are available (all passed by)
        return Connection("00:00,UNAVAIL,UNAVAIL,00:00,UNAVAIL");

    } else {

        // Get smallest positive difference index
        int smallestDiffInd = 0;
        for (int i = 0; i < diffs.size(); i++) {

            // Get difference between current and current smallest
            struct tm curDiffTM = diffs.at(i).getTMStruct();
            time_t currDiffTime = mktime(&curDiffTM);
            struct tm currSmallestTM = diffs.at(smallestDiffInd).getTMStruct();
            time_t currSmallest = mktime(&currSmallestTM);
            double diffSecs = difftime(currDiffTime, currSmallest);

            // If current difference is smaller than 'smallest' diff
            if (diffSecs < 0) {

                // Update smallest difference index
                smallestDiffInd = i;
            }
        }

        // Get connection linked to smallest positive duration
        Connection best = matchingConns.at(smallestDiffInd);

        // Return best connection
        return best;
    }
}

/**
 * Return a list of the direct connections to a given destination
 * @param destName
 * @return 
 */
vector<Connection> Timetable::getDirConns(String destName) {

    // Holder
    vector<Connection> directConns;

    // For all connections
    for (Connection curConn : connections) {

        // If connection goes directly to the desired destination
        if (strEquals(curConn.getDest(), destName)) {

            // Add to list of direction connections
            directConns.push_back(curConn);
        }
    }

    // Return list
    return directConns;
}

/**
 * Update the connections list
 */
void Timetable::updateConns() {

    // Open file and check
    ifstream myfile(fullTTpath);
    if (!myfile) {
        throwError("Open Error: " + fullTTpath, ENOENT);
    }

    // Read in lines from file
    vector<String> lines;
    std::string line;
    while (std::getline(myfile, line)) {
        lines.push_back(line);
    }

    // Empty the connections list
    connections.clear();

    // Pre allocate memory
    connections.reserve(lines.size() + 1);

    // Convert lines to connections
    for (int i = 1; i < lines.size(); i++) {
        Connection curConn = Connection(lines.at(i));
        connections.push_back(curConn);
    }

    // Update time last updated
    timeLastUpdated = getTimeLastModified();
}

/**
 * Update the connections list if timetable file was changed
 */
void Timetable::updateIfNeeded() {

    // If time last modified is greater than update time
    if (timeLastUpdated < getTimeLastModified()) {

        // Update connections
        updateConns();
    }
}

/**
 * Get last modified time of timetable file
 */
time_t Timetable::getTimeLastModified() {
    struct stat result;
    if (stat(fullTTpath.c_str(), &result) == 0) {
        return result.st_mtim.tv_sec;
    }
}

// ----
// ------------------------------------
// ----

/**
 * Construct a timetable
 */
Timetable::Timetable() {

}

/**
 * Special constructor
 * @param orig
 */
Timetable::Timetable(const Timetable& orig) {
}

/**
 * Destruct a timetable
 */
Timetable::~Timetable() {
}


