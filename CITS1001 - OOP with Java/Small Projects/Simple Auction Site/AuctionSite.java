import java.util.ArrayList;
import java.util.HashSet;

public class AuctionSite
{
    
    ArrayList<Items> Catalogue = new ArrayList<>();

    public AuctionSite()
    {
        
    }

    public void aAddItem(Items itemname)
    {
        Catalogue.add(itemname);
    }

    public void bRetrieveItem(int desiredindex)
    {
        String itemname = Catalogue.get(desiredindex).getname();
        System.out.println(itemname);

    }

}