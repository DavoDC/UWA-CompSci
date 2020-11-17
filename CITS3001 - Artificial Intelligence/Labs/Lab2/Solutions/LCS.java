/**
 * This class implements various longest common subsequence algorithms.
 */

import java.util.ArrayList;

public class LCS
{
    private LCS(){}
    
    /**
     * Returns all subsequences of s.
     */
    private static ArrayList<String> allsubseqs(String s)
    {
        ArrayList<String> zss = new ArrayList<>();
        if (s.isEmpty())
           zss.add("");
        else
        {
            ArrayList<String> xss = allsubseqs(s.substring(1));
            for (String xs : xss)
                zss.add(s.substring(0,1) + xs);
            zss.addAll(xss);
        }
        return zss;
    }
    
    /**
     * Returns all LCSs of s1 and s2.
     */
    private static ArrayList<String> lcss(String s1, String s2)
    {
        ArrayList<String> zss = new ArrayList<>();
        ArrayList<String> xs1 = allsubseqs(s1);
        ArrayList<String> xs2 = allsubseqs(s2);
        int maxlen = 0;
        for (String xs : xs1)
            if (xs2.contains(xs))
            {
               if (xs.length() > maxlen)
               {
                  zss.clear();
                  maxlen = xs.length();
               }
               if (xs.length() == maxlen)
                  zss.add(xs);
            }
        return zss;
    }
    
    /**
     * Returns one LCS of s1 and s2 using the recursive algorithm.
     */
    private static String lcsr(String s1, String s2)
    {
        if (s1.isEmpty() || s2.isEmpty())
           return "";
        else
        if (s1.charAt(s1.length() - 1) == s2.charAt(s2.length() - 1))
           return lcsr(s1.substring(0, s1.length() - 1), 
                       s2.substring(0, s2.length() - 1)) + 
                  s1.substring(s1.length() - 1);
        else
           return longer(lcsr(s1, s2.substring(0, s2.length() - 1)),
                         lcsr(s1.substring(0, s1.length() - 1), s2));
    }
    
    /**
     * Returns one LCS of s1 and s2 using the DP algorithm.
     */
    private static String lcsd(String s1, String s2)
    {
        // the table used to store the calculations for the substrings
        Cell[][] table = new Cell[s1.length() + 1][s2.length() + 1];
        // create the top row
        for (int i = 0; i <= s1.length(); i++) 
            table[i][0] = new Cell(0, Direction.LEFT);
        // create the subsequent rows
        for (int j = 1; j <= s2.length(); j++) 
        {
            // create the leftmost cell
            table[0][j] = new Cell(0, Direction.ABOVE);
            // create the subsequent cells
            for (int i = 1; i <= s1.length(); i++)
                // if the last chars match, extend the LCS
                if (s1.charAt(i-1) == s2.charAt(j-1))
                   table[i][j] = new Cell(table[i-1][j-1].getLen()+1, Direction.ABOVE_LEFT);
                else
                // otherwise use the longer of the two existing LCSs
                if (table[i-1][j].getLen() > table[i][j-1].getLen())
                   table[i][j] = new Cell(table[i-1][j].getLen(), Direction.LEFT);
                else
                   table[i][j] = new Cell(table[i][j-1].getLen(), Direction.ABOVE);
        }
        // print the table
        for (int i = 0; i <= s1.length(); i++)
        {
            for (int j = 0; j <= s2.length(); j++) 
                System.out.print(table[i][j].print());
            System.out.println();
        }
        // reconstruct the LCS, starting from the bottom-right cell
        String lcs = "";
        int i = s1.length();
        int j = s2.length();
        while (i > 0 && j > 0)
            if (table[i][j].getDir() == Direction.ABOVE_LEFT)
            {
                lcs = s1.charAt(i - 1) + lcs;
                i -= 1;
                j -= 1;
            }
            else
            if (table[i][j].getDir() == Direction.LEFT)
               i -= 1;
            else
               j -= 1;
        return lcs;
    }
    
    /**
     * Returns the longer of s1 and s2.
     */
    private static String longer(String s1, String s2)
    {
        if (s1.length() > s2.length())
           return s1; 
        else
           return s2;
    }
    
    /**
     * Runs the LCS algorithms. 
     */
    public static void compareLCSs()
    {
        // s1[k] is processed with s2[k]
        String[] s1 = {"ABCBDAB", "110110",   "01101001", "101010", "01"};
        String[] s2 = {"BDCABA",  "01101001", "110110",   "101010", "100000"};
        assert s1.length == s2.length : "s1 and s2 are different lengths";
        for (int k = 0; k < s1.length; k++)
        {
            for (String test : new String[] {s1[k], s2[k]})
            {
                System.out.print("Subseqs: ");
                for (String xs : allsubseqs(test))
                    System.out.print(xs + ", ");
                System.out.println();
            }
            System.out.print("All LCSs: ");
            for (String xs : lcss(s1[k], s2[k]))
                System.out.print(xs + ", ");
            System.out.println();
            System.out.println("Rec: " + lcsr(s1[k], s2[k]));
            System.out.println("DP:  " + lcsd(s1[k], s2[k]));
            System.out.println();
        }
    }
}
