/**
 * This class compares the running time of various sorting algorithms on random data.
 */

import java.util.Arrays;

public class Sorting
{
    // make sure runAndTimeSort knows all of the algorithms
    private static final int noOfAlgorithms = 3;
    
    private Sorting(){}
    
    /**
     * Sorts xs in-place using insert sort.
     */
    private static void insertSort(int[] xs)
    {
        for (int j = 1; j < xs.length; j++)
        {
            int key = xs[j];
            int i = j - 1;
            while (i >= 0 && xs[i] > key)
            {
                xs[i + 1] = xs[i];
                i -= 1;
            }
            xs[i + 1] = key;
        }
    }
    
    /**
     * Swaps xs[i] and xs[j].
     */
    private static void swap(int[] xs, int i, int j)
    {
        int x = xs[i];
        xs[i] = xs[j];
        xs[j] = x;
    }
    
    /**
     * Partitions xs[l..u] inclusive in-place around the pivot xs[u] 
     * and returns the final location of the pivot.
     * (Using a pre-determined pivot is not ideal...)
     */
    private static int partition(int[] xs, int l, int u)
    {
        int loc = l;
        for (int i = l; i < u; i++)
            if (xs[i] <= xs[u])
            {
               swap(xs, i, loc);
               loc += 1;
            }
        swap(xs, loc, u);
        return loc;
    }
    
    /**
     * Sorts xs[l..u] inclusive in-place using quick sort.
     */
    private static void qsort(int[] xs, int l, int u)
    {
        if (l < u)
        {
            int p = partition(xs, l, u);
            qsort(xs, l, p - 1);
            qsort(xs, p + 1, u);
        }
    }
    
    /**
     * Sorts xs in-place using quick sort.
     */
    private static void quickSort(int[] xs)
    {
        qsort(xs, 0, xs.length - 1);
    }
    
    /**
     * Sorts xs in-place using radix sort (https://en.wikipedia.org/wiki/Radix_sort). 
     * This version assumes all elements are non-negative: we could fix this 
     * by shifting all elements up by N, where -N is the smallest number, 
     * then shifting them back after sorting. 
     */
    private static void radixSort(int[] xs)
    {
        int[][] buckets = new int[10][xs.length + 1];
        // find the highest number
        int max = Integer.MIN_VALUE;
        for (int x : xs) max = Math.max(max, x);
        // for each digit, starting at the least significant
        for (int d = 1; d <= max; d *= 10)
        {
            // copy each element into the appropriate bucket for this digit
            for (int i = 0; i < xs.length; i++)
            {
                int k = xs[i] / d % 10;
                buckets[k][0] += 1; 
                buckets[k][buckets[k][0]] = xs[i];
            }
            // copy the elements from the buckets back into xs
            int z = 0;
            for (int i = 0; i < 10; i++)
            {
                for (int j = 1; j <= buckets[i][0]; j++)
                {
                    xs[z] = buckets[i][j];
                    z += 1;
                }
                // zero the bucket count for the next iteration
                buckets[i][0] = 0;
            }
        }
    }
    
    /**
     * Returns an array of length size containing random values in 0..9999.
     */
    private static int[] generateRandomArray(int size)
    {   
        int[] xs = new int[size];
        for (int i = 0; i < size; i++)
            xs[i] = (int) (Math.random() * 10000);
        return xs;
    }
    
    /**
     * Checks that ys is a sorted version of xs.
     * Cheats by using the built-in sort on a copy of xs. :) 
     */
    private static void validateSort(String s, int[] xs, int[] ys)
    {
        int[] xsn = Arrays.copyOf(xs, xs.length);
        Arrays.sort(xsn);
        assert Arrays.mismatch(xsn, ys) == -1 : 
               s + "sort is not correct for size " + xs.length;
    }
    
    /**
     * Calls and times one sorting algorithm denoted by alg, 
     * checks the result, and returns the time in nanoseconds.
     */
    private static double runAndTimeSort(int[] original, int alg)
    {
        assert 0 <= alg && alg < noOfAlgorithms : "no algorithm defined";
        int[] xs = Arrays.copyOf(original, original.length);
        double time = -System.nanoTime();
        switch (alg)
        {
            case 0 : insertSort(xs); break;
            case 1 :  quickSort(xs); break;
            case 2 :  radixSort(xs); 
        } 
        time += System.nanoTime();
        validateSort(alg + "", original, xs);
        return time;
    }
    
    /**
     * Rounds nanoseconds to milliseconds, to one decimal place. 
     */
    private static double round(double x)
    {
        return Math.round(x / 100000) / 10.0;
    }
    
    /**
     * Creates the data, calls and times each sorting algorithm, 
     * checks all results, and tabulates the output times in milliseconds. 
     */
    public static void compareSorts()
    {
        int instancesPerSize = 10;
        for (int size = 10000; size < 1000000; size *= 2)
        {
            double[] totalTimes = new double[noOfAlgorithms]; 
            for (int i = 0; i < instancesPerSize; i++)
            {
                int[] original = generateRandomArray(size);
                for (int alg = 0; alg < noOfAlgorithms; alg++)
                    totalTimes[alg] += runAndTimeSort(original, alg);
            }
            System.out.print(size + "       ");
            for (double time : totalTimes)
                System.out.print(round(time / instancesPerSize) + "ms      ");
            System.out.println();
        }
    }
}
