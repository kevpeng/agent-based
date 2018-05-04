import java.util.*;

public class Agent
{
	private int row;
	private int col;
	private String id;
	
	private int vision;
	private double metaRate;
	private double resources;
	private double intermovement;
	private double maxAge;
	private double currentAge;
	private double deathTime;
	
	static Random rng = new Random(1234567);
	
	Agent(String id)
	{
		this.id = id;
		this.vision = (int) getUniform(1, 6, true);
		this.metaRate = getUniform(1, 4, false);
		this.resources = getUniform(5, 25, false);
		this.maxAge = getUniform(60, 100, false);
		this.currentAge = 0;
		this.intermovement = getNewIntermovement();
	}
	
	private double getUniform(int lower, int upper, boolean isVision)
	{
		if(isVision == true)
		{
			double x = (double) (rng.nextDouble() * (upper + 1 - lower)) + lower;
			return (int) x;
		}
		double x = (double) (rng.nextDouble() * (upper - lower)) + lower;
		return x;
	}
	
	public static double getNewIntermovement()
	{
		return  Math.log(1 - rng.nextDouble())/(-1);
	}
	
	/* Getter Methods */
	public String getId()
	{
		return this.id;
	}
	
	public int getRow()
	{
		return this.row;
	}
	
	public int getCol()
	{
		return this.col;
	}
	
	public int getVision()
	{
		return this.vision;
	}
	
	public double getMetaRate()
	{
		return this.metaRate;
	}
	
	public double getResourceLevel()
	{
		return this.resources;
	}
	
	public double getIntermovement()
	{
		return this.intermovement;
	}
	
	public double getCurrentAge()
	{
		return this.currentAge;
	}
	
	public double getMaxAge()
	{
		return this.maxAge;
	}
	
	public double getDeathTime()
	{
		return this.deathTime;
	}
	
	/* Setter Methods */
	public void setResources(double x)
	{
		this.resources = x;
	}
	
	public void setRow(int row)
	{
		this.row = row;
	}
	
	public void setCol(int col)
	{
		this.col = col;
	}
	
	public void setIntermovement(double intermovement)
	{
		this.intermovement = intermovement;
	}
	
	public void setCurrentAge(double age)
	{
		this.currentAge = age;
	}
	
	public void setRowCol(int row, int col)
	{
		this.row = row;
		this.col = col;
	}
	
	public void setDeathTime(double t)
	{
		this.deathTime = t;
	}
	
	/* Prints information about agent */
	public void print()
	{
		System.out.println("ID: " + this.id);
		System.out.println("Location: (" + this.getRow() + ", " + this.getCol() + ")");
		System.out.println("Vision: " + this.vision);
		System.out.println("Metabolic Rate: " + this.metaRate);
		System.out.println("Current Resources: " + this.resources);
		System.out.println("Max Age: " + this.maxAge);
		System.out.println("Current Age: " + this.currentAge);
		System.out.println("Intermovement: " + this.intermovement + "\n");
	}
		
	public void move(SimulationManager manager)
	{
		//Agent's Current Cell
		Cell current = manager.landscape.getCellAt(this.row, this.col);
		//check if current resources and intermovement time allow for reaching new cell or does it die
		if(death(manager))
		{
			kill(manager);
		}
		//check if died because of age
		
		else
		{
			//it didn't die, so find cell with most resources
			manager.landscape.getCellAt(this.row, this.col).setOccupied(false);
			int[] new_row_col = findMaxResourceCell(current, manager);
			this.setRowCol(new_row_col[0], new_row_col[1]);
			manager.landscape.getCellAt(new_row_col[0], new_row_col[1]).setOccupied(true);
			
			//collect resources
			this.setResources(this.getResourceLevel() + 
					manager.landscape.getCellAt(new_row_col[0], new_row_col[1]).getCurrentResource());
			manager.landscape.getCellAt(new_row_col[0], new_row_col[1]).setCurrentResource(0);
		}
	}
	
	/*
	 * Schedules death.
	 */
	public void kill(SimulationManager manager)
	{
		manager.calendar.add(new Event(this, EventType.die, this.deathTime));
	}
	
	/*
	 * Returns the row and col in an int array with max resources within vision. 
	 */
	public int[] findMaxResourceCell(Cell max, SimulationManager manager)
	{
		Cell temp = max;
		int row = this.row;
		int col = this.col;
		for(int i = 1; i <= this.vision; i ++)
		{
			//right
			temp = manager.landscape.getCellAt(this.row,
					(this.col + i + manager.landscape.getMaxCol()) % manager.landscape.getMaxCol());
			if(max.getCurrentResource() < temp.getCurrentResource() && temp.isOccupied() == false)
			{
				max = temp;
				row = this.row;
				col = (this.col + i + manager.landscape.getMaxCol()) % manager.landscape.getMaxCol();
			}
			
			//left
			temp = manager.landscape.getCellAt(this.row, 
					(this.col - i + manager.landscape.getMaxCol()) % manager.landscape.getMaxCol());
			if(max.getCurrentResource() < temp.getCurrentResource() && temp.isOccupied() == false)
			{
				max = temp;
				row = this.row;
				col = (this.col - i + manager.landscape.getMaxCol()) % manager.landscape.getMaxCol();
			}
			
			//up
			temp = manager.landscape.getCellAt(
					(this.row + i + manager.landscape.getMaxRow()) % manager.landscape.getMaxRow(), this.col);
			if(max.getCurrentResource() < temp.getCurrentResource() && temp.isOccupied() == false)
			{
				max = temp;
				row = (this.row + i + manager.landscape.getMaxRow()) % manager.landscape.getMaxRow();
				col = this.col;
			}
			
			//down
			temp = manager.landscape.getCellAt(
					(this.row - i + manager.landscape.getMaxRow()) % manager.landscape.getMaxRow(), this.col);
			if(max.getCurrentResource() < temp.getCurrentResource() && temp.isOccupied() == false)
			{
				max = temp;
				row = (this.row - i + manager.landscape.getMaxRow()) % manager.landscape.getMaxRow();
				col = this.col;
			}
		}
		return new int[]{row, col};
	}
	
	/*
	 * Returns boolean if the agent died while moving and sets death time
	 */
	public boolean death(SimulationManager manager)
	{
		double x1 = manager.getTime() - this.getIntermovement();
		double x2 = manager.getTime();
		
		double y1 = this.getResourceLevel();
		double y2 = this.getResourceLevel() - this.metaRate * (x2 - x1);
		if(y2 < 0)
		{
			double slope = (y2 - y1) / (x2 - x1);
			double intercept = y2 - (slope * x2);
			
			this.deathTime = -intercept / slope;
			return true;
		}
		return false;
	}
}