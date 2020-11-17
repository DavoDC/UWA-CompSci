package threeChess;

import java.io.*;
import java.util.*;

import threeChess.agents.*;

/**
 * Class with static methods for running tournaments and playing threeChess
 * matches.
 *
 * @author Tim French
 */
public class ThreeChess {

    // To randomise play order
    private final static int[][] perms
            = {{0, 1, 2}, {0, 2, 1}, {1, 0, 2},
            {1, 2, 0}, {2, 0, 1}, {2, 1, 0}};
    private final static Random random = new Random();

    /**
     * Main
     *
     * @param args
     */
    public static void main(String[] args) {

        // Heading
        System.out.println("########### Three Chess Test ###########");

        // Bot sets
        ArrayList<Agent> bots = new ArrayList<>();

        // Options
        //bots.add(new Agent22477478());
        //bots.add(new CopyCatAgent());
        //bots.add(new GUIAgent("Human"));
        //bots.add(new GreedyAgent());
        //bots.add(new ManualAgent());
        //bots.add(new MaxNAgent());
        //bots.add(new ParanoidAgent());
        //bots.add(new RandomAgent());
        //
        //
        //
//        // @@@@@@@@ Preliminaries
//        // Set 1
//        bots.add(new RandomAgent());
//        bots.add(new RandomAgent());
//        bots.add(new RandomAgent());
////
////        // Set 2
//        bots.add(new GreedyAgent());
//        bots.add(new RandomAgent());
//        bots.add(new RandomAgent());
////
//        // Set 3
//        bots.add(new MaxNAgent());
//        bots.add(new RandomAgent());
//        bots.add(new RandomAgent());
//
//        // Set 4
//        bots.add(new ParanoidAgent());
//        bots.add(new RandomAgent());
//        bots.add(new RandomAgent());
//
//
//        // @@@@@@@@ Viability
//        // Set 1
//        bots.add(new GreedyAgent());
//        bots.add(new GreedyAgent());
//        bots.add(new GreedyAgent());
//
//        // Set 2
//        bots.add(new MaxNAgent());
//        bots.add(new GreedyAgent());
//        bots.add(new GreedyAgent());
//
//        // Set 3
//        bots.add(new ParanoidAgent());
//        bots.add(new GreedyAgent());
//        bots.add(new GreedyAgent());
//
//
        // @@@@@@@@ MaxN Versus Paranoid
        // Set 1
//        bots.add(new MaxNAgent());
//        bots.add(new ParanoidAgent());
//        bots.add(new ParanoidAgent());
//
//        // Set 2
//        bots.add(new ParanoidAgent());
//        bots.add(new MaxNAgent());
//        bots.add(new MaxNAgent());
//
//
        // @@@@@@@@ Tournament Agent Testing
        bots.add(new Agent22477478());
        bots.add(new RandomAgent());
        bots.add(new RandomAgent());
//        bots.add(new Agent22477478());
//        bots.add(new GreedyAgent());
//        bots.add(new GreedyAgent());
//        bots.add(new Agent22477478());
//        bots.add(new ParanoidAgent());
//        bots.add(new ParanoidAgent());
//        bots.add(new Agent22477478());
//        bots.add(new MaxNAgent());
//        bots.add(new MaxNAgent());
        // Run tournaments
        runMultiTestTourney(bots, 100, 10);
    }

    /**
     * Run test tournament for multiple sets of bots
     *
     * @param botSets
     * @param simpSetGames Number of set for simple agents
     * @param compSetGames Number of sets for complex agents
     */
    private static void runMultiTestTourney(ArrayList<Agent> botSets,
            int simpSetGames, int compSetGames) {

        // Bot set size
        int setSize = 3;

        // For each bot set
        for (int i = 0; i < botSets.size(); i += setSize) {

            // Get end index of current bot set
            int end = Math.min(botSets.size(), i + setSize);

            // Get current set
            List<Agent> curSet;
            curSet = botSets.subList(i, end);

            // Decide on games in set
            int setGames;
            boolean isSimple = true;
            for (Agent curAgent : curSet) {
                if (curAgent instanceof MaxNAgent
                        || curAgent instanceof ParanoidAgent) {
                    isSimple = false;
                    break;
                }
            }
            if (isSimple) {
                setGames = simpSetGames;
            } else {
                setGames = compSetGames;
            }

            // Tournament index
            int tInd = i / setSize;

            // Tournament heading
            System.out.println("\n");
            String h = "###### TOURNAMENT ";
            h += "" + (tInd + 1);
            h += " ######";
            System.out.println(h);

            // Run test tournament on bot set
            runTestTourney(curSet, setGames);
        }
    }

    /**
     * Run one test tournament for one set of bots according to types
     *
     * @param bots
     */
    private static void runTestTourney(List<Agent> botSet, int games) {

        // If no games wanted
        if (games == 0) {

            // Do not process further
            return;
        }

        // If not right amount of bots
        if (botSet.size() != 3) {

            // Notify and stop
            System.out.println("\nAdd/remove bots!\n");
            return;
        }

        // Make array
        Agent[] botsArr = new Agent[3];

        // Add first three bots to array
        for (int i = 0; i < 3; i++) {
            botsArr[i] = botSet.get(i);
        }

        // True if there all autonomous bots
        boolean allAutonomous = true;

        // For all agents
        for (Agent curAgent : botsArr) {

            // If this agent is not autonomous
            if (!curAgent.isAutonomous()) {

                // Set all autonomous to false
                allAutonomous = false;
            }
        }

        // If all autonomous
        if (allAutonomous) {

            // Do many matches quickly and show less
            // Time limit, no pause, many matches,
            // no display, no randomize, no log
            tournament(botsArr, 20 * 2, 0, games, false, false, null);
        } else {
            // Else If there is a manual agent,
            // do slowly and show more
            // No time limit, long pause, one match,
            // do display, no randomize, no log
            tournament(botsArr, 0, 888, 0, true, false, null);
        }
    }

    /**
     * Runs a tournament for a group of agents.The games in the tournament will
     * have the specified time limit.If a non-zero number of games are
     * specified, the agents will be randomly assigned to games.If 0 is
     * specified, every agent will play every other pair of agents, with the
     * colours of the pieces randomly assigned.
     *
     * @param bots an array of Agents to compete in the contest.
     * @param timeLimit the cumulative time each player has (in seconds). To
     * specify an untimed game, set as less than or equal to zero.
     * @param pauseTime The pause in milliseconds between updating the graphical
     * board
     * @param numGames
     * @param displayOn a boolean flag for whether the game should be
     * graphically displayed
     * @param randomize Randomize pairings
     * @param logFile a FileName to print the game logs to. If this can't be
     * found, or is null, System.out will be used instead. *
     */
    public static void tournament(Agent[] bots, int timeLimit,
            int pauseTime, int numGames,
            Boolean displayOn, boolean randomize, String logFile) {

        HashMap<Agent, Statistics> scoreboard = new HashMap<>();

        PrintStream logger = System.out;
        try {
            if (logFile != null) {
                logger = new PrintStream(new File(logFile));
            }
        } catch (IOException e) {
            System.out.println(logFile + "not found: "
                    + e.getMessage() + "\nUsing System.out instead.");
        }

        for (Agent a : bots) {
            scoreboard.put(a, new Statistics(a));
        }

        if (numGames == 0) {

            // All combinations of three agents play each other.
            // In each game the order is random.
            for (int i = 0; i < bots.length; i++) {
                for (int j = i + 1; j < bots.length; j++) {
                    for (int k = j + 1; k < bots.length; k++) {
                        int[] players = {i, j, k};
                        int[] ord = perms[random.nextInt(perms.length)];
                        HashMap<Integer, ArrayList<Object>> res;
                        res = play(0, 1, bots[players[ord[0]]],
                                bots[players[ord[1]]],
                                bots[players[ord[2]]],
                                timeLimit, pauseTime,
                                logger, displayOn);
                        for (int o = 0; o < 3; o++) {
                            scoreboard.get(bots[players[ord[o]]]).
                                    update(res.get(o));
                        }
                    }
                }
            }
        } else {

            // Else multiple games wanted
            int n = bots.length;
            for (int g = 0; g < numGames; g++) {

                // Player index
                int[] players;

                // If randomize on
                if (randomize) {

                    // Play randomly assigned games.
                    // Note: Agents may play themselves.
                    players = new int[]{
                        random.nextInt(n),
                        random.nextInt(n),
                        random.nextInt(n)
                    };
                } else {

                    // Else if randomize,
                    // do same order each time
                    players = new int[]{0, 1, 2};
                }

                HashMap<Integer, ArrayList<Object>> res;
                res = play(g, numGames,
                        bots[players[0]],
                        bots[players[1]],
                        bots[players[2]],
                        timeLimit,
                        pauseTime,
                        logger,
                        displayOn);
                for (int o = 0; o < 3; o++) {
                    scoreboard.get(bots[players[o]]).update(res.get(o));
                }
            }

        }

        // Get corrected game total
        int trueGames = numGames;
        if (trueGames == 0) {
            trueGames = 1;
        }

        // Stats heading
        String statH = "\n#### Statistics for " + trueGames + " Game";
        if (trueGames > 1) {
            statH += "s";
        }
        logger.println(statH);

        // Get stats and sort
        Statistics[] results = (Statistics[]) scoreboard.values().
                toArray(new Statistics[0]);
        Arrays.sort(results);

        // Print stats
        Statistics.printHeading(logger);
        int rank = 1;
        for (Statistics stat : results) {

            // Print row
            stat.printInfoRow(rank, logger);

            // Increase rank
            rank++;
        }
    }

    /**
     * Runs a threeChess game between three players.There are options to display
     * the board, and log the game to a text file.A time limit may also be
     * specified for a timed game.
     *
     * @param curGame Current game number
     * @param totGames
     * @param blue the agent playing the blue pieces.
     * @param green the agent playing the green pieces.
     * @param red the Agent playing the red pieces.
     * @param timeLimit the cumulative time each player has (in seconds). To
     * specify an untimed game, set as less than or equal to zero.
     * @param pauseTime The pause in milliseconds between updating the graphical
     * board
     * @param logger a printStream to write the game moves to.
     * @param displayOn a boolean flag for whether the game should be
     * graphically displayed
     * @return an array of three ints, the scores for blue, green and red, in
     * that order. *
     */
    public static HashMap<Integer, ArrayList<Object>> play(
            int curGame, int totGames,
            Agent blue, Agent green, Agent red, int timeLimit,
            int pauseTime, PrintStream logger, boolean displayOn) {

        // Initialize results map
        HashMap<Integer, ArrayList<Object>> res = new HashMap<>();
        for (Colour c : Colour.values()) {
            res.put(c.ordinal(), new ArrayList<>());
        }

        // Setup board
        Board board = new Board(timeLimit > 0 ? timeLimit * 1000 : 1);

        // Setup time limit
        boolean timed = timeLimit > 0;

        // Heading
        int actCG = curGame + 1;
        String heading = "\n====== GAME ";
        heading += "" + actCG;
        heading += " of " + totGames;
        heading += " (" + ((int) (actCG * 100.0f) / totGames);
        heading += "%)";
        heading += " ======";
        logger.println(heading);

        // Setup display
        ThreeChessDisplay display = null;
        if (displayOn) {
            display = new ThreeChessDisplay(board,
                    blue.toString(), green.toString(), red.toString());
            GUIAgent.currentDisplay = display;
        }

        // While game not over
        while (!board.gameOver()) {

            // Get agent to play move
            Colour colour = board.getTurn();
            Agent current
                    = (colour == Colour.BLUE ? blue
                            : (colour == Colour.GREEN ? green : red));
            long startTime = System.nanoTime();
            Position[] move = null;
            try {
                move = current.playMove((Board) board.clone());
            } catch (CloneNotSupportedException e) {
            }

            // Rounds to nearest millisecond
            long time = (System.nanoTime() - startTime + 500_000L) / 1_000_000L;
            if (move != null && move.length == 2
                    && board.isLegalMove(move[0], move[1])) {
                try {
                    board.move(move[0], move[1], (timed ? (int) time : 0));
                    if (displayOn && display != null) {
                        // There's no point in sleeping if we
                        // have to wait for the user to input their move anyway.
                        if (current.isAutonomous()) {
                            try {
                                Thread.sleep(pauseTime);
                            } catch (InterruptedException e) {
                            }
                        }
                        display.repaintCanvas();
                    }
                } catch (ImpossiblePositionException e) {
                    logger.println(e.getMessage());
                }
            } else {
                // Illegal move

                // All players get a win
                for (Colour c : Colour.values()) {
                    addToResMap(res, c, 1);
                }

                // Illegal move player gets -2
                setInResMap(res, board.getTurn(), 0, -2);

                // Return results
                return res;
            }
        }

        // Remove display
        GUIAgent.currentDisplay = null;

        // Initialize to 0
        for (Colour c : Colour.values()) {
            addToResMap(res, c, 0);
        }

        // Add actual scores
        setInResMap(res, board.getWinner(), 0, 1);
        setInResMap(res, board.getLoser(), 0, -1);

        // Print info
        logger.println("Player\t\t\tScore\t\tTime Used\tTime Left");
        for (Colour c : Colour.values()) {

            // String
            String info = "";

            // Add player to info
            switch (c) {
                case BLUE:
                    info += blue.toString();
                    break;
                case GREEN:
                    info += green.toString();
                    break;
                case RED:
                    info += red.toString();
                    break;
            }

            // Add agent name to info
            info = info.substring(0, 4);
            info += " (" + c + ")" + "\t\t";

            // Add score/points to info
            int score = (int) res.get(c.ordinal()).get(0);
            info += score + "\t\t";

            // Get time values
            int timeLeft = board.getTimeLeft(c);
            int timeUsed = ((timeLimit * 1000) - timeLeft);

            // Add to results map
            addToResMap(res, c, timeUsed);

            // Add timeleft
            if (timeLimit != 0) {

                info += timeUsed + "ms";
                info += "\t\t";

                info += timeLeft + "ms";
                info += "\t\t";

            } else {
                info += "0ms";
                info += "\t\t";

                info += "INFINITE";
                info += "\t\t";
            }

            // Print full
            logger.println(info);
        }

        // Return array
        return res;
    }

    /**
     * This plays a manual game where all rules are ignored. This effectively
     * allows you to move pieces around the board for simulating positions. *
     */
    public static void playCheat() {
        Board board = new CheatBoard();
        Agent agent = new ManualAgent();
        ThreeChessDisplay display
                = new ThreeChessDisplay(board, "Blue", "Green", "Red");
        // Note in an untimed game, this loop can run infinitely.
        while (!board.gameOver()) {
            Position[] move = null;
            try {
                move = agent.playMove((Board) board.clone());
            } catch (CloneNotSupportedException e) {
            }
            if (move != null && move.length == 2) {
                try {
                    board.move(move[0], move[1], 0);
                    display.repaintCanvas();
                } catch (ImpossiblePositionException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Add data to result map
     */
    private static void addToResMap(
            HashMap<Integer, ArrayList<Object>> map,
            Colour col, Object data) {

        // Retrieve list and add to it
        map.get(col.ordinal()).add(data);
    }

    /**
     * Set data in result map
     */
    private static void setInResMap(
            HashMap<Integer, ArrayList<Object>> map,
            Colour col, int pos, Object data) {

        // Retrieve list and add to it
        map.get(col.ordinal()).set(pos, data);
    }
}
