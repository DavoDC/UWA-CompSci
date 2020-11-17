package main;

import java.io.*;
import java.util.ArrayList;

/**
 * Reads a file into an ArrayList of strings.
 *
 * Simply create a FileIO object with a valid file name, and it will read the
 * contents of the file into the data structure lines.
 *
 * e.g. reading the file f.txt containing "abc\nde\n\ngh\n" will return an
 * object with filename = "f.txt" lines = <"abc", "de", "", "gh">
 *
 * Lyndon While, 2020
 */
class FileIO {

    // The name of the file 
    private String filename;

    // The contents of the file
    private ArrayList<String> lines;

    /**
     * Creates an object representing filename and its contents.
     */
    public FileIO(String filename) {
        this.filename = filename;
        try {
            // Open the file
            FileInputStream fstream = new FileInputStream(filename);

            // Convert fstream to a DataInputStream
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(fstream));

            // Read lines one at a time
            lines = new ArrayList<>();
            while (in.ready()) {
                lines.add(in.readLine());
            }

            // Close the data stream
            in.close();
        } catch (Exception e) {
            System.err.println("File input error reading " + filename);
        }
    }

    /**
     * Returns the name of the file read.
     */
    public String getName() {
        return filename;
    }

    /**
     * Returns the contents of the file read.
     */
    public ArrayList<String> getLines() {
        return lines;
    }
}
