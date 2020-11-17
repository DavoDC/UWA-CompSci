
/**
 * A ticket machine, inspired by the "naive ticket machine project", but much more complex
 * 
 * Features:
 * 1. Price is determined by customer's "agegroup"
 * 2. Only sensible values are allowed into fields (e.g. no negative money/balance)
 * 3. A password-protected "technician" method that can change instance variables (not available in a session)
 *
 * Created by David on 8/3/2018
 * 
 */

public class TM
{

    //Special Fields
    private String agegroup; //stores the age category of the customer, for printing
    private int valid; //keeps a record of whether entries have been valid 

    //Money Fields
    private int price; //stores the price of tickets
    private int balance; //stores the customer's credit
    private int profit;  //stores the profit acquired by the TM in its lifetime

    //Ticket Field
    private int tickets; //records how many tickets have been bought

    //Constructor
    /**
     * Constructs a ticketmachine
     * All values are initialized to defaults, except price and age group
     *  
     * */

    public TM()
    {
        balance = 0;
        profit = 0;
        valid = 0;
        tickets = 0;

        price = 1;
        agegroup = "Has not been entered yet";

    }  


    public boolean xUseCheck()
    {
        if (agegroup.equals("Has not been entered yet"))
        { 
            return true;
        }

        return false;
    }

    //Methods
    /**
     * Determines the price of tickets based on the user's agegroup
     * 
     * Options =
     *  - Child
     *  - Adult
     *  - Elder
     * 
     */

    public void aDeterminePrice(String agegroup)
    {
        this.agegroup = agegroup;

        this.agegroup = this.agegroup.toLowerCase();

        if (agegroup.equals("child"))
        {
            price = 1;
            valid = 1;
        }
        else if(agegroup.equals("adult"))
        {
            price = 2;
            valid = 1;
        }
        else if (agegroup.equals("elder"))
        {
            price = 1;
            valid = 1;
        }
        else
        {
            System.out.println("Please enter a valid agegroup");
            valid = 0;
            this.agegroup = "Has not been entered yet";
        }

    }  

    /**
     * Allows the customer to insert amount of money in dollars
     */

    public void bInsertMoney(int payment)
    {
        if (payment <= 0) 
        {
            System.out.println ("You have not entered a valid amount of money");
        } 

        else if (payment > 0)
        {
            balance += payment;
            System.out.println("You have successfully entered money. Your credit is now $" + balance);
        }
    }

    /**
     * Allows a customer to pay for a desired amount of tickets
     */
    public void cGetTickets(int ticketswanted)
    {
        int cost = ticketswanted*price;

        if (valid != 1)
        {
            System.out.println("As your entry was not valid, no tickets will be purchased");
            System.out.println("    ");
        }
        else if (balance == 0)
        {
            System.out.println("Please insert some money first");
            System.out.println("Remember that the price of a ticket is $" + price);
            System.out.println("    ");
        }
        else if (ticketswanted <= 0)
        {
            System.out.println("Please enter a valid number of desired tickets" + "\n");
        }
        else if (balance < cost)
        {
            System.out.println("However, you don't have enough credit to buy " + ticketswanted + " ticket(s)");
            System.out.println("The ticket purchase will now be cancelled, and your credit will remain" + "\n");
            System.out.println("<<<Information>>>");
            System.out.println("You currently have $" + balance + " worth of credit,");
            System.out.println("and you have ordered " + ticketswanted + " ticket(s), at a price of $" + price);
            System.out.println("Thus, you would have to increase your credit by $" + (cost-balance));
        }
        else if (balance > 0)
        {
            tickets += ticketswanted;
            balance -= cost;
            profit += cost;

            System.out.println("You have received " + ticketswanted + " ticket(s), and $" + cost + " has been deducted");
            System.out.println("You have $" + balance + " worth of credit remaining");
        }
    }

    /**
     * Print out information about your session
     */

    public void dPrintValues()
    {
        System.out.println("\n" + "###System information###");
        System.out.println("  Customer data:");
        System.out.println("Your age group is = " + agegroup);
        System.out.println("Session credit = $" + balance);
        System.out.println("Tickets purchased in this session = " + tickets + "\n");
        System.out.println("  Other data:");
        System.out.println("Price of a ticket is currently $" + price);
        System.out.println("Profit acquired by machine is currently $" + profit);
    }

    /**
     * Complete your session with this ticketmachine.
     * You will receive any excess money (if present).
     */
    public void eFinishSession()
    {
        if (balance > 0)
        {
            System.out.println("Your excess money ($" + balance + ") has been refunded");
            balance = 0;
            tickets = 0;
            agegroup = "Has not been entered yet";
            System.out.println("This machine is ready for a new customer");
        }
        else
        {
            System.out.println ("There is no excess money");
        }
    }

    //Technician tool
    /** 
     * TECHNICIAN USE ONLY
     * 
     * 1. This restricted function requires a correct password 
     * 2. Enter the new price, balance and ticket number in that order
     *
     * *********************************************************************************
     */

    public void xTTComponent1 (String pw, int newprice, int newbalance,int newtickets)
    {

        if (pw.equals("Dragon20"))
        {
            System.out.println("Password was correct. Technican tool will now function");
            System.out.println("    ");

            price = newprice;
            balance = newbalance;
            tickets = newtickets;

            System.out.println("Technician maintenance sucessfully completed");
            System.out.println("The new values will now be shown");

            dPrintValues();

        }
        else
        {
            System.out.println("Password was incorrect.");
        }
    }

}
