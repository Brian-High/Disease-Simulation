package project4;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import static java.lang.Math.random;

/**
 * Object representing the active agents which comprise the population
 * used to model the disease spread during the simulation. This could be
 * people, fish, cells, etc. Each agent runs on its own thread, which is
 * used to move the agent, process potential state changes, and notify
 * neighboring agents of potential exposure.
 */
public class Agent implements Runnable {
    private double x;
    private double y;
    private double direction;
    private final BlockingQueue<Message> inbox;
    private State state;
    private ArrayList<Agent> neighbors;
    private ArrayList<Agent> neighborsUpdate;
    private int agentNum;
    private int daysSick = 0;
    private static boolean paused = false;
    private static boolean moveOn = true;
    private static double probToSpread;
    private static double probToGetSick = .75;
    private static double probToDie;
    private static double daysOfIncubation;
    private static double daysOfSickness;
    private static double screenWidth = 200;
    private static double screenHeight = 200;
    private static double dayLength;
    private static long simSpeed = 100;
    private static final double MOVE_RADIUS = 2;

    /**
     * All 6 states which an agent can take on during the simulation
     */
    private enum State {
        VULNERABLE,
        INFECTED,
        ASYMPTOMATIC,
        SICK,
        IMMUNE,
        DEAD
    }

    /**
     * All 5 messages which agents can send to each other, or to themselves.
     * Each message directs an agent to undergo a specific state change, if
     * possible, once it is processed.
     */
    private enum Message {
        HAVE_GERMS,
        BECOME_SICK,
        BECOME_ASYMPTOMATIC,
        BECOME_IMMUNE,
        BECOME_DEAD,
    }

    /**
     * Agent is constructed either vulnerable or infected, with and x and y
     * coordinate, and a number
     * @param infected Initial agent state
     * @param x X position in the simulation UI
     * @param y Y position in the simulation UI
     * @param agentNum Unique integer ID
     */
    public Agent(boolean infected, double x, double y, int agentNum) {
        inbox = new ArrayBlockingQueue<>(50);
        this.x = x;
        this.y = y;
        this.agentNum = agentNum;
        direction = Math.random()*2*Math.PI;
        neighbors = new ArrayList<>();
        neighborsUpdate = new ArrayList<>();
        if(infected) this.state = State.INFECTED;
        else this.state = State.VULNERABLE;
    }

    /**
     * Runs the simulation logic then waits for a set amount of time. The
     * agents move every time the loop runs, but the logic to spread the
     * disease only runs every 5 times the loop runs. All state changes and
     * messages are run through each agent's inbox to avoid conflicts.
     */
    @Override
    public void run() {
        int frameCount = 0;
        while(true) {
            try {
                Thread.sleep(simSpeed);
            } catch (InterruptedException e) {
                System.out.println("e");
            }

            if (!paused) {
                if(state != State.DEAD && moveOn) moveRandDir();
                frameCount++;
                if (frameCount == 5) {
                    if (state == State.SICK || state == State.ASYMPTOMATIC) {
                        for (Agent agent : neighbors) {
                            if (agent != null) {
                                if (random() < probToSpread) {
                                    agent.sendMessage(Message.HAVE_GERMS);
                                }
                            }
                        }
                        daysSick++;
                        if (daysSick >= daysOfSickness) {
                            if(state == State.SICK) {
                                if (random() < probToDie) inbox.add(Message.BECOME_DEAD);
                                else inbox.add(Message.BECOME_IMMUNE);
                                StatsGraph.decSick();
                            }
                            if(state == State.ASYMPTOMATIC) {
                                inbox.add(Message.BECOME_IMMUNE);
                                StatsGraph.decAsym();
                            }
                            daysSick = 0;
                        }
                    }
                    if(state == State.INFECTED){
                        daysSick++;
                        if(daysSick >= daysOfIncubation){
                            if(random() < probToGetSick) {
                                inbox.add(Message.BECOME_SICK);
                            } else {
                                inbox.add(Message.BECOME_ASYMPTOMATIC);
                            }
                            daysSick = 0;
                        }
                    }

                    if (!inbox.isEmpty()) {
                        processInbox();
                    }
                    frameCount = 0;
                }
            }
            setNeighbors();
        }
    }

    /**
     * Sets the agents neighbors. Only run after simulation logic is done.
     */
    private void setNeighbors(){
        neighbors.clear();
        neighbors.addAll(neighborsUpdate);
    }

    /**
     * Stores the agents new neighbors until the simulation logic is done.
     * @param agents new neighbors
     */
    protected void updateNeighbors(ArrayList<Agent> agents){
        neighborsUpdate.clear();
        neighborsUpdate.addAll(agents);
    }

    /**
     * Updates the probability for the disease to spread between agents
     * upon contact with a sick or asymptomatic agent
     * @param probToSpread new probability of disease spread
     */
    protected static void setProbToSpread(double probToSpread) {
        Agent.probToSpread = probToSpread;
    }

    /**
     * Updates the probability for an infected agent to become sick after
     * the incubation period has ended
     * @param probToGetSick new probability of becoming sick rather than
     *                      asymptomatic
     */
    protected static void setProbToGetSick(double probToGetSick) {
        Agent.probToGetSick = probToGetSick;
    }

    /**
     * Updates the probability for a sick agent to die after the illness
     * period has ended
     * @param probToDie new probability of death due to sickness
     */
    protected static void setProbToDie(double probToDie) {
        Agent.probToDie = probToDie;
    }

    /**
     * Updates the amount of days in the disease's incubation period
     * @param daysOfIncubation new incubation period length in simulation days
     */
    protected static void setDaysOfIncubation(double daysOfIncubation) {
        Agent.daysOfIncubation = daysOfIncubation;
    }

    /**
     * Updates the amount of days in the disease's illness period
     * @param daysOfSickness new illness period length in simulation days
     */
    protected static void setDaysOfSickness(double daysOfSickness) {
        Agent.daysOfSickness = daysOfSickness;
    }

    /**
     * Updates the stored screen width of the simulation area, so that an
     * agent cannot move left or right out of the simulation area
     * @param screenWidth new simulation width in pixels
     */
    protected static void setScreenWidth(double screenWidth) {
        Agent.screenWidth = screenWidth;
    }

    /**
     * Updates the stored screen height of the simulation area, so that an
     * agent cannot move up or down out of the simulation area
     * @param screenHeight new simulation height in pixels
     */
    protected static void setScreenHeight(double screenHeight) {
        Agent.screenHeight = screenHeight;
    }

    /**
     * Updates the delay between each agent movement during the simulation
     * @param simSpeed new movement delay in milliseconds
     */
    protected static void setSimSpeed(long simSpeed) {
        Agent.simSpeed = simSpeed;
        Agent.dayLength = 5000000*simSpeed;
    }

    /**
     * Updates whether the agent threads are paused or not
     * @param pauseOn True to pause, false to resume
     */
    protected static void setPaused(boolean pauseOn) {
        Agent.paused = pauseOn;
    }

    /**
     * Updates whether agents are allowed to move or not
     * @param moveOn True if movement is allowed, otherwise false
     */
    protected static void setMovement(boolean moveOn) {
        Agent.moveOn = moveOn;
    }

    /**
     * Moves the agent to the specified location
     * @param x x coordinate of the new location
     * @param y y coordinate of the new location
     */
    protected void relocate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * changes the heading of the agent by up to (+/-) 1/8 pi radians.
     * Moves the agent up to 2 pixels in the direction of the heading.
     * If the agent goes off-screen its heading is changed to go back onto
     * the screen.
     */
    private void moveRandDir(){
        direction += (Math.random()-.5)*.25*Math.PI;
        double moveDistance = MOVE_RADIUS *(Math.random()*.5)+.5;
        x += Math.cos(direction)*moveDistance;
        y += Math.sin(direction)*moveDistance;
        if(x > screenWidth-5) direction = Math.PI;
        if(y > screenHeight-5) direction = 1.5*Math.PI;
        if(x < 0) direction = 0;
        if(y < 0) direction = .5*Math.PI;
    }

    /**
     * Resets the current state of the agent to initial conditions for
     * simulation reset purposes
     * @param infected True -> infected state, false -> vulnerable state
     */
    protected void resetState(boolean infected) {
        this.state = infected ? State.INFECTED : State.VULNERABLE;
    }

    /**
     * Clears the inbox for this agent for simulation reset purposes
     */
    protected void clearMessages() {
        this.inbox.clear();
    }

    /**
     * Adds a message to this agent's inbox
     * @param message new message to be sent to this agent's inbox
     */
    public void sendMessage(Message message) {
        inbox.add(message);
    }

    /**
     * Process every message in the inbox and sets the state accordingly.
     * If the agents state is changed the historyPane and StatsGraph records
     * this state change. The whole inbox must be processed everytime the
     * simulation logic is run because a single agent can receive many
     * HAVE_GERMS messages at once.
     */
    private void processInbox(){
        for (int i = 0; i < inbox.size(); i++) {
            Message message = inbox.poll();
            boolean stateChange = true;
            if (message != null) {
                switch (message) {
                    case HAVE_GERMS:
                        if (state == State.VULNERABLE)
                            state = State.INFECTED;
                        else stateChange = false;
                        break;
                    case BECOME_DEAD:
                        state = State.DEAD;
                        StatsGraph.incDead();
                        break;
                    case BECOME_IMMUNE:
                        state = State.IMMUNE;
                        StatsGraph.incImm();
                        break;
                    case BECOME_ASYMPTOMATIC:
                        state = State.ASYMPTOMATIC;
                        StatsGraph.incAsym();
                        break;
                    case BECOME_SICK:
                        state = State.SICK;
                        StatsGraph.incSick();
                        break;
                    default: stateChange = false;
                }
                if(stateChange) Platform.runLater(() -> HistoryPane.addToHistory(agentNum,this.toString()));
            }
        }
    }

    /**
     * @return String representation of the agent's current state
     */
    public String toString(){
        return switch (state) {
            case DEAD -> "dead";
            case VULNERABLE -> "vulnerable";
            case INFECTED -> "infected";
            case ASYMPTOMATIC -> "asymptomatic";
            case SICK -> "sick";
            case IMMUNE -> "immune";
        };
    }

    /**
     * @return Color shade used to represent this agent's current state
     */
    protected Color getStateColor(){
        return switch (state) {
            case VULNERABLE -> Color.BLUE;
            case INFECTED -> Color.SALMON;
            case SICK -> Color.RED;
            case DEAD -> Color.BLACK;
            case IMMUNE -> Color.GREEN;
            case ASYMPTOMATIC -> Color.YELLOWGREEN;
        };
    }

    /**
     * @return True if all agent threads are currently paused, otherwise false
     */
    public static boolean isPaused() {
        return paused;
    }

    /**
     * @return x coordinate of this agent's current position
     */
    public synchronized double getX() {
        return x;
    }

    /**
     * @return y coordinate of this agent's current position
     */
    public synchronized double getY() {
        return y;
    }

    /**
     * @return Current length in milliseconds of simulation days
     */
    protected static synchronized double getDayLength(){
        return dayLength;
    }

    /**
     * @return delay between each agent movement during the simulation
     */
    protected static synchronized long getSimSpeed() {
        return simSpeed;
    }
}
