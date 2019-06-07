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
	public Message AddPurchase(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		ResultSet 		  rs   = null;
		
		try {
			// Connect to DB
			SQLController.Connect();
			
			// Prepare statement to get current client's purchase
			String sql = "INSERT INTO Purchases (`userame`, `cityName`, `purchaseType`, `purchaseDate`," +
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

		return (new Message(Action.ADD_PURCHASE, data));
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
	 * Generic function for SELECT queries
	 * @param sql - the SELECT query
	 * @params params - {@link ArrayList} of parameters to complete the requested SELECT query 
	 * @return {@link ArrayList} - an {@link ArrayList} of type {@link Purchase} which satisfies the conditions
	 * @throws SQLException, Exception 
	 */
	public ArrayList<Purchase> getPurchases(String sql, ArrayList<Object> params) throws SQLException, Exception {
		// Variables
		ArrayList<Purchase> purchases = new ArrayList<Purchase>();
		ArrayList<Object>   data 	  = new ArrayList<Object>();
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
	 * Method that runs every start of a new day, sends reminders to clients that their purchase is 
	 * about to expire in 3 days from today
	 * @param expireDate - the expire date of 3 days three days prior to today 
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
}
