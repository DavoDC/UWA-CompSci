
#include "globals.h"

/**
 * Initialize
 */
void BrowserHandler::init() {

    // Get socket
    serverSock = getSocket(false, browserPort);

    // Indicate we are willing to accept clients
    if (listen(serverSock, 50) < 0) {
        perror("Listen Error");
        exit(EXIT_FAILURE);
    }

    // Set multiple connections option to true
    int opt = true;
    if (setsockopt(serverSock, SOL_SOCKET, SO_REUSEADDR,
            (char *) &opt, sizeof (opt)) != 0) {
        perror("SetSockOpt Error");
        exit(EXIT_FAILURE);
    }
}

/**
 * Handle one browser query and response
 */
void BrowserHandler::handleComm() {

    // Socket for this client
    int clientSock;

    // Wait for client to accept
    if ((clientSock = accept(serverSock, (struct sockaddr *) NULL,
            NULL)) < 0) {
        perror("Accept Error");
        exit(EXIT_FAILURE);
    }

    // Get response from browser
    String resp = readLine(clientSock);

    // Process response
    String html = processResp(resp);

    // Send reply to browser
    send(clientSock, html.c_str(), html.length(), 0);

    // Give browser time
    sleep(1);

    // Shutdown to release ports
    shutdown(clientSock, SHUT_RDWR);

    // Close connection
    close(clientSock);
}

/**
 * Process a browser response and return the output
 */
String BrowserHandler::processResp(String queryline) {

    // Get destination station name
    vector<String> parts = split(queryline, "/");
    String tempS = strReplace(parts[1], " HTTP", "");
    String destName = strReplace(tempS, "?to=", "");

    // Holder
    String html = "";

    // Add HTTP header
    html += "\nHTTP/1.1 200 OK";
    html += "\nContent-Type: text/html";
    html += "\nConnection: Closed";
    html += "\n";

    // Add HTML payload
    html += "\n<html>";
    html += "\n<body>";
    html += "\n<br>";
    html += "\n<h1> " + stName + " </h1>";
    html += "\n<br>";
    html += "\n<h2> Answer: </h2>";

    // If query is invalid
    if (strEquals(destName, "") || strContains(destName, "favicon")) {

        // Notify
        html += "\n<p style=\"font-size:18px\"><i>";
        html += "No query made";
        html += "</i></p>";
    } else {

        // Else if query is valid
        // Get a result
        String result = timetable.getRoute(destName);

        // Check
        if (strContains(result, "UNAVAIL")) {
            result = "Sorry, no route available today.";
            result += " Refreshing the page may work.";
        }

        // Package up result
        html += "\n<p><b> Trip from " + stName;
        html += " to " + destName;
        html += ": </b></p>";
        html += "\n<p style=\"font-size:18px\"><i>";
        html += result;
        html += "</i></p>";
    }

    html += "\n<br>";
    html += "\n<h3> Other Information: </h3>";
    html += "\n<p><b> Web browser TCP/IP port:</b> ";
    html += to_string(browserPort);
    html += "</p>";
    html += "\n<p><u> Station UDP/IP port:</u>     ";
    html += to_string(stPort);
    html += "</p>";
    html += "\n<p><u> Adjacent station UDP/IP ports:</u> ";
    for (int port : adjPorts) {
        html += to_string(port) + "  ";
    }
    html += "</p>";
    html += "\n<p><b> Random Number:</b> ";
    srand(time(NULL) * time(NULL) * rand());
    html += to_string((rand() % 100) + 1);
    html += "</p>";
    html += "\n</body>";
    html += "\n</html>";

    // Return String
    return html;
}

/**
 * Read line from socket
 */
String BrowserHandler::readLine(int socketfd) {
    int buf_size = 0;
    int in_buf = 0;
    int ret;
    char ch;
    char* buffer = NULL;
    char* new_buffer;

    // Loop
    while (true) {
        // Get char
        ret = read(socketfd, &ch, 1);

        // If end of line reached
        if (ch == '\n') {
            // Stop
            break;
        }

        // Check if more memory needed
        if ((buf_size == 0) || (in_buf == buf_size)) {
            buf_size += 128;
            new_buffer = (char*) realloc(buffer, buf_size);
            buffer = new_buffer;
        }

        buffer[in_buf] = ch;
        ++in_buf;
    }

    // Handle line endings
    if ((in_buf > 0) && (buffer[in_buf - 1] == '\r'))
        --in_buf;

    // Allocate more memory if needed
    if ((buf_size == 0) || (in_buf == buf_size)) {
        ++buf_size;
        new_buffer = (char*) realloc(buffer, buf_size);
        buffer = new_buffer;
    }

    // Terminate
    buffer[in_buf] = '\0';

    // Return String
    return String(buffer);
}

// ----
// ------------------------------------
// ----

/**
 * Constructor
 */
BrowserHandler::BrowserHandler() {
}

/**
 * Special
 * @param orig
 */
BrowserHandler::BrowserHandler(const BrowserHandler & orig) {
}

/**
 * Destructor
 */
BrowserHandler::~BrowserHandler() {
}

