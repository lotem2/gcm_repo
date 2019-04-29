package server;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import server.SQLController;
import common.*;
import entity.*;
import entity.Purchase.PurchaseType;

public class PurchaseDB {
	// Variables
	private static PurchaseDB m_instance = null;
	
	/* Constructor */
	private PurchaseDB() {}
	
	public static synchronized PurchaseDB getInstance() {
		if (m_instance == null) {
			m_instance = new PurchaseDB();
		}
		
		return m_instance;
	}
	
	public Message AddPurchase(ArrayList<Object> params) {
		// TODO: add purchase into Purchases table in DB
		// Variables
		Message msg  = new Message(Action.ADD_PURCHASE, new ArrayList<Object>());
		ResultSet rs = null;
		
		try {
			// Connect to DB
			SQLController.Connect();
			
			// Prepare statement to get current client's purchase
			String sql = "SELECT purchases FROM Clients WHERE username = ?";
			
			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);
			
			// check if query succeeded
			if(!rs.next()) {
				msg.getData().add(new Integer(1));
				msg.getData().add(new String("Client does not exist."));
			}
			else {
				int numOfPurchases = 0;	
				rs.beforeFirst();	// Moves cursor to the start of the result set
				
				// Reads data 
				while (rs.next()) {
					numOfPurchases = rs.getInt("purchases");	
				}
				
				// Adding another purchase
				numOfPurchases++;
				
				// Updating the row with the new number	
				sql = "UPDATE Clients SET purchases = ? WHERE username = ?";
				ArrayList<Object> input = new ArrayList<Object>();
				input.add(new Integer(numOfPurchases));
				
				// Add parameters for the prepared statement
				for (Object obj : params) {
					input.add(obj);
				}
				
				// Execute update for the specific row
				if(SQLController.ExecuteUpdate(sql, input) == 0) {
					// Update failed, return 1 to indicate failure
					msg.getData().add(new Integer(1));
					msg.getData().add(new String("Add purchase was not successful"));
				}
				else {
					// Update succeeded, return 0 to indicate success
					msg.getData().add(new Integer(0));
				}
			}
			
			// Disconnect DB
			SQLController.Disconnect(rs);
			
		} catch (Exception e) {
			// Update failed, return 1 to indicate failure
			msg.getData().add(new Integer(1));
			msg.getData().add(e.getMessage());
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(rs);
		}
		
		// Return message
		return msg;
	}
	
	public Message getPurchase(ArrayList<Object> params) {
		// Variables
		Message msg  = new Message(Action.SHOW_CLIENT_DETAILS, new ArrayList<Object>());
		ResultSet rs = null;
		
		try {
			// Connect to DB
			SQLController.Connect();
						
			// Prepare statement to get current client's purchase
			//String sql = "SELECT * FROM Clients WHERE username = ?";
			String sql = "SELECT purchases FROM Clients WHERE username = ?";
						
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
					/*Purchase currPurchase = new Purchase(
							rs.getString("cityName"), 
							PurchaseType.valueOf(rs.getString("PurchaseType")), 
							rs.getObject("PurchaseDate", LocalDate.class), 
							rs.getInt("renew"), 
							rs.getInt("Views"), 
							rs.getInt("Downloads"), 
							rs.getFloat("price"));

					msg.getData().add(currPurchase);*/
					
					msg.getData().add(new Integer(rs.getInt("purchases")));
				}
			}
			
			// Disconnect DB
			//SQLController.Disconnect(rs);
			
			// Return message
			return msg;
		} catch (Exception e) {
			msg.getData().add(new Integer(1));
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(rs);
		}
		
		return msg;
	}
}
