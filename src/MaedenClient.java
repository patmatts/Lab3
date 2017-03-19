

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MaedenClient {
	
	private final static int MAEDENPORT = 7237;
	private final static String HOST = "localhost";
	
	private static DataOutputStream dOut = null;
	private static DataInputStream dIn = null;
	private static BufferedReader in = null;
    private static int[] agentNums = new int[10];
    
    private static Socket gridSocket;
	
	public static void main(String[] args)
	{
		//holds agent status for finishing condition
		int agentStatus;
		
		try
		{
			if(socketConnect(HOST, MAEDENPORT, agentNums) != 0)
			{
				System.out.println("Unable to acquire agent number from host");
				return;
			}
			
			ReflexAgent refAgent = new ReflexAgent(in, dOut, agentNums[0]);
			
			agentStatus = refAgent.reflexMain();
			
			// messages corresponding to the different finish condition the agent returns
			if(agentStatus == 0)
				System.out.println("Agent " + refAgent.getAgentNum() + " was successful.");
			else if(agentStatus == 1)
				System.out.println("Agent " + refAgent.getAgentNum() + " was unsuccessful in getting cheese first.");
			else if(agentStatus == 2)
				System.out.println("Agent " + refAgent.getAgentNum() + " was unsuccessful and has died.");
			else
				System.out.println("Agent " + refAgent.getAgentNum() + " encountered an error.");
			
			//closes in and out protocol and socket
			dIn.close();
			dOut.close();
			gridSocket.close();
		}
		//exception handling
		catch(UnknownHostException e) {
			System.out.println("Unknown Host Exception from MaedenClient");
			return;
		} 
		catch(IOException e) {
			System.out.println("IOException from MaedenClient");
			return;
		}
		
	}
	
	//connects to socket and receives agent number and sends request to be a normal agent
	public static int socketConnect(String host, int port, int agentNums[])
	{
		try
		{
			System.out.print("Connecting to: " + host + " Port number: " + port + "\n");
		    gridSocket = new Socket(host, port);
		    System.out.println("Socket creation successful");
		    
		    dOut = new DataOutputStream(gridSocket.getOutputStream());
		    
		    dIn = new DataInputStream(gridSocket.getInputStream());
		    
		    in = new BufferedReader(new InputStreamReader(dIn));
			
		    dOut.writeBytes("base\n");
		    dOut.flush();
		    
		    agentNums[0] = Integer.parseInt(in.readLine());
		    
		    System.out.println("Agent: " + agentNums[0]);
		    
			return 0;
		}
		catch(UnknownHostException e) {
			System.out.println("Unknown Host Exception.");
			return -1;
		} 
		catch(IOException e) {
			System.out.println("IOException ");
			return -1;
		}
	}

}
