
public class Fitness
{
    String name;
    int age;
    int weight;
    int height;

    public Fitness (String name, int age, int weight, int height)
    {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }
    
    public Fitness()
    {
        name = "Person";
        age = 30;
        weight = 70;
        height = 170;
    }

    public boolean firstNameIs (String fname)
    {
        String actual = name.split(" ")[0];
        if (fname.equals(actual))
        {
            return true;
        }
        return false;
    }
    
    public int bmi()
    {
        double metres = (double) height/100;
        double hsq = Math.pow(metres ,2);
        double result = weight / hsq;
        return (int) Math.round(result);
    }
    
    public int bmitest(int weight, int height)
    {
        this.weight = weight;
        this.height = height;
        
        return bmi();
    }
    
    public boolean bmiNormal()
    {
        if (bmi() > 18.5 || bmi() < 25)
        {
            return true;
        }
        return false;
    }
    
}