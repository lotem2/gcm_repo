package server;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import common.*;
import entity.*;

public class UsersDB {
	// Variables
	private static UsersDB m_instance = null;
	
	/**
	 * Private constructor - prevent this object's creation 
	 */
	private UsersDB() {}
	
	/**
	 * Returns a static instance of UsersDB object
	 * @return UserDB
	 */
	public static synchronized UsersDB getInstance() {
		if (m_instance == null) {
			m_instance = new UsersDB();
		}
		
		return m_instance;
	}

	/**
	 * Add a new {@link User} to database
	 * @param params - Contain {@link Action} type and details of the new user
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message AddUser(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		// Get the users's password
		String hash="";
		String password = params.get(3).toString();
		// Create a unique and random salt to the new user
		byte[] salt = common.Jhash.Hash.randomSalt(24);
		try { 
			hash = common.Jhash.Hash.createHash(password, salt);
		}
		catch (Exception e)	{
			data.add(new Integer(1));
			data.add("Producing hashed password encountered a problem.");
		}
		//Add hashed password and user's unique salt to params.
		params.set(3, hash);
		params.set(4, salt);

		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare statement to insert new user
			String sql = "INSERT INTO Users (`firstname`, `lastname`, `username`, `password`, `salt`, `email`, " +
						"`permission`, `telephone`, `cardnumber`, `id`, `expirydate`)" +
						" VALUES (?, ?, ?, ?, ? ,? ,?, ?, ?, ?, ?)";

			// Execute sql query, get number of changed rows
			int changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if insert was successful - result should be greater than zero
			if (changedRows == 0) {
				 throw new Exception("User was not added successfully.");
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

		return (new Message(Action.REGISTER, data));
	}

	/**
	 * Get {@link User} according to the user's name and password provided by the client
	 * @param params - Contain {@link Action} type and the user's name of the requested user
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message getUser(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		ResultSet         rs   = null;

		// Save password given by the user
		String password = params.get(1).toString();
		params.remove(1);

		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare statement to get current client's details
			String sql = "SELECT * FROM Users WHERE username = ?";

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			// check if query succeeded
			if(!rs.next()) {
				throw new Exception("Username does not exist.");
			}
			else {
				boolean passMatch = verifyUsersPassword(password, rs.getBytes(4), rs.getBytes(5), rs.getString(7));

				if(!passMatch)
				{
					data.add(new Integer(1));
					data.add("Password does not match username.");
				}
				else
				{
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
								rs.getBytes("salt"),
								rs.getString("username"),
								rs.getString("email"),
								Permission.valueOf(rs.getString("permission").toUpperCase()),
								rs.getLong("telephone"),
								rs.getLong("cardnumber"),
								rs.getLong("id"),
								rs.getDate("expirydate").toLocalDate());
					}
					else {
						currUser = new Employee(
								rs.getString("firstname"),
								rs.getString("lastname"),
								rs.getString("password"),
								rs.getString("username"),
								rs.getString("email"),
								Permission.valueOf(rs.getString("permission").toUpperCase()),
								rs.getInt("id"));
					}

					data.add(currUser);
				}
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

	/**
	 * Get {@link ArrayList} of type {@link User} for management 
	 * @return {@link Message} - Include details of all user in {@link ArrayList} or failure message
	 */
	public Message getAllUsers() {
		// Variables
		ArrayList<Client> clients = new ArrayList<Client>();
		ArrayList<Object> data 	  = new ArrayList<Object>();
		ResultSet         rs   	  = null;

		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare statement to get current client's details
			String sql = "SELECT * FROM Users WHERE permission = ?";

			// Create parameters for query
			ArrayList<Object> params = new ArrayList<Object>();
			params.add("Client");

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			// check if query succeeded
			if(!rs.next())
				throw new Exception("Could not find any clients.");

			rs.beforeFirst();
			
			// Reads data
			while (rs.next()) {
				Client currClient = new Client(
								rs.getString("firstname"),
								rs.getString("lastname"),
								rs.getString("password"),
								rs.getBytes("salt"),
								rs.getString("username"),
								rs.getString("email"),
								Permission.valueOf(rs.getString("permission").toUpperCase()),
								rs.getLong("telephone"),
								rs.getLong("cardnumber"),
								rs.getLong("id"),
								rs.getDate("expirydate").toLocalDate());

					clients.add(currClient);
			}

			data.add(new Integer(0)); data.add(clients); // Prepare data for success message
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
	
	/**
	 * Verify the user's password
	 * @param password -   given password 
	 * @param dbPassword - password from the db
	 * @param salt - client's salt from the db
	 * @param permission - user's permission.
	 * @return Boolean - Indicates whether there was a match between given password and password from the db.
	 */
	public boolean verifyUsersPassword(String password, byte[] dbPassword, byte[] salt,String permission )//false;
	{
		if(permission.toUpperCase().equals("CLIENT"))
		{
			try
			{
				// Compare between password given by user to password hashed in the database.
				return common.Jhash.Hash.validatePassword(password, new String(dbPassword), salt);
			}
			catch(Exception e)
			{
				System.out.println("ERROR - Could not validate password for this client!");
			}
		}
		else
		{
			if (password.equals(new String(dbPassword)))
				return true;
		}
		return false;
	}


	/**
	 * Edit a specific user's details 
	 * @param params - Contains {@link Action} user details
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message EditUser(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data 	   = new ArrayList<Object>();

		try {
			SQLController.Connect();

			// Prepare statement to insert new user
			String sql = "UPDATE Users SET firstname = ?, lastname = ?, username = ?, password = ?, salt = ?" +
						" ,email = ?, permission = ?, telephone = ?, cardnumber = ?, id = ?, expirydate = ?" +
						" WHERE username = ?";

			// Execute sql query, get number of changed rows
			int changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0) {
				 throw new Exception("User was not updated successfully.");
			}

			data.clear();
			// Add 0 to indicate success
			data.add(new Integer(0));

			}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(InvalidKeySpecException|NoSuchAlgorithmException e) { // JHash exception's handling
			data.add(new Integer(1));
			data.add("Producing hashed password encountered a problem.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add(e.getMessage());
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(null);
		}

		return (new Message(Action.EDIT_USER_DETAILS, data));
	}

	/**
	 * Get first name and email of users for the email reminder
	 * @param params - {@link ArrayList} of type {@link Object} containing list of user's username
	 * @return {@link HashMap} - pairs of (first name,email) 
	 */
	public HashMap<String, String> getUsersDetails(ArrayList<Object> params){
		// Variables
		HashMap<String, String> details   = new HashMap<String, String>();
		ResultSet 		  		rs 		  = null;
		ArrayList<Object> 		usernames = new ArrayList<Object>();

		try { 
			// Connect to DB
			SQLController.Connect();

			// Get the names into a new ArrayList
			ArrayList<Object> names = new ArrayList<Object>(params.subList(0, params.size()));

			// Get every user name into an array list of objects for the query execution
			for (Object username : names) {
				usernames.add(username.toString());
			}

			// Prepare statement to insert new user
			String sql = "SELECT username, firstname, email FROM Users WHERE username IN (";

			// Append '?,' as the number of users needed
			for (int i = 0; i < usernames.size(); i++)
				sql += "?,";

			// Execute sql query, get number of changed rows
			rs = SQLController.ExecuteQuery(sql.substring(0, sql.length()-1) + ")", usernames);

			// If query was not successful returning null
			if(!rs.next())
				throw new Exception();

			rs.beforeFirst();

			// Read data
			while (rs.next()) {
				// Concatenate first name and email of the current user
				/*String username = rs.getString("username");
				String firstname = rs.getString("firstname");
				String email = rs.getString("email");*/
				details.put(rs.getString("username"), rs.getString("firstname") + "," + rs.getString("email"));
			}
		}
		catch(Exception e) {
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(null);
		}

		return details;		
	}
}