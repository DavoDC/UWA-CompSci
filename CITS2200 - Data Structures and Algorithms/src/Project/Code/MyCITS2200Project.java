package Project.Code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A class that implements several graph algorithms
 *
 * @author David, Nisarg
 */
public class MyCITS2200Project implements CITS2200Project
{

    /**
     * Acts as a container for relevant graph information
     */
    private GraphList graphL;

    /**
     * Used to record status of Hamiltonian path processing
     */
    private boolean check;

    /**
     * Create an instance of the project
     */
    public MyCITS2200Project()
    {
        // Initialize the graph container
        graphL = new GraphList();
    }

    /**
     * Adds an edge to the graph of Wikipedia page subset. Pages will be added
     * when they do not already exist in the graph.
     *
     * @param urlFrom the URL which has a link to urlTo.
     * @param urlTo the URL which urlFrom has a link to.
     */
    @Override
    public void addEdge(String urlFrom, String urlTo)
    {
        // Add the edge to the graph list 
        graphL.addEdge(urlFrom, urlTo);
    }

    /**
     * Finds the shortest path in number of links between two vertices. Uses a
     * Breadth First Search. If there is no path, returns -1.
     *
     * @param urlFrom the URL where the path should start.
     * @param urlTo the URL where the path should end.
     * @return the length of path in links. Otherwise -1
     */
    @Override
    public int getShortestPath(String urlFrom, String urlTo)
    {
        // If the URLs are the same, there is no distance
        if (urlFrom.equals(urlTo))
        {
            return 0;
        }

        // Local variables about the type of path
        boolean goingTo = false; // Path goes to existing
        boolean goingFrom = false; // Path goes from existing
        int from = 0; // Index of 'from' vertex

        // For every vertex in the graph
        for (int i = 0; i < graphL.getVertexCount(); i++)
        {
            // If the vertex contains the "to" URL, set 'goingTo' to true
            if (graphL.getVertex(i).equals(urlTo))
            {
                goingTo = true;
            }

            // If the vertex contains the "from" URL, set 'goingFrom' to true
            // Save index of 'from vertex'
            if (graphL.getVertex(i).equals(urlFrom))
            {
                goingFrom = true;
                from = i;
            }

            // If type of path determined, loop can end
            if (goingTo && goingFrom)
            {
                break;
            }
        }

        // If the path does not go to or from anywhere, return -1 to represent a non-existent path
        if (!goingTo || !goingFrom)
        {
            return -1;
        }

        // Local variables needed for the BFS
        int visited[] = new int[graphL.getVertexCount()]; // Visited vertice array
        int dist[] = new int[graphL.getVertexCount()]; // Distance array
        ArrayList<Integer> list = new ArrayList<>(); // Represents vertices
        int distance = -1; // Record of distance

        // Fill distance and visit arrays with -1 (default value)
        for (int m = 0; m < dist.length; m++)
        {
            visited[m] = -1; // Means unprocessed
            dist[m] = -1;
        }

        // Process from vertex
        visited[from] = -2;
        dist[from] = 0;
        list.add(from);

        // Process remaining vertices
        while (!list.isEmpty())
        {
            // Retrieve a row index
            int x = list.remove(0);

            // For every value in the row
            for (int index : graphL.getRow(x))
            {
                // If is not "itself" and has not been processed
                if (index != x && visited[index] == -1)
                {
                    // Update visit and distance arrays
                    visited[index] = x;
                    dist[index] = dist[x] + 1;

                    // Add to queue
                    list.add(index);
                }

                // Will determine the first occurrence of urlTo and update final distance
                if (index != x && graphL.getVertex(index).equals(urlTo))
                {
                    // Update distance
                    distance = dist[index];

                    // Clear queue
                    list.clear();

                    // Stop processing
                    break;
                }
            }
        }

        // Return final distance value
        return distance;
    }

    /**
     * Determines all the centers of the page graph and returns them in an array
     *
     * @return an array containing all the URLs that correspond to pages that
     * are centers.
     */
    @Override
    public String[] getCenters()
    {
        // Local variables needed
        String[] centers; // The list of centers
        int[] ecc = new int[graphL.getVertexCount()]; // The eccentricities
        int limit = GraphList.INF; // The running limit value
        ArrayList<Integer> list = new ArrayList<>(); // List to contain vertex indices

        // For every vertex
        for (int i = 0; i < graphL.getVertexCount(); i++)
        {
            // Determine its eccentricity
            ecc[i] = determineEcc(i);

            // If the eccentricity is less than the limit
            if (ecc[i] <= limit)
            {
                // If it is less than the value, but not equal
                if (ecc[i] < limit)
                {
                    // Clear the list
                    list.clear();

                    // Update the limit
                    limit = ecc[i];
                }

                // Add to the list
                list.add(i);
            }
        }

        // Initialize the final result
        centers = new String[list.size()];

        // Populate the centers array by filling it with the found vertices
        for (int j = 0; j < list.size(); j++)
        {
            centers[j] = graphL.getVertex(list.get(j));
        }

        // Return the array of centers
        return centers;
    }

    /*
     * Determines the longest edge of all those connected to the inputted vertex. 
     * This is the 'eccentricity' of the graph 
     * Uses a BFS, to from every vertex starting from the inputted vertex until 
     * the end of the graph.
     * 
     * @param the starting vertex
     */
    private int determineEcc(int start)
    {
        // The local variables needed for the BFS
        int distance[] = new int[graphL.getVertexCount()]; // Distances
        int visited[] = new int[graphL.getVertexCount()]; // Visited vertices
        ArrayList<Integer> list = new ArrayList<>(); // Stores vertices to be processed
        int greatestDist = 0; // Running greatest distance

        // Set distance and visit array to defaults
        for (int x = 0; x < distance.length; x++)
        {
            visited[x] = -1;
            distance[x] = -1;
        }

        // Process starting vertex
        visited[start] = -2;
        distance[start] = 0;
        list.add(start);

        // Process remaining vertices
        while (!list.isEmpty())
        {
            // Get row index
            int x = list.remove(0);

            // For every vertex in the row
            for (int index : graphL.getRow(x))
            {
                // If not "itself" and if unprocessed
                if (index != x && visited[index] == -1)
                {
                    // Update visit and distance array
                    visited[index] = x;
                    distance[index] = distance[x] + 1;

                    // Add to list
                    list.add(index);

                    // Update greatest distance
                    if (distance[index] > greatestDist)
                    {
                        greatestDist = distance[index];
                    }
                }
            }

        }

        // For every visited vertex
        for (int i = 0; i < visited.length; i++)
        {
            // If distance is still -1
            if (distance[i] == -1)
            {
                // Return infinity because you can never reach the vertex
                return GraphList.INF;
            }
        }

        // Otherwise, return the greatest distance
        return greatestDist;
    }

    /**
     * Finds all the strongly connected components (SCCs) of the page graph,
     * using Kosaraju's algorithm (2 Depth First Searches)
     *
     * @return an array of all the SCCs in a random order.
     */
    @Override
    public String[][] getStronglyConnectedComponents()
    {
        // Prepare for first stage
        int size = graphL.getVertexCount(); // Vertex count of graph
        boolean[] checked = new boolean[size]; // Checked vertices
        ArrayList<Integer> order = new ArrayList<>(); // Order

        // First stage = For every row
        for (int i = 0; i < size; i++)
        {
            // If row is not checked yet
            if (!checked[graphL.getRow(i).get(0)])
            {
                // Run DFS on row
                depthFirstSearch(graphL, checked, order, graphL.getRow(i).get(0));
            }
        }

        // Prepare for second stage
        GraphList transGraph = GraphList.transpose(graphL); // Transposed graph
        Arrays.fill(checked, false); // Reset checked array
        Collections.reverse(order); // Reverse order of order array
        int cols = 0; // The columns in the result
        ArrayList<ArrayList<Integer>> comp = new ArrayList<>(); // Adj. list to fill

        // For every vertex
        for (int i = 0; i < size; i++)
        {
            // If not checked
            if (!checked[order.get(i)])
            {
                // Create a list
                ArrayList<Integer> component = new ArrayList<>();

                // Run DFS with the new list
                depthFirstSearch(transGraph, checked, component, order.get(i));

                // If the size exceeds the cols, update cols
                if (component.size() > cols)
                {
                    cols = component.size();
                }

                // Add to component list
                comp.add(component);
            }
        }

        // Prepare for third stage
        String[][] strConComps = new String[comp.size()][cols]; // SCCs
        int counter1 = 0; // Counter 1
        int counter2 = 0; // Counter 2

        // Third stage = Generate component list
        for (ArrayList<Integer> aList : comp)
        {
            // Reset 2nd counter
            counter2 = 0;

            // For every value in the list
            for (int val : aList)
            {
                // Retrieve the component string 
                String compS = graphL.getVertex(comp.get(counter1).get(counter2));

                // Save the string in the SCC list
                strConComps[counter1][counter2] = compS;

                // Increase the second counter
                counter2++;
            }

            // Increase the first counter
            counter1++;
        }

        // Return the list of SCCs
        return strConComps;
    }

    /**
     * Conduct a depth first search
     *
     * @param G The graph
     * @param checked The array of checked vertices
     * @param ord The order array
     * @param rowI The rowIndex
     */
    private void depthFirstSearch(GraphList G, boolean[] checked, ArrayList<Integer> ord, int rowI)
    {
        // Set the row as checked
        checked[rowI] = true;

        // Process other rows
        for (int x = 1; x < G.getRow(rowI).size(); x++)
        {
            // If the row has not been checked
            if (!checked[G.getRow(rowI).get(x)])
            {
                // Recursively do depth first search 
                depthFirstSearch(G, checked, ord, G.getRow(rowI).get(x));
            }
        }

        // If the order array doesn't contain the row index, add it 
        if (!ord.contains(rowI))
        {
            ord.add(rowI);
        }
    }

    /**
     * Finds one Hamiltonian path in the page graph, using the Held-Karp
     * algorithm. This graph should have no more than 20 vertices. The output is
     * the URLs of pages in a Hamiltonian path. The order represents the
     * sequence of pages to follow.
     *
     * @return A Hamiltonian path, if it exists
     */
    @Override
    public String[] getHamiltonianPath()
    {
        // Retrieve the number of vertices
        int size = graphL.getVertexCount();

        // Calculate 2^n using a bitwise shift
        int exp = 1 << size;

        // Make a matrix with 2^n rows and n columns
        // Each cell represents
        int[][] mat = new int[exp][size];

        // Set all array values to default (infinity)
        for (int[] curArray : mat)
        {
            Arrays.fill(curArray, GraphList.INF);
        }

        // Set certain diagonals to zero
        for (int index = 0; index < size; index++)
        {
            int power = (1 << index);
            mat[power][index] = 0;
        }

        // Stage 1 = Masking
        // Iterate over all subsets of vertices
        for (int mask = 0; mask < (1 << size); mask++)
        {
            for (int i = 0; i < size; i++)
            {
                if ((mask & 1 << i) != 0)
                {
                    for (int j = 0; j < size; j++)
                    {
                        if ((mask & 1 << j) != 0)
                        {
                            // Determine if there is a path that visits i once and ends at i
                            // Save the smaller edge cost
                            mat[mask][i]
                                    = Math.min(mat[mask][i],
                                            mat[mask ^ (1 << i)][j] + graphL.compareEdges(j, i));
                        }
                    }
                }
            }
        }

        // Stage 2 = Tracing back the path taken
        //  Iterate over matrix created and get path indices
        int[] order = new int[size];
        int last = -1;
        int cur = (1 << size) - 1; // Calculate (2^n) -1
        int bj;
        //  From n-1 to 0
        for (int i = size - 1; i >= 0; i--)
        {
            bj = -1;

            for (int j = 0; j < size; j++)
            {
                if (((cur & 1 << j) != 0)
                        && (bj == -1 || mat[cur][bj]
                        + (last == -1 ? 0 : graphL.compareEdges(bj, last)) > mat[cur][j]
                        + (last == -1 ? 0 : graphL.compareEdges(j, last))))
                {
                    bj = j;
                }
            }

            order[i] = bj;
            cur ^= 1 << bj;
            last = bj;
        }

        // Make String array to hold answer
        String[] result = new String[size];

        // Validate answer
        // Loop to check if the path exists using the indices in the array
        for (int i = 0; i < order.length - 1; i++)
        {
            if (!graphL.getRow(order[i]).contains(order[i + 1]))
            {
                return result;
            }
        }

        // Convert the path from index form into String form
        for (int i = 0; i < size; i++)
        {
            result[i] = graphL.getVertex(order[i]);
        }

        // Return the result
        return result;

    }

    /**
     * A class that uses lists to hold the data of a graph
     */
    private static class GraphList
    {

        /**
         * An ArrayList of the graph's vertices
         */
        private ArrayList<String> vertices;

        /**
         * An ArrayList that contains an adjacency list of the graph's edges
         */
        private ArrayList<ArrayList<Integer>> edges;

        /**
         * A very large constant that represents infinity
         */
        public static final int INF = Integer.MAX_VALUE / 2;

        /**
         * Create an instance of the graph
         */
        private GraphList()
        {
            // Initialize lists
            vertices = new ArrayList<>();
            edges = new ArrayList<>();

        }

        /**
         * Add an edge to the graph
         *
         * @param from
         * @param to
         */
        public void addEdge(String from, String to)
        {
            // Indices of the vertices, set to defaults
            int fromIndex = -1;
            int toIndex = -1;

            // Type of edge
            boolean goingFrom = false;
            boolean goingTo = false;

            // For every vertex
            for (int i = 0; i < vertices.size(); i++)
            {
                // If "to" already exists, update the to index
                if (vertices.get(i).equals(to))
                {
                    toIndex = i;
                    goingTo = true;
                }

                // If "from" already exists, update the from index
                if (vertices.get(i).equals(from))
                {
                    fromIndex = i;
                    goingFrom = true;
                }

                // If both vertices have been updated, stop processing
                if (goingTo && goingFrom)
                {
                    break;
                }
            }

            // If from vertex does not exist
            if (!goingFrom)
            {
                // Add current edge count to edges
                ArrayList<Integer> fromList = new ArrayList<>();
                fromIndex = edges.size();
                fromList.add(fromIndex);
                edges.add(fromList);

                // Add vertex = "from" string 
                vertices.add(from);
            }

            // If to vertex does exist
            if (goingTo)
            {
                // Add connection between from and to
                edges.get(fromIndex).add(toIndex);
            }
            else
            {
                // Add current edge count as an edge
                ArrayList<Integer> toList = new ArrayList<>();
                toIndex = edges.size();
                toList.add(toIndex);
                edges.get(fromIndex).add(toIndex);
                edges.add(toList);

                // Add vertex = "to" string
                vertices.add(to);
            }
        }

        /**
         * Retrieve a vertices index using its string. Return -1 when not found
         *
         * @param str
         * @return the index. Otherwise -1
         */
        public int getIndex(String str)
        {
            // For every vertex
            for (int index = 0; index < vertices.size(); index++)
            {
                // If the string matches, return it
                if (vertices.get(index).equals(str))
                {
                    return index;
                }
            }

            // Return -1 when no matches occur
            return -1;
        }

        /**
         * Retrieve the row of edge1 and return 1 if edge2 is in it Otherwise,
         * return infinity.
         *
         * @param edge1
         * @param edge2
         * @return 1 when present. Otherwise infinity
         */
        public int compareEdges(int edge1, int edge2)
        {
            // Return 1 when match occurs
            if (edges.get(edge1).contains(edge2))
            {
                return 1;
            }

            // Return infinity when no matches occur
            return INF;
        }

        /**
         * Retrieve a vertices string using its index
         *
         * @param i the index
         * @return the vertices string
         */
        public String getVertex(int i)
        {
            return vertices.get(i);
        }

        /**
         * Retrieve the ith row of edges
         *
         * @param index the row index
         * @return the row
         */
        public ArrayList<Integer> getRow(int index)
        {
            return edges.get(index);
        }

        /**
         * Returns the number of vertices in the graph
         *
         * @return the vertex count
         */
        public int getVertexCount()
        {
            return vertices.size();
        }

        /**
         * Returns a transposed version of the inputted graph
         *
         * @param inputG the inputted graph
         * @return the transposed graph
         */
        public static GraphList transpose(GraphList inputG)
        {
            // Make a new graph
            GraphList transGraph = new GraphList();

            // Prepare edges and vertices
            for (int index = 0; index < inputG.getVertexCount(); index++)
            {
                ArrayList<Integer> temp = new ArrayList<>();
                temp.add(index);
                transGraph.edges.add(temp);
                transGraph.vertices.add(inputG.vertices.get(index));
            }

            // Add edges
            for (ArrayList<Integer> x : inputG.edges)
            {
                for (int child : x)
                {
                    if (child != x.get(0))
                    {
                        transGraph.addEdge(inputG.getVertex(child), inputG.getVertex(x.get(0)));
                    }
                }
            }

            // Return graph
            return transGraph;
        }
    }

}
