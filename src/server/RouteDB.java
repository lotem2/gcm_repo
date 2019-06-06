package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.*;
import entity.*;

/**
 * Responsible to interact with the Routes table in database and the different components route requires
 */
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
	
	/**
	 * Get routes list according to the requested city
	 * @param params - Contain city name
	 * @return {@link Message} - Contain {@link ArrayList} of type {@link Route} if retrieval succeeded, 
	 * else failure message
	 */
	public Message getRoutesByCity(ArrayList<Object> params){
		// Variables
		ArrayList<Route> routes   = new ArrayList<Route>();
		Message 		 replyMsg = null;
		ResultSet 		 rs 	  = null;
		ArrayList<Site>  sites;

		try {
			// Connect to DB
			SQLController.Connect();

			String sql = "SELECT * FROM Routes WHERE cityname = ?"; // Prepare SELECT query

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			// check if query succeeded
			if(!rs.next())
				throw new Exception("Routes not found for the requested city.");

			rs.beforeFirst(); // Return cursor to the start of the first row

			// Reads data
			while (rs.next())
			{
				int id = rs.getInt("id");
				String cityname = rs.getString("cityname");
				String description = rs.getString("description");

				// Get current route's sites list using SiteDB's getSitesByRoute
				ArrayList<Object> routeDesc = new ArrayList<Object>(); routeDesc.add(description);
				Message msg = SiteDB.getInstance().getSitesbyRoute(routeDesc);

				// Set the list of sites as null in case of a problem with the database/no sites for this route
				sites = msg.getData().get(1) instanceof String ? null : (ArrayList<Site>)msg.getData().get(1);

				// Add the new route to the array list
				routes.add(new Route(id, cityname, sites, description));
			}

			replyMsg = new Message(null, new Integer(0), routes);	// Create success message
		}
		catch (SQLException e) {
			replyMsg = new Message(null, new Integer(1), e.getMessage());
		}
		catch(Exception e) {
			replyMsg = new Message(null, new Integer(1), e.getMessage());
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(rs);	
		}

		return replyMsg;
	}
}
