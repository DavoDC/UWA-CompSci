
#include "globals.h"

/**
 * Initialize datagram handler
 */
void DatagramHandler::init() {

    // Initialize sending socket
    serverSock = getSocket(true, stPort);
}

/**
 * Get incoming statgram
 *
 * @return
 * @throws IOException
 */
StatGram DatagramHandler::getStatGram() {

    // Make socket set
    fd_set sockset;

    // Clear sockets
    FD_ZERO(&sockset);

    // Add socket to set
    FD_SET(serverSock, &sockset);

    // Call select
    int ret = select(serverSock + 1, &sockset, (fd_set *) 0, (fd_set *) 0, 0);

    // Check for error
    if (ret == -1) {
        perror("Select Error");
        close(serverSock);
        exit(EXIT_FAILURE);
    }

    // Datagram holder
    char buffer[DG_SIZE];

    // If socket is ready
    if (FD_ISSET(serverSock, &sockset)) {

        // Receive data that is ready
        // (Address is NULL as it is for filling in by source)
        int ret = recvfrom(serverSock, (char *) buffer,
                DG_SIZE, MSG_WAITALL, NULL, NULL);

        // Check for error
        if (ret == -1) {
            perror("RecvFrom Error");
            close(serverSock);
            exit(EXIT_FAILURE);
        }

        // Terminate datagram
        buffer[ret] = '\0';

        // Clear set
        FD_CLR(serverSock, &sockset);
    }

    // TEST
    printD("GOT SGstr:" + String(buffer) + "END\n");

    // Return as statgram
    return StatGram(String(buffer));
}

/**
 * Handle the receiving of datagrams
 */
void DatagramHandler::receiveDatagrams() {

    // Get statgram
    StatGram sg = getStatGram();

    // Get type
    String type = sg.getType();

    // If type is infogram
    if (strEquals(type, "InfoGram")) {

        // Get source of infogram
        RouteStop src = sg.getSrcStop();

        // Add source to adjacent stop list
        adjList.addRS(src);

        // Add entry to neighbor directory
        nbDir.insert(make_pair(src.getName(), src.getPort()));

        // Print SG info
        sg.printDesc("received");

        // Print directory
        printD("nbDir=");
        for (auto& t : nbDir) {
            printD("\n  >" + t.first + "," + to_string(t.second));
        }

    } else if ((strEquals(type, "RouteGram"))) {

        // Process route gram
        rgp.processRG(sg);

    } else {

        // Notify about unknown type
        throwError("receiveDatagrams: Unknown type", EINVAL);
    }
}

/**
 * Send a statgram on given port
 *
 * @param sg
 * @param port
 */
void DatagramHandler::sendStatgram(StatGram sg, int port) {

    // Get payload
    String payload = sg.toString();

    // Get length
    int len = payload.length() + 1;

    // Get address
    struct sockaddr_in address = getAddress(port);

    // Send payload to given address
    int ret = sendto(serverSock, payload.c_str(), len,
            MSG_CONFIRM,
            (struct sockaddr*) &address,
            sizeof (address));

    // If sending failed
    if (ret == -1) {

        perror("sendSG Error");
        exit(EXIT_FAILURE);
    }

    // Notify
    sg.printDesc("sent");
}

/**
 * Discover multi-station route by communicating with other stations
 *
 * @param destName
 * @return
 */
String DatagramHandler::discoverRoute(String destName) {

    // Good statgram holder
    StatGram good;
    ConnLog cl;
    String clStr = "NULL";

    // Go through the assortment of routes
    for (int i = 0; i < adjPorts.size() * 10; i++) {

        // Make route gram (has avoid log with all adjacent stops)
        RouteLog avoidLog = RouteLog(true);
        for (RouteStop adjStop : adjList.getVec()) {
            avoidLog.addRS(adjStop);
        }
        StatGram rg = StatGram(destName, avoidLog);

        // Send to all adjacent stations indiscriminately
        sendSGtoNeighbors(rg, false, true);
        sendSGtoNeighbors(rg, false, true); // Failsafe

        // Wait to receive statgram reply
        StatGram curSG = getStatGram();

        // Clean BT log
        if (!curSG.getBTLog().getVec().empty()) {
            curSG.getBTLog().getVec().erase(
                    curSG.getBTLog().getVec().begin());
        }

        // Notify
        curSG.printDesc("got REPLY in DGH");

        // Extract connection log
        ConnLog curCl = curSG.getConnLog();
        String curClStr = curCl.toString();

        // If SG is valid
        if (!strContains(curClStr, "UNAVAIL")) {
            good = curSG;
            cl = curCl;
            clStr = curClStr;
            break;
        }
        // Else, try again
    }

    // StatGram tries done
    // If nothing was found
    if (strEquals(clStr, "NULL")) {

        // Notify
        printD("Nothing was found");

        // No route is available
        return "UNAVAIL";
    } else {

        // Else if something was found
        // Print out connections
        printD("\nConnections in Final Route:");
        vector<String> clStrParts = split(clStr, cl.getSep());
        for (String conn : clStrParts) {
            printD(" >" + conn);
        }

        // Get first and last connection
        Connection first = cl.getFirst();
        Connection last = cl.getLast();

        // Extract final result
        String result = "";
        result += "At " + first.getDepTime().toString() + ",";
        result += " catch " + first.getMode();
        result += ", from " + first.getStartPoint() + ". ";
        result += "You will arrive at your final destination at ";
        result += last.getArrivalTime().toString();

        // Return result
        return result;
    }
}

/**
 * Send a given statgram to all neighboring stations
 *
 * @param sg
 * @param useAvoidLog
 * @param updateConnLog
 */
void DatagramHandler::sendSGtoNeighbors(StatGram sg,
        bool useAvoidLog, bool updateConnLog) {

    // Turn SG to string
    String sgStr = sg.toString();

    // If using avoid log is desired
    if (useAvoidLog) {

        // If adjacent list is not initialized
        if (adjList.getVec().empty()) {

            // Do not process further
            return;
        }

        // For every adjacent stop
        for (RouteStop adjStop : adjList.getVec()) {

            // If the avoid log does not contain that stop
            if (!sg.getAvoidLog().contains(adjStop)) {

                // Make statgram copy
                StatGram newSG = StatGram(sgStr);

                // Get port
                int port = adjStop.getPort();

                // If updating of connection log desired, do so
                if (updateConnLog) {
                    newSG.addToConnLog(port);
                }

                // Send statgram to stop
                sendStatgram(newSG, port);
            }
        }
    } else {
        // Else, send to all neighbours indiscriminately
        // For each adjacent port
        for (int curAdjPort : adjPorts) {

            // Make statgram copy
            StatGram newSG = StatGram(sgStr);

            // If updating of connection log desired, do so
            if (updateConnLog) {
                newSG.addToConnLog(curAdjPort);
            }

            // Send statgram
            sendStatgram(newSG, curAdjPort);
        }
    }
}

/**
 * Send a statgram on a backtracking journey
 *
 * @param sg
 */
void DatagramHandler::backtrackSG(StatGram sg) {

    // If empty
    if (sg.getBTLog().getVec().empty()) {

        // Cancel
        return;
    }

    // Get port of first stop in backtracking log
    int next = sg.getBTLog().getVec().at(0).getPort();

    // Send to that port
    sendStatgram(sg, next);
}

/**
 * Send an infogram to neighbours so they can build neigbour directory
 */
void DatagramHandler::sendNeighboursInfo() {

    // Send default infogram indiscriminately to all neighbours
    sendSGtoNeighbors(StatGram(0), false, false);
}

// ----
// ------------------------------------
// ----

/**
 * Constructor
 */
DatagramHandler::DatagramHandler() {
}

/**
 * Special
 * @param orig
 */
DatagramHandler::DatagramHandler(const DatagramHandler & orig) {
}

/**
 * Destructor
 */
DatagramHandler::~DatagramHandler() {
}

