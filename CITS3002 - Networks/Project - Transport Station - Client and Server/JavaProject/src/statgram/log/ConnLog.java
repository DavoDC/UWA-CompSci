package statgram.log;

import java.util.ArrayList;
import timetable.Connection;

/**
 * Represents a log of connections
 *
 * @author David
 */
public class ConnLog extends ArrayList<Connection> {

    // Separator
    public static final String sep = ">>";

    /**
     * Create an empty connection log
     */
    public ConnLog() {

    }

    /**
     * Create a connection log from a string representation
     *
     * @param strRep
     */
    public ConnLog(String strRep) {

        // If empty
        if (strRep.equals("Empty")) {

            // Do not process further
            return;
        }

        // Split into connection representations
        String[] connsS = strRep.split(sep);

        // For each connection string
        for (String connStr : connsS) {

            // Extract connection and add to log
            this.add(new Connection(connStr));
        }
    }

    /**
     * Get string representation
     *
     * @return
     */
    @Override
    public String toString() {

        // If log is empty
        if (isEmpty()) {

            // Notify
            return "Empty";
        }

        // Holder
        String rep = "";

        // Add connections separated
        for (Connection curConn : this) {

            rep += curConn.toString() + sep;
        }

        // Return representation
        return rep;
    }

    /**
     * Get the last connection
     *
     * @return
     */
    public Connection getLast() {
        return get(size() - 1);
    }
}
