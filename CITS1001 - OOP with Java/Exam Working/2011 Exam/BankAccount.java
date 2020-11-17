public class BankAccount 
{
    private String accName; // the account holder’s name, e.g. Bill Gates
    private int balance;
    
    public BankAccount(String accName, int balance)
    {
        this.accName = accName;
        this.balance = balance;
    }

    public String getAccName() 
    {
        return accName;
    }

    public int getBalance() 
    {
        return balance;
    }

}