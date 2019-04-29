package server;

import java.sql.ResultSet;
import java.util.ArrayList;

import common.*;
import entity.Client;

public class ClientDB {
	// Variables
	private static ClientDB m_instance = null;
	
	/* Constructor */
	private ClientDB() {}
	
	/* Getters */
	public static synchronized ClientDB getInstance() {
		if (m_instance == null) {
			m_instance = new ClientDB();
		}
		
		return m_instance;
	}
	
	// TODO: AddUser function
	public Message getUser(ArrayList<Object> params) {
		// Variables
		Message msg  = new Message(Action.LOGIN, new ArrayList<Object>());
		ResultSet rs = null;
				
		try {
			// Connect to DB
			SQLController.Connect();
								
			// Prepare statement to get current client's details
			String sql = "SELECT * FROM Clients WHERE username = ?";
								
			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);
					
			// check if query succeeded
			if(!rs.next()) {
				msg.getData().add(new Integer(1));
				msg.getData().add(new String("Client does not exist."));
			}
			else {
				msg.getData().add(new Integer(0)); // set query result as success
				rs.beforeFirst();	// Moves cursor to the start of the result set	
						
				// Reads data
				while (rs.next()) {
					Client currClient = new Client(
									rs.getString("firstname"),
									rs.getString("lastname"),
									/*rs.getString("passowrd"),*/
									rs.getString("username"),
									rs.getString("email"),
									Permission.valueOf(rs.getString("permission").toUpperCase()),
									rs.getLong("telephone"),
									rs.getInt("purchases"));

					msg.getData().add(currClient);
				}
			}
					
			// Disconnect DB
			//SQLController.Disconnect(rs);
					
			// Return message
			return msg;
			
			} 
		catch (Exception e) {
				msg.getData().add(new Integer(1));
				msg.getData().add(e.getMessage());
			}
		finally {
				// Disconnect DB
				SQLController.Disconnect(rs);
			}
				
		return msg;
	}
}
