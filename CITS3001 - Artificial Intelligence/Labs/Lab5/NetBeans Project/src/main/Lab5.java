package main;

import java.util.*;

/**
 * Plays Word Chess on a file of puzzles. Both of the input files are in the
 * Java folder; if you move them, just change the variables below.
 */
public class Lab5 {

    // Root folder
    private static final String root = "../Textfiles/";

    // The file of puzzles
    private static final String puzzleFile = root + "Puzzles.txt";

    // The (cut-down) file of words from Words With Friends
    private static final String wordsFile = root + "WWF uppercase.txt";

    // The list of legal words
    public static final ArrayList<String> wordsList = new FileIO(wordsFile).getLines();

    /**
     * Constructor
     */
    private Lab5() {
    }

    /**
     * Exercises the solver(s) on the file of puzzles.
     *
     * @param args
     */
    public static void main(String[] args) {

        // Counter
        int rounds = 0;
        int correct = 0;

        // Space
        System.out.println();

        // Current seq
        String full = "";

        // For all lines in puzzle file
        for (String l : new FileIO(puzzleFile).getLines()) {

            // Stop signla
            if (l.contains("STOP")) {
                break;
            }

            // Ignore blank lines and lines that start with //
            if (!l.isBlank() && l.length() >= 2
                    && !l.substring(0, 2).equals("//")) {

                // Ignore ## lines
                if (!l.contains("##")) {

                    // Split into two words and check
                    String[] ws = l.split(" ");
                    assert ws.length >= 2 : "illegal line in " + puzzleFile;

                    // Increase rounds
                    rounds++;

                    // Print heading
                    String h = "########## ROUND " + rounds + " ##########";
                    System.out.println(h);

                    // Run algorithm
                    WordChess wc = new WordChess(ws[0], ws[1]);
                    ArrayList<String> seq = wc.solve();

                    // Get result as string
                    full = "";
                    for (String step : seq) {
                        full += step + " ";
                    }
                    full = full.trim();
                }
            }

            // Comments for actual answer
            if (l.contains("##")) {

                // Print result
                System.out.println(full + " (RESULT)");

                // Get actual
                String actual = l.substring(3, l.length()).trim();

                // Check correctness
                if (full.equals(actual)) {
                    System.out.println("CORRECT! (Perfectly)");
                    correct++;
                } else if (full.length() == actual.length()) {
                    System.out.println("CORRECT! (Optimal alternative)");
                    correct++;
                } else {
                    System.out.println(actual + " (ACTUAL)");
                    System.out.println("FIX!");
                }

                // Spacer
                System.out.println();
            }
        }

        // Print final results
        String corrPerc = ((int) (correct * 100.0f) / rounds) + "%";
        String fraction = correct + " out of " + rounds;
        System.out.println(fraction + " (" + corrPerc + ")");
        System.out.println();
    }
}
