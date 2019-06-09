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
		ResultSet 		  rs   = null;
		
		try {
			// Connect to DB
			SQLController.Connect();
			
			// Prepare statement to get current client's purchase
			String sql = "INSERT INTO Purchases (`username`, `cityName`, `purchaseType`, `purchaseDate`," +
						 " `expiryDate`, `renew`, `views`, `downloads`, `price`)" +
						 " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			// Execute sql query, get number of changed rows
			int changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if insert was successful - result should be greater than zero
			if (changedRows == 0) {
				 throw new Exception("Purchase was not completed successfully.");
			}

			// Add 0 to indicate success
			data.add(new Integer(0));
			if(params.get(2).toString().equals(PurchaseType.SHORT_TERM_PURCHASE.toString()))
			{
				// Get city entity by the city's name using getCity method
				ArrayList<Object> input = new ArrayList<Object>(params.subList(1, 2));
				Message city = CityDB.getInstance().getCity(input);
				data.add(city);
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
			SQLController.Disconnect(null);
		}

		return (new Message(Action.BUY, data));
	}
	
	/**
	 * Edit renew column of specific purchase when user renews his purchase
	 * @param params - the purchase entity to renew, user name and city name
	 * @return {@link Message} - Contain result of action - success/failure and failure message in case of failure
	 */
	public Message renewPurchase(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		
		try {
			// Get current active purchase
			Purchase current = (Purchase)params.get(0);

			// Prepare statement to get current client's purchase
			String sql = "UPDATE Purchases SET purchaseDate = ?, expiryDate = ?, renew = renew + 1, price = ?" +
					 	 "WHERE username = ? AND cityName = ? purchaseDate = ? AND expiryDate = ?";

			// Add parameters to match the UPDATE query
			params.add(current.getPurchaseDate()); params.add(current.getExpirationDate());
			params.add(current.getPrice());

			// Execute sql query by calling private method editPurchase with the requested UPDATE query
			editPurchase(sql, params);

			data.add(new Integer(0)); // set query result as success
			data.add("Renew purchase was successful");	// write a success message
		}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("Renew purchase encountered a problem.");
		}

		return new Message(null, data);
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
			String sql = "UPDATE Purchases SET downloads = downloads + 1" +
				 	 "WHERE username = ? AND cityName = ? purchaseDate = ? AND expiryDate = ?";

			// Add parameters to match the UPDATE query
			params.add(current.getPurchaseDate()); params.add(current.getExpirationDate());

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
	 * Edit views column of specific purchase when user views map from the city he bought
	 * @param params - user name and city name
	 * @return {@link Message} - Contain result of action - success/failure and failure message in case of failure
	 */
	public Message viewPurchase(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		
		try {
			// Get current active purchase
			Purchase current = ((ArrayList<Purchase>)((Message)getActivePurchase(params)).getData().get(1)).get(0);
			
			// Prepare statement to get current client's purchase
			String sql = "UPDATE Purchases SET views = views + 1" +
				 	 "WHERE username = ? AND cityName = ? purchaseDate = ? AND expiryDate = ?";

			// Add parameters to match the UPDATE query
			params.add(current.getPurchaseDate()); params.add(current.getExpirationDate());

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
			String sql = "SELECT * FROM Purchases WHERE username = ? AND cityname = ? AND "
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
			String sql = "SELECT * FROM Purchases WHERE cityname = ?";

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
	 * @return {@link ArrayList} - an {@link ArrayList} of type {@link Purchase} which satisfies the conditions
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

			// Go through the result set and build the Purchase entity
			while (rs.next()) 
			{
				Purchase currPurchase = new Purchase(
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
