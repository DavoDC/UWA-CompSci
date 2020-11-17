package Wk3.Queues;

import CITS2200.Link;
import CITS2200.Queue;
import CITS2200.Underflow;

/**
 * A recursive or linked implementation of a queue
 *
 * @author David
 */
public class QueueLinked implements Queue
{

    // Ends
    private Link front;
    private Link back;

    public QueueLinked()
    {
        front = null;
        back = null;
    }

    @Override
    public void enqueue(Object o)
    {
        if (isEmpty())
        {
            // For first element, make a new link and set as both
            front = new Link(o, null);
            back = front;
        }
        else
        {
            // Add a new link to the next pointer
            back.successor = new Link(o, null);

            // Assign next pointer as current
            back = back.successor;
        }
    }

    @Override
    public Object examine() throws Underflow
    {
        return front.item;
    }

    @Override
    public Object dequeue() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot dequeue when empty");
        }
        else
        {
            // Get old item
            Object oldFront = front.item;

            // Move front pointer back 
            front = front.successor;

            // If this was the last element, make back null
            if (isEmpty())
            {
                back = null;
            }

            // Return old item
            return oldFront;
        }

    }

    @Override
    public boolean isEmpty()
    {
        return (front == null);
    }

}
