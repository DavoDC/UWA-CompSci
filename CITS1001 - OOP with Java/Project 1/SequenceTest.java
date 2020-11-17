import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The test class SequenceTest provides 
 * unit test cases for the Sequence class.
 * @author Lyndon While
 * @version 1.0
 */
public class SequenceTest
{
    private Sequence seq15, seq444, seqm;
    private double epsilon = 1e-6;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        seq15  = new Sequence("1, 5");
        seq444 = new Sequence("4, 4, 4");
        seqm   = new Sequence("0, -1.5, -4");
    }

    @Test
    public void testSequences() 
    {
       ArrayList<Double> s15 = new ArrayList<>(Arrays.asList(new Double(1), new Double(5)));
       assertTrue("seq15 gives <1, 5>}", s15.equals(seq15.getSequence()));
       
       ArrayList<Double> s444 = new ArrayList<>(Arrays.asList(new Double(4), new Double(4), new Double(4)));
       assertTrue("s444 gives <4, 4, 4>}", s444.equals(seq444.getSequence()));
       
       ArrayList<Double> sm = new ArrayList<>(Arrays.asList(new Double(0), new Double(-1.5), new Double(-4)));
       assertTrue("seqm gives <0, -1.5, -4>", sm.equals(seqm.getSequence()));
    }

    @Test
    public void testfactorial() 
    {
       assertEquals("fac(3) = 6",         6, seq15.factorial(3));
       assertEquals("fac(8) = 40320", 40320, seq15.factorial(8));
       
       ArrayList<Double> s15 = new ArrayList<>(Arrays.asList(new Double(1), new Double(5)));
       assertTrue("seq15 is unchanged by factorial", s15.equals(seq15.getSequence()));
    }

    @Test
    public void testallEqual() 
    {
       assertTrue("seq15 is NOT all equal", !seq15.allEqual(seq15.getSequence()));
       assertTrue("seq444 is all equal",    seq444.allEqual(seq444.getSequence()));
       assertTrue("seqm is NOT all equal",  !seqm.allEqual(seqm.getSequence()));
       
       ArrayList<Double> s15 = new ArrayList<>(Arrays.asList(new Double(1), new Double(5)));
       assertTrue("seq15 is unchanged by allEqual", s15.equals(seq15.getSequence()));
       ArrayList<Double> s444 = new ArrayList<>(Arrays.asList(new Double(4), new Double(4), new Double(4)));
       assertTrue("seq444 is unchanged by allEqual", s444.equals(seq444.getSequence()));
       ArrayList<Double> sm = new ArrayList<>(Arrays.asList(new Double(0), new Double(-1.5), new Double(-4)));
       assertTrue("seqm is unchanged by allEqual", sm.equals(seqm.getSequence()));
    }

    @Test
    public void testdifferences() 
    {
       ArrayList<Double> s15 = new ArrayList<>(Arrays.asList(new Double(1), new Double(5)));
       ArrayList<Double> f   = new ArrayList<>(Arrays.asList(new Double(4)));
       assertTrue("seq15 differences gives {4}",       f.equals(seq15.differences(seq15.getSequence())));
       assertTrue("seq15 is unchanged by differences", s15.equals(seq15.getSequence()));
       
       ArrayList<Double> s444 = new ArrayList<>(Arrays.asList(new Double(4), new Double(4), new Double(4)));
       ArrayList<Double> zz   = new ArrayList<>(Arrays.asList(new Double(0), new Double(0)));
       assertTrue("seq444 differences gives {0, 0}",    zz.equals(seq444.differences(seq444.getSequence())));
       assertTrue("seq444 is unchanged by differences", s444.equals(seq444.getSequence()));
       
       ArrayList<Double> sm = new ArrayList<>(Arrays.asList(new Double(0), new Double(-1.5), new Double(-4)));
       ArrayList<Double> ms = new ArrayList<>(Arrays.asList(new Double(-1.5), new Double(-2.5)));
       assertTrue("seqm differences gives {-1.5, -2.5}", ms.equals(seqm.differences(seqm.getSequence())));
       assertTrue("seqm is unchanged by differences",    sm.equals(seqm.getSequence()));
    }
    
    @Test
    public void testtermUpdate()
    {
       ArrayList<Double> s15 = new ArrayList<>(Arrays.asList(new Double(1), new Double(5)));
       assertEquals(-3, seq15.termUpdate(new Term(-3, 2), 1), epsilon);
       assertEquals(24, seq15.termUpdate(new Term(3, 3),  2), epsilon);
       assertTrue("seq15 is unchanged by termUpdate", s15.equals(seq15.getSequence()));
    }

    @Test
    public void testupdateSequence() 
    {
       ArrayList<Double> m3 = new ArrayList<>(Arrays.asList(new Double(-3), new Double(-3)));
       seq15.updateSequence(new Term(4, 1));
       assertTrue("seq15 after 4x is {-3, -3}",  m3.equals(seq15.getSequence()));
       
       ArrayList<Double> mh = new ArrayList<>(Arrays.asList(new Double(0.5), new Double(0.5), new Double(0.5)));
       seqm.updateSequence(new Term(-0.5, 2));
       assertTrue("seqm after -0.5x^2 is {0.5, 0.5, 0.5}", mh.equals(seqm.getSequence()));
    }

    @Test
    public void testnextTerm() 
    {
       ArrayList<Double> s15 = new ArrayList<>(Arrays.asList(new Double(1), new Double(5)));
       Term soln15 = seq15.nextTerm();
       assertEquals("seq15 nextTerm coeff is 4", 4, soln15.getCoefficient(), epsilon);
       assertEquals("seq15 nextTerm exp is 1",   1, soln15.getExponent());
       assertTrue("seq15 unchanged by nextTerm", s15.equals(seq15.getSequence()));
       
       ArrayList<Double> sm = new ArrayList<>(Arrays.asList(new Double(0), new Double(-1.5), new Double(-4)));
       Term solnm = seqm.nextTerm();
       assertEquals("seqm nextTerm coeff is -0.5", -0.5, solnm.getCoefficient(), epsilon);
       assertEquals("seqm nextTerm exp is 2",      2,   solnm.getExponent());
       assertTrue("seqm unchanged by nextTerm",    sm.equals(seqm.getSequence()));
    }

    @Test
    public void testsolveSequence() 
    {
       assertEquals("seq15 gives 4x - 3",       "4x-3",        seq15.solveSequence().displayPolynomial().replaceAll(" ", ""));
       assertEquals("seq44 gives 4",            "4",          seq444.solveSequence().displayPolynomial().replaceAll(" ", ""));
       assertEquals("seqm gives - 0.5x^2 + 0.5", "-0.5x^2+0.5", seqm.solveSequence().displayPolynomial().replaceAll(" ", ""));
        
    }

}
