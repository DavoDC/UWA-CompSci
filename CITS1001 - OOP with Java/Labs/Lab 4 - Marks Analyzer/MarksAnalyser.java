import java.util.ArrayList;
import java.util.*;

/**
 * MarksAnalyser stores a collection of Student records.
 * Complete the code missing from methods below for practice
 * at using ArrayList and for-each loops
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MarksAnalyser
{
    /*
     * Arbitrary length list of Student objects representing a 
     * specific group of students for a unit or lab class.
     */
    ArrayList<Student> Room = new ArrayList<>();

    ArrayList<Integer> Marks = new ArrayList<>();
    
    

    /**
     * Constructor for objects of class MarksAnalyser
     */
    public MarksAnalyser()
    {
      getMarks();
    }

    public void getMarks()
    {
        for(Student curstud : Room)
        {
            Marks.add(curstud.getMark());
        }
    }

    /**
     * Add a new student record to the class list.
     * @param member The Student object to be added.
     */
    public void addtoClass(Student newstudent)
    {
        Room.add(newstudent);
    }

    /**
     * @return The number of students (Student objects) in the class list.
     */
    public int getNumberOfStudents()
    {
        int classize = Room.size();
        return classize;
    }

    /**
     * 
     * For every student =
     * Get name and mark of student
     * Add to string
     * 
     * Return the string
     */
    public String showCourse() 
    {
        String course = " ";

        for (Student curstud : Room)
        {
            course += ("Name = " + (curstud.getName()) + "," + "Mark = " + (curstud.getMark()) + " ");
        } 

        return course;
    }

    public void getClassInfo() 
    {
        int no = 1;

        for (Student curstud : Room)
        {
            System.out.println("Student " + no + " =");

            String course = ("Name = " + (curstud.getName()) + "," + "Mark = " + (curstud.getMark()) + " ");

            System.out.println(course);

            ++no;
        }

    }

    /**
     * Generate a class list of student names (only) in a string
     * @return String list of names
     */
    public String showCourseNames() 
    {   
        String course = "Course =";

        for (Student curstud : Room)
        {
            course += ("Name = " + curstud.getName() + ", ");
        }
        
        
        return course;
    }

    /**
     * Get a specific Student object record from the class list
     * 
     * @param String name of the student in the class list
     * @return Student object if the name is present
     * 
     * Otherwise, return null
     */
    public Student findStudent(String name) 
    {

        for (Student curstud : Room)
        {
            if(curstud.getName() == name)
            {
                return curstud;
            }
        }

        return null;
    }

    /**
     * Find the minimum mark value, ignoring the student name
     * @return int the smallest of all values in the class list
     * 
     * 
     */
    public int minimum()
    {
        int minMark = 0;

        Collections.sort(Marks);

        minMark = Marks.get(0);

        return minMark;
    }

    /**
     * Find the maximum mark in the ArrayList
     * @return int the largest value in the marks ArrayList
     */
    public int maximum()
    {
        int maxMark = 0;

        Collections.sort(Marks);

        maxMark = Marks.get(Marks.size() - 1);

        return maxMark;
    }

    public String loopVersion()
    {
        int maxMark = 0;
                
        for (Integer curmark : Marks)
        {
            if (curmark > maxMark )
            {
                maxMark = curmark;
            }
        }
        
        
        int minMark = maxMark;
        
        for (Integer curmark : Marks)
        {
            
            if (curmark < minMark)
            {
                minMark = curmark;
            }
        }
             
        String stringmarks = ("Min = " + minMark + " , Max = " + maxMark);
        return stringmarks;
    }

    /**
     * Find the average mark for the class list
     * @return double average value
     */
    public double average()
    {
        double average = 0;

        for (Integer curmark : Marks)
        {
            average += curmark;
        }

        average = average/(Marks.size());
        return average;
    }

    /**
     * Will return the population standard deviation of marks in the class list
     * 
     * @return double value of the population standard deviation
     */
    public double getSD()
    {
        double av;
        av = average();

        double variance = 0;

        for (Integer curmark : Marks)
        {
            variance += ((curmark - av)*(curmark - av));
        }

        variance = variance/(Marks.size());

        double standev = 0;
        standev = Math.sqrt(variance);

        return standev; 
    }

    /**
     * Find all the star students (those with a mark of 75 or above)
     *
     * Print out their names
     * 
     * @return ArrayList of all "stars"
     * 
     * 
     */
    public ArrayList<Student> starStudents()
    {
        ArrayList<Student> stars = new ArrayList<>();

        for (Student curstud : Room)
        {

            if(curstud.getMark() >= 75)
            {
                stars.add(curstud);
            }

        }

        System.out.println (" ");
        System.out.println ("Star Students = ");
        for (Student curstud : stars)
        {
            System.out.println(curstud.getName());
        }

        return stars;
    }

}

