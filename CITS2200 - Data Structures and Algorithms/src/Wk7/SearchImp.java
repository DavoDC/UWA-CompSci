package Wk7;

import CITS2200.Graph;
import CITS2200.Link;
import CITS2200.Queue;
import CITS2200.Search;
import CITS2200.Underflow;

/**
 * Lab 6. A class that can traverse and analyze graphs, using both depth and
 * breadth first traversal methods
 *
 * @author David
 */
public class SearchImp implements Search
{

    /**
     * A constant representing a node that hasn't been visited
     */
    private static final int WHITE = 0;

    /**
     * A constant representing a node that is unprocessed
     */
    private static final int GREY = 1;

    /**
     * A constant representing a fully processed node
     */
    private static final int BLACK = 2;

    /**
     * The colors of the vertices
     */
    private int[] colors;

    /**
     * Stores the start and finish times of each vertex
     */
    private int[][] times;

    /**
     * Stores the time temporarily
     */
    private int tempTime;

    /**
     * Set a color and parent array to default values
     *
     * @param cols
     * @param parents
     */
    private void fillArray(int[] array, int len, int def)
    {
        for (int index = 0; index < len; ++index)
        {
            array[index] = def;
        }
    }

    /**
     * Search the graph breadth-first, starting at the root. Outputs an array
     * specifying the parent vertex for each vertex. If there is no parent, -1
     * is inputted
     *
     * @param graph the graph to be traversed
     * @param root the starting vertex
     * @return
     */
    @Override
    public int[] getConnectedTree(Graph graph, int root)
    {
        // Setup needed local variables
        //  The edge connections
        int[][] edges = graph.getEdgeMatrix();
        //  The colours
        int[] cols = new int[graph.getNumberOfVertices()];
        //  The parents
        int[] parents = new int[graph.getNumberOfVertices()];

        // Set colour and parent array to default values
        int len = graph.getNumberOfVertices();
        fillArray(cols, len, WHITE); // Fill colour array with white
        fillArray(parents, len, -1); // Fill parents with -1

        // Make a queue
        QueueLinked queue = new QueueLinked();

        // Set the root to grey and enqueue it
        cols[root] = GREY;
        queue.enqueue(root);

        // While the queue has something
        while (!queue.isEmpty())
        {
            // Dequeue and save the current vertex
            int vertex = (Integer) queue.dequeue();

            // Get one edge
            int[] edge = edges[vertex];

            // For every vertex
            for (int toVertex = 0; toVertex < edges.length; ++toVertex)
            {
                // Do not process further if there are no connections
                if (edge[toVertex] == 0)
                {
                    continue;
                }

                // If white, process the vertex partially
                if (cols[toVertex] == WHITE)
                {
                    // Change processing state
                    cols[toVertex] = GREY;

                    // Set parent
                    parents[toVertex] = vertex;

                    // Enqueue adjacent
                    queue.enqueue(toVertex);
                }
            }

            // Set vertex as processed fully
            cols[vertex] = BLACK;
        }

        // Return final array
        return parents;
    }

    /**
     * Generate an array representing edge distances, starting from the
     * specified root vertex
     *
     * @param root of the graph
     * @param graph
     * @return the distance array
     */
    @Override
    public int[] getDistances(Graph graph, int root)
    {
        // Setup needed local variables
        //  The edge connections
        int[][] edges = graph.getEdgeMatrix();
        //  The colours
        int[] cols = new int[graph.getNumberOfVertices()];
        //  The distances
        int[] distances = new int[graph.getNumberOfVertices()];

        // Set colour and parent array to default values
        int len = graph.getNumberOfVertices();
        fillArray(cols, len, WHITE); // Fill colour array with white
        fillArray(distances, len, -1); // Fill distances with -1

        // Create a queue
        QueueLinked queue = new QueueLinked();

        // Set the root to grey and its distance to 0
        cols[root] = GREY;
        distances[root] = 0;

        // Enqueue root
        queue.enqueue(root);

        // While the queue is not empty
        while (!queue.isEmpty())
        {
            // Dequeue current vertex
            int vertex = (Integer) queue.dequeue();

            int[] edge = edges[vertex];

            for (int toVertex = 0; toVertex < edges.length; ++toVertex)
            {
                // Don't process further when not connected
                if (edge[toVertex] == 0)
                {
                    continue;
                }

                // If vertex is white, process
                if (cols[toVertex] == WHITE)
                {
                    // Change state
                    cols[toVertex] = GREY;

                    // Set the distance
                    distances[toVertex] = distances[vertex] + 1;

                    // Enqueue adjacent
                    queue.enqueue(toVertex);
                }
            }

            cols[vertex] = BLACK;
        }

        // Return the distance array
        return distances;
    }

    /**
     * Search using the depth-first method from the root vertex Given that the
     * graph G has n vertices, the method getTimes(G,S) Should return an n × 2
     * array of integers. For example. if a = getTimes(G,S) then the start time
     * for vertex i is a[i][0] and the end time for vertex i is a[i][1].
     *
     * @param root of the graph
     * @param graph
     * @return a array of start time - finish time pairs
     */
    @Override
    public int[][] getTimes(Graph graph, int root)
    {
        // Initialize empty arrays
        this.colors = new int[graph.getNumberOfVertices()];
        this.times = new int[graph.getNumberOfVertices()][2];

        // Initialize the temporary time
        this.tempTime = 0;

        // Recursively determine the times
        getTimesDriver(graph.getEdgeMatrix(), root);

        return times;
    }

    /**
     * Recursive helper method that uses a DFS to find the times of a vertex
     *
     * @param edges the matrix of edges in the graph
     * @param vertex the next vertex
     */
    private void getTimesDriver(int[][] edgesIn, int vertex)
    {
        // Set vertex to grey
        colors[vertex] = GREY;

        // Add to time array
        times[vertex][0] = tempTime;

        // Increase time count
        tempTime += 1;

        int[] edges = edgesIn[vertex];
        for (int toVertex = 0; toVertex < edges.length; ++toVertex)
        {
            // Don't process edge with no connections
            if (edges[toVertex] == 0)
            {
                continue;
            }

            // If white, process and get time
            if (colors[toVertex] == WHITE)
            {
                getTimesDriver(edgesIn, toVertex);
            }
        }

        // Set as processed
        colors[vertex] = BLACK;

        // Save time
        times[vertex][1] = tempTime;

        // Increase time
        tempTime++;
    }

    /**
     * An inner class that is a linked implementation of a queue
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

}
