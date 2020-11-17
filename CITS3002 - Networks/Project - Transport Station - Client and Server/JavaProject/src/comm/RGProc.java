package comm;

import main.Globals;
import statgram.StatGram;
import static statgram.StatGram.Status.LookingForDest;
import statgram.log.RouteLog;
import statgram.log.RouteStop;

/**
 * Processes route grams
 *
 * @author David
 */
public class RGProc {

    /**
     * Process a given route gram
     *
     * @param sg
     */
    public void processRG(StatGram sg) {

        // Do not process if adjacent list not init
        if (Globals.adjList.isEmpty()) {
            return;
        }

        // If routegram is looking for dest
        if (sg.getStatus().equals(LookingForDest)) {

            // Process as a 'searching' routegram
            processSearchingRG(sg);

        } else {

            // Else, if it is looking for its source.
            // process as a backtracking routegram
            processBackTrackingRG(sg);
        }
    }

    /**
     * Process a searching routegram
     */
    private void processSearchingRG(StatGram newSG) {

        // Get destination stop and name
        RouteStop destRS = newSG.getDestStop();
        String destName = destRS.getName();

        // Update avoidlog (6) to indicate we shouldn't visit this 
        // stop for any more info
        newSG.getAvoidLog().addRS(Globals.thisRS);

        // Update routelog (5) to indicate we have visited this stop
        newSG.getRouteLog().addRS(Globals.thisRS);

        // If we have reached the destination 
        if (Globals.thisRS.equals(destRS)) {

            // Process as "arrived" routegram
            processArrivedRG(newSG);
        } else {

            // Else we are adjacent to the destination or far from it
            // Process as "further" routegram
            processFurtherRG(newSG, destName, destRS);
        }
    }

    /**
     * Process a routegram that has arrived at its destination
     *
     * @param newSG
     */
    private void processArrivedRG(StatGram newSG) {

        // Update status (2) to indicate we are no longer looking 
        // for the destination, but for the source
        newSG.setStatus(StatGram.Status.LookingForSrc);

        // Add routelog backwards to backtracking log (7)
        RouteLog rl = newSG.getRouteLog();
        for (int i = rl.size() - 1; i >= 0; i--) {
            newSG.getBTLog().addRS(rl.get(i));
        }

        // Send SG on backtracking journey 
        Globals.datagramH.backtrackSG(newSG);
    }

    /**
     * Process an RG is that one or more stops from the destination
     *
     * @param newSG
     * @param destName
     * @param destRS
     */
    private void processFurtherRG(StatGram newSG, String destName,
            RouteStop destRS) {

        // Extract port from neighbor directory
        Integer destPort = Globals.nbDir.get(destName);

        // Check if destination is adjacent
        boolean isDestAdj = Globals.isAdjacent(destName);

        // If we are adjacent to the destination
        if (isDestAdj) {

            // We have the information to update the destination
            // Update port in dest stop copy and load into SG
            destRS.setPort(destPort);
            newSG.setDestRS(destRS);

            // Update connection log (Add next best connection)
            newSG.addToConnLog(destPort);

            // We can send the new statgram 
            // directly to the destination port
            Globals.datagramH.sendStatgram(newSG, destPort);

        } else {
            // Else if we are far from the destination
            // Send the SG on neighbouring ports, avoiding some
            Globals.datagramH.sendSGtoNeighbors(newSG, true, true);
        }
    }

    /**
     * Process back tracking routegram
     *
     * @param sg
     */
    private void processBackTrackingRG(StatGram sg) {

        // Remove first entry from backtracking log
        if (!sg.getBTLog().isEmpty()) {
            sg.getBTLog().remove(0);
        }

        // If backtracking log is now empty
        if (sg.getBTLog().isEmpty()) {

            // We have finished backtracking!!
            // Let discoverRoute() in Datagram Handler handle it
            return;
        }

        // Send SG on backtracking journey
        Globals.datagramH.backtrackSG(sg);
    }
}
