package Wk4.Deques;

import CITS2200.Link;
import CITS2200.Underflow;

public class DequeLinked
{

    Link first;
    Link last;

    public DequeLinked()
    {
        first = new Link(null, last);
        last = new Link(null, first);
    }

    /**
     * Checks to see if Dequeue is empty
     *
     * @return boolean check true if dequeue is empty
     */
    boolean isEmpty()
    {
        //return first.successor.equals(last);
        return first.item == null && last.item == null;
    }

    /**
     * @return last item
     * @throws Underflow thrown if empty
     */
    Object peekLast() throws Underflow
    {
        if (!isEmpty())
        {
            return last.item;
        }
        else
        {
            throw new Underflow("Dequeue is empty!");
        }
    }

    /**
     * @return first item
     * @throws Underflow thrown if empty
     */
    Object peekFirst() throws Underflow
    {
        if (!isEmpty())
        {
            return first.item;
        }
        else
        {
            throw new Underflow("Dequeue is empty!");
        }
    }

    /**
     * Checks to see if the last item is still default if so, replaces null with
     * e. Else new Last object is inserted between oldLast and first.
     *
     * @param e
     */
    void addLast(Object e)
    {
        if (last.item == null)
        {
            last.item = e;
        }
        else
        {
            Link oldLast = last;
            last = new Link(e, first);
            oldLast.successor = last;
        }
    }

    /**
     * Checks to see if first is empty, if so inserts e. Else places new link as
     * first ahead of the previous first.
     *
     * @param e
     */
    void addFirst(Object e)
    {
        if (first.item == null)
        {
            first.item = e;
        }
        else
        {
            Link newSecond = first;
            first = new Link(e, newSecond);
            last.successor = first;
        }
    }

    /**
     * Returns the current first item. Assigns the next link to be first. Fixes
     * the link from last.
     *
     * @return Underflow if no first element else returns first item
     */
    Object popFirst() throws Underflow
    {
        if (first.item != null)
        {
            Object oldFirst = first.item;
            first.item = first.successor.item;
            last.successor = first.successor.successor;
            return oldFirst;
        }
        else
        {
            throw new Underflow("No first element.");
        }
    }

    Object popLast() throws Underflow
    {
        if (last.item != null)
        {
            Object oldLast = last.item;
            Link current = first.successor;
            Link previous = first;
            while (current != last)
            {
                previous = current;
                current = current.successor;
            }
            last = previous;
            previous.successor = first;
            return oldLast;
        }
        else
        {
            throw new Underflow("No last element.");
        }
    }

}
