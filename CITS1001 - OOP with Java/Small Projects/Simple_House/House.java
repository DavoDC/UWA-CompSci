
/**
 * Models a House
 *
 * @author David
 * @version 1.0
 */
public class House
{
    private int streetNumber;
    private String streetName;
    private int residentCount;

    /**
     * Constructor
     */
    public House(int streetNumber, String streetName, int residentCount) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.residentCount = residentCount;
    }

    public void getInfo() {
        String msg = "### House Information ### \n";
        msg += " - Street Number: " + streetNumber + "\n";
        msg += " - Street Name: " + streetName + "\n";
        msg += " - Resident Count: " + residentCount + "\n";
        System.out.println(msg);
    }
}
