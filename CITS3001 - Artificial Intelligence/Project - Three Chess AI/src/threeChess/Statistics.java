package threeChess;

import java.io.PrintStream;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;

/**
 * A private class for representing the statistics of an agent in a tournament.
 * *
 */
public class Statistics implements Comparable {

    // Agent
    private final Agent agent;

    // Game stats
    private int won;
    private int lost;
    private int played;

    // Time stats
    private int totalTimeUsed;
    private int maxTimeUsed;
    private int minTimeUsed;

    /**
     * Constructs a statistics object for the given agent
     *
     *
     * @param a
     */
    public Statistics(Agent a) {

        // Save agent
        agent = a;

        // Initialize stats
        won = 0;
        lost = 0;
        played = 0;
        totalTimeUsed = 0;
        maxTimeUsed = Integer.MIN_VALUE;
        minTimeUsed = Integer.MAX_VALUE;
    }

    /**
     * Print heading
     *
     * @param logger
     */
    public static void printHeading(PrintStream logger) {

        // Holder
        String h = "";

        // Add initial columns
        h += "Rank\t";
        h += "Agent\t";

        // Addd game columns
        h += "Won\t";
        h += "Lost\t";
        h += "WinRate\t";

        // Add score
        h += "AvgSc\t";

        // Add time columns
        h += "MinTime\t";
        h += "AveTime\t";
        h += "MaxTime\t";

        // Add extra
        h += "Played";
        h += "\n";

        // Print
        logger.println(h);
    }

    public void printInfoRow(int rank, PrintStream logger) {

        // Holder
        String info = "";

        // Add rank
        info += rank;
        info += "\t";

        // Add agent name
        String agentName = agent.toString();
        info += agentName.substring(0, 4);
        info += "\t";

        // Add games won
        info += won;
        info += "\t";

        // Add games lost
        info += lost;
        info += "\t";

        // Add win rate percentage
        info += ((int) (won * 100.0f) / played) + "%";
        info += "\t";

        // Add rounded average
        info += "" + Math.round(average() * 100.0) / 100.0;
        info += "\t";

        // Add minmimum time used
        info += minTimeUsed + "ms";
        info += "\t";

        // Add average time used
        info += ((int) (totalTimeUsed * 1.0f) / played) + "ms";
        info += "\t";

        // Add maximum time used
        info += maxTimeUsed + "ms";
        info += "\t";

        // Add games played
        info += played;
        info += "\t";

        // Add new line
        info += "\n";

        // Print info string
        logger.println(info);
    }

    /**
     * Updates the Statistics object
     *
     * @param res
     */
    public void update(ArrayList<Object> res) {

        // Get score and process
        int score = (int) res.get(0);
        switch (score) {
            case -2: // Illegal move
                lost += 2;
                break;
            case -1: // Loss
                lost++;
                break;
            case 1: // Win
                won++;
                break;
            default:
        }

        // Get time used and process
        int timeUsed = (int) res.get(1);
        totalTimeUsed += timeUsed;
        maxTimeUsed = max(maxTimeUsed, timeUsed);
        minTimeUsed = min(minTimeUsed, timeUsed);

        // Increase number of times played
        played++;
    }

    /**
     * @return a JSON representation of the Statistics for an agent
     */
    @Override
    public String toString() {
        return agent
                + " ="
                + " won:" + won
                + ", lost:" + lost
                + ", played:" + played
                + ", avg:" + average();
    }

    /**
     * @param o the object to compare to.
     * @return -1 if this object has a higher average than the parameter, 0 if
     * the averages are equivalent and +1 if it has a lower average score.
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof Statistics) {
            Statistics stats = (Statistics) o;
            return Double.compare(stats.average(), average());
        } else {
            return -1;
        }
    }

    /**
     * @return average score of player
     */
    public double average() {
        return (1.0 * (won - lost)) / played;
    }
}
