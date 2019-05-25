package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
	 * @param params - Contain {@link Action} type and the user's name of the requesting user
	 * @return {@link Message} - Contain {@link ArrayList} of the user's purchases or failure message
	 */
	public Message getPurchases(ArrayList<Object> params) {
		// Variables
		ArrayList<Purchase> purchases = new ArrayList<Purchase>();
		ArrayList<Object> data 		  = new ArrayList<Object>();
		ResultSet 		  rs   		  = null;
		
		try {
			// Connect to DB
			SQLController.Connect();
						
			// Prepare statement to get current client's purchase
			String sql = "SELECT * FROM Purchases WHERE username = ?";

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);
			
			// check if query succeeded
			if(!rs.next()) {
				throw new Exception("Purchases for the current user where not found.");
			}
			else {
				rs.beforeFirst();	// Moves cursor to the start of the result set	
				
				// Reads data
				while (rs.next()) {
					Purchase currPurchase = new Purchase(
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

			data.add(new Integer(0)); // set query result as success
			data.add(purchases);	// adding the purchases' array list
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
