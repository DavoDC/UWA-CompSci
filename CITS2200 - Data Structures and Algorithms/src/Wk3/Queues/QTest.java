package Wk3.Queues;

/**
 * Tests a Queue
 *
 * @author David
 */
public class QTest
{

    // Size
    private final int size = 5;

    // Queue
    // private QueueLinked q = new QueueLinked();
    // private QueueBlock q = new QueueBlock(size);
    private QueueCyclic q = new QueueCyclic(size);

    // Number of tests
    private int testNo = 0;

    // Type
    private boolean isRecur;

    public QTest()
    {
        try
        {
            // Initialization
            //  Get class name
            String init = "Made a " + q.getClass().getSimpleName();
            //  Check if recursive
            isRecur = init.toLowerCase().contains("link");
            // Add size if not recursive
            if (!isRecur)
            {
                init += " of size " + size;
            }
            // Print info
            System.out.println(init);
            System.out.println();

            // Run tests
            fullTest();
            fullTest();
            fullTest();
        }
        catch (Exception e)
        {
            System.out.println("Error! (" + e.getMessage() + ")");
            System.out.println();
        }

    }

    public final void fullTest()
    {
        testNo++;

        System.out.println("############### Test " + testNo + " ##################");

        // Check
        statusCheck();

        // Enqueue items
        System.out.println();
        for (int i = 0; i < size; i++)
        {
            String ob = "Elem" + i;
            q.enqueue(ob);
            System.out.println("Enqueued: " + ob);
            //q.printIndices();
        }

        // Examine 
        System.out.println();
        System.out.println("Examined: " + q.examine());

        // Check
        statusCheck();

        // Extra enqueue
        System.out.println();
        try
        {
            q.enqueue("Extra");
        }
        catch (Exception e)
        {
            System.out.println("Blocked extra ENqueue");
        }

        // Dequeue
        System.out.println();
        for (int i = 0; i < size; i++)
        {
            System.out.println("Dequeued: " + q.dequeue());
            //q.printIndices();
        }

        // Extra dequeue
        System.out.println();
        try
        {
            q.dequeue();
        }
        catch (Exception e)
        {
            System.out.println("Blocked extra DEqueue");
        }

        // Finished
        System.out.println();
        System.out.println("&&&&&&  Test " + testNo + " Finished Successfully &&&&&&&&");
        System.out.println();
    }

    public final void statusCheck()
    {
        System.out.println();
        System.out.println("Empty Status = " + q.isEmpty());
        //System.out.println("Full Status = " + q.isFull());
    }
}
