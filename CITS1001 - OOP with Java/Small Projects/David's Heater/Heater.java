
public class Heater
{

    //Fields
    private double temp;

    //Constructor
    public Heater()
    {
        temp = 0.0;
    }

    //Methods

    public void ChangeTemp(double change)
    {
        temp += change;

        if (change > 0)
        {
            System.out.println("Temperature has been raised to " + temp + " degrees Celsius");
        }
        else
        {
            System.out.println("Temperature has been lowered to " + temp + " degrees Celsius");
        }

    }

    public void Cycle()
    {

        int[] numbers = 
            {0,5,10,15,20,15,10,5,0};

        for (int temp : numbers)
        {
            System.out.println ("Temperature is " + temp + " degrees Celsius");
        }

    }

    public void Print10()
    {
        int number = 1;

        do 
        {
            System.out.println(number);
            number++;
        }
        while (number < 11);

    }

    public int Mystery (int k)
    {
        int z = k % 10;
        while (k >= 10)
        {
            z = k % 10;
            k = k / 10;
        }
        return z;
    }
}