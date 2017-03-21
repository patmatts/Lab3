
public class Node {
	
	public enum Color {
		WHITE, GRAY, BLACK
	}
	
	public static final int INFINITY = 10000;
	
	public static final String narrows = "=";
	
	private Point location;
	
	private boolean explored;
	private boolean access;
	private String items;
	
	public Color color;
	public int d;
	public Node parent;
	public String parentDir;
	
	public Node(boolean explored, Point pt)
	{
		this.explored = explored;
		this.location = pt;
	}
	
	public Node(boolean explored, Point pt, boolean access, String items)
	{
		this(explored, pt);
		this.access = access;
		this.items = items;
	}
	
	public Point getPoint()
	{
		return location;
	}
	
	public boolean getExplored()
	{
		return explored;
	}
	
	public void explore()
	{
		explored = true;
	}
	
	public void setAccessible(boolean access)
	{
		this.access = access;
	}
	
	public boolean getAccessible()
	{
		return access;
	}
	
	public String getItems()
	{
		return items;
	}
	
	public void setItems(String items)
	{
		this.items = items;
	}
	
	public String toString()
	{
		return location.toString();
	}
	
}
