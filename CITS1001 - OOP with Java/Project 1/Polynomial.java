import java.util.ArrayList;

/**
 * The Polynomial class models polynomials.
 * 
 * @author Lyndon While, David 
 * @version 15/4/18
 */
public class Polynomial
{
    // The terms making up the polynomial
    private ArrayList<Term> polynomial;

    /**
     * Creates a zero polynomial
     */
    public Polynomial()
    {
        polynomial = new ArrayList<>();
    }

    /**
     *  Returns the number of terms in polynomial
     */
    public int numberOfTerms()
    {
        return polynomial.size();
    }

    /**
     * Adds a new term to the end of polynomial
     * 
     * @param A term object
     */
    public void addTerm(Term t)
    {
        polynomial.add(t);
    }

    /**
     *  Returns the indicated term of polynomial
     *  
     *  @param The position of the term as an integer
     */
    public Term getTerm(int k)
    {
        if (0 <= k && k < numberOfTerms()) {
            return polynomial.get(k);
        }
        else {
            return null;
        }
    }

    /**
     * Returns polynomial as a String for display, in the layout required 
     * Example  = 3x^3 + x - 2
     */
    public String displayPolynomial()
    {
        String polyString = "";
        int repeats = numberOfTerms();
        int counter = 0;

        while(repeats > 0) {
            Term term = getTerm(counter);

            polyString += term.displayImproved();

            ++counter;
            --repeats;

        }

        
        if (polyString.length() != 0  && polyString.startsWith("+"))  {
            polyString = polyString.substring(1,polyString.length());
        }

        //Pseudo-convert doubles to integers
        polyString = polyString.replace(".0" , "");

        // If nothing has been added by now, the sequence is 0, 0 , 0...
        if (polyString.length() == 0)  {
            polyString = "0";
        }

        return polyString;
    }

}
