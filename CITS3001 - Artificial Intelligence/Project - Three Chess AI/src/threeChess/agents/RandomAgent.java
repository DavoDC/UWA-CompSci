package threeChess.agents;

import threeChess.*;

import java.util.Random;

/**
 * An agent that picks a random move
 *
 * @author David
 */
public class RandomAgent extends Agent {

    // Name
    private static final String name = "Random";

    // RNG
    private static final Random random = new Random();

    /**
     * A no argument constructor, required for tournament management.
     */
    public RandomAgent() {
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
     * is the position to move that piece to. *
     */
    @Override
    public Position[] playMove(Board board) {

        // Gets all pieces on board
        Position[] pieces = board.getPositions(board.getTurn()).
                toArray(new Position[0]);

        // Start and end holders
        Position start = pieces[0];
        Position end = pieces[0];

        // While move is not legal
        while (!board.isLegalMove(start, end)) {

            // Get a random piece
            start = pieces[random.nextInt(pieces.length)];

            // Attempt move
            // - Get type of piece
            Piece mover = board.getPiece(start);
            // - Get legal steps for that piece and choose
            Direction[][] steps = mover.getType().getSteps();
            Direction[] step = steps[random.nextInt(steps.length)];
            int reps = 1 + random.nextInt(mover.getType().getStepReps());

            // Set as end
            end = start;

            // We do steps
            // To find the end position of the piece, 
            // we put end at the start, and take one step (or 
            // several repetitions of a step for bishops, rooks and queens). 
            // After the loop is done, end 
            // will be the final position for the piece.  
            try {
                for (int i = 0; i < reps; i++) {
                    end = board.step(mover, step, end,
                            start.getColour() != end.getColour());
                    // Colour change is for flipping steps 
                    // when moving between board sections.
                }
            } catch (ImpossiblePositionException e) {
            }
        }

        // Return move array
        return new Position[]{start, end};
    }

    /**
     * @return the Agent's name, for annotating game description.
     */
    @Override
    public String toString() {
        return name;
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
