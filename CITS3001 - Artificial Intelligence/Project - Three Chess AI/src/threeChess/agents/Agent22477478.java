package threeChess.agents;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import threeChess.Agent;
import threeChess.Board;
import threeChess.Colour;
import threeChess.Direction;
import threeChess.ImpossiblePositionException;
import threeChess.Piece;
import threeChess.PieceType;
import threeChess.Position;

/**
 * My standalone tournament threeChess agent
 *
 * This agent alternates between using a Paranoid and greedy agent
 *
 * The Paranoid agent is used the most as it achieves the highest win rate
 * against the other agents
 *
 * The use of two different agents make the agent more unpredictable and thus
 * harder to beat
 *
 * Moreover, using the greedy agent helps the agent to use less time
 *
 * @author David
 */
public class Agent22477478 extends Agent {

    // Helper
    private final Helper h;

    // Paranoid agent
    private final ParanoidAgent para;

    /**
     * Zero parameter constructor for tournament
     */
    public Agent22477478() {

        // Initialize helper
        h = new Helper();

        // Initialize Paranoid agent
        para = new ParanoidAgent();
    }

    /**
     * Play move given board
     *
     * @param board
     * @return
     */
    @Override
    public Position[] playMove(Board board) {

        // Move holder
        Position[] move;

        // Get random number from 0 to 100
        int ran = h.getRandInt(100);

        // Use number to decide which agent's move to use. 
        if (ran < 60) {

            // Use Paranoid agent move 60% of the time (0 - 59)
            move = para.playMove(board);
        } else {

            // Use Greedy agent move 40% of the time (60 - 100)
            move = getGreedyMove(board);
        }

        // Return move
        return move;
    }

    /**
     * Get a greedy move for a given game board
     *
     * @param board
     * @return
     */
    public Position[] getGreedyMove(Board board) {

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
     * Return agent's name
     *
     * @return
     */
    @Override
    public String toString() {
        return "TournamentAgent22477478";
    }

    /**
     * Final Board
     *
     * @param finalBoard
     */
    @Override
    public void finalBoard(Board finalBoard) {

    }

    /**
     * Simplified version of paranoid agent for playing moves
     *
     * An agent that uses the paranoid minimax algorithm to choose moves.
     *
     * This algorithm assumes the other two players have teamed up against them
     * and treats them as one opponent
     *
     * @author David
     */
    private class ParanoidAgent {

        // Depth limit
        private final int depLim;

        // Our colour
        private Colour ourCol;

        /**
         * Create default agent. This zero parameter constructor is needed for
         * tournament
         */
        public ParanoidAgent() {

            // Save depth limit
            this.depLim = 2;
        }

        /**
         * Returns a move to play given a game state
         *
         * @param board The representation of the game state.
         * @return a move that is a Position array with the start and end
         * positions
         */
        public Position[] playMove(Board board) {

            // Get move from paranoid algorithm
            Position[] move = getParaMove(board);

            // Return playable move
            return move;
        }

        /**
         * Do paranoid minimax algorithm (with alpha beta pruning) on a given
         * game state
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
    }

    /**
     * Simplified helper class for helper methods
     *
     * @author David
     */
    private class Helper {

        // Infinity constants
        public final int POS_INF = Integer.MAX_VALUE;
        public final int NEG_INF = Integer.MIN_VALUE;

        // Random number generator
        private final Random rng;

        /**
         * Constructor
         */
        public Helper() {

            // Initialize random number generator
            rng = new Random(System.currentTimeMillis());
        }

        /**
         * Get legal moves for the current player of a given board
         *
         * @param board Game board/state
         * @return
         */
        public ArrayList<Move> getLegalMoves(Board board) {

            // Get current player colour
            Colour col = board.getTurn();

            // List of legal moves
            ArrayList<Move> legalMoves = new ArrayList<>();

            // Get positions of all the player's pieces
            Position[] piecePositions;
            piecePositions = board.getPositions(col).toArray(new Position[0]);

            // For each of the player's piece positions
            for (Position curPos : piecePositions) {

                // Get current position as piece
                Piece curPiece = board.getPiece(curPos);

                // Extract type of piece
                PieceType curType = curPiece.getType();

                // If no moves have been done,
                // and we are not on a Pawn or Knight
                boolean noMoves = board.getMoveCount() == 0;
                boolean notPawnOrKnight = !(curType.equals(PieceType.PAWN)
                        || curType.equals(PieceType.KNIGHT));
                if (noMoves && notPawnOrKnight) {

                    // Skip this position/piece,
                    // as if we are not a Pawn or a Knight
                    // there is no way that we
                    // can move at the very start
                    continue;
                }

                // TEST
                // println("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ "
                //        + getPieceInfo(board, curPos));
                // Get legal moves of current piece
                ArrayList<Move> lmop;
                lmop = getLMOP(board, curPos, curPiece, curType);

                // Add legal moves of this piece to the
                // list of legal moves for all pieces
                legalMoves.addAll(lmop);
            }

            // Print summary of legal moves
            //printLegalMovesSummary(legalMoves, board);
            // Return holder
            return legalMoves;
        }

        /**
         * Get legal moves of a given piece
         *
         * @return
         */
        private ArrayList<Move> getLMOP(Board b, Position start,
                Piece piece, PieceType type) {

            // Holds legal moves of this piece
            ArrayList<Move> lmop = new ArrayList<>();

            // Get steps piece can take
            Direction[][] steps = type.getSteps();

            // Get repetitions available
            int reps = type.getStepReps();

            // For each step in steps
            for (Direction[] step : steps) {

                // TEST
                //println("########## curStep: " + Arrays.toString(step));
                // Get board copy for move sequence in this direction
                Board bCopy = getBoardCopy(b);

                // Add legal moves for step
                addLMForStep(start, reps, bCopy, step, piece, lmop);
            }

            // Return legal moves of piece
            return lmop;
        }

        /**
         * Add legal moves for step
         *
         * @param start The starting position
         * @param reps The number of step repetitions
         * @param b The board (a copy)
         * @param step The step
         * @param piece The piece
         * @param lmop The current list of legal moves of the piece
         */
        private void addLMForStep(Position start, int reps, Board b,
                Direction[] step, Piece piece, ArrayList<Move> lmop) {

            // Get position sequence for this direction
            // (Size of 8 because there is never more than 8 reps/positions)
            ArrayList<Position> dirPosSeq = new ArrayList<>(8);

            // Add starting position
            dirPosSeq.add(start);

            // For each repetition
            for (int curRep = 0; curRep < reps; curRep++) {

                // TEST
                //println("  # Rep " + curRep + " of " + (reps - 1));
                try {

                    // Get two last positions in sequence
                    // - Get indices
                    int lastInd = dirPosSeq.size() - 1;
                    int secLastInd = lastInd - 1;
                    // - Correct if negative
                    if (Math.signum(secLastInd) == -1) {
                        // Use starting position as both on first rep
                        secLastInd = 0;
                    }
                    // - Retrieve from position sequence list
                    Position secLastPos = dirPosSeq.get(secLastInd);
                    Position lastPos = dirPosSeq.get(lastInd);

                    // If the starting colour is not the same
                    // as the ending colour, the step needs to be
                    // reversed as it crosses into a new colour
                    // area of the board
                    Colour col1 = secLastPos.getColour();
                    Colour col2 = lastPos.getColour();
                    boolean rev = col1 != col2;

                    // Get next position by trying step from last position
                    Position nextPos = b.step(piece, step, lastPos, rev);

                    // If going from true starting position
                    // to next is legal
                    if (b.isLegalMove(start, nextPos)) {

                        // Add position to sequence
                        dirPosSeq.add(nextPos);

                        // Convert position sequence to move
                        Move curMove = new Move(dirPosSeq);

                        // If we do not already have this move
                        if (!lmop.contains(curMove)) {

                            // Add the current move
                            lmop.add(curMove);

                            // TEST
                            //println("   *** Added new legal move! : "
                            //        + curMove.toString());
                        } else {

                            // TEST
                            //println("   * We already have : "
                            //        + curMove.toString());
                            // If on high rep
                            if (curRep >= 1) {

                                // TEST
                                //println("    @@@@@@@@@@@@@@@ STOPPED"
                                //        + " - following seq already done");
                                // No need to do anymore reps,
                                // as this means you are following
                                // a sequence already done
                                break;
                            }
                        }
                    } else {

                        // Else if not legal step,
                        // throw exception
                        throw new ImpossiblePositionException("");
                    }
                } catch (ImpossiblePositionException ex) {

                    // Else if not legal move:
                    // If on second or higher rep
                    if (curRep >= 1) {

                        // TEST
                        //println("    @@@@@@@@@@@@@@@ STOPPED"
                        //        + " - path is blocked");
                        // No need to do anymore reps,
                        // as this means a Queen/Bishop/Rook
                        // has hit a piece or wall,
                        // and you cannot go through/over it
                        break;
                    }
                }
            }
        }

        /**
         * Get heuristic evaluation of board for given player
         *
         * @param b
         * @param col
         * @return
         */
        public int getHeurEval(Board b, Colour col) {

            // Holder
            int eval = 0;

            // Add our score
            eval += getScore(b, col);

            // For all player positions
            Set<Position> positions = b.getPositions(col);
            for (Position curPos : positions) {

                // Extract piece
                Piece curPiece = b.getPiece(curPos);

                // Extract piece value
                int curVal = curPiece.getValue();

                // Add measure of central board control
                // If this position is a central position
                // If on border
                if (curPos.getRow() == 3) {

                    // If in middle of border
                    int column = curPos.getColumn();
                    if (column >= 2 && column <= 5) {

                        // If not King
                        // (No bonus for being in centre,
                        // as King should stay back)
                        if (curVal != 40) {

                            // Base value for being in centre
                            // Was 4
                            int value = 1;

                            // Bonus from piece type
                            int bonus = multiply(curVal, 0.1);

                            // Add sum
                            eval += value + bonus;
                        }
                    }
                }

                // If piece is valuable
                if (curVal >= 5) {

                    // For each direction
                    for (Direction curDir : Direction.values()) {

                        // Adjacent position
                        Position adjPos;
                        try {
                            // Attempt to get adjacent position
                            adjPos = curPos.neighbour(curDir);

                        } catch (ImpossiblePositionException ex) {

                            // Skip this direction if invalid
                            continue;
                        }

                        // Attempt to convert to piece
                        Piece adjPiece = b.getPiece(adjPos);

                        // If adjacent piece exists
                        if (adjPiece != null) {

                            // If adjacent piece is our colour
                            if (adjPiece.getColour().equals(col)) {

                                // Base value for protecting piece
                                // WAS 3
                                int value = 1;

                                // Add bonus for being a weaker piece,
                                // decrease for being too valuable
                                int adjVal = adjPiece.getValue();
                                if (adjVal <= 3) {
                                    int limit = PieceType.KNIGHT.getValue() + 1;
                                    value += limit - adjVal;
                                }

                                // Add bonus for value of piece being protected
                                // WAS 0.2
                                value += multiply(curVal, 0.1);

                                // Add to eval
                                eval += value;
                            }
                        }
                    }
                }
            }

            // Return value
            return eval;
        }

        /**
         * Get score of opposing team
         *
         * @param b
         * @param ourCol our colour
         * @return
         */
        public int getOppTeamSc(Board b, Colour ourCol) {

            // Score
            int totalScore = 0;

            // For all players
            for (Colour curCol : Colour.values()) {

                // If colour is not us
                if (!curCol.equals(ourCol)) {

                    // Add to score
                    totalScore += b.score(curCol);
                }
            }

            // To make score of 2 other players comparable, 
            // take average of score (divide by 2)
            totalScore = multiply(totalScore, 0.5);

            // Return score
            return totalScore;
        }

        /**
         * Get new board after applying move
         *
         * @param board
         * @param move
         * @return
         */
        public Board getNewBoard(Board board, Move move) {

            // Make copy of board
            Board newBoard = getBoardCopy(board);

            try {

                // Apply move
                newBoard.move(move.getStart(), move.getEnd());
            } catch (ImpossiblePositionException ex) {
            }

            // Return new board
            return newBoard;
        }

        /**
         * Get final move
         *
         * @param updateCount Updates to best utility
         * @param legalMoves Legal moves list
         * @param bestMove Best move found
         * @return
         */
        public Position[] getFinalMove(int updateCount,
                ArrayList<Move> legalMoves, Move bestMove) {

            // If update count was 1
            if (updateCount == 1) {

                // This means all moves have the same utility,
                // so return a random legal move
                //
                // Get random index
                int ranInd = getRandInt(legalMoves.size());

                // Get random legal move
                Move ranMove = legalMoves.get(ranInd);

                // Return as playable move
                return ranMove.getPlayable();
            } else {

                // Else if there are varying utilities,
                // return best move
                return bestMove.getPlayable();
            }
        }

        /**
         * Get copy of given board
         *
         * @param b
         * @return
         */
        public Board getBoardCopy(Board b) {
            try {
                return (Board) b.clone();
            } catch (CloneNotSupportedException e) {
            }
            return null;
        }

        /**
         * Multiply an integer by a factor and round down
         *
         * @param value
         * @param factor
         * @return
         */
        public int multiply(int value, double factor) {
            return (int) ((double) value * factor);
        }

        /**
         * Get score of a given player
         *
         * @param b
         * @param ourCol
         * @return
         */
        public int getScore(Board b, Colour ourCol) {
            return b.score(ourCol);
        }

        /**
         * Get random integer from 0 to (excBound - 1)
         *
         * @param excBound Exclusive bound
         * @return
         */
        public int getRandInt(int excBound) {
            return rng.nextInt(excBound);
        }
    }

    /**
     * Simplified move class
     */
    private class Move {

        // Underlying sequence of positions
        private final Position[] posSeq;

        /**
         * Create move sequence from position sequence list and board
         *
         * @param posSeqList Position sequence
         */
        public Move(ArrayList<Position> posSeqList) {

            // Convert list to array and save
            // (Note: This has minimal performance cost)
            this.posSeq = posSeqList.toArray(new Position[0]);
        }

        /**
         * Override equals (to make list.contains() work)
         *
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o) {

            // If object is an instance of move
            if (o instanceof Move) {

                // Convert to playable move
                Position[] inputPMove = ((Move) o).getPlayable();

                // Return true if matches this move's playable move
                return Arrays.equals(this.getPlayable(), inputPMove);
            }

            // Otherwise return false
            return false;
        }

        /**
         * Get move sequence as playable move (only has start and end)
         *
         * @return
         */
        public Position[] getPlayable() {

            // Holder
            Position[] pMove = new Position[2];

            // Add start and end
            pMove[0] = getStart();
            pMove[1] = getEnd();

            // Return playable move
            return pMove;
        }

        /**
         * Override hash code (to go with equals() override)
         *
         * @return
         */
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 41 * hash + Arrays.deepHashCode(this.getPlayable());
            return hash;
        }

        /**
         * Get ending position of move sequence
         *
         * @return
         */
        public final Position getEnd() {

            // Return last
            return posSeq[posSeq.length - 1];
        }

        /**
         * Get starting position of move sequence
         *
         * @return
         */
        public final Position getStart() {

            // Return first
            return posSeq[0];
        }
    }
}
