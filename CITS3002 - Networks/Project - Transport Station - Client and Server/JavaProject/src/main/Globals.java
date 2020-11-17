package main;

import comm.BrowserHandler;
import comm.DatagramHandler;
import java.util.ArrayList;
import java.util.HashMap;
import statgram.log.RouteLog;
import statgram.log.RouteStop;
import timetable.Timetable;

/**
 * Application-wide accessible data
 *
 * @author David
 */
public class Globals {

    // >>> Arguments to program
    // Name of the station
    public static String stName;

    // Web browser TCP/IP port
    public static int browserPort;

    // Incoming UDP/IP port for stations
    public static int stPort;

    // Adjacent station UDP/IP ports
    public static ArrayList<Integer> adjacentPorts;

    // >>> Modules
    // Timetable
    public static Timetable timetable;

    // Browser Hanlder
    public static BrowserHandler browserH;

    // Datagram Handler
    public static DatagramHandler datagramH;

    // >>> Adjacent Station Info
    // Neigbour directory - adjacent station names linked to their ports
    public static final HashMap<String, Integer> nbDir = new HashMap<>();

    // List of adjacent stations
    public static final RouteLog adjList = new RouteLog(false);

    // >>> Constants
    // Datagram packet size
    public static final int DATAGRAM_SIZE = 4096;

    // The route stop representing this station
    public static RouteStop thisRS;

    // >>> Settings
    // Timetable folder 
    // Note: runJava.sh ttFolder needs to be updated as well
    //public static String ttFolder = "time36";
    public static String ttFolder = "time6";

    // Time comparison settings
    public static final boolean useCurrentTime = true;
    public static final int testHour = 12 + 5; // 5PM 

    // Debug print settings
    public static final boolean debugOn = false;
    public static final boolean wantSGdesc = true;
    //private static final String printSt = "BusportAB";
    private static final String printSt = "BusportC";
    //private static final String printSt = "JunctionF";
    //private static final String printSt = "TerminalA";
    //private static final String printSt = "StationD";

    // >>> Global Functions
    /**
     * Print debugging string if debugging is on and if we are on the "print
     * station".
     *
     * @param dbs Debugging string
     */
    public static void printD(String dbs) {

        // If debugging is on and we are at the print station
        if (debugOn && stName.equals(printSt)) {

            // Print out string
            System.out.println(dbs);
        }
    }

    /**
     * Returns true if the given destination is adjacent to this station
     *
     * @param dest
     * @return
     */
    public static boolean isAdjacent(String dest) {

        // If the destination is a key in the neighbor directory,
        // it is adjacent
        return nbDir.containsKey(dest);
    }
}
