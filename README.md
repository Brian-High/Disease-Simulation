# Project 4: Disease Simulation
### Diego Ornelas & Brian High

The project was completed with all the requirements fulfilled. 
Each agent runs on its own thread and can only communicate to its
neighbors. Each state change or sent message is run through each 
agent's blocking queue to avoid conflicts. Neighbors are determined
through a specified radius and handled by the Main class. The 
display object is updated by an Animation timer. 

#### Agent States
* Vulnerable - Blue
* Infected - Pink
* Sick - Red
* Dead - Black
* Immune - Green
* Asymptomatic - Yellow-green

#### Features Implemented

* History of the simulation
  * An object that displays a lists of which agents have changed 
  states and the time the state change occurred
* Current statistic bar graph
  * An object that displays a bar graph of the number of agents
  currently in each state
* Agents move from initial position
  * Agents start in a grid spaced evenly. When the simulation starts
  they each are given a random heading. Every frame each agents heading
  is incremented by a random amount and then each agent moves a random 
  distance within a specified radius. Each agent keeps track of their 
  position, and a method in the Main class updates their neighbors using 
  the distance formula.
  * Agent movement is on by default. To disable agent movement, the simulation
  config file that you choose at launch must contain one line stating "move 
  off", or the word "move" followed by whitespace and a negative number.
* The simulation can be rerun
  * When the program starts the simulation is running by default. The
  simulation can be paused using the pause button, or reset using the 
  reset button. When the simulation is paused the game logic is stopped
  but each thread continues. When the simulation is reset the 
  simulation is paused, each agent's state is reset, and each agent's 
  position is moved back to the initial position. 
* The simulation parameters can be changed
  * The GUI has fields for each of the simulations parameters. The 
  confirm button is used to apply the parameters when the simulation 
  is running. The GUI also has load and save buttons to load and save
  the simulation's parameters to and from a file. Config files are provided in
  the res directory. The file I/O is handled by the fileIO object.
* Additional agent states
  * An additional state "Asymptomatic" was added. After the incubation
  period an agent will become sick or asymptomatic based on the
  probability to get sick. During the incubation period, agents cannot
  spread the disease. When an agent is sick or asymptomatic they can 
  spread the disease to its neighbors who are within a specified radius.
  Although sick agents have the possibility to die, asymptomatic agents
  will always become immune, since symptoms are the only cause of death for
  all diseases
* Simulation initialization mode can be changed via config file
  * There are 3 different initialization modes which the user can select
  by including one of the following in their config file:
    * **grid r c**: agents start in a rectangular grid of r rows and c columns,
    spaced apart by the disease exposure distance
    * **random n**: n agents are placed at random locations within the simulation
    area
    * **randomgrid r c n**: n agents are placed at random positions within a grid
    of r rows and c columns, spaced apart by the disease exposure distance
  * The initialization mode can only be changed at the start of the simulation.
  To do so, the name of the config file must be provided as a command line 
  argument when launching the JAR file. If no mode is specified, the simulation
  will default to randomly placing 100 agents in the simulation area.
* Other options
  * The simulations speed can be adjusted through a slider in the GUI
  while simulation is running. The slider sets the number of milliseconds
  each thread is delayed.
