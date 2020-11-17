
import java.util.*;

public class MyTest
{
    Sequence Null = new Sequence("1");
    Sequence Example = new Sequence("1, 1, 5, 13, 25");

    private Sequence seqa;   
    private Sequence seqb;
    private Sequence seqc;
    private Sequence seq1;
    private Sequence seq2;
    private Sequence seq3;

    private Sequence seq15;
    private Sequence seq444;
    private Sequence seqm;

    public MyTest()
    {
        seqa   = new Sequence("0.6, 1.2, 2.4 , 4.8");
        seqb  = new Sequence("-9, -18");
        seqc   = new Sequence("5, 10");
        seq1   = new Sequence("10, 20, 30");
        seq2   = new Sequence("-1, 0, 3");
        seq3   = new Sequence("0, 85, 1640, 13107, 65104, 239945, 720600, 1864135");

        seq15  = new Sequence("1, 5");
        seq444 = new Sequence("4, 4, 4");
        seqm   = new Sequence("0, -1.5, -4");
    }

    public void agetSeqInfo()
    {
        ArrayList<Sequence> sequences = new ArrayList<>();

        sequences.add(Example);
        sequences.add(seq15);
        sequences.add(seq444);
        sequences.add(seqm);

        sequences.add(seqa);
        sequences.add(seqb);
        sequences.add(seqc);
        sequences.add(seq1);
        sequences.add(seq2);
        sequences.add(seq3);

        int counter = 0;
        
        for (Sequence seq : sequences)
        {

            for (Double d : seq.getSequence())
            {
                System.out.print(" " + d + " ");
            }
            
            ++counter;
            
            if (counter == 4)
            {
                 System.out.println();
            }
            
            System.out.println();
        }

        System.out.println();
        System.out.println("NB = Shows set 1 then set 2");
    }

    public void bTestAll()
    {
        cSeq1Test();
        dSeq2Test();
    }

    public void cSeq1Test()
    {

        System.out.println("#####  Polynomials generated from Sequence Set 1 =  #####" );
        System.out.println(Example.solveSequence().displayPolynomial());
        System.out.println(seq15.solveSequence().displayPolynomial());
        System.out.println(seq444.solveSequence().displayPolynomial());
        System.out.println(seqm.solveSequence().displayPolynomial());
        System.out.println();
    }

    public void dSeq2Test()
    {
        System.out.println();
        System.out.println();
        System.out.println("#####    Polynomials generated from Sequence Set 2 =   #####");
        System.out.println(seqa.solveSequence().displayPolynomial());
        System.out.println(seqb.solveSequence().displayPolynomial());
        System.out.println(seqc.solveSequence().displayPolynomial());
        System.out.println(seq1.solveSequence().displayPolynomial());
        System.out.println(seq2.solveSequence().displayPolynomial());
        System.out.println(seq3.solveSequence().displayPolynomial());
        System.out.println();
    }

    public static void xTest()
    {
        //Polynomial poly = new Polynomial();
        //poly.addTerm(new Term(-15,0));
        
        Term t = new Term(-15,0);
        
        System.out.println(t.displayImproved());
    }
    
    }
