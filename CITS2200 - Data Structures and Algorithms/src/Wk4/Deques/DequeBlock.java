package Wk4.Deques;

import CITS2200.Deque;
import CITS2200.Overflow;
import CITS2200.Underflow;

/**
 * A doubled ended queue implemented using an array block UNFINISHED
 *
 * @author David
 */
public class DequeBlock implements Deque
{

    public Object[] array;

    private int left;

    private int itemCount;

    public DequeBlock(int s)
    {
        array = new Object[s];

        left = array.length;
        itemCount = 0;
    }

    @Override
    public boolean isEmpty()
    {
        return (itemCount == 0);
    }

    @Override
    public boolean isFull()
    {
        return (itemCount == array.length);
    }

    /**
     * Add an object from the left (goes in len-1 first)
     *
     * @param o
     * @throws Overflow
     */
    @Override
    public void pushLeft(Object o) throws Overflow
    {
        if (isFull())
        {
            throw new Overflow("Cannot pushLeft when empty");
        }
        else
        {
            // Move pointer left
            left--;

            // Save object into left
            array[left] = o;

            // Increase item count
            itemCount++;
        }

    }

    /**
     * Helper method that gets the leftmost index
     */
    private int getLeftmost()
    {
        int leftmost = 0;

        if (isFull())  // or at least pushRight has occurred
        {
            leftmost = 0;
        }
        else if (false) // if only pushlefts have occurred
        {
            // return left
            leftmost = left;
        }

        // Should never be reached
        return leftmost;
    }

    /**
     * Remove and return the leftmost object
     *
     * @return
     * @throws Underflow
     */
    @Override
    public Object popLeft() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot XXX when empty");
        }
        else
        {
            // Save left object
            Object oldLeft = array[left];

            // Delete object (optional)
            array[left] = null;

            // Move pointer right
            left++;

            // Decrease itemCount
            itemCount--;

            // Return old left object
            return oldLeft;
        }

    }

    @Override
    public Object peekLeft() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot XXX when empty");
        }
        else
        {
        }

        return null;
    }

    @Override
    public void pushRight(Object e) throws Overflow
    {
        if (isFull())
        {
            throw new Overflow("Cannot XXX when empty");
        }
        else
        {

        }
    }

    @Override
    public Object popRight() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot XXX when empty");
        }
        else
        {
        }

        return null;
    }

    @Override
    public Object peekRight() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot XXX when empty");
        }
        else
        {
        }

        return null;
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
