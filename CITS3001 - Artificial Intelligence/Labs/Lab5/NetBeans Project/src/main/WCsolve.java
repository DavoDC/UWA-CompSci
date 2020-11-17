package main;

import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Applies uninformed search algorithms to Word Chess puzzles.
 */
public class WCsolve {

    /**
     * Tree Node
     */
    private static class Node extends DefaultMutableTreeNode {

        /**
         * Constructor
         *
         * @param userObject
         */
        public Node(Object userObject) {
            super(userObject);
        }

        /**
         * Print out node information
         */
        public void print() {
            String s = "(" + toString();
            if (isRoot()) {
                s += ", root";
            } else {
                s += ", from '" + getParent().toString() + "'";
            }
            s += ", " + getChildCount() + " children)";
            println(s);
        }

        /**
         * Get the sequence of words this node represents
         *
         * @param seq
         */
        public ArrayList<String> getSequence() {

            // Retrieve path back to root
            Object[] objPath = getUserObjectPath();

            // Convert to string array
            String[] strPath;
            strPath = Arrays.stream(objPath).toArray(String[]::new);

            // Convert to array list
            ArrayList<String> path = new ArrayList<>(objPath.length);
            path.addAll(Arrays.asList(strPath));

            // Return path
            return path;
        }

        /**
         * Get length of word sequence for this node
         *
         * @return
         */
        private int getSeqLen() {
            return (getLevel() + 1);
        }
    }

    // Testing mode
    private static final boolean testMode = false;

    // Parameters
    // Starting word fo sequence
    private static String curStart;
    // Ending/Target word for sequence
    private static String curTarget;
    private static int curTargetLen;
    private static char[] curTargetArr;

    // Simple Sequence Global Variables
    // First valid sequence found
    private static boolean validFound;

    // Best sequence details
    private static ArrayList<String> bestSeq;
    private static int bestSeqLen;

    /**
     * Constructor
     */
    public WCsolve() {
    }

    /**
     * Solves the puzzle start -> target using <breadth-first search>. Returns
     * one optimal solution.
     *
     * Starting from an English word W, and changing only one letter at a time,
     * the puzzle is to derive a second word W’. All intermediate stages must
     * also be valid English words. For a problem like SICK -> WELL, you need to
     * return a solution in the form, showing the steps
     * <"SICK", "SILK", "SILL", "WILL", "WELL">.
     *
     * Note that some of these puzzles require more moves than the number of
     * letters in the words, but it is easy to spot the minimum possible number
     * of moves. Your method is required to find an optimal solution to each
     * puzzle in the input file, “optimal” here meaning the shortest possible
     * sequence of words. Many puzzles will have multiple optimal solutions;
     * your method can return any of those solutions. If a puzzle is impossible,
     * your method can return anything – but it should never go into an infinite
     * loop.
     *
     * @param start
     * @param target
     * @return
     */
    public static ArrayList<String> solve(String start, String target) {

        // Save parameters
        curStart = start;
        curTarget = target;
        curTargetLen = curTarget.length();
        curTargetArr = curTarget.toCharArray();

        // Get sequence of words using only target characters
        // using breadth first search 
        getSequence(false);

        // If no sequence found
        if (bestSeq == null) {

            // Get sequence of words using all characters
            // using breadth first search
            getSequenceAlt();
        }

        // Return final result
        return bestSeq;
    }

    /**
     * Get sequence of words using breadth first search (method1)
     *
     * @param useAll True means use all characters
     * @return
     */
    private static void getSequence(boolean useAll) {

        // Reset global variables
        validFound = false;
        bestSeq = null;
        bestSeqLen = 0;

        // Do breadth-first search
        // Initialize root node with starting word
        Node root = new Node(curStart);

        // Initialize queue and add root
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(root);

        // Initialize discovered list and add root
        ArrayList<String> discovered = new ArrayList<>();
        discovered.add(root.toString());

        // While queue is not empty
        while (!queue.isEmpty()) {

            // Get current node
            Node cur = queue.poll();

            // Add word steps as children
            addSteps(cur, useAll);

            // For each child node
            for (int i = 0; i < cur.getChildCount(); i++) {

                // Retrieve child
                Node curChild = (Node) cur.getChildAt(i);

                // If child has not been discovered
                if (!discovered.contains(curChild.toString())) {

                    // Set as discovered
                    discovered.add(curChild.toString());

                    // Add child to queue
                    queue.add(curChild);
                }
            }
        }
    }

    /**
     * Add word steps as children to a node
     *
     * @param node
     * @param useAll
     */
    private static void addSteps(Node node, boolean useAll) {

        // Print sequence
        // println("S: " + node.getSequence());
        //
        // Get length
        int curLen = node.getSeqLen();

        // Extract string of node
        String nodeS = node.toString();

        // If sequence is valid
        if (nodeS.equals(curTarget)) {

            // If this is the first one
            if (!validFound) {

                // The first valid sequence has been found
                validFound = true;

                // Record length
                bestSeqLen = curLen;

                // Record best sequence
                bestSeq = node.getSequence();

                // Notify
                println(bestSeq + " (1ST SEQ)");

                // Stop
                return;

            } else {

                // Else if this is not the first one
                // If current is larger
                if (bestSeqLen < curLen) {

                    // Stop, longer solutions aren't needed
                    return;
                } else if (bestSeqLen > curLen) {

                    // Else if current is better (shorter),
                    // update best sequence
                    bestSeq = node.getSequence();
                    bestSeqLen = curLen;

                    // Notify
                    println(bestSeq + " (UPDATE)");

                    // Stop, move on to next sequence
                    return;
                }
            }
        }

        // If first valid has been found but current length has crossed limit
        if (validFound && bestSeqLen < curLen) {

            // Stop, longer solutions aren't needed
            return;
        }

        // Try adding steps forward 
        // Try adding steps using target chars only
        addStepsUsingTarget(node, nodeS);

        // If nothing was added AND all characters can be used
        if (node.getChildCount() == 0 && useAll) {

            // Try adding steps using alphabet
            addStepsUsingAlphabet(node, nodeS);
        }
    }

    /**
     * Add steps using characters of target
     *
     * @param node
     * @param chars
     */
    private static void addStepsUsingTarget(Node node, String nodeS) {

        // For each character in the target
        for (int i = 0; i < curTargetLen; i++) {

            // Make new word by substituting character
            String newWord;
            newWord = getWithSubChar(nodeS, i, curTargetArr[i]);

            // Add word
            addWordIfGood(node, newWord);
        }
    }

    /**
     * Add steps using the alphabet
     *
     * @param node
     * @param chars
     */
    private static void addStepsUsingAlphabet(Node node, String nodeS) {

        // For each character in the alphabet
        for (char a = 'A'; a <= 'Z'; a++) {

            // For each character in the string
            for (int i = 0; i < nodeS.length(); i++) {

                // Make new word by substituting character
                String newWord = getWithSubChar(nodeS, i, a);

                // Add word if good
                addWordIfGood(node, newWord);
            }
        }
    }

    /**
     * Add a given word to a given node if good
     *
     * @param node
     * @param newWord
     */
    private static void addWordIfGood(Node node, String newWord) {

        // If word is already in path
        if (node.getSequence().contains(newWord)) {

            // Don't add, no duplicates needed
            return;
        }

        // If word is valid
        if (isValidWord(newWord)) {

            // Add as child node
            node.add(new Node(newWord));
        }
    }

    /**
     * Get sequence of words using breadth first search (method2). Slower but
     * more optimal answers
     *
     * @return
     */
    private static void getSequenceAlt() {

        // Initialize running list and add start word
        LinkedList<String> running = new LinkedList<>();
        running.add(curStart);

        // Initialize list of words already encountered and add start word
        ArrayList<String> encountered = new ArrayList<>();
        encountered.add(curStart);

        // Initialize pairs of word steps
        HashMap<String, String> steps = new HashMap<>();

        // While running list still has items
        while (!running.isEmpty()) {

            // Get first element in list
            String first = running.poll();

            // For every character in starting word
            for (int s = 0; s < curStart.length(); s++) {

                // For every character in alphabet
                for (char a = 'A'; a <= 'Z'; a++) {

                    // Create new word by substituting character
                    String newWord = getWithSubChar(first, s, a);

                    // If we have not seen this word
                    if (!encountered.contains(newWord)) {

                        // If word is valid
                        if (isValidWord(newWord)) {

                            // Add to running list
                            running.add(newWord);

                            // Set as encountered
                            encountered.add(newWord);

                            // Add as step
                            steps.put(newWord, first);
                        }
                    }

                    // If new word is target
                    if (newWord.equals(curTarget)) {

                        // Create holder for solution
                        ArrayList<String> solution = new ArrayList<>();

                        // Add ending
                        solution.add(curTarget);

                        // Construct sequence working backwards
                        boolean generating = true;
                        while (generating) {

                            // Get first word
                            String firstWord = solution.get(0);

                            // Get matching back step
                            String backStep = steps.get(firstWord);

                            // Add as first in list
                            solution.add(0, backStep);

                            // If start reached
                            if (backStep.equals(curStart)) {

                                // Stop
                                break;
                            }
                        }

                        // Update best sequence
                        bestSeq = solution;

                        // Finished processing
                        return;
                    }
                }
            }
        }
    }

    /**
     * Get the given string with a substituted character
     *
     * @param orig
     * @param pos
     * @param newChar
     * @return
     */
    private static String getWithSubChar(String orig, int pos, char newChar) {

        // Create string builder from original
        StringBuilder sb;
        sb = new StringBuilder(orig);

        // Substitute character
        sb.setCharAt(pos, newChar);

        // Convert string builder into string
        return sb.toString();
    }

    /**
     * Return true if the given word is valid
     *
     * @param query
     * @return
     */
    private static boolean isValidWord(String query) {

        // Use provided method
        return WordChess.isWord(query);
    }

    /**
     * Print debug string on new line
     *
     * @param s
     */
    private static void println(String s) {
        if (testMode) {
            System.out.println(s);
        }
    }

    /**
     * Print debug string
     *
     * @param s
     */
    private static void print(String s) {
        if (testMode) {
            System.out.print(s);
        }
    }

    /**
     * Stop
     *
     * @param s
     */
    private static void stop() {
        if (testMode) {
            System.exit(0);
        }
    }
}
