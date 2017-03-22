README.txt

CS455 Lab3 group: Patrick Matts, Levi Sinclair, Sheridan Olds

Reactive Agent that uses the MAEDEN simulator environment with a mental map

This agent was created on a Windows Operating system with Java 1.8
and functions with the Maeden simulator environment developed by:
Wayne Iba, Nicholas Burwell, Chris Phillips, Josh Holm, Cuyler Cannon

Instructions to run:
1. (if on windows) use the cmd line to set PATH=c:\Program Files\Java\[your version of java]\bin;%PATH%
2. Compile MaedenClient.java in the src folder using the command line and the command javac MaedenClient.java
3. Run a map using the MAEDEN simulator environment on the same machine
4. Run the client using the command java MaedenClient
5. The client will run the reflex agent automatically and return if the agent succeeds or fails
or if it encounters an error

Lab 3 general acknowledgements
1. Created a mental map so the agent has knowledge about the world outside of the immediately percievable area
We went back and forth on how to actually implement the mental map. Our 2 main choices were storing it as an array
or as a custom graph data structure. We eventually decided to go with a graph of sorts. Luckily this way its easy
to keep adding Nodes to the graph so there are no size limitations.

2.Added a variety of functions to make the mental map work such as pathing to find the shortest distance to a point and return
a list of instructions to get there.

3. The agent uses this mental map to get items, destroy boulders/doors, and avoid obstacles. The program attempts find paths
to hammers and keys and if it has a key or hammer it will attempt to find a rock/door to opening up as much area as possible seems
to work fairly well. The agent also avoids obstacles in its field of vision and uses its mental map to circumvent them if it can.
Narrows took some trial and error, but a simple solution seemed to work fairly well. Just dont consider the path when trying to reach
a locked object with the unlocking object. Otherwise just treat them as an open space and drop objects before you go into them.

4. Explores via the original simple reflective agent from lab 1 for a while. After it expends a certain amount of energy
it goes into Explore Mode which just paths to the nearest unexplored reachable area. It will still path to rocks or hammers
when it is useful in order to open up more area to explore. Of course the agent immediately paths to the cheese if
it sees a viable path.
