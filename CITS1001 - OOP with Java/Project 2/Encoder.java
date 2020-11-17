/**
 * 
 * Models an encoder
 * 
 * @author Arran, David, Kyle 
 *
 */
public class Encoder 
{
    public static final int ALPHABET_SIZE = 26;
    private final Deck deck;  

    /** 
     * Create an Encoder using the default ordering of a deck of cards.
     * 
     */
    public Encoder() {
        deck = new Deck();
    }

    /** 
     * Create an Encoder using a particular deck of cards to generate the key.
     * 
     */
    public Encoder(Deck d) {
        deck = d;
    }

    /** Remove all non-alphabetic characters from a string, and convert all alphabetic characters to upper-case.
     * 
     * @param s Input string
     * @return Sanitized string
     */
    public static String sanitize(String s) {
        return s.replaceAll("[^A-Za-z]","").toUpperCase();  
    }

    /** 
     * Return the position in the alphabet of an uppercase character, starting from 1 
     * (i.e., charToInt('A') returns 1, charToInt('B') returns 2, and so on).
     * 
     * @param c Character to convert to an int
     * @return Result of conversion
     */
    public static int charToInt(char c) {
        return ( (int) (c - 64));
    }

    /** 
     * Given a position in the alphabet (starting from 1), return the character at that position. 
     * (i.e. intToChar(1) returns 'A', intToChar(2) returns 'B', and so on). 
     * If a number larger than 26 is passed in, subtract 26 from it before applying this conversion.
     * 
     * @param c int to convert to a character
     * @return Result of conversion
     */
    public static char intToChar(int i) {
        if (i > 52)
        {
            throw new IllegalArgumentException();
        }

        if (i > ALPHABET_SIZE) 
        { 
            i -= ALPHABET_SIZE; 
        }
        return ( (char) (i + 64) );
    }

    /** 
     * Encode a character (inputChar) using a character from the keystream (keyChar).
     * 
     * To do this, firstly convert both characters into integers, as described in charToInt.
     * 
     * Then add the numbers together. If the result is greater than 26,
     * subtract 26 from it; then convert that result back to a character.
     * 
     * @param inputChar Character from message
     * @param keyChar Character from keystream
     * @return Encoded character
     */
    public static char encodeChar(char inputChar, char keyChar) {
        int sum = charToInt(inputChar) + charToInt(keyChar);
        
        if (sum > ALPHABET_SIZE) 
        { 
            sum -= ALPHABET_SIZE; 
        }
        
        return intToChar(sum);
    }

    /** 
     * Decode a character (inputChar) from an encrypted message using a character from the keystream (keyChar).
     * 
     * Convert both characters to integers, as described for charToInt. If inputChar is less than or equal to keyChar,
     * add 26 to it. Then subtract keyChar from inputChar, and convert the result to a character.
     * 
     * @param inputChar Character from an encrypted message
     * @param keyChar Character from keystream
     * @return Decoded character
     */
    public static char decodeChar(char inputChar, char keyChar) {
        int inputVal = charToInt(inputChar);
        int keyVal = charToInt(keyChar);

        if (inputVal <= keyVal) 
        { 
            inputVal += ALPHABET_SIZE; 
        }

        return  intToChar(inputVal - keyVal);
    }

    /** 
     * Encode the string inputText using the keystream characters in keyChars, by repeatedly calling encodeChar.
     * 
     * @param inputText Message text to encode
     * @param keyChars Characters from keystream
     * @return Encoded message
     * @throws Throws IllegalArgumentException if the inputted strings are of a different length
     */
    public static String encodeString(String inputText, String keyChars) {
        if ( inputText.length() != keyChars.length() )
        {
            throw new IllegalArgumentException();
        }
        String encoded = "";

        int keyCharPos = 0;
        for (char curchar : inputText.toCharArray()) //For every character in inputText
        {
            encoded += encodeChar(curchar, keyChars.charAt(keyCharPos));
            keyCharPos++;
        }

        return encoded;
    }

    /** 
     * Decode the string inputText using the keystream characters in keyChars, by repeatedly calling decodeChar.
     * 
     * @param  inputText Encoded text which needs to be decoded
     * @param  keyChars Characters from keystream
     * @return Decoded message
     * @throws Throws IllegalArgumentException if inputted strings are of a different length
     */
    public static String decodeString(String inputText, String keyChars) {
        if ( inputText.length() != keyChars.length() )
        {
            throw new IllegalArgumentException();
        }
        
        String decoded = "";

        int keyCharPos = 0;
        for (char curchar : inputText.toCharArray()) //For every character in inputText
        {
            decoded += decodeChar(curchar, keyChars.charAt(keyCharPos));
            keyCharPos++;
        }

        return decoded;
    }

    /** 
     * The nextKeyStreamChar method generates a keystream character, based on the current state of the deck.
     * 
     * @return A keystream character
     */
    public char nextKeyStreamChar() 
    {
        deck.shiftDownOne(53);
        deck.shiftDownOne(54);
        deck.shiftDownOne(54);

        int joker1 = deck.findCard(53);
        int joker2 = deck.findCard(54);
        deck.tripleCut(joker1, joker2); 

        int bottomCard = deck.getCard(53);
        if (bottomCard == 54) 
        { 
            bottomCard--; 
        }
        deck.countCut(bottomCard);

        int topCard = deck.getCard(0);
        if (topCard == 54)
        {
            topCard--;
        }

        int outputCard = deck.getCard(topCard);

        if (outputCard >= 53) // If the card is a joker, do a recursive call
        { 
            return nextKeyStreamChar(); 
        }

        return intToChar(outputCard);
    }

    /** 
     * Encrypt a string, using the deck to generate *keystream* characters which can be passed to encodeChar.
     * 
     * @param inputString The string to encrypt
     * @return The result of encryption
     */
    public String encrypt(String inputString) {
        String output = "";

        for (char c :  inputString.toCharArray())
        {
            output += encodeChar(c , nextKeyStreamChar());
        }

        return output;
    }

    /** 
     * Decrypt a string, using the deck to generate *keystream* characters which can be passed to decodeChar.
     * 
     * @param inputString The string to decrypt
     * @return The result of decryption
     */
    public String decrypt(String inputString) {
        String output = "";

        for (char c : inputString.toCharArray())
        {
            output += decodeChar(c , nextKeyStreamChar());
        }

        return output;
    }
}
