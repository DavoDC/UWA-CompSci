package threeChess.agents.utils;

import static java.lang.Math.max;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import threeChess.Board;
import threeChess.Colour;
import threeChess.Direction;
import threeChess.ImpossiblePositionException;
import threeChess.Piece;
import threeChess.PieceType;
import threeChess.Position;

/**
 * Provides helper methods
 *
 * @author David
 */
public class Helper {

    // Infinity constants
    public final int POS_INF = Integer.MAX_VALUE;
    public final int NEG_INF = Integer.MIN_VALUE;

    // Testing toggle
    private final boolean testMode = false;

    // Timing variables
    private Instant startTime;
    private long maxSearchTime;

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
     * Get info string of piece at given position
     *
     * @param b
     * @param p
     * @return
     */
    public static String getPieceInfo(Board b, Position p) {

        // Holder
        String info = "";

        // Get piece
        Piece piece = b.getPiece(p);

        // If piece is null
        if (piece == null) {

            // Return early
            return getPosInfo(p);
        }

        // Add colour
        info += piece.getColour() + " ";

        // Add type
        info += piece.getType().toString();

        // Add position start
        info += " at ";

        // Add position info
        info += getPosInfo(p);

        // Return
        return info;
    }

    /**
     * Print summary of legal moves
     *
     * @param legalMoves
     */
    private void printLegalMovesSummary(ArrayList<Move> legalMoves,
            Board b) {

        // If testing mode is not on
        if (!testMode) {

            // Do not process further
            return;
        }

        // Heading
        println("\nLegal Moves: ");

        // If there are no legal moves
        if (legalMoves.isEmpty()) {

            // Notify
            println("\nNo legal moves!\n");
        } else {

            // Else if there are legal moves:
            // For each move
            legalMoves.forEach(move -> {

                // Print description
                println(move.getDesc(b));
            });
        }
    }

    /**
     * Print time taken
     *
     * @param testMode
     */
    public void printTimeTaken(boolean testMode) {

        // If not testing
        if (!testMode) {

            // Do not process further
            return;
        }

        // Get time after search
        Instant endTime = Instant.now();

        // Get search time in milliseconds
        Duration searchTimeDur;
        searchTimeDur = Duration.between(startTime, endTime);
        long searchTime = searchTimeDur.toMillis();

        // Update maximum search time
        maxSearchTime = max(maxSearchTime, searchTime);

        // Print search time
        String sTime = "";
        sTime += "Search Time: " + searchTime;
        sTime += " (" + maxSearchTime + ")";
        println(sTime);
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

            // Notify
            println("Bad move in getNewBoard()!");
        }

        // Return new board
        return newBoard;
    }

    /**
     * Get a string summarizing a position
     *
     * @param p
     * @return
     */
    public static String getPosInfo(Position p) {

        // Holder
        String info = "";

        // Add colour and coordinate
        info += p.getColour();
        info += "-";
        info += p.toString().substring(1);

        // Return
        return info;
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
     * Get start time for timing
     *
     * @param testMode
     */
    public void setStartTime(boolean testMode) {

        // If testing
        if (testMode) {

            // Set start time
            startTime = Instant.now();
        }
    }

    /**
     * Internal print
     *
     * @param s
     */
    private void println(String s) {
        if (testMode) {
            System.out.println(s);
        }
    }

    /**
     * External print
     *
     * @param s
     * @param localTestMode
     */
    public void println(String s, boolean localTestMode) {
        if (testMode && localTestMode) {
            System.out.println(s);
        }
    }

    /**
     * Stop program
     */
    public void stop() {
        if (testMode) {
            System.exit(0);
        }
    }

    /**
     * Return agent name from class
     *
     * @param aClass
     * @return
     */
    public String getAgentName(Class<?> aClass) {
        return aClass.getSimpleName().replace("Agent", "");
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

    /**
     * Return true if in test mode
     *
     * @return
     */
    public boolean inTestMode() {
        return testMode;
    }
}
