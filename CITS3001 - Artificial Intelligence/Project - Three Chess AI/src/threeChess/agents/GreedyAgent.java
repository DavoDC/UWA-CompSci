package threeChess.agents;

import java.util.ArrayList;
import threeChess.Agent;
import threeChess.Board;
import threeChess.Colour;
import threeChess.Position;
import threeChess.agents.utils.Helper;
import threeChess.agents.utils.Move;

/**
 * An agent that simply selects the move that produces the best score
 *
 * @author David
 */
public class GreedyAgent extends Agent {

    // Helper
    private final Helper h;

    /**
     * Create default agent. This zero parameter constructor is needed for
     * tournament
     */
    public GreedyAgent() {

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

        // Get our colour
        Colour ourCol = board.getTurn();

        // Get legal moves
        ArrayList<Move> legalMoves = h.getLegalMoves(board);

        // Best move holder
        Move bestMove = legalMoves.get(0);

        // Best score holder
        int bestScore = h.NEG_INF;

        // Number of updates to best score
        int updateCount = 0;

        // For every legal move
        for (Move curMove : legalMoves) {

            // Get new game board resulting from move
            Board newBoard = h.getNewBoard(board, curMove);

            // DISABLED: Get evaluation of new board
            //int curScore = h.getHeurEval(newBoard, ourCol);
            // Get score in new board
            int curScore = h.getScore(newBoard, ourCol);

            // If current is larger than best
            if (curScore > bestScore) {

                // Update best score
                bestScore = curScore;

                // Update best move
                bestMove = curMove;

                // Increase update count
                updateCount++;
            }
        }

        // Return final move
        return h.getFinalMove(updateCount, legalMoves, bestMove);
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
