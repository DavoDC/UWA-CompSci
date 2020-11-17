package Wk3.Stacks;

import CITS2200.Link;
import CITS2200.Overflow;
import CITS2200.Stack;
import CITS2200.Underflow;

/**
 * A recursive/linked implementation of a Stack
 *
 * @author David
 */
public class StackLinked implements Stack
{

    // First link
    private Link first;

    /**
     * List starts with null object
     */
    public StackLinked()
    {
        first = null;
    }

    /**
     * Adds a link using the first one
     *
     * @param o
     * @throws Overflow
     */
    @Override
    public void push(Object o)
    {
        first = new Link(o, first);
    }

    @Override
    public boolean isEmpty()
    {
        return (first == null);
    }

    @Override
    public Object examine() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Nothing to examine");
        }
        else
        {
            return first.item;
        }
    }

    @Override
    public Object pop() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Underflow: Nothing to examine");
        }
        else
        {
            // Get the object of the current link
            Object oldFirstItem = first.item;

            // Remove the first link by replacing it with the previous link
            first = first.successor;

            // Return old first item
            return oldFirstItem;
        }
    }

}
