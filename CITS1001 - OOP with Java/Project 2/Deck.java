import java.util.ArrayList;
import java.util.Random;

/** 
 * 
 * Represents a deck of cards, and operations that can be performed on it.
 * 
 * @author Arran, David, Kyle
 *
 */
public class Deck 
{
    /** 
     * The size of a deck of cards. Four suits of thirteen cards, plus two jokers
     */
    public final static int DEFAULT_DECK_SIZE = 13 * 4 + 2;

    /** 
     * The value given to the first joker
     **/
    public final static int JOKER1 = 13 * 4 + 1;

    /** 
     * The value given to the second joker
     **/
    public final static int JOKER2 = 13 * 4 + 2;

    /** 
     * Array holding ints representing the cards. 
     * Card values should start from 1
     * Each int should be unique within the array.
     **/

    private int cards[]; 

    /** 
     * Create a deck of cards in the default order.
     */
    public Deck() 
    {
        cards = new int[DEFAULT_DECK_SIZE];
        for (int i = 0; i < DEFAULT_DECK_SIZE; i++) {
            cards[i] = i + 1;
        }
    }

    /** 
     * Construct a deck of cards from a String of comma-separated values.
     * 
     * @param s A string, consisting of comma-separated integers.
     */
    public Deck(String inputString) 
    {
        if (inputString.equals("")) {
            cards = new int[0];
            return;
        }

        String[] strings = inputString.split(",");

        cards = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            cards[i] = Integer.parseInt( strings[i] );
        }
        
        validateCards();
    }

    public int size() 
    {
        return cards.length;
    }

    public int getCard(int idx) 
    {
        return cards[idx];
    }

    /** 
     * Returns true when all values of the array arr are
     * different to each other; returns false otherwise.
     * 
     * @param arr An array of int values to be checked
     * @return Whether all values in the array are different
     */
    public static boolean allDifferent(int arr[]) 
    {  
        for (int j = 1; j < arr.length ; j++) {
            if (arr[0] == arr[j]) {
                return false;
            }
        }
        return true;
    }

    /** 
     * Find the position in the deck of the card with value cardVal.
     * 
     * @param cardVal The card to find
     * @return The position of the card to find, or -1 if it wasn't in the deck. 
     */
    public int findCard(int cardVal) 
    {
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] == cardVal) {
                return i;
            }
        }

        return -1;
    }

    /** 
     * Checks whether all the values in the "cards" instance variable are different.
     * 
     * @throws If they are not all unique values, an IllegalArgumentException is thrown
     */
    private void validateCards() 
    { 
        if (!allDifferent(cards)) { 
            throw new IllegalArgumentException();
        }
    }

    /** 
     * Shift a particular card down the deck by one place, and if this would take you past the end of the deck,
     * treat the end of the deck as joining onto the beginning.
     * 
     * Suppose you were using a deck of only 12 cards: 1,2,3,4,5,6,7,8,9,10,11,12. 
     * Then a call to shiftDownOne(10) would put the cards into the following order: 1,2,3,4,5,6,7,8,9,11,10,12. 
     * Whereas calling shiftDownOne(12) on that deck would result in this order: 1,12,2,3,4,5,6,7,8,9,10,11.
     * 
     * @param cardVal The value of the card to be shifted.
     * @throws Throws IllegalArgumentException if the argument passed is not found in the deck
     */
    public void shiftDownOne(int cardVal) 
    {
        if (findCard(cardVal) == -1) {
            throw new IllegalArgumentException();
        }

        if (findCard(cardVal) == cards.length-1) {   // Special Case = The last card
            int[] shiftedDeck = new int [cards.length];

            shiftedDeck[0] = cards[0];
            shiftedDeck[1] = cards[cards.length-1];  

            for (int i  = 2;  i < cards.length; i++)
            {
                shiftedDeck[i] = cards[i-1];
            }

            cards = shiftedDeck;
        }
        else {  // Normal swap
            int cardPos = findCard(cardVal);
            
            int tempCard = cards[cardPos];
            cards[cardPos] = cards[cardPos + 1];
            cards[cardPos + 1] = tempCard;
        }
    }

    /** 
     *  Perform a "triple cut": Given the positions of 2 cards in the deck, divide the deck into three "chunks": 
     *  chunk "A", the "top" - cards before either position
     *  chunk "B", the "middle" - cards lying between the 2 positions
     *  chunk "C", the "bottom" - cards after either position.
     *  
     *  Reorder the deck by swapping the top and bottom chunks
     *  (chunks "A" and "C").
     *  
     *  The method takes as its argument two positions which divide the deck into three“chunks”,
     *  and the first and last “chunks” are swapped. 
     *  By way of example: again, suppose you are using a deck of only 12 cards: 1,2,3,4,5,6,7,8,9,10,11,12. 
     *  A call to tripleCut(1,9) on the 12-card deck would result in the new order: 11,12,2,3,4,5,6,7,8,9,10,1.
     *  Even if one or more “chunks” consist of zero cards (i.e., are empty), the operation should still work.
     *  
     * @param pos1 Position of a "fixed" card, counting from 0.
     * @param pos2 Position of another "fixed" card, counting from 0.
     */
    public void tripleCut(int pos1, int pos2) 
    {
        //Swap to ensure x,y and y,x give the same deck
        if (pos1 > pos2) {
            int savedPos = pos1;
            pos1 = pos2;
            pos2 = savedPos;
        }

        ArrayList<Integer> chunkA = new ArrayList<>();
        ArrayList<Integer> chunkB = new ArrayList<>();
        ArrayList<Integer> chunkC = new ArrayList<>();

        for (int i : cards) {
            int cardPos = findCard(i);
            if (cardPos < pos1) {
                chunkA.add(i);
            }
            else if (pos2 < cardPos) {
                chunkC.add(i); 
            }
            else {
                chunkB.add(i);
            }
        }

        // Add chunk B then A to produce the order CBA
        chunkC.addAll(chunkB);
        chunkC.addAll(chunkA);

        for (int j = 0; j < cards.length; j++) {
            cards[j] =  chunkC.get(j);
        }
    }

    /** 
     * Perform a "count cut". Let n be a number of cards.
     * Remove n cards from the top of the deck, and place them
     * just above the last card.
     * 
     * If countCut(4) were applied to the 12-card deck from the previous examples,
     * this would result in the new card order: 
     * 5,6,7,8,9,10,11,1,2,3,4,12.
     * 
     * @param numCards
     * @throws Throws IllegalArgumentException if numCards if negative or greater than the deck size
     */
    public void countCut(int numCards) 
    {
        if (numCards < 0 || numCards > cards.length) {
            throw new IllegalArgumentException();
        }

        int[] newDeck = new int[cards.length];

        int newDeckPos = 0;

        for (int i = numCards ; i < cards.length-1 ; i++) {
            newDeck[newDeckPos] = cards[i];
            newDeckPos++;
        }

        for (int i = 0 ; i < numCards ; i++) {
            newDeck[newDeckPos] = cards[i];
            newDeckPos++;
        }

        newDeck[cards.length - 1] = cards[cards.length - 1];

        cards = newDeck;
    }

    /** 
     * Shuffles elements of an array into a random permutation.
     * 
     * When called, this method should shuffle the deck into a new order. 
     * There are three JUnit tests for this method (low, medium and high grade)
     * They measure the amount of entropy (a measure of disorder) your method introduces
     * You should aim to pass all three tests. 
     * The highest entropy is achieved when each possible ordering of the deck 
     * has the same probability of occurring when the method is called.
     *
     * 
     * @param arr Array to be shuffled.
     */
    public static void shuffleArray( int arr[] ) 
    {
        Random RNG = new Random();

        for (int i = 0; i != arr.length*3; i++)  {  //Number of swaps is proportional to array size
            int randPos1 = RNG.nextInt(53);
            int randPos2 = RNG.nextInt(53);

            int saved = arr[randPos1];

            arr[randPos1] = arr[randPos2];   
            arr[randPos2] = saved;  
        }
    }

    public void shuffle() 
    {
        shuffleArray(cards);
    }
}
