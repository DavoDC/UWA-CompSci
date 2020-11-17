import java.awt.*;
/**
 * A TextAnalyser object takes text (either directly as a String, or read in from a file), 
 * and it maintains a count of the occurrences of each letter in that text. 
 * 
 * So e.g. analysing the text "A tap tot" would mean adding 
 * 1 each to the counts for O and P, 
 * 2 to the count for A, and 
 * 3 to the count for T. 
 * 
 * Letters are regarded as case-insensitive (so e.g. count bs and Bs together). 
 * Non-letter characters are ignored. 
 * The counts for each text analysed are added together until a reset operation is invoked. 
 * The class provides various methods for interrogating the data. 
 * 
 * @author Lyndon While 
 * @version 1.1 
 */

public class TextAnalyser
{
    private int letters;     // the total number of letters analysed 
    private int[] frequencies; // the number of occurrences of each letter of the alphabet (case-insensitive)

    public TextAnalyser()
    {
        letters = 0;
        frequencies = new int[26];
    }

    public TextAnalyser(String initial)
    {
        letters = 0;
        frequencies = new int[26];
        analyse(initial);
    }

    public void aGetInfo()
    {
        System.out.println("Letters processed = " + letters);

        for (int element : frequencies)
        {
            System.out.print (element + "  " );
        }
    }

    // analyse the String s 
    // add to each count in frequencies the number of occurrences of each letter in s
    // update letters 
    // remember that non-letter characeters must be ignored
    public void analyse (String s)
    {
        s = s.replaceAll("[0-9]","").replaceAll("[^A-Za-z]","").replaceAll(" ","");
        s = s.toLowerCase();

        int length = s.length();
        int counter = 0;

        while (length > 0)
        {
            //Get character
            char curchar = s.charAt(counter);

            //Add 1 to correct box
            frequencies[converter(curchar)]++;

            counter++;
            letters++;
            length--;
        }

    }

     // return the number of occurrences of the letter c that have been seen since the last reset 
    public int frequency (char c)
    {
        return frequencies[converter(c)];
    } 
    
    // return the number of letters that have been seen since the last reset 
    public int lettersAnalysed()
    {
        return letters;
    }

    // Converts a character to its numerical ASCII value
    public int converter(char a)
    {
        return ((int) a - 97);
    }

    // return the most common letter seen since the last reset 
    // if no letters have been analysed, return '?' 
    public char mostFrequent ()
    {
        int maxfreq = 0;
        int maxpos = 0;

        for (int index = 0 ; index < frequencies.length ; index++)
        {
            int curfreq = frequencies[index];

            if (maxfreq < curfreq)
            {
                maxfreq = curfreq;
                maxpos = index;
            }
        }

        char maxchar = (char) (maxpos + 97);

        if (letters == 0)
        {
            return '?';
        }

        return maxchar;
    }

    // clear the instance variables 
    public void reset() 
    {
        letters = 0;
        frequencies = null;
        frequencies = new int [26];
    }

    // analyse the contents of the file f 
    // read in the file as a FileIO object, then analyse the contents 
    public void analyseFile (String f)
    {
        String input = "";

        for (String cur : (new FileIO(f).lines))
        {
            input += cur;
        }

        analyse(input);
    }

    // use Oblongs to draw a histogram of the frequencies 
    // create an Oblong for each bar of the histogram, and arrange them appropriately on the screen 
    // Oblong constructor = xPosition , yPosition, width , height , color
    public void histogram()
    {
        int xPos = 20;
       
        for (int curfreq : frequencies)
        {
            Oblong column = new Oblong(xPos ,50, 10 , -curfreq , "blue");
            column = null;
            xPos += 15;
        }
        
    
    }

    public String test(String s)
    {
        s = s.replaceAll("[^A-Za-z]","");
        return s;
    }
}
