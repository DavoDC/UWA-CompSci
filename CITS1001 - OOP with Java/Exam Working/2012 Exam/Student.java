
public class Student
{
    private String name;
    private int mark;
    private int maxscore;

    public Student (String name, int mark, int max)
    {
        this.name = name;
        this.mark = mark;
        maxscore = max;
    }

    public int getMark()
    {
        return mark;
    }

    public int getMax()
    {
        return maxscore;
    }
    
    public void addBonus(int bonus)
    {
        mark += bonus;
    }

    public String toString()
    {
        String output = "Name = " + name + ", ";
        output += "Score = " + mark + " out of " + maxscore;
        return output;
    }
}
