import javax.swing.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Random;
import java.util.*;

class SimulationManager extends WindowManager
{
    protected ArrayList<Agent> agentList; 
        // A list of all agents in the simulation; this is declared as
        // protected because we access it directly from within AgentCanvas.  
        // Why?  Because we only access it to draw the agents, and given 
        // that the list may be large, it doesn't make sense to
        // make a copy and return that copy to AgentCanvas.

    
	PriorityQueue<Event> calendar = new PriorityQueue<Event>(new EventComparator());
	private boolean debug = true;
	protected Landscape landscape;
    protected int gridSize;

    private AgentCanvas canvas;  // the canvas on which agents are drawn
    private static Random rng;

    private double time;  // the simulation time

    //======================================================================
    //* public SimulationManager(int gridSize, int numAgents, int initialSeed)
    //======================================================================
    public SimulationManager(int gridSize, int numAgents, int initialSeed, double maxTime)
    {
        super("Sugarscape", 500, 500);  // name, window width, window height

        this.gridSize  = gridSize;
        this.agentList = new ArrayList<Agent>();

        rng = new Random(initialSeed);

        this.time = 0;   // initialize the simulation clock

        landscape = new Landscape(gridSize, gridSize);

        for (int i = 0; i < numAgents; i++)
        {
            Agent a = new Agent("agent " + agentList.size());
            agentList.add(a);

			// we should check to make sure the cell isn't already occupied!
            while(true)
			{
				int row = rng.nextInt(gridSize); // an int in [0, gridSize-1]
				int col = rng.nextInt(gridSize); // an int in [0, gridSize-1]
				if(landscape.getCellAt(row, col).isOccupied() == false)
				{
					a.setRowCol(row, col);
					landscape.getCellAt(row, col).setOccupied(true);
					break;
				}
			}
            calendar.add(new Event(a, EventType.move, a.getIntermovement()));
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
        // bogus simulation code below...
        /* for (int t = 1; t <= 100; t++)
        {
            this.time = t;
            for (int i = 0; i < agentList.size(); i++)
            {
                Agent a = agentList.get(i);

                int row = rng.nextInt(gridSize); // an int in [0, gridSize-1]
                int col = rng.nextInt(gridSize); // an int in [0, gridSize-1]

                // we should check to make sure the cell isn't already occupied!
                a.setRowCol(row, col); 
            }
            canvas.repaint();
            try { Thread.sleep(500); } catch (Exception e) {}
        } */	
    	
//    	try { Thread.sleep(1000); } catch (Exception e) {}
    	this.time = 0;
    	canvas.repaint();
		while(this.getTime() < maxTime)
		{
			try { Thread.sleep(1); } catch (Exception e) {}
			//get event data
			
			Event e = calendar.peek();
//			this.time = e.getTime();
			double previous = this.time;

			update(e.getTime() - previous);
			
			e = calendar.poll();
			EventType type = e.getEvent();
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
			this.time = e.getTime();
//			update(this.time - previous);
			
			if(type == EventType.move)
			{
//				System.out.println("MOVE");
				a.move(this);
				a.setIntermovement(Agent.getNewIntermovement());
				calendar.add(new Event(a, EventType.move, this.time + a.getIntermovement()));
			}
			else if(type == EventType.die)
			{
//				System.out.println("DEATH");
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
				Agent new_a = new Agent("agent " + this.agentList.size());
				this.agentList.add(new_a);
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
				calendar.add(new Event(new_a, EventType.move, this.time + new_a.getIntermovement()));
			}
				else
			{
				//Something
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
    
    private void update(double timeDiff)
    {
    	for(int i = 0; i < agentList.size(); i++)
    	{
    		Agent agent_i = this.agentList.get(i);
    		agent_i.setCurrentAge(agent_i.getCurrentAge() + timeDiff);
    		if(agent_i.getCurrentAge() > agent_i.getMaxAge())
    		{
    			//schedule death
    			double x1 = this.time - timeDiff;
    			double x2 = this.time;
    			
    			double y1 = agent_i.getCurrentAge() - timeDiff;
    			double y2 = agent_i.getCurrentAge();
    			
    			double slope = (y2 - y1) / (x2 - x1);
    			double intercept = y1 - (slope * x1);
    			
    			agent_i.setDeathTime(-intercept / slope);
    			calendar.add(new Event(agent_i, EventType.die, agent_i.getDeathTime()));
    		}
    	}
    	
    	for(int i = 0; i < this.gridSize; i++)
    	{
    		for(int j = 0; j < this.gridSize; j++)
    		{
    			Cell i_j = this.landscape.getCellAt(i, j);
    			i_j.setCurrentResource(Math.min(i_j.getCapacity(), 
    					i_j.getCurrentResource() + i_j.getResourceRegrowth() * timeDiff));
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
        new SimulationManager(40, 400, 8675309, 100);
    }
}
