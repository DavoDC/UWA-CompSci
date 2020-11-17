
#include "globals.h"

// ####################################
// GENERAL 
// ####################################

/**
 * Print debug message
 * @param msg
 */
void printD(String msg) {

    // If debugging is on AND we are on print station
    if (debugOn && strEquals(stName, "BusportC")) {
        cout << "\n" << msg;
        cout.flush();
    }
}

/**
 * Print error message and exit
 * @param msg
 * @param errtype
 */
void throwError(String msg, int errtype) {

    // Set error
    errno = errtype;

    // Print error
    perror(msg.c_str());

    // Exit
    exit(EXIT_FAILURE);
}

/**
 * Check if a int vector contains a given number
 * @param v
 * @param __val
 * @return 
 */
bool intVecContains(vector<int> vec, int query) {
    for (int curNum : vec) {
        if (curNum == query) {
            return true;
        }
    }
    return false;
}

/**
 * Return true if the given destination is adjacent
 * @param dest
 * @return 
 */
bool isAdjacent(String dest) {
    // If the destination is a key in the neighbor directory,
    // it is adjacent
    return (nbDir.count(dest) >= 1);
}

/**
 * Get an address structure pointer with the given port and local host
 * @param port
 * @return 
 */
struct sockaddr_in getAddress(int port) {

    // Get host entity
    struct hostent* he;
    if ((he = gethostbyname("localhost")) == NULL) {
        perror("GetHostByName Error");
        exit(EXIT_FAILURE);
    }

    // Make structure
    struct sockaddr_in address;

    // Add address
    memcpy(&address.sin_addr, he->h_addr_list[0], he->h_length);

    // Add family (IPv4)
    address.sin_family = AF_INET;

    // Add port
    address.sin_port = htons(port);

    // Return
    return address;
}

/**
 * Get a socket descriptor for a given protocol and port
 * @param isUDP
 * @param port
 * @return 
 */
int getSocket(bool isUDP, int port) {

    // Process type
    int type;
    if (isUDP) {
        type = SOCK_DGRAM;
    } else {
        type = SOCK_STREAM;
    }

    // Get socket file desc
    int sockfd;
    if ((sockfd = socket(AF_INET, type, 0)) == 0) {
        perror("Socket Error");
        exit(EXIT_FAILURE);
    }

    // Make address structure for binding
    struct sockaddr_in address = getAddress(port);
    size_t size = sizeof (address);

    // Bind the address to the opened socket
    if (bind(sockfd, (struct sockaddr *) &address, size) != 0) {
        perror("Binding Error");
        close(sockfd);
        exit(EXIT_FAILURE);
    }

    // Return socket file desc
    return sockfd;
}


// ####################################
// STRING FUNCTIONS
// ####################################

/**
 * Return true if the strings are the same
 * @param first
 * @param second
 * @return 
 */
bool strEquals(String first, String second) {
    if (first.compare(second) == 0) {
        return true;
    }
    return false;
}

/**
 * Return true if the first String contains the second
 * @param first
 * @param query
 * @return 
 */
bool strContains(String first, String query) {
    if (first.find(query) != String::npos) {
        return true;
    }
    return false;
}

/**
 * Replace 'search' in 'input' with 'replacement'
 * @param input
 * @param search
 * @param replace
 * @return 
 */
String strReplace(String input,
        const String& search,
        const String& replacement) {
    size_t pos = 0;
    while ((pos = input.find(search, pos)) != String::npos) {

        input.replace(pos, search.length(), replacement);
        pos += replacement.length();
    }
    return input;
}

/**
 * Split a given String using a given separator
 * @param s
 * @param delim
 * @return 
 */
vector<String> split(const String& text, const String& delimiter) {
    // Holder
    vector<String> parts;

    // String 
    String s = String(text);

    // Position
    size_t pos = 0;

    // Token
    string token;

    // Get tokens
    while ((pos = s.find(delimiter)) != string::npos) {
        token = s.substr(0, pos);
        parts.push_back(token);
        s.erase(0, pos + delimiter.length());
    }

    // Add final part
    parts.push_back(s);

    // Return holder
    return parts;
}