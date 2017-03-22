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
2. The agent uses this mental map to get items, destroy boulders/doors, and avoid obstacles.
3. Explores via the original simple reflective agent from lab 1.
4. One thing we could do to improve the agent is to explore a better way.