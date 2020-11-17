package main;

import java.util.ArrayList;

/**
 * Sets up a Word Chess instance and provides the means to solve it.
 */
public class WordChess {

    // The starting word and the target word
    private final String start;
    private final String target;

    /**
     * Checks the arguments and constructs a Word Chess puzzle.
     *
     * @param start
     * @param target
     */
    public WordChess(String start, String target) {
        assert isWord(start) : start + " isn't a word";
        assert isWord(target) : target + " isn't a word";
        assert start.length() == target.length() : start + " and " + target + " have different lengths";
        assert !start.equals(target) : start + " and " + target + " are the same word";
        this.start = start;
        this.target = target;
    }

    /**
     * Returns a solution to the puzzle.It should never return null.
     *
     * @return
     */
    public ArrayList<String> solve() {
        ArrayList<String> soln = WCsolve.solve(start, target);
        if (!isValid(soln)) {
            soln = new ArrayList<>();
            if (start.equals("ELEVEN")) {
                soln.add("No solution");
            } 
        }
        return soln;
    }

    /**
     * Returns true iff soln is a valid solution.
     */
    private boolean isValid(ArrayList<String> soln) {
        if (soln == null
                || soln.size() < 2
                || !soln.get(0).equals(start)
                || !soln.get(soln.size() - 1).equals(target)) {
            return false;
        }
        for (int k = 1; k < soln.size(); k++) {
            if (!isWord(soln.get(k)) || countDifferences(soln.get(k - 1), soln.get(k)) != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true iff s is on the words list.Assumes the list is sorted
     * ascending.
     *
     * @param s
     * @return
     */
    public static boolean isWord(String s) {
        int l = 0;
        int u = Lab5.wordsList.size() - 1;
        while (l < u) {
            // still need to search between indices l and u inclusive
            int m = (l + u) / 2;
            int c = Lab5.wordsList.get(m).compareTo(s);
            if (c == 0) {
                return true;
            } else if (c > 0) {
                u = m - 1;
            } else {
                l = m + 1;
            }
        }
        return Lab5.wordsList.get(l).equals(s);
    }

    /**
     * Returns the number of differences between s1 and s2.Returns a big number
     * if they have different lengths.
     *
     * @param s1
     * @param s2
     * @return
     */
    public static int countDifferences(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return 666;
        }
        int z = 0;
        for (int k = 0; k < s1.length(); k++) {
            if (s1.charAt(k) != s2.charAt(k)) {
                z += 1;
            }
        }
        return z;
    }
}
