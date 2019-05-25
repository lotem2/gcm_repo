package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
		int i;
		ResultSet rs 		   = null;
		ArrayList<Object> args = new ArrayList<Object>();
		ArrayList<Object> data = new ArrayList<Object>();
		ArrayList<Map> maps    = new ArrayList<Map>();
					
		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare statement to get current client's details
			String sql = "SELECT * FROM ConnectSM WHERE ";
				
			// Iterating over the parameters and constructing the sql string and values of the query
			for (i = 0; i < params.size() - 2; i+=2) {
				sql += params.get(i).toString() + " LIKE " + params.get(i + 1).toString() + " OR ";
				args.add(params.get(i + 1));
			}
	
			// Adding the last search parameter to the sql string and the query params arraylist
			sql += params.get(i - 2).toString() + " LIKE " + params.get(i - 1).toString();
			sql += " ORDER BY mapID ASC";
			args.add(params.get(i - 1));
				
			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, args);
	
			if(!rs.next())
				throw new Exception("Could not find a map according to according to search parameters");

			rs.beforeFirst();
			int prevMapID = -1; // Set a variable to indicate the previous map id that was read
				
			// Reads data 
			while (rs.next()) {
				// Get values from the result set
				int currentMapID = rs.getInt("mapID");
				if(prevMapID != currentMapID) {
					String description = rs.getString("mapDescription");

					// Clear args array list and add arguments for the next query, 
					// get current map's sites list and add it to the arraylist
					args.clear();
					args.add(new Integer(currentMapID));
					ArrayList<Site> mapSites = (ArrayList<Site>)SiteDB.getInstance().getSites(args).getData().get(1);
					maps.add(new Map(description, mapSites));
					prevMapID = currentMapID;
				}
			}
					
			data.add(new Integer(0));
			data.add(maps);
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

		return new Message(null, data);
	}
}
