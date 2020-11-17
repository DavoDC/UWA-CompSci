package Wk8;

import CITS2200.IllegalValue;
import CITS2200.Iterator;
import CITS2200.Link;
import CITS2200.PriorityQueue;
import CITS2200.Queue;
import CITS2200.Underflow;

/**
 * Lab 7. A linked implementation of a priority queue
 *
 * @author David
 */
public class PriorityQueueLinked implements PriorityQueue
{

    /**
     * A marker link that holds the front element (highest priority)
     */
    private PriorityLink<Object> front;

    /**
     * Create a new priority queue
     */
    public PriorityQueueLinked()
    {
        this.front = null;
    }

    /**
     * Check if the priority queue is empty
     *
     * @return true if the queue is empty, and false otherwise
     */
    @Override
    public boolean isEmpty()
    {
        return front == null;
    }

    /**
     * Enqueue an object with a priority
     *
     * @param ob The object
     * @param p The priority value
     * @throws IllegalValue
     */
    @Override
    public void enqueue(Object ob, int p) throws IllegalValue
    {
        // Reject negative priorities
        if (p < 0)
        {
            throw new IllegalValue("Priorities cannot be negative");
        }

        // Add the object to the front if:
        // - the queue is empty 
        // - the inputted priority is greater than the highest one
        if (isEmpty() || p > front.priority)
        {
            // Make the new element point to the font
            front = new PriorityLink<>(ob, front, p);
            return;
        }

        // Create a temporary marker starting at the front (left)
        PriorityLink<Object> link = front;

        // While:
        // - not at the end of the queue
        // - the inputted priority is less than or equal to the next priority
        while (link.next != null && p <= link.next.priority)
        {
            // Move the marker right
            link = link.next;
        }

        // Add the link after the marker
        link.next = new PriorityLink<>(ob, link.next, p);
    }

    /**
     * Inspect the front element
     *
     * @return The object of the front-most element
     * @throws Underflow when empty
     */
    @Override
    public Object examine() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot examine when empty");
        }

        return front.value;
    }

    /**
     * Dequeue the front object (highest priority)
     *
     * @return The front-most object's value
     * @throws Underflow when empty
     */
    @Override
    public Object dequeue() throws Underflow
    {
        if (isEmpty())
        {
            throw new Underflow("Cannot dequeue when empty");
        }

        // Save the front-most object's value
        Object value = front.value;

        // Move the front marker right
        front = front.next;

        // Return the old front-most value
        return value;
    }

    /**
     * An inner class implementing a link with priority. Note: private -> public
     * to prevent false security issue positives
     */
    public static class PriorityLink<Object>
    {

        /**
         * The link's value
         */
        public final Object value;

        /**
         * The next link that this link points to
         */
        public PriorityLink<Object> next;

        /**
         * The link's priority
         */
        public final int priority;

        /**
         * Create a link
         *
         * @param value The link's value
         * @param next The next link
         * @param priority The priority of this link
         */
        public PriorityLink(Object value, PriorityLink<Object> next, int priority)
        {
            this.value = value;
            this.next = next;
            this.priority = priority;
        }
    }

    /**
     * Create an iterator for the queue
     *
     * @return An iterator object for this queue
     */
    @Override
    public Iterator iterator()
    {
        return new PriorityQueueLinkedIterator<>(this);
    }

    /**
     * An inner class that implements a iterator for the priority queue. Note:
     * private -> public to prevent false security issue positives
     *
     * @param <E> The data type of the queue
     */
    public static class PriorityQueueLinkedIterator<E> implements Iterator<E>
    {

        /**
         * A copy of the queue to be iterated
         */
        public QueueLinked queue;

        /**
         * Create an iterator for the inputted queue
         *
         * @param queue
         */
        public PriorityQueueLinkedIterator(PriorityQueueLinked queue)
        {
            // Initialize the queue
            this.queue = new QueueLinked();

            // Populate the iterator queue with the actual queue elements
            //  Create a temporary marker at the left
            PriorityLink link = queue.front;
            //  While there is still a link remaining
            while (link != null)
            {
                // Enqueue the current link's value
                this.queue.enqueue(link.value);

                // Move the marker right
                link = link.next;
            }
        }

        /**
         * Get the next element
         *
         * @return true when iterator is not empty, false otherwise
         */
        @Override
        public E next()
        {
            return (E) queue.dequeue();
        }

        /**
         * Check if a next item exists
         *
         * @return true when there is a next item, false otherwise
         */
        @Override
        public boolean hasNext()
        {
            return !queue.isEmpty();
        }
    }

    /**
     * An inner class that is a linked implementation of a queue. Note: private
     * -> public to prevent false security issue positives
     *
     * @author David
     */
    public static class QueueLinked implements Queue
    {

        // Ends
        public Link front;
        public Link back;

        /**
         * Create a queue
         */
        public QueueLinked()
        {
            front = null;
            back = null;
        }

        /**
         * Add object o to the queue
         *
         * @param o
         */
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

        /**
         * Return the current front item
         *
         * @return
         * @throws Underflow
         */
        @Override
        public Object examine() throws Underflow
        {
            return front.item;
        }

        /**
         * Return and remove the item at the front
         *
         * @return
         * @throws Underflow
         */
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

        /**
         * Return true if the queue is empty
         *
         * @return
         */
        @Override
        public boolean isEmpty()
        {
            return (front == null);
        }

    }

    /**
     * Get a string representing the priority queue's state
     *
     * @return the state string
     */
    @Override
    public String toString()
    {
        String state = "[ ";

        Iterator it = this.iterator();

        while (it.hasNext())
        {
            state += (String) it.next();
            state += ", ";
        }

        state += " ]";

        return state;
    }
}
