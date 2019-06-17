package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import server.SQLController;
import common.*;
import entity.*;
import entity.Purchase.PurchaseType;

public class PurchaseDB {
	// Variables
	private static PurchaseDB m_instance = null;
	
	/**
	 * Private constructor - prevent this object's creation 
	 */
	private PurchaseDB() {}

	/**
	 * Returns a static instance of PurchaseDB object
	 * @return PurchaseDB
	 */
	public static synchronized PurchaseDB getInstance() {
		if (m_instance == null) {
			m_instance = new PurchaseDB();
		}
		
		return m_instance;
	}

	/**
	 * Add a new {@link Purchase} to database
	 * @param params - Contain {@link Action} type and details of user's new purchase
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message addPurchase(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		ArrayList<Object> input = new ArrayList<Object>();
		ResultSet 		  rs   = null;
		Message			  msg  = null;
		String 			  sql  = "";		
		
		try {
			// Connect to DB
			SQLController.Connect();
			
			if(params.get(2).toString().equals(PurchaseType.SHORT_TERM_PURCHASE.toString())) {
			// Prepare statement to insert new purchase to current client
			 sql = "INSERT INTO Purchases (`username`, `cityName`, `purchaseType`, `purchaseDate`," +
						 " `expiryDate`, `renew`, `views`, `downloads`, `price`)" +
						 " VALUES (?, ?, ?, ?, ?, ?, ?, 1, ?)";
			 params.remove(7);
			}
			else
				 sql = "INSERT INTO Purchases (`username`, `cityName`, `purchaseType`, `purchaseDate`," +
						 " `expiryDate`, `renew`, `views`, `downloads`, `price`)" +
						 " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			// Execute sql query, get number of changed rows
			int changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if insert was successful - result should be greater than zero
			if (changedRows == 0) {
				 throw new Exception("Purchase was not completed successfully.");
			}

			// Update the purchased city counter on the database
			data.add(params.get(1));
			msg = CityDB.getInstance().updateCityPurchaseCounter(data);
			if(msg.getData().equals(new Integer(1))) throw new Exception("Could not update city counter");

			data.clear();
			if(params.get(2).toString().equals(PurchaseType.SHORT_TERM_PURCHASE.toString()))
			{
				
				// Get city entity by the city's name using getCity method
				input.add(Permission.CLIENT);
				input.add(params.get(1));
				Message city = CityDB.getInstance().getCity(input);
				if((Integer)city.getData().get(0) == 1)
					throw new Exception("Could not retrieve city for download");
				
				// Add 0 to indicate success
				data.add(new Integer(0));
				data.add(city.getData().get(1));
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

		return (new Message(Action.BUY, data));
	}
	
	/**
	 * Edit renew column of specific purchase when user renews his purchase
	 * @param params - the purchase entity to renew
	 * @return {@link Message} - contains {@link ArrayList} of type {@link Purchase} 
	 * the updated user's purchases after renewal
	 */
	public Message renewPurchase(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		Message msg;

		try {
			// Get current active purchase
			Purchase current = (Purchase)params.get(0);

			// Prepare statement to get current client's purchase
			String sql = "UPDATE Purchases SET purchaseDate = ?, expiryDate = ?, renew = renew + 1, price = ?" +
					 	 "WHERE id = ?";
			
			// Add parameters to match the query
			data.add(current.getPurchaseDate()); data.add(current.getExpirationDate());
			data.add(current.getPrice()); data.add(current.getID());

			// Execute sql query by calling private method editPurchase with the requested UPDATE query
			editPurchase(sql, data);
			
			// After update, get the user's list of purchases to display the updated information
			data.clear(); data.add(current.getUserName()); Message result = getPurchasesByUser(data);
			if((Integer)result.getData().get(0) == 1)
				throw new Exception(result.getData().get(1).toString());

			msg = new Message(null, new Integer(0), result.getData().get(1));
		}
		catch (SQLException e) {
			msg = new Message(null, new Integer(1), "There was a problem with the SQL service.");
		}
		catch(Exception e) {
			msg = new Message(null, new Integer(1), e.getMessage());
		}

		return msg;
	}
	
	/**
	 * Edit downloads column of specific purchase when user downloads city's collection of maps
	 * @param params - user name and city name
	 * @return {@link Message} - Contain result of action - success/failure and failure message in case of failure
	 */
	public Message downloadPurchase(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		
		try {
			// Get current active purchase
			Purchase current = ((ArrayList<Purchase>)((Message)getActivePurchase(params)).getData().get(1)).get(0);
			
			// Prepare statement to get current client's purchase
			String sql = "UPDATE Purchases SET downloads = downloads + 1 WHERE id = ?";

			ArrayList<Object> sql_params = new ArrayList<>();
			// Add parameters to match the UPDATE query
			sql_params.add(current.getID());

			// Execute sql query by calling private method editPurchase with the requested UPDATE query
			editPurchase(sql, sql_params);

			data.add(new Integer(0)); // set query result as success

		}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("Could not update purchase.");
		}

		return new Message(null, data);
	}

	/**
	 * Edit views column of specific purchase when user views map from the city he bought
	 * @param params - purchase's id
	 * @return {@link Message} - Contain result of action - success/failure and failure message in case of failure
	 */
	public Message viewPurchase(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		
		try {
			// Prepare statement to get current client's purchase
			String sql = "UPDATE Purchases SET views = views + 1 WHERE id = ?";

			// Execute sql query by calling private method editPurchase with the requested UPDATE query
			editPurchase(sql, params);

			data.add(new Integer(0)); // set query result as success

		}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("Could not update purchase.");
		}

		return new Message(null, data);
	}
	
	/**
	 * Get user's active purchase according to a city 
	 * @param params - the requested user's name, city name
	 * @return {@link Message} - Contain result of action - success/failure and 
	 * {@link Purchase} - the user's active purchase or failure message
	 */
	public Message getActivePurchase(ArrayList<Object> params) {
		// Variables
		ArrayList<Purchase> purchases;
		ArrayList<Object> data = new ArrayList<Object>();

		try {
			// Prepare statement to get current client's purchase
			String sql = "SELECT * FROM Purchases WHERE username = ? AND cityName = ? AND "
					+ "expiryDate > ? AND purchaseType = ? LIMIT 1";

			// Add today's date and purchase type to match the query parameter
			params.add(LocalDate.now()); params.add(PurchaseType.LONG_TERM_PURCHASE.toString());
			
			// Execute sql query by calling private method getPurchases with the requested SELECT query
			purchases = getPurchases(sql, params);

			data.add(new Integer(0)); // set query result as success
			data.add(purchases);	// adding the purchases' array list
		}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("Active purchases for the current user where not found.");
		}

		return new Message(null, data);
	}
	
	/**
	 * Get user's purchases according to the user's name provided by the client
	 * @param params - the requested user's name
	 * @return {@link Message} - Contain result of action - success/failure and 
	 * {@link ArrayList} of type {@link Purchase} - the user's purchases or failure message
	 */
	public Message getPurchasesByUser(ArrayList<Object> params) {
		// Variables
		ArrayList<Purchase> purchases;
		ArrayList<Object> data = new ArrayList<Object>();

		try {
			// Prepare statement to get current client's purchase
			String sql = "SELECT * FROM Purchases WHERE username = ?";

			// Execute sql query by calling private method getPurchases with the requested SELECT query
			purchases = getPurchases(sql, params);

			data.add(new Integer(0)); // set query result as success
			data.add(purchases);	// adding the purchases' array list
		}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("Purchases for the current user where not found.");
		}

		return new Message(null, data);
	}
	
	/**
	 * Get user's purchases according to a specific city name provided by the client
	 * @param params - the requested city's name
	 * @return {@link Message} - Contain result of action - success/failure and 
	 * {@link ArrayList} of type {@link Purchase} - purchases of users of the requested city or failure message
	 */
	public Message getPurchasesByCity(ArrayList<Object> params) {
		// Variables
		ArrayList<Purchase> purchases;
		ArrayList<Object> data = new ArrayList<Object>();
		
		try {			
			// Prepare statement to get current client's purchase
			String sql = "SELECT * FROM Purchases WHERE cityName = ?";

			// Execute sql query by calling private method getPurchases with the requested SELECT query
			purchases = getPurchases(sql, params);

			data.add(new Integer(0)); // set query result as success
			data.add(purchases);	// adding the purchases' array list
		}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("Purchases for the current city where not found.");
		}
		
		return new Message(null, data);
	}
	
	/**
	 * Get purchases according to a specific expiry date - for a renewal's reminder to the clients
	 * @param params - the expiry date of a purchase
	 * @return {@link Message} - Contain result of action - success/failure and 
	 * {@link ArrayList} of type {@link Purchase} - purchases of with the requested expiry date or failure message
	 */
	public Message getPurchasesByExpiryDate(ArrayList<Object> params) {
		// Variables
		ArrayList<Purchase> purchases;
		ArrayList<Object> data = new ArrayList<Object>();
		
		try {			
			// Prepare statement to get current client's purchase
			String sql = "SELECT * FROM Purchases WHERE expiryDate = ?";

			// Execute sql query by calling private method getPurchases with the requested SELECT query
			purchases = getPurchases(sql, params);

			data.add(new Integer(0)); // set query result as success
			data.add(purchases);	// adding the purchases' array list
		}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("Purchases for the current city where not found.");
		}
		
		return new Message(null, data);
	}

	/**
	 * Method that runs every start of a new day, sends reminders to clients that their purchase is 
	 * about to expire in 3 days from today
	 * @param expireDate - the expire date of 3 days prior to today
	 * 
	 */
	public void sendReminders(LocalDate expireDate){
		// Variables
		ArrayList<Purchase> 	  purchases = new ArrayList<Purchase>();
		ArrayList<Object> 	  	  data 		= new ArrayList<Object>();
		HashMap<Purchase, String> users 	= new HashMap<Purchase, String>();
		ResultSet				  rs		= null;

		try {
			// Get the list of purchases with expire date equals to the expireDate parameter
			data.add(expireDate);
			Message msg = getPurchasesByExpiryDate(data);

			// If there are no purchases with this expiry date or there was a sql error stop the function
			if(msg.getData().get(1) instanceof String) return;

			purchases = (ArrayList<Purchase>)msg.getData().get(1);
			
			// Clear data array list for the next query and adds the parameters for it
			data.clear();
			for (Purchase purchase : purchases) {
				data.add(purchase.getUserName());
			}

			// Get the first name and email of the clients using getUsersDetails from UserD
			HashMap<String, String> details = UsersDB.getInstance().getUsersDetails(data);
			
			if(details == null) return; // Check for null object

			// Build HashMap object for the mail reminder method
			for (int i = 0; i < details.size(); i++) {
				users.put(purchases.get(i), details.get(purchases.get(i).getUserName()));
			}

			// Send the clients a reminder via mail using Services' sendReminderMail
			Services.sendReminderMail(users);			
		}
		catch(Exception e) {
			e.printStackTrace();;
		}
	}

	/**
	 * Generic function for SELECT queries
	 * @param sql - the SELECT query
	 * @param params - {@link ArrayList} of parameters to complete the requested SELECT query 
	 * @return {@link ArrayList} - an {@link ArrayList} of type {@link Purchase} which satisfies the conditions
	 * @throws SQLException, Exception 
	 */
	private ArrayList<Purchase> getPurchases(String sql, ArrayList<Object> params) throws SQLException, Exception {
		// Variables
		ArrayList<Purchase> purchases = new ArrayList<Purchase>();
		ResultSet			rs		  = null;

		try {
			// Connect to DB
			SQLController.Connect();

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			// check if query succeeded
			if(!rs.next()) {
				throw new Exception();
			}

			rs.beforeFirst(); // Return cursor to start of the first row
			
			// Go through the result set and build the Purchase entity
			while (rs.next()) 
			{
				Purchase currPurchase = new Purchase(
						rs.getInt("id"),
						rs.getString("username"),
						rs.getString("cityName"), 
						PurchaseType.valueOf(rs.getString("purchaseType").toUpperCase()), 
						rs.getDate("purchaseDate").toLocalDate(),
						rs.getDate("expiryDate").toLocalDate(),
						rs.getInt("renew"), 
						rs.getInt("views"), 
						rs.getInt("downloads"), 
						rs.getFloat("price"));

				purchases.add(currPurchase);
			}	
		}
		catch(SQLException e) {
			throw e;
		}
		catch(Exception e){
			throw e;
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(rs);	
		}

		return purchases;
	}
	
	/**
	 * Generic function for UPDATE queries
	 * @param sql - the UPDATE query
	 * @param params - {@link ArrayList} of parameters to complete the requested UPDATE query 
	 * @throws SQLException, Exception 
	 */
	private void editPurchase(String sql, ArrayList<Object> params) throws SQLException, Exception {
		try {
			// Connect to DB
			SQLController.Connect();

			// Execute sql query, get results
			int changedRows = SQLController.ExecuteUpdate(sql, params);

			// check if query succeeded
			if(changedRows == 0) {
				throw new Exception();
			}	
		}
		catch(SQLException e) {
			throw e;
		}
		catch(Exception e){
			throw e;
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(null);	
		}
	}
}
