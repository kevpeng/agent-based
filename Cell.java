import java.util.*;

public class Cell
{
	private boolean occupied;
	private double resourceCap;
	private double resourceRegrowth;
	private double currentResource;
	
	Cell(double cap, double growth)
	{
		this.occupied = false;
		this.resourceCap = cap;
		this.resourceRegrowth = growth;
		this.currentResource = cap;
	}
	
	/* Getter Methods */
	public boolean isOccupied()
	{
		return this.occupied;
	}
	
	public double getCapacity()
	{
		return this.resourceCap;
	}
	
	public double getResourceRegrowth()
	{
		return this.resourceRegrowth;
	}
	
	public double getCurrentResource()
	{
		return this.currentResource;
	}
	
	/* Setter Methods */
	public void setOccupied(boolean isOccupied)
	{
		this.occupied = isOccupied;
	}
	
	public void setCurrentResource(double resourceLevel)
	{
		this.currentResource = resourceLevel;
	}
	
	/* Print method to show info about cell */
	public void print()
	{
		System.out.println("Occupied: " + this.isOccupied());
		System.out.println("Resource Capacity: " + this.getCapacity());
		System.out.println("Resource Regrowth: " + this.getResourceRegrowth());
		System.out.println("Current Resource: " + this.getCurrentResource() + "\n");
	}
}