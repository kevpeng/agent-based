package abs;
import java.util.*;

public class Agent
{
    // initialize rand
    static Random rand = new Random(8675309);
    
    // identifier for the agent
    private String id; 
    
    // position variables
    private int row;
    private int col;
    
    // characteristics for an agent
    private int vision;
    private double wealth;
    private double metabolicRate;
    private double intermovement;
    private double currentAge;
    private double maxAge;

    // constructor
    Agent(String id)
    {
        // initialize id/position
        this.id = id;


        // initialize characteristics
        this.vision = rand.nextInt(6) + 1;
        this.wealth = getRandomDouble(5, 25);
        this.metabolicRate = getRandomDouble(1,4);
        this.intermovement = getNextTime();
        this.currentAge = 0;
        this.maxAge = getRandomDouble(60,100);
    }

    // helper function to get a double
    public static double getRandomDouble(double min, double max) {
        return min + (rand.nextDouble() * (max - min));
    }
    
    public static double getNextTime() {
        return Math.log(1 - rand.nextDouble())/(-1);
    }


    // simple accessor methods below
    public String getID()  { return this.id;  }
    public int    getRow() { return this.row; }
    public int    getCol() { return this.col; }
    public int    getVision() { return this.vision; }
    public double getWealth() { return this.wealth; }
    public double getMetabolicRate() { return this.metabolicRate; }
    public double getIntermovement() { return this.intermovement; }
    public double getCurrentAge() { return this.currentAge; }
    public double getMaxAge() { return this.maxAge; }

    public void print() {
        System.out.println("ID: " + this.getID());
        System.out.println("Location: " + this.getRow() + ", " + this.getCol());
        System.out.println("Vision: " + this.getVision());
        System.out.println("Current Wealth: " + this.getWealth());
        System.out.println("Metabolic rate: " + this.getMetabolicRate());
        System.out.println("Intermovement time: " + this.getIntermovement());
        System.out.println("Current Age: " + this.getCurrentAge());
        System.out.println("Max age: " + this.getMaxAge());
    }

    // simple mutator methods below
    
    // Update the position!!
    public void setRowCol(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // getting more wealth!!
    public void collect(Cell C) {
        this.wealth += C.getResource();
        C.setResource(0);
    }

    // set new intermovement time
    public void setNewTime() { this.intermovement = Math.exp(1); }

    // move agent to new 
    public void move(Landscape land)
    {
        Cell C = look(this.getVision(), land);                     // check for place to go
        this.setRowCol(C.getRow(), C.getCol());                    // move to that location
        land.getCellAt(C.getRow(), C.getCol()).setOccupancy(true); // set the landscape's cell to occupied
        this.collect(land.getCellAt(C.getRow(), C.getCol()));      // collect resources
         this.setNewTime();                                         // update timer for the agent
        
    }

    public Cell look(int vision, Landscape landscape)
    {
        Cell max = new Cell();
        int n = landscape.getMaxDimension();
        for(int i = 1; i <= vision; i++) { // run the vision algorithm.

            // Temp looks for the max valid cell
            Cell temp = landscape.getCellAt(this.row, helperPlus(this.col, n));           
            if( temp.getResource() > max.getResource() && temp.getOccupancy() == false ) 
            { max = landscape.getCellAt(this.row, helperPlus(this.col, n)); }
            
            temp = landscape.getCellAt(this.row, helperMinus(this.col, n)); 
            if( temp.getResource() > max.getResource() && temp.getOccupancy() == false ) 
            { max = landscape.getCellAt(this.row, helperMinus(this.col, n)); }
            
            temp = landscape.getCellAt(helperPlus(this.row, n), this.col); 
            if( temp.getResource() > max.getResource() && temp.getOccupancy() == false ) 
            { max = landscape.getCellAt(  helperPlus(this.row, n), this.col); }
            
            temp = landscape.getCellAt(helperMinus(this.row, n), this.col);
            if( temp.getResource() > max.getResource() && temp.getOccupancy() == false ) 
            { max = landscape.getCellAt(helperMinus(this.row, n), this.col); }
        }
        return max; // returns max valid cell
    }

    // calculates where the cell looks positively (donut array)
    public int helperPlus(int x, int max)
    { return ((x + this.vision + max) % max ); }

    // calculates where the cell looks negatively (donut array)
    public int helperMinus(int x, int max)
    { return ((x - this.vision + max) % max ); }

}
