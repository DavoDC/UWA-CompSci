package Wk3.Stacks;

import CITS2200.Underflow;

/**
 * Tests the StackLinked structure
 *
 * @author David
 */
public class SLTest
{

    private final int elements = 4;

    public SLTest()
    {
        // Create StackBlock
        StackLinked sl = new StackLinked();
        System.out.println("Just made a StackLinked");

        // Push test
        makeSpaces(4);
        for (int i = 0; i < elements; i++)
        {
            String ob = "Elem" + i + "";
            sl.push(ob);
            System.out.println("Pushed on: " + ob);

        }

        // Examine test
        makeSpaces(2);
        System.out.println("Examined top object, was:" + sl.examine());

        // Pop test
        makeSpaces(4);
        for (int i = 0; i < elements; i++)
        {
            System.out.println("Popped off: " + sl.pop());
        }

        // Pop when empty test
        makeSpaces(2);
        System.out.println("Tried to pop once more:");
        try
        {
            sl.pop();
        }
        catch (Underflow e)
        {

            System.out.println(e.getMessage());
        }

        // Finished
        System.out.println();

    }

    private void makeSpaces(int no)
    {
        int counter = no;
        while (counter >= 0)
        {
            System.out.println();
            counter--;
        }
    }
}
