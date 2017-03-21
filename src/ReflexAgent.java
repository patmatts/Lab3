// CS 455: AI, Group 8: Patrick Matts, Levi Sinclair, Sheridan Olds
//ReflexAgent.java contains the reflexagent and the protocols to connect to the Maeden Simulator Environment

//note to other members: will try to clean up program, eventually I want ReflexAgent to just deal with 
//controlling the agent and will set up a class called MaedenClient.java to handle initial connection
//and contain the main function, many things will be static for now

//test changes



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ReflexAgent {
	
	//values for mental map
	char direction;
	Point position;
	MentalMap mm;
	
	//current subgoal to complete
	Queue<String> path;
	
	// constants to indicate errors or conditions for the agent
	private final int ERROR = -1;
	private final int SUCCESS = 0;
	private final int END = 1;
	private final int DIE  = 2;
	private final int CONTINUE = 3;
	
	//in and out protocol
	private DataOutputStream dOut;
	private BufferedReader in;
	
	//the number the agent was assigned
	private int agentNum;
	
	//class variables which store sensor information
	private char smell;
	private char[] inventory;
	private String[][] sight;
	private char[] ground;
	private String[] messages;
	private int energy;
	private String status;
	private int time;
	
	//basic constructor initializes arrays
	public ReflexAgent()
	{
		inventory = new char[10];
		ground = new char[10];
		messages = new String[10];
		direction = 'w';
		position = new Point(0, 0);
		mm = new MentalMap();
		path = new LinkedList<String>();
	}
	
	//constructor to be called by MaedenClient class in order to set in and out protocol and agent number
	public ReflexAgent(BufferedReader in, DataOutputStream dOut, int agentNum)
	{
		this();
		this.in = in;
		this.dOut = dOut;
		
		this.agentNum = agentNum;
	}
	
	//main body of reflex agent, uses a long decision tree going over common obstacles the agent might experience
	//function reads in data before executing command, does not store any data from previous actions
	//if there is an error reading in data the agent main returns an error
	public int reflexMain() throws IOException, UnknownHostException
	{
		// agentStatus indicates whether agent is working or what finished condition in creates
		int agentStatus = CONTINUE;
		//initalize action to execute, default is forward
		String cmd = "f";
		//number for random number generators
		int num;
		
		do
		   {	    
			   
			   agentStatus = readSensors();
			   mm.updateMap(sight, direction, position);
			   //mm.printMap();
			   System.out.println(energy);
			   //if agent status is finished return the status
			   if(agentStatus != CONTINUE)
				   return agentStatus;
			   
			   //print sensor data and sight data if agent fails an action
			   if(status.equals("fail"))
			   {
				   printSensors();
				   printSight();
			   }
			   
			   //test to see if item is nearby
			   String itemDir = itemNearby();
			   
			   if(!cheesePath())
			   {
				   if(path.peek() == null)
					   avoidObstacle();
				   if(haveHammer() && !objectAdjacent("@"))
					   pathToRock();
				   if(haveKey() && !objectAdjacent("#"))
					   pathToDoor();
				   if(!haveKey())
					   pathToKey();
				   if(!haveHammer())
					   pathToHammer();
			   }
			   
			   mm.BFS(position);
				
			   //if have cheese, use it
			   if(haveCheese())
				  cmd  = "u";
			 
			   
			   //this block deals with narrows and generally just tries to go into them and drops stuff before
			   else if(sight[2][2].contains("=") && haveHammer())
				  cmd = "d T";
			   else if(sight[2][2].contains("=") && haveKey())
				   cmd = "d K";
			  // else if (sight[1][1].contains("="))
					 // cmd = "r";
			  // else if (sight[1][3].contains("="))
					  //cmd = "l";
			   
			   else if(pickupItem()&& !sight[2][2].contains("="))
				   cmd = "g";
			   
			   else if(path.peek() != null)
				   cmd = executePath();
			   
			   //if  there is something to pickup execute grab
			   //else if(pickupItem() && !sight[2][2].contains("="))
				  // cmd = "g";
			   
			  //if there is a rock in front and have hammer use hammer
			  else if(sight[2][2].contains("@") && haveHammer())
				  cmd = "u T";
			   
			  //if there is a door in front and have key use key
			  else if(useKey(getLoc(2, 2)))
				  cmd = "u K";
			   
			   //turn in direction of door if have a key
			  else if(useKey(getLoc(1, 3)) || useHammer(getLoc(1, 3)))
				  cmd = "l";
			   
			 //turn in direction of door if have a key
			  else if(useKey(getLoc(1, 1)) || useHammer(getLoc(1, 1)))
				  cmd = "r";
			   
			 //turn in direction of door if have a key
			  else if(useKey(getLoc(0, 1)) || useHammer(getLoc(0, 1)))
				  cmd = "r";
			   
			  //if there is an item nearby move in direction of item
			  else if(!itemDir.equals("e"))
				   cmd = itemDir;
			   
			  else if(energy < 1600 && mm.nearestUnexploredNode() != null && path.peek() == null)
			  {
				  Point p = mm.nearestUnexploredNode();
				  
				  if(p != null)
				  {
					  mm.BFS(position);
					  path = mm.findPath(position, p, false);
				  }
				  
				  if(path.peek() != null)
					  cmd = executePath();
				  else
					  cmd = "r";
			  }
			   
			  // if there is an obstacle in front and left go right most of the time
			  else if (ifObstacle(getLoc(2, 2)) && ifObstacle(getLoc(1, 3)))
			  {
				  if(1 + (int)(Math.random() * 10) == 1)
					  cmd = "l";
				  else 
					  cmd = "r";
			   }
			   
			// if there is an obstacle in front and right go left most of the time
			  else if(ifObstacle(getLoc(2, 2)) && ifObstacle(getLoc(1, 1)))
			  {
				  if(1 + (int)(Math.random() * 10) == 1)
					  cmd = "r";
				  else 
					  cmd = "l";
			   }
			   
			  //if there is an obstacle in front and smell is right go right most of the time
			  else if(ifObstacle(getLoc(2, 2)) && smell == 'r')
			  {
				  if(1 + (int)(Math.random() * 3) == 1)
					  cmd = "l";
				  else
					  cmd = "r";
			  }
			   
			 //if there is an obstacle in front and smell is left go left most of the time
			  else if(ifObstacle(getLoc(2, 2)) && smell == 'l')
			  {
				  if(1 + (int)(Math.random() * 3) == 1)
					  cmd = "r";
				  else 
					  cmd = "l";
			  }
			   
			   //if no previous combinations exist and there is an obstacle in front turn left most of the time
			   //agent favors side in order to escape long loops
			   else if(ifObstacle(getLoc(2, 2)))
			   {
				  if(1 + (int)(Math.random() * 4) == 1)
					  cmd = "r";
				  else 
					  cmd = "l";
			   }
			   
			   //if there is a dead end go right or left(favor left)
			   else if(ifObstacle(getLoc(4, 2)) && ifObstacle(getLoc(3, 3)) && ifObstacle(getLoc(3, 1)) && ifObstacle(getLoc(2, 3)) && ifObstacle(getLoc(2, 2)))
			   {
				   num = 1 + (int)(Math.random() * 3);
				   
				   if(num == 1)
					   cmd = "r";
				   else
					   cmd = "l";
			   }
			   
			   //same as above
			   else if(ifObstacle(getLoc(3, 2)) && ifObstacle(getLoc(2, 3)) && ifObstacle(getLoc(2, 1)))
			   {
				   num = 1 + (int)(Math.random() * 3);
				   
				   if(num == 1)
					   cmd = "r";
				   else
					   cmd = "l";
			   }
			   
			   //if agent is located on a wall and smell is the opposite direction, follow wall 7/8 times or turn 1/8 times
			   else if(ifObstacle(getLoc(2, 3)) && ifObstacle(getLoc(1, 3)) && smell == 'r')
			   {
				   if(1 + (int)(Math.random() * 8) == 1)
						  cmd = "r";
					  else 
						  cmd = "f";
			   }
			   
			 //if agent is located on a wall and smell is the opposite direction, follow wall 7/8 times or turn 1/8 times
			   else if(ifObstacle(getLoc(0, 1)) && ifObstacle(getLoc(1, 1)) && ifObstacle(getLoc(2, 1)) && smell == 'l')
			   {
				   if(1 + (int)(Math.random() * 8) == 1)
						  cmd = "l";
					  else 
						  cmd = "f";
			   }
			   
			   //if right side is a dead end
			   else if(ifObstacle(getLoc(0, 1)) && ifObstacle(getLoc(1, 2)) && ifObstacle(getLoc(2, 1)))
				   cmd = "f";
			   
			   //if left side is a dead end
			   else if(ifObstacle(getLoc(0, 3)) && ifObstacle(getLoc(1, 4)) && ifObstacle(getLoc(2, 3)))
				   cmd = "f";
			   
			   //if there is an obstacle nearby go forward to follow along
			   else if(ifObstacle(getLoc(1, 1)) && !ifObstacle(getLoc(0, 3)))
				   cmd = "f";
			   
			   //same as above
			   else if(ifObstacle(getLoc(1, 3))  && !ifObstacle(getLoc(0, 3)))
				   cmd = "f";
			   /*
			   //if cheese is behind but there is an obstacle behind go free direction
			   else if(smell == 'b' && ifObstacle(getLoc(0, 2)) && ifObstacle(getLoc(1, 1)))
				   cmd = "l";
			   
			   //same as above
			   else if(smell == 'b' && ifObstacle(getLoc(0, 2)) && ifObstacle(getLoc(1, 3)))
				   cmd = "r";
			   */
			   
			   //if nothing else matches and there is an obstacle on either side, go forward
			   else if(ifObstacle(getLoc(1, 1)) || ifObstacle(getLoc(1, 3)))
			   {
				   num = 1 + (int)(Math.random() * 5);
				   
				   if(num == 1)
					   cmd = "r";
				   else
					   cmd = "f";
			   }
			   
			   //hug corners generally if not go towards smell
			   /*else if(ifObstacle(getLoc(2, 1)) && smell == 'r')
			   {
				   num = 1 + (int)(Math.random() * 5);
				   
				   if(num == 1)
					   cmd = "r";
				   else
					   cmd = "f";
			   }*/
			   
			   //same as above
			   else if(ifObstacle(getLoc(2, 3)) && smell == 'l')
			   {
				   num = 1 + (int)(Math.random() * 5);
				   
				   if(num == 1)
					   cmd = "r";
				   else
					   cmd = "f";
			   }
			   
			   //basic actions when no obstacles to maneuver around
			   //basic actions are randomized slightly in order to break out of agent loops
			   else if(smell == 'r')
			   {
				   num = 1 + (int)(Math.random() * 10);
				   
				   if(num == 1)
					   cmd = "f";
				   else
					   cmd = "r";
			   }
			   
			   else if(smell == 'b')
			   {
				   if(1 + (int)(Math.random() * 2) == 1)
						  cmd = "r";
					  else 
						  cmd = "l";
			   }
			   
			   else if(smell == 'l')
			   {
				   num = 1 + (int)(Math.random() * 10);
				   
				   if(num == 1)
					   cmd = "f";
				   else
					   cmd = "l";
			   }
			   
			   else if(smell == 'f')
			   {
				   cmd = "f";
			   }
			   
			   //calls executeAction function in order to actually send the action to the server program
			   executeAction(cmd);
			   
		   } while(true); //breaks out of loop by returning a finishing value
	}
	
	//reads in sensory data for the agent from the host
	private int readSensors() throws IOException
	{
		String line;
		String[] tokens = new String[10];
		
		//read in Maeden Directive and return error if it is not 8(0)
		line = in.readLine();
		
		//
		if(line.equals("8"))
		{}
		else if(line.equals("success"))
			return SUCCESS;
		else if(line.equals("End"))
			return END;
		else if(line.equals("die"))
			return DIE;
		else
			return ERROR;
		
		//read in smell direction to line(1)
		line = in.readLine();
		smell = line.charAt(0);
		
		//read in the inventory for the agent to line and parse data(2)
		line = in.readLine();
		line = line.replaceAll("\\(|\\)|\"", "");
		
		tokens = parseRegular(line);
		for(int i = 0; i < 10; i++)
			inventory[i] = tokens[i].charAt(0);
		
		//read in sight information and call parseSight to parse the information(3)
		line  = in.readLine();
		
		sight = parseSight(line);
		
		//read in items on the "ground" and store into the class array(4)
		line = in.readLine();
		line = line.replaceAll("\\(|\\)|\"", "");
		
		tokens = parseRegular(line);
		for(int i = 0; i < 10; i++)
			ground[i] = tokens[i].charAt(0);
		
		//read in messages from other agents and store into the class array(5)
		line = in.readLine();
		line = line.replaceAll("\\(|\\)|\"", "");
		
		messages = parseRegular(line);
		
		//read in energy and store into class variable(6)
		line = in.readLine();
		energy = Integer.parseInt(line);
		
		//read in status of the agent(7)
		line = in.readLine();
		status = line;
		
		//read in world time of the environment(8)
		line = in.readLine();
		time = Integer.parseInt(line);
		
		return 3;
	}
	
	//print all sensor variables of agent except for sight
	private void printSensors()
	{
		System.out.println("\n**NEW SET OF SENSES**\n");
		System.out.println("Smell: " + smell);
		
		System.out.println("Inventory:");
		for(int i = 0; i < inventory.length; i++)
		{
			if(inventory[i] == 'e')
				break;
			System.out.print(i + ". " + inventory[i] + " ");
		}
		System.out.println();
		
		System.out.println("Ground:");
		for(int i = 0; i < ground.length; i++)
		{
			if(ground[i] == 'e')
				break;
			System.out.print(" \"" + ground[i] + "\"");
		}
		System.out.println();
		
		for(int i = 0; i < messages.length; i++)
		{
			if(messages[i].charAt(0) == 'e')
				break;
			System.out.print(" \"" + messages[i] + "\"");
		}
		
		System.out.println("Status: " + status);
		
		System.out.println("Energy: " + energy);
		
		System.out.println("World Time: " + time);
	}
	
	//prints the sight array
	private void printSight()
	{
		for(int i = 0; i < 7; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				System.out.print("(" + sight[i][j] + ")");
			}
			System.out.println();
		}
	}
	
	//picks up item if it is on ground array but not in inventory
	private boolean pickupItem()
	{
		boolean groundHammer = false;
		boolean groundCheese = false;
		boolean groundKey = false;
		
		for(int i = 0; i < ground.length; i++)
		{
			if(ground[i] == 'T')
				groundHammer = true;
			if(ground[i] == '+')
				groundCheese = true;
			if(ground[i] == 'K')
				groundKey = true;
		}
		
		if(groundCheese && !haveCheese())
			return true;
		else if(groundHammer && !haveHammer())
			return true;
		else if(groundKey && !haveKey())
			return true;
		
		return false;
		
		
	}
	
	private String itemNearby()
	{
		//
		for(int i = 2; i < 7; i++)
		{
			if(sight[i][2].equals("*") || sight[i][2].equals("@") || sight[i][2].equals("#") || sight[i][2].equals("="))
				break;
			else if((sight[i][2].contains("T") && !haveHammer()) || (sight[i][2].contains("K") && !haveKey()))
				return "f";
		}
		
		for(int i = 3; i < 5; i++)
		{
			if(sight[1][i].equals("*") || sight[1][i].equals("@")  || sight[1][i].equals("#")  || sight[1][i].equals("="))
				break;
			else if((sight[1][i].contains("T") && !haveHammer()) || (sight[1][i].contains("K") && !haveKey()))
				return "l";
		}
		
		for(int i = 1; i >= 0; i--)
		{
			if(sight[1][i].equals("*") || sight[1][i].equals("@")  || sight[1][i].equals("#")  || sight[1][i].equals("="))
				break;
			if((sight[1][i].contains("T") && !haveHammer()) || (sight[1][i].contains("K") && !haveKey()))
				return "r";
		}
		
		return "e";
	}
	
	
	//returns string location of coordinates given for the 7x5 array
	private String getLoc(int y, int x)
	{
		return sight[y][x];
	}
	
	//test if inputted location is an obstacle returns true if wall
	//or if it is a rock and agent has no hammer
	//or if it is a door
	private boolean ifObstacle(String location)
	{
		if(location.contains("*"))
			return true;
		else if(location.contains("@") && !haveHammer())
			return true;
		else if(location.contains("#") && !haveKey())
			return true;
		else
			return false;
	}
	
	//if agent can use a key at a given location
	private boolean useKey(String location)
	{
		if(location.contains("#") && haveKey())
		{
			return true;
		}
		else
			return false;
	}
	
	//if agent can use a hammer at a given location
	private boolean useHammer(String location)
	{
		if(location.contains("@") && haveHammer())
		{
			return true;
		}
		else
			return false;
	}
	
	//test if agent has Cheese
	private boolean haveCheese()
	{
		for(int i = 0; i < inventory.length; i++)
		{
			if(inventory[i] == '+')
				return true;
		}
		
		return false;
	}
	
	//test if agent has a hammer
	private boolean haveHammer()
	{
		for(int i = 0; i < inventory.length; i++)
		{
			if(inventory[i] == 'T')
				return true;
		}
		
		return false;
	}
	
	//test if agent has a key
	private boolean haveKey()
	{
		for(int i = 0; i < inventory.length; i++)
		{
			if(inventory[i] == 'K')
				return true;
		}
		
		return false;
	}
	
	private void pathToRock()
	{
		
		
		if(sight[2][2].contains("@"))
			return;
		
		if(mm.rock == true)
		{
			mm.BFSNar(position);
			
			ArrayList<Point> list = mm.itemSearch("@");
			
			for (Point val : list) 
			{
				Point rockP = val.copy();
				rockP.x = rockP.x - 1;
				path = mm.findPath(position, rockP, true);
				if(path.peek() != null)
					return;
				
				rockP.x = rockP.x + 2;
				path = mm.findPath(position, rockP, true);
				if(path.peek() != null)
					return;
				
				rockP.x = rockP.x - 1;
				rockP.y = rockP.y - 1;
				path = mm.findPath(position, rockP, true);
				if(path.peek() != null)
					return;
				
				rockP.y = rockP.y + 2;
				path = mm.findPath(position, rockP, true);
			}
		}
	}
	
	private void pathToDoor()
	{
		if(sight[2][2].contains("#"))
			return;
		
		if(mm.door == true)
		{
			mm.BFSNar(position);
			
			ArrayList<Point> list = mm.itemSearch("#");
			
			for (Point val : list) 
			{
				Point rockP = val.copy();
				rockP.x = rockP.x - 1;
				path = mm.findPath(position, rockP, true);
				if(path.peek() != null)
					return;
				
				rockP.x = rockP.x + 2;
				path = mm.findPath(position, rockP, true);
				if(path.peek() != null)
					return;
				
				rockP.x = rockP.x - 1;
				rockP.y = rockP.y - 1;
				path = mm.findPath(position, rockP, true);
				if(path.peek() != null)
					return;
				
				rockP.y = rockP.y + 2;
				path = mm.findPath(position, rockP, true);
			}
		}
	}
	
	private void pathToKey()
	{
		if(sight[1][2].contains("K")  || sight[1][2].contains("="))
			return;
		
		mm.BFSNar(position);
		
		ArrayList<Point> list = mm.itemSearch("K");
		Queue<String> tempPath;
		
		for (Point val : list) 
		{
			Point keyP = val.copy();
			tempPath = mm.findPath(position, keyP, true);
			if(tempPath.peek() != null)
			{
				path = tempPath;
				return;
			}
		}
	}
	
	private void pathToHammer()
	{
		if(sight[1][2].contains("T") || sight[1][2].contains("="))
			return;
		
		mm.BFSNar(position);
		
		ArrayList<Point> list = mm.itemSearch("T");
		
		Queue<String> tempPath;
		
		for (Point val : list) 
		{
			Point hammerP = val.copy();
			tempPath = mm.findPath(position, hammerP, true);
			if(tempPath.peek() != null)
			{
				path = tempPath;
				return;
			}
		}
	}
	
	private void avoidObstacle()
	{
		boolean obstacle = false;
		int obstacleRow = 2;
		mm.BFS(position);
		
		/*if(ifObstacle(sight[2][2]) && smell == 'f')
		{
			path = mm.findPath(position, translatePosition(0, 2));
		}*/
		
		int row = 2;
		if(smell == 'f')
		{
				for(row = 2; row < 6; row++)
				{
					if(ifObstacle(sight[row][2]))
					{
						obstacle = true;
						obstacleRow = row;
					}
				}
			
			if(obstacle)
			{
				for(int row2 = obstacleRow + 1; row2 < 7; row2++)
				{
					for(int col = 0; col < 5; col++)
					{
						if(!ifObstacle(sight[row2][col]))
							path = mm.findPath(position, translatePosition(2 - col, row2 - 1), false);
						
						if(path != null)
							return;
					}
				}
			}
		}
		
		else if(smell == 'r')
		{
			if(ifObstacle(sight[1][1]))
			{
				obstacle = true;
				obstacleRow = row;
			}
			
			if(obstacle)
			{
				for(int row2 = 0; row2 < 7; row2++)
				{
					if(!ifObstacle(sight[row2][0]))
						path = mm.findPath(position, translatePosition(2, row2 - 1), false);
					
					if(path != null)
						return;
				}
			}
		}
		else if(smell == 'l')
		{
			if(ifObstacle(sight[1][3]))
			{
				obstacle = true;
				obstacleRow = row;
			}
			
			if(obstacle)
			{
				for(int row2 = 0; row2 < 7; row2++)
				{
					if(!ifObstacle(sight[row2][4]))
						path = mm.findPath(position, translatePosition(-2, row2 - 1), false);
					
					if(path != null)
						return;
				}
			}
		}
	}
	
	private boolean cheesePath()
	{
		mm.BFS(position);
		
		Point cheese = mm.getCheese();
		Queue<String> tempPath = null;
		
		if(cheese != null)
		{
			tempPath = mm.findPath(position, cheese, false);
		}
		
		if(tempPath != null)
		{
			if(tempPath.peek() != null)
			{
				path = tempPath;
				return true;
			}
		}
		
		return false;
			
	}
	
	private String executePath()
	{
		if(path.peek().charAt(0) == direction)
		{
			path.remove();
			return "f";
		}
		else if(direction == 'e' && path.peek().charAt(0) == 'n')
			return "l";
		else if(direction == 'n' && path.peek().charAt(0) == 'w')
			return "l";
		else if(direction == 'w' && path.peek().charAt(0) == 's')
			return "l";
		else if(direction == 's' && path.peek().charAt(0) == 'e')
			return "l";
		else
			return "r";
	}
	
	private boolean objectAdjacent(String s)
	{
		if(sight[2][2].contains(s))
			return true;
		else if(sight[1][1].contains(s))
			return true;
		else if(sight[1][3].contains(s))
			return true;
		else if(sight[0][2].contains(s))
			return true;
		else
			return false;
	}
	
	//function writes to output socket with inputted command
	private void executeAction(String cmd) throws IOException
	{
		changePos(cmd);
		
		dOut.writeBytes(cmd + "\n");
		dOut.flush(); 
	}
	
	//moves coordinate to the right x and up y
	private Point translatePosition(int x, int y)
	{
		Point p = position.copy();
		
		if(direction == 'n')
		{
			p.x = p.x + x;
			p.y = p.y + y;
		}
		else if(direction == 'e')
		{
			p.x = p.x + y;
			p.y = p.y - x;
		}
		else if(direction == 's')
		{
			p.x = p.x - x;
			p.y = p.y - y;
		}
		else
		{
			p.x = p.x - y;
			p.y = p.y + x;
		}
		
		return p;
	}
	
	private void changePos(String cmd)
	{
		if(cmd.equals("f"))
			if(direction == 'n')
				position.y = position.y + 1;
			else if(direction == 'e')
				position.x = position.x + 1;
			else if(direction == 's')
				position.y = position.y - 1;
			else
				position.x = position.x - 1;
		
		else if(cmd.equals("b"))
			if(direction == 'n')
				position.y = position.y - 1;
			else if(direction == 'e')
				position.x = position.x - 1;
			else if(direction == 's')
				position.y = position.y + 1;
			else
				position.x = position.x + 1;
		
		else if(cmd.equals("r"))
			if(direction == 'n')
				direction = 'e';
			else if(direction == 'e')
				direction = 's';
			else if(direction == 's')
				direction = 'w';
			else
				direction = 'n';
		
		else if(cmd.equals("l"))
			if(direction == 'n')
				direction = 'w';
			else if(direction == 'e')
				direction = 'n';
			else if(direction == 's')
				direction = 'e';
			else
				direction = 's';
	}
	
	//parsing functions to return items from all lists that the hosts sends except for sight
	private String[] parseRegular(String line)
	{
		
		String[] tokens = new String[10];
		line = line.replaceAll("\\s+\"", "");
		line = line.replaceAll("\\(\\)", "");
		
		//fills up rest of array with e's(empty) entries
		for(int i = 0; i < 10; i++)
			tokens[i] = "e";
				
		for(int i = 0; i < line.length(); i++)
			tokens[i] = Character.toString(line.charAt(i));
		
		return tokens;
	}
	
	//parses sight information and returns it as a multidimensional array
	private String[][] parseSight(String line)
	{
		line = line.replaceAll("\\s+","");
		String[][] parsed = new String[7][5];
		
		
		int count = 0;
		String space;
		
		if(line.charAt(count) != '(')
			return null;
		count++;
		
		for(int row = 0; row < 7; row++)
		{
			if(line.charAt(count) != '(')
				return null;
			count++;
			
			for(int col = 4; col >= 0; col--)
			{
				space = "";
				
				if(line.charAt(count) != '(')
					return null;
				count++;
				
				while(line.charAt(count) != ')')
				{
					space = space + line.charAt(count);
					count++;
				}
				count++;
				
				space = space.replaceAll("\"", "");
				parsed[row][col] = space;
				 
			}
			
			
			if(line.charAt(count) != ')')
				return null;
			count++;
		}
		
		return parsed;
		
	}
	
	public int getAgentNum()
	{
		return agentNum;
	}
	
}