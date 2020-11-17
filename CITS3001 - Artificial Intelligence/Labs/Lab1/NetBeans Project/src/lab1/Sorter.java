package lab1;

import java.util.ArrayList;

/**
 * Lab 1 A class to give a comparative view of common sorting algorithms. The
 * maintains a private static variable that counts the number of array
 * assignments that are performed (as an approximate measure of the complexity
 * of the algorithm.
 *
 * @author Tim French.
 *
 */
public class Sorter {

    // Constant for Radix sort
    private final int RADIX = 10;

    // Array assignment count 
    private int count;

    /**
     * Returns the number of array assignment operations performed by this class
     * since the count variable was rest.
     *
     * @return the number of assignments
     *
     */
    public int getCount() {
        return count;
    }

    /**
     * Resets the counter variable to 0
     *
     */
    public void reset() {
        count = 0;
    }

    /**
     * Helper method that swaps the elements at the given positions
     */
    private void swap(long[] arr, int pos1, int pos2) {
        long temp = arr[pos1];
        arr[pos1] = arr[pos2];
        count++;
        arr[pos2] = temp;
        count++;
    }

    /**
     * Executes the insertion sort algorithm sorting the argument array. There
     * is no return as the parameter is to be mutated.
     *
     * @param a the array of long integers to be sorted
     *
     */
    public void insertionSort(long[] a) {

        // Iterate starting at array pos 1 
        for (int outerIndex = 1; outerIndex < a.length; outerIndex++) {

            // Initialise inner to outer
            int innerIndex = outerIndex;

            // Keep shifting current element left 
            // until element left of it is less.
            // While inner not Negative and Element left is greater
            while ((innerIndex > 0) && (a[innerIndex - 1] > a[innerIndex])) {

                // Move element left
                swap(a, innerIndex, innerIndex - 1);

                // Decrease inner
                innerIndex--;
            }
        }
    }

    /**
     * Initiates mergeSort algorithm
     *
     * @param array the array of long integers to be sorted
     *
     */
    public void mergeSort(long[] array) {

        // Sort the full range of numbers
        mergeSortDrive(array, 0, array.length - 1);
    }

    /**
     * Drives mergeSort algorithm
     */
    private void mergeSortDrive(long[] a, int l, int r) {

        // If right exceeds left
        // (This stops recursion when array length is 1)
        if (r > l) {
            // Save average of l, r into mid
            int mid = (l + r) / 2;  // Decimal part dropped

            // Merge sort left parrt
            mergeSortDrive(a, l, mid);

            // Merge sort right part
            mergeSortDrive(a, mid + 1, r);

            // Merge both parts
            merge(a, l, mid, r); // Merge
        }
    }

    /**
     * A private method to merge the elements in the array between p and r. the
     * array a, between the indices p and r, inclusive. Given the array is
     * sorted between p and q and q+1 and r sorts the array between p and r.
     *
     * @param arr the array to be sorted, which is mutated by the method
     * @param lo the lower index of the range to be partitioned
     * @param mid the midpoint of the two sorted sections.
     * @param hi the upper index of the range to be partitioned
     * @return the index of the point of partition
     *
     */
    private void merge(long[] arr, int lo, int mid, int hi) {

        //// Setup left array
        int leftLen = mid - lo + 1; // leftLen is mid-lo+1
        long[] left = new long[leftLen]; // Make new array 
        for (int i = 0; i < leftLen; i++) // loop: leftLen exceeds i0  
        {
            left[i] = arr[lo + i]; // left i gets arr lo+1
            count++;
        }

        //// Setup right array
        int rLen = hi - mid;  // rLen is hi-mid
        long[] right = new long[rLen]; // Make new array
        for (int i = 0; i < rLen; i++) // loop: rLen exceeds i0   
        {
            right[i] = arr[mid + i + 1]; // right i gets arr mid+i+1
            count++;
        }

        // Setup ints li and ri
        int li = 0;
        int ri = 0;

        // Loop over numbers
        for (int i = lo; i <= hi; i++) {

            // If left index has reached maximum
            if (li == leftLen) {

                // Array gets value from right
                arr[i] = right[ri++];
            } else if (ri == rLen || right[ri] > left[li]) {

                // If right index has reach maximum, 
                // or right exceeds left
                // Array gets the smaller value (left)
                arr[i] = left[li++];
            } else {

                // Otherwise, get value from right
                arr[i] = right[ri++];
            }
            count++;
        }
    }

    /**
     * Executes the quick sort algorithm sorting the argument array. There is no
     * return as the parameter is to be mutated.
     *
     * @param a the array of long integers to be sorted
     *
     */
    public void quickSort(long[] a) {

        // Drive using array , 0, len-1
        qSortDrive(a, 0, a.length - 1);
    }

    /**
     * Helper method that drives the quick sorting algorithm
     *
     * @param arr
     * @param lowBound
     * @param hiBound
     */
    private void qSortDrive(long[] arr, int lo, int hi) {
        // If high exceeds low
        if (hi > lo) {

            // Partition using array, low, hi and save returned pivot
            int pivot = qSortPartition(arr, lo, hi);

            // Sort left part = lo to pivot-1
            qSortDrive(arr, lo, pivot - 1);

            // Sort right part = pivot + 1 to hi
            qSortDrive(arr, pivot + 1, hi);
        }
    }

    /**
     *
     * @param arr
     * @param lowBound
     * @param hiBound
     */
    private int qSortPartition(long[] arr, int lo, int hi) {

        // Get highElem
        long highElem = arr[hi];

        // Set outer to lo-1
        int outerIndex = lo - 1;

        // Iterate over section
        // Initialise innerIndex to lo
        // Condition is hi-1 exceeds/equal innerIndex
        for (int innerIndex = lo; hi - 1 >= innerIndex; innerIndex++) {
            // If highElem exceeds/equals innerElem
            if (highElem >= arr[innerIndex]) {
                // Increase outer THEN swap outer and inner
                outerIndex++;
                swap(arr, outerIndex, innerIndex);
            }
        }

        // Swap outer+1 and highest
        swap(arr, outerIndex + 1, hi);

        // Return outer + 1
        return outerIndex + 1;
    }

    /**
     * Radix sort algorithm
     *
     * Time Complexity of Solution: O(k*n) (Worst, best average) (k is the
     * length of the longest number and n is array size)
     *
     * https://www.java67.com/2018/03/how-to-implement-radix-sort-in-java.html
     *
     * @param input
     */
    public void radixSort(long[] input) {

        // Setup bucket array
        ArrayList<Long>[] bucket = new ArrayList[RADIX];
        for (int i = 0; i < bucket.length; i++) {
            bucket[i] = new ArrayList<>();
        }

        // Setup local variables
        boolean maxLength = false;
        long tmp = -1;
        long placement = 1;

        // While Maxlength is false
        while (!maxLength) {

            // Set max length to true
            maxLength = true;

            // Split input between lists
            for (long i : input) {
                tmp = i / placement;
                int index = (int) tmp % RADIX;
                bucket[index].add(i);
                if (maxLength && tmp > 0) {
                    maxLength = false;
                }
            }

            // Empty lists into input array
            int a = 0;
            for (int b = 0; b < RADIX; b++) {
                for (Long i : bucket[b]) {
                    input[a++] = i;
                }
                bucket[b].clear();
            }

            // Move to next digit
            placement *= RADIX;
        }
    }
}
