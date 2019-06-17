package server;
import java.sql.ResultSet;
import java.util.Map;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import common.Action;
import common.Message;
import common.PurchaseType;
import entity.*;
import server.PurchaseDB;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsDB implements IReport{
	// Variables
	private static ReportsDB m_instance = null;
		
	/**
	 * Private constructor - prevent this object's creation 
	 */
	private ReportsDB() {}
		
	/**
	 * Returns a static instance of ReportsDB object
	 * @return ReportsDB
	 */
	public static synchronized ReportsDB getInstance() {
		if (m_instance == null) {
			m_instance = new ReportsDB();
		}

		return m_instance;

	}
	/**
	 * Update report in the DB on users's activity.
	 * 
	 * @param username - given user name
	 * @param params - report details for a given user
	 * @return - Return true whether activity was added successfully, else returns false.
	 * 
	 */
	public Message InsertReport(String username, ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data   = new ArrayList<Object>();

		try {
			// Connect to DB
			SQLController.Connect();
			
			// Prepare statement to get current client's purchase
			String sql = "INSERT INTO Report (`username`, `purchaseType`, `activityDate`, `content`)" +
						 " VALUES (?, ?, ?, ?) WHERE username = ?";

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

		return new Message(null, data);
	}

   /**
	* Produce daily report which contains per each city its: name, number of purchases,
 	* number of renewed purchases, amount of long term purchases and short term purchases.
 	* 
 	* @return - Return Message that contain data per each city on the daily report.
 	* 
 	*/
	public Message produceDailyReport()
	{
		// Variables
		ArrayList<Object>         data                 = new ArrayList<Object>();
		HashMap <String, Integer> cityDetails          = new HashMap <String, Integer>();
		ResultSet                 rs                   = null;
		String                    sql                  = "";
		int                       numOfRenw            = 0;
		int                       numOfLTP             = 0;
		int                       numOfOTP             = 0;

		try {
			// Connect to DB
			SQLController.Connect();
		    sql = "SELECT cityName ,COUNT(cityName) as 'numOfPurchases', renew,"
		    		+ "SUM(CASE WHEN purchaseType = 'LONG_TERM_PURCHASE' THEN 1 ELSE 0 END) as \'LongTermPurchases\',"
		    		+ "SUM(CASE WHEN purchaseType = 'SHORT_TERM_PURCHASE' THEN 1 ELSE 0 END) as \'ShortTermPurchases\'"
		    		+ "FROM Purchases GROUP BY cityName, renew";

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, null);

			// check if query succeeded
			if(!rs.next()) {
				throw new Exception("Error - encountered an error while producing the report");
			}

			rs.beforeFirst(); // Return cursor to the start of the first row

			// Add 0 to indicate success
			data.add(new Integer(0));

			while (rs.next()) 
			{	
				cityDetails.put(rs.getString("cityName"),rs.getInt("numOfPurchases") );
				numOfRenw += rs.getInt("renew");
				numOfLTP += rs.getInt("LongTermPurchases");
				numOfOTP += rs.getInt("ShortTermPurchases");
			}

			data.add(cityDetails);
			data.add(numOfRenw);
			data.add(numOfLTP);
			data.add(numOfOTP);
		}
		catch(SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e){
			data.add(new Integer(1));
			data.add(e.getMessage());
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(rs);	
		}

		return new Message(Action.DAILY_REPORT, data);
	}

	/**
	* Produce activity report which contains per each city its: 
	* name, number of subscribers, number of renewed purchases, number of views,
	* number of downloads, amount of long term purchases and short term purchases.
	*
	* @param  params - Contains purchase date and expiry purchase date.
	* @return - Return Message that contain data per each city on the daily report.
	*
	*/
	public Message produceActivityReport(ArrayList<Object> params)
	{
		// Variables
		ArrayList<Object>         data        = new ArrayList<Object>();
		ArrayList<Report>		  reports    = new ArrayList<Report>();
		String 					  sql         = "";
		ResultSet				  rs          = null;

		try {

			// Connect to DB
			SQLController.Connect();

			//params will include purchaseDate and exipryDate
			sql = "SELECT cityName ,count(username) as \"numOfMembers\", SUM(renew) as \"renew\", SUM(views) as \"views\", SUM(downloads) as \"downloads\"," +
					"       SUM(CASE WHEN purchaseType = 'LONG_TERM_PURCHASE' THEN 1 ELSE 0 END) as \'LongTermPurchases\'," +
					"	    SUM(CASE WHEN purchaseType = 'SHORT_TERM_PURCHASE' THEN 1 ELSE 0 END) as \'ShortTermPurchases\'" +
					"       FROM Purchases" +
					"       WHERE purchaseDate >= ? AND expiryDate <= ? " +
					"GROUP by cityName";

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			// check if query succeeded
			if(!rs.next()) {
				throw new Exception("Error - report data was not found for the requested dates");
			}

			rs.beforeFirst();

			// Add 0 to indicate success
			data.add(new Integer(0));

			// Go through the result set and build the Purchase entity
			while (rs.next()) 
			{
				Report report = new Report ( 
						   	rs.getString("cityName"),
							rs.getInt("numOfMembers"), 
							rs.getInt("renew"), 
							rs.getInt("views"),
							rs.getInt("downloads"),
							rs.getInt("LongTermPurchases"),
							rs.getInt("ShortTermPurchases")
				);
				reports.add(report);
			}
			data.add(reports);
	}
	catch(SQLException e) {
		data.add(new Integer(1));
		data.add("There was a problem with the SQL service.");
	}
	catch(Exception e){
		data.add(new Integer(1));
		data.add(e.getMessage());
	}
	finally {
		// Disconnect DB
		SQLController.Disconnect(rs);	
	}
	return new Message(Action.ACTIVITY_REPORT, data);
	}

	/**
	* Produce activity report for a specific city which contains its:
	* name, number of subscribers, number of renewed purchases, number of views,
	* number of downloads, amount of long term purchases and short term purchases.
	*
	* @param  params - Contains purchase date, expiry purchase date and the city name.
	* @return - Return Message that contain data per each city on the daily report.
	*
	*/
	public Message produceActivityReportToCity(ArrayList<Object> params)
	{
		// Variables
		ArrayList<Object>         data        = new ArrayList<Object>();
		String 					  sql         = "";
		ResultSet				  rs          = null;

		try {

			// Connect to DB
			SQLController.Connect();

			//params will include purchaseDate, exipryDate and cityName
			sql = "SELECT cityName ,count(username) as \"numOfMembers\", SUM(renew) as \"renew\", SUM(views) as \"views\", SUM(downloads) as \"downloads\"," +
					"       SUM(CASE WHEN purchaseType = 'LONG_TERM_PURCHASE' THEN 1 ELSE 0 END) as \'LongTermPurchases\'," +
					"	    SUM(CASE WHEN purchaseType = 'SHORT_TERM_PURCHASE' THEN 1 ELSE 0 END) as \'ShortTermPurchases\'" +
					"       FROM Purchases" +
					"       WHERE purchaseDate >= ? AND expiryDate <= ?  AND cityName = ? " +
					" GROUP by cityName";

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			// check if query succeeded
			if(!rs.next()) {
				throw new Exception("Error - report data was not found for the requested dates");
			}

			rs.beforeFirst();

			// Add 0 to indicate success
			data.add(new Integer(0));

			// Go through the result set and build the Purchase entity
			while (rs.next())
			{
				Report report = new Report (
							rs.getString("cityName"),
							rs.getInt("numOfMembers"),
							rs.getInt("renew"),
							rs.getInt("views"),
							rs.getInt("downloads"),
							rs.getInt("LongTermPurchases"),
							rs.getInt("ShortTermPurchases")
				);
				data.add(report);
			}
	}
	catch(SQLException e) {
		data.add(new Integer(1));
		data.add("There was a problem with the SQL service.");
	}
	catch(Exception e){
		data.add(new Integer(1));
		data.add(e.getMessage());
	}
	finally {
		// Disconnect DB
		SQLController.Disconnect(rs);
	}
	return new Message(Action.CITY_ACTIVITY_REPORT, data);
	}

}
