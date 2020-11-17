import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
/**
 * The test class TextAnalyserTest.
 *
 * @author  Lyndon While
 * @version April 2015
 */
public class TextAnalyserTest
{
    String s;
    TextAnalyser empty, lincoln, funny; 

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method. 
     */
    @Before
    public void setUp()
    {
        s = "There is a green hill far away, without the city wall.";
        empty   = new TextAnalyser();
        lincoln = new TextAnalyser();
        funny   = new TextAnalyser(s);
        
    }
      
    @Test
    public void testAnalysers() 
    {
        compare("empty",   empty,   0, new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, '?');
        compare("funny",   funny,  42, new int[] {5,0,1,0,5,1,1,4,4,0,0,4,0,1,1,0,0,3,1,5,1,0,3,0,2,0}, 'a');
    }
      
    @Test
    public void testanalyse() 
    {
        lincoln.analyse(s);
        compare("analyse", lincoln, 42, new int[] {5,0,1,0,5,1,1,4,4,0,0,4,0,1,1,0,0,3,1,5,1,0,3,0,2,0}, 'a');
    }
      
    @Test
    public void testanalyseFile() 
    {
        lincoln.analyseFile("GA.txt");
        compare("analyseFile", lincoln, 1151, new int[] {102,14,31,58,165,27,28,80,68,0,3,42,13,77,93,16,1,79,44,126,22,24,28,0,10,0}, 'e');
    }
      
    @Test
    public void testreset() 
    {
        funny.reset();
        compare("funny reset", funny, 0, new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, '?');
        lincoln.analyseFile("GA.txt");
        lincoln.reset();
        compare("lincoln reset", lincoln, 0, new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, '?');
    }
    
    @Test
    public void testhistogram()
    {
        lincoln.analyseFile("GA.txt");
        lincoln.histogram();
    }
    
    // t is the object being tested 
    // x, xs, and m are the expected answers 
    // s is the root of the error msg 
    private void compare(String s, TextAnalyser t, int x, int[] xs, char m) 
    {
        assertEquals("Wrong count in " + s, x, t.lettersAnalysed());
        for (int i = 0; i < 26; i++)
        {
            char c = (char) ('a' + i);
            assertEquals("Wrong '" + c + "'s in " + s, xs[i], t.frequency(c));
        }
        assertEquals("Wrong most frequent in " + s, m, t.mostFrequent());
    }
}
