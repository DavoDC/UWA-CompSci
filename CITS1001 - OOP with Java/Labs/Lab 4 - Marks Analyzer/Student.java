/**
 * Naive model of a student with a total percentage mark 
 * that is initially 0 and can have new marks added to the total
 * TODO This version has no validity checking for mark values
 * 
 * @author Rachel Cardell-Oliver
 * @version January 2013 based on Barnes and Koelling
 */
public class Student
{
    // the student's full name
    private String name;
    // the mark out of 100 awarded
    private int mark;

    /**
     * Create a new student with a given name and ID number.
     */
    public Student(String fullName, int markawarded)
    {
        name = fullName;
        mark = markawarded;
    }

    /**
     * Return the full name of this student.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * @return int the mark awarded for this student.
     */
    public int getMark()
    {
        return mark;
    }
    
    /**
     * Method to add a newmark to the current total mark.
     * @param newMark score to be added to totalMark
     * Currently no checks for mark validity
     */
    public void addMark(int newMark) 
    {
        mark = mark + newMark;
    }
    
    /**
     * Method to return a String of student details
     */
    public String toString()
    {
        return ("Student name: " + name + " has been awarded " + mark);
    }
}
