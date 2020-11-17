import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Arran Stewart
 *
 */
public class DeckTest {

    Deck miniDeck;

    @Before
    public void setUp()  {
        miniDeck = new Deck("1,2,3,4,5,6,7,8,9,10,11,12");
    }

    public static void assertMatch(Deck d, int arr[]) {
        assertEquals("deck size matches expectation", arr.length, d.size());
        for (int i = 0; i < arr.length; i++) {
            String msg = String.format("card at idx %d matches", i);
            assertEquals(msg, arr[i], d.getCard(i));
        }
    }

    public static void assertMatch(Deck d, ArrayList<Integer> list) {
        assertEquals("deck size matches expectation", list.size(), d.size());
        for (int i = 0; i < list.size(); i++) {
            String msg = String.format("card at idx %d matches", i);
            assertEquals(msg, (int) list.get(i), d.getCard(i));
        }
    }

    /**
     * Test method for {@link Deck#Deck()}.
     */
    @Test
    public void testDeck() {
        Deck d = new Deck();
        for (int i = 0; i < Deck.DEFAULT_DECK_SIZE; i++) {
            String msg = String.format("should match at pos %d", i); 
            assertEquals(msg, i+1, d.getCard(i));
        }
    }

    /**
     * Test method for {@link Deck#Deck()}.
     */
    @Test
    public void testDeckEmptyInput() {
        Deck d = new Deck("");
        assertEquals("deck size should be zero", 0, d.size() );
    }

    /**
     * Test method for {@link Deck#Deck()}.
     */
    @Test
    public void testDeckSingletonInput() {
        Deck d = new Deck("1");
        assertEquals("deck size should be 1", 1, d.size() );
        assertEquals("card val should be 1", 1, d.getCard(0));
    }

    /**
    * Test method for {@link Deck#Deck()}.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testDeckBadInput() {
        @SuppressWarnings("unused")
        Deck d = new Deck("1,1");
    }

    @Test
    public void testDeckGoodDupleInput() {
        Deck d = new Deck("2,1");
        int expected[] = new int[] { 2, 1 };
        assertMatch(d, expected);
    }

    /**
     * Test method for {@link Deck#allDifferent(int[])}.
     */
    @Test
    public void testAllDifferentZeroLen() {
        int arr[] = new int[0];
        assertTrue("zero len arr is all different", Deck.allDifferent(arr));
    }

    /**
     * Test method for {@link Deck#allDifferent(int[])}.
     */
    @Test
    public void testAllDifferentSingleton() {
        int arr[] = new int[] {1};
        assertTrue("singleton arr is all different", Deck.allDifferent(arr));
    }

    /**
     * Test method for {@link Deck#allDifferent(int[])}.
     */
    @Test
    public void testAllDifferentFalseDuple() {
        int arr[] = new int[] {2,2};
        assertTrue("false for arr with duplicates", !Deck.allDifferent(arr));
    }

    /** log base 2
     */
    private static double log2(double a) {
        return Math.log(a) / Math.log(2);
    }

    /**
     * calculate Shannon entropy of distribution
     */
    private static double shannon(int arr[]) {
        double e = 0.0;

        int total = 0;
        for (int n : arr) {
            total += n;
        }

        for (int n : arr) {
            if (n == 0) 
            { continue; } // discard zero-prob outcomes

            double p = n * 1.0 / total; 
            e += p * log2(p);
        }
        return -e;
    }

    private double shuffledEntropy() {
        final int MAX_SHUFFLES = 100000;

        double total_entropy = 0; 

        int vals_seen[][] = 
            new int[Deck.DEFAULT_DECK_SIZE][Deck.DEFAULT_DECK_SIZE];

        for(int i = 0; i < MAX_SHUFFLES; i++) {
            Deck d = new Deck();
            d.shuffle();
            for (int j = 0; j < Deck.DEFAULT_DECK_SIZE; j++) {
                // record distribution
                int val_seen = d.getCard(j);
                vals_seen[j][val_seen - 1] += 1;
            }
        }

        for (int j = 0; j < Deck.DEFAULT_DECK_SIZE; j++) {
            double e = shannon( vals_seen[j] );
            total_entropy += e;
        }
        return total_entropy;
    }

    /**
     * Test method for {@link Deck#shuffle()}.
     */
    @Test
    public void testShuffle_low_grade() {
        double e = shuffledEntropy();
        assertTrue(e > 200);
    }

    /**
     * Test method for {@link Deck#shuffle()}.
     */
    @Test
    public void testShuffle_medium_grade() {
        double e = shuffledEntropy();
        assertTrue(e > 250);
    }

    /**
     * Test method for {@link Deck#shuffle()}.
     */
    @Test
    public void testShuffle_high_grade() {
        double e = shuffledEntropy();
        assertTrue(e > 300);
    }

    /** Join values in an array into a comma-separated string.
     */
    private static String joinArray(int[] arr) {
        List<String> list = new ArrayList<>();
        for (int i : arr) {
            list.add( "" + i );
        }
        return String.join(",", list);
    }

    /**
     * Test method for {@link Deck#shiftDownOne(int)}.
     */
    @Test
    public final void testShiftDownOne() {
        int vals[] = { 9,7,2,3 };
        String joined = joinArray(vals);

        for(int i = 0; i < vals.length - 1; i++) {
            Deck d = new Deck(joined);
            d.shiftDownOne( vals[i] );
            assertEquals( vals[i], d.getCard(i+1) );
        }

        // test last position.
        Deck d = new Deck(joined);
        d.shiftDownOne( vals[ vals.length - 1 ]);
        assertEquals( vals[vals.length - 1], d.getCard(1) );
    }

    /**
     * Test method for {@link Deck#tripleCut(int, int)}.
     */
    @Test
    public final void testTripleCut() {
        int inputVals[] = { 2, 4, 6, 888, 5, 8, 7, 1, 999, 3, 9 };
        int expected[]  = { 3, 9, 888, 5, 8, 7, 1, 999, 2, 4, 6 };

        Deck d = new Deck( joinArray(inputVals) );
        int pos1 = d.findCard(888);
        int pos2 = d.findCard(999);
        d.tripleCut(pos1, pos2);

        for (int i = 0; i < expected.length; i++) {
            int actualCard   = d.getCard(i);
            int expectedCard = expected[i];
            assertEquals(expectedCard, actualCard);
        }
    }

    /** When the whole deck constitutes the "middle",
     * a triple cut makes no change.
     * 
     * Test method for {@link Deck#tripleCut(int, int)}.
     */
    @Test
    public final void testTripleCut_noChange() {
        int inputVals[] = { 888, 2, 4, 6, 5, 8, 7, 1, 3, 9, 999 };

        Deck d = new Deck( joinArray(inputVals) );
        int pos1 = d.findCard(888);
        int pos2 = d.findCard(999);
        d.tripleCut(pos1, pos2);

        for (int i = 0; i < inputVals.length; i++) {
            int actualCard   = d.getCard(i);
            int expectedCard = inputVals[i];
            assertEquals(expectedCard, actualCard);
        }
    }

  
    /** Test a "count cut".
     * 
     * Test method for {@link Deck#countCut(int)}.
     */
    @Test
    public final void testCountCut() {
        int inputVals[] = 
            { 7, 22, 23, 24, 25, 26, 27, 28, 4, 5, 71, 72, 73, 8, 9  };
        int expectedVals[] = 
            { 5, 71, 72, 73, 8,  7, 22, 23, 24, 25, 26, 27, 28, 4, 9  };				

        Deck d = new Deck( joinArray(inputVals) );

        d.countCut(9);

        for (int i = 0; i < expectedVals.length; i++) {
            int actualCard   = d.getCard(i);
            int expectedCard = expectedVals[i];
            assertEquals(expectedCard, actualCard);
        }
    }

    /** Test a "count cut".
     * 
     * Test method for {@link Deck#countCut(int)}.
     */
    @Test
    public final void testCountCut2() {
        int inputVals[] = 
            { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15  };
        int expectedVals[] = 
            { 10, 11, 12, 13, 14,  1, 2, 3, 4, 5, 6, 7, 8, 9, 15  };				

        Deck d = new Deck( joinArray(inputVals) );

        d.countCut(9);

        for (int i = 0; i < expectedVals.length; i++) {
            int actualCard   = d.getCard(i);
            int expectedCard = expectedVals[i];
            assertEquals(expectedCard, actualCard);
        }
    }

    /** Test that example given in the documentation is correct.
     */
    @Test
    public final void testDocExamples_shiftDownOne_1() {
        miniDeck.shiftDownOne(10);
        int expected1[] = { 1,2,3,4,5,6,7,8,9,11,10,12 };
        assertMatch(miniDeck, expected1);
    }

    /** Test that example given in the documentation is correct.
     */
    @Test
    public final void testDocExamples_shiftDownOne_2() {
        miniDeck.shiftDownOne(12);
        int expected1[] = { 1,12,2,3,4,5,6,7,8,9,10,11 };
        assertMatch(miniDeck, expected1);
    }

    /** Test that example given in the documentation is correct.
     */
    @Test
    public final void testDocExamples_tripleCut_1() {
        miniDeck.tripleCut(1,9);
        int expected1[] = { 11,12,2,3,4,5,6,7,8,9,10,1 };

        assertMatch(miniDeck, expected1);
    }

    /** Test that example given in the documentation is correct.
     */
    @Test
    public final void testDocExamples_countCut() {
        miniDeck.countCut(4);
        int expected1[] = { 5,6,7,8,9,10,11,1,2,3,4,12 };
        assertMatch(miniDeck, expected1);
    }

}
