import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

/**
 * @author Arran Stewart
 *
 */
public class EncoderTest  {

    private static Random randGen = new Random();

    /**
     * Test method for {@link Encoder#sanitize(java.lang.String)}.
     */
    @Test
    public void testSanitize_empty() {
        String actual = Encoder.sanitize("");
        assertEquals("empty string => empty string", "", actual);
    }

    /**
     * Test method for {@link Encoder#sanitize(java.lang.String)}.
     */
    @Test
    public void testSanitize_allgood() {
        String actual = Encoder.sanitize("zxavHILQ");
        assertEquals("ZXAVHILQ", actual);
    }

    /**
     * Test method for {@link Encoder#sanitize(java.lang.String)}.
     */
    @Test
    public void testSanitize_somegood() {
        String actual = Encoder.sanitize("1,zxa2,??vH{}ILQ[]");
        assertEquals("ZXAVHILQ", actual);
    }

    /**
     * Test method for {@link Encoder#sanitize(java.lang.String)}.
     */
    @Test
    public void testSanitize_allbad() {
        String actual = Encoder.sanitize("1,2~/\t\n\r");
        assertEquals("", actual);
    }

    // sanitize:
    // property-test: is always shorter than input

    @Test
    public void testIntConversion_basic() {
        assertEquals( 1, Encoder.charToInt('A') );
        assertEquals( 2, Encoder.charToInt('B') );
        assertEquals( 26, Encoder.charToInt('Z') );

        assertEquals( 'A', Encoder.intToChar(1) );
        assertEquals( 'B', Encoder.intToChar(2) );
        assertEquals( 'Z', Encoder.intToChar(26) );
    }

    /**
     * Test method for {@link Encoder#charToInt(char)}.
     */
    @Test
    public final void testCharToInt_known() {
        String s = "DONOTUSEPC";
        int expected[] = new int[] { 4, 15, 14, 15, 20, 21, 19, 5, 16, 3 };
        for (int i = 0; i < s.length(); i++) {
            int actual = Encoder.charToInt( s.charAt(i) );
            String msg = String.format("difference at string pos %d", i);
            assertEquals(msg, actual, expected[i]);
        }
    }

    /**
     * Test method for {@link Encoder#intToChar(int)}.
     * Test numbers past 26.
     */
    @Test
    public final void testIntToChar() {
        assertEquals( 'A', Encoder.intToChar(27) );
        assertEquals( 'B', Encoder.intToChar(28) );
    }

    @Test
    public void testIntConversion_inverse() {
        for (char c = 'A'; c <= 'Z'; c++) {
            assertEquals(c, Encoder.intToChar(Encoder.charToInt(c)));
        }
    }

    /**
     * Test method for {@link Encoder#encodeString(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testEncodeChar() {
        char plainC = 'D';
        char keyC   = 'K';
        char expected  = 'O';
        char actual    = Encoder.encodeChar(plainC, keyC);
        //char actualX  = Encoder.intToChar(actual);
        String msg = String.format("known text. expected %c, actual %c", expected, actual);
        assertEquals(msg, expected, actual);
    }

    /**
     * Test method for {@link Encoder#encodeString(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testEncodeInverse() {
        for(char plainC = 'A'; plainC <= 'Z'; plainC++) {
            for(char keyC = 'A'; keyC <= 'Z'; keyC++) {
                char encodedC = Encoder.encodeChar(plainC, keyC);
                //char encodedC = Encoder.intToChar(encoded);
                char decodedC = Encoder.decodeChar(encodedC, keyC);
                //char decodedC = Encoder.intToChar(decoded);

                String msg = 
                    String.format("plain: %c, key: %c", plainC, keyC);
                assertEquals(msg, plainC, decodedC);
            }
        }

        char plainC = 'D';
        char keyC   = 'K';
        char expected  = 'O';
        char actualX    = Encoder.encodeChar(plainC, keyC);
        //char actualX  = Encoder.intToChar(actual);
        assertEquals("known text", expected, actualX);
    }

    /**
     * Test method for {@link Encoder#encodeString(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testEncodeString() {
        String plainText           = "DONOTUSEPC";
        String key                 = "KDWUPONOWT";
        String expectedCipherText  = "OSKJJJGTMW";
        String actual              = Encoder.encodeString(plainText, key);
        assertEquals("known text", expectedCipherText, actual);
    }

    /**
     * Test method for {@link Encoder#decodeString(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testDecodeString() {
        String cipherText        = "OSKJJJGTMW";
        String key               = "KDWUPONOWT";
        String expectedPlainText = "DONOTUSEPC";

        String actual    = Encoder.decodeString(cipherText, key);
        assertEquals("known text", expectedPlainText, actual);
    }

    /**
     * Test method for {@link Encoder#encodeString(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testEncodeDecode() {
        String plainText = "DONOTUSEPC";
        String key       = "KDWUPONOWT";
        String encoded   = Encoder.encodeString(plainText, key);
        String actual    = Encoder.decodeString(encoded, key);
        assertEquals("known text", plainText, actual);
    }

    /**
     * Test method for {@link Encoder#nextKeyChar()}.
     */
    @Test
    public void testNextKey() {
        char expectedVals[] = 
            { 'D','W', 'J', 'X', 'H', 'Y', 'R', 'F', 'D', 'G'  };				
        Encoder e = new Encoder();
        for(int i = 0; i < expectedVals.length; i++) {
            int key = e.nextKeyStreamChar();
            int expected = expectedVals[i];
            assertEquals(expected, key) ;
        }

    }

    /**
     * Test method for {@link Encoder#encrypt()}.
     */
    @Test
    public void testEncrypt() {
        String plainText = "AAAAAAAAAA";
        Encoder e = new Encoder();
        String expected = "EXKYIZSGEH";
        String actual = e.encrypt(plainText);
        assertEquals("known plaintext", expected, actual);
    }

    public static String randomString(int length) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; i++) {
            int letter = randGen.nextInt( Encoder.ALPHABET_SIZE );
            char c = (char) ('A' + letter);
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Test method for {@link Encoder#encrypt()}.
     * 
     * Given any key, encryption and decryption are inverses.
     */
    @Test
    public void testEncrypt_inverse() {

        // random string
        final int NUM_TIMES = 500;
        for(int i = 0; i < NUM_TIMES; i++) {
            for(int len = 5; len < 100; len += 5) {
                String s = randomString(len);
                String encrypted = new Encoder().encrypt(s);
                String decrypted = new Encoder().decrypt(encrypted);

                String msg = String.format("with random string '%s': ", s);
                assertEquals(msg, s, decrypted);
            }
        }
    }

    public static void main(String args[]) {
        // encrypted string 'BGVKJ':  expected:<[BGVKJ]> but was:<[NSHWV]>
        //String s = "BGVKJ";
        String s = "AAAAAAAAAA";
        String encrypted = new Encoder().encrypt(s);
        String decrypted = new Encoder().decrypt(encrypted);

        //String msg = String.format("with random string '%s': ", s);
        //assertEquals(msg, s, decrypted);

        System.out.println("encrypted: " + encrypted);
        System.out.println("decrypted: " + decrypted);
    }

}
