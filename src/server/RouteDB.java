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
					"                  		WHERE c.name LIKE ? OR c.description LIKE ?) AND Routes.is_active = 1";

			// Add special characters for LIKE search in database
			for(int i = 0; i < params.size(); i++) {
				if(!params.get(i).toString().contains("%"))	// Add '%' in case the current parameter does not contain it
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
	 * @param params - Contains User permission and city name
	 * @return {@link Message} - Contains {@link ArrayList} of type {@link Route} if retrieval succeeded, 
	 * else failure message
	 */
	public Message getRoutesByCity(ArrayList<Object> params){
		// Variables
		ArrayList<Route> routes   = new ArrayList<Route>();
		ArrayList<Site>  sites    = new ArrayList<Site>();
		Message 		 replyMsg = null;
		ResultSet 		 rs 	  = null;
		String           sql      = null;

		try {
			// Connect to DB
			SQLController.Connect();

			if((params.get(0).toString().equals(Permission.CLIENT.toString())))
			{
				// Prepare SELECT query for client
				sql = "SELECT * FROM Routes WHERE cityname = ? and is_active = 1";
			}
			else
			{
				sql = "SELECT * \n" + 
						"FROM Routes r \n" + 
						"WHERE r.cityname = ? AND r.id IN \n" + 
						"(SELECT a.id as \"id\" FROM Routes a \n" + 
						"WHERE r.cityname = a.cityname AND r.name = a.name AND \n" + 
						"NOT EXISTS(SELECT 1 FROM Routes b "
						+ "WHERE b.name = a.name AND b.cityname = a.cityname AND "
						+ "b.is_active = 0 AND a.is_active = 1))";
			}

			ArrayList<Object> city = new ArrayList<Object>(params.subList(1, params.size()));
			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, city);

			// check if query succeeded
			if(!rs.next())
				throw new Exception("Routes not found for the requested city.");

			rs.beforeFirst(); // Return cursor to the start of the first row

			// Reads data
			while (rs.next())
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String cityname = rs.getString("cityname");
				String description = rs.getString("description");

				// Add the new route to the array list
				routes.add(new Route(id, name, cityname, null, description));
			}

			for (Route route : routes) {
				// Get current route's sites list using SiteDB's getSitesByRoute
				ArrayList<Object> routeid = new ArrayList<Object>(params.subList(0, 1)); 
				routeid.add(route.getID());
				Message msg = SiteDB.getInstance().getSitesbyRoute(routeid);

				// Set the list of sites as null in case of a problem with the database/no sites for this route
				sites = msg.getData().get(1) instanceof String ? null : (ArrayList<Site>)msg.getData().get(1);
				route.setSites(sites);
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

	/**
	 * Get route according to the requested city, description and sites
	 * @param params - Contains route name, city name, description and sites
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message UpdateRoute(ArrayList<Object> params){
		// Variables
		ArrayList<Object> data        = new ArrayList<Object>();
		Message 		  msg         = null;
		String            sql         = null;

		try {
			// Check if a new map version is currently under management approval
			if(SQLController.DoesRecordExist("Inbox","content", "status", 
					"Approve " + params.get(1).toString() + "'s new version", "New"))
				throw new Exception("New version is under approval, cannot save new changes.");

            // Check if a change to the requested route was already made
            if(SQLController.DoesRecordExist("Routes","name","cityname", "is_active",
								params.get(0), params.get(1), 0))
            {
            	// Prepare statement to insert new route
            	sql = "UPDATE Routes SET name = ?, cityname = ?, description = ?," +
							 "sites = ?, is_active = 0 " +
							 "WHERE name = ? AND cityname = ? AND is_active = 0";

            	// Add parameters for the WHERE clause
            	params.add(params.get(0)); params.add(params.get(1)); 
            }
            else // A change to the requested route hasn't been made yet
            {
            	// Prepare statement to insert new route
    			sql = "INSERT INTO Routes (`name`, `cityname`, `description`, `sites`, `is_active`)" +
    						 "VALUES (?, ?, ?, ?, 0) ";
            }

			// Insert update to routes using private editRoute method
            if(editRoute(sql, params) == 0) throw new Exception("Route was not updated successfuly");

            msg = new Message(Action.EDIT_ROUTE, new Integer(0), "Update route was successful.");
		} catch (SQLException e) {
    		msg = new Message(null, new Integer(1), e.getMessage());
    	}
    	catch(Exception e) {
    		msg = new Message(null, new Integer(1), e.getMessage());
    	}

    	return msg;
    }

	/**
	 * Add route according to the requested city, description and sites
	 * @param params - Contain route name, city name, description and sites
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message AddRoute(ArrayList<Object> params){
		// Variables
		Message 		  msg         = null;
		String            sql         = null;

		try {
			// Check if a new map version is currently under management approval
			if(SQLController.DoesRecordExist("Inbox","content", "status", 
					"Approve " + params.get(1).toString() + "'s new version", "New"))
				throw new Exception("New version is under approval, cannot save new changes.");

            // Check if a change to the requested route was already made
            if(SQLController.DoesRecordExist("Routes","name", "cityname", "description", "sites", 
								params.get(0), params.get(1), params.get(2), params.get(3)))
            {
            	throw new Exception("This route already exists!");
            }
            else // A change to the requested route hasn't been made yet
            {
            	// Prepare statement to insert new route
            	sql = "INSERT INTO Routes (`name`, `cityname`, `description`, `sites`)" +
            			"VALUES (?, ?, ?, ?) ";
            }
            
            // Insert new updates to Routes table using private editRoute method
            if (editRoute(sql, params) == 0) throw new Exception("Route was not added successfuly");
            msg = new Message(Action.ADD_ROUTE, new Integer(0), "Route was added successfully.");

		} catch (SQLException e) {
			msg = new Message(null, new Integer(1), e.getMessage());
		}
		catch(Exception e) {
    		msg = new Message(null, new Integer(1), e.getMessage());
    	}

		return msg;
	}

	/**
	 * Responsible to update route 
	 * @param params - Contains manager's response to the publishing and city name
	 * @throws Exception - an exception was thown
	 */
	public void publishRoutes(ArrayList<Object> params) throws Exception {
		// Variables
		String sql 		= "";
		String response = params.get(0).toString();

		// Get the needed parameters for the queries ahead
		params = new ArrayList<Object>(params.subList(1, params.size()));

		try {
			// If new version was approved - update details of existing maps to the changes made
			if(response.equals("APPROVED")) {
				// Update exisiting map's details to display the changes
				sql = "UPDATE  Routes r1 \n" + 
						"CROSS JOIN Routes r2 \n" + 
						"SET     r1.name = r2.name, \n" + 
						"		r1.cityname = r2.cityname, \n" + 
						"	 	r1.description = r2.description, \n" + 
						"		r1.sites = r2.sites \n" + 
						"WHERE   r1.name = r2.name AND \n" + 
						"		r1.cityname = r2.cityname AND \n" + 
						"		r1.is_active = 1 AND \n" + 
						"		r2.is_active = 0 AND \n" + 
						"        r1.cityname = ?";

				// Execute sql query using private editMap method
				editRoute(sql, params);

				// Update new maps to be displayed to users
				sql = "UPDATE Routes SET Routes.is_active = 1 \n" + 
					  "WHERE Routes.cityname = ? AND \n" + 
					  "Routes.is_active = 0 AND \n" + 
					  "NOT EXISTS(SELECT 1 FROM (SELECT * FROM Routes) as T1 "
					  + "WHERE T1.name = Routes.name AND T1.cityname = Routes.cityname AND T1.is_active = 1)";

				// Execute sql query using private editMap method
				editRoute(sql, params);
			}
			else{
				// Delete new maps which were added during editing process
				sql = "DELETE FROM Routes \n" + 
					  "WHERE Routes.cityname = ? AND \n" + 
					  "Routes.is_active = 0 AND \n" + 
					  "NOT EXISTS(SELECT 1 FROM (SELECT * FROM Routes) as T1 "
					  + "WHERE T1.name = Routes.name AND T1.cityname = Routes.cityname AND T1.is_active = 1)";

				// Execute sql query using private editMap method
				editRoute(sql, params);
			}
			
			// After update was made to existing/new maps, need to delete rows which handled
			// the changes to the existing maps
			sql = "DELETE FROM Routes WHERE Routes.cityname = ? AND "
					+ "id IN (SELECT T1.id as \"id\"\n" + 
					"			 FROM (SELECT * FROM Routes) as T1, (SELECT * FROM Routes) T2 \n" + 
					"			 WHERE T1.id <> T2.id AND \n" + 
					"				   T1.name = T2.name AND\n" + 
					"                  T1.cityname = T2.cityname AND \n" + 
					"				   T1.is_active = 0)";

			// Execute sql query using private editMap method
			editRoute(sql, params);
		}
		catch (SQLException e) {
			throw e;
		}
		catch(Exception e) {
			throw new Exception("Routes' update after management response was unsuccessful");
		}
	}
	
	/**
	 * Generic query for UPDATE queries 
	 * @param sql - the UPDATE query 
	 * @param params - {@link ArrayList} of parameters to complete the requested UPDATE query
	 * @return int - number of the affected rows
	 * @throws SQLException
	 */
	private int editRoute(String sql, ArrayList<Object> params) throws SQLException {
		// Variables
		int changedRows = 0;

		try {
			// Connect to DB
			SQLController.Connect();

			// Execute sql query, get number of rows affected
			changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0)
				 throw new Exception("Operation on routes was unsuccessful.");
			}
		catch (SQLException e) {
			throw e;
		}
		catch(Exception e) {
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(null);
		}

		return changedRows;
	}
}
