import javax.swing.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Random;
import java.util.*;

class SimulationManager extends WindowManager
{
    protected int gridSize;
        // A list of all agents in the simulation; this is declared as
        // protected because we access it directly from within AgentCanvas.  
        // Why?  Because we only access it to draw the agents, and given 
        // that the list may be large, it doesn't make sense to
        // make a copy and return that copy to AgentCanvas.

    // create a priority queue of events 
    static PriorityQueue<Event> calendar = new PriorityQueue<Event>(new EventComparator());
    protected ArrayList<Agent> agentList; 
    protected Landscape landscape;
    //counters for deaths
    protected int healthyDeaths;
    protected int infectedDeaths;

    private AgentCanvas canvas;  // the canvas on which agents are drawn
    private static Random rng;

    private double time;  // the simulation time


    // the debugger //========================//
    //========================================//
    private boolean debug = false; //=========//  
    //========================================//
    //========================================//  






    //======================================================================
    //* public SimulationManager(int gridSize, int numAgents, int initialSeed)
    //======================================================================
    public SimulationManager(int gridSize, int numAgents, int initialSeed, double maxTime)
    {
        super("Sugarscape", 500, 500);  // name, window width, window height

        this.gridSize  = gridSize;
        this.agentList = new ArrayList<Agent>();
        this.healthyDeaths = 0;
        this.infectedDeaths = 0;
        rng = new Random(initialSeed);

        this.time = 0;   // initialize the simulation clock

        // initialize a landscape
        landscape = new Landscape(gridSize, gridSize);

        // add agents to a list
        for (int i = 0; i < numAgents; i++)
        {
            Agent a = new Agent("agent "+ agentList.size(), this.time);
            agentList.add(a);
            while(true)
            {
                // put agents randomly in the landscape
                int row = rng.nextInt(gridSize); // an int in [0, gridSize-1]
                int col = rng.nextInt(gridSize); // an int in [0, gridSize-1]
                if(landscape.getCellAt(row, col).isOccupied() == false)
                {
                    a.setRowCol(row, col);
                    landscape.getCellAt(row, col).setOccupied(true);
                    break;
                }
            }
            // add the agent to the list
            calendar.add(new Event(a, a.getminTime()));
        }
        this.createWindow();
        this.run(maxTime);
    }

    //======================================================================
    //* public void createWindow()
    //======================================================================
    public void createWindow()
    {
        this.setLayout(new BorderLayout());
        this.getContentPane().setLayout(new BorderLayout()); // java.awt.*
        canvas = new AgentCanvas(this);
        this.getContentPane().add( new JScrollPane(canvas), BorderLayout.CENTER);
    }

    // simple accessor methods
    public int    getGridSize() { return this.gridSize; }
    public double getTime()     { return this.time;     }

    //======================================================================
    //* public void run()
    //* This is where your main simulation event engine code should go...
    //======================================================================
    public void run(double maxTime)
    {
       
        try { Thread.sleep(10); } catch (Exception e) {}
        this.time = 0;
        canvas.repaint();
        while(this.getTime() < maxTime)
        {
            try { Thread.sleep(1); } catch (Exception e) {}
            //get event data
            Event e = calendar.poll();
            // get the agent
            Agent a = e.getAgent();
            
            ////////////////////////////////////////////
            if(debug)
            {
                System.out.println("BEFORE_____________________________");
                e.print();
                a.print();
                landscape.getCellAt(a.getRow(), a.getCol()).print();
                System.out.println(this.time + "\n");
                System.out.println("DONE BEFORE_____________________________");
            }
            ///////////////////////////////////////////
            
            //get previous event's time and new event's time and update age and resource regrowth
            double previous = this.time;
            this.time = e.getTime();
            update(this.time - previous);
            
            // 0 == move 
            if(a.getNextEventType() == 0)
            {
                a.move(this);
                calendar.add(new Event(a,  a.getminTime()));
            }
            else // not move, die
            {
                // increment death counter based on infected or not
                if(a.getHasDisease())
                    this.infectedDeaths += 1;
                else
                    this.healthyDeaths += 1;
                //System.out.println("DEATH");
                //set occupancy to false
                landscape.getCellAt(a.getRow(), a.getCol()).setOccupied(false);
                        
                //remove the agent
                for(int i = 0; i < agentList.size(); i++)
                {
                    if(agentList.get(i).getId().equals(a.getId()))
                    {
                        agentList.remove(i);
                        break;
                    }
                }
                
                //add new agent
                Agent new_a = new Agent("agent " + agentList.size(),this.time);
                this.agentList.add(new_a);
                // find place to add onto the landscape
                while(true)
                {
                    int row = rng.nextInt(this.gridSize); // an int in [0, gridSize-1]
                    int col = rng.nextInt(this.gridSize); // an int in [0, gridSize-1]
                    if(this.landscape.getCellAt(row, col).isOccupied() == false)
                    {
                        new_a.setRowCol(row, col);
                        break;
                    }           
                }
                //schedule an event for new agent
                calendar.add(new Event(new_a, new_a.getminTime()));
            }
                
            ////////////////////////////////////////////
            if(debug)
            {
                System.out.println("AFTER_____________________________");
                a.print();
                landscape.getCellAt(a.getRow(), a.getCol()).print();
                System.out.println(this.time + "\n");
                System.out.println("DONE AFTER_____________________________");
            }
            ///////////////////////////////////////////
            canvas.repaint();
        }
    }
   

    // updates regrowth of cell 
    private void update(double time)
    {
        
        for(int i = 0; i < this.gridSize; i++)
        {
            for(int j = 0; j < this.gridSize; j++)
            {
                Cell i_j = this.landscape.getCellAt(i, j);
                i_j.setCurrentResource(Math.min(i_j.getCapacity(), 
                        i_j.getCurrentResource() + i_j.getResourceRegrowth() * time));
            }
        }
    }

    //======================================================================
    //* public static void main(String[] args)
    //* Just including main so that the simulation can be executed from the
    //* command prompt.  Note that main just creates a new instance of this
    //* class, which will start the GUI window and then we're off and
    //* running...
    //======================================================================
    public static void main(String[] args)
    {
        // if you want to customize input, more than one command line arg
        if(args.length > 0)
        {
            System.out.println("Specify inputs: (n-dimension, numAgents, initial seed, run time)");
            Scanner sc = new Scanner(System.in);
            int dim = sc.nextInt();
            int numAgents = sc.nextInt();
            int initSeed = sc.nextInt();
            int runTime = sc.nextInt(); 
            sc.close();
            new SimulationManager(dim, numAgents, initSeed, runTime);
        }
        else {
            new SimulationManager(40, 200, 8675309, 100);
        }
    }
}
