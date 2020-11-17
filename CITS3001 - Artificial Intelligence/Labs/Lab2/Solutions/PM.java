/**
 * This class implements various pattern-matching algorithms.
 */

import java.util.ArrayList;

public class PM
{
    private PM(){}
    
    /**
     * Returns all instances of p in t, using the naive algorithm.
     */
    private static ArrayList<Integer> naive(String t, String p)
    {
        int n = t.length();
        int m = p.length();
        ArrayList<Integer> zs = new ArrayList<>();
        // test each shift
        for (int s = 0; s < n - m + 1; s++)
        {
            boolean match = true;
            int j = 0;
            // test each character in the pattern
            while (match && j < m)
                if (t.charAt(s+j) != p.charAt(j)) match = false;
                else                              j += 1;
            if (match) zs.add(s);
        }
        return zs;
    }
    
    /**
     * Returns all instances of p in t, using Rabin-Karp.
     * Assumes both strings are all digits and p fits into int.
     */
    private static ArrayList<Integer> rabinkarp(String t, String p)
    {
        int n = t.length();
        int m = p.length();
        // create the number for p
        int y = 0;
        for (int j = 0; j < m; j++)
            y = y * 10 + (p.charAt(j) - '0');
        // create the (incomplete) first number for t
        int z = 0;
        for (int j = 0; j < Math.min(t.length(), m-1); j++)
            z = z * 10 + (t.charAt(j) - '0');
        ArrayList<Integer> zs = new ArrayList<>();
        // test each shift
        for (int s = 0; s < n - m + 1; s++)
        {
            // update the number for t
            z = z % ((int) Math.pow(10, m-1)) * 10 + (t.charAt(s+m-1) - '0');
            if (z == y) zs.add(s);
        }
        return zs;
    }
    
    /**
     * Returns the prefix function for Knuth-Morris-Pratt.
     */
    private static ArrayList<Integer> kmpprefix(String p)
    {
        int m = p.length();
        ArrayList<Integer> zs = new ArrayList<>();
        zs.add(0); 
        int k = 0;
        for (int q = 1; q < m; q++)
        {
            while (k > 0 && p.charAt(k) != p.charAt(q)) k = zs.get(k-1);
            if (p.charAt(k) == p.charAt(q))             k += 1;
            zs.add(k);
        }
        return zs;
    }
    
    /**
     * Returns all instances of p in t, using Knuth-Morris-Pratt.
     */
    private static ArrayList<Integer> knuthmorrispratt(String t, String p)
    {
        int n = t.length();
        int m = p.length();
        ArrayList<Integer> pi = kmpprefix(p);
        int q = 0;
        ArrayList<Integer> zs = new ArrayList<>();
        for (int s = 0; s < n; s++)
        {
            while (q > 0 && p.charAt(q) != t.charAt(s)) q = pi.get(q - 1);
            if (p.charAt(q) == t.charAt(s))             q += 1;
            if (q == m)
            {
                zs.add(s - m + 1);
                q = pi.get(q - 1);
            }
        }
        return zs;
    }
    
    /**
     * Returns all instances of p in t, using Boyer-Moore.
     * Assumes both strings are all digits.
     */
    private static ArrayList<Integer> boyermoore(String t, String p)
    {
        int n = t.length();
        int m = p.length();
        ArrayList<Integer> zs = new ArrayList<>();
        // set up the bad-character data
        int[] lambd = new int[10]; // digits
        for (int k = 0; k < p.length(); k++)
            lambd[p.charAt(k) - '0'] = k + 1;
        // set up the good-suffix data 
        // uses only the basic rule - 
        // using the more-advanced rule sacrifices monotonicity on d 
        int[] gamma = new int[p.length()];
        gamma[gamma.length - 1] = 1;
        int k = gamma.length - 1;
        int d = p.substring(0,p.length()-1).lastIndexOf(p.substring(k));
        while (k > 0 && d >= 0)
        {
            gamma[k-1] = k - d;
            k -= 1;
            d = p.substring(0,p.length()-1).lastIndexOf(p.substring(k));
        }
        if (k >= 0)
        {
            String suffix = p.substring(k);
            while (!p.startsWith(suffix))
                  suffix = suffix.substring(1);
            for (int j = 0; j < k; j++)
                gamma[j] = p.length() - suffix.length();
        }
        // set up the first shift
        int s = 0;
        while (s <= n - m)
        {
            System.out.print(s + " ");
            boolean match = true;
            // test the characters in p backwards
            int j = m - 1;
            while (match && j >= 0)
                if (t.charAt(s+j) != p.charAt(j))
                {
                    match = false;
                    s += Math.max(Math.max(1, gamma[j]), j + 1 - lambd[t.charAt(s+j) - '0']); 
                }
                else
                    j -= 1;
            if (match)
            {
                zs.add(s);
                s += Math.max(1, gamma[0]);
            }
        }
        System.out.println();
        return zs;
    }
    
    /**
     * Runs the pattern-matching algorithms. Using only digits makes 
     * things easier for some algorithms, especially Boyer-Moore. 
     */
    public static void comparePMs()
    {
        // {t, p}
        String[][] tests = {//   "one-shone-the-one-phone"
                            {"", "12346512348534123475123"}
                           ,{"1", "2112121"}
                           ,{"191919", "1919"}
                           ,{"45444546664455466445454644545446", "445"}
                           ,{"23", "23"}
                           ,{"23", "223"}
                           ,{"823822281000018", "8128"}
                           ,{"001000", "000"}}; 
        int z = tests.length;
        // fill in the rest with random data: keep it binary to maximise incidental instances
        tests = java.util.Arrays.copyOf(tests, 100);
        for (int k = z; k < tests.length; k++)
        {
            tests[k] = new String[2];
            tests[k][1] = "";
            for (int i = 0; i < 6; i++)
                tests[k][1] += (int) (Math.random() * 2);
            tests[k][0] = "";
            for (int i = 0; i < 15; i++)
            {
                for (int j = 0; j < Math.random() * 20; j++)
                    tests[k][0] += (int) (Math.random() * 2);
                tests[k][0] += tests[k][1];
            }
        }
        for (String[] ss : tests)
        {
            for (String s : ss)
                for (int k = 0; k < s.length(); k++)
                    assert Character.isDigit(s.charAt(k)) : "only digits are allowed";
            System.out.println("T = " + ss[0]);
            System.out.println("P = " + ss[1]);
            
            ArrayList<Integer> nv = naive(ss[0], ss[1]);
            System.out.print("Naive:              ");
            for (Integer x : nv) System.out.print(x + ", ");
            System.out.println();
            
            if (ss[1].length() <= 8)
            {
                ArrayList<Integer> rk = rabinkarp(ss[0], ss[1]);
                System.out.print("Rabin-Karp:         ");
                for (Integer x : rk) System.out.print(x + ", ");
                assert rk.size() == nv.size() : "rk different numbers of results: " + rk.size() + " vs. " + nv.size();
                for (int i = 0; i < rk.size(); i++)
                    assert rk.get(i).compareTo(nv.get(i)) == 0 : "rk different result: " + rk.get(i) + " vs. " + nv.get(i); 
                System.out.println();
            }
            
            System.out.print("Boyer-Moore shifts tested: ");
            ArrayList<Integer> bm = boyermoore(ss[0], ss[1]);
            System.out.print("Boyer-Moore:        ");
            for (Integer x : bm) System.out.print(x + ", ");
            assert bm.size() == nv.size() : "bm different numbers of results: " + bm.size() + " vs. " + nv.size();
            for (int i = 0; i < bm.size(); i++)
                assert bm.get(i).compareTo(nv.get(i)) == 0 : "bm different result: " + bm.get(i) + " vs. " + nv.get(i); 
            System.out.println();
            
            ArrayList<Integer> km = knuthmorrispratt(ss[0], ss[1]);
            System.out.print("Knuth-Morris-Pratt: ");
            for (Integer x : km) System.out.print(x + ", ");
            assert km.size() == nv.size() : "km different numbers of results: " + km.size() + " vs. " + nv.size();
            for (int i = 0; i < km.size(); i++)
                assert km.get(i).compareTo(nv.get(i)) == 0 : "km different result: " + km.get(i) + " vs. " + nv.get(i); 
            System.out.println();
            
            System.out.println();
        }
    }
}
