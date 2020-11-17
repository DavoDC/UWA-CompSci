/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Wk3.Stacks;

import CITS2200.Overflow;
import CITS2200.Stack;
import CITS2200.Underflow;

/**
 * Tests the StackBlock
 *
 * @author David
 */
public class SBTest
{

    // Stackblock
    private StackBlock sb;

    // Size
    private final int size = 3;

    public SBTest()
    {
        // Create StackBlock
        sb = new StackBlock(size);
        System.out.println("Made a StackBlock of size: " + size);

        // Empty test
        statusCheck();

        // Push test
        makeSpaces(4);
        for (int i = 0; i < size; i++)
        {
            String ob = "Elem" + i + "";
            sb.push(ob);
            System.out.println("Pushed on: " + ob);

        }

        // Push excess test
        makeSpaces(4);
        System.out.println("Tried to push again:");
        try
        {
            sb.push("Elem3");
        }
        catch (Overflow e)
        {
            System.out.println(e.getMessage());
        }

        // Examine test
        makeSpaces(2);
        System.out.println("Examined top object, was:" + sb.examine());

        // Empty/Full test
        statusCheck();

        // Pop test
        makeSpaces(4);
        for (int i = 0; i < size; i++)
        {
            System.out.println("Popped off: " + sb.pop());
        }

        // Pop when empty test
        makeSpaces(2);
        System.out.println("Tried to pop once more:");
        try
        {
            sb.pop();
        }
        catch (Underflow e)
        {

            System.out.println(e.getMessage());
        }

        // Empty/Full test
        statusCheck();

        // Finished
        System.out.println();

    }

    public final void statusCheck()
    {
        System.out.println();
        System.out.println("Empty Status = " + sb.isEmpty());
        System.out.println("Full Status = " + sb.isFull());
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
