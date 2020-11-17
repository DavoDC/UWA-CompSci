package main;

import java.util.ArrayList;

/**
 * Implements the Nearest Neighbor algorithm for the TSP, and an iterative
 * improvement method that uses 2-OPT greedily. Results are returned as an array
 * of indices into the table argument, e.g. if the table has length four, a
 * valid result would be {2,0,1,3}.
 *
 */
public class NearestNeighbour {

    // Controls whether printing occurs
    private static final boolean printOn = false;

    // Current distance table
    private static double[][] curTable;

    // Current number of cities
    private static int curCities;

    // Current tour distance
    private static double curTourDist;

    // Counter
    private static int round;

    /**
     * Constructor
     */
    private NearestNeighbour() {
    }

    /**
     * Returns the shortest tour found by exercising the NN algorithm from each
     * possible starting city in table. Tries starting from every city and
     * returns the best tour found.
     *
     * @param table
     * @return
     */
    public static int[] tspnn(double[][] table) {

        // Update table and number of cities
        curTable = table;
        curCities = table.length;

        // Current best tour (initialize to tour from first city)
        int[] bestTour = getNNTourFromCity(0);

        // For every city (other than the first)
        for (int i = 1; i < curCities; i++) {

            // Get the tour starting from the city
            int[] curTour = getNNTourFromCity(i);

            // If the current tour is better
            if (isFirstTourBetter(curTour, bestTour)) {

                // Make the current tour the best tour
                bestTour = curTour;
            }
        }

        // Return the best tour
        return bestTour;
    }

    /**
     * Get nearest neighbor tour starting from a given city. The Nearest
     * Neighbor (NN) algorithm: 1. Select an initial city 2. Always visit next
     * the closest unvisited city (the tour always grows from the end.)
     * Table[i][j] == table[j][i] gives the cost of travel between City i and
     * City j.
     *
     * @param startCity
     * @return
     */
    private static int[] getNNTourFromCity(int startCity) {

        // Make a list of the available cities
        ArrayList<Integer> avail = new ArrayList<>();
        for (int curCity = 0; curCity < curCities; curCity++) {
            avail.add(curCity);
        }

        // An array of the cities visited
        int[] visited = new int[curCities];
        int visInd = 0;

        // Visit start city and make unavailable
        visited[visInd] = startCity;
        visInd++;
        removeCity(startCity, avail);

        // While there are still cities to visit
        while (visInd != curCities) {

            // Get current city
            int curCity = visited[visInd - 1];

            // Nearest neighbour variable (init to first)
            int nearestCity = avail.get(0);

            // Best distance variable (init to first)
            double bestDist = curTable[nearestCity][curCity];

            // For each available city (other than the first)
            for (int acInd = 1; acInd < avail.size(); acInd++) {

                // Get current available city
                int curAC = avail.get(acInd);

                // Get distance to available city
                double curDist = curTable[curAC][curCity];

                // If current distance is better
                if (curDist < bestDist) {

                    // Update city and distance
                    nearestCity = curAC;
                    bestDist = curDist;
                }
            }

            // Visit nearest city and make unavailable
            visited[visInd] = nearestCity;
            visInd++;
            removeCity(nearestCity, avail);
        }

        // Return tour
        return visited;
    }

    /**
     * Uses 2-OPT repeatedly to improve cs, choosing the shortest option in each
     * iteration.You can assume that cs is a valid tour initially.
     *
     * Implement a simple hill-climber to improve your solutions. Apply 2-OPT
     * from Page 38 of Lecture 4 iteratively for the improvement step; at each
     * iteration, retain the change which gives the biggest improvement. Return
     * the current tour when it cannot be improved any further.
     *
     * @param cs
     * @param table
     * @return
     */
    public static int[] tsp2opt(int[] cs, double[][] table) {

        // Get details
        curTourDist = roundtrip(cs);
        curCities = cs.length;

        // While changes can still be made
        boolean keepGoing = true;
        while (keepGoing) {

            // Reset
            keepGoing = false;

            // Holder
            double bestDiff = 0;
            int[] bestOption = null;

            // For every city
            for (int x = 0; x < curCities - 1; x++) {

                // For every other city
                for (int y = 0; y < curCities - 1; y++) {

                    // Change tour to create different one
                    int[] diffTour = do2OptChange(cs, x, y);
                    double diffDist = roundtrip(diffTour);

                    // Calculate difference
                    double curDiff = curTourDist - diffDist;

                    // If difference is tiny positive number
                    if (curDiff > 0 && curDiff < 0.0001) {

                        // Correct to zero
                        curDiff = 0;
                    }

                    // Initialize best difference to first difference
                    if (x == 0 && y == x) {
                        bestDiff = curDiff;
                    }

                    // If current diff is better
                    if (curDiff > bestDiff) {

                        // Update best diff
                        bestDiff = curDiff;

                        // Save option associated with it
                        bestOption = diffTour;
                    }
                }
            }

            // If no improvement could be made
            // (i.e. was 0 (same) or worse (larger tour time))
            if (bestDiff <= 0) {

                // Stop processing
                break;

            } else if (bestOption != null) {

                // Else if improvement could be made
                // Update tour
                cs = bestOption;

                // Keep going
                keepGoing = true;
            }
        }

        // Print progress
        round++;
        println((int) ((round * 100.0f) / 30) + "% complete");

        // Return improved tour
        return cs;
    }

    /**
     * Try doing 2 opt change on given tour, with given start and end
     *
     * @param cs
     * @param start
     * @param end
     * @return
     */
    private static int[] do2OptChange(int[] cs, int start, int end) {

        // Holder
        int[] res = new int[cs.length];

        // Add up to start
        for (int index = 0; index <= start - 1; index++) {
            res[index] = cs[index];
        }

        // Add cities reversed between start and end
        int factor = 0;
        for (int index = start; index <= end; index++) {
            res[index] = cs[end - factor];
            factor++;
        }

        // Add remaining
        for (int index = end + 1; index < curCities; index++) {
            res[index] = cs[index];
        }

        // Return result
        return res;
    }

    /**
     * Print out standard info for a given tour (adapted from 'display' in Lab3)
     *
     * @param tour
     */
    private static void printTour(String header, int[] cs) {
        // Heading
        println("\n" + header);

        // Print tour
        int k = 0;
        while (cs[k] != 0) {
            k++;
        }
        if (cs[(k + 1) % cs.length] < cs[(k - 1 + cs.length) % cs.length]) {
            for (int c = 0; c < cs.length; c++) {
                print(cs[(c + k) % cs.length] + " ");
            }
        } else {
            for (int c = cs.length; c > 0; c--) {
                print(cs[(c + k) % cs.length] + " ");
            }
        }

        // Print distance
        println("\ncost " + String.format("%.2f", roundtrip(cs)));
    }

    /**
     * Print out distance table
     *
     * @param table
     */
    private static void printTable(double[][] table) {

        // Space
        println("");

        // Print numbers as ints
        for (double[] row : table) {
            for (double num : row) {
                print((int) num + " ");
            }
            println("");
        }

        // Space
        println("");
    }

    /**
     * Returns the total cost for a given tour (adapted from TSP.java)
     */
    private static double roundtrip(int[] cs) {
        int n = curCities;
        double z = curTable[cs[0]][cs[n - 1]];
        for (int k = 0; k < n - 1; k++) {
            z += curTable[cs[k]][cs[k + 1]];
        }
        return z;
    }

    /**
     * Remove city from list of available cities
     *
     * @param cityNum
     * @param avail
     */
    private static void removeCity(Integer cityNum, ArrayList<Integer> avail) {
        if (!avail.remove(cityNum)) {
            println("Removal issue: " + cityNum);
        }
    }

    /**
     * Returns true if the first tour is better
     */
    private static boolean isFirstTourBetter(int[] t1, int[] t2) {
        return (roundtrip(t1) < roundtrip(t2));
    }

    /**
     * Print debug string on new line
     *
     * @param s
     */
    private static void println(String s) {
        if (printOn) {
            System.out.println(s);
        }
    }

    /**
     * Print debug string
     *
     * @param s
     */
    private static void print(String s) {
        if (printOn) {
            System.out.print(s);
        }
    }

}
