import java.util.ArrayList;

/**
 * Write a description of class Standalones here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Standalones
{

    public static int calculateFare(int status, int numZones, boolean SRused)
    {
        int fare = - 1;

        if (status == 1)
        {
            switch(numZones)
            {
                case 1 : fare = 250;
                break;
                case 2 : fare = 370;
                break;
            }
        }
        else if (status == 2) {
            switch(numZones)
            {
                case 1 : fare = 100;
                break;
                case 2 : fare = 150;
                break;
            }
        }

        if (SRused)
        {
            fare = (int) Math.round(fare*(1-0.15));
        }

        if (status == 3)
        {
            if (SRused)
            {
                fare = 50;
            }
            else 
            {
                return calculateFare(1,numZones,true);
            }
        }

        return fare;
    }

    /**
     * True if S1 is a substring of S2
     */
    public static boolean isSubstring(String S1, String S2)
    {
        int matchStreak = 0;
        for (int i = 0; i < S2.length(); i++)
        {
            int S2p = i;
            for (int j = 0; j < S1.length(); j++)
            {
                if (S2.charAt(S2p) == S2.charAt(j))
                {
                    matchStreak++;
                }

                if (matchStreak == S1.length())
                {
                    return true;
                }

                S2p++;
            }
            System.out.println();
        }

        return false;
    }

    public static char[][]  noWin = new char[][] { {'O','O','X'} , {'X','O','O'} , {'O','X','X'} };

    public static char[][]  row = new char[][] { {'X','O','X'} , {'X','X','X'} , {'O','X','O'} };
    public static char[][]  col = new char[][] { {'X','O','X'} , {'X','O','O'} , {'O','O','X'} };
    public static char[][]  diag = new char[][] { {'X','O','O'} , {'O','X','O'} , {'O','O','X'} };

    public static boolean isSame(char[] arr)
    {
        for (char c : arr)
        {
            if (arr[0] != c)
            {
                return false;
            }
        }
        return true;
    }

    public static String checkWinner (char[][] grid)
    {
        for (char[] row : grid)
        {
            if (isSame(row))
            {
                return ("Row win. " + "Letter won = " + row[0]);
            }
        }

        for (int i = 0; i < grid.length; i++) // for every column
        {
            // Construct column
            char[] col = new char[grid.length];            
            for (int j = 0; j < grid.length; j++)
            {
                col[j] = grid[j][i];
            }

            // Check column
            if (isSame(col))
            {
                return ("Column win. " + "Letter won = " + col[0]);
            }

        }

        char[] diag = new char[grid.length];  
        for (int i = 0; i < grid.length; i++)
        {      
            diag[i] = grid[i][i];
        }

        if (isSame(diag))
        {
            return ("Diag win. " + "Letter won = " + diag[0]);
        }

        return "No win";
    }

    public static int[] up = new int[] {5,1,5,1,5,6}; //up,down,up,down,up = More ups
    public static int[] down = new int[] {4,2,1,6,3}; // down, down, up, down = More downs
    public static int[] equal = new int[] {1,2,1,2,1}; // up,down,up,down = Equal

    public static String moreUpsThanDowns(int[] a)
    {
        int ups = 0;
        int downs = 0;

        for (int i = 0; i < a.length - 1; i ++)
        {
            if (a[i] < a[i+1])
            {
                ups++;
            }
            else if (a[i] != a[i+1])
            {
                downs++;
            }
        }

        if (ups > downs)
        {
            return "More ups";
        }
        else if (downs > ups)
        {
            return "More downs";
        }
        else if (downs == ups)
        {
            return "Equal";
        }

        return "Unknown";
    }

    public static int[] stest1 = new int[] {0,1,2,3,4};
    public static int[] stest2 = new int[] {8,10,4,9,3,56};

    public static int[][] separate (int[] a)
    {
        //assuming a is of even length

        ArrayList<Integer> even = new ArrayList<>();
        ArrayList<Integer> odd = new ArrayList<>();

        for (int i = 0; i < a.length; i++)
        {
            if (i%2 == 0)
            {
                even.add(a[i]);
            }
            else 
            {
                odd.add(a[i]);
            }
        }

        int[][] sep = new int[2][a.length/2];

        for (int j = 0; j < even.size(); j++)
        {
            sep[0][j] = even.get(j);
        }

        for (int j = 0; j < odd.size(); j++)
        {
            sep[1][j] = odd.get(j);
        }

        return sep;
    }

}

