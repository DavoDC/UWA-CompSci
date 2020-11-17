package Wk6;

import CITS2200.BinaryTree;
import CITS2200.Iterator;
import CITS2200.Link;
import CITS2200.OutOfBounds;
import CITS2200.Queue;
import CITS2200.Underflow;

/**
 * Lab 5 A Binary Tree that additionally implements an iterator and equals
 * method. Traverses using the breadth-first pattern
 *
 * @param <E> the type of data held in the binary tree
 * @author David
 */
public class BinTree<E> extends BinaryTree<E>
{

    /**
     * Make an empty binary tree
     */
    public BinTree()
    {
        super();
    }

    /**
     * Create a binary tree using a node and two trees
     *
     * @param item node
     * @param left left subtree
     * @param right right subtree
     */
    public BinTree(E item, BinaryTree<E> left, BinaryTree<E> right)
    {
        super(item, left, right);
    }

    /**
     * An inner class for an iterator. Traverses starting at the first level,
     * from left to right (breadth-first)
     *
     * @param <E> the type of data held in the binary tree
     */
    private static class BinTreeIterator<E> implements Iterator<E>
    {

        // The internal queue of the iterator
        private final QueueLinked trees;

        /**
         * Creates the iterator for the inputted tree
         *
         * @param root the inputted tree to be iterated over
         */
        public BinTreeIterator(BinTree<E> root)
        {
            // Initalise queue
            trees = new QueueLinked();

            // Add the root to the queue
            enqueueTree(root);
        }

        /**
         * Adds the tree to the queue
         *
         * @param tree the tree to be potentially iterated in the future
         */
        private void enqueueTree(BinaryTree<E> tree)
        {
            // Don't add empty trees
            if (tree.isEmpty())
            {
                return;
            }

            trees.enqueue(tree);
        }

        /**
         * See if there are more items left
         *
         * @return true if there are more objects to iterate. False otherwise
         */
        @Override
        public boolean hasNext()
        {
            return !trees.isEmpty();
        }

        /**
         * Return the next object, traversing breadth first
         *
         * @return the next object
         * @throws OutOfBounds if traversal has reached end of tree (bottom
         * rightmost)
         */
        @Override
        public E next() throws OutOfBounds
        {
            // Prevent processing of empty trees
            if (trees.isEmpty())
            {
                throw new OutOfBounds("Cannot call 'next' on an empty tree");
            }

            // Get and delete the tree copy
            BinaryTree<E> tree = (BinaryTree<E>) trees.dequeue();

            // Save the left and right subtrees in the queue
            enqueueTree(tree.getLeft()); // Left is enqueued first that so it is accessed first
            enqueueTree(tree.getRight());

            // Get the item of the removed node
            return tree.getItem();
        }
    }

    /**
     * A inner class that is a linked queue. Used by the iterator
     *
     * @author David
     */
    private static class QueueLinked implements Queue
    {

        // Ends
        private Link front;
        private Link back;

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
     * Get an iterator for the tree
     *
     * @return an instance of the iterator
     */
    @Override
    public Iterator<E> iterator()
    {
        // Return an instance of the BinTreeIterator inner class
        return new BinTreeIterator<>(this);
    }

    /**
     * Check if the inputted tree is equal to this tree. Two trees are defined
     * as equal when they have the same structure and items
     *
     * @param object the inputted tree
     * @return true if the trees are equal
     */
    @Override
    public boolean equals(Object object)
    {
        // If the object is not a binary tree, the comparison cannot occur
        if (!(object instanceof BinaryTree))
        {
            return false;
        }

        // Cast the input as a binary tree
        BinaryTree other = (BinaryTree) object;

        // Empty cases
        if (isEmpty() && other.isEmpty())
        {
            // If both trees are empty, they are equal
            return true;
        }
        if (isEmpty() || other.isEmpty())
        {
            // If only one tree is empty, they aren't equal 
            return false;
        }

        // Filled trees must have the same items and structure
        //  Equate first node 
        boolean nodeEqual = getItem().equals(other.getItem());
        //  Equate subtrees recursively
        boolean leftEqual = getLeft().equals(other.getLeft());
        boolean rightEqual = getRight().equals(other.getRight());

        // Check all tests produced true
        boolean equalTrees = nodeEqual && leftEqual && rightEqual;

        // Return final result
        return equalTrees;

    }
}
