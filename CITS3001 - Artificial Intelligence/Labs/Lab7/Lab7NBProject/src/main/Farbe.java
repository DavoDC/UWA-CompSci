package main;

/**
 * Enumerates the colours in Bohnenspiel.
 */
public enum Farbe {
    WEISSE, SCHWARZ;

    /**
     * Returns the first player to move.
     *
     * @return
     */
    public static Farbe first() {
        return WEISSE;
    }

    /**
     * Returns the colour opposite to f.
     *
     * @param f
     * @return
     */
    public static Farbe flip(Farbe f) {
        if (f == WEISSE) {
            return SCHWARZ;
        } else {
            return WEISSE;
        }
    }
}
