

/**
 * Write a description of class test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Methods
{

    public static String compress(String s)
    {
        String result = s;

        for (int i = s.length(); i > 0; i--)
        {
            String oneSeq = "";
            for (int j = 0; j < i; j++)
            {
                oneSeq += "1";
            }
            String zeroSeq = oneSeq.replaceAll("1","0");

            result = result.replaceAll(oneSeq, "" + i + "");
            result = result.replaceAll(zeroSeq, "" + i + "");
        }

        return result;
    }

    public static double exp(double base, int pow)
    {
        if (pow ==0) 
        { 
            return 1;
        }
        else 
        {
            return (base * exp(base,pow-1));
        }
    }

    public static void largestSquare(int x, int y)
    {
        int ydiff = 20;
        int xdiff = 10;

        if (ydiff > xdiff)
        {

        }
    }

    public static String mkCompress(String s)
    {
        String z = "";
        int c = 0;
        int count = 0;
        
        for (int k = 0; k < s.length(); k++)
        {
            int comp = ('0' + c);
            int input = s.charAt(k);
            if (input == comp)
            {
                count++;
            }
            else
            {
                z = z + count + ",";
                c = 1 - c;
                count = 1;
            }
        }

        return z + count;
    }

    
    //2015 Q5b
    public static void nestSquares(int n, int d)
    {
        
        String col = "black";
        int pos = 0;
        int sidesize = n;

        for (int i = 0; i < n/2 ; i++)
        {
            System.out.println( "TLx and y= " + pos  + " , " + "SideSize = " + sidesize + ", Col =" + col);
            
            if (col == "black") { col = "white";}
            else { col = "black"; }
            
            pos += d;
            
            sidesize -= 2d;
            
        }
        
    }
    
    
}

