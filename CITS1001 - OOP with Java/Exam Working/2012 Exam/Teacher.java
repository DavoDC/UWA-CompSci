import java.util.ArrayList;

public class Teacher
{
    public static ArrayList<Student> classroom;
    
    public int Teacher()
    {
         classroom = new ArrayList<>();
         
         classroom.add(new Student("David", 70, 80));
         classroom.add(new Student("Jim", 65, 80));
         classroom.add(new Student("John", 55, 80));
         
         return getPercentage(classroom.get(0));
    }
    
    public int getPercentage(Student student)
    {
        double mark = student.getMark();
        double max = student.getMax();
        
        double result = (mark/max)*100;
        result = Math.round(result);
        
        return (int) result;
    }
    
    public String topStudent (ArrayList<Student> classroom)
    {
        int highestScore = getPercentage(classroom.get(0));
        Student top = classroom.get(0);
        
        for (Student curStud : classroom)
        {
            int score = getPercentage(curStud);
            
            if (score >= highestScore)
            {
                highestScore = score;
                top = curStud;
            }
            
        }
        
        
        return top.toString() + " Percentage = " + highestScore;
    }
}
