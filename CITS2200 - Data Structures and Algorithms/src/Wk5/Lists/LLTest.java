package Wk5.Lists;

import CITS2200.WindowLinked;

/**
 * Tests the LinkedList
 *
 * @author David
 */
public class LLTest
{

    // Objects
    private ListLinked ll;
    private WindowLinked window;

    public LLTest()
    {
        // Initialise
        ll = new ListLinked();
        String init = "Made a " + ll.getClass().getSimpleName();
        System.out.println(init);
        System.out.println();

        // Check initial state
        System.out.println();
        System.out.println("Empty? = " + ll.isEmpty());

        // Initialise window
        System.out.println();
        window = new WindowLinked();
        ll.beforeFirst(window);
        System.out.println("Initialised window at beforeFirst");

        // Insertions
        System.out.println();
        addAfterAndCheck();
        addAfterAndCheck();
        addAfterAndCheck();

        // Check backwards
        System.out.println();
        getItemUnderWindow();
        goBackAndCheck();
        goBackAndCheck();

        // insertBeforetest
        insertBeforeTest();

        // Replace test
        replaceTest();

        // Delete
        deleteTest();

        // Finished testing
        System.out.println();
        System.out.println("############ Test finished");
        System.out.println();

    }

    private String getObject()
    {
        return ("Elem" + (int) (Math.random() * 1000));
    }

    private void addAfterAndCheck()
    {
        System.out.println();

        ll.insertAfter(getObject(), window);
        System.out.println("An object was inserted after the window");

        ll.next(window);
        System.out.println("Moved window right");
        getItemUnderWindow();

        System.out.println();
        System.out.println();
    }

    private void goBackAndCheck()
    {
        System.out.println();

        ll.previous(window);
        System.out.println("Moved window left");
        getItemUnderWindow();

        System.out.println();
        System.out.println();
    }

    private void goForwardAndCheck()
    {
        System.out.println();

        ll.next(window);
        System.out.println("Moved window right");
        getItemUnderWindow();

        System.out.println();
        System.out.println();
    }

    private void getItemUnderWindow()
    {
        String exMsg = "Examined the object under the window: ";
        String ob = (String) ll.examine(window);
        System.out.println(exMsg + ob);
    }

    private void insertBeforeTest()
    {
        System.out.println();

        // Move right
        ll.next(window);
        System.out.println("Moved window right");

        // InsertBefore
        String newOb = getObject();
        ll.insertBefore(newOb, window);
        System.out.println("'" + newOb + "' was inserted before the window");

        // Check current 
        getItemUnderWindow();

        // Go forward and check
        goBackAndCheck();

    }

    private void replaceTest()
    {
        // Current under?
        getItemUnderWindow();

        // Moved right
        goForwardAndCheck();

        // Replace
        String newOb = getObject();
        String returned = (String) ll.replace(newOb, window);
        System.out.println("Replaced current object with '" + newOb + "'");
        System.out.println("The returned, old object was: " + returned);

        // Check
        getItemUnderWindow();
    }

    private void deleteTest()
    {
        // Current under?
        getItemUnderWindow();

        // Move right
        goForwardAndCheck();

        // Delete
        String returned = (String) ll.delete(window);
        System.out.println("Deleted '" + returned + "' , which was under the window");
        System.out.println("The window was moved forward");

        // Move back
        goBackAndCheck();
    }
}
