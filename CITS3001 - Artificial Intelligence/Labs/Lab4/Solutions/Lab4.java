/**
 * Exercises two algorithms for the 0-1 Knapsack problem; 
 * the basic recursive algorithm, and the dynamic programming algorithm. 
 */
import java.util.*;

public class Lab4
{
    // each item has a weight, a value, and an ID
    ArrayList<int[]> items;
    
    public Lab4()
    {
        // experiments for debugging purposes
        items = new ArrayList<>();
        for (int[] item : new int[][] {{1,22,11}, {2,3,12}, {3,42,13}, {4,52,14}})
            items.add(item);
        ArrayList<Integer> kp;
        int totalw = 0; 
        for (int[] xs : items) 
            totalw += xs[0];
        for (int w = 1; w <= totalw; w++)
        {
            System.out.println("Weight: " + w);
            for (int i = 0; i < 2; i++)
            {
                kp = DP01(items, w);
                printknapsack("DP ", kp);
                kp = rec01(items, w);
                kp.remove(0);
                printknapsack("rec", kp);
                // reverse the items
                reverse (items);
            }
        }
        
        // experiments on the effect of item-ordering
        // all results should be the same, although the order of the items will vary
        items = randomData(40);
        totalw = 0;
        for (int[] xs : items) 
            totalw += xs[0];
        for (int w = totalw / 32; w <= totalw; w *= 2)
        {    
            System.out.println("Weight: " + w);
            // try ten random orders
            for (int i = 0; i < 10; i++)
            {
                kp = DP01(items, w);
                printknapsack("DP ", kp);
                /* kp = rec01(items, w);
                kp.remove(0);
                printknapsack("rec", kp);  */
                // randomise the items each time
                randomise (items);
            }
        }
    }
    
    /**
     * Returns a random set of n items, within defined parameters.
     * Each item has a weight, a value, and an ID
    */
    private ArrayList<int[]> randomData(int n)
    {
        int minvalue  = 1000;
        int maxvalue  = 5000;
        int minweight = 1000;
        int maxweight = 5000;
        ArrayList<int[]> items = new ArrayList<>();
        for (int i = 0; i < n; i++)
        {
            int[] item = new int[3];
            item[0] = (int) (Math.random() * (maxvalue  - minvalue  + 1) + minvalue);
            item[1] = (int) (Math.random() * (maxweight - minweight + 1) + minweight);
            item[2] = i + 101;
            items.add(item);
        }
        return items;
    }
    
    /**
     * Returns the best solution for capacity w and the items IN ANY ORDER. 
     * Returns the IDs of the packed items.
     * Also includes the value of the returned subset as the first item. 
     */
    private ArrayList<Integer> rec01(ArrayList<int[]> items, int w)
    {
        ArrayList<Integer> zs = new ArrayList<>();
        zs.add(0);
        if (w > 0)
           for (int k = 0; k < items.size(); k++)
           {
               int[] curr = items.remove(k);
               ArrayList<Integer> xs = rec01(items, w);
               if (curr[0] <= w)
               {
                  ArrayList<Integer> ys = rec01(items, w - curr[0]);
                  ys.add(curr[2]);
                  ys.set(0, ys.get(0) + curr[1]);
                  if (ys.get(0) > xs.get(0)) xs = ys;
               }
               items.add(k, curr);
               if (xs.get(0) > zs.get(0)) zs = xs;
           }
        return zs;
    }
    
    /**
     * Runs the dynamic programming algorithm for capacity w and the items 
     * IN THE GIVEN ORDER. Returns the IDs of the packed items.
     */
    private ArrayList<Integer> DP01(ArrayList<int[]> items, int w)
    {
        // create the table
        int[][] table = new int[items.size() + 1][w + 1];
        for (int m = 1; m < table.length; m++)
            for (int wp = 1; wp <= w; wp++)
                if (wp >= items.get(m - 1)[0])
                   // if item m fits into wp, consider both possibilities
                   table[m][wp] = Math.max(table[m - 1][wp - items.get(m - 1)[0]] 
                                             + items.get(m - 1)[1], 
                                           table[m - 1][wp]);
                else 
                   // can't fit item m
                   table[m][wp] = table[m - 1][wp];
        // extract the items
        ArrayList<Integer> zs = new ArrayList<>();
        int m = table.length - 1;
        int wp = w;
        while (m > 0 && wp > 0)
        {
            // if the value with this item exceeds the value w/o it, it is included 
            if (table[m][wp] > table[m - 1][wp])
            {
                zs.add(items.get(m - 1)[2]);
                wp -= items.get(m - 1)[0];
            }
            m -= 1;
        }
        return zs;
    }
    
    /**
     * Reverses xs in-place.
    */
    private void reverse(ArrayList<int[]> xs)
    {
        for (int k = 0; k < xs.size() / 2; k++)
            swap(xs, k, xs.size() - 1 - k);
    }
    
    /**
     * Randomises xs in-place.
    */
    private void randomise(ArrayList<int[]> xs)
    {
        for (int k = 0; k < xs.size() - 2; k++)
        {
            // choose an element x[j], j >= k, to put at x[k]
            int r = (int) (Math.random() * (xs.size() - k));
            swap(xs, k, k + r);
        }
    }
    
    /**
     * Swaps xs.get(a) and xs.get(b) in-place.
     * Assumes a and b are both legal indices.
    */
    private void swap(ArrayList<int[]> xs, int a, int b)
    {
        int[] x = xs.get(a);
        xs.set(a, xs.get(b));
        xs.set(b, x);
    }
    
    /**
     * Displays the knapsack kp labelled with s, and its value with the current items.
     */
    private void printknapsack(String s, ArrayList<Integer> kp)
    {
        System.out.print(s + ": ");
        int v = 0;
        for (int k : kp) 
        {
            System.out.print(k + " ");
            for (int[] item : items)
                if (item[2] == k) v += item[1];
        }
        System.out.println("- " + v);
    }
}
