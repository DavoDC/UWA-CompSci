package Main;

import java.util.ArrayList;

/**
 * Convenient methods for testing data structures
 *
 * @author David
 */
public class TestTools
{

    // Used objects
    private static ArrayList<String> used = new ArrayList<>();

    /**
     * Get a random string of 4 letters. The same object will not be generated
     * in one runtime session
     *
     * @return the random sequence
     */
    public static String getRandomObject()
    {
        // Create string
        String ob = "";

        // Add random capital letters
        int letters = 2;
        while (letters > 0)
        {
            // Get a random number between 65 and 90
            int ranNum = 65;
            ranNum += (int) (Math.random() * 25);

            // Convert the number to a char and add to the string
            ob += "" + (char) ranNum + "";

            letters--;
        }

        // If has already been generated, generate a new string
        if (used.contains(ob))
        {
            return getRandomObject();
        }
        else // If unique, add to unique list and return
        {
            used.add(ob);
            return ob;
        }
    }

    /**
     * Assert that a method returns true. Throw an exception if false is
     * inputted
     *
     * @param value boolean input
     */
    public static void assertTrue(boolean value)
    {
        if (value != true)
        {
            throw new IllegalArgumentException("Assertion failed");
        }
    }

    /**
     * Assert that the values are equal. Throw an exception if they are not
     * equal.
     *
     * @param exp expected value
     * @param actual actual value
     */
    public static void assertEquals(String exp, String actual)
    {
        if (!exp.equals(actual))
        {
            throw new IllegalArgumentException("Assertion failed");
        }
    }

    /**
     * Assert that a method throws an exception. Input the method with a lambda
     * expression like so: ' () -> { ds.act(); } '. Will throw an exception if
     * method does not throw exception
     *
     * @param runnable the method as a runnable
     */
    public static void assertThrows(Runnable runnable) throws AssertionError
    {
        boolean wasThrown = false;

        try
        {
            runnable.run();
        }
        catch (Exception e)
        {
            wasThrown = true;
        }
        finally
        {
            if (wasThrown == false)
            {
                throw new AssertionError();
            }
        }

    }
}
