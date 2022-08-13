package project4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * A pane that displays the number of agents in specific states during the
 * simulation using a bar graph. Uses a canvas to display the bar graph of
 * agent states, and is updated when agent objects undergo state changes.
 */
public class StatsGraph extends Pane {
    private static double agentNum;
    private static double vulNum;
    private static double deadNum;
    private static double sickNum;
    private static double immNum;
    private static double asymNum;
    private static final double HEIGHT = 200;
    private static final double WIDTH = 30;
    private static final Canvas canvas = new Canvas(250, 200);
    private static final GraphicsContext gc = canvas.getGraphicsContext2D();

    /**
     * Creates a new StatsGraph object
     */
    public StatsGraph() {
        setMinHeight(200);
        setMinWidth(280);
        getChildren().add(canvas);
    }

    /**
     * Updates the bar in the bar graph which represents the amount of
     * vulnerable agents currently in the simulation
     */
    private static void drawVulBar() {
        gc.setFill(Color.BLUE);
        gc.clearRect(20,0,WIDTH, HEIGHT);
        double h = (vulNum/agentNum)*HEIGHT;
        gc.fillRect(20,200 - h, WIDTH, h);
    }

    /**
     * Updates the bar in the bar graph which represents the amount of
     * sick agents currently in the simulation
     */
    private static void drawSickBar() {
        gc.setFill(Color.RED);
        gc.clearRect(60,0, WIDTH, HEIGHT);
        double h = (sickNum/agentNum)*HEIGHT;
        gc.fillRect(60,200 - h, WIDTH, h);
    }

    /**
     * Updates the bar in the bar graph which represents the amount of
     * asymptomatic agents currently in the simulation
     */
    private static void drawAsymBar() {
        gc.setFill(Color.YELLOWGREEN);
        gc.clearRect(100,0, WIDTH, HEIGHT);
        double h = (asymNum/agentNum)*HEIGHT;
        gc.fillRect(100,200 - h, WIDTH, h);
    }

    /**
     * Updates the bar in the bar graph which represents the amount of
     * dead agents currently in the simulation
     */
    private static void drawDeadBar() {
        gc.setFill(Color.BLACK);
        gc.clearRect(140,0, WIDTH, HEIGHT);
        double h = (deadNum/agentNum)*HEIGHT;
        gc.fillRect(140,200 - h, WIDTH, h);
    }

    /**
     * Updates the bar in the bar graph which represents the amount of
     * immune agents currently in the simulation
     */
    private static void drawImmBar() {
        gc.setFill(Color.GREEN);
        gc.clearRect(180,0, WIDTH, HEIGHT);
        double h = (immNum/agentNum)*HEIGHT;
        gc.fillRect(180,200 - h, WIDTH, h);
    }

    /**
     * Sets the total number of agents in the simulation, and updates the
     * bar graph magnitude ticks accordingly
     * @param agentNum Amount of agents in the simulation
     */
    protected static synchronized void setAgentNum(int agentNum){
        StatsGraph.agentNum = agentNum;
        vulNum = agentNum;
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(9));
        gc.fillText((int)agentNum + "-",0,6);
        gc.fillText((int)3*agentNum/4 + "-",0,HEIGHT/4);
        gc.fillText((int)agentNum/2 + "-",0,HEIGHT/2);
        gc.fillText((int)agentNum/4 + "-",0,3*HEIGHT/4);
        gc.fillText("-" + (int)agentNum,212,6);
        gc.fillText("-" + (int)3*agentNum/4,212,HEIGHT/4);
        gc.fillText("-" + (int)agentNum/2,212,HEIGHT/2);
        gc.fillText("-" + (int)agentNum/4,212,3*HEIGHT/4);
        drawVulBar();
    }

    /**
     * Increments the number of dead agents in the simulation by 1. This
     * method should only be called when an agent has transitioned to dead
     * state.
     */
    protected static synchronized void incDead(){
        deadNum++;
        drawDeadBar();
    }

    /**
     * Increments the number of sick agents in the simulation by 1. Also
     * decrements the number of vulnerable agents by 1. This method should
     * only be called when an agent has transitioned to sick state.
     */
    protected static synchronized void incSick(){
        sickNum++;
        drawSickBar();
        vulNum--;
        drawVulBar();
    }

    /**
     * Decrements the number of sick agents in the simulation by 1. This
     * method should only be called when an agent has transitioned out of
     * sick state.
     */
    protected static synchronized void decSick() {
        sickNum--;
        drawSickBar();
    }

    /**
     * Increments the number of immune agents in the simulation by 1. This
     * method should only be called when an agent has transitioned to immune
     * state.
     */
    protected static synchronized void incImm(){
        immNum++;
        drawImmBar();
    }

    /**
     * Increments the number of asymptomatic agents in the simulation by 1.
     * Also decrements the number of vulnerable agents by 1. This method
     * should only be called when an agent has transitioned to asymptomatic
     * state.
     */
    protected static synchronized void incAsym(){
        asymNum++;
        drawAsymBar();
        vulNum--;
        drawVulBar();
    }

    /**
     * Decrements the number of asymptomatic agents in the simulation by 1.
     * This method should only be called when an agent has transitioned out
     * of asymptomatic state.
     */
    protected static synchronized void decAsym(){
        asymNum--;
        drawAsymBar();
    }

    /**
     * Resets the StatsGraph pane by returning the bar graph to initial
     * simulation conditions. This method should only be called when the
     * simulation has been reset.
     */
    protected static synchronized void reset(){
        vulNum = agentNum;
        deadNum = 0;
        sickNum = 0;
        immNum = 0;
        asymNum = 0;
        gc.clearRect(20,0,192,200);
        drawVulBar();
    }
}
