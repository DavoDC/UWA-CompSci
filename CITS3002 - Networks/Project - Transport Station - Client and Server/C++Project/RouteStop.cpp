
#include "globals.h"

/**
 * Create a route stop from parts
 * @param nameIn
 * @param portIn
 */
RouteStop::RouteStop(String nameIn, int portIn) {
    name = nameIn;
    port = portIn;
}

/**
 * Create a route stop from a String rep
 * @param nameIn
 * @param portIn
 */
RouteStop::RouteStop(String strRep) {
    vector<String> parts = split(strRep, sep);
    name = parts.at(0);
    port = stoi(parts.at(1));
}

/**
 * Get the String rep of a route stop
 * @return 
 */
String RouteStop::toString() {
    return (name + sep + to_string(port));
}

/**
 * Return true if this RS is as same the input
 * @param comp
 * @return 
 */
bool RouteStop::equals(RouteStop comp) {
    bool samePort = port == comp.getPort();
    bool sameName = strEquals(name, comp.getName());
    return sameName && samePort;
}

/**
 * Update the port's stop
 *
 * @param newPort
 */
void RouteStop::setPort(int newPort) {
    port = newPort;
}

/**
 * Get stop name
 *
 * @return
 */
String RouteStop::getName() {
    return name;
}

/**
 * Get stop port
 *
 * @return
 */
int RouteStop::getPort() {
    return port;
}

// ----
// ------------------------------------
// ----

/**
 * Default value
 */
RouteStop::RouteStop() {
    name = "Default";
    port = -1;
}

/**
 * Copy constructor - enables use in vectors
 * @param orig
 */
RouteStop::RouteStop(const RouteStop& orig) {
    name = orig.name;
    port = orig.port;
}

/**
 * Destructor
 */
RouteStop::~RouteStop() {
}

