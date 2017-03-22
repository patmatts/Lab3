/**
		Node.java
		Node class to be used by the mental map.
	**/
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
	
	/**
		Node(boolean explored, Point pt)
		Node constructor
		Sets if it is already explored and the point it is located
	**/
	public Node(boolean explored, Point pt)
	{
		this.explored = explored;
		this.location = pt;
	}
	
	/**
		Node(boolean explored, Point pt, boolean access, String items)
		Node constructor
		Sets if it is already explored, the point it is located, if it is accessable, and the items in it
	**/
	public Node(boolean explored, Point pt, boolean access, String items)
	{
		this(explored, pt);
		this.access = access;
		this.items = items;
	}
	
	/**
		getPoint()
		Returns the point where the node is located
	**/
	public Point getPoint()
	{
		return location;
	}
	
	/**
		getExplored()
		Returns whether the node has been explored or not
	**/
	public boolean getExplored()
	{
		return explored;
	}
	
	/**
		explore()
		Sets explored to true.
	**/
	public void explore()
	{
		explored = true;
	}
	
	/**
		setAccessable(boolean access)
		Sets the access of the node.
	**/
	public void setAccessible(boolean access)
	{
		this.access = access;
	}
	
	/**
		getAccessable()
		Gets the accessability of the node.
	**/
	public boolean getAccessible()
	{
		return access;
	}
	
	/**
		getItems()
		Returns the string of items of the node.
	**/
	public String getItems()
	{
		return items;
	}
	
	/**
		setItems()
		Sets the string of items in the node.
	**/
	public void setItems(String items)
	{
		this.items = items;
	}
	
	/**
		toString()
		Returns the point as a string
	**/
	public String toString()
	{
		return location.toString();
	}
	
}
