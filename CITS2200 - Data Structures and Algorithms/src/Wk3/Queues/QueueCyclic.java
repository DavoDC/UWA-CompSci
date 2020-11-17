package Wk3.Queues;

import CITS2200.Overflow;
import CITS2200.Queue;
import CITS2200.Underflow;

/**
 * A cyclic implementation of a queue
 *
 * @author David
 */
public class QueueCyclic implements Queue
{

    // Stores array
    private Object[] array;

    // Window indices
    private int front;
    private int back;

    /**
     * Create an empty queue of size s.
     *
     * @param size
     */
    public QueueCyclic(int size)
    {
        array = new Object[size + 1];

        front = 0;
        back = size;
    }

    /**
     * Returns true if empty
     *
     * @return
     */
    @Override
    public boolean isEmpty()
    {
        return (front == (back + 1) % array.length);
    }

    /**
     * Return true if full
     *
     * @return
     */
    public boolean isFull()
    {
        return (front == (back + 2) % array.length);
    }

    /**
     * Add an element to the back of the queue
     *
     * @param o
     */
    @Override
    public void enqueue(Object o)
    {
        if (isFull())
        {
            throw new Overflow("Cannot enqueue. Full");
        }
        else
        {
            back = (back + 1) % array.length;
            array[back] = o;
        }
    }

    /**
     * Return and remove the front-most element
     *
     * @return
     * @throws Underflow
     */
    @Override
    public Object dequeue() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot dequeue. Empty");
        }
        else
        {
            Object oldFront = array[front];

            front = (front + 1) % array.length;

            return oldFront;
        }
    }

    /**
     * Return the front-most element
     *
     * @return
     * @throws Underflow
     */
    @Override
    public Object examine() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot examine. No elements");
        }
        else
        {
            return array[front];
        }
    }

}
