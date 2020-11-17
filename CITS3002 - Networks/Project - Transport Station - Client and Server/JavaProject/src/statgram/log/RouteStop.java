package statgram.log;

import java.util.Objects;

/**
 * Represents a stop in a route
 *
 * @author David
 */
public class RouteStop {

    // Separator
    private final String sep = "~~";

    // The station's name
    private final String name;

    // The station's port
    private int port;

    /**
     * Create a route stop from values
     *
     * @param name
     * @param port
     */
    public RouteStop(String name, int port) {
        this.name = name;
        this.port = port;
    }

    /**
     * Create a route stop from a string representation
     *
     * @param strRep
     */
    public RouteStop(String strRep) {
        String[] parts = strRep.split(sep);
        name = parts[0];
        port = Integer.parseInt(parts[1]);
    }

    /**
     * Get string representation
     *
     * @return
     */
    @Override
    public String toString() {
        return (name + sep + port);
    }

    /**
     * Replacement for equals function
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        RouteStop comp = null;
        if (obj instanceof RouteStop) {
            comp = (RouteStop) obj;
        } else {
            throw new IllegalArgumentException("RouteStop equals");
        }
        boolean samePort = this.port == comp.getPort();
        boolean sameName = this.name.equals(comp.getName());
        return sameName && samePort;
    }

    /**
     * Replacement for hash code function
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.sep);
        hash = 53 * hash + Objects.hashCode(this.name);
        hash = 53 * hash + this.port;
        return hash;
    }

    /**
     * Update the port
     *
     * @param newPort
     */
    public void setPort(int newPort) {
        port = newPort;
    }

    /**
     * Get station name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Get station port
     *
     * @return
     */
    public int getPort() {
        return port;
    }

}
