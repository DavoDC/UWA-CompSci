import java.util.ArrayList;

/**
 * Write a description of class LatinSquare here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class LatinSquare
{
    static Lab lab = new Lab();

    // returns true iff n is in the xth row of a
    // e.g. inRow(4, 1,  {{3, 5}, {3, 6, 4}, {2}}) = true, 
    // e.g. inRow(4, 2,  {{3, 5}, {3, 6, 4}, {2}}) = false
    public static boolean inRow(int number, int row, int[][] a )
    {
        boolean result = false;

        for (int i : a[row] )
        {
            if (i == number)
            {
                result = true;
            }
        }

        return result;
    }

    // returns true iff n is in the xth column of a
    // e.g. inColumn(4, 2, {{3, 5}, {3, 6, 4}, {2}}) = true, 
    // e.g. inColumn(4, 1, {{3, 5}, {3, 6, 4}, {2}}) = false
    public static boolean inColumn(int number, int column,  int[][]a)
    {
        boolean result = false;

        for (int[] i : a)
        {
            for (int j : i)
            {
                if (i.length-1 >= column)
                {
                    if (i[column] == j)
                    {
                        if (j == number)
                        {
                            result = true;
                        }
                    }
                }

            }

        }

        return result;
    }

    // returns true iff a is a Latin Square containing the numbers 1..n 
    // a must be a square of side n, and every row and every column must be a permutation of 1..n
    // http://en.wikipedia.org/wiki/Latin_square
    public static boolean isLatin(int[][] a)
    {
        boolean check1 = lab.isSquare(a); // Checks if it is a square

        boolean check2 = true; // Check if all row sums are the same
        int rows = 0;
        int[] rowsums = lab.allRowSums(a);
        for (int i : rowsums)
        {
            rows++;
            if (i != rowsums[0] )
            {
                check2 = false;
            }
        }

        int columns = rows;
        boolean check3 = true; // Checks whether columns have different numbers
        for (int[] i : a)
        {
            int comparison = i[0];
            i[0] = -i[0];

            for (int j : i)
            {
                if (comparison == j )
                {
                    check3 = false;
                }
            }

            i[0] = comparison;
        }

        return (check1 && check2 && check3);
    }

    // CHALLENGE (optional): 
    // returns true iff a is legal, complete Sudoku board 
    // http://en.wikipedia.org/wiki/Sudoku
    public static boolean isSudoku(int[][] a)
    {
        boolean check1 = isLatin(a); // Check that it is a latin square

        boolean check2 = true; // Check that the numbers are between 1 and 9
        for (int[] i : a)
        {
            for (int j : i)
            {
                if (j < 1  || j > 9 )
                {
                    check2 = false;
                }
            }
        }

        boolean check3 = true; //Check rows add up to 45
        int[] rowsums = lab.allRowSums(a);
        for (int x : rowsums)
        {
            if (x != 45)
            {
                check3 = false;
            }
        }
     
        boolean check4 = true; //Check columns add up to 45
        int checked = 0;
        while (checked != 9)
        {
            if (lab.columnSum(a, checked) != 45)
            {
                check4 = false;
            }
            checked++;
        }

        
        boolean check5 = true; //Check that there are 9 of each digit
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int [] i : a)
        {
            for (int j : i)
            {
                numbers.add(j);
            }
        }
        
        int count = 0;
        for (int i : numbers)
        {
            if (i == 1)
            {
                count++;
            }
            else if (i == 2)
            {
                count++;
            }
            else if (i == 3)
            {
                count++;
            }
            else if (i == 4)
            {
                count++;
            }
            else if (i == 5)
            {
                count++;
            }
            else if (i == 6)
            {
                count++;
            }
            else if (i == 7)
            {
                count++;
            }
            else if (i == 8)
            {
                count++;
            }
            else if (i == 9)
            {
                count++;
            }
        }
        
        if (count != 81)
        {
            check5 = false;
        }
        
        return (check1 && check2 && check3 && check4 && check5);
    }
}
