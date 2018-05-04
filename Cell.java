import java.util.*;

public class Cell
{
    // characteristics of a cell
	private double RESOURCE_CAP;;
	private double RESOURCE_REGROWTH;;
	
    private boolean occupied;
	private double currentResource;
	
    // initialize a cell with a capacity and a regrowth rate.
	Cell(double cap, double growth)
	{
		this.occupied = false;
		this.RESOURCE_CAP = cap;
		this.RESOURCE_REGROWTH = growth;
		this.currentResource = cap;
	}
	
	/* Getter Methods */
	public boolean isOccupied() { return this.occupied; }
	public double getCapacity() { return this.RESOURCE_CAP; }
	public double getResourceRegrowth() { return this.RESOURCE_REGROWTH; }
	public double getCurrentResource(){ return this.currentResource; }
	
	/* Setter Methods */
	public void setOccupied(boolean isOccupied)	{ this.occupied = isOccupied; }
	public void setCurrentResource(double resourceLevel) { this.currentResource = resourceLevel; }
	
	/* Print method to show info about cell */
	public void print()
	{
		System.out.println("Occupied: " + this.isOccupied());
		System.out.println("Resource Capacity: " + this.getCapacity());
		System.out.println("Resource Regrowth: " + this.getResourceRegrowth());
		System.out.println("Current Resource: " + this.getCurrentResource() + "\n");
	}
}
