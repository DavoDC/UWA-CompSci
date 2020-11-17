package statgram.log;

import java.util.ArrayList;
import main.Globals;

/**
 * Represents a log of routes taken
 *
 * @author David
 */
public class RouteLog extends ArrayList<RouteStop> {

    // Separator
    public static final String sep = "->";

    /**
     * Create an route log
     *
     * @param addStart
     */
    public RouteLog(boolean addStart) {

        // If start desired
        if (addStart) {

            // Add start
            this.add(Globals.thisRS);
        }
    }

    /**
     * Create a route log from a string representation
     *
     * @param strRep
     */
    public RouteLog(String strRep) {

        // If empty
        if (strRep.equals("Empty")) {

            // Do not process further
            return;
        }

        // Split into route stop representations
        String[] routeStopsS = strRep.split(sep);

        // For each route stop string
        for (String stopStr : routeStopsS) {

            // Extract route stop and add to log
            this.add(new RouteStop(stopStr));
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

        // Add route stops separated
        for (RouteStop curRS : this) {
            rep += curRS.toString() + sep;
        }

        // Return representation
        return rep;
    }

    /**
     * Add route stop
     *
     * @param rs
     */
    public void addRS(RouteStop rs) {

        // If stop is not already in list
        if (!this.contains(rs)) {

            // Add to list
            this.add(rs);
        }
    }
}
