import java.util.HashMap;
import java.util.Set;

public class Methods
{

    public static int wordCount(String text)
    {
        return text.split(" ").length;
    }

    public static String getShortName(String input)
    {
        String[] words = input.split(" ");
        String output = "";
        for (int i = 0; i < words.length-1; i++)
        {
            output += words[i].charAt(0) + ". ";
        }
        output += words[words.length-1];

        return output;
    }

    public static HashMap<String, Integer> movies = new HashMap<>();

    public static String mostDownloaded()
    {
        movies.put("Jaw" , 50);
        movies.put("Chappie", 1000);
        movies.put("XMen" , 600);
        
        String movie = null;
        int highest = 0;

        for (String s : movies.keySet())
        {
            int downloads = movies.get(s);
            
            if (downloads > highest)
            {
                movie = s;
                highest = downloads;
            }
            
        }

        return movie;
    }
    
    public static boolean palindrome(String str)
    {
        str = str.replaceAll("[^A-Za-z]" , "");
        
        if (str.length() == 1)
        {
            return true;
        }
        
        String reverse = "";
        for (int i = str.length()-1; i != -1 ; i--)
        {
            reverse += str.charAt(i);
        }
        
        if (reverse.equals(str))
        {
            return true;
        }
        
        return false;
    }
}
