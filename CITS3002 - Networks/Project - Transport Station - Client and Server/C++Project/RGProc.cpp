
#include "globals.h"

/**
 * Process a given route gram
 *
 * @param sg
 */
void RGProc::processRG(StatGram sg) {

    // If route gram is looking for dest
    if (strEquals(sg.getStatus(), LookingForDest)) {

        // Process as a 'searching' RG
        processSearchingRG(sg);
    } else {

        // Else, if it is looking for the source.
        // process as a backtracking RG
        processBackTrackingRG(sg);
    }
}

/**
 * Process a searching route gram
 */
void RGProc::processSearchingRG(StatGram newSG) {

    // Get destination stop and name
    RouteStop destRS = newSG.getDestStop();
    String destName = destRS.getName();

    // Update avoid log (6) to indicate we shouldn't visit this 
    // stop for any more info
    newSG.getAvoidLog().addRS(thisRS);

    // Update route log (5) to indicate we have visited this stop
    newSG.getRouteLog().addRS(thisRS);

    // If we have reached the destination 
    if (thisRS.equals(destRS)) {
        
        // Process as an arrived RG
        processArrivedRG(newSG);
    } else {
        // Else we are adjacent to the destination or far from it
        // Process as a further RG
        processFurtherRG(newSG, destName, destRS);
    }
}

/**
 * Process a route gram that has arrived at the destination
 *
 * @param newSG
 */
void RGProc::processArrivedRG(StatGram newSG) {

    // Switch status (2) to indicate we are no longer looking 
    // for the destination, but for the source
    newSG.switchStatus();

    // Add route log backwards to backtracking log (7)
    vector<RouteStop> rlv = newSG.getRouteLog().getVec();
    for (int i = rlv.size() - 1; i >= 0; i--) {
        newSG.getBTLog().addRS(rlv.at(i));
    }

    // Send SG on backtracking journey 
    datagramH.backtrackSG(newSG);
}

/**
 * Process an RG is that one or more stops from the dest
 */
void RGProc::processFurtherRG(StatGram newSG, String destName,
        RouteStop destRS) {

    // If we are adjacent to the dest
    if (isAdjacent(destName)) {

        // Extract port from neighbor directory
        int destPort = nbDir.at(destName);

        // We have the information to update the destination
        // Update port in dest stop copy, 
        // and overwrite dest (4) in SG with newer one
        destRS.setPort(destPort);
        newSG.setDestRS(destRS);

        // Update connection log
        newSG.addToConnLog(destPort);

        // We can send the new statgram 
        // directly to the destination port
        datagramH.sendStatgram(newSG, destPort);

    } else {
        // Else we are far from the destination
        // Send the SG on neighbouring ports, avoiding some
        datagramH.sendSGtoNeighbors(newSG, true, true);
    }
}

/**
 * Process back tracking route gram
 *
 * @param sg
 */
void RGProc::processBackTrackingRG(StatGram sg) {

    // If backtracking log is not empty
    if (!sg.getBTLog().getVec().empty()) {

        // Remove first entry from bt log
        sg.getBTLog().getVec().erase(sg.getBTLog().getVec().begin());
    }

    // If backtracking log is NOW empty
    if (sg.getBTLog().getVec().empty()) {

        // We have finished backtracking!!
        // Let discoverRoute() in Datagram Handler handle it
        return;
    }

    // Send SG on backtracking journey
    datagramH.backtrackSG(sg);
}


// ----
// ------------------------------------
// ----

RGProc::RGProc() {
}

RGProc::RGProc(const RGProc& orig) {
}

RGProc::~RGProc() {
}

