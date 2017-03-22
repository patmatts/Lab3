import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

	/**
		MentalMap.java Group 9: Patrick Matts, Levi Sinclair, Sheridan Olds
		The agent's mentalmap graph.
	**/
public class MentalMap {
	
	private HashMap<String, Node> map;
	
	private Queue<String> pathQueue;
	
	Point cheese;
	Boolean rock;
	Boolean door;
	
	//Direction enum
	public enum Direction {
	    NORTH, EAST, SOUTH, WEST
	}
	
	/**
		MentalMap()
		MentalMap Constructor
		Instantiates the fields.
	**/
	public MentalMap()
	{
		map = new HashMap<String, Node>();
		
		insertNode(true, new Point(0,0), true, "b");
		cheese = null;
		rock = false;
		door = false;
	}
	
	/**
		updateMap(String[][] sight, char direction, Point position)
		Runs another BST with an updated body.
	**/
	public void updateMap(String[][] sight, char direction, Point position)
	{
		Node current = map.get(position.toString());
		
		Direction d;
		
		if(direction == 'n')
			d = Direction.NORTH;
		else if(direction == 'e')
			d = Direction.EAST;
		else if(direction == 's')
			d = Direction.SOUTH;
		else
			d = Direction.WEST;
		
		updateMapBody(current, sight, d);
		BFS(position);
	}
	
	/**
		updateMapBody(Node current, String[][] sight, Direction d)
		Rotates the map based on the agent's direction.
	**/
	public void updateMapBody(Node current, String[][] sight, Direction d)
	{
		//System.out.println("At point: " + current.toString());
		
		current = toRight(current, d);
		exploreNode(current.getPoint(), canAccess(sight[1][1], current.getPoint()), sight[1][1]);
		
		current = toRight(current, d);
		exploreNode(current.getPoint(), canAccess(sight[0][1], current.getPoint()), sight[0][1]);
		
		current = toDown(current, d);
		
		
		for(int row = 0; row < 7; row++)
		{
			Node temp = map.get(current.toString());
			
			for(int col = 0; col < 5; col++)
			{
				exploreNode(current.getPoint(), canAccess(sight[row][col], current.getPoint()), sight[row][col]);
				//System.out.print("(" + current.getItems() + ")");
				if(col < 4)
					current = toLeft(current, d);
			}
			current = temp;
			
			if(row < 6)
				current = toUp(current, d);
			//System.out.println();
		}
	}
	
	/**
		BFS(Point pt)
		runs a breadth-first search and sets node values.
	**/
	public void BFS(Point pt)
	{
		Node start = map.get(pt.toString());
		
		for (Node value : map.values()) 
		{
		    value.color = Node.Color.WHITE;
		    value.d = Node.INFINITY;
		    value.parent = null;
		}
		
		start.color = Node.Color.GRAY;
		start.d = 0;
		start.parent = null;
		
		Queue<Node> q = new LinkedList<Node>();
		q.add(start);
		
		while(q.peek() != null)
		{
			Node cur = q.remove();
			
			
			Node west = toWest(cur);
			Node south = toSouth(cur);
			Node east = toEast(cur);
			Node north = toNorth(cur);
			
			if(west.color == Node.Color.WHITE && west.getExplored() == true)
			{
				if(!west.getAccessible())
					west.color = Node.Color.BLACK;
				else
				{
					west.color = Node.Color.GRAY;
					west.d = cur.d + 1;
					west.parent = cur;
					west.parentDir = "w";
					q.add(west);
				}
			}
			
			if(east.color == Node.Color.WHITE && east.getExplored() == true)
			{
				if(!east.getAccessible())
					east.color = Node.Color.BLACK;
				else
				{
					east.color = Node.Color.GRAY;
					east.d = cur.d + 1;
					east.parent = cur;
					east.parentDir = "e";
					q.add(east);
				}
			}
			
			if(north.color == Node.Color.WHITE && north.getExplored() == true)
			{
				if(!north.getAccessible())
					north.color = Node.Color.BLACK;
				else
				{
					north.color = Node.Color.GRAY;
					north.d = cur.d + 1;
					north.parent = cur;
					north.parentDir = "n";
					q.add(north);
				}
			}
			
			if(south.color == Node.Color.WHITE && south.getExplored() == true)
			{
				if(!south.getAccessible())
					south.color = Node.Color.BLACK;
				else
				{
					south.color = Node.Color.GRAY;
					south.d = cur.d + 1;
					south.parent = cur;
					south.parentDir = "s";
					q.add(south);
				}
			}
			
			cur.color = Node.Color.BLACK;
				
		}
		
	}
	
	/**
		BFS(Point pt)
		runs a breadth-first search with Narrows in mind.
	**/
	public void BFSNar(Point pt)
	{
		Node start = map.get(pt.toString());
		
		for (Node value : map.values()) 
		{
		    value.color = Node.Color.WHITE;
		    value.d = Node.INFINITY;
		    value.parent = null;
		}
		
		start.color = Node.Color.GRAY;
		start.d = 0;
		start.parent = null;
		
		Queue<Node> q = new LinkedList<Node>();
		q.add(start);
		
		while(q.peek() != null)
		{
			Node cur = q.remove();
			
			
			Node west = toWest(cur);
			Node south = toSouth(cur);
			Node east = toEast(cur);
			Node north = toNorth(cur);
			
			if(west.color == Node.Color.WHITE && west.getExplored() == true)
			{
				if(!west.getAccessible() || west.getItems().contains(Node.narrows))
					west.color = Node.Color.BLACK;
				else
				{
					west.color = Node.Color.GRAY;
					west.d = cur.d + 1;
					west.parent = cur;
					west.parentDir = "w";
					q.add(west);
				}
			}
			
			if(east.color == Node.Color.WHITE && east.getExplored() == true)
			{
				if(!east.getAccessible() || east.getItems().contains(Node.narrows))
					east.color = Node.Color.BLACK;
				else
				{
					east.color = Node.Color.GRAY;
					east.d = cur.d + 1;
					east.parent = cur;
					east.parentDir = "e";
					q.add(east);
				}
			}
			
			if(north.color == Node.Color.WHITE && north.getExplored() == true)
			{
				if(!north.getAccessible() || north.getItems().contains(Node.narrows))
					north.color = Node.Color.BLACK;
				else
				{
					north.color = Node.Color.GRAY;
					north.d = cur.d + 1;
					north.parent = cur;
					north.parentDir = "n";
					q.add(north);
				}
			}
			
			if(south.color == Node.Color.WHITE && south.getExplored() == true)
			{
				if(!south.getAccessible()  || south.getItems().contains(Node.narrows))
					south.color = Node.Color.BLACK;
				else
				{
					south.color = Node.Color.GRAY;
					south.d = cur.d + 1;
					south.parent = cur;
					south.parentDir = "s";
					q.add(south);
				}
			}
			
			cur.color = Node.Color.BLACK;
				
		}
		
	}
	
	/**
		itemSearch(String s)
		searches through the mentalmap and returns a list where the item s is found
	**/
	public ArrayList<Point> itemSearch(String s)
	{
		ArrayList<Point> list = new ArrayList<Point>();
		
		for (Node value : map.values()) 
		{
			String temp = value.getItems();
			
			if(value.getExplored())
			    if(temp.contains(s))
			    	list.add(value.getPoint());
		}
		
		return list;
	}
	
	/**
		nearestUnexploredNode()
		returns the point where the nearest unexplored node is.
	**/
	public Point nearestUnexploredNode()
	{
		Node minD = null;
		
		for (Node value : map.values())
		{
			if(value != null)
			{
				if(value.d < 10000)
					if(minD == null)
					{
						if(isUnexplored(value))
							minD = value;
					}
					else if(value.d < minD.d && isUnexplored(value))
						minD = value;
			}
		}
		
		if(minD != null)
			return minD.getPoint();
		else
			return null;
	}
	
	/**
		isUnexplored(Node v)
		returns true if the node has adjacent nodes that are unexplored
	**/
	private boolean isUnexplored(Node v)
	{
		if(!toEast(v).getExplored())
			return true;
		else if(!toWest(v).getExplored())
			return true;
		else if(!toSouth(v).getExplored())
			return true;
		else if(!toNorth(v).getExplored())
			return true;
		else
			return false;
	}
	
	/**
		insertUnexploredNode(Point pt)
		inserts a node at pt that is unexplored into the map
	**/
	private void insertUnexploredNode(Point pt)
	{
		Node n = new Node(false, pt);
		
		map.put(n.toString(), n);
	}
	
	/**
		exploreNode(Point pt, boolean access, String items)
		Set the node as explored as you explore it
	**/
	public void exploreNode(Point pt, boolean access, String items)
	{
		
		Node n = map.get(pt.toString());
		
		if(items.contains("@"));
			rock = true;
		if(items.contains("#"));
			door = true;
		
		n.setAccessible(access);
		n.setItems(items);
		
		if(n.getExplored())
			return;
		
		n.explore();
		
		if(items.contains("+"))
			cheese = n.getPoint();
		
		if(!toWestB(n))
			insertUnexploredNode(n.getPoint().west());
		
		if(!toEastB(n))
			insertUnexploredNode(n.getPoint().east());
		
		if(!toNorthB(n))
			insertUnexploredNode(n.getPoint().north());
		
		if(!toSouthB(n))
			insertUnexploredNode(n.getPoint().south());
		
		
	}
	
	/**
		insertNode(boolean explored, Point pt, boolean access, String items)
		inserts a node with the indicated fields
	**/
	public void insertNode(boolean explored, Point pt, boolean access, String items)
	{
		Node n = new Node(explored, pt, access, items);
		
		map.put(n.toString(), n);
		
		if(!toWestB(n))
			insertUnexploredNode(n.getPoint().west());
		
		if(!toEastB(n))
			insertUnexploredNode(n.getPoint().east());
		
		if(!toNorthB(n))
			insertUnexploredNode(n.getPoint().north());
		
		if(!toSouthB(n))
			insertUnexploredNode(n.getPoint().south());
			
	}
	
	/**
		findPath(Point startP, Point destP, boolean narrowsMatter)
		finds the shortest path from startP to destP
		if narrowsMatter is flagged, then doesn't ignore narrows
	**/
	public Queue<String> findPath(Point startP, Point destP, boolean narrowsMatter)
	{
		pathQueue = new LinkedList<String>();
		
		Node start = map.get(startP.toString());
		Node dest = map.get(destP.toString());
		
		path(start, dest, narrowsMatter);
		
		return pathQueue;
	}
	
	/**
		path(Node start, Node dest, boolean narrowsMatter)
		helper function for findPath
	**/
	private void path(Node start, Node dest, boolean narrowsMatter)
	{
		
		if(dest == start)
			;
		else if(dest.parent == null)
			return;
		else
		{
			
			path(start, dest.parent, narrowsMatter);
			pathQueue.add(dest.parentDir);
		}
		
	}
	
	public Boolean toWestB(Node node)
	{
		String key = node.getPoint().west().toString();
		if(map.get(key) != null)
			return true;
		else
			return false;
	}
	
	public Node toWest(Node node)
	{
		String key = node.getPoint().west().toString();
		return map.get(key);
	}
	
	public Node toRight(Node node, Direction d)
	{
		Point p = node.getPoint().copy();
		
		int xChange = 0;
		int yChange = 0;
		
		if(d == Direction.NORTH)
			xChange++;
		else if(d == Direction.WEST)
			yChange++;
		else if(d == Direction.SOUTH)
			xChange--;
		else
			yChange--;
		
		p.x = p.x + xChange;
		p.y = p.y + yChange;
		
		String s = p.toString();
		Node n = map.get(s);
		
		return n;
		
	}
	
	public Node toLeft(Node node, Direction d)
	{
		Point p = node.getPoint().copy();
		
		int xChange = 0;
		int yChange = 0;
		
		if(d == Direction.NORTH)
			xChange--;
		else if(d == Direction.WEST)
			yChange--;
		else if(d == Direction.SOUTH)
			xChange++;
		else
			yChange++;
		
		p.x = p.x + xChange;
		p.y = p.y + yChange;
		
		String s = p.toString();
		Node n = map.get(s);
		
		return n;
		
	}
	
	public Node toUp(Node node, Direction d)
	{
		Point p = node.getPoint().copy();
		
		int xChange = 0;
		int yChange = 0;
		
		if(d == Direction.NORTH)
			yChange++;
		else if(d == Direction.WEST)
			xChange--;
		else if(d == Direction.SOUTH)
			yChange--;
		else
			xChange++;
		
		p.x = p.x + xChange;
		p.y = p.y + yChange;
		
		String s = p.toString();
		Node n = map.get(s);
		
		return n;
		
	}
	
	public Node toDown(Node node, Direction d)
	{
		Point p = node.getPoint().copy();
		
		int xChange = 0;
		int yChange = 0;
		
		if(d == Direction.NORTH)
			yChange--;
		else if(d == Direction.WEST)
			xChange++;
		else if(d == Direction.SOUTH)
			yChange++;
		else
			xChange--;
		
		p.x = p.x + xChange;
		p.y = p.y + yChange;
		
		String s = p.toString();
		Node n = map.get(s);
		
		return n;
		
	}
	
	public Boolean toEastB(Node node)
	{
		String key = node.getPoint().east().toString();
		if(map.get(key) != null)
			return true;
		else
			return false;
	}
	
	public Node toEast(Node node)
	{
		String key = node.getPoint().east().toString();
		return map.get(key);
	}
	
	public Boolean toSouthB(Node node)
	{
		String key = node.getPoint().south().toString();
		if(map.get(key) != null)
			return true;
		else
			return false;
	}
	
	public Node toSouth(Node node)
	{
		String key = node.getPoint().south().toString();
		return map.get(key);
	}
	
	public Boolean toNorthB(Node node)
	{
		String key = node.getPoint().north().toString();
		if(map.get(key) != null)
			return true;
		else
			return false;
	}
	
	public Node toNorth(Node node)
	{
		String key = node.getPoint().north().toString();
		return map.get(key);
	}
	
	/**
		canAccess(String s, Point p)
		returns true if the point can be accessed
	**/
	public boolean canAccess(String s, Point p)
	{
		if(s.contains("*"))
			return false;
		else if(s.contains("@"))
		{
			rock = true;
			return false;
		}
		else if(s.contains("#"))
		{
			door = true;;
			return false;
		}
		else
			return true;
	}
	
	/**
		printMap()
		prints the mental map in chunks
	**/
	public void printMap()
	{
		Node n = map.get(new Point(1, 2).toString());
		System.out.println(n.d);
	}
	
	/**
		getCheese()
		Returns the point that cheese is at.
	**/
	public Point getCheese()
	{
		return cheese;
	}
	
	
}
