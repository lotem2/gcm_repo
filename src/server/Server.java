package server;

import java.util.ArrayList;
import java.util.HashMap;

import common.*;
import entity.*;

public class Server extends AbstractServer {
	// Variables
	final public static int DEFAULT_PORT = 5555;
	
	public Server(int port) {
		super(port);
		// TODO Auto-generated constructor stub
	}
	
	public void handleMessageFromClient
    (Object msg, ConnectionToClient client) {
		// Variables
		Message currMsg  = (Message)msg;
		Message replyMsg = null;
		ArrayList<Object> params;
		
		try {
			switch (currMsg.getAction()) {
			case LOGIN:
				if(DoesUserExists(currMsg.getData().get(0).toString())) {
					params = new ArrayList<Object>();
					params.add(new Integer(1));
					params.add(new String("Client already connected."));
					replyMsg = new Message(Action.LOGIN, params);
				}
				else {
					replyMsg = UsersDB.getInstance().getUser(currMsg.getData());
					if(((Integer)replyMsg.getData().get(0)) == 0)
						client.setName(((User)currMsg.getData().get(1)).getUserName());
					
					replyMsg.setAction(Action.LOGIN);
				}
				break;
			case LOGOUT:
				// Set thread name to empty and return success message to client
				client.setName("");
				params = new ArrayList<Object>();
				params.add(new Integer(0));
				replyMsg = new Message(Action.LOGOUT, params);
				break;
			case SEARCH:
				replyMsg = MapDB.getInstance().Search(currMsg.getData());
				replyMsg.setAction(Action.SEARCH);
				break;
			/*case ADD_PURCHASE:
				params = new ArrayList<Object>();
				params.add(((Client)client.getInfo("UserInfo")).getUsername());
				client.sendToClient(PurchaseDB.getInstance().AddPurchase(params));
				break;
			case SHOW_CLIENT_DETAILS:
				params = new ArrayList<Object>();
				params.add(((Client)client.getInfo("UserInfo")).getUsername());
				
				replyMsg = UsersDB.getInstance().getUser(params);
				if(((Integer)replyMsg.getData().get(0)) == 0)
					client.setInfo("UserInfo", replyMsg.getData().get(1));
				client.sendToClient(replyMsg);
				break;*/
			default:
				break;
			}

			client.sendToClient(replyMsg);	// Send message to the current client
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/** Hook method to handle client's connection **/
	  protected void clientConnected(ConnectionToClient client) 
	  {
	    // display on server and clients that the client has connected.
	    String msg = "A Client has connected";
	    System.out.println(msg);
	    //client.sen(msg);
	  }
	  
	  protected void clientDisconnected(ConnectionToClient client) 
	  {
	    // display on server and clients that the client has connected.
		  //String msg = ((Client)client.getInfo("UserInfo")).getUsername() + " has disconnected";
	    //System.out.println(msg);
	    //client.sen(msg);
	  }
	  
	/** Hoot method to handle client's disconnection **/ 
	  synchronized protected void clientException
	  	(ConnectionToClient client, Throwable exception) 
	  {
		  String msg;
		  
		  if(!client.getName().startsWith("Thread")){
			  msg = client.getName() + " has disconnected.";
		  }
		  else {
			  msg = "A client has disconnected.";
		  }
		  System.out.println(msg);
	  }
	  
	  /** Private method to get current user name
	   * params: {@value} user name
	   * return value: {@value} true if user exists, else false **/
	  private boolean DoesUserExists(String username) {
		  Thread[] clients = getClientConnections();
		  
		  for (Thread client : clients) {
			if (client.getName() == username) {
				return true;
			}
		  }
		  
		  return false;
	  }
	  
	public static void main(String[] args) {
	    int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
		
	    Server sv = new Server(port);
	    
	    try 
	    {
	    	while(true) {
	    		sv.listen(); //Start listening for connections	
	    	}
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }
	}
}
