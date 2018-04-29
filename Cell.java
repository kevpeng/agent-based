import java.util.*;

public class Cell
{

    // RESOURCE VALUES
    private static double RESOURCE_CAP; // may be double? static!!
    private static double REGROWTH_RATE;

    // variables for each cell
    private boolean occupied;
    private double resourceLevel;
    private int row;
    private int col;
    private double timeLastUpdated = 0;

    // default constructor
    public Cell()
    {
     /*   RESOURCE_CAP = 0;
        REGROWTH_RATE = 0;
        occupied = false;
        resourceLevel = 0;
        */
        occupied = false;
        resourceLevel = 0;
    }

    // constructor that takes in stuff
    public Cell(double resourceCap, double resourceRate,
            boolean occupancy, double resourceLev)
    {
        RESOURCE_CAP = resourceCap;
        REGROWTH_RATE = resourceRate;
        occupied = occupancy;
        resourceLevel = resourceLev;
    }

    // getters
    public int getRow() { return this.row; }
    public int getCol() { return this.col; }
    public boolean getOccupancy()   { return occupied; }
    public double getCapacity()     { return RESOURCE_CAP; }
    public double getRegrowthRate() { return REGROWTH_RATE; }
    public double getResource()     { return resourceLevel; }

    // setters
    public void setOccupancy(boolean boo)   { occupied = boo; }
    public void setResource(double level) { resourceLevel = level; }

    // mutators
    public void updateCell(double time)   { timeLastUpdated = time; }
    public void regrowCell(double time)   {  
        resourceLevel = Math.min(RESOURCE_CAP, resourceLevel + 
                ((time - timeLastUpdated) * REGROWTH_RATE));
    }
}
