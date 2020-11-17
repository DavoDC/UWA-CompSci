package statgram;

import statgram.log.ConnLog;
import statgram.log.RouteLog;
import statgram.log.RouteStop;
import java.net.DatagramPacket;
import java.time.LocalTime;
import java.util.Map.Entry;
import java.util.Set;
import main.Globals;
import timetable.Connection;

/**
 * Models a "station datagram", a "statgram", used for station
 * inter-communication
 *
 * @author David
 */
public class StatGram {

    // Separator
    public static final String sep = "&";

    // Name Separator
    public static final String nameSep = "=";

    // 0 = Protocol name
    private final String protoName = "UDP_DC";

    // 1 = Type
    private String type;

    // 2 = Status
    private Status status;

    /**
     * Status enumeration
     */
    public enum Status {
        LookingForSrc,
        LookingForDest
    }

    // 3 = Source stop
    private RouteStop srcStop;

    // 4 = Destination stop
    private RouteStop destStop;

    // 5 = Log of route stops visited
    private RouteLog routeLog;

    // 6 = Log of route stops to avoid
    private RouteLog avoidLog;

    // 7 = Used to get back to the source when final route has been acquired
    private RouteLog btLog;

    // 8 = Holds the connections that describe the final result/route
    private ConnLog connLog;

    /**
     * Make default statgram
     *
     * @param type Infogram or Routegram
     * @param status
     * @param srcStop
     * @param destStop
     * @param routeLog
     * @param avoidLog
     * @param btLog
     * @param connRes
     */
    public StatGram(String type, Status status,
            RouteStop srcStop, RouteStop destStop,
            RouteLog routeLog, RouteLog avoidLog, RouteLog btLog,
            ConnLog connRes) {

        // Save parameters
        this.type = type;
        this.status = status;
        this.srcStop = srcStop;
        this.destStop = destStop;
        this.routeLog = routeLog;
        this.avoidLog = avoidLog;
        this.btLog = btLog;
        this.connLog = connRes;
    }

    /**
     * Make a statgram from string representation
     *
     * @param strRep
     */
    public StatGram(String strRep) {

        // Split into parts
        String[] parts = strRep.split(sep);

        // Load 1 = Type of Statgram
        type = extractValue(parts, 1);

        // Load 2 = Status
        String statusS = extractValue(parts, 2);
        if (statusS.contains("Src")) {
            status = Status.LookingForSrc;
        } else {
            status = Status.LookingForDest;
        }

        // Load 3 = Source stop
        srcStop = new RouteStop(extractValue(parts, 3));

        // Load 4 = Destination stop
        destStop = new RouteStop(extractValue(parts, 4));

        // Load 5 = Add route log
        routeLog = new RouteLog(extractValue(parts, 5));

        // Load 6 = Add log of route stops to avoid
        avoidLog = new RouteLog(extractValue(parts, 6));

        // Load 7 = Add log of stops to backtrack
        btLog = new RouteLog(extractValue(parts, 7));

        // Load 8 = Add connections for final result
        connLog = new ConnLog(extractValue(parts, 8));
    }

    /**
     * Make a statgram from a datagram packet
     *
     * @param dp
     */
    public StatGram(DatagramPacket dp) {

        // Convert DP to string and convert string to statgram
        this(new String(dp.getData(), 0, dp.getLength()));
    }

    /**
     * Retrieve a string representation of the statgram for sending in datagram
     * packets
     *
     * @return
     */
    @Override
    public String toString() {
        // Holder
        String rep = "";

        // Add 0 = Protocol name
        rep += addPart("Protocol", protoName);

        // Add 1 = Type of Statgram
        rep += addPart("Type", type);

        // Add 2 = Status
        rep += addPart("Status", status.toString());

        // Add 3 = Source stop
        rep += addPart("Source", srcStop.toString());

        // Add 4 = Destination stop
        rep += addPart("Destination", destStop.toString());

        // Add 5 = Add route log
        rep += addPart("RouteLog", routeLog.toString());

        // Add 6 = Add log of route stops to avoid
        rep += addPart("AvoidLog", avoidLog.toString());

        // Add 7 = Add log of stops to backtrack
        rep += addPart("BackTrack", btLog.toString());

        // Add 8 = Add connections for final result
        rep += addPart("ConnLog", connLog.toString());

        // Return
        return rep;
    }

    /**
     * Print out a description of this statgram
     *
     * @param action What has happened to the statgram
     */
    public void printDesc(String action) {

        // If statgrams descriptions not wanted
        if (!Globals.wantSGdesc) {

            // Do not go further
            return;
        }

        // State station name and type of statgram
        String msg = "\n ### ";
        msg += this.getType() + " " + action + " by ";
        msg += Globals.stName + "!";
        Globals.printD(msg);

        // Print payload info
        String[] parts = this.toString().split(StatGram.sep);
        for (int i = 0; i < parts.length; i++) {
            Globals.printD(" " + i + "=" + parts[i]);
        }

        // Space
        Globals.printD("");
    }

    /**
     * Add to connection log
     *
     * @param port
     */
    public void addToConnLog(int port) {

        // If port is not adjacent
        if (!Globals.adjacentPorts.contains(port)) {
            String msg = "addToConnLog: " + port + " was not adjacent";
            throw new IllegalArgumentException(msg);
        }

        // Convert port to name
        String dest = null;
        Set<Entry<String, Integer>> entrySet;
        entrySet = Globals.nbDir.entrySet();
        for (Entry<String, Integer> curEnt : entrySet) {

            // If port of entry matches query
            if (curEnt.getValue() == port) {

                // Save name linked
                dest = curEnt.getKey();
            }
        }

        // Comparison time in time calculation
        LocalTime compTime = null;

        // If connection log is not empty
        if (!connLog.isEmpty()) {

            // Use the last connection's arrival time as the comparison time
            Connection last = connLog.getLast();
            compTime = last.getArrivalTime();
        }

        // Get next best connection for that destination and time
        Connection best = Globals.timetable.getNextDepStr(dest, compTime);

        // Add to connection log
        connLog.add(best);
    }

    /**
     * Extract the value section of a data piece
     *
     * @param parts
     * @param index
     * @return
     */
    private String extractValue(String[] parts, int index) {
        return parts[index].split(nameSep)[1];
    }

    /**
     * Add a piece of data to a string, name and value
     *
     * @param name
     * @param value
     * @return
     */
    private String addPart(String name, String value) {
        return (name + nameSep + value + sep);
    }

    /**
     * Set the type of the statgram
     *
     * @param newType
     */
    public final void setType(String newType) {
        type = newType;
    }

    /**
     * Set the destination route stop
     *
     * @param destRS
     */
    public void setDestRS(RouteStop destRS) {
        this.destStop = destRS;
    }

    /**
     * Update status
     *
     * @param status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Retrieve route log
     *
     * @return
     */
    public final RouteLog getRouteLog() {
        return routeLog;
    }

    /**
     * Retrieve avoid log
     *
     * @return
     */
    public final RouteLog getAvoidLog() {
        return avoidLog;
    }

    /**
     * Get backtracking log
     *
     * @return
     */
    public final RouteLog getBTLog() {
        return btLog;
    }

    /**
     * Get connection log
     *
     * @return
     */
    public ConnLog getConnLog() {
        return connLog;
    }

    /**
     * Get the destination stop
     *
     * @return
     */
    public RouteStop getDestStop() {
        return destStop;
    }

    /**
     * Get the source stop
     *
     * @return
     */
    public RouteStop getSrcStop() {
        return srcStop;
    }

    /**
     * Get status
     *
     * @return
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Get type
     *
     * @return
     */
    public String getType() {
        return type;
    }
}
