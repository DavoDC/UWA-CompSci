package statgram.types;

import main.Globals;
import statgram.StatGram;
import statgram.log.ConnLog;
import statgram.log.RouteLog;
import statgram.log.RouteStop;

/**
 * A statgram used in determining routes implied by user queries
 *
 * @author David
 */
public class RouteGram extends StatGram {

    /**
     * Make default routegram
     *
     * @param destName
     * @param avoidLog
     */
    public RouteGram(String destName, RouteLog avoidLog) {

        // Call statgram constructor
        super("Temp",
                StatGram.Status.LookingForDest,
                Globals.thisRS,
                new RouteStop(destName, -1),
                new RouteLog(true),
                avoidLog,
                new RouteLog(false),
                new ConnLog());

        // Set type
        super.setType(getClass().getSimpleName());
    }
}
