package server;

import java.util.ArrayList;

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
		Message currMsg = (Message)msg;
		Message replyMsg;
		ArrayList<Object> params;
		
		try {
			switch (currMsg.getAction()) {
			case LOGIN:
				if(client.getInfo("UserInfo") != null) {
					ArrayList<Object> al = new ArrayList<Object>();
					al.add(new Integer(1));
					al.add(new String("Client already connected."));
					replyMsg = new Message(Action.LOGIN, al);
				}
				else {
					replyMsg = ClientDB.getInstance().getUser(currMsg.getData());
					if(((Integer)replyMsg.getData().get(0)) == 0)
						client.setInfo("UserInfo", replyMsg.getData().get(1));
				}
				
				client.sendToClient(replyMsg);
				break;
			case ADD_PURCHASE:
				params = new ArrayList<Object>();
				params.add(((Client)client.getInfo("UserInfo")).getUsername());
				client.sendToClient(PurchaseDB.getInstance().AddPurchase(params));
				break;
			case SHOW_CLIENT_DETAILS:
				params = new ArrayList<Object>();
				params.add(((Client)client.getInfo("UserInfo")).getUsername());
				
				replyMsg = ClientDB.getInstance().getUser(params);
				if(((Integer)replyMsg.getData().get(0)) == 0)
					client.setInfo("UserInfo", replyMsg.getData().get(1));
				client.sendToClient(replyMsg);
				break;
			default:
				break;
			}			
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
		  String msg = ((Client)client.getInfo("UserInfo")).getUsername() + " has disconnected";
	    System.out.println(msg);
	    //client.sen(msg);
	  }
	  
	/** Hoot method to handle client's disconnection **/ 
	  synchronized protected void clientException
	  	(ConnectionToClient client, Throwable exception) 
	  {
		  String msg = ((Client)client.getInfo("UserInfo")).getUsername() + " has disconnected";
		  System.out.println(msg);
	  }
	  
	public static void main(String[] args) {
	    Server sv = new Server(DEFAULT_PORT);
	    
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
