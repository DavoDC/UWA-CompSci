import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class PolynomialTest provides 
 * unit test cases for the Polynomial class.
 * @author Lyndon While
 * @version 1.20
 */
public class PolynomialTest
{
    private Polynomial poly, constant, negative;
    private double epsilon = 10e-6;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        poly     = new Polynomial();
        constant = new Polynomial();
        negative = new Polynomial();
    }

    @Test
    public void testPolynomial() 
    {
        assertEquals("a new Polynomial should have no terms", 0, poly.numberOfTerms());
    }

    @Test
    public void testaddTerm() 
    {
        poly.addTerm(new Term(3, 2));
        assertEquals("3x^2 was added",             1,   poly.numberOfTerms());
        assertEquals("coefficient was set to 3.0", 3.0, poly.getTerm(0).getCoefficient(), epsilon);
        assertEquals("exponent was set to 2",      2,   poly.getTerm(0).getExponent());
        poly.addTerm(new Term(-3, 1));
        assertEquals("-3x was added",                2,   poly.numberOfTerms());
        assertEquals("coefficient was set to -3.0", -3.0, poly.getTerm(1).getCoefficient(), epsilon);
        assertEquals("exponent was set to 1",        1,   poly.getTerm(1).getExponent());
    }

    @Test
    public void testdisplay() 
    {
        String s;
        poly.addTerm(new Term( 3, 3));
        poly.addTerm(new Term( 1, 1));
        poly.addTerm(new Term(-2, 0));
        s = poly.displayPolynomial().replaceAll(" ", "");
        assertEquals("poly should appear as '3x^3 + x - 2'", "3x^3+x-2", s);
        assertEquals("still three terms",            3,   poly.numberOfTerms());
        assertEquals("first coefficient unchanged",  3.0, poly.getTerm(0).getCoefficient(), epsilon);
        assertEquals("first exponent unchanged",     3,   poly.getTerm(0).getExponent());
        assertEquals("second coefficient unchanged", 1.0, poly.getTerm(1).getCoefficient(), epsilon);
        assertEquals("second exponent unchanged",    1,   poly.getTerm(1).getExponent());
        assertEquals("third coefficient unchanged", -2.0, poly.getTerm(2).getCoefficient(), epsilon);
        assertEquals("third exponent unchanged",     0,   poly.getTerm(2).getExponent());
        constant.addTerm(new Term(8, 0));
        s = constant.displayPolynomial().replaceAll(" ", "");
        assertEquals("constant should appear as 8", "8", s);
        assertEquals("still one term",         1,   constant.numberOfTerms());
        assertEquals("coefficient unchanged",  8.0, constant.getTerm(0).getCoefficient(), epsilon);
        assertEquals("exponent unchanged",     0,   constant.getTerm(0).getExponent());
        negative.addTerm(new Term(-8.1, 0));
        s = negative.displayPolynomial().replaceAll(" ", "");
        assertEquals("negative should appear as - 8.1", "-8.1", s);
        assertEquals("still one term",         1,    negative.numberOfTerms());
        assertEquals("coefficient unchanged",  -8.1, negative.getTerm(0).getCoefficient(), epsilon);
        assertEquals("exponent unchanged",     0,    negative.getTerm(0).getExponent());
    } 

}
