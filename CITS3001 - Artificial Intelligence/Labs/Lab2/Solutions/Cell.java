/**
 * A cell in the table for the DP LCS algorithm
 */
public class Cell
{
    // the length of the LCS for these two substrings
    private int len;
    // the rule used to construct the data in this cell
    private Direction dir;
    
    public Cell(int len, Direction dir)
    {
        this.len = len;
        this.dir = dir;
    }
    
    public int getLen()
    {
        return len;
    }
    
    public Direction getDir()
    {
        return dir;
    }
    
    public String print()
    {
        return len + "," + dir.ordinal() + " ";
    }
}
