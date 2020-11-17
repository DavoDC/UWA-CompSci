

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class LifeViewerTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class LifeViewerTest
{
    private Life L;
    private LifeViewer LV;

    /**
     * Default constructor for test class LifeViewerTest
     */
    public LifeViewerTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        L = new Life(150, 100, 0.3);
        LV = new LifeViewer(L);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void see()
    {
        LV.animate(5000);
    }
}

