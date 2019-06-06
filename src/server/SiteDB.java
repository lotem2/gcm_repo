package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import server.SQLController;
import common.*;
import entity.*;
import java.awt.Point;

public class SiteDB {
	// Variables
	private static SiteDB m_instance = null;
	
	/**
	 * Private constructor - prevent this object's creation 
	 */
	private SiteDB() {}

	/**
	 * Returns a static instance of SiteDB object
	 * @return SiteDB
	 */
	public static synchronized SiteDB getInstance() {
		if (m_instance == null) {
			m_instance = new SiteDB();
		}
		
		return m_instance;
	}

	/**
	 * Get maps's sites according to the map's description provided by the client
	 * @param params - Contains {@link Action} type and the map's description of the requested map
	 * @return {@link Message} - Contains {@link ArrayList} of sites or failure message
	 */
	public Message getSitesbyMap(ArrayList<Object> params){
		// Variables
		ArrayList<Site>   sites = new ArrayList<Site>();
		ArrayList<Object> data  = new ArrayList<Object>();
		ResultSet         rs 	= null;
		String sql              = "";

		try {
			// Connect to DB
			SQLController.Connect();
				
			// Prepare statement to get map's sites
			sql = "SELECT s.name as \"name\", s.cityname as \"cityname\", s.classification as \"classification\", "+
					 " s.description as \"description\", s.accessible as \"accessible\", " +
					 " s.visitDuration as \"visitDuration\", s.location as \"location\" " +
					 " FROM Sites s, Maps m, Bridge b" +
					 " WHERE m.mapID = b.mapID AND s.siteID AND m.description = ?";

			// Execute sql query by calling private method getSites with the requested SELECT query
			sites = getSites(sql, params);
			data.add(new Integer(0)); // set query result as success
			data.add(sites);	// adding sites' array list
	}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
		}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("Sites for the current map where not found.");
		}
		finally {
			SQLController.Disconnect(rs);
		}

		return new Message(null, data);
	}


	/**
	 * Get route's sites according to the route's description provided by the client
	 * @param params - Contains {@link Action} type and the route's description of the requested map
	 * @return {@link Message} - Contains {@link ArrayList} of sites or failure message
	 */
	public Message getSitesbyRoute(ArrayList<Object> params){
		// Variables
		ArrayList<Site>   sites = new ArrayList<Site>();
		ArrayList<Object> data  = new ArrayList<Object>();
		ResultSet         rs 	= null;
		String sql 				= "";

		try {
			// Connect to DB
			SQLController.Connect();

				// Prepare statement to get route's sites
			sql = "SELECT s.name as \"name\", s.cityname as \"cityname\", s.classification as \"classification\", "+
					" s.description as \"description\", s.accessible as \"accessible\", " +
					 "s.visitDuration as \"visitDuration\", s.location as \"location\" " +
		            "FROM Sites s, Routes r "+
		            "WHERE LOCATE(s.name, r.sites) > 0 AND r.description LIKE '%?%'";

			// Execute sql query by calling private method getSites with the requested SELECT query
			sites = getSites(sql, params);

			data.add(new Integer(0)); // set query result as success
			data.add(sites);	// adding sites' array list
		}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
			}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("Sites for the current route where not found.");
		}
		finally {
			SQLController.Disconnect(rs);
		}

		return new Message(null, data);
	}


	/**
	 * Get City's sites according to the city's description provided by the client
	 * @param params - Contains {@link Action} type and the city's description of the requested map
	 * @return {@link Message} - Contains {@link ArrayList} of sites or failure message
	 */
	public Message getSitesbyCity(ArrayList<Object> params){
		// Variables
		ArrayList<Site>   sites = new ArrayList<Site>();
		ArrayList<Object> data  = new ArrayList<Object>();
		ResultSet         rs 	= null;
		String sql 				= "";

		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare statement to get route's sites
			sql = "SELECT s.name as \"name\", s.cityname as \"cityname\", s.classification as \"classification\", "+
						 " s.description as \"description\", s.accessible as \"accessible\", " +
						 " s.visitDuration as \"visitDuration\", s.location as \"location\" " +
						 " FROM Cities c, Sites s, BridgeMSC b" +
						 " WHERE r.mapID = b.mapID AND s.siteID AND c.description = ?";

			// Execute sql query by calling private method getSites with the requested SELECT query
			sites = getSites(sql, params);

			data.add(new Integer(0)); // set query result as success
			data.add(sites);	// adding sites' array list
		}
		catch (SQLException e) {
			data.add(new Integer(1));
			data.add("There was a problem with the SQL service.");
			}
		catch(Exception e) {
			data.add(new Integer(1));
			data.add("Sites for the current city where not found.");
		}
		finally {
			SQLController.Disconnect(rs);
		}

		return new Message(null, data);
	}

	/**
	 * Generic function for SELECT queries
	 * @param sql - the SELECT query
	 * @params params - {@link ArrayList} of parameters to complete the requested SELECT query
	 * @return {@link ArrayList} - an {@link ArrayList} of type {@link Site} which satisfies the conditions
	 * @throws SQLException, Exception
	 */
	private ArrayList<Site> getSites(String sql, ArrayList<Object> params) throws SQLException, Exception {
		// Variables
		ArrayList<Site>     sites     = new ArrayList<Site>();
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

			rs.beforeFirst(); // Return cursor to the start of the first row
			
			// Go through the result set and build the site entity
			while (rs.next())
			{
				// Reads location coordinates
				String xLocation = rs.getString("location".split(",")[0]);
				String yLocation = rs.getString("location".split(",")[1]);

				Site currSite = new Site(
						rs.getString("name"),
						rs.getString("cityName"),
						Classification.valueOf(rs.getString("classification").toUpperCase()),
						rs.getString("description"),
						rs.getBoolean("accessible"),
						rs.getFloat("visitDuration"),
						new Point(Integer.parseInt(xLocation), Integer.parseInt(yLocation)));

				sites.add(currSite);
			}
		}
		catch (SQLException e) {
			throw e;
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(rs);	
		}

		return sites;
	}
	
	/**
	 * Edit a specific site's details 
	 * @param params - Contain {@link Action} site's new details
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message EditSite(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data 		  = new ArrayList<Object>();
		String 			  sql  		  = "";
		int 			  changedRows = 0;

		try {
			// Check if a new map version is currently under management approval
			if(SQLController.DoesRecordExist("Inbox","content", "status", "Approve new version", "New"))
				throw new Exception("New version is under approval, cannot save new changes.");

			// Adding parameters for the WHERE clause
			params.add(params.get(0)); params.add(params.get(5));

			// Check if a change to the requested site was already made
			if(SQLController.DoesRecordExist("Sites","name", "location","is_active", 
					params.get(0), params.get(1), false)) {
				// Prepare statement to insert new user
				sql = "UPDATE Sites SET name = ?, classification = ?, description = ?, accessible = ?, " +
							 "visitDuration = ?, location = ? " +
							 "WHERE name = ?, location = ?, is_active = ?";	
				
				// Connect to DB
				SQLController.Connect();

				// Execute sql query, get number of changed rows
				changedRows = SQLController.ExecuteUpdate(sql, params);
			}
			else {
				changedRows = AddSite(params);	// Call the AddSite method to insert the new update to Sites table
			}

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0) {
				 throw new Exception("Site was not edited successfully.");
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

		return (new Message(Action.EDIT_SITE, data));
	}

	/**
	 * Generic add site method
	 * @param params - Contain new site's details and the ADD query
	 * @return int - Indicating number of rows affected
	 * @throws Exception 
	 */
	public int AddSite(ArrayList<Object> params) throws Exception {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		int changedRows = 0;

		try {
			// Connect to DB
			SQLController.Connect();
			
			// Prepare statement to insert new site
			String sql = "INSERT INTO Sites (`name`, `classification`, `description`, `accessible`, " +
						 "`visitDuration`, `location`, `is_active`)" +
						 "VALUES (?, ?, ?, ?, ?, ?, ?) " +
						 "WHERE name = ?, location = ?;";

			// Execute sql query, get number of changed rows
			changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0) {
				 throw new Exception("Site was not added successfully.");
			}
			
			}
		catch (SQLException e) {
			throw e;
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(null);
		}

		return changedRows;
	}
	
	/**
	 * Generic add site method
	 * @param params - Contain new site's details and the ADD query
	 * @return int - Indicating number of rows affected
	 * @throws Exception 
	 */
	public int ApproveSites(ArrayList<Object> params) throws Exception {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		int changedRows = 0;

		try {
			// Connect to DB
			SQLController.Connect();
			
			// Prepare statement to insert new site
			String sql = "INSERT INTO Sites (`name`, `classification`, `description`, `accessible`, " +
						 "`visitDuration`, `location`, `is_active`)" +
						 "VALUES (?, ?, ?, ?, ?, ?, ?) " +
						 "WHERE name = ?, location = ?;";

			// Execute sql query, get number of changed rows
			changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0) {
				 throw new Exception("Site was not added successfully.");
			}
			
			}
		catch (SQLException e) {
			throw e;
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(null);
		}

		return changedRows;
	}
}
