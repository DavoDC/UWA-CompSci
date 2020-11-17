
#include "globals.h"

// ####################################
// DECLARATIONS FOR GLOBALS
// ####################################
// Arguments
String stName;
int browserPort;
int stPort;
vector<int> adjPorts;

// Modules
Timetable timetable;
BrowserHandler browserH;
DatagramHandler datagramH;

// Variables
RouteStop thisRS;
String LookingForDest;
String LookingForSrc;
bool debugOn;
map<String, int> nbDir;
String ttFolder;
bool useCurrentTime;
int testHour;
int DG_SIZE;
RouteLog adjList;


// >>> Function declarations
void saveArguments(vector<String>);
void initProject();
void startProcessing();
void udpThread();
void tcpThread();
void timetableCheck();

/*
 * Main function
 */
int main(int argc, char** argv) {

    // Basic argument check
    if (argc < 2) {
        errno = EINVAL;
        perror("Error");
        exit(EXIT_FAILURE);
    }

    // Put arguments in vector of Strings
    vector<String> args(argv + 1, argv + argc);

    // Save arguments
    saveArguments(args);

    // Initialize project
    initProject();

    // Start processing
    startProcessing();
}

/**
 * Save arguments into global variables
 * @param argv
 */
void saveArguments(vector<String> args) {

    // Save station name
    stName = args[0];

    // Save browser port
    browserPort = stoi(args[1]);

    // Save station port
    stPort = stoi(args[2]);

    // Save list of adjacent ports
    for (int i = 3; i < args.size(); i++) {
        adjPorts.push_back(stoi(args[i]));
    }
}

/**
 * Initialize project
 */
void initProject(void) {

    // Initialize global variables
    thisRS = RouteStop(stName, stPort);
    LookingForDest = "LookingForDest";
    LookingForSrc = "LookingForSrc";
    debugOn = false;
    ttFolder = "time6";
    useCurrentTime = true;
    testHour = 12 + 5; // 5PM
    DG_SIZE = 4096;
    adjList = RouteLog(false);

    // Initialize modules
    timetable.init();
    browserH.init();
    datagramH.init();

    // Print info
    printD("\n stName= " + stName);
    printD(" browserPort= " + to_string(browserPort));
    printD(" stPort= " + to_string(stPort));
    String adjPortS = "";
    for (int port : adjPorts) {
        adjPortS += to_string(port) + " ";
    }
    printD(" adjPorts= " + adjPortS);
    printD(" Extra: SourceStop(thisRS) = " + thisRS.toString());
    printD("");
}

/**
 * Start processing threads
 */
void startProcessing(void) {

    // Make UDP thread first
    // DISABLED
    //thread udpThreadOb(udpThread);

    // Make TCP thread 
    thread tcpThreadOb(tcpThread);

    // Make timetable check thread
    thread ttcThreadOb(timetableCheck);

    // Send off initial infograms so neighbor directory can be built
    // DISABLED
    //datagramH.sendNeighboursInfo();

    // Join threads
    tcpThreadOb.join();
    ttcThreadOb.join();
    //udpThreadOb.join();
}

/**
 * Thread function for UDP
 * @param threadid
 * @return 
 */
void udpThread() {

    // Receive datagrams repeatedly
    while (true) {
        datagramH.receiveDatagrams();
    }
}

/**
 * Thread function for TCP 
 * @param threadid
 * @return 
 */
void tcpThread() {

    // Handle browser communication repeatedly
    while (true) {
        browserH.handleComm();
    }
}

/**
 * Thread function for checking timetable
 */
void timetableCheck() {

    // Repeatedly update timetable if needed
    while (true) {
        timetable.updateIfNeeded();

        // Sleep for a while, 
        // so that this doesn't occur too often
        sleep(5);
    }
}

