package threeChess.agents;

import java.util.ArrayList;
import threeChess.Agent;
import threeChess.Board;
import threeChess.Colour;
import threeChess.Position;
import threeChess.agents.utils.Helper;
import threeChess.agents.utils.Move;

/**
 * An agent that uses the MaxN algorithm to choose moves
 *
 * @author David
 */
public class MaxNAgent extends Agent {

    // Depth limit
    private final int depLim;

    // Helper
    private final Helper h;

    // Test mode
    private final boolean testMode = false;

    /**
     * Create default agent. This zero parameter constructor is needed for
     * tournament
     */
    public MaxNAgent() {

        // Save depth limit
        this.depLim = 2;

        // Init helper
        h = new Helper();
    }

    /**
     * Returns a move to play given a game state
     *
     * @param board The representation of the game state.
     * @return a move that is a Position array with the start and end positions
     */
    @Override
    public Position[] playMove(Board board) {

        // Set start time for timing
        //h.setStartTime(testMode);
        // Search for move using the MaxN algorithm
        Position[] move = getMaxNMove(board);

        // Print time taken
        //h.printTimeTaken(testMode);
        // Return playable move
        return move;
    }

    /**
     * Do MaxN algorithm to get move from a given game state
     *
     * @param game
     * @return
     */
    private Position[] getMaxNMove(Board b) {

        // TEST
        h.println("\n NEW MOVE", testMode);

        // Save our colour and index
        Colour ourCol = b.getTurn();
        int ourInd = ourCol.ordinal();

        // Get legal moves
        ArrayList<Move> legalMoves = h.getLegalMoves(b);

        // Best utility holder
        int[] bestUtil = {h.NEG_INF, h.NEG_INF, h.NEG_INF};

        // Best move holder
        Move bestMove = legalMoves.get(0);

        // Updates to best utility
        int updates = 0;

        // For every legal move
        for (Move curMove : legalMoves) {

            // Get new board resulting from our move
            Board newBoard = h.getNewBoard(b, curMove);

            // Get node on second level of game tree
            int[] curUtil = runMaxN(newBoard, 1);

            // TEST
            String msg = "util: " + curUtil[ourInd];
            msg += " , " + curMove.getDesc(b);
            h.println(msg, testMode);

            // If current is larger than best
            if (bestUtil[ourInd] < curUtil[ourInd]) {

                // Update best utility
                bestUtil = curUtil;

                // Update best choice
                bestMove = curMove;

                // Increase update count
                updates++;

                // TEST
                h.println("Updated best to " + bestUtil[ourInd], testMode);
            }
        }

        // Return best choice
        // (The highest value node on the second level)
        return h.getFinalMove(updates, legalMoves, bestMove);
    }

    /**
     * Run MaxN algorithm
     *
     * @param b Board
     * @param d Depth
     * @return
     */
    private int[] runMaxN(Board b, int d) {

        // Holder
        int[] arr = new int[3];

        // If at terminal state (leaf node)
        // or depth limit has been reached
        if (b.gameOver() || d > depLim) {

            // For all players
            for (Colour c : Colour.values()) {

                // If game over
                if (b.gameOver()) {

                    // Save utility
                    arr[c.ordinal()] = getMaxNUtil(b, c);

                } else if (d > depLim) {

                    // Else if depth limit reached,
                    // save evaluation
                    arr[c.ordinal()] = getMaxNEval(b, c);
                }
            }
        } else {

            // Get player colour and index
            Colour curCol = b.getTurn();
            int curIndex = curCol.ordinal();

            // Set array to lowest
            arr = new int[]{h.NEG_INF, h.NEG_INF, h.NEG_INF};

            // Get legal moves
            ArrayList<Move> legalMoves = h.getLegalMoves(b);

            // For every legal move
            for (Move curMove : legalMoves) {

                // Get new board resulting from our move
                Board newBoard = h.getNewBoard(b, curMove);

                // Get current array from MaxN
                int[] curArr = runMaxN(newBoard, d + 1);

                // If score of player is better in current array
                if (arr[curIndex] < curArr[curIndex]) {

                    // Update array
                    arr = curArr;
                }
            }
        }

        // Return util/eval array
        return arr;
    }

    /**
     * Get simple MaxN utility for given player
     *
     * @param b Board
     * @param col Player's colour
     * @return
     */
    public int getMaxNUtil(Board b, Colour col) {

        // If game is not over
        if (!b.gameOver()) {

            // Notify as function should not be called
            h.println("Incorrect call to getSimpUtil()!", testMode);
        }

        // If we won
        if (b.getWinner().equals(col)) {

            // Return largest value
            return h.POS_INF;
        } else if (b.getLoser().equals(col)) {

            // Else if we lost, return smallest value
            return h.NEG_INF;
        } else {

            // Else if game is over,
            // but didnt win or lose,
            // return 0 for a draw
            return 0;
        }
    }

    /**
     * Get MaxN evaluation function value
     *
     * @param b
     * @param c
     * @return
     */
    private int getMaxNEval(Board b, Colour c) {

        // Holder
        int eval = 0;

        // Add score
        eval += h.getScore(b, c);

        // Add heuristic evaluation
        // Slower
        //eval += h.getHeurEval(b, col);
        //
        // Return evaluation
        return eval;
    }

    /**
     * Return the agent's name
     *
     * @return
     */
    @Override
    public String toString() {
        return h.getAgentName(this.getClass());
    }

    /**
     * Act on final board
     *
     * @param finalBoard the finished game state
     */
    @Override
    public void finalBoard(Board finalBoard) {

    }
}
