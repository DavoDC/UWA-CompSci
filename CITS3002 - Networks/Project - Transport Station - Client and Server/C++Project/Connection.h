

#ifndef CONNECTION_H
#define CONNECTION_H

#include "stringfix.h"

class Connection {
public:
    // Construction
    Connection();
    Connection(String);
    Connection(const Connection& orig);
    Connection operator=(const Connection& orig);
    virtual ~Connection();

    // Methods
    String toString();
    void printDesc();
    String getDesc();
    String getDest();
    String getMode();
    String getStartPoint();
    LocalTime getArrivalTime();
    LocalTime getDepTime();

private:
    // Variables
    // Separator
    String sep = ",";

    // Departure time
    LocalTime depart;

    // Mode of transport (bus number/bus line/train line)
    String mode;

    // Starting stop
    String startPoint;

    // Arrival time
    LocalTime arrive;

    // Destination name
    String destName;

};

#endif /* CONNECTION_H */

