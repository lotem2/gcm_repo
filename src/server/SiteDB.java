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
	 * Edit a specific site's details 
	 * @param params - Contain {@link Action} site's new details
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message updateSiteDetails(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data 		  = new ArrayList<Object>();
		String 			  sql  		  = "";
		int 			  changedRows = 0;

		try {
			// Check if a change to the requested site was already made
			if(SQLController.DoesRecordExist("Sites","name", "location","is_active", 
					params.get(0), params.get(5), 0)) {
				// Prepare statement to insert new user
				sql = "UPDATE Sites SET name = ?, classification = ?, description = ?, accessible = ?, " +
							 "visitDuration = ?, location = ? " +
							 "WHERE name = ?, location = ?, is_active = ?";	
				// Adding parameters for the WHERE clause
				params.add(params.get(0)); params.add(params.get(5)); params.add(0);
				
				// Edit site's details using private editSite method
				editSite(sql, params);
			}
			else {
				// Adding parameters for the WHERE clause and call AddSite 
				// to add new record with updated value of a site that already exists but record that is not displayed yet
				params.add(0); params.add(params.get(0)); params.add(params.get(5)); params.add(0);
				AddSite(params);
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
			// Check if a the requested new site was already added
			if(SQLController.DoesRecordExist("Sites","name", "location","is_active", 
					params.get(0), params.get(5), 0)) throw new Exception("Site already exists.");

			// Connect to DB
			SQLController.Connect();
			
			// Prepare statement to insert new site
			String sql = "INSERT INTO Sites (`name`, `classification`, `description`, `accessible`, " +
						 "`visitDuration`, `location`, `is_active`)" +
						 "VALUES (?, ?, ?, ?, ?, ?, ?) " +
						 "WHERE name = ?, location = ?";

			// Execute sql query, get number of changed rows
			changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0)
				 throw new Exception("Site was not added successfully.");
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
	 * Decline or approve existing sites with the new details provided until the maps' new version's publish
	 * @param params - {@link ArrayList} containing the new sites' id
	 * @throws Exception 
	 */
	public void approveEditedSites(ArrayList<Object> params) throws Exception {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		String sql;

		try {
			// If version is approved need to update exisiting sites with new details
			if(params.get(0) == "Approve") {
				// Prepare statement to insert new site
				sql = "UPDATE  Sites s1\n" + 
						"        CROSS JOIN Sites s2\n" + 
						"SET     s1.name = s2.name,\n" + 
						"        s1.cityname = s2.cityname,\n" + 
						"        s1.classification = s2.classification,\n" + 
						"        s1.description = s2.description,\n" + 
						"        s1.accessible = s2.accessible\n" + 
						"WHERE   s1.name = s2.name AND\n" + 
						"        s1.location = s2.location AND\n" + 
						"        s1.is_active = 1 AND\n" + 
						"        s2.is_active = 0 AND " +
					    "		 s1.id IN (";
				
				// Add question marks for the site's id
				for (int i = 0; i < params.size(); i++)
					sql += "?, ";
	
				// Execute query using private editSites method
				editSite(sql.substring(0, sql.length()-1) + ")", (ArrayList<Object>)params.subList(1, params.size()));
			}

			// Create sql DELETE query to delete the rows which used for future update of sites
			sql = "DELETE FROM Sites WHERE siteID IN " +
				  "(SELECT T1.siteID as \"siteID\"\n" + 
				   "FROM (SELECT * FROM Sites) as T1, (SELECT * FROM Sites) T2\n" + 
				   " WHERE T1.siteID <> T2.siteID AND\n" + 
					"      T1.name = T2.name AND \n" + 
					"      T1.location = T2.location AND\n" + 
					"      T1.is_active = 0 AND\n" + 
					"      T2.siteID IN (";
			
			// Add question marks for the site's id
			for (int i = 0; i < params.size(); i++)
				sql += "?, ";

			// Execute query using private editSites method
			editSite(sql.substring(0, sql.length()-1) + "))", (ArrayList<Object>)params.subList(1, params.size()));			
			}
		catch (SQLException e) {
			throw e;
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	/**
	 * Decline or approve new sites according to the manager's response
	 * @param params - {@link ArrayList} containing the new sites' id
	 * @throws Exception 
	 */
	public void approveNewSites(ArrayList<Object> params) throws Exception {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		String sql;

		try {
			if(params.get(0).toString() == "Approve") {
				// Prepare statement to insert new site
				sql = "UPDATE Sites SET is_active = 1 WHERE siteID IN (";
			}
			else {
				sql = "DELETE FROM WHERE siteID IN (";
			}

			// Add question marks for the site's id
			for (int i = 0; i < params.size(); i++)
				sql += "?, ";

			// Execute query using private editSites method
			editSite(sql.substring(0, sql.length()-1) + ")", (ArrayList<Object>)params.subList(1, params.size()));
			}
		catch (SQLException e) {
			throw e;
		}
		catch(Exception e) {
			throw e;
		}
	}

	/**
	 * Generic function for SELECT queries
	 * @param sql - the SELECT query
	 * @param params - {@link ArrayList} of parameters to complete the requested SELECT query
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
	 * Generic query for UPDATE queries 
	 * @param sql - the UPDATE query 
	 * @param params - {@link ArrayList} of parameters to complete the requested UPDATE query
	 * @throws SQLException, Exception
	 */
	private void editSite(String sql, ArrayList<Object> params) throws SQLException, Exception {
		try {
			// Connect to DB
			SQLController.Connect();

			// Execute sql query, get number of rows affected
			int changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0)
				 throw new Exception("Operation on sites was unsuccessful.");
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
	}
}
