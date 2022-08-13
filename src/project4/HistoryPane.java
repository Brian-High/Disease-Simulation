package project4;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * A pane that lists the last 5 state changes that occurred
 */
public class HistoryPane extends Pane {
    private static final Label historyLbl = new Label("History:");
    private static final VBox listBox = new VBox(10, historyLbl);
    private static int day = 0;

    /**
     * Creates a new HistoryPane object
     */
    public HistoryPane() {
        getChildren().add(listBox);
        setMinWidth(280);
    }

    /**
     * updates the list with a state change and removes the oldest line.
     * @param agentNum the agent
     * @param message the state change
     */
    protected static void addToHistory(int agentNum, String message){
        Label historyLine = new Label("Agent " + agentNum + " became "
                + message + " on Day " + day);
        listBox.getChildren().add(1,historyLine);
        if(listBox.getChildren().size() > 6) listBox.getChildren().remove(6);
    }

    /**
     * Clears the list of events and resets the day count to day 0
     */
    protected static void reset(){
        listBox.getChildren().remove(1,listBox.getChildren().size());
        day = 0;
    }

    /**
     * Increments the current day elapsed in the simulation by 1
     */
    protected static void incDay() {
        HistoryPane.day++;
    }
}

