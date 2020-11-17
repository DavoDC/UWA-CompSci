
/**
 * Write a description of class Tester here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Tester
{
    MarksAnalyser MA1 = new MarksAnalyser();

    /**
     * Constructor for objects of class Tester
     * 
     * Input 'A' to test using the results = 100, 100, 100
     * 
     * Input 'B' to test using the results = 50 , 85 , 85 , 100, 101
     * 
     * Input 'C' to test using the results = 0, 60, 0
     * 
     * 
     * Note: If you wish to enter A , enter this exactly = 'A'
     * 
     */
    public Tester(char input)
    {

        switch (input) 
        {
            case 'A': aEnterSetA();
            break; 

            case 'B': bEnterSetB();
            break;

            case 'C': cEnterSetC();
            break;
            
            default: System.out.println("Please enter a valid character");
        }
    }

    private void aEnterSetA()
    {
        Student JR = new Student("Jim", 100);
        Student ET = new Student("Eric", 100);
        Student LB = new Student("Leslie", 100);
        
        MA1.addtoClass(JR);
        MA1.addtoClass(ET);
        MA1.addtoClass(LB);
    }

    private void bEnterSetB()
    {
        Student Ben = new Student("Ben", 50);
        Student Dave = new Student("Dave", 85);
        Student Nate = new Student("Nate", 85);
        Student Marsha = new Student("MC", 100);
        Student Steven = new Student ("Steven", 101);

        MA1.addtoClass(Ben);
        MA1.addtoClass(Dave);
        MA1.addtoClass(Nate);
        MA1.addtoClass(Marsha);
        MA1.addtoClass(Steven);
    }

    private void cEnterSetC()
    {
        Student Jim = new Student("Jim", 0);
        Student John = new Student("John", 60);
        Student Jack = new Student("Jack", 0);
        
        MA1.addtoClass(Jim);
        MA1.addtoClass(John);
        MA1.addtoClass(Jack);
    }

    public void Test()
    {
        MA1.getClassInfo();

        MA1.getMarks();

        System.out.println("\n" + "Top mark = " + MA1.maximum());
        System.out.println("Lowest mark = " + MA1.minimum());

        System.out.println("Loop version test = " + MA1.loopVersion());

        System.out.println("Average = " + MA1.average());

        MA1.starStudents();

        System.out.println(" ");
        System.out.println("Standard Deviation = " + MA1.getSD());
    }

}

