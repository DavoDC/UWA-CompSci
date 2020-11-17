package Project.Code;

import java.io.*;
import java.util.Arrays;

/**
 * Test the project
 *
 * @author David
 */
public class CITS2200ProjectTester
{

    /**
     * Run the test
     */
    public static void runTest()
    {
        // Create an instance of your implementation.
        MyCITS2200Project proj = new MyCITS2200Project();
        // Project2 proj = new Project2();
        System.out.println("Project Test : " + proj.getClass().getName());
        System.out.println();

        // Load graph
        String filePath = "src/Project/Code/hampathdata.txt";
        // String filePath = "src/Project/Code/algodata.txt";
        // String filePath = "src/Project/Code/countrydata.txt";
        
        loadGraph(proj, filePath);
        System.out.println();

        // Test getCenters
        String[] centers = proj.getCenters();
        System.out.println("getCenters:");
        System.out.println(Arrays.toString(centers));
        System.out.println();

        // Test getSCC
        String[][] SCCs = proj.getStronglyConnectedComponents();
        System.out.println("getStronglyConnectedComponents:");
        for (String[] array : SCCs)
        {
            System.out.println(Arrays.toString(array));
        }
        System.out.println();

        // Test getShortestPath
        String start = "/wiki/Gomory%E2%80%93Hu_tree";
        String end = "/wiki/Multi-commodity_flow_problem";
        int spInt = proj.getShortestPath(start, end);
        System.out.println("getShortestPath:");
        System.out.println("Edges = " + start + " , " + end);
        System.out.println("Length found: " + spInt);
        System.out.println("Expected (algodata): " + 3);
        System.out.println();

        // Test getHamilonianPath
        String[] hPath = proj.getHamiltonianPath();
        System.out.println("getHamiltonianPath:");
        System.out.println("Length = " + hPath.length);
        System.out.println(Arrays.toString(hPath));
        System.out.println();

    }

    /**
     * Load a graph
     *
     * @param project
     * @param path
     */
    public static void loadGraph(CITS2200Project project, String path)
    {
        // The graph is in the following format:
        // Every pair of consecutive lines represent a directed edge.
        // The edge goes from the URL in the first line to the URL in the second line.
        boolean noProb = true;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while (reader.ready())
            {
                String from = reader.readLine();
                String to = reader.readLine();
                // System.out.println("Adding edge from " + from + " to " + to);
                project.addEdge(from, to);
            }
        }
        catch (Exception e)
        {
            System.out.println("There was a problem:");
            System.out.println(e.toString());
            noProb = false;
        }
        finally
        {
            if (noProb)
            {
                System.out.println("Edges added successfully from " + path);
            }

        }

    }

}
