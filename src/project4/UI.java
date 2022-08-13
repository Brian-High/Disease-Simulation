package project4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

/**
 * A pane that displays the simulation state. Uses a canvas for efficiency
 * and is updated with the animation timer in Main.
 */
public class UI extends Pane {
    private final GraphicsContext gc;
    private final double WIDTH;
    private final double HEIGHT;
    private final Agent[][] grid;

    /**
     * Creates a new simulation UI with the following parameters
     * @param grid 2-D array of agents comprising the simulation
     * @param width Width (in pixels) of the simulation UI
     * @param height Height (in pixels) of the simulation UI
     */
    UI(Agent[][] grid, double width, double height){
        this.grid = grid;
        this.WIDTH = width;
        this.HEIGHT = height;
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Agent.setScreenWidth(WIDTH);
        Agent.setScreenHeight(HEIGHT);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);
    }

    /**
     * Clears the UI canvas, then redraws the agents at their updated
     * positions and agent states
     * @param rows Amount of rows in the 2-D agent array
     * @param cols Amount of columns in the 2-D agent array
     */
    protected void updateAgents(int rows, int cols) {
        gc.clearRect(0,0, WIDTH, HEIGHT);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] != null) {
                    gc.setFill(grid[i][j].getStateColor());
                    gc.fillOval(grid[i][j].getX(),grid[i][j].getY(),8,8);
                }
            }
        }
    }

    /**
     * @return Width (in pixels) of the simulation UI
     */
    protected final double getWIDTH() {
        return WIDTH;
    }

    /**
     * @return Height (in pixels) of the simulation UI
     */
    protected final double getHEIGHT() {
        return HEIGHT;
    }
}
