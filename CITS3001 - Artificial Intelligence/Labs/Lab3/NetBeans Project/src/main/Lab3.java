package main;

import java.io.*;

/**
 * Exercises the TSP algorithms for a folder of problem instances.
 */
public class Lab3 {

    // The folder of test files
    private static final String tsptestdata = "../TestData";

    // One problem instance
    private static TSP tsp;

    // The output file
    private static final String outfile = "../Output/outTSP.txt";

    // The output file handle
    private static PrintWriter out;

    /**
     * Constructor
     */
    private Lab3() {
    }

    /**
     * Reads in each problem in the folder, runs the algorithms, and displays
     * the output.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            out = new PrintWriter(outfile);
            for (File file : new File(tsptestdata).listFiles()) {

                // Uses every file in the folder
                if (file.isFile()) {
                    out.println(file.getName());
                    tsp = new TSP(tsptestdata + "/" + file.getName());
                    int[] cs = tsp.tspnn();
                    display("Nearest neighbour", cs);
                    cs = tsp.tsp2opt(cs);
                    display("Improved", cs);
                    out.println();
                }
            }
            out.close();

            System.out.println("\nSuccessful!");
            System.out.println("Output: " + outfile + "\n");

        } catch (FileNotFoundException e) {
            System.out.println("Something wrong with " + outfile);
        }
    }

    /**
     * Displays s, cs, and its performance. cs is standardized to start from 0
     * and so that the next city is a smaller index.
     */
    private static void display(String s, int[] cs) {
        out.println(s);

        // Find City 0
        int k = 0;
        while (cs[k] != 0) {
            k++;
        }
        // Select direction based on k+1 and k-1
        if (cs[(k + 1) % cs.length] < cs[(k - 1 + cs.length) % cs.length]) {
            for (int c = 0; c < cs.length; c++) {
                out.print(cs[(c + k) % cs.length] + " ");
            }
        } else {
            for (int c = cs.length; c > 0; c--) {
                out.print(cs[(c + k) % cs.length] + " ");
            }
        }
        out.println();
        out.println("cost " + String.format("%.2f", tsp.validate(cs)));
    }
}
