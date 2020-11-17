package threeChess.agents.utils;

import java.util.ArrayList;
import java.util.Arrays;
import threeChess.Board;
import threeChess.Position;

/**
 * Represents a sequence of moves
 */
public class Move {

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
     * Get description
     *
     * @param b
     * @return
     */
    public String getDesc(Board b) {

        // Holder
        String desc = "";

        // Add starting piece
        desc += Helper.getPieceInfo(b, posSeq[0]);

        // Add positions after
        for (int i = 1; i < posSeq.length; i++) {
            desc += " to ";
            desc += Helper.getPosInfo(posSeq[i]);
        }

        // Return desc
        return desc;
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
