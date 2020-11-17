package comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Globals;

/**
 * Handles browser interface communications (via TCP)
 *
 * @author David
 */
public class BrowserHandler {

    // Web browser socket server
    private final ServerSocket browserSS;

    /**
     * Create browser handler
     *
     * @throws java.io.IOException
     */
    public BrowserHandler() throws IOException {

        // Initialize socket server to listen for browser connection on port
        browserSS = new ServerSocket(Globals.browserPort);
    }

    /**
     * Process a browser query
     *
     * @param queryline
     * @return
     */
    private String processQuery(String queryline) {

        // Get destination station name
        String[] parts = queryline.split("/");
        String tempS = parts[1].replace(" HTTP", "");
        String destName = tempS.replace("?to=", "");

        // Holder
        String midHtml = "";

        // If query is invalid
        if (destName.equals("") || destName.contains("favicon")) {

            // Notify 
            midHtml += "\n<p style=\"font-size:18px\"><i>";
            midHtml += "No query made";
            midHtml += "</i></p>";
        } else {

            // Else if query is valid
            // Get route from timetable
            String route = Globals.timetable.getRoute(destName);

            // If route has unavailable section
            if (route.contains("UNAVAIL")) {
                route = "Sorry, no route available today.";
                route += " Refreshing the page may work.";
            }

            // Give route heading and stylize
            midHtml += "\n<p><b> Trip from " + Globals.stName;
            midHtml += " to " + destName;
            midHtml += ": </b></p>";
            midHtml += "\n<p style=\"font-size:18px\"><i>";
            midHtml += route;
            midHtml += "</i></p>";
        }

        // Return string
        return midHtml;
    }

    /**
     * Send HTML content to the browser
     *
     * @param socket
     * @param middleContent
     */
    private void sendToBrowser(Socket socket, String middleContent) {

        // Holder
        String full = "";

        // Add HTTP header
        full += "\nHTTP/1.1 200 OK";
        full += "\nContent-Type: text/html";
        full += "\nConnection: Closed";
        full += "\n";

        // Add HTML payload
        full += "\n<html>";
        full += "\n<body>";
        full += "\n<br>";
        full += "\n<h1> " + Globals.stName + " </h1>";
        full += "\n<br>";
        full += "\n<h2> Answer: </h2>";

        // Add content
        full += middleContent;

        // Add bottom statistics
        full += "\n<br>";
        full += "\n<h3> Other Information: </h3>";
        full += "\n<p><b> Web Browser TCP/IP Port:</b> ";
        full += Globals.browserPort;
        full += "</p>";
        full += "\n<p><u> Station UDP/IP Port:</u>     ";
        full += Globals.stPort;
        full += "</p>";
        full += "\n<p><u> Adjacent Station UDP/IP Ports:</u> ";
        full += Globals.adjacentPorts;
        full += "</p>";
        full += "\n<p><b> Random Number:</b> ";
        full += (int) (Math.random() * 100);
        full += "</p>";
        full += "\n</body>";
        full += "\n</html>";

        // Get bytes
        byte[] bytes = full.getBytes();

        // Write to socket
        try {
            socket.getOutputStream().write(bytes);
        } catch (IOException ex) {
            Logger.getLogger(BrowserHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Receive browser connection requests and handle responses
     */
    public void receiveRequest() {

        // Wait to accept connection (as socket) from browser
        try (Socket socket = browserSS.accept()) {

            // When connection has been made:
            // Get browser query
            String query = getFromBrowser(socket);

            // Process query
            String result = processQuery(query);

            // Send result back to browser
            sendToBrowser(socket, result);

        } catch (IOException ex) {
            Logger.getLogger(BrowserHandler.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        // Do again after a short delay
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                receiveRequest();
            }
        }, 10);
    }

    /**
     * Get the the first line of the browser's HTTP response
     *
     * @param socket
     * @return
     * @throws IOException
     */
    private String getFromBrowser(Socket socket)
            throws IOException {

        // Get input stream and reader
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);

        // Create reader
        BufferedReader reader = new BufferedReader(isr);

        // Return first line
        return reader.readLine();
    }
}
