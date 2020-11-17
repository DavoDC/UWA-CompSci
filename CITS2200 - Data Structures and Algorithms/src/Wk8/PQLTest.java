package Wk8;

import Main.TestTools;

/**
 * Tests the priority queue
 *
 * @author David
 */
public class PQLTest
{

    private PriorityQueueLinked pql = new PriorityQueueLinked();
    private int itemCount = 0;
    private final String sep = " +++++++++ ";

    public PQLTest()
    {
        // Start
        System.out.println();
        System.out.println(" PQL Test ");
        System.out.println();

        // Test empty
        TestTools.assertTrue(pql.isEmpty());

        // Check empty exceptions
        checkExamAndDeqExceptions();

        // Enqueue and check
        System.out.println(sep + "EnQing" + sep);
        enQAndCheck(TestTools.getRandomObject() + 6, 6);
        enQAndCheck(TestTools.getRandomObject() + 1, 1);
        enQAndCheck(TestTools.getRandomObject() + 3, 3);
        enQAndCheck(TestTools.getRandomObject() + 12, 12);
        enQAndCheck(TestTools.getRandomObject() + 0, 0);
        System.out.println();

        // Dequeue and check
        System.out.println(sep + "DeQing" + sep);
        while (itemCount > 0)
        {
            deQAndCheck();
        }
        System.out.println();

        // Check empty exceptions
        checkExamAndDeqExceptions();

    }

    private void enQAndCheck(String val, int p)
    {
        pql.enqueue(val, p);
        System.out.println("Pushed on '" + val + "' with priority " + p);
        System.out.println(pql.toString());
        System.out.println("Examined '" + pql.examine() + "'");
        System.out.println();
        itemCount++;
    }

    private void deQAndCheck()
    {
        System.out.println("Examined '" + pql.examine() + "'");
        String val = (String) pql.dequeue();
        System.out.println("Retrieved '" + val + "'");
        System.out.println(pql.toString());
        System.out.println();
        itemCount--;
    }

    private void checkExamAndDeqExceptions()
    {
        TestTools.assertThrows(() ->
        {
            pql.dequeue();
        });
        TestTools.assertThrows(() ->
        {
            pql.examine();
        });
    }
}
