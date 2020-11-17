package Wk4.Deques; // Remove getState too!!

import CITS2200.Deque;
import CITS2200.Overflow;
import CITS2200.Underflow;

/**
 * Lab 3 A doubled ended queue implemented using a circular array
 *
 * @author David
 */
public class DequeCyclic implements Deque
{

    /**
     * Holds the elements of the deque
     */
    private Object[] array;

    /**
     * Keeps track of the current number of elements added
     */
    private int itemCount;

    /**
     * Keeps track of the leftmost index
     */
    private int left;

    /**
     * Create a new DequeCyclic with a size of s
     *
     * @param s
     */
    public DequeCyclic(int s)
    {
        // Initialise array
        array = new Object[s];

        // Initialise counters
        left = 0;
        itemCount = 0;
    }

    /**
     * Return true if the deque is empty.
     *
     * @return True when itemCount is 0
     */
    @Override
    public boolean isEmpty()
    {
        return itemCount == 0;
    }

    /**
     * Returns true if the deque is full. False otherwise
     *
     * @return True if itemCount = size of deque
     */
    @Override
    public boolean isFull()
    {
        return itemCount == array.length;
    }

    /**
     * Add object o to the left of the deque. If full, throw an Overflow
     * exception
     *
     * @param o Object to be added
     * @throws Overflow exception if full
     */
    @Override
    public void pushLeft(Object o) throws Overflow
    {
        if (isFull())
        {
            throw new Overflow("Cannot pushLeft when full");
        }
        else
        {
            // Decrease left pointer 
            // Go to the last array index when too far left
            left = (left - 1 + array.length) % array.length;

            // Save object into next left position
            array[left] = o;

            // Increase item count
            itemCount++;
        }
    }

    /**
     * Add the object o to the right of the deque. If full, throw an Overflow
     * exception
     *
     * @param o Object to be added
     * @throws Overflow exception if full
     */
    @Override
    public void pushRight(Object o) throws Overflow
    {
        if (isFull())
        {
            throw new Overflow("Cannot pushRight when full");
        }
        else
        {
            // Increment right index
            // If only pushing right, increase up to len-1 and go back to 0
            int rightIndex = (left + itemCount) % array.length;

            // Save the object into the right index
            array[rightIndex] = o;

            // Increase the item count
            itemCount++;
        }
    }

    /**
     * Removes and returns the leftmost item. If empty, throws an Underflow
     * exception
     *
     * @return The leftmost object
     * @throws Underflow when empty
     */
    @Override
    public Object popLeft() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot popLeft when empty");
        }
        else
        {
            // Save thee leftmost object
            Object oldLeft = array[left];

            // Delete the leftmost object (optional)
            array[left] = null;

            // Increase left position
            left = (left + 1) % array.length;

            // Decrease itemCount
            itemCount--;

            // Return old left object
            return oldLeft;
        }
    }

    /**
     * Returns the leftmost element. If empty, throws an Underflow exception
     *
     * @return Underflow exception if empty
     */
    @Override
    public Object peekLeft() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot peekLeft when empty");
        }
        else
        {
            return array[left];
        }
    }

    /**
     * Helper method that finds the rightmost index
     *
     * @return Index of the rightmost position
     */
    private int getRightmost()
    {
        return (left + itemCount - 1) % array.length;
    }

    /**
     * Removes and returns the rightmost item
     *
     * @return Rightmost item
     */
    @Override
    public Object popRight() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot popRight when empty");
        }
        else
        {
            // Get the rightmost index
            int rightmost = getRightmost();

            // Save the rightmost object
            Object oldRight = array[rightmost];

            // Delete the rightmost object (optional)
            array[rightmost] = null;

            // Decrease item count
            itemCount--;

            return oldRight;
        }
    }

    /**
     * Return the rightmost element. If empty, throw an Underflow exception
     *
     * @return The rightmost object
     * @throws Underflow
     */
    @Override
    public Object peekRight() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot peekRight when empty");
        }
        else
        {
            return array[getRightmost()];
        }
    }

    /**
     * Returns string representing state. REMOVE BEFORE SUBMIT
     *
     * @return
     */
    public String getState()
    {
        String state = "";

        // Add elements
        state += "Elements: ";
        for (Object ob : array)
        {
            state += (String) ob + " - ";
        }

        // Add indices
        state += "  ";
        state += "curCount: " + itemCount + " , ";
        state += "Left: " + left;

        return state;
    }
}
