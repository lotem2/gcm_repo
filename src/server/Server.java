package server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import common.*;
import entity.*;


/**
* This class extends {@link AbstractServer} class which
* maintains a thread that waits for connection attempts
* from clients. Thus, when a connection attempt occurs
* it creates a new {@link ConnectionToClient} instance which
* runs as a thread. When a client is thus connected to the
* server, the two programs can then exchange {@link Object}
* instances.
*
* Method {@link handleMessageFromClient} is overridden in
* this class along with several other hook methods.
*/
public class Server extends AbstractServer {
	// Variables
	private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	final public static int DEFAULT_PORT = 5555;
	static private HashMap<User, ConnectionToClient> clients;
	public Server(int port) {
		super(port);
		clients = new HashMap<User, ConnectionToClient>();
	}

	/**
	* Handles a command sent from one client to the server.
	* This method is called by a synchronized method so it is also
	* implicitly synchronized.
	*
	* @param msg   the message sent.
	* @param client the connection connected to the client that
	*  sent the message.
	*/
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		// Variables
		Message currMsg = (Message) msg;
		Message replyMsg = null;

		try {
			switch (currMsg.getAction()) {
			case LOGIN:
				// Check if user already connected
				if (DoesUserExists(currMsg.getData().get(0).toString())) {
					// Sending a login failure to client
					ArrayList<Object> input = new ArrayList<Object>();
					input.add(new Integer(1));
					input.add(new String("Client already connected."));
					replyMsg = new Message(Action.LOGIN, new Integer(1), new String("Client already connected."));
				} else {
					// Get User's details
					replyMsg = UsersDB.getInstance().getUser(currMsg.getData());
					if (((Integer) replyMsg.getData().get(0)) == 0) {
						// Adding the current user to the list of clients connected
						clients.put((User)replyMsg.getData().get(1), client);
						client.setName(currMsg.getData().get(0).toString());

						System.out.println("[" + sdf.format(Calendar.getInstance().getTime()) +  "] " + 
								currMsg.getData().get(0).toString() + " has connected.");
					}

					replyMsg.setAction(Action.LOGIN);
				}
				break;
			case LOGOUT:
				// remove client from list of clients and send success message
				System.out.println("[" + sdf.format(Calendar.getInstance().getTime()) +  "] " + 
						client.getName() + " has disconnected.");
				client.setName("");
				clients.remove(getKeybyValue(client));
				ArrayList<Object> input = new ArrayList<Object>();
				input.add(new Integer(0));
				replyMsg = new Message(Action.LOGOUT, new Integer(0));
				break;
			case REGISTER:
				replyMsg = UsersDB.getInstance().AddUser(currMsg.getData());
				break;
			case EDIT_USER_DETAILS:
				replyMsg = UsersDB.getInstance().EditUser(currMsg.getData());
				break;
			case GET_USER_PURCHASES:
				replyMsg = PurchaseDB.getInstance().getPurchasesByUser(currMsg.getData());
				replyMsg.setAction(Action.GET_USER_PURCHASES);
				break;
			case SEARCH:
				replyMsg = MapDB.getInstance().Search(currMsg.getData());
				break;
			case BUY:
				replyMsg = PurchaseDB.getInstance().addPurchase(currMsg.getData());
				replyMsg.setAction(Action.BUY);
				break;
			case VIEW_PURCHASE:
				replyMsg = PurchaseDB.getInstance().viewPurchase(currMsg.getData());
				replyMsg.setAction(Action.VIEW_PURCHASE);
				break;
			case RENEW:
				replyMsg = PurchaseDB.getInstance().renewPurchase(currMsg.getData());
				replyMsg.setAction(Action.RENEW);
				break;
			case DOWNLOAD_PURCHASE:
				replyMsg = PurchaseDB.getInstance().downloadPurchase(currMsg.getData());
				replyMsg.setAction(Action.DOWNLOAD_PURCHASE);
				break;
			case GET_CITY_PRICE:
				replyMsg = CityDB.getInstance().getCitiesList();
				replyMsg.setAction(Action.GET_CITY_PRICE);
				break;
			case EDIT_CITY_PRICE:
				replyMsg = Services.changeCityPriceRequest(currMsg.getData());
				replyMsg.setAction(Action.EDIT_CITY_PRICE);
				break;
			case APPROVE_CITY_PRICE:
				replyMsg = CityDB.getInstance().UpdateCityPriceAfterApproval(currMsg.getData());
				replyMsg.setAction(Action.APPROVE_CITY_PRICE);
				break;
			case SHOW_ALL_CLIENTS:
				replyMsg = UsersDB.getInstance().getAllUsers();
				replyMsg.setAction(Action.SHOW_ALL_CLIENTS);
				break;	
			case GET_CITY:
				replyMsg = CityDB.getInstance().getCity(currMsg.getData());
				replyMsg.setAction(Action.GET_CITY);
				break;
			case DAILY_REPORT:
				replyMsg = ReportsDB.getInstance().produceDailyReport();
				replyMsg.setAction(Action.DAILY_REPORT);
				break;
			case ACTIVITY_REPORT:
				replyMsg = ReportsDB.getInstance().produceActivityReport((currMsg.getData()));
				replyMsg.setAction(Action.ACTIVITY_REPORT);
				break;
			case CITY_ACTIVITY_REPORT:
				replyMsg = ReportsDB.getInstance().produceActivityReportToCity((currMsg.getData()));
				replyMsg.setAction(Action.CITY_ACTIVITY_REPORT);
				break;
			case GET_INBOX_MESSAGES:
				replyMsg = InboxDB.getInstance().getInboxMessagesByReciever((currMsg.getData()));
				replyMsg.setAction(Action.GET_INBOX_MESSAGES);
				break;
			case UPDATE_INBOX_MSG_STATUS:
				replyMsg = InboxDB.getInstance().EditInboxMessageStatus((currMsg.getData()));
				replyMsg.setAction(Action.UPDATE_INBOX_MSG_STATUS);
				break;
			default:
				break;
			}

			client.sendToClient(replyMsg); // Send message to the current client
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/*
	 * Hook method to handle client's connection
	 */
	protected void clientConnected(ConnectionToClient client) {
		// display on server and clients that the client has connected.
		String msg = "[" + sdf.format(Calendar.getInstance().getTime()) +  "] A Client has connected";
		System.out.println(msg);
	}

	/**
	 * Hook method called each time a new client connection is
	 * accepted.
	 * @param client the connection connected to the client.
	 */
	protected void clientDisconnected(ConnectionToClient client) {
		// Remove client from list of clients
		String msg = "[" + sdf.format(Calendar.getInstance().getTime()) +  "] " + client.getName()+ " has disconnected.";
		clients.remove(getKeybyValue(client));
	}

	/*
	 * Hook method to handle client's disconnection *
	 */
	synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
		String msg;

		if (!client.getName().startsWith("Thread")) {
			msg = "[" + sdf.format(Calendar.getInstance().getTime()) +  "] " + client.getName() + " has disconnected.";
		} else {
			msg = "[" + sdf.format(Calendar.getInstance().getTime()) +  "] A Client has disconnected";
		}
		
		clients.remove(getKeybyValue(client));
		System.out.println(msg);
	}

   /**
	* Hook method called when the server starts listening for
	*/
	protected void serverStarted()  {
			Services.setTimer();
	}

	/**
	 * Private method to get current user name params: {@value} user name return
	 * value: {@value} true if user exists, else false
	 **/
	private boolean DoesUserExists(String username) {
		for (User user : clients.keySet()) {
			if(user.getUserName().equals(username)) return true;
		}
		return false;
	}
	
	/**
	 * Private method to get current user object from value object in hash map
	 * value: {@value} true if user exists, else false
	 **/
	private User getKeybyValue(ConnectionToClient client) {
		for (User user : clients.keySet()) {
			if(clients.get(user).equals(client))
				return user;
		}
		
		return null;
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
