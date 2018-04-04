public class Agent
{
    private String id;                // identifier for the agent
    private int row; private int col; // x, y location values
    private int vision;               // field of view
    private double wealth;            // how much food it has
    private double met_rate;          // how quickly food is eaten
    private double max_age;              // maximum age

    // Constructor
    Agent(String id)
    {
        this.id = id;
    }

    // Constructor with a starting location
    Agent(String id, int row, int col)
    {
        this.id = id;
        this.row = row;
        this.col = col;

        // initialize values 
        this.vision   = (int)(Math.random()*6) + 1; // discrete uniform (1,6)
        this.met_rate = Math.random()*4  + 1;       // uniform (1,4)
        this.wealth   = Math.random()*25 + 5;       // uniform (5, 25)
        this.max_age  = Math.random()*60 + 100;     // uniform (60,100)
    }

    // simple accessor methods below
    public int    getRow() { return this.row; }
    public int    getCol() { return this.col; }
    public String getID()  { return this.id;  }
    public int    getVision() { return this.vision;   }
    public double getWealth() { return this.wealth;   }
    public double getMetRate(){ return this.met_rate; }
    public int    getMaxAge() { return this.max_age;  }

    // simple mutator methods below
    public void setRowCol(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    // to do: account for ties
    public Cell look(Landscape landscape)
    {
        Cell max = new Cell();
        int n = landscape.getMaxDimension();
        for(int i = 1; i <= this.vision; i++) { // run the vision algorithm.
          if(landscape.getCellAt(this.row, helperPlus(this.col, n)).getResource() > max.getResource()) {
            max = landscape.getCellAt(this.row, helperPlus(this.col, n));
          }
          if(landscape.getCellAt(this.row, helperMinus(this.col, n)).getResource() > max.getResource()) {
            max = landscape.getCellAt(this.row, helperMinus(this.col, n));
          }
          if(landscape.getCellAt(helperPlus(this.row, n), this.col).getResource() > max.getResource()) {
            max = landscape.getCellAt(  helperPlus(this.row, n), this.col);
          }
          if(landscape.getCellAt(helperMinus(this.row, n), this.col).getResource() > max.getResource()) {
            max = landscape.getCellAt(helperMinus(this.row, n), this.col);
          }
        }

        return max; // returns max valid cell
    }

    // calculates where the cell looks positively (donut array)
    public int helperPlus(int x, int max)
    {
      return ((x + this.vision + max) % max );
    }

    // calculates where the cell looks negatively (donut array)
    public int helperMinus(int x, int max)
    {
      return ((x - this.vision + max) % max );
    }



}

//movement rule so agent can successfully look (testing border conditions too)
//find closest unocccupied cell with max resources
// (row - vision + NUM_ROWS) % NUM_ROWS
