import java.util.*;

public class Agent
{
    // generate a random
    static Random rng = new Random(1);	

    // identifiers for agent
    private String id;    
    private int row;
    private int col;

    // values each agent gets
    private int vision;
    private double maxAge;
    private double metaRate;
    private double resources;

    //varibles for handle events
    private double intermovement;
    private double deathTime;
    private double minTime;


    // extension variable
    private boolean hasDisease; 

    // initialize
    // used for agents that were generated when some agent died
    Agent(String id, double time)
    {
        this.id = id;
        this.vision = (int) getUniform(1, 6, true);
        this.metaRate = getUniform(1, 4, false);
        this.resources = getUniform(5, 25, false);
        this.intermovement = time+ getNewIntermovement();
        this.maxAge = time + getUniform(60, 100, false);
        this.deathTime = time + (resources / metaRate);
        this.deathTime = Math.min(this.deathTime, maxAge);
        this.minTime = Math.min(intermovement, deathTime);

        int random = (int)getUniform(1,100, false);
        if(random <= 80)
        { this.hasDisease = false; }
        else
            // having a disease comes with a few consequences :(
        { 
            this.hasDisease = true; 
            this.maxAge = this.maxAge / 2;      // life expectancy cut in half
            this.metaRate = this.metaRate * 2;  // your body needs more resources to survive
        }
    }

    // generates a random uniform number between a range.
    // If it is for vision, return a random uniform integer instead.
    private double getUniform(int lower, int upper, boolean isVision)
    {
        if(isVision == true){
            double x = (double) (rng.nextDouble() * (upper + 1 - lower)) + lower;
            return (int) x;
        }
        double x = (double) (rng.nextDouble() * (upper - lower)) + lower;
        return x;
    }

    //set up checking the the next event type 
    //if return 0, then it would move else it jump to death event
    public int getNextEventType() {
        if(minTime == intermovement){
            return 0;
        }

        else{

            return 1;
        }
    }

    // generates a random exp(1)	
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


    public double getminTime()
    {
        return this.minTime;
    }

    public boolean getHasDisease()
    {
        return this.hasDisease;
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


    public void setRowCol(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    // would be used for spreading disease...
    public void setHasDisease(boolean boo)
    {
        this.hasDisease = boo;
        if(this.hasDisease == true)
        { 
            this.maxAge = this.maxAge / 2;      // life expectancy cut in half
            this.metaRate = this.metaRate * 2;  // your body needs more resources to survive
        }
    }


    /* Prints information about agent */
    public void print()
    {
        System.out.println("ID: " + this.id);
        System.out.println("Location: (" + this.getRow() + ", " + this.getCol() + ")");
        System.out.println("Vision: " + this.vision);
        System.out.println("Metabolic Rate: " + this.metaRate);
        System.out.println("Current Resources: " + this.resources);
        System.out.println("Intermovement: " + this.intermovement);
        System.out.println("Has a disease? " + this.hasDisease + "\n");
    }


    // move the agent!  
    public void move(SimulationManager manager)
    {

        //Agent's Current Cell
        Cell current = manager.landscape.getCellAt(this.row, this.col);
        
        //it didn't die, so find cell with most resources
        manager.landscape.getCellAt(this.row, this.col).setOccupied(false);
        int[] new_row_col = findMaxResourceCell(current, manager);
        this.setRowCol(new_row_col[0], new_row_col[1]);
        manager.landscape.getCellAt(new_row_col[0], new_row_col[1]).setOccupied(true);

        //collect resources
        this.setResources(this.getResourceLevel() + 
                manager.landscape.getCellAt(new_row_col[0], new_row_col[1]).getCurrentResource());
        manager.landscape.getCellAt(new_row_col[0], new_row_col[1]).setCurrentResource(0);

        //update the intermovment after moving 
        this.intermovement = minTime + getNewIntermovement();
        this.deathTime = minTime + (resources / metaRate);
        this.deathTime = Math.min(this.deathTime, maxAge);
        this.minTime = Math.min(intermovement, deathTime);
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

}
