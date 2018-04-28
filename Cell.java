
public class Cell
{
	private double RESOURCE_CAP; // may be double? static!!
	private double REGROWTH_RATE;

	private boolean occupied;
	private double resourceLevel;
        private int row;
        private int col;
        private double timeLastUpdated = 0;

	public Cell()
	{
		RESOURCE_CAP = 0;
		REGROWTH_RATE = 0;
		occupied = false;
		resourceLevel = 0;
	}

	public Cell(double resourceCap, double resourceRate,
		boolean occupancy, double resourceLev)
	{
		RESOURCE_CAP = resourceCap;
		REGROWTH_RATE = resourceRate;
		occupied = occupancy;
		resourceLevel = resourceLev;
	}

    public int getRow() { return this.row; }
    public int getCol() { return this.col; }

	public boolean getOccupancy()
	{
		return occupied;
	}

	public double getCapacity()
	{
		return RESOURCE_CAP;
	}

	public double getResource()
	{
		return resourceLevel;
	}

	public void setOccupancy(boolean x)
	{
		occupied = x;
	}

	public void setResource(double level)
	{
		resourceLevel = level;
	}
        
        public void updateCell(double time) {
    
       timeLastUpdated = time;
  }

  public void regrowCell(double time) {
   resourceLevel= Math.min(RESOURCE_CAP, resourceLevel + ((time - timeLastUpdated) * REGROWTH_RATE));
   
  }
}
