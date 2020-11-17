
/**
 * Write a description of class Bank here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Bank
{
    private BankAccount[] accounts = new BankAccount[4];
    
    public Bank()
    {
        accounts[0] = new BankAccount("Jim" , 4000);
        accounts[1] = new BankAccount("Eric" , 3000);
        accounts[2] = new BankAccount("Tony" , -100);
        accounts[3] = new BankAccount("Zig" , 5000);
    }

    public BankAccount[] getAccountA()
    {
        return accounts;
    }
    
    public String findHighest(BankAccount[] accList)
    {
        String nameOfRichest = "";
        int curMax = accounts[0].getBalance();
        
        for (BankAccount curBA : accList)
        {
            if (curMax <= curBA.getBalance())
            {
                curMax = curBA.getBalance();
                nameOfRichest = curBA.getAccName();
            }
        }
        
        return nameOfRichest;
    }
    
}
