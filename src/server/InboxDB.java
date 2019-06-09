package server;

import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.*;
import entity.InboxMessage;

public class InboxDB {
	// Variables
	private static InboxDB m_instance = null;
	
	/**
	 * Private constructor - prevent this object's creation 
	 */
	private InboxDB() {}

	/**
	 * Returns a static instance of InboxDB object
	 * @return InboxDB
	 */
	public static synchronized InboxDB getInstance() {
		if (m_instance == null) {
			m_instance = new InboxDB();
		}
		
		return m_instance;
	}
	
	/**
	 * Add a new inbox message to database
	 * @param params - Contain dynamic new message's details
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message AddInboxMessage(Object...objects) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		ArrayList<Object> params = new ArrayList<Object>();

		try {
			// Insert into params the values for the INSERT query
			for (Object param : objects) {
				params.add(param);
			}

			// Connect to DB
			SQLController.Connect();

			// Prepare statement to insert new user
			String sql = "INSERT INTO Inbox (`senderUsername`, `senderPermission`, `receiverUsername`, `receiverPermission`, " +
						 "`content`, `status`, `receiveDate`) VALUES (?, ?, NULL, ?, ?, ?, ?)";

			// Execute sql query, get number of changed rows
			int changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0) {
				 throw new Exception("Inbox message was not added successfully.");
			}

			// Add 0 to indicate success
			data.add(new Integer(0));

			}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add(e.getMessage());
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(null);
		}

		return (new Message(null, data));
	}
	
	/**
	 * Edit inbox message to database
	 * @param params - Contain the message to be updated
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message EditInboxMessage(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();

		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare statement to insert new user
			String sql = "UPDATE Inbox SET receiverUsername = ?, status = ? " +
						 "WHERE id = ?";

			// Execute sql query, get number of changed rows
			int changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0) {
				 throw new Exception("Inbox message was not added successfully.");
			}

			// Add 0 to indicate success
			data.add(new Integer(0));

			}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add(e.getMessage());
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(null);
		}

		return (new Message(null, data));
	}
	
	/**
	 * Get inbox message from database by the sender user name
	 * @param params - Contain the sender user name
	 * @return {@link Message} - contains {@link ArrayList} of type {@link Inbox} messages or a failure message
	 */
	public Message getInboxMessagesBySender(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> 		data  	 = new ArrayList<Object>();
		ArrayList<InboxMessage> messages = new ArrayList<InboxMessage>();

		try {
			// Prepare statement to insert new user
			String sql = "SELECT * FROM Inbox WHERE senderUsername = ?";

			// Get messages from private method, add results into the data array list
			messages = getInboxMessages(sql, params);
			data.add(new Integer(0)); data.add(messages);

			}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("No Inbox Messages were found.");
		}

		return (new Message(null, data));
	}

	/**
	 * Get inbox message from database by the reciever's user name
	 * @param params - Contain the reciever's user name
	 * @return {@link Message} - contains {@link ArrayList} of type {@link Inbox} messages or a failure message
	 */
	public Message getInboxMessagesByReciever(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> 		data  	 = new ArrayList<Object>();
		ArrayList<InboxMessage> messages = new ArrayList<InboxMessage>();

		try {
			// Prepare statement to insert new user
			String sql = "SELECT * FROM Inbox WHERE receiverUsername = ?";

			// Get messages from private method, add results into the data array list
			messages = getInboxMessages(sql, params);
			data.add(new Integer(0)); data.add(messages);

			}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("No Inbox Messages were found.");
		}

		return (new Message(null, data));
	}
	
	/**
	 * get inbox message from database by the receiver's permission
	 * @param params - Contain the receiver's permission
	 * @return {@link Message} - contains {@link ArrayList} of type {@link Inbox} messages or a failure message
	 */
	public Message getInboxMessagesByPermission(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> 		data  	 = new ArrayList<Object>();
		ArrayList<InboxMessage> messages = new ArrayList<InboxMessage>();

		try {
			// Prepare statement to insert new user
			String sql = "SELECT * FROM Inbox WHERE receiverPermission = ?";

			// Get messages from private method, add results into the data array list
			messages = getInboxMessages(sql, params);
			data.add(new Integer(0)); data.add(messages);

			}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("No Inbox Messages were found.");
		}

		return (new Message(null, data));
	}
	
	/**
	 * Generic get Inbox Messages
	 * @param sql - SELECT query to be executed
	 * @param params - Contain parameters to the complete the query
	 * @return {@link ArrayList} of type {@link InboxMessage} - result of the SELECT query
	 * @throws Exception 
	 */
	private ArrayList<InboxMessage> getInboxMessages(String sql, ArrayList<Object> params) throws Exception {
		// Variables
		ArrayList<InboxMessage> InboxMessages  = new ArrayList<InboxMessage>();
		ResultSet		rs		  	  = null;

		try {
			// Connect to DB
			SQLController.Connect();

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			// check if query succeeded
			if(!rs.next()) {
				throw new Exception();
			}

			rs.beforeFirst(); // Return cursor to the start of the first row

			// Go through the result set and build the InboxMessage entity
			while (rs.next())
			{
				// Construct new InboxMessage entity
				InboxMessage msg = new InboxMessage(rs.getInt("id"), 
													rs.getString("senderUsername"), 
													Permission.valueOf(rs.getString("senderPermission").toUpperCase()), 
													rs.getString("receiverUsername"), 
													Permission.valueOf(rs.getString("receiverPermission").toUpperCase()), 
													rs.getString("content"), 
													Status.valueOf(rs.getString("status").toUpperCase()),
													rs.getDate("receiveDate").toLocalDate());
				InboxMessages.add(msg);
			}

		}
		catch (SQLException e) {
			throw e;
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(rs);
		}

		return InboxMessages;
	}
}
