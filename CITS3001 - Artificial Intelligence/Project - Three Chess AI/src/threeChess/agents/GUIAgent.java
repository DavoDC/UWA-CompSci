package threeChess.agents;

import threeChess.Agent;
import threeChess.Board;
import threeChess.Position;
import threeChess.ThreeChessDisplay;

/**
 * An agent that allows the user to select its moves through the display.
 */
public class GUIAgent extends Agent {

    // Three Chess Display
    public static ThreeChessDisplay currentDisplay = null;

    // Name
    private final String name;

    /**
     * A no argument constructor, required for tournament management.
     */
    public GUIAgent() {
        this("GUI Agent");
    }

    /**
     * Set name and print info
     *
     * @param name
     */
    public GUIAgent(String name) {
        this.name = name;
        System.out.println(
                name + " is a manually controlled agent.\n"
                + "To make a move click the piece "
                + "you want to move in the display, \n"
                + "and then click the square where you wish to move it to."
        );
    }

    /**
     * Not autonomous
     *
     * @return
     */
    @Override
    public boolean isAutonomous() {
        return false;
    }

    /**
     * Gets and returns a move from the display for this agent.
     */
    @Override
    public Position[] playMove(Board board) {
        try {
            return currentDisplay.askForMove().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(
                    "Exception waiting for user to enter move on display",
                    e);
        }
    }

    /**
     * @return the Agent's name, for annotating game description.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Unused for the GUI agent.
     */
    @Override
    public void finalBoard(Board finalBoard) {
    }
}
