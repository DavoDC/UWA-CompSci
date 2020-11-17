
#ifndef ROUTESTOP_H
#define ROUTESTOP_H

using namespace std;

/**
 * Represents a stop in a route
 */
class RouteStop {
public:

    // Constructors
    RouteStop();
    RouteStop(String, int);
    RouteStop(String);
    RouteStop(const RouteStop&);
    virtual ~RouteStop();

    // Methods
    String toString();
    bool equals(RouteStop);
    void setPort(int);
    String getName();
    int getPort();
private:

    // Variables
    String sep = "~~";
    String name;
    int port;

};

#endif /* ROUTESTOP_H */

