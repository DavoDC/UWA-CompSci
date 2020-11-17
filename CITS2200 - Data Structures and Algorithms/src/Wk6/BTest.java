package Wk6;

import CITS2200.Iterator;
import Main.TestTools;

public final class BTest
{

    public BTest()
    {
        testBinTree();
    }

    public void testBinTree()
    {

        // Tree structure
        //         "a"
        //      /       \
        //    "b"       "c"
        //   /  \       /
        // "d"  "e"   "f"
        //   \
        //   "g"
        // Make binary trees
        BinTree<String> leaf = new BinTree<>();
        BinTree<String> root;
        {
            BinTree<String> g = new BinTree<>("g", leaf, leaf);

            BinTree<String> d = new BinTree<>("d", leaf, g);
            BinTree<String> e = new BinTree<>("e", leaf, leaf);
            BinTree<String> f = new BinTree<>("f", leaf, leaf);

            BinTree<String> b = new BinTree<>("b", d, e);
            BinTree<String> c = new BinTree<>("c", f, leaf);

            root = new BinTree<>("a", b, c);
        }

        // Iterator test
        String[] expected =
        {
            "a", "b", "c", "d", "e", "f", "g"
        };

        Iterator<String> iterator = root.iterator();

        for (String expectedNext : expected)
        {
            TestTools.assertTrue(iterator.hasNext());

            String next = iterator.next();

            TestTools.assertEquals(expectedNext, next);
        }

        TestTools.assertTrue(!iterator.hasNext());

        // Equality test
        BinTree<String> rootCopy;
        {
            BinTree<String> g = new BinTree<>("g", leaf, leaf);

            BinTree<String> d = new BinTree<>("d", leaf, g);
            BinTree<String> e = new BinTree<>("e", leaf, leaf);
            BinTree<String> f = new BinTree<>("f", leaf, leaf);

            BinTree<String> b = new BinTree<>("b", d, e);
            BinTree<String> c = new BinTree<>("c", f, leaf);

            rootCopy = new BinTree<>("a", b, c);
        }

        TestTools.assertTrue(root.equals(rootCopy));
        TestTools.assertTrue(root.getLeft().equals(rootCopy.getLeft()));

        BinTree<String> rootNoG;
        {
            BinTree<String> d = new BinTree<>("d", leaf, leaf);
            BinTree<String> e = new BinTree<>("e", leaf, leaf);
            BinTree<String> f = new BinTree<>("f", leaf, leaf);

            BinTree<String> b = new BinTree<>("b", d, e);
            BinTree<String> c = new BinTree<>("c", f, leaf);

            rootNoG = new BinTree<>("a", b, c);
        }

        TestTools.assertTrue(!root.equals(rootNoG));
        TestTools.assertTrue(!root.equals(null));
        TestTools.assertTrue(!root.equals("apples"));
        TestTools.assertTrue(!root.equals(root.getLeft()));

        System.out.println("BinTree passed");
    }

}
