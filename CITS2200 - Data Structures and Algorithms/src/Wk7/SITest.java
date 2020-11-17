package Wk7;

import CITS2200.Graph;
import Wk9.PathTest;

/**
 * Test search imp.
 *
 * @author David
 */
public class SITest
{

    public SITest()
    {
        SearchImp si = new SearchImp();
        Graph graph = Graph.randomGraph(6, 0.6);

        si.getConnectedTree(graph, 0);
        si.getDistances(graph, 0);
        si.getTimes(graph, 0);

    }

}
