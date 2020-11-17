package statgram.types;

import main.Globals;
import statgram.StatGram;
import statgram.log.ConnLog;
import statgram.log.RouteLog;
import statgram.log.RouteStop;

/**
 * A statgram used by a station to inform neighbours about itself
 *
 * @author David
 */
public class InfoGram extends StatGram {

    /**
     * Make default infogram
     */
    public InfoGram() {

        // Call statgram constructor
        super("Temp",
                StatGram.Status.LookingForDest,
                Globals.thisRS,
                new RouteStop("Unknown", -1),
                new RouteLog(true),
                new RouteLog(false),
                new RouteLog(false),
                new ConnLog());

        // Set type
        super.setType(getClass().getSimpleName());
    }
}
