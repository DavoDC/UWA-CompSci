

public class Ticket
{
    
    public static int ticketPrice(String status, boolean isTuesday, boolean beforeSix)
    {
        if (isTuesday)
        {
            if (beforeSix)
            {
                return 850;
            }
            else
            {
                return 1050;
            }
        }
        
        status = status.toLowerCase();
        
        switch (status)
        {
            case "adult" : return 1650;
            case "subscriber" : return 1200;
        }
        
        if (status.equals("senior") || status.equals("pensioner") || status.equals("child"))
        {
            return 1100;
        }
        
        return -1;
    }
}
