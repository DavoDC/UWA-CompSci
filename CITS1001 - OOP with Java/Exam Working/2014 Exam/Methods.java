import java.util.ArrayList;

public class Methods
{
    
    
    public static void countEvens()
    {
        ArrayList<String> list = new ArrayList<>();
        
        list.add("nup");
        list.add("yeah");
        list.add("yol");
        list.add("at");
        list.add("odddd");
        
        int evens = 0;
        
        for (String s : list)
        {
            if (s.length() % 2 == 0 && s.length() != 0)
            {
                evens++;
            }
            
            System.out.println(s);
        }

        System.out.println("Even = " + evens);
    }
    
    public static ArrayList<Integer> mirror()
    {
        ArrayList<Integer> input = new ArrayList<>();
        input.add(3);
        input.add(6);
        input.add(9);
        
        ArrayList<Integer> output = input;
        for (int i = input.size()-1; i != -1; i--)
        {
            output.add(input.get(i));
        }
        
        return output;
    }
    
    public static String validCheck(int day, int month)
    {
        if (day <= 0 || day > 31) 
        {
            return "Invalid day";
        }
        else if (month <= 0 || month > 12 )
        {
            return "Invalid month";
        }
        return "Uncaught";
    }
    
    public static void window()
    {
        String sill = "+===+===+";
        String middle = "|   |    |";
        
        for (int i = 3; i != 10; i++)
        {
            if (i % 3 == 0)
            {
                System.out.println(sill);
            }
            else 
            {
                System.out.println(middle);
            }
        }
    }
}
