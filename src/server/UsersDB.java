package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.*;
import entity.*;

public class UsersDB {
	// Variables
	private static UsersDB m_instance = null;
	
	/* Constructor */
	private UsersDB() {}
	
	/* Getters */
	public static synchronized UsersDB getInstance() {
		if (m_instance == null) {
			m_instance = new UsersDB();
		}
		
		return m_instance;
	}
	
	// TODO: AddUser function
	public Message getUser(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		ResultSet         rs   = null;
				
		try {
			// Connect to DB
			SQLController.Connect();
								
			// Prepare statement to get current client's details
			String sql = "SELECT * FROM Clients WHERE username = ? AND password = ?";
								
			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);
					
			// check if query succeeded
			if(!rs.next()) {
				throw new Exception("Client does not exist.");
			}
			else {
				data.add(new Integer(0)); // set query result as success
				rs.beforeFirst();	// Moves cursor to the start of the result set	
						
				// Reads data
				while (rs.next()) {
					User currUser;
					Permission per = Permission.valueOf(rs.getString("permission").toUpperCase());
					if (per == Permission.CLIENT) {
						currUser = new Client(
								rs.getString("firstname"),
								rs.getString("lastname"),
								rs.getString("password"),
								rs.getString("username"),
								rs.getString("email"),
								Permission.valueOf(rs.getString("permission").toUpperCase()),
								rs.getLong("telephone"),
								rs.getInt("purchases"));	
					}
					else {
						currUser = new Employee(
								rs.getString("firstname"),
								rs.getString("lastname"),
								rs.getString("password"),
								rs.getString("username"),
								rs.getString("role"),
								rs.getString("email"),
								Permission.valueOf(rs.getString("permission").toUpperCase()),
								rs.getInt("id"));
					}

					data.add(currUser);
				}
			}
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
			SQLController.Disconnect(rs);
		}
				
		return new Message(null, data);
	}
}