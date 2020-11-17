package main;

/**
 * Q1
 *
 * @author David
 */
public class Question1 {

    private String t = "99799739976392993993993993997639"
            + "97997399763793991639976399";
    private String p = "99799739976399";

    private void checkForPattern() {
        System.out.println("test: " + t.contains(p));
    }

    private void printTextIndices() {
        for (int i = 0; i < t.length(); i++) {
            System.out.print(i + " = '" + t.charAt(i) + "'  ");
        }
    }

    private void printBadCharTable() {

        for (int i = 0; i < 10; i++) {

            char alpha = ("" + i + "").charAt(0);

            for (int j = p.length() - 1; j != -1; j--) {

                char comp = p.charAt(j);
                if (comp == alpha) {
                    System.out.println("char in p:" + comp + " pos:" + j);
                    break;
                }
            }
        }
    }
}
