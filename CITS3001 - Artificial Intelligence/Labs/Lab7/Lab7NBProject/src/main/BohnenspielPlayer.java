package main;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Implements an intelligent player for Bohnenspiel.
 *
 * @author David
 */
public class BohnenspielPlayer {

    // ###  Original Variables
    // My name
    private final String name;

    // My color
    private final Farbe color;

    // Used to index the board
    private final int turn;

    // ### My Globals
    // Testing/printing mode
    private final boolean testMode = false;

    // Max houses
    private final int maxHouses = Bohnenspiel.numberofhouses;

    // Infinity constants
    private final int POS_INF = Integer.MAX_VALUE;
    private final int NEG_INF = Integer.MIN_VALUE;

    // Depth limit
    private final int DEPTH_LIMIT = 4;

    // Search time limit
    private final long SEARCH_TIME_LIMIT = 8;

    // Highest search time
    private long maxSearchTime;

    /**
     * Constructs a Bohnenspiel player.
     *
     * @param f
     */
    public BohnenspielPlayer(Farbe f) {

        // Set name to my own
        name = "DaveAI";

        // Save color
        color = f;

        // Set turn using color
        turn = color.ordinal();
    }

    /**
     * Returns a legal move in game, i.e.a number h in [1, 6]. h must denote a
     * non-empty house on this player's side of the board. You can assume that
     * at least one legal move is available. DO NOT RETURN AN ILLEGAL MOVE -
     * that's an automatic loss of game.
     *
     * @param game
     * @return
     */
    public int chooseMove(Bohnenspiel game) {

        // House choice holder
        int houseChoice;

        // If testing
        Instant start;
        if (testMode) {

            // Get time before search
            start = Instant.now();
        }

        // Search for move using minimax with alpha-beta pruning
        houseChoice = alphaBetaSearch(game);

        // If testing
        if (testMode) {

            // Get time after search
            Instant end = Instant.now();

            // Get search time in milliseconds
            Duration searchTimeDur = Duration.between(start, end);
            long searchTime = searchTimeDur.toMillis();

            // Update maximum search time
            maxSearchTime = max(maxSearchTime, searchTime);

            // Print maximum search time if over/equal to limit
            if (maxSearchTime > SEARCH_TIME_LIMIT) {
                println("Search time over limit (" + maxSearchTime + "ms)");
            }
        }

        // Return house choice
        return houseChoice;
    }

    /**
     * Do alpha-beta search and minimax algorithm on a given game state
     *
     * @param game
     * @return
     */
    private int alphaBetaSearch(Bohnenspiel game) {

        // Get valid house choices
        ArrayList<Integer> validChoices;
        validChoices = getValidChoices(game, color);

        // Best utility holder
        int bestUtil = NEG_INF;

        // Best choice holder
        int bestChoice = validChoices.get(0);

        // For every valid house choice
        for (int i = 0; i < validChoices.size(); i++) {

            // Get house choice
            int houseChoice = validChoices.get(i);

            // Get new game state resulting from house choice
            Bohnenspiel curNewGame;
            curNewGame = getNewState(game, color, houseChoice);

            // Get topmost value of game tree
            int curUtil = getMin(curNewGame, Farbe.flip(color),
                    NEG_INF, POS_INF, 1);

            // If current is larger than best
            if (curUtil > bestUtil) {

                // Update best utility
                bestUtil = curUtil;

                // Update best choice
                bestChoice = houseChoice;
            }
        }

        // Return best choice
        return bestChoice;
    }

    /**
     * Get value of MIN node in minimax alpha-beta algorithm
     *
     * @param game Game state
     * @param a Alpha value (lower bound)
     * @param b Beta (higher bound)
     * @param d Depth
     * @return
     */
    private int getMin(Bohnenspiel game, Farbe col,
            int alpha, int beta, int depth) {

        // If at terminal state (leaf node)
        if (game.isOver()) {

            // Return the utility of the game
            return getUtil(game);
        }

        // If depth limit has been reached
        if (depth > DEPTH_LIMIT) {

            // Return the evaluation of this game
            // (Game not over yet)
            return getEval(game);
        }

        // Get valid choices
        ArrayList<Integer> validChoices;
        validChoices = getValidChoices(game, col);

        // Minimum value
        int minVal = POS_INF;

        // For every valid house choice
        for (int i = 0; i < validChoices.size(); i++) {

            // Get new game state
            Bohnenspiel curNewGame;
            curNewGame = getNewState(game, col, validChoices, i);

            // Get current value as max
            int curVal = getMax(curNewGame, Farbe.flip(col),
                    alpha, beta, depth + 1);

            // Reduce minVal to curVal if curVal is smaller
            minVal = min(minVal, curVal);

            // If minVal has gone below or equal to alpha
            if (minVal <= alpha) {

                // Return minimum value
                // (other child nodes do not need checking)
                return minVal;
            }

            // Reduce beta if minVal is smaller
            beta = min(beta, minVal);
        }

        // Return minimum value
        return minVal;
    }

    /**
     * Get value of MAX node in minimax alpha-beta algorithm
     *
     * @param game Game state
     * @param a Alpha value (lower bound)
     * @param b Beta (higher bound)
     * @param d Depth
     * @return
     */
    private int getMax(Bohnenspiel game, Farbe col,
            int alpha, int beta, int depth) {

        // If at terminal state (leaf node)
        if (game.isOver()) {

            // Return the utility of the game
            return getUtil(game);
        }

        // If depth limit has been reached
        if (depth > DEPTH_LIMIT) {

            // Return the evaluation of this game
            // (Game not over yet)
            return getEval(game);
        }

        // Get valid choices
        ArrayList<Integer> validChoices;
        validChoices = getValidChoices(game, col);

        // Maximum value
        int maxVal = NEG_INF;

        // For every valid house choice
        for (int i = 0; i < validChoices.size(); i++) {

            // Get new game state
            Bohnenspiel curNewGame;
            curNewGame = getNewState(game, col, validChoices, i);

            // Get current value as min
            int curVal = getMin(curNewGame, Farbe.flip(col),
                    alpha, beta, depth + 1);

            // Increase maxVal to curVal if curVal is larger
            maxVal = max(maxVal, curVal);

            // If maxVal has gone above or equal to beta
            if (maxVal >= beta) {

                // Return maximum value
                // (other child nodes do not need checking)
                return maxVal;
            }

            // Increase alpha if maxVal is larger
            alpha = max(alpha, maxVal);
        }

        // Return minimum value
        return maxVal;
    }

    /**
     * Get the utility of a given final game state
     *
     * @param pos
     * @return
     */
    private int getUtil(Bohnenspiel game) {

        // Get score difference
        int scoreDiff = getScoreDiff(game);

        // If MAX score is larger
        if (Math.signum(scoreDiff) == 1) {

            // If MAX won
            if (game.isOver()) {

                // Return positive infinity
                // (+∞ if s is a win for MAX)
                return POS_INF;
            } else {

                // Return positive number
                // (Positive if s is good for MAX)
                return scoreDiff;
            }
        } else if (scoreDiff == 0) {

            // If scores are equal, return zero
            // (0 if s is even)
            return 0;
        } else {

            // Else if MIN is doing better
            // If MIN has won
            if (game.isOver()) {

                // Return negative infinity
                // (–∞ if s is a win for MIN)
                return NEG_INF;
            } else {

                // Return negative number
                // (Negative if s is good for MIN)
                return scoreDiff;
            }
        }
    }

    /**
     * Get list of valid house choices
     *
     * @return
     */
    private ArrayList<Integer> getValidChoices(Bohnenspiel game,
            Farbe col) {

        // Holder
        ArrayList<Integer> validChoices = new ArrayList<>();

        // For all house options
        for (int curHouse = 1; curHouse <= maxHouses; curHouse++) {

            // If the house is valid
            if (isValidChoice(game, col, curHouse)) {

                // Add to list
                validChoices.add(curHouse);
            }
        }

        // If there are no valid choices
        if (validChoices.isEmpty()) {

            // Throw exception
            throw new IllegalArgumentException("No valid choices!");
        }

        // Return list
        return validChoices;
    }

    /**
     * Get an evaluation of a non-final game state
     *
     * @param game
     * @return
     */
    private int getEval(Bohnenspiel game) {

        // Holder 
        int evaluation = 0;

        // Add score difference
        evaluation += getScoreDiff(game);

        // Add bean distribution factor (BDF)
        int bdf = 0;
        // - For all house options
        for (int curHouse = 1; curHouse <= maxHouses; curHouse++) {

            // - Add beans from our house
            bdf += getBeansAtHouse(game, color, curHouse);

            // - Subtract beans from their house
            bdf -= getBeansAtHouse(game, Farbe.flip(color),
                    curHouse);
        }
        // - Add with weighting
        double bdfWeight = 0.5;
        evaluation += (int) (bdfWeight * bdf);

        // Return evaluation
        return evaluation;
    }

    /**
     * Get the beans at a given house on a given side/color
     *
     * @param game
     * @param col
     * @param houseIndex
     * @return
     */
    private int getBeansAtHouse(Bohnenspiel game, Farbe col,
            int houseIndex) {
        // Reduce h by 1
        houseIndex--;

        // Convert house index to board index,
        // accounting for color
        int boardIndex;
        boardIndex = houseIndex;
        boardIndex += col.ordinal() * maxHouses;

        // Return number of beans
        return game.getBoard()[boardIndex];
    }

    /**
     * Get the score of a given side/color
     *
     * @param game
     * @param col
     * @return
     */
    private int getScore(Bohnenspiel game, Farbe col) {

        // Get scores
        int[] scores = game.getStores();

        // If color is White/Weisse
        if (col == Farbe.WEISSE) {

            // Return stores[0] which
            // holds the current score for Weisse.
            return scores[0];

        } else {
            // Else, color is Black/Schwarz
            // Return stores[1] which
            // holds the score for Schwarz.
            return scores[1];
        }
    }

    /**
     * Get score difference (us subtract opponent)
     *
     * @param game
     * @return
     */
    private int getScoreDiff(Bohnenspiel game) {

        // Get score of MAX (us)
        int scoreOfMax = getScore(game, color);

        // Get score of MIN (them)
        int scoreOfMin = getScore(game, Farbe.flip(color));

        // Return score difference
        return (scoreOfMax - scoreOfMin);
    }

    /**
     * Get new game state after given house choice
     *
     * @param game Starting state
     * @param col Color
     * @param houseChoice Chosen house
     * @return
     */
    private Bohnenspiel getNewState(Bohnenspiel game, Farbe col,
            int houseChoice) {

        // Make copy of game
        Bohnenspiel newGameState;
        newGameState = game.copyGame(col);

        // Do move on new game state
        newGameState.move(houseChoice);

        // Return new game state
        return newGameState;
    }

    /**
     * Return true if the house choice is valid
     *
     * @param houseIndex
     * @return
     */
    private boolean isValidChoice(Bohnenspiel game, Farbe col,
            int houseIndex) {

        // Get beans at house
        int beans = getBeansAtHouse(game, col, houseIndex);

        // The house is valid if it is not empty
        // Return true if house does NOT have zero beans
        return (beans != 0);
    }

    /**
     * Get new game state after given house choice sourced from a list
     *
     * @param game Starting state
     * @param col Color
     * @param validChoices List of valid house choices
     * @param choicePos Position of valid choice in list
     * @return
     */
    private Bohnenspiel getNewState(Bohnenspiel game, Farbe col,
            ArrayList<Integer> validChoices, int choicePos) {

        // Get house choice
        int houseChoice = validChoices.get(choicePos);

        // Return new game state
        return getNewState(game, col, houseChoice);
    }

    /**
     * Print debug string on new line
     *
     * @param s
     */
    private void println(String s) {
        if (testMode) {
            System.out.println(s);
        }
    }

    /**
     * Stop
     *
     * @param s
     */
    private void stop() {
        if (testMode) {
            System.exit(0);
        }
    }

    /**
     * Returns the player's name.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the player's color.
     *
     * @return
     */
    public Farbe getFarbe() {
        return color;
    }
}
