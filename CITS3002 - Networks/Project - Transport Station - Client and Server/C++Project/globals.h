/* 
 * Application-wide accessible data
 */

#ifndef GLOBALS_H
#define GLOBALS_H

// CPP Libraries
#include <map>
#include <ctime>
#include <thread> 
#include <vector>
#include <cstdlib>
#include <fstream>
#include <iostream>
#include <algorithm>

// C Libraries
#include <time.h>
#include <errno.h> 
#include <netdb.h>
#include <stdio.h> 
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/stat.h>
#include <sys/time.h>
#include <arpa/inet.h>
#include <sys/types.h> 
#include <sys/socket.h> 
#include <sys/select.h>
#include <netinet/in.h>



// String fix
#include "stringfix.h"

// Smallest modules
#include "RouteStop.h"
#include "LocalTime.h"
#include "Connection.h"

// Logs
#include "ConnLog.h"
#include "RouteLog.h"

// Intermediate modules
#include "RGProc.h"
#include "StatGram.h"

// Main modules
#include "Timetable.h"
#include "BrowserHandler.h"
#include "DatagramHandler.h"




// ####################################
// ARGUMENTS
// ####################################
// Name of the station
extern String stName;

// Web browser TCP/IP port
extern int browserPort;

// Incoming UDP/IP port for stations
extern int stPort;

// Adjacent station UDP/IP ports
extern vector<int> adjPorts;




// ####################################
// VARIABLES
// ####################################
// The route stop representing this station
extern RouteStop thisRS;

// Status
extern String LookingForDest;
extern String LookingForSrc;

// The folder containing the timetables
extern String ttFolder;

// Debugging switch
extern bool debugOn;

// Time comparison setting
extern bool useCurrentTime;
extern int testHour;

// Datagram packet size
extern int DG_SIZE;



// ####################################
// ADJACENT STATION INFO
// ####################################
// Neigbour directory - adjacent station names linked to their ports
extern map<String, int> nbDir;

// List of adjacent stations
extern RouteLog adjList;




// ####################################
// MODULES
// ####################################
// Timetable
extern Timetable timetable;

// Browser Handler
extern BrowserHandler browserH;

// Datagram Handler
extern DatagramHandler datagramH;




// ####################################
// GLOBAL FUNCTION DECLARATIONS
// ####################################
// General
void printD(String);
bool isAdjacent(String);
void throwError(String, int);
bool intVecContains(vector<int>, int);
struct sockaddr_in getAddress(int);
int getSocket(bool, int);

// String functions
bool strEquals(String, String);
bool strContains(String, String);
String strReplace(String, const String&, const String&);
vector<String> split(const String&, const String&);



#endif /* GLOBALS_H */

