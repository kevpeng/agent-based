public class Landscape
{
	private int MAX_DIMENSION;
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
				landscape[i][j] = new Cell(10, 0.75, false, 4);
			}
		}
	}

//	public void setCell(int row, int col)
//	{
//		landscape[row][col] = new Cell
//	}

	public Cell getCellAt(int row, int col)
	{
		return landscape[row][col];
	}

	public int getMaxDimension()
	{
		return MAX_DIMENSION;
	}


}
