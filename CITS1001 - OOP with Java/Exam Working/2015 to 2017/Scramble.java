import java.util.Random;

public class Scramble
{

    public static void aScramble(String input)
    {
        int len = input.length();

        if (len < 3 && len != 3)
        {
            throw new IllegalArgumentException();
        }

        String firstL = "" + input.charAt(0) + "";
        String middle = jumble(input.substring(1 , len - 1)) ;
        String lastL = "" + input.charAt(len-1) + "";

        String output = (firstL + middle + lastL);
        
        System.out.println("Output was = " + output + "\n");
        
        bValidityCheck(input,output);
    }

    private static String jumble(String input)
    {
        Random RNG = new Random();
        String jumbled = "";
        char[] array = input.toCharArray();

        for (int i = 0; i < 101; i++)
        {
            int rspot = RNG.nextInt(input.length());
            int rdest = RNG.nextInt(input.length());
            array = swap(array, rspot, rdest);
        }

        for (char c : array)
        {
            jumbled += c;
        }

        return jumbled;
    }

    private static char[] swap(char[] arr, int spot, int dest)
    {
        char temp = arr[dest];
        arr[dest] = arr[spot];
        arr[spot] = temp;
        return arr;
    }
    
    public static void bValidityCheck(String orig, String jumbled)
    {
        if (orig.length() != jumbled.length())
        {
            System.out.println("Invalid. Lengths are different");
        }
        
        boolean correct = true;
        int jumbpos = 0;
        for (char c : orig.toCharArray())
        {
            if (!jumbled.contains("" + c + ""))
            {
                correct = false;
            }
            jumbpos++;
        }
        
        if (correct)
        {
            System.out.println("Correct. Lengths are the same and characters are equivalent");
        }
        else 
        {
            System.out.println("Invalid. Doesn't contain same characters");
        }
    }
}
