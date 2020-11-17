package threeChess.agents;

import java.util.logging.Level;
import java.util.logging.Logger;
import threeChess.Agent;
import threeChess.Board;
import threeChess.Colour;
import threeChess.ImpossiblePositionException;
import threeChess.Position;
import threeChess.agents.utils.Helper;

/**
 * Agent that copies the last move done
 *
 * @author David
 */
public class CopyCatAgent extends Agent {

    // Random agent to fall back on
    private final RandomAgent ra;

    // Helper
    private final Helper h;

    /**
     * Zero parameter constructor
     */
    public CopyCatAgent() {

        // Initialize
        ra = new RandomAgent();
        h = new Helper();
    }

    /**
     * Play a move in the game. The agent is given a Board Object representing
     * the position of all pieces, the history of the game and whose turn it is.
     * They respond with a move represented by a pair (two element array) of
     * positions: the start and the end position of the move.
     *
     * @param board The representation of the game state.
     * @return a two element array of Position objects, where the first element
     * is the current position of the piece to be moved, and the second element
     * is the position to move that piece to.
     */
    @Override
    public Position[] playMove(Board board) {

        // Get move count
        int moveCount = board.getMoveCount();

        // If at least one move has been done
        if (moveCount > 0) {

            // Get last start as ours
            Position ourStart = getLastPartAsOurs(board, moveCount, 0);

            // Get last end as ours
            Position ourEnd = getLastPartAsOurs(board, moveCount, 1);

            // If move is legal
            if (board.isLegalMove(ourStart, ourEnd)) {

                // Return move
                return new Position[]{ourStart, ourEnd};
            }
        }

        // Otherwise, fall back on random agent
        return ra.playMove(board);
    }

    /**
     * Get part of last move done and convert to our move
     *
     * @return
     */
    private Position getLastPartAsOurs(Board board, int moveCount, int part) {

        // Get last move
        Position[] lastMove = board.getMove(moveCount - 1);

        // Get last part and colour
        Position lastPart = lastMove[part];
        Colour lastCol = lastPart.getColour();

        // Get next colour (ours)
        Colour nextCol = getNextCol(lastCol);

        // Get row and column of previous part
        int row = lastPart.getRow();
        int col = lastPart.getColumn();

        // Make same move but with our colour
        Position ourPart = null;
        try {
            ourPart = Position.get(nextCol, row, col);
        } catch (ImpossiblePositionException ex) {
            Logger.getLogger(CopyCatAgent.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        // Return
        return ourPart;
    }

    /**
     * Get next colour starting from given colour
     *
     * @param col
     * @return
     */
    public Colour getNextCol(Colour col) {

        // Extract index of colour
        int index = col.ordinal();

        // Increase to get next colour (and wrap around)
        index = (index + 1) % 3;

        // Convert index to get the next colour
        return Colour.values()[index];
    }

    /**
     * Return agent's name
     *
     * @return
     */
    @Override
    public String toString() {
        return "CopyCat";
    }

    /**
     * Displays the final board position to the agent, if required for learning
     * purposes. Other a default implementation may be given.
     *
     * @param finalBoard the end position of the board
     */
    @Override
    public void finalBoard(Board finalBoard) {

    }
}
