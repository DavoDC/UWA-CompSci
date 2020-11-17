package Wk3.Stacks;

import CITS2200.Overflow;
import CITS2200.Stack;
import CITS2200.Underflow;

/**
 * Lab 2 Models a stack that uses a block implementation
 *
 * @author David
 */
public class StackBlock implements Stack
{

    /**
     * Holds the stacked elements in a block
     */
    private Object[] elements;

    /**
     * Current index being used
     */
    private int topIndex;

    /**
     * Creates a StackBlock of size s
     *
     * @param s
     */
    public StackBlock(int s)
    {
        elements = new Object[s];
        topIndex = -1;
    }

    /**
     * Return true iff the stack is empty. False otherwise
     *
     * @return
     */
    @Override
    public boolean isEmpty()
    {
        return (topIndex == -1);
    }

    /**
     * Return true iff the stack is full. False otherwise
     *
     * @return
     */
    public boolean isFull()
    {
        return (topIndex == elements.length - 1);
    }

    /**
     * Push Object o onto the top of the stack, or throw an Overflow exception
     * if the stack is full
     *
     * @param o
     * @throws Overflow
     */
    @Override
    public void push(Object o) throws Overflow
    {
        if (isFull())
        {
            throw new Overflow("Cannot push, stack is full");
        }
        else
        {
            topIndex++;
            elements[topIndex] = o;
        }
    }

    /**
     * Return the topmost Object of the stack, or throw an Underflow exception
     * if the stack is empty
     *
     * @return
     * @throws Underflow
     */
    @Override
    public Object examine() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot examine, stack is empty");
        }
        else
        {
            return elements[topIndex];
        }
    }

    /**
     * Return and remove the topmost Object of the stack, or throw an Underflow
     * exception if the stack is empty
     *
     * @return
     * @throws Underflow
     */
    @Override
    public Object pop() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot pop, stack is empty");
        }
        else
        {
            // Get the top item
            Object oldTop = elements[topIndex];

            // Move topIndex back (so it gets overwritten)
            topIndex--;

            // Return old top item
            return oldTop;
        }
    }

}
