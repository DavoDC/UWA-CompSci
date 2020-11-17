package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import statgram.log.RouteStop;
import comm.BrowserHandler;
import comm.DatagramHandler;
import timetable.Timetable;

/**
 * A class that models a station server
 *
 * @author david
 */
public class Station {

    /**
     * Main method
     *
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {

        // Save arguments
        saveArguments(args);

        // Initialize modules
        initModules();

        // Start processing
        startProcessing();
    }

    /**
     * Save arguments into global variables
     *
     * @param args
     */
    private static void saveArguments(String[] args) {
        // Basic argument check
        if (args.length < 2) {
            throw new IllegalArgumentException();
        }

        // Save station name
        Globals.stName = args[0];

        // Save browser port
        Globals.browserPort = Integer.parseInt(args[1]);

        // Save station port
        Globals.stPort = Integer.parseInt(args[2]);

        // Save adjacent ports
        Globals.adjacentPorts = new ArrayList<>();
        for (int i = 3; i < args.length; i++) {

            // Convert to int
            Integer curPort = Integer.parseInt(args[i]);

            // Add to list
            Globals.adjacentPorts.add(curPort);
        }

        // Initialize source route stop
        Globals.thisRS = new RouteStop(Globals.stName, Globals.stPort);
    }

    /**
     * Initialize modules
     */
    private static void initModules() {

        try {
            // Initialize timetable
            Globals.timetable = new Timetable();

            // Initialize browser handler
            Globals.browserH = new BrowserHandler();

            // Initialize datagramHandler
            Globals.datagramH = new DatagramHandler();

        } catch (IOException ex) {
            Logger.getLogger(Station.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Start processing threads
     */
    private static void startProcessing() {

        // Schedule the datagram handling loop 
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                // Receive datagrams
                Globals.datagramH.receiveDatagrams();
            }
        }, 5);

        // Schedule neighbour info datagrams being sent (ONCE)
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                // Send neighbours info
                Globals.datagramH.sendNeighboursInfo();
            }
        }, 10);

        // Schedule the browser handling loop
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                // Receive browser requests
                Globals.browserH.receiveRequest();
            }
        }, 20);

        // After 5sec, make timetable update (if needed) every 5secs
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                // Update timetable if needed
                Globals.timetable.updateIfNeeded();
            }
        }, 5000, 5000);

    }
}
