import java.util.ArrayList;

/**
 * Write a description of class Items here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Items
{
    // instance variables - replace the example below with your own
    private String name;

    /**
     * Constructor for objects of class Items
     */
    public Items(String name)
    {
        this.name = name;
    }

    public String getname()
    {
        return name;

    }

    public String mystery(int n)
    {
        String soln = "";
        while (n > 0)
        {
            soln = n % 10 + soln;
            n = n / 100;
        }
        return soln;
    }

    public int arraymethod()
    {
        int[] xs = {6, 7, 8, 9, 10};
        int[] ys = xs;
        ys[ys.length - 1] = 100;
        ys = new int[] {4, 3, 2, 1};
        ys[ys.length - 1] = 1000;
        return xs[xs.length - 1];
    }

    public static void mystery2017() 
    {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        
        for (int i = 0; i < list.size(); i++) {
            int element = list.get(i);
            list.remove(i);
            list.add(0, element + 1);
        }
        
        
        for (int i : list)
        {
            System.out.println(i);
        }
    }

}
