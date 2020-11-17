package Wk4.Deques;

/**
 * Tests the DequeCyclic
 *
 * @author David
 */
public class DequeTest
{

    // Size
    private final int size = 4;

    // DS
    // private DequeBlock deque = new DequeBlock(size);
    private DequeCyclic deque = new DequeCyclic(size);

    // Tests wanted
    private int testWanted = 1;

    // Tests done
    private int testNo = 0;

    public DequeTest()
    {
        try
        {
            // Initialization
            String init = "Made a " + deque.getClass().getSimpleName() + " of size " + size;
            System.out.println(init);
            System.out.println();

            // Check initial state
            System.out.println();
            printState();

            // Run tests
            while (testWanted != 0)
            {
                doCustomTestRun();
                // doFullTestRun();

                testWanted--;
            }

        }
        catch (Exception e)
        {
            System.out.println("Error! (" + e.getMessage() + ")");
            e.printStackTrace();
            System.out.println();
        }

    }

    private void doCustomTestRun()
    {
        push("R");
        push("R");
        push("R");
        push("R");

        pop("R");
        pop("R");
        pop("R");
        pop("R");

    }

    private void doFullTestRun()
    {

        // Test push and peek
        System.out.println();
        push(getRandomSide());
        peekBoth();
        push(getRandomSide());
        peekBoth();
        push(getRandomSide());
        peekBoth();
        push(getRandomSide());
        peekBoth();

        // Test overflow exceptions
        // push("Left");
        // push("Right");
        // Check state
        System.out.println();
        printState();

        // Test pop
        System.out.println();
        pop(getRandomSide());
        pop(getRandomSide());
        pop(getRandomSide());
        pop(getRandomSide());

        // Test underflow exceptions 
        // pop("Left");
        // pop("Right");
        // peek("Left");
        // peek("Right);
        // Check state
        System.out.println();
        printState();

        // Finished testing
        testNo++;
        System.out.println("TEST " + testNo + " DONE ^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println("###################################");
        System.out.println("###################################");
    }

    private String getRandomSide()
    {
        if (Math.random() > 0.5)
        {
            return "Left";
        }
        else
        {
            return "Right";
        }
    }

    private final void printState()
    {
        System.out.println(deque.getState());
        System.out.println("Empty? = " + deque.isEmpty());
        System.out.println("Full? = " + deque.isFull());
        System.out.println();
    }

    private void peekBoth()
    {
        peek("L");
        peek("R");
        System.out.println();
    }

    private void peek(String rawSide)
    {
        String peekStart = "Peeked from the ";
        if (rawSide.toLowerCase().contains("l"))
        {
            System.out.println(peekStart + "left and saw: " + deque.peekLeft());
        }
        else
        {
            System.out.println(peekStart + "right and saw: " + deque.peekRight());
        }

    }

    private final void push(String rawSide)
    {
        // Create random object
        String ob = getObject();

        // Push from left or right
        String side = "";
        if (rawSide.toLowerCase().contains("l"))
        {
            deque.pushLeft(ob);
            side += "left";
        }
        else
        {
            deque.pushRight(ob);
            side += "right";
        }

        System.out.println("Pushed on '" + ob + "' from the " + side);

        printState();
    }

    private void pop(String rawSide)
    {
        String ob = "";
        String side = "";
        if (rawSide.toLowerCase().contains("l"))
        {
            ob = (String) deque.popLeft();
            side += "left";
        }
        else
        {
            ob = (String) deque.popRight();
            side += "right";
        }

        System.out.println("Popped off '" + ob + "' from the " + side);

        printState();

    }

    private String getObject()
    {
        return ("Elem" + (int) (Math.random() * 1000));
    }
}
