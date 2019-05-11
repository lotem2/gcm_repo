 package client;

import entity.*;
import common.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.

 */
public class MainGuiClient extends AbstractClient
{
	
	/*
  public PurchaseClientController(String host, int port) {
		super(host, port);
		// TODO Auto-generated constructor stub
	}
*/
//Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */


  /**
   * The Login ID of the user.
   */
  String loginID;

  
  //Constructors ****************************************************

  
  public MainGuiClient(String host, int port) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    openConnection();
  //  this.loginID = "ANONYMOUS";
  //  sendToServer("#login ANONYMOUS");
  }

  //Instance methods ************************************************
  
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
	  Message currMsg = (Message)msg;
	  switch (currMsg.getAction()) {
	  case LOGIN:
	      	 if((Integer)currMsg.getData().get(0) == 0) {
	      		 System.out.println(((Client)currMsg.getData().get(1)).toString());
	      	 }
	      	 else {
	      	//	 clientUI.display(currMsg.getData().get(1).toString() + "\n"
	      		//		 + "For retry please enter \"login\"");
	      	 }
	    	  break;
	  case SEARCH:
	      	 if((Integer)currMsg.getData().get(0) == 0) {
	      		 //sendDataToGUI(currMsg);
	      	     System.out.println("The message was sent to the gui");
	      	 }
	      	 else {
	      		// clientUI.display(currMsg.getData().get(1).toString() + "\n"
	      			//	 + "The message was not sent to the gui.Please retry\"");
	      	 }
	    	  break;
	  case ADD_PURCHASE:
	      	 if((Integer)currMsg.getData().get(0) == 0) {
	      	//	 clientUI.display("Purchase added successfully\n");
	      	 }
	      	 else {
	      	//	 clientUI.display(currMsg.getData().get(1).toString() + "\n");
	      	 }
	      	  break;
	  case SHOW_CLIENT_DETAILS:
	      		if((Integer)currMsg.getData().get(0) == 0) {
	      	//		clientUI.display(currMsg.getData().get(1).toString());	      			
	      		}
	      		else {
	      		//	clientUI.display(currMsg.getData().get(1).toString() + "\n");
	      		}
	      	  break;
	  }
  }
 

   /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
//  public void handleMessageFromClientUI(String message)
//  {
//	// detect commands
//	if (message.charAt(0) == '#')
//	{
//	      runCommand(message);
//	}
//	else
//	{
//		try
//		{
//	    	  Message myMessage;
//	    	  ArrayList<Object> data = new ArrayList<Object>();
//	    	  BufferedReader fromConsole = 
//	  		        new BufferedReader(new InputStreamReader(System.in));
//	    	  String userName = "";
//	    	  switch (message) {
//	    	  case "login":
//	    	  if(message.contentEquals("get purchase count"))  {	
//	    		  System.out.println("Enter user name: ");
//	    		  userName = fromConsole.readLine();
//	 		      data.add(userName);
//	 		     myMessage = new Message(
//	 	  	    		Action.LOGIN,
//	 	  	    		data);
//		    sendToServer(myMessage);
//	      break;
//	    	case "buy": 
//	    		  myMessage = new Message(
//	        	    		Action.ADD_PURCHASE,
//	        	    		new ArrayList<Object>());
//	  	    sendToServer(myMessage);
//	  	    break;
//	    	case "show details":  
//	      	    myMessage = new Message(
//	      	    		Action.SHOW_CLIENT_DETAILS,
//	      	    		new ArrayList<Object>());
//	      	    sendToServer(myMessage);
//	      	    break;
//	    	default: System.out.println("ERROR: INVALID ENUM in client"); 
//	        }
//	      }
//	      catch(IOException e)
//	      {
//	    	clientUI.display(e.toString());
//	        clientUI.display
//	          ("Could not send message to server.  Terminating client.");
//	        quit();
//	      }
//	}
//  }

  /**
   * This method executes client commands. Benjamin Bergman, Oct 22, 
   * 2009
   *
   * @param message string from the client console
   */
//  private void runCommand(String message)
//  {
//    // a bunch of ifs
//
//    if (message.equalsIgnoreCase("#quit"))
//    {
//      quit();
//    }
//    else if (message.equalsIgnoreCase("#logoff"))
//    {
//      try
//      {
//        closeConnection();
//      }
//      catch(IOException e) {}
//    //  clientUI.display("You have logged off.");
//    }
//    else if (message.toLowerCase().startsWith("#setport"))
//    {
//      // requires the command, followed by a space, then the port number
//      try 
//      {
//        int newPort = Integer.parseInt(message.substring(9));
//        setPort(newPort);
//        // error checking for syntax a possible addition
//     //   clientUI.display
//       //   ("Port changed to " + getPort());
//      }
//      catch (Exception e)
//      {
//        System.out.println("Unexpected error while setting client port!");
//      }
//    }
//    else if (message.toLowerCase().startsWith("#sethost"))
//    {
//      setHost(message.substring(9));
//   //   clientUI.display
//     //   ("Host changed to " + getHost());
//    }
//    else if (message.toLowerCase().startsWith("#login"))
//    {
//      if (isConnected())
//      {
//     //   clientUI.display("You must logout before you can login.");
//        return;
//      }
//      // login
//      // if no id, login anonymous
//      loginID = message.substring(7);
//      try
//      {
//        openConnection();
//        sendToServer("#login " + loginID);
//      }
//      catch (Exception e)
//      {
//     //   clientUI.display("Connection could not be established.");
//      }
//    }
//    else if (message.equalsIgnoreCase("#gethost"))
//    {
//  //    clientUI.display("Current host: " + getHost());
//    }
//    else if (message.equalsIgnoreCase("#getport"))
//    {
// //     clientUI.display("Current port: " + Integer.toString(getPort()));
//    }
//  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  /**
   * Reacts to a closed connection while waiting for
   * messages from the server. Overrides method in 
   * <code>AbstractClient</code>. Added by Benjamin 
   * Bergman, Oct 22, 2009.
   *
   * @param exception the exception raised.
   */
  protected void connectionException(Exception exception)
  {
	//  clientUI.display("sadd "+ exception );  
   // clientUI.display
    //  ("The connection to the Server (" + getHost() + ", " + getPort() + 
    //  ") has been disconnected");
  }
}
//End of ChatClient class
