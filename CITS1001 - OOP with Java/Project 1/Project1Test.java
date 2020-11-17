import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * The test class Project1Test provides unit test cases for CITS1001 Project 1 2018.
 * @author Lyndon While
 * @version 1.0
 */
public class Project1Test
{
    private Polynomial poly;
    private Sequence[] aeq; 
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
        aeq      = new Sequence[9];
        aeq[0]   = new Sequence("-4");
        aeq[1]   = new Sequence("0, 0");
        aeq[2]   = new Sequence("2, 2, 2");
        aeq[3]   = new Sequence("4, -5");
        aeq[4]   = new Sequence("4, 0, -4");
        aeq[5]   = new Sequence("4, 4, -3.5");
        aeq[6]   = new Sequence("4, -4, 4");
        aeq[7]   = new Sequence("-1.5, -1.6, -1.6");
        aeq[8]   = new Sequence("-909, -696, -181, 2136, 10875, 36296, 97539");
    }

    @Test
    public void testPolynomial() 
    {
        assertEquals("a new Polynomial should have no terms", 0, poly.numberOfTerms());
    }

    // turns a double[] into an ArrayList<Double> 
    private ArrayList<Double> mkd (double[] ds)
    {
        ArrayList<Double> zs = new ArrayList<>();
        for (double d : ds) zs.add(d);
        return zs;
    }

    // returns true iff x and ys are equal
    private boolean eqtest (Sequence x, double[] ys)
    {
        return x.getSequence().equals(mkd (ys));
    }

    @Test
    public void testSequences() 
    {
        assertTrue("aeq0 gives {-4}",                                          eqtest(aeq[0], new double[] {-4})); 
        assertTrue("aeq1 gives {0, 0}",                                        eqtest(aeq[1], new double[] {0, 0}));
        assertTrue("aeq2 gives {2, 2, 2}",                                     eqtest(aeq[2], new double[] {2, 2, 2}));
        assertTrue("aeq3 gives {4, -5}",                                       eqtest(aeq[3], new double[] {4, -5}));
        assertTrue("aeq4 gives {4, 0, -4}",                                    eqtest(aeq[4], new double[] {4, 0, -4}));
        assertTrue("aeq5 gives {4, 4, -3.5}",                                  eqtest(aeq[5], new double[] {4, 4, -3.5}));
        assertTrue("aeq6 gives {4, -4, 4}",                                    eqtest(aeq[6], new double[] {4, -4, 4}));
        assertTrue("aeq7 gives {-1.5, -1.6, -1.6}",                            eqtest(aeq[7], new double[] {-1.5, -1.6, -1.6}));
        assertTrue("aeq8 gives {-909, -696, -181, 2136, 10875, 36296, 97539}", eqtest(aeq[8], new double[] {-909, -696, -181, 2136, 10875, 36296, 97539}));
    }

    @Test
    public void testTermdisplay1() // tests +ve and -ve integer and fractional coefficients with powers of 0 and 1
    {
        double k;
        String s, u;
        String v = "";
        for (int e = 0; e <= 1; e++)
        {
            for (double d = -10.0; d <= 10.0; d++)
            {
                if (d == 0) k = 0.001; else k = d + d / 2;
                if (k >  0) u = "+";   else u = "-";
                Term t = new Term (k, e);
                assertEquals("t simple should appear as '" + u + " " + Math.abs(k) + "x^" + e + "'", u + Math.abs(k) + "x^" + e, t.displaySimple().replaceAll(" ", ""));
                s = (d != 0 && d % 2 == 0) ? "" + (int)Math.abs(k) : "" + Math.abs(k);
                assertEquals("t improved should appear as '" + u + " " + s + v + "'",        u + s + v,        t.displayImproved().replaceAll(" ", ""));
                assertEquals("t coefficient should be unchanged by display", k, t.getCoefficient(), epsilon); 
                assertEquals("t exponent should be unchanged by display",    e, t.getExponent()); 
                // System.out.println("Tested " + k + " " + e);
            }
            v = "x"; // assumes e is only ever 0 or 1
        }
    }

    @Test
    public void testTermdisplay2() 
    {
        Term t = new Term(-1, 2);
        assertEquals("t simple should appear as '- 1.0x^2'",    "-1.0x^2", t.displaySimple().replaceAll(" ", ""));
        assertEquals("t improved should appear as '- x^2'",        "-x^2", t.displayImproved().replaceAll(" ", ""));
        assertEquals("t coefficient should be unchanged by display", -1.0, t.getCoefficient(), epsilon); 
        assertEquals("t exponent should be unchanged by display",       2, t.getExponent()); 
    }

    @Test
    public void testTermdisplay3() 
    {
        Term t = new Term(4.5, 3);
        assertEquals("t simple should appear as '+ 4.5x^3'",   "+4.5x^3", t.displaySimple().replaceAll(" ", ""));
        assertEquals("t improved should appear as '+ 4.5x^3'", "+4.5x^3", t.displayImproved().replaceAll(" ", ""));
        assertEquals("t coefficient should be unchanged by display", 4.5, t.getCoefficient(), epsilon); 
        assertEquals("t exponent should be unchanged by display",      3, t.getExponent()); 
    }

    @Test
    public void testTermdisplay4() 
    {
        Term t = new Term(2.03, 2);
        assertEquals("t simple should appear as '+ 2.03x^2'",   "+2.03x^2", t.displaySimple().replaceAll(" ", ""));
        assertEquals("t improved should appear as '+ 2.03x^2'", "+2.03x^2", t.displayImproved().replaceAll(" ", ""));
        assertEquals("t coefficient should be unchanged by display",  2.03, t.getCoefficient(), epsilon); 
        assertEquals("t exponent should be unchanged by display",        2, t.getExponent()); 
    }

    @Test
    public void testaddTerm() 
    {
        poly.addTerm(new Term(3, 2));
        assertEquals("3x^2 was added",               1, poly.numberOfTerms());
        assertEquals("coefficient was set to 3.0", 3.0, poly.getTerm(0).getCoefficient(), epsilon);
        assertEquals("exponent was set to 2",        2, poly.getTerm(0).getExponent());
        poly.addTerm(new Term(-3, 1));
        assertEquals("-3x was added",                  2, poly.numberOfTerms());
        assertEquals("coefficient was set to -3.0", -3.0, poly.getTerm(1).getCoefficient(), epsilon);
        assertEquals("exponent was set to 1",          1, poly.getTerm(1).getExponent());
    }

    @Test
    public void testPolynomialdisplay1() 
    {
        poly.addTerm(new Term(   66, 4));
        poly.addTerm(new Term(   -1, 3));
        poly.addTerm(new Term(-12.6, 1));
        poly.addTerm(new Term(  2.8, 0));
        assertEquals("poly should appear as '66x^4 - x^3 - 12.6x + 2.8'", "66x^4-x^3-12.6x+2.8", poly.displayPolynomial().replaceAll(" ", ""));
        assertEquals("still four terms",                4, poly.numberOfTerms());
        assertEquals("first coefficient unchanged",  66.0, poly.getTerm(0).getCoefficient(), epsilon);
        assertEquals("first exponent unchanged",        4, poly.getTerm(0).getExponent());
        assertEquals("second coefficient unchanged",   -1, poly.getTerm(1).getCoefficient(), epsilon);
        assertEquals("second exponent unchanged",       3, poly.getTerm(1).getExponent());
        assertEquals("third coefficient unchanged", -12.6, poly.getTerm(2).getCoefficient(), epsilon);
        assertEquals("third exponent unchanged",        1, poly.getTerm(2).getExponent());
        assertEquals("fourth coefficient unchanged",  2.8, poly.getTerm(3).getCoefficient(), epsilon);
        assertEquals("fourth exponent unchanged",       0, poly.getTerm(3).getExponent());
    }

    @Test
    public void testPolynomialdisplay2() 
    {
        poly.addTerm(new Term(0.1, 0));
        assertEquals("poly should appear as 0.1", "0.1", poly.displayPolynomial().replaceAll(" ", ""));
        assertEquals("still one term",                1, poly.numberOfTerms());
        assertEquals("coefficient unchanged",       0.1, poly.getTerm(0).getCoefficient(), epsilon);
        assertEquals("exponent unchanged",            0, poly.getTerm(0).getExponent());
    }

    @Test
    public void testPolynomialdisplay3() 
    {
        poly.addTerm(new Term(-15, 0));
        assertEquals("poly should appear as - 15", "-15", poly.displayPolynomial().replaceAll(" ", ""));
        assertEquals("still one term",                 1, poly.numberOfTerms());
        assertEquals("coefficient unchanged",        -15, poly.getTerm(0).getCoefficient(), epsilon);
        assertEquals("exponent unchanged",             0, poly.getTerm(0).getExponent());
    }

    @Test
    public void testPolynomialdisplay4() 
    {
        poly.addTerm(new Term(0, 0));
        assertEquals("poly should appear as 0", "0", poly.displayPolynomial().replaceAll(" ", ""));
        assertEquals("still one term",            1, poly.numberOfTerms());
        assertEquals("coefficient unchanged",     0, poly.getTerm(0).getCoefficient(), epsilon);
        assertEquals("exponent unchanged",        0, poly.getTerm(0).getExponent());
    } 

    @Test
    public void testPolynomialdisplay5() 
    {
        poly.addTerm(new Term(-1000, 3));
        poly.addTerm(new Term(-12.6, 1));
        poly.addTerm(new Term( -2.8, 0));
        assertEquals("poly should appear as '- 1000x^3 - 12.6x - 2.8'", "-1000x^3-12.6x-2.8", poly.displayPolynomial().replaceAll(" ", ""));
        assertEquals("still three terms",                3, poly.numberOfTerms());
        assertEquals("first coefficient unchanged",  -1000, poly.getTerm(0).getCoefficient(), epsilon);
        assertEquals("first exponent unchanged",         3, poly.getTerm(0).getExponent());
        assertEquals("second coefficient unchanged", -12.6, poly.getTerm(1).getCoefficient(), epsilon);
        assertEquals("second exponent unchanged",        1, poly.getTerm(1).getExponent());
        assertEquals("third coefficient unchanged",   -2.8, poly.getTerm(2).getCoefficient(), epsilon);
        assertEquals("third exponent unchanged",         0, poly.getTerm(2).getExponent());
    }

    @Test
    public void testfactorial() 
    {
        int z = 1;
        for (int k = 0; k <= 8; k++)
        {
            z *= Math.max(1, k);
            assertEquals("fac(" + k + ") = " + z, z, aeq[0].factorial(k));
        }
        assertTrue("aeq0 unchanged by factorial", eqtest(aeq[0], new double[] {-4}));
    }

    @Test
    public void testallEqual() 
    {
        for (int i = 0; i < 9; i++) 
            assertEquals("aeq[" + i + "] failed", i < 3, aeq[i].allEqual(aeq[i].getSequence()));
        assertTrue("aeq2 should be unchanged by allEqual", eqtest(aeq[2], new double[] {2, 2, 2}));
        assertTrue("aeq7 should be unchanged by allEqual", eqtest(aeq[7], new double[] {-1.5, -1.6, -1.6}));
        ArrayList<Double> zz = aeq[2].getSequence();
        for (int i = 0; i <= zz.size(); i++)
        {
            zz.add(i, new Double(zz.get(0) + 664));
            assertTrue("zz[" + i + "] failed", !aeq[2].allEqual(zz));
            zz.remove(i);
        }
    }

    // returns true iff the differences in x are equal to ys
    private boolean difftest (Sequence x, double[] ys)
    {
        return x.differences(x.getSequence()).equals(mkd (ys));
    }

    @Test
    public void testdifferences() 
    {
        assertTrue("aeq1 differences failed",         difftest(aeq[1], new double[] {0}));
        assertTrue("aeq1 unchanged by differences",     eqtest(aeq[1], new double[] {0, 0}));
        assertTrue("aeq2 differences failed",         difftest(aeq[2], new double[] {0, 0}));
        assertTrue("aeq2 unchanged by differences",     eqtest(aeq[2], new double[] {2, 2, 2}));
        assertTrue("aeq3 differences failed",         difftest(aeq[3], new double[] {-9}));
        assertTrue("aeq3 unchanged by differences",     eqtest(aeq[3], new double[] {4, -5}));
        assertTrue("aeq4 differences failed",         difftest(aeq[4], new double[] {-4, -4}));
        assertTrue("aeq4 unchanged by differences",     eqtest(aeq[4], new double[] {4, 0, -4}));
        assertTrue("aeq5 differences failed",         difftest(aeq[5], new double[] {0, -7.5}));
        assertTrue("aeq5 unchanged by differences",     eqtest(aeq[5], new double[] {4, 4, -3.5}));
        assertTrue("aeq6 differences failed",         difftest(aeq[6], new double[] {-8, 8}));
        assertTrue("aeq6 unchanged by differences",     eqtest(aeq[6], new double[] {4, -4, 4}));
        assertTrue("aeq7 differences failed",         difftest(aeq[7], new double[] {1.5-1.6, 0}));
        assertTrue("aeq7 unchanged by differences",     eqtest(aeq[7], new double[] {-1.5, -1.6, -1.6}));
        assertTrue("aeq8 differences failed",         difftest(aeq[8], new double[] {213, 515, 2317, 8739, 25421, 61243}));
        assertTrue("aeq8 unchanged by differences",     eqtest(aeq[8], new double[] {-909, -696, -181, 2136, 10875, 36296, 97539}));
        // extra test where the argument is different to the field variable
        ArrayList<Double> zz = aeq[3].getSequence();
        aeq[3] = new Sequence("-4, 0");
        assertTrue("extra test differences failed",            aeq[3].differences(zz).equals(mkd (new double[] {-9})));
        assertTrue("aeq3 unchanged by differences extra test", eqtest(aeq[3], new double[] {-4, 0}));
    }

    @Test
    public void testtermUpdate()
    {
        for (double c = -3; c <= 3; c += 0.2)
            for (int e = 0; e <= 3; e++)
                for (int x = 1; e <= 5; e++)
                    assertEquals("termUpdate(Term<" + c + ", " + e + ">, " + x + ") failed", c * Math.pow(x, e), aeq[0].termUpdate(new Term (c, e), x), epsilon);
    }

    @Test
    public void testupdateSequence() 
    {  
        aeq[3].updateSequence(new Term(-9, 1));
        assertTrue("aeq3 after -9x failed",      eqtest(aeq[3], new double[] {13, 13}));
        aeq[4].updateSequence(new Term(-4, 1));
        assertTrue("aeq4 after -4x failed",      eqtest(aeq[4], new double[] {8, 8, 8}));
        aeq[5].updateSequence(new Term(-3.75, 2));
        assertTrue("aeq5 after -3.75x^2 failed", eqtest(aeq[5], new double[] {7.75, 19, 30.25}));
        aeq[6].updateSequence(new Term(8, 2));
        assertTrue("aeq6 after 8x^2 failed",     eqtest(aeq[6], new double[] {-4, -36, -68}));
        aeq[7].updateSequence(new Term(1, 2));
        assertTrue("aeq7 after x^2 failed",      eqtest(aeq[7], new double[] {-2.5, -5.6, -10.6}));
        aeq[8].updateSequence(new Term(1, 6));
        assertTrue("aeq6 after x^6 failed",      eqtest(aeq[8], new double[] {-910, -760, -910, -1960, -4750, -10360, -20110}));
    }

    @Test
    public void testnextTerm0() 
    {
        Term t = aeq[0].nextTerm();
        assertEquals("aeq0 nextTerm coeff is -4", -4, t.getCoefficient(), epsilon);
        assertEquals("aeq0 nextTerm exp is 0",     0, t.getExponent());
        assertTrue("aeq0 unchanged by nextTerm", eqtest(aeq[0], new double[] {-4}));
    }

    @Test
    public void testnextTerm1() 
    {
        Term t = aeq[1].nextTerm();
        assertEquals("aeq1 nextTerm coeff is 0", 0, t.getCoefficient(), epsilon);
        assertEquals("aeq1 nextTerm exp is 0",   0, t.getExponent());
        assertTrue("aeq1 unchanged by nextTerm", eqtest(aeq[1], new double[] {0, 0}));
    }

    @Test
    public void testnextTerm2() 
    {
        Term t = aeq[2].nextTerm();
        assertEquals("aeq2 nextTerm coeff is 2", 2, t.getCoefficient(), epsilon);
        assertEquals("aeq2 nextTerm exp is 0",   0, t.getExponent());
        assertTrue("aeq2 unchanged by nextTerm", eqtest(aeq[2], new double[] {2, 2, 2}));
    }

    @Test
    public void testnextTerm3() 
    {
        Term t = aeq[3].nextTerm();
        assertEquals("aeq3 nextTerm coeff is -9", -9, t.getCoefficient(), epsilon);
        assertEquals("aeq3 nextTerm exp is 1",     1, t.getExponent());
        assertTrue("aeq3 unchanged by nextTerm", eqtest(aeq[3], new double[] {4, -5}));
    }

    @Test
    public void testnextTerm4() 
    {
        Term t = aeq[4].nextTerm();
        assertEquals("aeq4 nextTerm coeff is -4", -4, t.getCoefficient(), epsilon);
        assertEquals("aeq4 nextTerm exp is 1",     1, t.getExponent());
        assertTrue("aeq4 unchanged by nextTerm", eqtest(aeq[4], new double[] {4, 0, -4}));
    }

    @Test
    public void testnextTerm5() 
    {
        Term t = aeq[5].nextTerm();
        assertEquals("aeq5 nextTerm coeff is -3.75", -3.75, t.getCoefficient(), epsilon);
        assertEquals("aeq5 nextTerm exp is 2",           2, t.getExponent());
        assertTrue("aeq5 unchanged by nextTerm", eqtest(aeq[5], new double[] {4, 4, -3.5}));
    }

    @Test
    public void testnextTerm6() 
    {
        Term t = aeq[6].nextTerm();
        assertEquals("aeq6 nextTerm coeff is 8", 8, t.getCoefficient(), epsilon);
        assertEquals("aeq6 nextTerm exp is 2",   2, t.getExponent());
        assertTrue("aeq6 unchanged by nextTerm", eqtest(aeq[6], new double[] {4, -4, 4}));
    }

    @Test
    public void testnextTerm7() 
    {
        Term t = aeq[7].nextTerm();
        assertEquals("aeq7 nextTerm coeff is -0.05", 0.05, t.getCoefficient(), epsilon);
        assertEquals("aeq7 nextTerm exp is 2",          2, t.getExponent());
        assertTrue("aeq7 unchanged by nextTerm", eqtest(aeq[7], new double[] {-1.5, -1.6, -1.6}));
    }

    @Test
    public void testnextTerm8() 
    {
        Term t = aeq[8].nextTerm();
        assertEquals("aeq8 nextTerm coeff is 1", 1.0, t.getCoefficient(), epsilon);
        assertEquals("aeq8 nextTerm exp is 6",     6, t.getExponent());
        assertTrue("aeq8 unchanged by nextTerm", eqtest(aeq[8], new double[] {-909, -696, -181, 2136, 10875, 36296, 97539}));
    }

    @Test
    public void testsolveSequence() 
    {
        assertEquals("aeq0 gives - 4",                         "-4",                    aeq[0].solveSequence().displayPolynomial().replaceAll(" ", ""));
        assertEquals("aeq1 gives 0",                           "0",                     aeq[1].solveSequence().displayPolynomial().replaceAll(" ", ""));
        assertEquals("aeq2 gives 2",                           "2",                     aeq[2].solveSequence().displayPolynomial().replaceAll(" ", ""));
        assertEquals("aeq3 gives - 9x + 13",                   "-9x+13",                aeq[3].solveSequence().displayPolynomial().replaceAll(" ", ""));
        assertEquals("aeq4 gives - 4x + 8",                    "-4x+8",                 aeq[4].solveSequence().displayPolynomial().replaceAll(" ", ""));
        assertEquals("aeq5 gives - 3.75x^2 + 11.25x - 3.75",   "-3.75x^2+11.25x-3.5",   aeq[5].solveSequence().displayPolynomial().replaceAll(" ", ""));
        assertEquals("aeq6 gives 8x^2 - 32x + 28",             "8x^2-32x+28",           aeq[6].solveSequence().displayPolynomial().replaceAll(" ", ""));
        assertEquals("aeq8 gives x^6 - 10x^4 + 100x^2 - 1000", "x^6-10x^4+100x^2-1000", aeq[8].solveSequence().displayPolynomial().replaceAll(" ", ""));
    }

}