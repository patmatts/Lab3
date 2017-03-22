/**
	Point.java
	An x,y coordinate.
**/
public class Point {
	public int x;
	public int y;
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Point west()
	{
		return new Point(x - 1, y);
	}
	
	public Point east()
	{
		return new Point(x + 1, y);
	}
	
	public Point north()
	{
		return new Point(x, y + 1);
	}
	
	public Point south()
	{
		return new Point(x, y - 1);
	}
	
	public String toString()
	{
		return x + " " + y;
	}
	
	public Point copy()
	{
		Point p = new Point(x, y);
		
		return p;
	}
	
}
