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
	private static final DateFormat sdf  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	final public static int DEFAULT_PORT = 5555;
	static private HashMap<User, ConnectionToClient> clients;
	private static ServerController m_ServerController;

	/**
	 * Public constructor of class Server which handles the client-server process
	 * @param port - the port's number
	 */
	public Server(int port) {
		super(port);
		m_ServerController = new ServerController();
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
					replyMsg = new Message(Action.LOGIN, new Integer(1), new String("Client already connected."));
				} else {
					// Get User's details
					replyMsg = UsersDB.getInstance().getUser(currMsg.getData());
					if (((Integer) replyMsg.getData().get(0)) == 0) {
						// Adding the current user to the list of clients connected
						clients.put((User)replyMsg.getData().get(1), client);
						client.setName(currMsg.getData().get(0).toString());
						m_ServerController.setMsg(currMsg.getData().get(0).toString() + " has connected.");
					}

					replyMsg.setAction(Action.LOGIN);
				}
				break;
			case LOGOUT:
				// remove client from list of clients and send success message
				m_ServerController.setMsg(client.getName().equals("") ? 
						"A client " : client.getName() + " has disconnected.");
				client.setName("");
				clients.remove(getKeybyValue(client));
				replyMsg = new Message(Action.LOGOUT, new Integer(0));
				break;
			case REGISTER:
				replyMsg = UsersDB.getInstance().AddUser(currMsg.getData());
				m_ServerController.setMsg("New client has registred the system.");
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
				replyMsg = CityDB.getInstance().downloadCity(currMsg.getData());
				replyMsg.setAction(Action.DOWNLOAD_PURCHASE);
				break;
			case GET_CITY_PRICE:
				replyMsg = CityDB.getInstance().getCitiesList(currMsg.getData());
				replyMsg.setAction(Action.GET_CITY_PRICE);
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
				replyMsg = InboxDB.getInstance().getInboxMessages((currMsg.getData()));
				replyMsg.setAction(Action.GET_INBOX_MESSAGES);
				break;
			case REQUEST_PRICE_CHANGE:
				replyMsg = Services.changeCityPriceRequest((currMsg.getData()));
				replyMsg.setAction(Action.REQUEST_PRICE_CHANGE);
				break;
			case HANDLE_PRICE_CHANGE_REQ:
				replyMsg = Services.handleCityPriceRequest((currMsg.getData()));
				replyMsg.setAction(Action.HANDLE_PRICE_CHANGE_REQ);
				break;
			case REQUEST_NEW_VER_APPROVAL:
				replyMsg = Services.createNewVersionRequest((currMsg.getData()));
				replyMsg.setAction(Action.REQUEST_NEW_VER_APPROVAL);
				break;
			case HANDLE_NEW_VER_REQ:
				replyMsg = Services.handleNewVersionRequest((currMsg.getData()));
				replyMsg.setAction(Action.HANDLE_NEW_VER_REQ);
				break;
			case REMOVE_SITE:
				replyMsg = MapDB.getInstance().updateSitesOfMap(currMsg.getData());
				replyMsg.setAction(Action.REMOVE_SITE);
				break;
			case GET_ALL_SITES_LIST:
				replyMsg = SiteDB.getInstance().getSitesbyCity(currMsg.getData());
				replyMsg.setAction(Action.GET_ALL_SITES_LIST);
				break;
			case EDIT_ROUTE:
				replyMsg = RouteDB.getInstance().UpdateRoute(currMsg.getData());
				replyMsg.setAction(Action.EDIT_ROUTE);
				break;
			case EDIT_MAP:
				replyMsg = MapDB.getInstance().editMapDetails(currMsg.getData());
				replyMsg.setAction(Action.EDIT_MAP);
				break;
			case EDIT_SITE:
				replyMsg = SiteDB.getInstance().updateSiteDetails(currMsg.getData());
				replyMsg.setAction(Action.EDIT_SITE);
				break;
			case ADD_MAP:
				replyMsg = MapDB.getInstance().AddNewMap(currMsg.getData());
				replyMsg.setAction(Action.ADD_MAP);
				break;
			case ADD_SITE:
				replyMsg = MapDB.getInstance().AddSiteToMap(currMsg.getData());
				replyMsg.setAction(Action.ADD_SITE);
				break;
			case ADD_ROUTE:
				replyMsg = RouteDB.getInstance().AddRoute(currMsg.getData());
				replyMsg.setAction(Action.ADD_ROUTE);
				break;
			case ADD_CITY:
				replyMsg = CityDB.getInstance().addNewCity(currMsg.getData());
				replyMsg.setAction(Action.ADD_CITY);
				break;
			case WATCH_MAP:
				replyMsg = PurchaseDB.getInstance().viewPurchase(currMsg.getData());
				replyMsg.setAction(Action.WATCH_MAP);
				break;
			case GET_MAP:
				replyMsg = MapDB.getInstance().getMap(currMsg.getData());
				replyMsg.setAction(Action.GET_MAP);
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
		m_ServerController.setMsg("A Client has connected");
	}

	/**
	 * Hook method called each time a new client connection is
	 * accepted.
	 * @param client the connection connected to the client.
	 */
	protected void clientDisconnected(ConnectionToClient client) {
		// Remove client from list of clients
		clients.remove(getKeybyValue(client));
		if(!client.getName().equals(""))
			m_ServerController.setMsg(client.getName()+ " has disconnected.");
	}

	/*
	 * Hook method to handle client's disconnection *
	 */
	synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
		String msg;

		if (!client.getName().startsWith("Thread") || client.getName().equals("")) {
			msg = client.getName() + " has disconnected.";
		} else {
			msg = "A Client has disconnected";
		}
		
		clients.remove(getKeybyValue(client));
		m_ServerController.setMsg(msg);
	}

   /**
	* Hook method called when the server starts listening for clients
	*/
	protected void serverStarted()  {
		Services.setTimer();
		m_ServerController.serverConnected("Server started to listen on port " + getPort());
		m_ServerController.setServer(this);
	}

	/**
	 * Hook method called when the server stops listening for clients
	*/
	protected void serverStopped() {
		m_ServerController.serverStopped("Server stopped.");
		m_ServerController.setServer(null);
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

	/**
	 * Start listening for clients
	 * @param instance - ServerController instance
	 * @param currentPort - port to listen on
	 */
	public static void runServer(ServerController instance, String currentPort) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(currentPort); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		Server sv = new Server(port);

		try {
			m_ServerController = instance;	// set current ServerController instance
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			m_ServerController.setMsg("ERROR - Could not listen for clients!");
			m_ServerController = null;
		}
	}
}
