package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import server.SQLController;
import common.*;
import entity.*;
import java.awt.geom.Point2D;

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
	 * @param params - contain permission of user who requested the information and the requested map id
	 * @return {@link Message} - Contains {@link ArrayList} of sites or failure message
	 */
	public Message getSitesbyMap(ArrayList<Object> params){
		// Variables
		Message 		  msg 	= null;
		ArrayList<Site>   sites = new ArrayList<Site>();
		ArrayList<Object> data  = new ArrayList<Object>();
		String sql              = "";

		try {
			// Prepare sql statement according to the permission of the requesting user
			if(params.get(0).toString().equals(Permission.CLIENT.toString())) {
				// Prepare statement to get map's sites
				sql = "SELECT s.name as \"name\", s.cityname as \"cityname\", s.classification as \"classification\", "+
						 " s.description as \"description\", s.accessible as \"accessible\", " +
						 " s.visitDuration as \"visitDuration\", s.location as \"location\" " +
						 " FROM Sites s, Maps m, BridgeMSC b" +
						 " WHERE b.mapID = ? AND b.is_active = 1 AND m.mapID = b.mapID AND s.siteID = b.siteID";
			}
			else {
				sql = "SELECT s.name as \"name\", s.cityname as \"cityname\", s.classification as \"classification\",  \r\n" + 
						"						s.description as \"description\", s.accessible as \"accessible\", \r\n" + 
						"						 s.visitDuration as \"visitDuration\", s.location as \"location\"\r\n" + 
						"						FROM Sites s, (SELECT BridgeMSC.siteID as \"id\", BridgeMSC.is_active as \"is_active\"\r\n" + 
						"						 				FROM BridgeMSC \r\n" + 
						"										WHERE BridgeMSC.mapID = ? and BridgeMSC.to_delete <> 1) as maps_sites \r\n" + 
						"						WHERE s.siteID IN ((SELECT b.siteID \r\n" + 
						"						                   FROM   Sites b, BridgeMSC c \r\n" + 
						"						                   WHERE  b.siteID = maps_sites.id AND maps_sites.is_active = 0 AND\r\n" + 
						"                                            NOT EXISTS (SELECT 1 FROM Sites s\r\n" + 
						"						 								   WHERE s.name = b.name AND s.location = b.location AND s.is_active = 1)\r\n" + 
						"						 UNION \r\n" + 
						"						               SELECT i.siteID as \"id\"\r\n" + 
						"										 FROM Sites i \r\n" + 
						"										 WHERE EXISTS (SELECT 1 FROM Sites s\r\n" + 
						"						 								   WHERE s.name = i.name AND s.location = i.location AND i.is_active = 0 AND s.is_active = 1 AND maps_sites.id = s.siteID))\r\n" + 
						"						UNION  \r\n" + 
						"						                SELECT a.siteID as \"id2\"\r\n" + 
						"										 FROM Sites a \r\n" + 
						"										 WHERE NOT EXISTS (SELECT 1 FROM Sites s\r\n" + 
						"						 								   WHERE s.name = a.name AND s.location = a.location AND s.is_active = 0)\r\n" + 
						"						 								   AND maps_sites.id = a.siteID)";
			}
			// Exclude the permission type for the query
			data = new ArrayList<Object>(params.subList(1, params.size()));

			// Execute sql query by calling private method getSites with the requested SELECT query
			sites = getSites(sql, data);
			msg = new Message(null, new Integer(0), sites);
		}
		catch (SQLException e) {
			msg = new Message(null, new Integer(1), new String("There was a problem with the SQL service."));
		}
		catch(Exception e) {
			msg = new Message(null, new Integer(1), new String("Sites for the current map where not found."));
		}

		return msg;
	}


	/**
	 * Get route's sites according to the route's description provided by the client
	 * @param params - contain permission of user who requested the information and the requested route id
	 * @return {@link Message} - Contains {@link ArrayList} of sites or failure message
	 */
	public Message getSitesbyRoute(ArrayList<Object> params){
		// Variables
		ArrayList<Site>   sites = new ArrayList<Site>();
		ArrayList<Object> data  = new ArrayList<Object>();
		String sql 				= "";
		Message 		  msg 	= null;

		try {
			if(params.get(0).toString().equals(Permission.CLIENT.toString()))
				// Prepare statement to get route's sites
				sql = "SELECT s.name as \"name\", s.cityname as \"cityname\", s.classification as \"classification\", "+
						" s.description as \"description\", s.accessible as \"accessible\", " +
						 "s.visitDuration as \"visitDuration\", s.location as \"location\" " +
			            "FROM Sites s, Routes r "+
			            "WHERE LOCATE(s.name, r.sites) > 0 AND r.id = ? AND r.is_active = 1";
			else
				sql = "SELECT s.name as \"name\", s.cityname as \"cityname\", s.classification as \"classification\", \n" + 
						"s.description as \"description\", s.accessible as \"accessible\", \n" + 
						"s.visitDuration as \"visitDuration\", s.location as \"location\"" + 
						"FROM Sites s, "
						+ "(SELECT st.siteID as \"rid\" FROM Sites st, Routes r WHERE LOCATE(st.name, r.sites) > 0 AND r.id = ?) as route_site\r\n" + 
						"WHERE s.siteID IN \r\n" + 
						"	((SELECT b.siteID \r\n" + 
						"	FROM Sites b \r\n" + 
						"    WHERE  b.siteID = route_site.rid AND b.is_active = 0 AND "
						+ "NOT EXISTS(SELECT 1 from Sites s WHERE s.name = b.name AND s.location = b.location AND s.is_active = 1)) \r\n" + 
						"     \r\n" + 
						"     UNION  \r\n" + 
						"     \r\n" + 
						"     (SELECT s.siteID\r\n" + 
						"     FROM Sites s\r\n" + 
						"     JOIN Sites as s1 ON s.name = s1.name AND\r\n" + 
						"     (s.location = s1.location AND s.is_active = 0 AND s1.is_active = 1) WHERE s1.siteID = route_site.rid)\r\n" + 
						"     \r\n" + 
						"     UNION\r\n" + 
						"     \r\n" + 
						"     (SELECT a.siteID as \"id2\" FROM Sites a\r\n" + 
						"WHERE NOT EXISTS (SELECT 1 from Sites s WHERE s.name = a.name AND s.location = a.location AND s.is_active = 0) AND route_site.rid = a.siteID))";

			// Remove user's permission from parameters
			data = new ArrayList<Object>(params.subList(1, params.size()));

			// Execute sql query by calling private method getSites with the requested SELECT query
			sites = getSites(sql, data);
			msg = new Message(null, new Integer(0), sites);
		}
		catch (SQLException e) {
			msg = new Message(null, new Integer(1), new String("There was a problem with the SQL service."));
		}
		catch(Exception e) {
			msg = new Message(null, new Integer(1), new String("Sites for the current route where not found."));
		}

		return msg;
	}


	/**
	 * Get City's sites according to the city's name provided by the client
	 * @param params - contain permission of user who requested the information and the requested city name
	 * @return {@link Message} - Contains {@link ArrayList} of sites or failure message
	 */
	public Message getSitesbyCity(ArrayList<Object> params){
		// Variables
		ArrayList<Site>   sites = new ArrayList<Site>();
		ArrayList<Object> data  = new ArrayList<Object>();
		String sql 				= "";
		Message msg				= null;

		try {
			// Prepare statement according to the permission
			if(params.get(0).toString().equals(Permission.CLIENT.toString()))
				// Prepare statement to get route's sites
				sql = "SELECT * FROM Sites WHERE cityname = ? AND is_active = 1";
			else
				sql = "SELECT * \n" + 
						"FROM Sites s \n" + 
						"WHERE s.cityname = ? AND s.siteID IN \n" + 
						"((SELECT b.siteID \n" + 
						" FROM Sites b \n" + 
						" WHERE b.is_active = 0) UNION \n" + 
						" (SELECT s.siteID \n" + 
						"  FROM Sites s JOIN Sites as s1 ON s.name = s1.name AND \n" + 
						"(s.location = s1.location AND s.is_active = 0 AND s1.is_active = 1) \n" + 
						"WHERE s1.cityname = s.cityname) UNION \n" + 
						"(SELECT a.siteID \n" + 
						"FROM Sites a \n" + 
						"WHERE NOT EXISTS \n" + 
						"(SELECT 1 FROM Sites s WHERE \n" + 
						"(s.name = a.name AND s.location = a.location AND s.is_active = 0) AND a.cityname = s.cityname)))";

			// Remove user's permission from parameters
			data = new ArrayList<Object>(params.subList(1, params.size()));

			// Execute sql query by calling private method getSites with the requested SELECT query
			sites = getSites(sql, data);
			msg = new Message(null, new Integer(0), sites);
		}
		catch (SQLException e) {
			msg = new Message(null, new Integer(1), new String("There was a problem with the SQL service."));
		}
		catch(Exception e) {
			msg = new Message(null, new Integer(1), new String("Sites for the current city where not found."));
		}

		return msg;
	}
	
	/**
	 * Edit a specific site's details 
	 * @param params - Contain site's new details
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message updateSiteDetails(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data 		  = new ArrayList<Object>(params);
		String 			  sql  		  = "";
		Message			  msg		  = null;

		try {
			// Check if a new map version is currently under management approval
			if(SQLController.DoesRecordExist("Inbox","content", "status", 
					"Approve " + data.get(1).toString() + "'s new version", "New"))
				throw new Exception("New version is under approval, cannot save new changes.");
			
			// Check if a change to the requested site was already made
			if(SQLController.DoesRecordExist("Sites","name", "location","is_active", 
					data.get(0), data.get(6), 0)) {
				// Prepare statement to insert new site
				sql = "UPDATE Sites SET name = ?, cityname = ?, classification = ?, description = ?, `accessible` = ?, " +
							 "visitDuration = ?, location = ? " +
							 "WHERE name = ? AND location = ? AND is_active = 0";	

				// Adding parameters for the WHERE clause
				data.add(data.get(0)); data.add(data.get(6));

				// Edit site's details using private editSite method
				if (editSite(sql, data) == 0) throw new Exception("Update of site was unsuccessful");
			}
			else {
				// Add new record with updated value of a site that already exists but record that is not displayed yet
				AddSite(data);
			}

			// Add 0 to indicate success
			msg = new Message(Action.EDIT_SITE, new Integer(0));

			}
		catch (SQLException e) {
			msg = new Message(Action.EDIT_SITE, new Integer(1), "There was a problem with the SQL service.");
		}
		catch(Exception e) {
			msg = new Message(Action.EDIT_SITE, new Integer(1), e.getMessage());
		}

		return msg;
	}

	/**
	 * Generic add site method
	 * @param params - Contains new site's details
	 * @return int - Indicating number of rows affected
	 * @throws Exception - an exception has occurred 
	 */
	public int AddSite(ArrayList<Object> params) throws Exception {
		// Variables
		int changedRows = 0;

		try {
			// Check if a the requested new site was already added
			if(SQLController.DoesRecordExist("Sites","name", "location","is_active", 
					params.get(0), params.get(6), 0)) throw new Exception("Site already exists.");

			// Connect to DB
			SQLController.Connect();
			
			// Prepare statement to insert new site
			String sql = "INSERT INTO Sites (`name`, `cityname`, `classification`, `description`, `accessible`, " +
						 "`visitDuration`, `location`, `is_active`)" +
						 "VALUES (?, ?, ?, ?, ?, ?, ?, 0)";

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
	 * @throws Exception - an exception has occurred
	 */
	public void approveEditedSites(ArrayList<Object> params) throws Exception {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		String sql;

		try {
			// If version is approved need to update exisiting sites with new details
			if(params.get(0).toString().equals("APPROVED")) {
				// Prepare statement to insert new site
				sql = "UPDATE  Sites s1\n" + 
						"        CROSS JOIN Sites s2\n" + 
						"SET     s1.name = s2.name,\n" + 
						"        s1.cityname = s2.cityname,\n" + 
						"        s1.classification = s2.classification,\n" + 
						"        s1.description = s2.description,\n" + 
						"        s1.accessible = s2.accessible \n" + 
						"WHERE   s1.name = s2.name AND\n" + 
						"        s1.location = s2.location AND\n" + 
						"        s1.is_active = 1 AND\n" + 
						"        s2.is_active = 0 AND " +
					    "		 s1.siteID IN (";
				
				// Add question marks for the site's id
				for (int i = 0; i < params.size() - 1; i++)
					sql += "?, ";
	
				// Execute query using private editSites method
				ArrayList<Object> input = new ArrayList<Object>(params.subList(1, params.size()));
				editSite(sql.substring(0, sql.length() - 2) + ")", input);
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
			for (int i = 0; i < params.size() - 1; i++)
				sql += "?, ";

			// Execute query using private editSites method
			ArrayList<Object> input = new ArrayList<Object>(params.subList(1, params.size()));
			editSite(sql.substring(0, sql.length() - 2) + "))", input);			
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
	 * @throws Exception - an exception has occurred
	 */
	public void approveNewSites(ArrayList<Object> params) throws Exception {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		String sql;

		try {
			if(params.get(0).toString().equals("APPROVED")) {
				// Prepare statement to insert new site
				sql = "UPDATE Sites SET is_active = 1 WHERE siteID IN (";
			}
			else {
				sql = "DELETE FROM Sites WHERE siteID IN (";
			}

			// Add question marks for the site's id
			for (int i = 0; i < params.size() - 1; i++)
				sql += "?, ";

			// Execute query using private editSites method
			ArrayList<Object> input = new ArrayList<Object>(params.subList(1, params.size()));
			editSite(sql.substring(0, sql.length() - 2) + ")", input);
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
				String[] Location = rs.getString("location").split(",");
				Point2D.Double location = new Point2D.Double(Double.parseDouble(Location[0]), 
						Double.parseDouble(Location[1]));

				Site currSite = new Site(
						rs.getString("name"),
						rs.getString("cityName"),
						Classification.valueOf(rs.getString("classification").toUpperCase()),
						rs.getString("description"),
						rs.getBoolean("accessible"),
						rs.getFloat("visitDuration"),
						location);

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
	 * @return int - number of affected rows
	 * @throws SQLException
	 */
	private int editSite(String sql, ArrayList<Object> params) throws SQLException {
		// Variables
		int changedRows = 0;

		try {
			// Connect to DB
			SQLController.Connect();

			// Execute sql query, get number of rows affected
			changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0)
				 throw new Exception("Operation on sites was unsuccessful.");
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
