package threeChess.agents;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import threeChess.Agent;
import threeChess.Board;
import threeChess.Colour;
import threeChess.Position;
import threeChess.agents.utils.Helper;
import threeChess.agents.utils.Move;

/**
 * An agent that uses the paranoid minimax algorithm to choose moves.
 *
 * This algorithm assumes the other two players have teamed up against them and
 * treats them as one opponent
 *
 * @author David
 */
public class ParanoidAgent extends Agent {

    // Depth limit
    private final int depLim;

    // Helper
    private final Helper h;

    // Test mode
    private final boolean testMode = true;

    // Our colour
    private Colour ourCol;

    /**
     * Create default agent. This zero parameter constructor is needed for
     * tournament
     */
    public ParanoidAgent() {

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
        // Get move from paranoid algorithm
        Position[] move = getParaMove(board);

        // Print time taken
        //h.printTimeTaken(testMode);
        // Return playable move
        return move;
    }

    /**
     * Do paranoid minimax algorithm (with alpha beta pruning) on a given game
     * state
     *
     * @param b
     * @return
     */
    private Position[] getParaMove(Board b) {

        // Save our colour
        ourCol = b.getTurn();

        // Get legal moves
        ArrayList<Move> legalMoves = h.getLegalMoves(b);

        // Best utility holder
        int bestUtil = h.NEG_INF;

        // Best move holder
        Move bestMove = legalMoves.get(0);

        // Updates to best
        int updates = 0;

        // For every legal move
        for (Move curMove : legalMoves) {

            // Get new board resulting from move
            Board newBoard = h.getNewBoard(b, curMove);

            // Get node on second level of game tree
            int curUtil = runPara(newBoard, h.NEG_INF, h.POS_INF, 1);

            // If current is larger than best
            if (curUtil > bestUtil) {

                // Update best utility
                bestUtil = curUtil;

                // Update best choice
                bestMove = curMove;

                // Increase updates
                updates++;
            }
        }

        // Return best choice
        // (The highest value node on the second level)
        return h.getFinalMove(updates, legalMoves, bestMove);
    }

    /**
     * Run paranoid minimax algorithm with alpha beta pruning
     *
     * @param board Game state
     * @param a Alpha
     * @param b Beta
     * @param d Depth
     * @return
     */
    private int runPara(Board board, int a, int b, int d) {

        // If game is over (terminal state)
        if (board.gameOver()) {

            // Return the utility of the game
            return getParaUtil(board);
        } else if (d > depLim) {

            // Else if depth limit has been reached,
            // return the evaluation of this unfinished game
            return getParaEval(board);
        } else {

            // Get current colour
            Colour curCol = board.getTurn();

            // Set as true if on Max node
            boolean onMax = curCol.equals(ourCol);

            // Min/Max(imum) value
            int minimaxVal;

            // If on max
            if (onMax) {

                // Start at lowest
                minimaxVal = h.NEG_INF;
            } else {

                // Else if on Min,
                // start at highest
                minimaxVal = h.POS_INF;
            }

            // Get legal moves
            ArrayList<Move> legalMoves;
            legalMoves = h.getLegalMoves(board);

            // For every legal move
            for (Move curMove : legalMoves) {

                // Get new board resulting from move
                Board newBoard = h.getNewBoard(board, curMove);

                // Get current value
                int curVal = runPara(newBoard, a, b, d + 1);

                // If on max node
                if (onMax) {

                    // Increase maxVal to curVal if curVal is larger
                    minimaxVal = max(minimaxVal, curVal);

                    // Increase alpha if maxVal is larger
                    a = max(a, minimaxVal);

                    // If alpha is above or equal to beta
                    if (a >= b) {

                        // Return maximum value
                        // (other child nodes do not need checking)
                        return minimaxVal;
                    }
                } else {

                    // Reduce minVal to curVal if curVal is smaller
                    minimaxVal = min(minimaxVal, curVal);

                    // Reduce beta if minVal is smaller
                    b = min(b, minimaxVal);

                    // If beta is below or equal to alpha
                    if (b <= a) {

                        // Return minimum value
                        // (other child nodes do not need checking)
                        return minimaxVal;
                    }
                }
            }

            // Return minimax value
            return minimaxVal;
        }
    }

    /**
     * Get the Paranoid utility of a given final game state
     *
     * @param b
     * @return
     */
    public int getParaUtil(Board b) {

        // Get score of MAX (us)
        int scoreOfMax = b.score(ourCol);

        // Get score of MIN (them)
        int scoreOfMin = h.getOppTeamSc(b, ourCol);

        // Get score difference (us take them)
        int scoreDiff = scoreOfMax - scoreOfMin;

        // If score difference was positive
        if (Math.signum(scoreDiff) == 1) {

            // This means MAX is doing well
            // If MAX won
            if (b.gameOver()) {

                // Return positive infinity
                // (+∞ if s is a win for MAX)
                return h.POS_INF;
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

            // Else if score difference is negative,
            // this means MIN is doing well
            // If MIN has won
            if (b.gameOver()) {

                // Return negative infinity
                // (–∞ if s is a win for MIN)
                return h.NEG_INF;
            } else {

                // Return negative number
                // (Negative if s is good for MIN)
                return scoreDiff;
            }
        }
    }

    /**
     * Evaluation function for paranoid algorithm
     *
     * @param game
     * @return
     */
    private int getParaEval(Board b) {

        // Holder
        int eval = 0;

        // Add heuristic evaluation
        //eval += h.getHeurEval(b, col);
        // Faster
        eval += h.getScore(b, ourCol);

        // Subtract score of opposite team - steer away from their wins
        // Reduce by factor - lets paranoid play more offensively by
        // reducing emphasis on fighting alliance of players
        //int oppSc = h.getOppTeamSc(b, ourCol);
        //eval -= h.multiply(oppSc, 0.8);
        //
        // Return evaluation
        return eval;
    }

    /**
     * Print node info
     */
    private void printNodeInfo(String t, int v, Colour c,
            int a, int b, int d) {

        // Make string
        String s = "";
        s += "type:" + t + ",";
        s += "val: " + v + ",";
        s += "col: " + c + ",";
        s += "a: " + a + ",";
        s += "b: " + b + ",";
        s += "d: " + d;

        // Fix
        s = s.replace("" + h.NEG_INF, "-INF");
        s = s.replace("" + h.POS_INF, "+INF");

        // Print
        h.println(s, testMode);
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
