
/**
 * Write a description of class BankTest here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class BankTest
{
    static Bank bank = new Bank();
    
    public static String Test()
    {
        return bank.findHighest(bank.getAccountA());
    }
    

}
