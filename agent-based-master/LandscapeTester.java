import java.io.*;

public class LandscapeTester
{
	public static void main(String[] args) 
	{
		Landscape landscape = new Landscape(5, 5);
		Cell temp = landscape.getCellAt(0,0);
		System.out.println(temp.getResource());
		System.out.println(temp.getOccupancy());
		System.out.println("hello");
	}
}
