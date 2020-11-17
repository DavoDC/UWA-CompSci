import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * This "TrainStation" allows for multiple ticketmachine sessions to be accessed at once
 * 
 *  Created by David on 25/3/18
 * 
 */
public class TrainStation
{
    ArrayList<Session> Station = new ArrayList<>();

    public TrainStation()
    {
        //Add anonymous session object
        Station.add(new Session());

        //Add "used" session
        Session sesh1 = new Session();
        Station.add(sesh1);
        sesh1.a1Run("Adult", 50, 10);

        //Add "used" session
        Session sesh2 = new Session();
        Station.add(sesh2);
        sesh2.a1Run("Child", 10, 2);
        
    }

    /**
     * Add a session to the station
     */
    public void aAddSession(Session seshname)
    {    
        Station.add(seshname);
    }

    /**
     * Remove a session from the station
     */
    public void bRemoveSession(Session seshname)
    {    
        Station.remove(seshname);
    }

    //For each printer
    /**
     * Print out information for all sessions
     */
    public void cFPrintAllSessions()
    {
        int position = 0;

        for (Session entry : Station)
        {
            ++position;
            System.out.println("\n" + "Session number = " + position);
            entry.a2GetInfo();
            System.out.println("\n");

        }
    }

    //Iterator version of above method
    public void cIPrintAllSessions()
    {
        Iterator<Session> it = Station.iterator();

        while (it.hasNext())
        {
            it.next().a2GetInfo();
        }
    }
    
    public void eRemoveUnused()
    {
        Iterator<Session> iter = Station.iterator();
                   
        while (iter.hasNext())
        {
            Session cursesh = iter.next();
            boolean check = cursesh.cGetTM().xUseCheck();
            
            if (check == true)
            {
                iter.remove();
                System.out.println("A Session was removed");
            }
        }
          
        }
        
    }

