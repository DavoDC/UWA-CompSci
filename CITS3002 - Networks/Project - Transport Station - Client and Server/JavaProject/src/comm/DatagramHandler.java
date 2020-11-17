package comm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Globals;
import main.Station;
import statgram.StatGram;
import statgram.log.ConnLog;
import statgram.log.RouteLog;
import statgram.log.RouteStop;
import statgram.types.InfoGram;
import statgram.types.RouteGram;
import timetable.Connection;

/**
 * Handles inter-station server communication (via UDP)
 *
 * @author David
 */
public class DatagramHandler {

    // Station socket
    private final DatagramSocket stationS;

    // Route gram processor
    private final RGProc rgp;

    // If time to find route has expired
    boolean timeExpired = false;

    /**
     * Create datagram handler
     *
     * @throws java.net.SocketException
     */
    public DatagramHandler() throws SocketException {

        // Create datagram socket for station
        stationS = new DatagramSocket(Globals.stPort);

        // Initialize RGP
        rgp = new RGProc();
    }

    /**
     * Handle the receiving of datagrams
     */
    public void receiveDatagrams() {

        try {

            // Get statgram
            StatGram sg = getStatGram();

            // Process according to type
            switch (sg.getType()) {

                // Process infogram
                case "InfoGram":

                    // Get source of infogram
                    RouteStop src = sg.getSrcStop();

                    // Add to adjacent stop list
                    Globals.adjList.add(src);

                    // Add to neighbour directory
                    Globals.nbDir.put(src.getName(), src.getPort());
                    break;

                // Process routegram
                case "RouteGram":
                    rgp.processRG(sg);
                    break;
            }
        } catch (SocketException ex) {
            Logger.getLogger(Station.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Station.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Do again after a short delay
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                receiveDatagrams();
            }
        }, 10);
    }

    /**
     * Send a statgram on given port
     *
     * @param sg
     * @param port
     */
    public void sendStatgram(StatGram sg, int port) {

        try {
            // Get payload
            String payload = sg.toString();

            // Convert to bytes
            byte[] data = payload.getBytes();

            // Get length
            int len = payload.length();

            // Get IP address object
            InetAddress ip = InetAddress.getLocalHost();

            // Create datagram packet
            DatagramPacket datagram;
            datagram = new DatagramPacket(data, len, ip, port);

            // Send datagram packet out from station socket
            stationS.send(datagram);

        } catch (UnknownHostException ex) {
            Logger.getLogger(Station.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Station.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Discover multi-station route by communicating with other stations
     *
     * @param destName
     * @return
     */
    public String discoverRoute(String destName) {

        // Holder
        String result = "";

        try {

            // Final result holder
            StatGram good = null;
            ConnLog cl = null;
            String clStr = null;

            // If nothing is found after a long time,
            // it is assumed that there are no valid routes
            // A heuristic of 2 seconds for each adjacent port is used
            // After the expiry time, set time expired to true
            timeExpired = false;
            long expiry = Globals.adjacentPorts.size() * 2000;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    timeExpired = true;
                }
            }, expiry);

            // Keep processing many different returning routegrams
            while (true) {

                // Make avoid log
                RouteLog avoidLog = new RouteLog(true);

                // Add adjacent stops
                avoidLog.addAll(Globals.adjList);

                // Make routegram
                RouteGram rg = new RouteGram(destName, avoidLog);

                // Send to all adjacent stations indiscriminately
                sendSGtoNeighbors(rg, false, true);
                sendSGtoNeighbors(rg, false, true); // Failsafe

                // Wait to receive statgram reply
                StatGram curSG = getStatGram();

                // Clean BT log
                if (!curSG.getBTLog().isEmpty()) {
                    curSG.getBTLog().remove(0);
                }

                // Extract connection log
                ConnLog curCl = curSG.getConnLog();
                String curClStr = curCl.toString();

                // If valid route was found (!!!)
                if (!curClStr.contains("UNAVAIL")) {

                    // Save
                    good = curSG;
                    cl = curCl;
                    clStr = curClStr;

                    // Stop looking 
                    break;
                } else if (timeExpired == true) {

                    // Else if time has expired,
                    // stop looking
                    break;
                }
            }

            // If nothing was found
            if (good == null || cl == null || clStr == null) {

                // Notify
                Globals.printD("\nNothing was found");

                // Return that route is not available
                return "UNAVAIL";
            }

            // Print out connections of valid route
            Globals.printD("\nConnections in Final Route:");
            String[] clStrParts = clStr.split(ConnLog.sep);
            for (String conn : clStrParts) {
                Globals.printD(" >" + conn);
            }

            // Get first and last connection
            Connection first = cl.get(0);
            Connection last = cl.getLast();

            // Extract final result
            result += "At " + first.getDepTime().toString() + ",";
            result += " catch " + first.getMode();
            result += ", from " + first.getStartPoint() + ". ";
            result += "You will arrive at your final destination at ";
            result += last.getArrivalTime().toString();

        } catch (IOException ex) {
            Logger.getLogger(DatagramHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Return result
        return result;
    }

    /**
     * Send a given statgram to all neighboring stations
     *
     * @param sg
     * @param useAvoidLog
     * @param updateConnLog
     */
    public void sendSGtoNeighbors(StatGram sg,
            boolean useAvoidLog, boolean updateConnLog) {

        // If using avoid log is desired
        if (useAvoidLog) {

            // If adjacent list is not initialized
            if (Globals.adjList.isEmpty()) {

                // Stop processing
                return;
            }

            // For every adjacent stop
            for (RouteStop adjStop : Globals.adjList) {

                // If the avoid log does not contain that stop
                if (!sg.getAvoidLog().contains(adjStop)) {

                    // Make statgram copy
                    StatGram newSG = new StatGram(sg.toString());

                    // Get port
                    int port = adjStop.getPort();

                    // If update connection log desired, do so
                    if (updateConnLog) {
                        newSG.addToConnLog(port);
                    }

                    // Send statgram to stop
                    sendStatgram(newSG, port);
                }
            }
        } else {
            // Else, send to all neigbours indiscriminately
            // For each adjacent port
            for (int curAdjPort : Globals.adjacentPorts) {

                // Make statgram copy
                StatGram newSG = new StatGram(sg.toString());

                // If update connection log desired, do so
                if (updateConnLog) {
                    newSG.addToConnLog(curAdjPort);
                }

                // Send statgram
                sendStatgram(newSG, curAdjPort);
            }
        }
    }

    /**
     * Get incoming statgram
     *
     * @return
     * @throws IOException
     */
    private StatGram getStatGram() throws IOException {

        // Datagram holder
        byte[] buf = new byte[Globals.DATAGRAM_SIZE];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);

        // Wait for datagram
        stationS.receive(dp);

        // Load as statgram
        StatGram sg = new StatGram(dp);

        // Return statgram
        return sg;
    }

    /**
     * Send a statgram on a backtracking journey
     *
     * @param sg
     */
    public void backtrackSG(StatGram sg) {

        // If empty, notify
        if (sg.getBTLog().isEmpty()) {
            String msg = "backtrackSG: Empty btLog";
            throw new IllegalArgumentException(msg);
        }

        // Get port of the first stop in the backtracking log
        int next = sg.getBTLog().get(0).getPort();

        // Send to that port
        sendStatgram(sg, next);
    }

    /**
     * Send an infogram to neighbours so they can build neigbour directory
     */
    public void sendNeighboursInfo() {

        // Send default infogram indiscriminately to all neighbours
        sendSGtoNeighbors(new InfoGram(), false, false);
    }
}
