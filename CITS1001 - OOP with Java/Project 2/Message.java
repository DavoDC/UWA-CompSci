import java.util.ArrayList;

/**
 * 
 * Models a message
 * 
 * @author Arran, David, Kyle
 *
 */
public class Message 
{

    /** 
     * If you work out the correct deck order to use
     * when decrypting the message -- the "deck string"
     * (comma-separated values which can be used to re-create
     * a Deck object in that order) should be stored in this
     * instance variable. */
    private String deckString;

    /** 
     * If you work out what the decrypted message is --
     * it should be stored in this instance variable 
     */
    private String decryptedText;

    /**	
     * The encrypted message you find.
     */
    public final static String encryptedText = 
        "DGNKAJBQKCGBOOYHCINCKDDXXIZVYLDFFKNXDZZAQFRNNRGBSMASCE";

    /** 
     * The partial card deck ordering you find -- with one
     * card missing.
     */	
    public final static int partialDeck[] = { 
            8, 48, 52, 13, 14, 47, 18, 19, 20, 11,  2, 
            25, 26, 27, 28, 29, 23, 32,  9, 53, 17, 12, 
            15,  1, 30, 31, 33, 34, 24, 35, 21, 22,  3, 
            4, 16, 41, 54, 36, 37, 38, 50, 42, 43, 44,
            45, 46, 40, 51, 49,  5,  6,  7, 10 };

    /** 
     * Return the "deck string" -- when used to
     * construct a Deck object, this string should
     * produce a Deck with its cards in
     * the correct order for decrypting the message contained
     * in "encryptedText".
     * 
     * @return Returns a string that can be passed to Deck(String inputString).
     */
    public String getDeckString() {
        return deckString;
    }

    /** 
     * Return the "decrypted text" -- this should be the result
     * of decrypting the message in "encryptedText".
     * 
     * @return Returns the decrypted text
     */
    public String getDecodedMessage() {
        return decryptedText;
    }

    /** 
     * Given a position at which the missing card (the King of Hearts) could be, this method returns a String which
     * can be used to construct a Deck with the King at that position.
     * 
     * @param kingPos A possible position for the King of Hearts
     * @return A String which can be used to initialize a Deck
     */
    public String tryPosition(int kingPos) {      
        ArrayList<Integer> newDeck = new ArrayList<>();

        for (int i : partialDeck)
        {
            newDeck.add(i);
        }
        newDeck.add(kingPos, 39);

        String output = "";
        for (int i : newDeck)
        {
            output += (i + ",");
        }
        
        return output.substring( 0 , output.length() - 1);  // Substring removes extra comma
    }

    /**
     * Tests various positions at which the missing card could be, 
     * by calling tryPosition() to obtain a "deck string" for that position.
     * 
     * Once the correct position for the King is found, the constructor stores:
     * - the decrypted message in the "decryptedText" instance variable 
     * - the "deck string" in the "deckString" instance variable.
     */
    public Message() {
        String inputText = Encoder.sanitize(encryptedText);
        
        for (int i = 0; i != 54 ; i++) {
            Encoder encoder = new Encoder(new Deck(tryPosition(i)));
            String message = encoder.decrypt(inputText);

            if (message.contains("TREASURE")) {
                deckString = tryPosition(i);
                decryptedText = message;
            }
        }
    }

}
