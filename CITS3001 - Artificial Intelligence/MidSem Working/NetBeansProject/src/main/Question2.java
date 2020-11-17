package main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Q2 - TSP with Cheapest Insertion
 *
 * @author David
 */
public class Question2 {

    // Distance table
    private final HashMap<String, Integer> distTable;

    /**
     * Constructor
     */
    public Question2() {

        // Initialize pairs
        distTable = new HashMap<>();

        // Col 1
        addPair("AB", 46); // Checked
        addPair("AC", 67); // Checked
        addPair("AD", 45); // Checked
        addPair("AE", 54); // Checked

        // Col 2
        addPair("BC", 50); // Checked
        addPair("BD", 19); // Checked
        addPair("BE", 62); // Checked

        // Col 3
        addPair("CD", 63); // Checked
        addPair("CE", 30); // Checked

        // Col 4
        addPair("DE", 59); // Checked

        // Run algorithm
        processTour("A");
    }

    /**
     * Process a given tour in the form "A-B-C" etc.
     *
     * @param tour
     * @return
     */
    public final String processTour(String tour) {

        // Heading
        System.out.println("######## STEP " + getTourLen(tour)
                + " ########");

        // First case
        if (tour.length() == 1) {

            // Get char
            char start = tour.charAt(0);

            // Notify
            System.out.println("We start at city " + start + ".");
            String expS = "We do not have edges in our tour";
            expS += " so we cannot use the CIM expression yet.";
            System.out.println(expS);
            System.out.println("Thus, we simply choose "
                    + "the closest city initially.");

            // Get and notify about cities not in tour
            ArrayList<Character> remCities = getRemCities(tour, false);

            // Minimum distance
            int minDist = Integer.MAX_VALUE;
            String bestEdge = "";

            // For all cities not in tour
            for (Character curCity : remCities) {

                // Get distance to it
                int curDist = getEdgeDist(start, curCity);

                // Notify
                String msg = "The distance to " + curCity;
                msg += " is " + curDist + ".";
                System.out.println(msg);

                // If curEV is smaller
                if (curDist < minDist) {

                    // Update min
                    minDist = curDist;
                    bestEdge = start + "-";
                    bestEdge += curCity;
                }
            }

            // Notify
            String msg = "We therefore choose ";
            msg += bestEdge.split("-")[1];
            msg += " at the smallest distance of " + minDist;
            System.out.println(msg + ".");
            System.out.println("\n\n");

            // Return processing of best edge
            return processTour(bestEdge);
        }

        // Show tour
        System.out.println("Our tour is now " + tour + ".");

        // Get and notify about edges
        ArrayList<String> edges = getEdges(tour);

        // Get and notify about cities not in tour
        ArrayList<Character> remCities = getRemCities(tour, false);

        // Pairs
        int pairs = remCities.size() * edges.size();
        System.out.println("Thus, there are " + pairs + " pairs, ");
        System.out.println("each of which are assessed "
                + "using the CIM expression:");
        System.out.println("");

        // Minimum expression value
        int minEV = Integer.MAX_VALUE;
        String minPair = "";

        // For all edges
        for (String curEdge : edges) {

            // For all cities not in tour
            for (Character curCity : remCities) {

                // Get variables
                char u = curEdge.charAt(0);
                char v = curEdge.charAt(1);
                char x = curCity;

                // Calculate expression for pair
                int curEV = calcExp(u, v, x);

                // If curEV is smaller
                if (curEV < minEV) {

                    // Update min
                    minEV = curEV;
                    minPair = "(" + u + "," + v + "," + x + ")";
                }

                // Space
                System.out.println("");
            }
        }

        // Print min
        String minS = "The pair that gave the smallest value of ";
        minS += minEV + " was " + minPair + ".";
        System.out.println(minS);
        System.out.println("");

        // Extract variables from best pair
        minPair = minPair.replace("(", "");
        minPair = minPair.replace(")", "");
        String[] parts = minPair.split(",");
        String u = parts[0];
        String v = parts[1];
        String x = parts[2];

        // Get combinations
        String uvS = "(" + u + "," + v + ")";
        String uxS = "(" + u + "," + x + ")";
        String xvS = "(" + x + "," + v + ")";

        // Make new tour
        String newTour;

        // Delete UV
        System.out.println("From the old tour " + tour + ",");
        System.out.println("we delete (u,v), which is " + uvS + ",");
        newTour = tour.replace(v, "X");
        System.out.println("to get " + newTour + " (where X is unknown).");

        // Add UX
        newTour = newTour.replace("X", x);
        System.out.println("We add (u,x), which is " + uxS + ","
                + " producing " + newTour + ".");

        // Add XV
        newTour = newTour.replace(x, x + "-" + v);
        System.out.println("We add (x,v), which is " + xvS + ","
                + " producing " + newTour + ".");

        // If we have reached all cities
        if (getRemCities(newTour, true).isEmpty()) {

            // Notify
            System.out.println("\n");
            System.out.println("######## STEP " + getTourLen(newTour)
                    + " ########");
            System.out.println("The algorithm stops,");
            System.out.println("as we have now completed");
            System.out.println("the circular tour and returned back to A.");

            // Stop and return tour
            return newTour;
        } else {

            // Lots of space
            System.out.println("\n\n");

            // Else go again
            return processTour(newTour);
        }
    }

    /**
     * Calculate insertion method expression
     *
     * @param u
     * @param v
     * @param x
     * @return Expression value
     */
    public int calcExp(char u, char v, char x) {

        // Get formula
        String formula = "CIM(u,v,x) = d(u, x)+d(x, v)–d(u, v)";

        // Get variables and substitute in
        u = Character.toUpperCase(u);
        v = Character.toUpperCase(v);
        x = Character.toUpperCase(x);
        formula = formula.replace('u', u);
        formula = formula.replace('v', v);
        formula = formula.replace('x', x);

        // Calculation
        String calc = formula;

        // Add values of terms
        int first = getEdgeDist(u, x);
        int second = getEdgeDist(x, v);
        int third = getEdgeDist(u, v);
        calc += " = ";
        calc += first + " + ";
        calc += second + " - ";
        calc += third + "";

        // Add expression value
        int ev = first + second - third;
        calc += " = " + ev;

        // Print result
        System.out.println(calc);

        // Return expression value
        return ev;
    }

    /**
     * Get a list of the cities not in the tour
     *
     * @param tour
     * @return
     */
    private ArrayList<Character> getRemCities(String tour, boolean noMsg) {

        // Init list
        ArrayList<Character> remCities = new ArrayList<>();

        // For all cities
        for (char c = 'A'; c <= 'E'; c++) {

            // If the tour does not have that city
            if (!tour.contains(c + "")) {

                // Add to the list
                remCities.add(c);
            }
        }

        // If empty or no message wanted
        if (remCities.isEmpty() || noMsg) {
            // Return empty
            return remCities;
        }

        // Start
        String rcMsg;

        // Get right start
        if (remCities.size() == 1) {
            rcMsg = "The vertex/city not in the tour is ";

            // Add city
            rcMsg += remCities.get(0) + "";
        } else {
            rcMsg = "The vertices/cities not in the tour are ";

            // Add cities
            for (Character city : remCities) {
                rcMsg += city + ",";
            }
        }

        // Add 'and'
        Character last = remCities.get(remCities.size() - 1);
        rcMsg = rcMsg.replace("," + last + ",", " and " + last);

        // Notify about vertices
        System.out.println(rcMsg.trim() + ".");

        // Return list
        return remCities;
    }

    /**
     * Get the edges in a given tour and notify
     *
     * @param tour
     * @return
     */
    private ArrayList<String> getEdges(String tour) {

        // Get tour properties
        String[] cities = tour.split("-");
        int tourLen = getTourLen(tour);
        int lastCityInd = tourLen - 1;

        // Holder
        ArrayList<String> edges = new ArrayList<>();

        // For every city except the last
        for (int i = 0; i < lastCityInd; i++) {

            // Create edge
            String curEdge = cities[i] + cities[i + 1];

            // Add edge
            edges.add(curEdge);
        }

        // If tour is more than two cities
        // (prevents duplicate edges (e.g. connecting D to A in AD))s
        if (tourLen > 2) {

            // Connect last to first
            String first = cities[0];
            String last = cities[lastCityInd];
            edges.add(last + first);
        }

        // Start
        String eMsg = "We have ";

        // Get right start
        if (edges.size() == 1) {
            eMsg += "one edge: ";

            // Add edge
            eMsg += edges.get(0);

        } else {
            eMsg += edges.size() + " edges: ";

            // Add edges
            for (String edge : edges) {
                eMsg += edge + ",";
            }
        }

        // Add 'and'
        String last = edges.get(edges.size() - 1);
        eMsg = eMsg.replace("," + last + ",", " and " + last);

        // Notify about edges
        System.out.println(eMsg.trim() + ".");

        // Return edges
        return edges;
    }

    /**
     * Add pair to map
     *
     * @param edge
     * @param dist
     */
    private void addPair(String edge, int dist) {

        // Add normal 
        distTable.put(edge, dist);

        // Reverse edge letters
        StringBuffer rev = new StringBuffer(edge);
        rev = rev.reverse();
        edge = rev.toString();

        // Add reversed 
        distTable.put(edge, dist);
    }

    /**
     * Get the distance between two given cities
     */
    private int getEdgeDist(char c1, char c2) {

        // Make edge
        String edge = c1 + "" + c2;

        // Return value
        return distTable.get(edge);
    }

    /**
     * Get the length of a tour
     *
     * @param tour
     * @return
     */
    private int getTourLen(String tour) {
        return tour.split("-").length;
    }

}
