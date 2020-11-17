import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Arran Stewart
 *
 */
public class MessageTest {

    /**
     * Test method for {@link Message#tryPosition(int)}.
     */
    @Test
    public final void testTryPosition_0() {
        String expected = 
         "39,8,48,52,13,14,47,18,19,20,11,2,25,26,27,28,29,23,32,9,53,17,12,15,1,30,31,33,34,24,35,21,22,3,4,16,41,54,36,37,38,50,42,43,44,45,46,40,51,49,5,6,7,10";
        Message m = new Message();
        String actual = m.tryPosition(0);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Message#tryPosition(int)}.
     */
    @Test
    public final void testTryPosition_1() {
        String expected = 
        "8,39,48,52,13,14,47,18,19,20,11,2,25,26,27,28,29,23,32,9,53,17,12,15,1,30,31,33,34,24,35,21,22,3,4,16,41,54,36,37,38,50,42,43,44,45,46,40,51,49,5,6,7,10";
        Message m = new Message();
        String actual = m.tryPosition(1);
        assertEquals(expected, actual);
    }

}
