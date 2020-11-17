 


/**
 * Implements an intelligent player for Bohnenspiel.
 */
public class BohnenspielPlayer {

    // what's my name?
    private final String name;
    // what colour do I have?
    private final Farbe farbe;
    // used to index the board
    private final int turn;

    /**
     * Constructs a Bohnenspiel player.
     */
    public BohnenspielPlayer(Farbe f) {
        name = "Placeholder";
        farbe = f;
        turn = farbe.ordinal();
    }

    /**
     * Returns the player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the player's colour.
     */
    public Farbe getFarbe() {
        return farbe;
    }

    /**
     * Returns a legal move in game, i.e. a number h in [1, 6]. h must denote a
     * non-empty house on this player's side of the board. You can assume that
     * at least one legal move is available. DO NOT RETURN AN ILLEGAL MOVE -
     * that's an automatic loss of game.
     */
    public int chooseMove(Bohnenspiel game) {
        // COMPLETE THIS 
        // Placeholder simply plays a random move
        while (true) {
            int h = (int) (Math.random() * Bohnenspiel.numberofhouses);
            if (game.getBoard()[h + turn * Bohnenspiel.numberofhouses] > 0) {
                return h + 1;
            }
        }
    }
}
