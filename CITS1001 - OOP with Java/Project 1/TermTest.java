import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TermTest provides 
 * unit test cases for the Term class.
 * @author Lyndon While
 * @version 1.0
 */
public class TermTest
{
    private Term term23, termm11;
    private double epsilon = 10e-6;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        term23  = new Term(2.5, 3);
        termm11 = new Term(-1, 1);
    }

    @Test
    public void testTerms() 
    {
        assertEquals("new Term(2.5, 3) should set coefficient to 2.5", 2.5, term23.getCoefficient(), epsilon);
        assertEquals("new Term(2.5, 3) should set exponent to 3",        3, term23.getExponent());
        assertEquals("new Term(-1,  1) should set coefficient to -1.0", -1, termm11.getCoefficient(), epsilon);
        assertEquals("new Term(-1,  1) should set exponent to 1",        1, termm11.getExponent());
    }

    @Test
    public void testdisplaySimple() 
    {
        String s;
        s = term23.displaySimple().replaceAll(" ", "");
        assertEquals("term23 should appear as '+ 2.5x^3'", "+2.5x^3", s);
        assertEquals("term23 coefficient is unchanged by displaySimple", 2.5, term23.getCoefficient(), epsilon); 
        assertEquals("term23 exponent is unchanged by displaySimple",      3, term23.getExponent()); 
        s = termm11.displaySimple().replaceAll(" ", "");
        assertEquals("termm11 should appear as '- 1.0x^1'", "-1.0x^1", s);
        assertEquals("termm11 coefficient is unchanged by displaySimple", -1, termm11.getCoefficient(), epsilon); 
        assertEquals("termm11 exponent is unchanged by displaySimple",     1, termm11.getExponent()); 
    } 

    @Test
    public void testdisplayImproved() 
    {
        String s;
        s = term23.displayImproved().replaceAll(" ", "");
        assertEquals("term23 should appear as '+ 2.5x^3'", "+2.5x^3", s);
        assertEquals("term23 coefficient is unchanged by displayImproved", 2.5, term23.getCoefficient(), epsilon); 
        assertEquals("term23 exponent is unchanged by displayImproved",      3, term23.getExponent()); 
        s = termm11.displayImproved().replaceAll(" ", "");
        assertEquals("termm11 should appear as '- x'", "-x", s);
        assertEquals("termm11 coefficient is unchanged by displayImproved", -1, termm11.getCoefficient(), epsilon); 
        assertEquals("termm11 exponent is unchanged by displayImproved",     1, termm11.getExponent()); 
    } 

}
