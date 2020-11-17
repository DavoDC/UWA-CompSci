package timetable;

import java.time.LocalTime;
import main.Globals;

/**
 * Represents a timetable connection between two stations
 *
 * @author David
 */
public class Connection {

    // Separator
    private final String sep = ",";

    // Departure time
    private final LocalTime depart;

    // Mode of transport (bus number/bus line/train line)
    private String mode;

    // Starting stop
    private String startPoint;

    // Arrival time
    private LocalTime arrive;

    // Destination name
    private String destName;

    /**
     * Create connection from a string representation
     *
     * @param strRep
     */
    public Connection(String strRep) {

        // Split line into parts
        String[] parts = strRep.split(",");

        // If number of parts is not 5
        if (parts.length != 5) {

            // Notify
            String msg = "Bad line: " + strRep;
            throw new IllegalArgumentException(msg);
        }

        // Save parts
        depart = LocalTime.parse(parts[0] + ":00"); // Add ss
        mode = parts[1];
        startPoint = parts[2];
        arrive = LocalTime.parse(parts[3] + ":00"); // Add ss
        destName = parts[4];
    }

    /**
     * Get string rep of connection
     *
     * @return
     */
    @Override
    public String toString() {

        // Holder
        String rep = "";

        // Add departure time
        rep += depart.toString() + sep;

        // Add mode of transport
        rep += mode + sep;

        // Starting stop
        rep += startPoint + sep;

        // Arrival time
        rep += arrive.toString() + sep;

        // Destination name
        rep += destName;

        // Return rep
        return rep;
    }

    /**
     * Get human readable description of the connection
     *
     * @return
     */
    public String getDesc() {
        String desc = "";
        desc += "At " + depart.toString() + ",";
        desc += " catch '" + mode + "' from '";
        desc += startPoint + "', ";
        desc += "and you will arrive at " + arrive.toString();
        desc += " at '" + destName + "'";
        return desc;
    }

    /**
     * Print description of connection
     */
    public void printDesc() {
        Globals.printD("\n" + getDesc());
    }

    /**
     * Get destination name
     *
     * @return
     */
    public String getDest() {
        return destName;
    }

    /**
     * Get arrival time
     *
     * @return
     */
    public LocalTime getArrivalTime() {
        return arrive;
    }

    /**
     * Get departure time
     *
     * @return
     */
    public LocalTime getDepTime() {
        return depart;
    }

    /**
     * Get start point
     *
     * @return
     */
    public String getStartPoint() {
        return startPoint;
    }

    /**
     * Get mode of transport
     *
     * @return
     */
    public String getMode() {
        return mode;
    }

}
