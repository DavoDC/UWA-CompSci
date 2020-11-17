package timetable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Globals;

/**
 * Models the timetable of a station server
 *
 * @author David
 */
public class Timetable {

    // Timetable file path
    private final String fullTTpath;

    // Last update time
    private long timeLastUpdated;

    // Connection list
    private ArrayList<Connection> connections;

    /**
     * Create timetable
     */
    public Timetable() {

        // Initialize Timetable file path
        fullTTpath = Globals.ttFolder + "/tt-" + Globals.stName;

        // Do initial update of connection list
        updateConns();
    }

    /**
     * Get a route (description) from the current station to given destination
     *
     * @param dest
     * @return
     */
    public String getRoute(String dest) {

        // Check directory
        if (Globals.nbDir == null) {
            return "Not ready yet, Try again";
        }

        // If the destination is adjacent
        if (Globals.isAdjacent(dest)) {

            // Get next best connection
            Connection next = getNextDepStr(dest, null);

            // If next was unavailable
            if (next.getDesc().contains("UNAVAIL")) {

                // No route available
                return "Sorry, no route is available today";
            } else {

                // Return the next departure using timetable information
                return next.getDesc();
            }
        } else {

            // Else if destination is not adjacent,
            // communicate with other stations to find out route
            return Globals.datagramH.discoverRoute(dest);
        }
    }

    /**
     * Find the closest next departure connection to a given destination
     *
     * @param dest
     * @param comp Comparison time
     * @return
     */
    public Connection getNextDepStr(String dest, LocalTime comp) {

        // Get direct connections
        ArrayList<Connection> dirConns;
        dirConns = getDirConns(dest);

        // Comparison/current time
        LocalTime currTime;

        // If comparison input is not null
        if (comp != null) {

            // Use inputted time
            currTime = comp;
        } else {

            // Else if input was null
            // If current time wanted
            if (Globals.useCurrentTime) {

                // Use current time
                currTime = LocalTime.now();

            } else {
                // Else, use static time
                currTime = LocalTime.of(Globals.testHour, 0);
            }
        }

        // Difference holders
        ArrayList<Duration> diffs = new ArrayList<>();
        HashMap<Duration, Connection> diffPairs = new HashMap<>();

        // For all connections
        for (Connection curConn : dirConns) {

            // Get difference to current time
            LocalTime depTime = curConn.getDepTime();
            Duration duration = Duration.between(currTime, depTime);

            // If not negative
            if (!duration.isNegative()) {

                // Add duration and connection pair
                diffPairs.put(duration, curConn);

                // Add duration
                diffs.add(duration);
            }
        }

        // If no durations were added
        if (diffs.isEmpty()) {

            // All connections are negative,
            // so no routes are available (all passed by)
            return new Connection("00:00,UNAVAIL,UNAVAIL,00:00,UNAVAIL");

        } else {

            // Get smallest positive duration
            Duration smallestPos = Collections.min(diffs);

            // Return connection linked to smallest positive duration
            return diffPairs.get(smallestPos);
        }
    }

    /**
     * Update connections list
     */
    private void updateConns() {

        // Read in lines from file
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(fullTTpath));
        } catch (IOException ex) {
            Logger.getLogger(Timetable.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Check first line
        if (!lines.get(0).contains(Globals.stName)) {
            String msg = "Mismatch in first line of: " + fullTTpath;
            throw new IllegalArgumentException(msg);
        }

        // Re-initialize list
        connections = new ArrayList<>();

        // Convert lines to connections
        for (int i = 1; i < lines.size(); i++) {
            connections.add(new Connection(lines.get(i)));
        }

        // Update time last updated
        timeLastUpdated = getTimeLastModified();
    }

    /**
     * Get all connections from this station to the given destination
     *
     * @param dest
     * @return
     */
    private ArrayList<Connection> getDirConns(String dest) {

        // Holder
        ArrayList<Connection> directConns = new ArrayList<>();

        // For all connections
        for (Connection curConn : connections) {

            // If connection goes directly to the desired destination
            if (curConn.getDest().equals(dest)) {

                // Add to list of direction connections
                directConns.add(curConn);
            }
        }

        // Return list
        return directConns;
    }

    /**
     * If timetable file has changed, update connections list
     */
    public void updateIfNeeded() {

        // If time last modified is greater than update time
        if (timeLastUpdated < getTimeLastModified()) {

            // Update connections
            updateConns();
        }
    }

    /**
     * Get last modified time of timetable file
     */
    private long getTimeLastModified() {
        return new File(fullTTpath).lastModified();
    }
}
