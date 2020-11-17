package Wk5.Lists;

import CITS2200.Link;
import CITS2200.List;
import CITS2200.OutOfBounds;
import CITS2200.WindowLinked;

/**
 * Lab 4 A singly linked implementation of a list
 *
 * @author David
 */
public class ListLinked implements List
{

    /**
     * Sentinel cell that represents the beforeFirst position
     */
    private Link before;

    /**
     * Sentinel cell that represents the afterLast position
     */
    private Link after;

    /**
     * Create a ListLinked
     */
    public ListLinked()
    {
        after = new Link(null, null);
        before = new Link(null, after);
    }

    /**
     * Check if the list is empty
     *
     * @return True if empty, false otherwise
     */
    @Override
    public boolean isEmpty()
    {
        // If the "next" of before is after = Empty
        return (before.successor == after);
    }

    /**
     * Check if the window is over the before first position
     *
     * @param wl
     * @return True if the window over before first
     */
    @Override
    public boolean isBeforeFirst(WindowLinked wl)
    {
        // If window equals before cell
        return (wl.link == before);
    }

    /**
     * Check if the window is over the after last position
     *
     * @param wl
     * @return True if the window over after last
     */
    @Override
    public boolean isAfterLast(WindowLinked wl)
    {
        // If window equals after cell
        return (wl.link == after);
    }

    /**
     * Initialize the window at the before first position
     *
     * @param wl
     */
    @Override
    public void beforeFirst(WindowLinked wl)
    {
        // Make window equal to before marker
        wl.link = before;
    }

    /**
     * Initialize the window at the after last position
     *
     * @param wl
     */
    @Override
    public void afterLast(WindowLinked wl)
    {
        // Make window equal to after marker
        wl.link = after;
    }

    /**
     * Move the window to next position
     *
     * @param wl
     * @throws OutOfBounds if the window is over the after last position
     */
    @Override
    public void next(WindowLinked wl) throws OutOfBounds
    {
        if (isAfterLast(wl))
        {
            throw new OutOfBounds("Cannot do 'next' when at end");
        }
        else
        {
            // Make the next link the current link
            wl.link = wl.link.successor;
        }
    }

    /**
     * Move the window to previous position
     *
     * @param wl
     * @throws OutOfBounds if the window is over the before first position
     */
    @Override
    public void previous(WindowLinked wl) throws OutOfBounds
    {
        if (isBeforeFirst(wl))
        {
            throw new OutOfBounds("Cannot do 'previous' when at start");
        }
        else
        {
            // Start at left
            Link current = before;

            // While the window is not at pos = cur + 1
            while (current.successor != wl.link)
            {
                // The current marker is moved forward
                current = current.successor;
            }

            // When the window equal to next, make the window current
            // If window is on "1" , it will become "0" = correct
            wl.link = current;
        }

    }

    /**
     * Add the object o to the list after the window
     *
     * @param o
     * @param wl
     * @throws OutOfBounds if the window is over the after last position
     */
    @Override
    public void insertAfter(Object o, WindowLinked wl) throws OutOfBounds
    {
        if (isAfterLast(wl))
        {
            throw new OutOfBounds("Cannot insertAfter when at end");
        }
        else
        {
            // Make the next link equal to a link that points to the next
            wl.link.successor = new Link(o, wl.link.successor);
        }
    }

    /**
     * Add the object o to the list before the window
     *
     * @param o
     * @param wl
     * @throws OutOfBounds if the window is over the before first position
     */
    @Override
    public void insertBefore(Object o, WindowLinked wl) throws OutOfBounds
    {
        if (isBeforeFirst(wl))
        {
            throw new OutOfBounds("Cannot insertBefore when at start");
        }
        else
        {

            // Make current item point to a copy of itself
            wl.link.successor = new Link(wl.link.item, wl.link.successor);

            // When window is at the end, move afterLast marker
            if (isAfterLast(wl))
            {
                after = wl.link.successor;
            }

            // Store object in original link 
            wl.link.item = o;

            // Move window forward (next becomes current)
            wl.link = wl.link.successor;
        }
    }

    /**
     * Retrieve the element under the window
     *
     * @param wl
     * @return the element under the window
     * @throws OutOfBounds if the window is before first or after last position
     */
    @Override
    public Object examine(WindowLinked wl) throws OutOfBounds
    {
        if (isAfterLast(wl) || isBeforeFirst(wl))
        {
            throw new OutOfBounds("Cannot examine object at before/after marker");
        }
        else
        {
            return wl.link.item;
        }
    }

    /**
     * Replace the element under the window with Object o, and return the old
     * element
     *
     * @param o
     * @param wl
     * @return the old element under the window
     * @throws OutOfBounds if the window is before first or after last position
     */
    @Override
    public Object replace(Object o, WindowLinked wl) throws OutOfBounds
    {
        if (isAfterLast(wl) || isBeforeFirst(wl))
        {
            throw new OutOfBounds("Cannot save object into before/after marker");
        }
        else
        {
            // Save the old object under the window
            Object oldWinOb = wl.link.item;

            // Replace the old object with the new one
            wl.link.item = o;

            // Return the old window object
            return oldWinOb;
        }
    }

    /**
     * Deletes and returns the element under w, and shift the window to the next
     * element
     *
     * @param wl
     * @return
     * @throws OutOfBounds if the window is in the before first or after last
     * position
     */
    @Override
    public Object delete(WindowLinked wl) throws OutOfBounds
    {
        if (isAfterLast(wl) || isBeforeFirst(wl))
        {
            throw new OutOfBounds("Cannot save object into before/after marker");
        }
        else
        {
            // Save the old object under the window
            Object oldWinOb = wl.link.item;

            // If last is to the right of the window
            if (after == wl.link.successor)
            {
                // Move after back
                after = wl.link;
            }

            // Save next item into current item
            wl.link.item = wl.link.successor.item;

            // Make next item skip one link
            wl.link.successor = wl.link.successor.successor;

            // Return the object that used be under the window
            return oldWinOb;
        }

    }

}
