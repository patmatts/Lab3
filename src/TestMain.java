import java.util.Queue;
/**
	TestMain.java Group 9: Patrick Matts, Levi Sinclair, Sheridan Olds
	test class for the mental map and such
**/
public class TestMain {
	
	public enum Direction {
	    NORTH, EAST, SOUTH, WEST
	}
	
	/*public static void main(String[] args)
	{
		MentalMap mm = new MentalMap();
		
		String[][] sight = 
			{
				new String[] {"*", "*", "b", "*", "*"},
				new String[] {"*", "*", "b", "*", "b"},
				new String[] {"*", "*", "b", "*", "b"},
				new String[] {"*", "*", "b", "*", "b"},
				new String[] {"*", "*", "b", "b", "b"},
				new String[] {"*", "*", "b", "*", "*"},
				new String[] {"*", "*", "b", "*", "*"}
			};
		
		mm.updateMap(sight, 'n', new Point(0, 0));
		
		mm.printMap();
		
		String[][] sight2 = 
			{
				new String[] {"*", "*", "*", "*", "*"},
				new String[] {"*", "b", "b", "b", "b"},
				new String[] {"*", "*", "*", "*", "*"},
				new String[] {"*", "*", "*", "*", "*"},
				new String[] {"*", "*", "*", "*", "*"},
				new String[] {"*", "*", "*", "*", "*"},
				new String[] {"*", "*", "b", "*", "*"}
			};
		
		mm.updateMap(sight2, 'e', new Point(0, 0));
		
		Queue<String> path = mm.findPath(new Point(0, 0), new Point(-2, 2), false);
		
		for (String value : path) 
		{
			System.out.println(value);
		}
		
		mm.printMap();
		
		String items = "";
		
		System.out.println(items.contains("@"));
		
		
	}*/
}
