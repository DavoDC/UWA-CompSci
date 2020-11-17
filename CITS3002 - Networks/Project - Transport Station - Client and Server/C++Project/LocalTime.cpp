
#include "globals.h"

/**
 * Make local time from values
 */
LocalTime::LocalTime(int hourIn, int minIn) {

    // Save into fields
    saveIntoFields(hourIn, minIn);
}

/**
 * Make local time from string rep
 */
LocalTime::LocalTime(String strRep) {

    // Split line into parts
    vector<String> parts = split(strRep, sep);

    // Save into fields
    saveIntoFields(stoi(parts[0]), stoi(parts[1]));
}

/**
 * Save the parameters into the underlying structures
 * @param hours
 * @param mins
 */
void LocalTime::saveIntoFields(int hours, int mins) {

    // Save time parts
    tmStruct.tm_year = 0;
    tmStruct.tm_hour = hours;
    tmStruct.tm_min = mins;
    tmStruct.tm_sec = 0;
}

/**
 * Get string rep
 * @return 
 */
String LocalTime::toString() {
    String hourS = to_string(tmStruct.tm_hour);
    if (hourS.length() == 1) {
        hourS = "0" + hourS;
    }
    String minS = to_string(tmStruct.tm_min);
    if (minS.length() == 1) {
        minS += "0";
    }
    return hourS + sep + minS;
}

/**
 * Get underlying tm struct
 * @return 
 */
struct tm LocalTime::getTMStruct() {
    return tmStruct;
}

/**
 * Get difference between this time and another
 * (This time is 'beginning', input is 'end')
 * @param orig
 */
LocalTime LocalTime::getDiffBetween(LocalTime start, LocalTime end) {

    // Get values
    int startHour = start.getTMStruct().tm_hour;
    int startMin = start.getTMStruct().tm_min;
    int endHour = end.getTMStruct().tm_hour;
    int endMin = end.getTMStruct().tm_min;

    // If second has more minutes
    if (endMin > startMin) {

        // Reduce hour and increase second mins
        --startHour;
        startMin += 60;
    }

    int mins = startMin - endMin;
    int hours = startHour - endHour;

    // Convert to local time
    LocalTime lt = LocalTime(hours, mins);

    // Return local time
    return lt;
}

/**
 * Return true if this time is negative
 * @return 
 */
bool LocalTime::isNegative() {
    bool hourNeg = tmStruct.tm_hour < 0;
    bool minNeg = tmStruct.tm_min < 0;
    return hourNeg || minNeg;
}

// ----
// ------------------------------------
// ----

/**
 * Default constructor
 */
LocalTime::LocalTime() {
}

/**
 * Copy constructor - enables use in vectors
 * @param orig
 */
LocalTime::LocalTime(const LocalTime& orig) {

    tmStruct = orig.tmStruct;
}

/**
 * Destructor
 */
LocalTime::~LocalTime() {
}
