
#ifndef TIMETABLE_H
#define TIMETABLE_H

#include "stringfix.h"

/**
 * Models the timetable of a station
 */
class Timetable {
public:
    // Construction
    Timetable();
    Timetable(const Timetable& orig);
    virtual ~Timetable();

    // Methods
    void init();
    String getRoute(String);
    void updateIfNeeded();

private:

    // Variables
    vector<Connection> connections;
    String fullTTpath;
    time_t timeLastUpdated;

    // Methods
    void updateConns();
    time_t getTimeLastModified();
    vector<Connection> getDirConns(String);
    Connection getNextDepStr(vector<Connection>, String);
};

#endif /* TIMETABLE_H */

