public class Lab
{
    // returns the largest element in a 
    // e.g. max({{1, 3}, {7, -2, 0}, {4, 4}}) = 7
    public static int max(int[][] a)
    {
        int max = 0;

        for (int[] i : a)
        {

            for (int j : i)
            {

                if (j > max)
                {
                    max = j;
                }

            }

        }

        return max;
    }

    // returns the sum of the elements in the xth row in a 
    // e.g. rowSum({{3}, {8, -2, 6}, {5, -2}}, 1) = 8 + (-2) + 6 = 12
    public static int rowSum(int[][] a, int x)
    {
        int sum = 0;

        for (int i : a[x])
        {
            sum += i;
        }

        return sum;
    }

    // returns the sum of the elements in the xth column in a
    // e.g. columnSum({{3, 4}, {6}, {0, 8, -2}}, 1) = 4 + 8 = 12 
    public static int columnSum(int[][] a, int x)
    {
        int sum = 0;

        for (int[] i : a)
        {
            sum += i[x];
        }

        return sum;
    }

    // returns an array holding the sums of the rows in the inputted array
    // e.g. allRowSums({{1}, {2, 3}, {4, 5, 6}}) = {1, 5, 15}
    public static int[] allRowSums(int[][] a)
    {
        int[] rowsums = new int[a.length];
        int counter = -a.length;
  
        for (int [] i : a)
        {
            int cursum = 0;
            for (int j : i)
            {
                cursum += j;
            }

            rowsums[a.length + (counter) ] = cursum;
            counter++;
        }
        
        
        return rowsums;
    }

    public static int[] allRowSums2(int[][] a)
    {
        int[] rowsums = new int[a.length];
        int repeats = a.length-1; 
  
        while (repeats != -1)
        {
            rowsums[repeats] += rowSum(a , repeats); 
            
            repeats--;
        }
        
        return rowsums;
    }
    // returns true iff a is square, 
    // i.e. iff every row has the same length as the number of rows 
    public static boolean isSquare(int[][] a)
    {
        int rows = a.length;
        int columns = 0;
        int numbers = 0;

        for (int[] i : a)
        {

            if (i.length > columns)
            {
                columns += i.length;
            }

            for (int j : i)
            {
                numbers++;
            }
        }

        boolean check1 = (rows * columns) == numbers;
        boolean check2 = (columns/rows) == 1;

        return (check1 && check2);
    }
    

    public static int totalSum(int[][] a)
    {
        int sum = 0;
        
        for (int row = a.length-1; row != -1; row--)  //go from last index to 0
        {
            
            for (int column = a[row].length-1 ; column != -1 ; column--)
            {
                sum += a[row][column];
            }
        
        }
        
        return sum;
    }
}

