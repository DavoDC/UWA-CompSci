package Wk9;

import CITS2200.Graph;
import CITS2200.IllegalValue;
import CITS2200.Overflow;
import CITS2200.Path;
import CITS2200.Underflow;

/**
 * Lab 8 - Implementations to solve the MST problem
 *
 * @author David
 */
public class PathImp implements Path
{

    /**
     * Uses Prim's algorithm to return the weight of a MST of a graph. Otherwise return -1
     *
     * @param graph the original graph
     * @return the weight of the graph
     */
    @Override
    public int getMinSpanningTree(Graph graph)
    {
        // Prevent 0 vertex graphs from being processed
        if (graph.getNumberOfVertices() == 0)
        {
            return -1;
        }

        // Local variables for calculation
        //  The graph's edge matrix 
        int[][] matrix = graph.getEdgeMatrix();
        //  A boolean array of processed vertices
        boolean[] processed = new boolean[graph.getNumberOfVertices()];
        //  The weights of edges
        int[] weights = new int[graph.getNumberOfVertices()];
        //  The total weight
        int totalWeight = 0;
        //  A heap of the vertices 
        RandomAccessMinHeap heap = new RandomAccessMinHeap(graph.getNumberOfVertices());

        // Populate heap and set defaults
        for (int vertex = 0; vertex < graph.getNumberOfVertices(); ++vertex)
        {
            heap.enqueue(vertex, Integer.MAX_VALUE);
            weights[vertex] = -1;
        }

        // Relax the 0th node
        heap.relax(0, 0);
        weights[0] = 0;

        // Keep repeating unitl MST is found
        while (!heap.isEmpty())
        {
            int vertex = heap.dequeue();

            // We have hit a vertex that was not reached from vertex 0
            if (weights[vertex] == -1)
            {
                return -1;
            }

            // Mark that we have added this vertex to the minimum spanning tree
            processed[vertex] = true;
            totalWeight += weights[vertex];

            // Loop through all the edges from vertex
            int[] edges = matrix[vertex];
            for (int toVertex = 0; toVertex < edges.length; ++toVertex)
            {
                int weight = edges[toVertex];

                // Skip:
                // - edges to self
                // - edges that do not exist
                // - edges to already processed vertices
                if (vertex == toVertex || weight == 0 || processed[toVertex])
                {
                    continue;
                }

                // Skip edges that will not lower the weight
                if (weights[toVertex] != -1 && weight > weights[toVertex])
                {
                    continue;
                }

                // Relax the next node
                weights[toVertex] = weight;
                heap.relax(toVertex, weight);
            }
        }

        return totalWeight;
    }

    /**
     * Use Djikstra's algorithm to find the lengths of the shortest paths for a
     * given vertex
     *
     * @param graph the graph to examine
     * @param root the vertex to use
     * @return an array of the paths
     */
    @Override
    public int[] getShortestPaths(Graph graph, int root)
    {
        // Used to iterate the edges each vertex is connected to
        int[][] matrix = graph.getEdgeMatrix();

        // Intermediate variables to calculate the result
        boolean[] processed = new boolean[graph.getNumberOfVertices()];
        int[] distances = new int[graph.getNumberOfVertices()];

        // A heap to store the next vertices to be processed
        RandomAccessMinHeap heap = new RandomAccessMinHeap(graph.getNumberOfVertices());

        // Initialise the heap with all the vertexes
        for (int vertex = 0; vertex < graph.getNumberOfVertices(); ++vertex)
        {
            heap.enqueue(vertex, Integer.MAX_VALUE);
            distances[vertex] = -1;
        }

        // Relax the root node for it to be processed
        heap.relax(root, 0);
        distances[root] = 0;

        // Loop until all shortest paths are found
        while (!heap.isEmpty())
        {
            int vertex = heap.dequeue();
            int distance = distances[vertex];

            // Mark that we have found the shortest path to this vertex
            processed[vertex] = true;

            // Loop through all the edges from vertex
            int[] edges = matrix[vertex];
            for (int toVertex = 0; toVertex < edges.length; ++toVertex)
            {
                int edgeWeight = edges[toVertex];

                // Skip edges to self, edges that do not exist or edges to already processed vertices
                if (vertex == toVertex || edgeWeight == 0 || processed[toVertex])
                {
                    continue;
                }

                // The distance through the vertex we are processing to this vertex
                int weight = edgeWeight + distance;

                // Skip edges that don't reduce the distances
                if (distances[toVertex] >= 0 && weight > distances[toVertex])
                {
                    continue;
                }

                // Update the distance to the vertex and relax its weight in the heap
                distances[toVertex] = weight;
                heap.relax(toVertex, weight);
            }
        }

        return distances;
    }

    public static class RandomAccessMinHeap
    {

        // Data fields
        private final int[] values;
        private final int[] weights;
        private final int[] valueIndexes;
        private int length;

        /**
         * Create a new heap of a given size
         *
         * @param size the heap's size
         */
        public RandomAccessMinHeap(int size)
        {
            // Initialize fields
            this.values = new int[size];
            this.weights = new int[size];
            this.valueIndexes = new int[size];
            this.length = 0;

            // Set defaults
            for (int index = 0; index < size; ++index)
            {
                values[index] = -1;
                weights[index] = -1;
                valueIndexes[index] = -1;
            }
        }

        /**
         * Check if the heap is empty
         *
         * @return true if empty. False otherwise
         */
        public boolean isEmpty()
        {
            return length <= 0;
        }

        /**
         * Check if the heap is full
         *
         * @return true if Full. False otherwise
         */
        public boolean isFull()
        {
            return length >= weights.length;
        }

        /**
         * Relax the weight of certain value, if the input is less than the
         * current value
         *
         * @param value for relaxing
         * @param weight the inputted new weight
         */
        public void relax(int value, int weight)
        {
            // Check if value exists
            if (value < 0 || value >= values.length || valueIndexes[value] == -1)
            {
                throw new IllegalValue("'" + value + "' is non-existent value");
            }

            // Retrieve index
            int index = valueIndexes[value];

            // Don't process
            if (weights[index] <= weight)
            {
                return;
            }

            // Replace weight
            weights[index] = weight;

            // Fix heap
            do
            {
                int parent = getParent(index);

                if (parent == -1 || weight >= weights[parent])
                {
                    break;
                }

                swap(index, parent);

                index = parent;
            }
            while (true);

            heapify(index);
        }

        /**
         * Enqueue a value with a certain weight
         *
         * @param value the new element's value
         * @param weight the new element's weight
         *
         * @throws Overflow when full
         * @throws IllegalValue if value goes beyond size
         */
        public void enqueue(int value, int weight) throws Overflow, IllegalValue
        {
            // Reject when full
            if (isFull())
            {
                throw new Overflow("Cannot Enqueue when full");
            }

            // Check for illegal values
            if (value < 0 || value >= values.length)
            {
                throw new IllegalValue("Value is negative or greater than size");
            }

            int index = length;
            length += 1;

            do
            {
                int parent = getParent(index);

                if (parent == -1 || weight >= weights[parent])
                {
                    break;
                }

                swap(index, parent);

                index = parent;
            }
            while (true);

            weights[index] = weight;
            values[index] = value;
            valueIndexes[value] = index;
        }

        /**
         * Get the heaviest value in the heap
         *
         * @return the greatest weight value
         */
        public int examine()
        {
            if (isEmpty())
            {
                throw new Underflow("Cannot examine when empty");
            }

            return values[0];
        }

        /**
         * Dequeue the heaviest element
         *
         * @return the value removed
         */
        public int dequeue()
        {
            if (isEmpty())
            {
                throw new Underflow("Cannot dequeue when empty");
            }

            // Set to defaults
            int value = values[0];
            length -= 1;

            values[0] = -1;
            weights[0] = -1;
            valueIndexes[value] = -1;

            // Fix heap
            swap(0, length);

            heapify();

            return value;
        }

        /**
         * Correct the position of the root element
         */
        private void heapify()
        {
            heapify(0);
        }

        /**
         * Correct the position of a value at a given index
         *
         * @param index the index of the value to be fixed
         */
        private void heapify(int index)
        {
            int weight = weights[index];

            do
            {
                int leftIndex = getLeftChild(index);
                int rightIndex = getRightChild(index);

                int swapIndex = -1;
                int swapWeight = weight;

                if (leftIndex >= 0 && swapWeight > weights[leftIndex])
                {
                    swapIndex = leftIndex;
                    swapWeight = weights[leftIndex];
                }

                if (rightIndex >= 0 && swapWeight > weights[rightIndex])
                {
                    swapIndex = rightIndex;
                }

                if (swapIndex == -1)
                {
                    break;
                }

                swap(index, swapIndex);
                index = swapIndex;
            }
            while (true);
        }

        /**
         * Exchange the values at different indices
         *
         * @param indexA the first element's index
         * @param indexB the second element's index
         */
        private void swap(int indexA, int indexB)
        {
            int valueA = values[indexA];
            int valueB = values[indexB];
            int weightA = weights[indexA];
            int weightB = weights[indexB];

            values[indexA] = valueB;
            values[indexB] = valueA;

            weights[indexA] = weightB;
            weights[indexB] = weightA;

            // Update index references
            if (valueA != -1)
            {
                valueIndexes[valueA] = indexB;
            }
            if (valueB != -1)
            {
                valueIndexes[valueB] = indexA;
            }
        }

        /**
         * Get the left child index of a given index. Otherwise return -1
         *
         * @param index of element to examine
         * @return the left child index
         */
        private int getLeftChild(int index)
        {
            int leftIndex = index * 2 + 1;

            return (leftIndex < length ? leftIndex : -1);
        }

        /**
         * Get the right child index of a given index. Otherwise return -1
         *
         * @param index of element to examine
         * @return the right child index
         */
        private int getRightChild(int index)
        {
            int rightIndex = index * 2 + 2;

            return (rightIndex < length ? rightIndex : -1);
        }

        /**
         * Get the parent index of a given index. Otherwise return -1
         *
         * @param index of element to examine
         * @return the parent index
         */
        private int getParent(int index)
        {
            return (index + 1) / 2 - 1;
        }

        /**
         * Get a string representation of the heap
         *
         * @return the state string
         */
        @Override
        public String toString()
        {
            String output = "";

            int level = 1;

            while ((1 << level) >> 1 < values.length)
            {
                int to = (1 << level) - 1;
                int from = to / 2;

                for (int index = from; index < to; ++index)
                {
                    if (index != from)
                    {
                        output += (", ");
                    }

                    output += (index < length ? values[index] : "_");
                }

                output += '\n';

                level += 1;
            }

            return output;
        }

    }

}
