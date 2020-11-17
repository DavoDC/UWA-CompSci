package Wk2;

/**
 * Written by Gareth Lee, 2004. Revised by Du Huynh, Feb 2005. Revised by Tim
 * French, 2008
 */
import java.util.Random;
import CITS2200.Sort;
import java.util.Arrays;

public class SortTest
{

    public SortTest()
    {
        // Make array of a random size
        int ranLen = (int) (Math.random() * 20) + 1;
        //ranLen = 5; //temp
        int[] testData = new int[ranLen];

        // Populate with random numbers
        for (int i = 0; i < testData.length; i++)
        {
            testData[i] = (int) (Math.random() * 50);
        }

        // Convert Int array to string array
        int len = testData.length;
        String[] args = new String[len];
        for (int i = 0; i < len; i++)
        {
            args[i] = "" + testData[i] + "";
        }

        // Act on input
        switch (args.length)
        {
            case 0:
                usage();
                break;
            case 1:
                //runMain(Integer.parseInt(args[0]));
                System.out.println("Length 1");
                break;
            default:
                /* convert arguments to longs */
                long a[] = new long[args.length];
                for (int i = 0; i < args.length; i++)
                {
                    a[i] = Long.parseLong(args[i]);
                }
                testMain(a);
                break;
        }
    }

    /**
     * Prints usage and terminates program
     */
    public static void usage()
    {
        System.out.println("Usage:");
        System.out.println("e.g. For testing a given array:");
        System.out.println("  java SortTest 9 7 6 5 3");
        System.out.println("e.g. For testing a random array, specify the size:");
        System.out.println("  java SortTest 1000");
        System.exit(1);
    }

    /**
     * Prints error message and terminates program
     */
    public static void pperr(String msg)
    {
        System.out.println("Error:");
        System.out.println(msg);
        System.exit(1);
    }

    /**
     * Method for testing a given array of long integers for debugging.
     *
     * @param a
     * @arg a the array of longs to be sorted.
     */
    public static void testMain(long[] a)
    {
        // print contents of array a before sorting
        System.out.println("Original array contents");
        printArray(a);
        System.out.println();

        // Create sort class
        Sort s = new Sorter();

        // Make array clones
        long[] a1 = (long[]) a.clone();
        long[] a2 = (long[]) a.clone();
        long[] a3 = (long[]) a.clone();

        // Create variables to to save array assignment counts
        int insCount = 0;
        int mergeCount = 0;
        int quickCount = 0;

        // Sort arrays using sorter
        s.insertionSort(a1);
        insCount = s.getCount();
        s.reset();

        s.mergeSort(a2);
        mergeCount = s.getCount();
        s.reset();

        s.quickSort(a3);
        quickCount = s.getCount();
        s.reset();

        // Print insertion results
        System.out.println("After Insertion Sort =");
        printArray(a1);
        System.out.println("Array assignments = " + insCount);
        System.out.println();

        // Print merge results
        System.out.println("After Merge Sort =");
        printArray(a2);
        System.out.println("Array assignments = " + mergeCount);
        System.out.println();

        // Print quick results
        System.out.println("After Quick Sort =");
        printArray(a3);
        System.out.println("Array assignments = " + quickCount);
        System.out.println();

        // Check arrays
        long[] compArr = (long[]) a.clone();
        Arrays.sort(compArr);
        boolean a1good = Arrays.equals(a1, compArr);
        boolean a2good = Arrays.equals(a2, compArr);
        boolean a3good = Arrays.equals(a3, compArr);
        boolean allGood = a1good && a2good && a3good;
        System.out.println("All arrays sorted properly = " + allGood);
        System.out.println();

    }

    /**
     * Helper method to print arrays
     */
    private static void printArray(long[] numbers)
    {
        for (long curNo : numbers)
        {
            System.out.print(curNo + "  ");
        }
        System.out.println();
    }

    /**
     * Method for testing an array of randomly generated long integers.
     *
     * @param size
     * @arg size the length of the array to be generated.
     */
    public static void runMain(int size)
    {
        if (size <= 0)
        {
            pperr("runMain: argument must be a positive integer.");
        }

        long a[] = new long[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++)
        {
            a[i] = rand.nextLong();
        }
        Sort s = new Sorter();
        s.reset();

        //INSERTIONSORT
        long[] a1 = (long[]) a.clone();
        s.insertionSort(a1);
        for (int i = 1; i < size; i++)
        {
            if (a1[i - 1] > a1[i])
            {
                System.out.println("Sort Error");
                return;
            }
        }
        System.out.println("Insertionsort used " + s.getCount() + " assignments to sort " + size + " longs");

        //MERGESORT
        s.reset();
        a1 = (long[]) a.clone();
        s.mergeSort(a1);
        for (int i = 1; i < size; i++)
        {
            if (a1[i - 1] > a1[i])
            {
                System.out.println("Sort Error");
                return;
            }
        }
        System.out.println("Mergesort used " + s.getCount() + " assignments to sort " + size + " longs");

        //QUICKSORT
        s.reset();
        a1 = (long[]) a.clone();
        s.quickSort(a1);
        for (int i = 1; i < size; i++)
        {
            if (a1[i - 1] > a1[i])
            {
                System.out.println("Sort Error");
                return;
            }
        }
        System.out.println("Quicksort used " + s.getCount() + " assignments to sort " + size + " longs");
    }

}
