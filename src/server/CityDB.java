package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import common.Action;
import common.Message;
import entity.Site;

public class CityDB {
	// Variables
	private static CityDB m_instance = null;
	
	/**
	 * Private constructor - prevent this object's creation 
	 */
	private CityDB() {}

	/**
	 * Returns a static instance of CityDB object
	 * @return CityDB
	 */
	public static synchronized CityDB getInstance() {
		if (m_instance == null) {
			m_instance = new CityDB();
		}

		return m_instance;
	}

	/**
	 * Get list of cities currently in the database and their prices
	 * @return {@link Message} - Contains {@link ArrayList} of pairs with <City, Price> or failure message
	 */
	public Message getCitiesList(){
		// Variables
		HashMap<String, Float> cities = new HashMap<String, Float>();
		ArrayList<Object> data  = new ArrayList<Object>();
		ResultSet         rs 	= null;
		String sql;

		try {
			// Connect to DB
			SQLController.Connect();

			sql = "SELECT name, price FROM Cities";	// Prepare sql query
			
			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, null);

			// check if query succeeded
			if(!rs.next()) {
				throw new Exception("No cities where found.");
			}
			
			rs.beforeFirst();
			
			// Read data
			while(rs.next()) {
				cities.put(rs.getString("name"), rs.getFloat("price"));
			}

			data.add(new Integer(0)); // set query result as success
			data.add(cities);	// adding sites' array list
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
}
