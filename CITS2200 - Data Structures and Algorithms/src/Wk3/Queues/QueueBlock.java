package Wk3.Queues;

import CITS2200.Overflow;
import CITS2200.Queue;
import CITS2200.Underflow;

/**
 * A block implementation of a queue
 *
 * @author David
 */
public class QueueBlock implements Queue
{

    // Stores elements
    private Object[] elements;

    // Window indices
    private int front;
    private int back;

    /**
     * Create an empty queue of size s.
     *
     * @param size
     */
    public QueueBlock(int size)
    {
        elements = new Object[size];

        front = 0;
        back = -1;
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
            back++;
            elements[back] = o;
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
            return elements[front];
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
            throw new Underflow("Empty");
        }
        else
        {
            Object oldFront = elements[front];
            front++;
            return oldFront;
        }
    }

    /**
     * Returns true if empty
     *
     * @return
     */
    @Override
    public boolean isEmpty()
    {
        return (front == back + 1);
    }

    /**
     * Return true if full
     *
     * @return
     */
    public boolean isFull()
    {
        return (back == elements.length - 1);
    }

    public void printIndices()
    {
        System.out.println("Front is " + front);
        System.out.println("Back is " + back);
    }

}
