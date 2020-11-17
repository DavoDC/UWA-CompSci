/**
 * The Term class models terms in a polynomial. 
 * 
 * @author Lyndon While, David
 * @version 15/4/18
 * 
 */
public class Term
{
    // The term = coefficient * x ^ exponent
    private double coefficient;
    private int    exponent;

    public Term(double c, int e)
    {
        coefficient = c;
        exponent = e;
    }

    /**
     * Returns the coefficient
     */
    public double getCoefficient()
    {
        return coefficient;
    }

    /**
     * Returns the exponent
     */
    public int getExponent()
    {
        return exponent;
    }

    /**
     * Returns the term as a simple String for display 
     * e.g. with coefficient = 5 and exponent= 1, return "+5.0x^1" 
     */
    public String displaySimple()  
    {
        String term = "";

        double coefficient = getCoefficient();

        int exponent = getExponent();

        if (Math.signum(coefficient) == 1) {
            term += "+";
        }

        term += coefficient + "x^" + exponent;

        return term; 
    }

    /**
     * Returns the term as a String in the required format
     * e.g. With coefficient = 5 and exponent = 1, return "5x" 
     */
    public String displayImproved()
    {
        String term = "";

        double coefficient = getCoefficient();
        
        // Round the coefficient to 1 decimal place
        coefficient = (Math.round(coefficient * 100.0) / 100.0);

        int exponent = getExponent();

        if (Math.signum(coefficient) == 1) {
            term += "+";
        }
             
        if (coefficient == -1) {
            term += "-";
        }
        else if (coefficient != 1) {
            term += coefficient;
        }

        if (exponent == 0 && term.length() == 1) {
            // If term is just one character at this point, it must be "-1"
            term += "1";
        }
        else if (exponent == 1) {
            term += "x";
        }
        else if (exponent != 0) {
            term += "x^";
            term += exponent;
        }

        if (coefficient == 0) {
            return "";
        }

        return term;
    }

}
