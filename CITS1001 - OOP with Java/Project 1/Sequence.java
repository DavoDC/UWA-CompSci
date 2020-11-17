
import java.util.ArrayList;

/**
 * The Sequence class models sequences.
 * 
 * @author Lyndon While, David
 * @version 15/4/18
 * 
 */
public class Sequence
{
    // The numbers in the sequence 
    private ArrayList<Double> sequence;

    /**
     * Sets up sequence by parsing s 
     * e.g. Sequence("3, -4, 8.5") sets sequence to <3, -4, 8.5> 
     */
    public Sequence(String s)
    {
        sequence = new ArrayList<>();

        for (String x : s.split(",") ){
            sequence.add(Double.parseDouble(x));
        }
    }

    /**
     * Returns sequence 
     */
    public ArrayList<Double> getSequence()
    {
        return sequence;
    }

    /**
     * Returns the product of 1 ... x 
     * e.g. factorial(4) = 1 * 2 * 3 * 4 returns 24 
     * 
     * @param The number we want the factorial of
     */
    public int factorial(int x)
    {
        int result = 1;
        int repeats = x;

        while (repeats > 0)
        {
            result = result * repeats;
            repeats--;
        }

        return result;
    }

    /**
     * Returns true if all the numbers in the sequence are the same
     * e.g. allEqual(<4, 4, 4>) returns true, and allEqual(<3, 3, -2>) returns false 
     * 
     * @param An ArrayList of doubles (the sequence)
     */
    public boolean allEqual(ArrayList<Double> seq)
    {
        int numberOfSame = 0;

        double comparison = seq.get(0);

        for (Double number : seq) {
            if (number == comparison) {
                numberOfSame++;
            }
        }

        if (numberOfSame == seq.size()) {
            return true;
        }

        return false;
    }

    /**
     * Returns a new ArrayList holding the differences between adjacent items in the sequence
     * e.g. differences(<4, 6, 1, 1>) returns <2, -5, 0>  
     * 
     * @param An ArrayList of doubles (the sequence)
     */
    public ArrayList<Double> differences(ArrayList<Double> seq)
    {
        ArrayList<Double> differences = new ArrayList<>();

        int index = 0;

        int repeats = seq.size();

        while (repeats > 0  && (index + 1 != seq.size() )) {

            double difference = seq.get(index + 1) - seq.get(index);

            differences.add(difference);

            --repeats;

            ++index;
        }

        return differences;
    }

    /**
     * Returns the next term in the simplest polynomial that generates sequence 
     * Implements Steps 1 to 3 of the algorithm
     */
    public Term nextTerm()
    {
        ArrayList<Double> sequenceCopy = sequence;

        int exponent = 0;

        while (allEqual(sequenceCopy) != true) {
            sequenceCopy = differences(sequenceCopy);

            ++exponent;
        }

        double coefficient = (sequenceCopy.get(0) / factorial(exponent) );

        return (new Term(coefficient, exponent));
    }

    /**
     * Returns the value to subtract from the kth index of term t
     * e.g. termUpdate(Term<2, 3>, 4) returns 128 
     * 
     * @param A term object
     * @param An integer (the index)
     */
    public double termUpdate(Term t, int k)
    {
        double result = Math.pow(k, t.getExponent());

        result = result * t.getCoefficient();

        return result;
    }

    /**
     * Subtracts from each item in sequence the effect of the term t.
     * In other words, subtract the new term from each element of the original sequence. 
     * 
     * @param A term object
     */
    public void updateSequence(Term t)
    {
        ArrayList<Double> sequencecopy = new ArrayList<>();

        int counter = 0;

        for (Double cur : sequence) {
            ++counter;

            sequencecopy.add (cur - termUpdate (t, counter) );
        }

        sequence = sequencecopy;

    }

    /**
     * Returns the simplest polynomial that generates sequence 
     * Implements the full algorithm on the project web page 
     */
    public Polynomial solveSequence()
    {                
        Polynomial polynomial = new Polynomial();

        int iterations = sequence.size();

        while (iterations > 0) {
            Term nextTerm = nextTerm();

            updateSequence(nextTerm);

            double coefficient = nextTerm.getCoefficient();

            int exponent = nextTerm.getExponent();

            polynomial.addTerm (new Term (coefficient, exponent));

            iterations--;
        }

        return polynomial;
    }

    /**
     * Reads a filename, solves the sequences therein, and displays the results 
     */ 
    public static void solveFileSequences(String filename)
    {
        for (String s : (new FileIO(filename)).lines) {

            if (s.startsWith("#") ) {
                System.out.println ("Correct polynomial = " + s.replaceAll("#", "").replaceAll(" ", "") );
            }

            if (s.length() > 0 && "0123456789-".indexOf(s.charAt(0)) != -1) {
                System.out.println("Polynomial generated: " + (new Sequence(s)).solveSequence().displayPolynomial() );
                System.out.println("Sequence = " + s);
                System.out.println();
            }

        }

    }

}

