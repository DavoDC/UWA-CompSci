
/**
 * 
 * Creates a ticketmachine session
 * 
 * The "session" increases user-machine interaction efficiency, 
 * by reducing the number of user actions required.
 * 
 * This is achieved by:
 * - Making a TicketMachine object automatically
 * - Having a method which calls multiple other required methods
 * 
 * 
 *  Created by David on 16/3/18
 * 
 */

public class Session
{
    
    
    //Constructor
    /**
     * Use this to interact with a ticket machine
     */
    public Session()
    {

    }

    //Methods
    //Make a ticketmachine to call the methods on
    TM TM1 = new TM();

    /**
     * Welcome to the BlueJ Line
     * 
     * 1. Enter your age group ("Child", "Adult" or "Elder")
     *    - Price is $1 for Children and Elders 
     *    - Price is $2 for Adults
     * 
     * 2. Insert an amount of money in dollars
     * 
     * 3. Enter how many tickets you want
     * 
     */

    public void a1Run(String agecateg, int paymentvalue, int ticketsdesired)
    {

        TM1.aDeterminePrice(agecateg);

        TM1.bInsertMoney(paymentvalue);

        TM1.cGetTickets(ticketsdesired);
        System.out.println("\n");

    }

    /**
     * Receive information about the current session
     */
    public void a2GetInfo()
    {
        System.out.println("\n");
        TM1.dPrintValues();
    }

    /**
     * Finish your interaction with the ticket machine
     * You will receive any unused credit
     * The ticket machine will be ready for a new customer
     */
    public void bFinish()
    {
        TM1.eFinishSession();
    }

    
    public TM cGetTM()
    {
        return TM1;
    }
    
   
}
