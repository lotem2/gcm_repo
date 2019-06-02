package server;

import java.util.ArrayList;
import java.util.HashMap;

import common.*;
import entity.*;

public class Server extends AbstractServer {
	// Variables
	final public static int DEFAULT_PORT = 5555;
	static private HashMap<String, ConnectionToClient> clients;
	public Server(int port) {
		super(port);
		clients = new HashMap<String, ConnectionToClient>();
	}

	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		// Variables
		Message currMsg = (Message) msg;
		Message replyMsg = null;
		ArrayList<Object> params;
 
		try {
			switch (currMsg.getAction()) {
			case LOGIN:
				// Check if user already connected
				if (DoesUserExists(currMsg.getData().get(0).toString())) {
					// Sending a login failure to client
					ArrayList<Object> input = new ArrayList<Object>();
					input.add(new Integer(1));
					input.add(new String("Client already connected."));
					replyMsg = new Message(Action.LOGIN, input);
				} else {
					// Get User's details
					replyMsg = UsersDB.getInstance().getUser(currMsg.getData());
					if (((Integer) replyMsg.getData().get(0)) == 0) {
						// Adding the current user to the list of clients connected
						clients.put(currMsg.getData().get(0).toString(), client);
						client.setName(currMsg.getData().get(0).toString());
					}

					replyMsg.setAction(Action.LOGIN);
				}
				break;
			case LOGOUT:
				// remove client from list of clients and send success message
				client.setName("");
				clients.remove(currMsg.getData().get(0).toString());
				ArrayList<Object> input = new ArrayList<Object>();
				input.add(new Integer(0));
				replyMsg = new Message(Action.LOGOUT, input);
				break;
			case REGISTER:
				replyMsg = UsersDB.getInstance().AddUser(currMsg.getData());
				break;
			case EDIT_USER_DETAILS:
				replyMsg = UsersDB.getInstance().EditUser(currMsg.getData());
				break;
			case SEARCH:
				replyMsg = MapDB.getInstance().Search(currMsg.getData());
				break;
			case GET_CITY_PRICE:
				replyMsg = CityDB.getInstance().getCitiesList();
				replyMsg.setAction(Action.GET_CITY_PRICE);
				break;
			/*
			 * case ADD_PURCHASE: params = new ArrayList<Object>();
			 * params.add(((Client)client.getInfo("UserInfo")).getUsername());
			 * client.sendToClient(PurchaseDB.getInstance().AddPurchase(params)); break;
			 * case SHOW_CLIENT_DETAILS: params = new ArrayList<Object>();
			 * params.add(((Client)client.getInfo("UserInfo")).getUsername());
			 * 
			 * replyMsg = UsersDB.getInstance().getUser(params);
			 * if(((Integer)replyMsg.getData().get(0)) == 0) client.setInfo("UserInfo",
			 * replyMsg.getData().get(1)); client.sendToClient(replyMsg); break;
			 */
			default:
				break;
			}

			client.sendToClient(replyMsg); // Send message to the current client
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/** Hook method to handle client's connection **/
	protected void clientConnected(ConnectionToClient client) {
		// display on server and clients that the client has connected.
		String msg = "A Client has connected";
		System.out.println(msg);
	}

	protected void clientDisconnected(ConnectionToClient client) {
		// Remove client from list of clients
		clients.remove(client.getName());
	}

	/** Hoot method to handle client's disconnection **/
	synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
		String msg;

		if (!client.getName().startsWith("Thread")) {
			msg = client.getName() + " has disconnected.";
		} else {
			msg = "A client has disconnected.";
		}
		
		clients.remove(client.getName());
		System.out.println(msg);
	}

	/**
	 * Private method to get current user name params: {@value} user name return
	 * value: {@value} true if user exists, else false
	 **/
	private boolean DoesUserExists(String username) {
//		Thread[] clients = getClientConnections();
//
//		for (Thread client : clients) {
//			if (client.getName() == username) {
//				return true;
//			}
//		}
		if(clients.containsKey(username)) return true;
		return false;
	}

	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		Server sv = new Server(port);

		try {
			while (true) {
				sv.listen(); // Start listening for connections
			}
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
