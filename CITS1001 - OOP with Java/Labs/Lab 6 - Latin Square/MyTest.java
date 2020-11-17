

public class MyTest
{
    Lab lab = new Lab();
    int[][] zeroes = new int[][] {  {0,0} , {0,0} , {0,0}  } ;
    int[][] ones   = new int[][] {  {1, 1, 1} , {1, 1, 1} , {1, 1 , 1}  } ;
    int[][] array1 = new int[][] {  {1, 3},     {7, -2, 0}, {4, 4} } ;
    int[][] array2 = new int[][] {  {5, 10},    {4, 8, 12}, {9, 9} } ;
    int[][] latin = new int [][] {  {1, 2, 3} , {2, 3, 1} , {3, 1 , 2} };

    int[][] sudoku = new int[][] { 
            {4,2,9,8,1,3,5,6,7}, {5,1,6,4,7,2,9,3,8}, {7,8,3,6,5,9,2,4,1},
            {6,7,2,1,3,4,8,5,9}, {3,9,5,2,8,6,1,7,4}, {8,4,1,7,9,5,6,2,3},
            {1,5,8,3,6,7,4,9,2}, {9,3,4,5,2,8,7,1,6}, {2,6,7,9,4,1,3,8,5} 
        }; 

    int[][] nonsud = new int[][] {
            {4,2,3,4,5,6,7,8,9},{1,5,6,7,8,9,1,2,3},{7,8,9,1,2,3,4,5,6},
            {3,1,2,9,7,8,6,4,5}, {6,4,5,3,1,2,9,7,8}, {9,7,8,6,4,5,3,1,2},
            {2,3,1,5,6,4,8,9,7}, {5,6,4,8,9,7,2,3,1}, {8,9,7,2,3,1,5,6,4}
        };

    
    public  void maxT()
    {
        System.out.println(lab.max(zeroes));
        System.out.println(lab.max(ones));
        System.out.println(lab.max(array1));
        System.out.println(lab.max(array2));
    }

    
    public  void rowSumT()
    {
        System.out.println(lab.rowSum(zeroes, 0));
        System.out.println(lab.rowSum(ones, 0));
        System.out.println(lab.rowSum(array1, 0));
        System.out.println(lab.rowSum(array2, 0));
    }

    
    public  void columnSumT()
    {
        System.out.println(lab.columnSum(zeroes, 0));
        System.out.println(lab.columnSum(ones, 0));
        System.out.println(lab.columnSum(array1, 0));
        System.out.println(lab.columnSum(array2, 0));
    }

    
    public void isSquareT()
    {
        System.out.println(lab.isSquare(zeroes));
        System.out.println(lab.isSquare(ones));
        System.out.println(lab.isSquare(array1));
        System.out.println(lab.isSquare(array2));
    }

    
    public void allRowSumsT()
    {
        helper(zeroes);
        helper(ones);
        helper(array1);
        helper(array2);
    }

    private void helper(int[][] input)
    {
        System.out.println(" ");
        for (int i : lab.allRowSums(input))
        {
            System.out.print(i + " , ");
        }
        System.out.println();
    }

    
    public  void inRowT()
    {
        System.out.println("False  " + LatinSquare.inRow(1,0, zeroes)); 
        System.out.println("True  " + LatinSquare.inRow(1,2, ones)); 
        System.out.println("True  " + LatinSquare.inRow(-2,1, array1)); 
        System.out.println("False  " + LatinSquare.inRow(8,2, array2)); 
    }

    
    public  void inColumnT()
    {
        System.out.println("False  " + LatinSquare.inColumn(1,0, zeroes)); 
        System.out.println("False  " + LatinSquare.inColumn(0,0, ones)); 
        System.out.println("True  " + LatinSquare.inColumn(0,2, array1)); 
        System.out.println("True  " + LatinSquare.inColumn(12,2, array2)); 
    }

    
    public  void latinT()
    {
        System.out.println("True  " + LatinSquare.isLatin(latin));
    }

    
    public  void sudokuT()
    {
        System.out.println("True  " + LatinSquare.isSudoku(sudoku));
        System.out.println("False  " + LatinSquare.isSudoku(nonsud));
    }

    
    public  void totalSumT()
    {
        System.out.println(lab.totalSum(zeroes));
        System.out.println(lab.totalSum(ones));
        System.out.println(lab.totalSum(array1));
        System.out.println(lab.totalSum(array2));
        System.out.println(lab.totalSum(latin));
        System.out.println(lab.totalSum(sudoku));
        System.out.println(lab.totalSum(nonsud));
    }
}
