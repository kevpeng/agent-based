import java.util.*;

public class Landscape
{
    // variables
	private Cell[][] cellList;
	private int maxRow;
	private int maxCol;
	
    // generate the landscape based on given dimensions.
	public Landscape(int row, int col)
	{
		maxRow = row;
		maxCol = col;
		cellList = new Cell[row][col];
		for(int i = 0; i < row; i++)
		{
			for(int j = 0; j < col; j++)
			{
                // each cell gets a value based on a gaussian and 1 unit of regrowth 
				cellList[i][j] = new Cell(getGaussian(i, j), 1); 
			}
		}
	}
	
	/* Getter Methods */
	public int getMaxRow() { return this.maxRow; }
	public int getMaxCol() { return this.maxCol; }
	
	private double getGaussian(int row, int col)
	{
		return getFunction(row - this.maxRow/4, col - this.maxCol/4) + 
			getFunction(row - 3*this.maxRow/4, col - 3*this.maxCol/4);
	}
	
	private double getFunction(double x, double y)
	{
		double thetaX = (double) 0.3 * this.maxRow;
		double thetaY = (double) 0.3 * this.maxCol;
		double exp = -Math.pow((x/thetaX), 2) -Math.pow((y/thetaY), 2);
		return 4 * (Math.exp(exp));
	}
	
	/* Returns the cell at the given row and column */
	public Cell getCellAt(int row, int col) { return cellList[row][col]; }
	
	/* Prints information about the entire landscape */
	public void print()
	{
		for(int i = 0; i < this.maxRow; i++) {
			for(int j = 0; j < this.maxCol; j++) {
				cellList[i][j].print();
			}
		}
	}
}
