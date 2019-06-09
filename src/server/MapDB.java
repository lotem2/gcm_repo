package server;

import java.awt.Point;
import java.io.File;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import common.*;
import entity.*;

public class MapDB {
	// Variables
	private static MapDB m_instance = null;
		
	/**
	 * Private constructor - prevent this object's creation 
	 */
	private MapDB() {}
		
	/**
	 * Returns a static instance of MapDB object
	 * @return MapDB
	 */
	public static synchronized MapDB getInstance() {
		if (m_instance == null) {
			m_instance = new MapDB();
		}

		return m_instance;
	}

	/**
	 * Searching for one or more maps according to search parameters provided by the client
	 * @param params - Contain {@link Action} type and the search parameters: 
	 * City name/Site name/Site or City description
	 * @return {@link Message} - Contain {@link ArrayList} of maps if found in database, else failure message
	 */
	public Message Search(ArrayList<Object> params) {
		// Variables
		ResultSet 				 rs 		  = null;
		ArrayList<Object> 		 data 	  	  = new ArrayList<Object>();
		HashMap<Integer, String> searchResult = new HashMap<Integer, String>();
					
		try {
			// Connect to DB
			SQLController.Connect();

			// Get sql query and search parameter
			String[] searchBy = getSearchSQL(params).split(":");
			
			// Execute sql query (located in index 1 of searchBy array), get results
			rs = SQLController.ExecuteQuery(searchBy[1], params);

			if(!rs.next())	// Search provided empty result
				throw new Exception("Could not find a map according to search parameters");

			rs.beforeFirst();	// Moves cursor to the start of the result set
			data.add(new Integer(0)); // Indicate action's success

			// Check if search was by city or site
			if (searchBy[0].equals("City")) {
				Integer routes = new Integer(0); // Counter for number of routes

				// Reads data 
				while (rs.next()) {
					// Insert into the HashMap the map's id and description
					searchResult.put(rs.getInt("mapID"), 
							rs.getString("mapDescription") + "," + rs.getInt("sitesCount"));
				}

				// Prepare parameters to to the query 
				if(params.size() == 1) params.add(params.get(0));

				// If result of the query was successful get the number of routes 
				Object result = ((Message)RouteDB.getInstance().getRoutesCount(params)).getData().get(1);
				if(!(result instanceof String)) 
					routes = (Integer)result;

				// Insert the results into the data array list
				data.add(searchResult); data.add(routes);				
			}
			else {	// Search was by Site
				// Reads data 
				while (rs.next()) {
					// Insert into the HashMap the map's id and description
					searchResult.put(rs.getInt("mapID"), 
							rs.getString("mapDescription") + "," + rs.getString("cityname"));
				}

				// Insert the results into the data array list
				data.add(searchResult);
			}

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

		return new Message(Action.SEARCH, data);
	}

	/**
	 * Add new map to database
	 * @param params - Contain new map's details
	 * @return {@link Message} - Indicating success/failure with corresponding message 
	 */
	public Message AddNewMap(ArrayList<Object> params){
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();

		try {
			// Check if the collections of maps of the city the new map belongs is under approval
			if(SQLController.DoesRecordExist("Inbox","content", "status", 
					"Approve " + params.get(1).toString() + " new version", "New"))
				throw new Exception("New version is under approval, cannot save new changes.");
			
			// Check if the map to add is already in the database
			if(SQLController.DoesRecordExist("Maps", "mapname", "cityname", "description", 
					params.get(0), params.get(1), params.get(2)))
				throw new Exception("Map already exists.");

			// Prepare statement to insert new map
			String sql = "INSERT INTO Maps (`mapname`, `cityname`, `description`, `url`)" +
						 " VALUES (?, ?, ?, ?)";

			// Add the conversion of the map's image url to byte array to params object
			params.set(3, setImage(params.get(3).toString()));

			// Insert new map using private editMap method
			editMap(sql, params);

			// Create data to match the success pattern
			data.add(new Integer(0)); data.add(new String("Map was added successfully."));
		}
		catch (SQLException e) {
			data.add(new Integer(1)); data.add(new String("There was a problem with the SQL service."));
		}
		catch(Exception e) {
			data.add(new Integer(1)); data.add("Map was not added successfully");
		}

		return new Message(null, data);
	}
	
	/**
	 * Edit map details in database
	 * @param params - Contains map's details to update (map id, map name, city name, description, url, is_active)
	 * @return {@link Message} - Indicating success/failure with corresponding message 
	 */
	public Message editMapDetails(ArrayList<Object> params){
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		String 			  sql  = "";
		ArrayList<Object> map_params;

		try {
			// Check if the map to add is already in the database
			if(SQLController.DoesRecordExist("Maps", "mapname", "cityname", "is_active", 
					params.get(1), params.get(2), 0)) {
				// Prepare statement to update map
				sql = "UPDATE Maps SET description = ?, url = ? WHERE mapID = ?";

				// prepare parameters to sql
				map_params = new ArrayList<Object>(params.subList(3, 5)); 
				// Add the conversion of the map's image url to byte array to params object
				map_params.set(1, setImage(params.get(4).toString()));
				map_params.add(params.get(0));
			}
			else {
				// Prepare statement to insert new record with updated details
				sql = "INSERT INTO Maps (`mapname`, cityname`, `description`, `url`) VALUES"
						+ "(?, ?, ?, ?)";
				
				// Convert the map's image path to byte array
				params.set(4, setImage(params.get(4).toString()));

				// prepare parameters to sql
				map_params = new ArrayList<Object>(params.subList(1, 4)); 
				// Add the conversion of the map's image url to byte array to params object
				map_params.add(setImage(params.get(4).toString()));
			}

			// Update map details using private editMap method
			editMap(sql, map_params);

			// Create data to match the success pattern
			data.add(new Integer(0)); data.add(new String("Map was added successfully."));
		}
		catch (SQLException e) {
			data.add(new Integer(1)); data.add(new String("There was a problem with the SQL service."));
		}
		catch(Exception e) {
			data.add(new Integer(1)); data.add("Map was not added successfully");
		}

		return new Message(null, data);
	}
	
	/**
	 * Add a new site to the requested map
	 * @param params - Contains requested map entity that we want to connect the site and new site's details 
	 * OR requested map entity, and site name
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message AddSiteToMap(ArrayList<Object> params){
		// Variables
		ArrayList<Object> data 		  = new ArrayList<Object>();
		String 			  sql 		  = "";
		int 			  id 		  = -1;
		String			  site		  = params.get(1).toString();
		Map 			  currentMap  = (Map)params.get(0);

		try {
			// Check if a new map version is currently under management approval
			if(SQLController.DoesRecordExist("Inbox","content", "status", 
					"Approve " + currentMap.getCityName() + " new version", "New"))
				throw new Exception("New version is under approval, cannot save new changes.");

			// Check if number of parameters is higher than 3 - indicating a new site is been added
			if (params.size() > 3) {
				ArrayList<Object> sitedetails = new ArrayList<Object>(params.subList(1, params.size()));

				// Adding name, location to the parameters for the WHERE clause
				sitedetails.add(sitedetails.get(0)); sitedetails.add(sitedetails.get(6));
				
				// Throwing exception in case the site's addition to Sites table was unsuccessful
				if (SiteDB.getInstance().AddSite(sitedetails) == 0) throw new Exception("Unable to add the site.");
			}

			// Get the displayed map's id if current map is under edit - to make sure the map who is
			// getting updated every time will display the site
			if((!currentMap.getIsActive()) && (!
					SQLController.DoesRecordExist("Maps","mapname", "cityname", "is_active", 
					currentMap.getName(), currentMap.getCityName(), 1))) {
				ArrayList<Object> tosql = new ArrayList<Object>();
				tosql.add(currentMap.getName()); tosql.add(currentMap.getCityName());
				id = getActiveMap(params);
			}
			else id = currentMap.getID();

			// Prepare insert statement
			sql = "INSERT INTO BridgeMSC (`mapID`, `siteID`, `cityID`) "
					+ "VALUES (?, "
					+ 		  "(SELECT id FROM Cities WHERE name = ?), "
					+ 		  "(SELECT id FROM Sites WHERE name = ? LIMIT 1))";
			
			// Get the correct parameters for the map-site relation query
			params.clear(); params.add(id); params.add(currentMap.getCityName()); params.add(site);

			// Insert the relation between the site and map using private editMap method
			editMap(sql, params);
			data.add(new Integer(0), "Site added successfuly to map.");
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
	 * Get maps list according to the requested city
	 * @param params - Contain user's permission and requested city name
	 * @return {@link Message} - Contain {@link ArrayList} of type {@link Map} if retrieval succeeded, 
	 * else failure message
	 */
	public Message getMapsByCity(ArrayList<Object> params){
		// Variables
		ArrayList<Map>  maps 	 = new ArrayList<Map>();
		ArrayList<Site> sites;
		Message 		replyMsg = null;
		ResultSet 		rs 		 = null;
		String sql				 = "";

		try {
			// Connect to DB
			SQLController.Connect();
			
			// Check permission of the requested user
			if(params.get(0).toString().equals(Permission.CLIENT.toString()))
				sql = "SELECT * FROM Maps WHERE cityname = ? AND is_active = 1"; // Prepare SELECT query
			else
				sql = "SELECT *" + 
					  "FROM Maps m1 \n" + 
					  "WHERE m1.cityname = ? AND m1.mapID IN \r\n" + 
					  "(SELECT a.mapID as \"id\" FROM Maps a \r\n" + 
					  "WHERE m1.mapname = a.mapname AND m1.cityname = a.cityname AND \r\n" + 
					  "NOT EXISTS"
					  + "(SELECT 1 FROM Maps b WHERE b.mapname = a.mapname AND b.cityname = a.cityname AND "
					  + "b.is_active = 0 AND a.is_active = 1))";

			ArrayList<Object> cityparam = new ArrayList<Object>(params.subList(1, params.size()));

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, cityparam);

			// check if query succeeded
			if(!rs.next()) {
				throw new Exception("No map was found.");
			}

			rs.beforeFirst(); // Return cursor to the start of the first row
			
			// Reads data
			while (rs.next())
			{
				int id = rs.getInt("mapID");
				String mapname = rs.getString("mapname");
				String description = rs.getString("description");
				String cityname = rs.getString("cityname");
				byte[] image = rs.getBytes("url");
				boolean is_active = rs.getBoolean("is_active");

				// Clear lists of parameters for the next queries and add the desired parameter
				cityparam.clear(); cityparam.add(params.get(0)); cityparam.add(id);

				// Get map's list of sites
				Object mapSites = SiteDB.getInstance().getSitesbyMap(cityparam);

				// Set the list of sites as null in case of a prbolem with the database/no sites for this map
				sites = ((Message)mapSites).getData().get(1) instanceof String ? null : 
					(ArrayList<Site>)((Message)mapSites).getData().get(1);

				// Add current instance of Map to the array list
				maps.add(new Map(id, mapname, description, cityname, sites, image, is_active));
			}

			replyMsg = new Message(null, new Integer(0), maps);	// Add content of the array list to the message
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
	 * Get the amount of maps for a requested city
	 * @param params - Contains city name
	 * @return {@link Message} - Contains {@link ArrayList} of amount of {@link Map}s to a requested city,
	 * else failure message
	 */
	public Message getMapsCount(ArrayList<Object> params){
		// Variables
		HashMap<String,Integer> data      = new HashMap<String,Integer>();
		ResultSet 		        rs        = null;
		Message 		        replyMsg  = null;

		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare SELECT query
			String sql = "SELECT cityname, COUNT(mapID) as 'numOfMaps' FROM `Maps` where cityname = ?";

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			// check if query succeeded
			if(!rs.next()) {
				throw new Exception("No map was found.");
			}

			rs.beforeFirst(); // Return cursor to the start of the first row

			// Reads data
			while (rs.next())
			{
				data.put(rs.getString("cityname"), rs.getInt("numOfMaps"));
			}

			replyMsg = new Message(null, data);	// Add content of the array list to the message
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
	 * Update details in database of the current map's new version, set current version as active
	 * @param params - response - approve/decline of publishing new version, map id
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 * @throws Exception 
	 */
	public void publishNewVersion(ArrayList<Object> params) throws Exception{
		// Variables
		ArrayList<Object> map_sities;

		try {
			// Add is_active = false to get the new sites add to database and to the map to display them on this version
			// Get the id of sites that belong to the map using private getMapSitesID method
			params.add(0); 
			ArrayList<Object> input = new ArrayList<Object>(params.subList(1, params.size()));
			ArrayList<Object> sitesID = getMapSitesID(input);

			// Add response of management to publishing new version and the map id
			map_sities = new ArrayList<Object>(params.subList(0, params.size()));

			// Delete sites related to the map according to the response, using map id given
			updateSitesOfMap(map_sities);

			map_sities.remove(1); // remove map id 
			
			// Enter each site into the array list for query
			for (Object site : sitesID)
				map_sities.add(site);

			SiteDB.getInstance().approveNewSites(map_sities); // Update new sites added according to the response

			// Add is_active = true to get the existing sites already in map to display their new details in map 
			input.remove(1); input.add(1); map_sities.clear(); map_sities.add(params.get(0));
			sitesID = getMapSitesID(input);

			// Enter each site into the array list for query
			for (Object site : sitesID)
				map_sities.add(site);

			// Approve editing of sites for this new version
			SiteDB.getInstance().approveEditedSites(map_sities);
		}
		catch (SQLException e) {
			throw e;
		}
		catch(Exception e) {
			throw e;
		}
	}

	/**
	 * Delete sites from map - set as to delete in the future when new version is approved or 
	 * delete sites when new version is approved
	 * @param params - contains map id and site id in case manager decides to remove the site from the map,
	 * or manager's response to publishing city's maps collection and map id of current city's map
	 * @throws Exception 
	 */
	public void updateSitesOfMap(ArrayList<Object> params) throws Exception{
		// Variables
		String sql = "";
		
		try {
			// Build query according to the removal action - after new version's publishing or on delete
			// from site from map - management's action
			if(params.get(0) instanceof Integer) {
				sql = "UPDATE BridgeMSC SET to_delete = 1 " +
					  "WHERE mapID = ? AND siteID = ? AND is_active = ?";
				params.add(1);

				editMap(sql, params); // Execute sql query using private editMap method
			}
			// Check if the method was called as from the publishNewVersion method
			else{
				// Get map id parameter for the next queries
				params = new ArrayList<Object>(params.subList(1, params.size()));

				// If new version was approved - update is_active of new sites to true
				if(params.get(0).toString() == "Approve") {
					// Add another query after the update to delete the sites in map where to_delete = true
					sql = " DELETE FROM BridgeMSC " + 
							" WHERE mapID = ? AND to_delete = 1 AND is_active = 1";

					editMap(sql, params); // Execute sql query using private editMap method

					// Update new maps to display the sites
					sql = " UPDATE BridgeMSC SET is_active = 1 WHERE mapID = ? AND is_active = 0";

					editMap(sql, params); // Execute sql query using private editMap method
				}
				else{
					// The update was declined - delete the sites which where related to a new map
					// or added to an existing map
					sql = "DELETE FROM BridgeMSC WHERE mapID = ? AND is_active = 0";
					
					editMap(sql, params); // Execute sql query using private editMap method
					
					// Update sites that where set to be deleted to be displayed after the new version was declined
					sql =  " UPDATE BridgeMSC SET to_delete = 0 WHERE mapID = ? AND is_active = 1";

					editMap(sql, params); // Execute sql query using private editMap method
				}
			}
		}
		catch (SQLException e) {
			throw e;
		}
		catch(Exception e) {
			throw new Exception("Editing the current map was unsuccessful");
		}
	}

	/**
	 * Responsible to update Maps table according to the management response
	 * @param params - Contains management response - approve/decline and city name
	 * @throws Exception 
	 */
	public void updateMapDetailsAfterApproval(ArrayList<Object> params) throws Exception{
		// Variables
		String sql 		= "";
		String response = params.get(0).toString();

		// Get the needed parameters for the queries ahead
		params = new ArrayList<Object>(params.subList(1, params.size()));

		try {
			// If new version was approved - update details of existing maps to the changes made
			if(response.equals("Approve")) {
				// Update exisiting map's details to display the changes
				sql = "UPDATE  Maps m1 \n" + 
						"CROSS JOIN Maps m2 \n" + 
						"SET     m1.mapname = m2.mapname, \n" + 
						"		m1.cityname = m2.cityname, \n" + 
						"	 	m1.description = m2.description, \n" + 
						"		m1.url = m2.url \n" + 
						"WHERE   m1.mapname = m2.mapname AND \n" + 
						"		m1.cityname = m2.cityname AND \n" + 
						"		m1.is_active = 1 AND \n" + 
						"		m2.is_active = 0 AND \n" + 
						"        m1.cityname = ?";

				// Execute sql query using private editMap method
				editMap(sql, params);

				// Update new maps to be displayed to users
				sql = "UPDATE Maps SET Maps.is_active = 1 \n" + 
					  "WHERE Maps.cityname = ? AND \n" + 
					  "Maps.is_active = 0 AND \n" + 
					  "NOT EXISTS(SELECT 1 FROM (SELECT * FROM Maps) as T1 "
					  + "WHERE T1.mapname = Maps.mapname AND T1.cityname = Maps.cityname AND T1.is_active = 1)";

				// Execute sql query using private editMap method
				editMap(sql, params);
			}
			else{
				// Delete new maps which were added during editing process
				sql = "DELETE FROM Maps \n" + 
					  "WHERE Maps.cityname = ? AND \n" + 
					  "Maps.is_active = 0 AND \n" + 
					  "NOT EXISTS(SELECT 1 FROM (SELECT * FROM Maps) as T1 "
					  + "WHERE T1.mapname = Maps.mapname AND T1.cityname = Maps.cityname AND T1.is_active = 1)";

				// Execute sql query using private editMap method
				editMap(sql, params);
			}
			
			// After update was made to existing/new maps, need to delete rows which handled
			// the changes to the existing maps
			sql = "DELETE FROM Maps WHERE Maps.cityname = ? AND "
					+ "mapID IN (SELECT T1.mapID as \"mapID\"\n" + 
					"			 FROM (SELECT * FROM Maps) as T1, (SELECT * FROM Maps) T2 \n" + 
					"			 WHERE T1.mapID <> T2.mapID AND \r\n" + 
					"				   T1.mapname = T2.mapname AND\r\n" + 
					"                  T1.cityname = T2.cityname AND\r\n" + 
					"				   T1.is_active = 0)";

			// Execute sql query using private editMap method
			editMap(sql, params);
		}
		catch (SQLException e) {
			throw e;
		}
		catch(Exception e) {
			throw new Exception("Maps' update after management response was unsuccessful");
		}
	}
	
	/**
	 * Providing the sql search query according to the search criteria
	 * @param params - {@link ArrayList} of the search parameters
	 * @return String - the sql query with indicate if search was by site/city
	 */
	private String getSearchSQL(ArrayList<Object> params) {
		// Variables
		boolean city_name_exist   = !(params.get(0) == null);
		boolean site_name_exist   = !(params.get(1) == null);
		boolean description_exist = !(params.get(2) == null);
		String sql 				  = "";

		if (description_exist) {
			// Check whether the search was by site or city description
			if (SQLController.DoesRecordExist("Sites", "description", "%" + params.get(2).toString() + "%"))
				sql = "Site:" + "s.description LIKE ?";
			else
				sql = "City:" + "c.description LIKE ?";

			// Adding the description the special characters for the LIKE section in the query
			params.set(2, "%" + params.get(2).toString() + "%");
		}

		// Generate search sql according to the search parameters
		if (city_name_exist && site_name_exist && description_exist) {
			sql = "Site:" + "c.name = ? AND s.name = ? AND (c.description LIKE ? OR s.description LIKE ?)";

			params.add(params.get(2));	// Adding fourth parameter for the description's double input and

		}
		else if(city_name_exist && site_name_exist) {
			sql = "Site:" + "c.name = ? AND s.name = ?";
		}
		else if(city_name_exist && description_exist) {
			sql = sql.split(":")[0] + ":c.name = ? AND " + sql.split(":")[1];
		}
		else if(site_name_exist && description_exist) {
			sql = sql.split(":")[0] + ":s.name = ? AND " + sql.split(":")[1];
		}
		else if(city_name_exist) {
			sql = "City:" + "c.name = ?";
		}
		else if(site_name_exist) {
			sql = "Site:" + "s.name = ?";
		}

		// Remove from params null entries
		params.removeIf(filter -> filter == null);
		// Prepare sql statement according to the search criteria
		if(sql.split(":")[0].equals("City"))
			sql = "City:SELECT search.mapID as \"mapID\", m.description as \"mapDescription\", " +
					"	  COUNT(s.siteID) as \"sitesCount\" " + 
					"	  FROM (SELECT b.mapID as \"mapID\", b.siteID as \"siteID\" " + 
					"			FROM BridgeMSC b " + 
					"			WHERE b.mapID IN (SELECT b.mapID as \"mapID\" " + 
					"						      FROM Sites s, Maps m, BridgeMSC b, Cities c " + 
					"						      WHERE m.mapID = b.mapID AND s.siteID = b.siteID " +
					" AND c.id = b.cityID AND " + sql.split(":")[1] + " AND b.is_active = 1)) as search, Sites s, Maps m " + 
					"	  WHERE search.mapID = m.mapID AND search.siteID = s.siteID AND s.is_active = 1" + 
					"	  GROUP BY search.mapID";
		else
			sql = "Site:SELECT b.mapID as \"mapID\", s.cityname as \"cityname\", " +
								"m.description as \"mapDescription\" " + 
								"FROM Sites s, Maps m, BridgeMSC b, Cities c " +
								"WHERE m.mapID = b.mapID AND s.siteID = b.siteID AND c.id = b.cityID " + 
								"AND " + sql.split(":")[1] + " AND b.is_active = 1 AND s.is_active = 1";

		return sql;
	}

	/**
	 * Private method to get the id of the sites of a specific map
	 * @param params - {@link ArrayList} contains map id, is_active type - current displayed or for future display
	 * @return {@link ArrayList} - an {@link ArrayList} of type int representing the map's sites' id
	 */
	private ArrayList<Object> getMapSitesID(ArrayList<Object> params){
		// Variables
		ArrayList<Object> sites = new ArrayList<Object>();
		ResultSet	   rs	 	= null;

		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare SELECT query
			String sql = "SELECT siteID FROM BridgeMSC WHERE mapID = ? AND is_active = ?";

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			// check if query succeeded
			if(!rs.next()) {
				return null;
			}

			rs.beforeFirst(); // Return cursor to the start of the first row
			
			// Read data
			while (rs.next())
				sites.add(new Integer(rs.getInt("siteID")));
		}
		catch (SQLException e) {
			return null;
		}
		catch(Exception e) {
			return null;
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(rs);	
		}

		return sites;
	}

	/**
	 * Private method to get the currently displayed map's id when this is under editing
	 * @param params - {@link ArrayList} contains map name, city name 
	 * @return int - id of the currently displayed map
	 * @throws SQLException 
	 */
	private int getActiveMap(ArrayList<Object> params) throws SQLException{
		// Variables
		ResultSet rs   		= null;
		int	 	  activemap = 0;

		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare SELECT query
			String sql = "SELECT mapID FROM Maps WHERE mapname = ? AND cityname = ? AND is_active = 1";

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			// check if query succeeded
			if(!rs.next()) {
				throw new Exception();
			}

			rs.beforeFirst(); // Return cursor to the start of the first row
			
			// Read data
			while (rs.next())
				activemap = rs.getInt("mapID");
		}
		catch (SQLException e) {
			throw e;
		}
		catch(Exception e) {
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(rs);	
		}

		return activemap;
	}

	/**
	 * Generic query for UPDATE queries 
	 * @param sql - the UPDATE query 
	 * @param params - {@link ArrayList} of parameters to complete the requested UPDATE query
	 * @throws SQLException, Exception
	 */
	private void editMap(String sql, ArrayList<Object> params) throws SQLException, Exception {
		try {
			// Connect to DB
			SQLController.Connect();

			// Execute sql query, get number of rows affected
			int changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0)
				 throw new Exception("Operation on maps was unsuccessful.");
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

	/**
	 * Private method to turn image to byte array to be saved in database
	 * @param path - image's path
	 * @return byte array representing image
	 */
	private byte[] setImage(String path) {
		if (path == null)
			return null;
		else {
			// Get real path of image
			path = path.substring(path.indexOf("file:/") + 6, path.length());
			File file = new File(path); // try to open the file in path
			try {
					return Files.readAllBytes(file.toPath()); // reads content of image as byte
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		return null;
	}
}