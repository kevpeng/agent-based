package abs;

import java.util.Random;

public class Landscape
{
	private int MAX_DIMENSION = 40;
	private Cell [][] landscape; // instance of the landscape 2x2 array

/*	public Landscape()
	{
		landscape = new Cell[][];
	} */

	public Landscape(int row, int col)
	{
		landscape = new Cell[row][col];

		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				landscape[i][j] = new Cell(getGaussian(i,j), 0.75, false, 4);
			}
		}
	}


        
        
	public double getGaussian(int x, int y) {
    return gaussianHelper(x - MAX_DIMENSION/4, y - MAX_DIMENSION/4) + gaussianHelper(x - 3*MAX_DIMENSION/4, y - 3*MAX_DIMENSION/4);
  }

	private double gaussianHelper(double x, double y) {
    double thetaX = 0.4 * MAX_DIMENSION;
    double thetaY = 0.4 * MAX_DIMENSION;
    return Math.pow(4.0, - ((x/thetaX)*(x/thetaX)) - ((y/thetaY)*(y/thetaY)));
  }

	public Cell getCellAt(int row, int col)
	{
		return landscape[row][col];
	}

	public int getMaxDimension()
	{
		return MAX_DIMENSION;
	}


}