package server;

import java.sql.ResultSet;
import java.sql.SQLException;
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
			if (SQLController.DoesRecordExists("Sites", "description", "%" + params.get(2).toString() + "%"))
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
					" AND c.id = b.cityID AND " + sql.split(":")[1] + ")) as search, Sites s, Maps m " + 
					"	  WHERE search.mapID = m.mapID AND search.siteID = s.siteID " + 
					"	  GROUP BY search.mapID";
		else
			sql = "Site:SELECT b.mapID as \"mapID\", s.cityname as \"cityname\", " +
								"m.description as \"mapDescription\" " + 
								"FROM Sites s, Maps m, BridgeMSC b, Cities c " +
								"WHERE m.mapID = b.mapID AND s.siteID = b.siteID AND c.id = b.cityID " + 
								"AND " + sql.split(":")[1];

		return sql;
	}
}