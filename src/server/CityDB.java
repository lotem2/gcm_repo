package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import common.Action;
import common.Message;
import common.Services;
import entity.*;
import entity.Purchase.PurchaseType;

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
	
	/**
	 * Download requested city's maps and save them in a file
	 * @param - {@link ArrayList} of type {@link Object} containing city name, purchase type and file's path
	 * @return {@link Message} - Contains {@link ArrayList} with success meesage or failure message
	 */
	public Message downloadCity(ArrayList<Object> params){
		// Variables
		City 	cityToDownload = null;
		Message replyMsg 	   = null;

		try {
			// Get city entity by the city's name using getCity method
			Message city = getCity((ArrayList<Object>)params.subList(0, 2));
			
			// If we encountered a problem during retrieval throwing an exception
			if (city.getData().get(1) instanceof String) throw new Exception("Download proccess encountered a problem.");

			cityToDownload = (City)city.getData().get(1); // Convert data to City object
			
			// Write the city to a file using Services' writeCityToFiles
			if(!Services.writeCityToFile(cityToDownload, params.get(2).toString()))
				throw new Exception("Download proccess has encountered a problem.");
			
			// Create success message
			replyMsg = new Message(Action.DOWNLOAD_PURCHASE, 
					new Integer(0), new String("Download city procces was successful."));
		}
		catch(Exception e) {
			// Create failure message
			replyMsg = new Message(Action.DOWNLOAD_PURCHASE, 
								new Integer(1), e.getMessage());
		}

		return replyMsg;
	}
	
	/**
	 * Get city from database according to the requested city's name
	 * @param params - {@link ArrayList} of type {@link Object} contains requested city's name
	 * @return {@link Message} - Contains {@link City} object with full details or failure message
	 */
	public Message getCity(ArrayList<Object> params) {
		// Variables
		Message			 replyMsg	  = null;
		City			 current_city = null;
		ResultSet      	 rs 		  = null;
		int 		   	 id		  	  = 0;
		String 		     sql;

		try {
			// Connect to DB
			SQLController.Connect();

			sql = "SELECT id, name, description FROM Cities where name = ?";	// Prepare sql query
			
			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, (ArrayList<Object>)params.subList(0, 1));

			// check if query succeeded
			if(!rs.next())
				throw new Exception("City where not found.");
			
			rs.beforeFirst();
			
			// Read data
			while(rs.next()) {
				id = rs.getInt("id"); // Get the city's id from database
				// Create instance of the requested city
				current_city = new City(rs.getString("name"), 
										rs.getString("description"), 
										null, 
										null, 
										rs.getInt("purchasecounter"), 
										rs.getFloat("price"));
			}
			
			// Get city's maps by calling MapDB's getMapsByCity
			Message maps = MapDB.getInstance().getMapsByCity((ArrayList<Object>)params.subList(0, 1));

			if(maps.getData().get(1) instanceof String) // If encountered a problem during information retrieval 
				throw new Exception(maps.getData().get(1).toString());
			
			current_city.setMaps((ArrayList<Map>)maps.getData().get(1)); // Set city's maps list
			
			// Check according to the purchase type if city's routes retrieval is needed
			if(PurchaseType.valueOf(params.get(1).toString()) == PurchaseType.LONG_TERM_PURCHASE) {
				// Get city's routes by calling RouteDB's getRoutesByCity
				Message routes = RouteDB.getInstance().getRoutesByCity((ArrayList<Object>)params.subList(0, 1));

				if(routes.getData().get(1) instanceof String) // If encountered a problem during information retrieval 
					throw new Exception(routes.getData().get(1).toString());
				
				current_city.setRoutes((ArrayList<Route>)routes.getData().get(1)); // Set city's routes list
			}

			// Create success message with the city's instance
			replyMsg = new Message(null, new Integer(0), current_city);
		}
		catch (SQLException e) {
			replyMsg = new Message(null, new Integer(1),
					new String("There was a problem with the SQL service."));
			}
		catch(Exception e) {
			replyMsg = new Message(null, new Integer(1), e.getMessage());
		}
		finally {
			SQLController.Disconnect(rs);
		}

		return replyMsg;
	}
	
	/**
	 * Add new city to database
	 * @param params - Contain new city's details
	 * @return {@link Message} - Indicating success/failure with corresponding message 
	 */
	public Message AddNewCity(ArrayList<Object> params){
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();
		int changedRows = 0;

		try {
			// Connect to DB
			SQLController.Connect();

			// Check if the map to add is already in the database
			if(SQLController.DoesRecordExist("Cities", "name", params.get(0)))
				throw new Exception("City already exists.");

			// Prepare statement to insert new city
			String sql = "INSERT INTO Cities (`name`, `description`, `purchasecounter`, `price`)" +
						 " VALUES (?, ?, ?, ?)";

			// Execute sql query, get number of changed rows
			changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0)
				 throw new Exception("City was not added successfully.");
			
			// Create data to match the success pattern
			data.add(new Integer(0)); data.add(new String("City was added successfully."));
		}
		catch (SQLException e) {
			data.add(new Integer(1)); data.add(new String("There was a problem with the SQL service."));
		}
		catch(Exception e) {
			data.add(new Integer(1)); data.add(e.getMessage());
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(null);
		}

		return new Message(null, data);
	}
}
