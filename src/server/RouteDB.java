package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.*;

public class RouteDB {
	// Variables
	private static RouteDB m_instance = null;
	
	/**
	 * Private constructor - prevent this object's creation 
	 */
	private RouteDB() {}

	/**
	 * Returns a static instance of RouteDB object
	 * @return RouteDB
	 */
	public static synchronized RouteDB getInstance() {
		if (m_instance == null) {
			m_instance = new RouteDB();
		}

		return m_instance;
	}

	/**
	 * Get number of routes of a specific city
	 * @param params - the requested city's name
	 * @return {@link Message} - Contain result of action - success/failure and 
	 * the number of routes of the requested city or failure message
	 */
	public Message getRoutesCount(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data 		  = new ArrayList<Object>();
		ResultSet 		  rs   		  = null;
		int 		      routesCount = 0;
		
		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare statement to get number of routes for a specific city
			String sql = "SELECT COUNT(Routes.id) as \"routesCount\"\n" + 
						 "FROM Routes\n" + 
						 "WHERE cityname = (SELECT c.name as \"cityname\"\n" + 
					"                  		FROM Cities c\n" + 
					"                  		WHERE c.name LIKE ? OR c.description LIKE ?)";

			// Add special characters for LIKE search in database
			for(int i = 0; i < params.size(); i++) {
				params.set(i,"%" + params.get(i).toString() + "%");
			}

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			if (!rs.next())
				throw new Exception("Could not find routes for the requested city.");

			rs.beforeFirst();

			// Read data
			while (rs.next()) {
				routesCount = rs.getInt("routesCount");
			}
			
			data.add(new Integer(0)); // set query result as success
			data.add(new Integer(routesCount));	// adding the number to the array list
		}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add(e.getMessage());
		}

		return new Message(null, data);
	}	
}
