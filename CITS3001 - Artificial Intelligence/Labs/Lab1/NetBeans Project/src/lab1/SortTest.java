package lab1;

import java.util.Arrays;

/**
 * Written by Gareth Lee, 2004. Revised by Du Huynh, Feb 2005. Revised by Tim
 * French, 2008
 */
public class SortTest {

    /**
     * SortTest Constructor
     */
    public SortTest() {

        // Check then compare
        //checkAlgo();
        compAlgo();
    }

    /**
     * Test all algorithms for correctness
     */
    public final void checkAlgo() {

        // Get random array
        int ranLen = (int) (Math.random() * 20) + 1;
        long[] arr = getRanArr(ranLen);

        // Print header
        String msg = "Original array contents";
        msg += " (length " + arr.length + "):";
        System.out.println(msg);

        // Print contents of array before sorting
        printArray(arr);
        System.out.println();

        // Create sorter
        Sorter s = new Sorter();

        // Make array clones
        long[] a1 = (long[]) arr.clone();
        long[] a2 = (long[]) arr.clone();
        long[] a3 = (long[]) arr.clone();
        long[] a4 = (long[]) arr.clone();

        // Create variables to to save array assignment counts
        int insCount;
        int mergeCount;
        int quickCount;

        // Sort arrays different ways using sorter
        s.insertionSort(a1);
        insCount = s.getCount();
        s.reset();

        s.mergeSort(a2);
        mergeCount = s.getCount();
        s.reset();

        s.quickSort(a3);
        quickCount = s.getCount();
        s.reset();

        s.radixSort(a4);
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

        // Print quick results
        System.out.println("After Radix Sort =");
        printArray(a4);
        System.out.println();

        // Sort using Java library to get guaranteed correct answer
        long[] compArr = (long[]) arr.clone();
        Arrays.sort(compArr);

        // Check that arrays are sorted
        boolean a1good = Arrays.equals(a1, compArr);
        boolean a2good = Arrays.equals(a2, compArr);
        boolean a3good = Arrays.equals(a3, compArr);
        boolean a4good = Arrays.equals(a4, compArr);
        boolean allGood = a1good && a2good && a3good && a4good;
        System.out.println("All arrays correct = " + allGood);
        System.out.println();
        System.out.println();
    }

    /**
     * Compare algorithm runtimes
     */
    public final void compAlgo() {

        // Create sorter
        Sorter s = new Sorter();

        // Print table header
        print(" Length  |                   Times (ns) ");
        String types = "         |   Insertion   |     Merge     |";
        types += "    Quick    |     Radix      ";
        print(types);

        // Number of tests wanted
        int numTests = 10;

        // For each test
        for (int i = 1; i < numTests + 1; i++) {

            // Row String
            String row = "";

            // Calculate length of testing array
            int testLen = i * 10;
            row += "  " + testLen + "  ";

            // Get random array of numbers
            long arr[] = getRanArr(testLen);

            // Make array clones
            long[] a1 = (long[]) arr.clone();
            long[] a2 = (long[]) arr.clone();
            long[] a3 = (long[]) arr.clone();
            long[] a4 = (long[]) arr.clone();

            // Time insertion sort
            long start1 = System.nanoTime();
            s.insertionSort(a1);
            row += "         " + addTime(start1);

            // Time merge sort
            long start2 = System.nanoTime();
            s.mergeSort(a2);
            row += "          " + addTime(start2);

            // Time quick sort
            long start3 = System.nanoTime();
            s.quickSort(a3);
            row += "          " + addTime(start3);

            // Time radix sort
            long start4 = System.nanoTime();
            s.quickSort(a4);
            row += "         " + addTime(start4);

            // Print row
            print(row);
        }
    }

    /**
     * Get an array of random numbers of a given length
     *
     * @param lenIn
     * @return
     */
    private long[] getRanArr(int lenIn) {

        // Make array
        int[] ranArr = new int[lenIn];

        // Populate array with random numbers
        for (int i = 0; i < ranArr.length; i++) {
            ranArr[i] = (int) (Math.random() * 50);
        }

        // Convert int array to string array
        int len = ranArr.length;
        String[] strArr = new String[len];
        for (int i = 0; i < len; i++) {
            strArr[i] = "" + ranArr[i] + "";
        }

        // Convert arguments to longs
        long longArr[] = new long[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            longArr[i] = Long.parseLong(strArr[i]);
        }

        // Return array
        return longArr;
    }

    /**
     * Add time information to table row
     *
     * @param startTime
     * @return
     */
    private String addTime(long startTime) {

        // Get end time
        long endTime = System.nanoTime();

        // Get duration
        long durNano = (endTime - startTime);

        // Return string
        return ("" + durNano + "");
    }

    /**
     * Helper method to print arrays
     *
     * @param numbers
     */
    private void printArray(long[] numbers) {
        for (long curNo : numbers) {
            System.out.print(curNo + "  ");
        }
        System.out.println();
    }

    /**
     * Quick print
     *
     * @param s
     */
    private void print(String s) {
        System.out.println(s);
    }
}
