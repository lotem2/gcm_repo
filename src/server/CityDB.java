package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import common.Action;
import common.Message;
import common.Permission;
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
	 * @param params - given user's permission
	 * Get list of cities currently in the database and their prices
	 * @return {@link Message} - Contains {@link ArrayList} of pairs with (City, Price) or failure message
	 */
	public Message getCitiesList(ArrayList<Object> params){
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

			if(params.get(0).toString().equals(Permission.CLIENT.toString())) {
				// Check if there is a city with no active maps, if so remove this city
				Iterator<String> it = cities.keySet().iterator();
				while (it.hasNext())
				{
					String city = it.next();
					// Check if there are active maps for the current city
					ArrayList<Object> checkIsActive = new ArrayList<>();
					checkIsActive.add(city);
	
					if(!SQLController.DoesRecordExist("Maps","cityname", "is_active", 
							city, 1))
						it.remove();
					
				}
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
	 * @param params - {@link ArrayList} of type {@link Object} user name who requested download, city name to download,
	 * the user's permission
	 * @return {@link Message} - Contains {@link ArrayList} with success message or failure message
	 */
	public Message downloadCity(ArrayList<Object> params){
		// Variables
		Message replyMsg 	   = null;

		try {
			// Get city entity by the city's name using getCity method
			ArrayList<Object> input = new ArrayList<Object>(); input.add(params.get(2)); input.add(params.get(1));
			
			if(!SQLController.DoesRecordExist("Maps","cityname", "is_active", params.get(1).toString(), 1))
				throw new Exception("There are no maps for this city.");

			Message city = getCity(input);
			
			// If we encountered a problem during retrieval throwing an exception
			if (city.getData().get(1) instanceof String) throw new Exception("Download proccess encountered a problem.");
			
			// After city retrieval was successful, update the download column for the current purchase
			// add the user name and city name for next request
			input.clear(); input.add(params.get(0)); input.add(params.get(1));

			// Update download column in the user's purchase, if encountered an issue raise exception
			Message msg = PurchaseDB.getInstance().downloadPurchase(input);
			if((Integer)(msg.getData().get(0)) == 1)
				throw new Exception(msg.getData().get(1).toString());

			// Create success message
			replyMsg = new Message(Action.DOWNLOAD_PURCHASE, 
					new Integer(0), city.getData().get(1));
		}
		catch(Exception e) {
			// Create failure message
			replyMsg = new Message(Action.DOWNLOAD_PURCHASE, 
								new Integer(1), e.getMessage());
		}

		return replyMsg;
	}
	
	/**
	 * Edit requested city's price
	 * @param params - {@link ArrayList} of type {@link Object} containing new price and city name
	 * @return {@link Message} - Contains {@link ArrayList} with success message or failure message
	 */
	public Message UpdateCityPriceAfterApproval(ArrayList<Object> params){
		// Variables
		ArrayList<Object> data        = new ArrayList<Object>();
		Message           replyMsg    = null;
		ResultSet      	  rs 		  = null;
		String 		      sql		  = null;
		int result = 0;

		//Insert params at the following order: city name, new price.
		data.add(params.get(1)); data.add(params.get(0));
		try {
			// Connect to DB
			SQLController.Connect();

			sql = "UPDATE `Cities` SET price = ? WHERE Cities.name = ?"; // Prepare sql query

			// Execute sql query, get results
			result = SQLController.ExecuteUpdate(sql, data);

			// check if query succeeded
			if((Integer)result == 0)
				throw new Exception("City price was not updated succesfully.");

			// Create success message with the city's instance
			replyMsg = new Message(Action.HANDLE_PRICE_CHANGE_REQ, new Integer(0));
		}
		catch (SQLException e) {
			replyMsg = new Message(Action.HANDLE_PRICE_CHANGE_REQ, new Integer(1),
					new String("There was a problem with the SQL service."));
			}
		catch(Exception e) {
			replyMsg = new Message(Action.HANDLE_PRICE_CHANGE_REQ, new Integer(1), e.getMessage());
		}
		finally {
			SQLController.Disconnect(rs);
		}

		return replyMsg;
	}

	/**
	 * Get city from database according to the requested city's name
	 * @param params - {@link ArrayList} of type {@link Object} contain user's permission and requested city name
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

			sql = "SELECT * FROM Cities where name = ?";	// Prepare sql query

			// Get city name from params
			ArrayList<Object> param = new ArrayList<Object>(params.subList(1, params.size()));
			
			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, param);

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
			Message maps = MapDB.getInstance().getMapsByCity(params);

			if(maps.getData().get(1) instanceof String) // If encountered a problem during information retrieval 
				throw new Exception(maps.getData().get(1).toString());
			
			current_city.setMaps((ArrayList<Map>)maps.getData().get(1)); // Set city's maps list
			
			// Check according to the purchase type if city's routes retrieval is needed
			// Or when user request a specific city
				if((params.get(1).getClass().equals(PurchaseType.class) &&
						PurchaseType.valueOf(params.get(1).toString()) == PurchaseType.LONG_TERM_PURCHASE) ||
					(params.get(0).getClass().equals(Permission.class))) {
					// Get city's routes by calling RouteDB's getRoutesByCity
					Message msg = RouteDB.getInstance().getRoutesByCity(params);

					// If encountered a problem during information retrieval set routes as null
					ArrayList<Route> city_routes = msg.getData().get(1) instanceof String ? null : 
												   (ArrayList<Route>)msg.getData().get(1);
					
					current_city.setRoutes(city_routes); // Set city's routes list
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
	public Message addNewCity(ArrayList<Object> params){
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

	/**
	 * Update details in database of the current city's new version, update the collection of maps
	 * @param params - Contains the  manager's response - approve/decline, city name
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message publishMapsCollection(ArrayList<Object> params) {
		// Variables 
		Message replyMsg;
		
		try {
			// Get the maps' id that need to update their content after the management response
			// using city name
			ArrayList<Object> param = new ArrayList<Object>(params.subList(1, params.size()));
			ArrayList<Integer> maps = getToApproveMapsID(param);

			// Update every maps using MapDB's publishNewVersion method
			for (Integer mapid : maps) {
				param.clear(); param.add(params.get(0)); param.add(mapid);
				MapDB.getInstance().publishNewVersion(param);
			}
			
			// Update the details of the city's maps collection using MapDB's updateMapDetailsAfterApproval method
			MapDB.getInstance().updateMapDetailsAfterApproval(params);
			
			// Update the details of the city's routes collection using RouteDB's publishRoutes method
			RouteDB.getInstance().publishRoutes(params);

			replyMsg = new Message(null, new Integer(0));
		}
		catch (SQLException e) {
			replyMsg = new Message(null, new Integer(1), e.getMessage());
		}
		catch (Exception e) {
			replyMsg = new Message(null, new Integer(1), e.getMessage());
		}

		return replyMsg;
	}
	
	/**
	 * Update purchase counter of city when there is a new purchase of the city
	 * @param params - contains the city name to update
	 * @return {@link Message} - contains result of action - success/failure and failure message in case of failure
	 */
	public Message updateCityPurchaseCounter(ArrayList<Object> params) {
		// Variables
		int 			  changedRows = 0;
		Message 		  replyMsg;
		
		try {
			// Prepare statement to get current client's purchase
			String sql = "UPDATE Cities SET purchasecounter = purchasecounter + 1" +
					 	 " WHERE name = ?";

			// Execute sql query, get number of changed rows
			changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0)
				 throw new Exception("Update of purchases counter of the city was not successful.");

			replyMsg = new Message(null, new Integer(0), "Update of purchases counter of the city was successful");
		}
		catch (SQLException e) {
			replyMsg = new Message(null, new Integer(1), "There was a problem with the SQL service.");
		}
		catch(Exception e) {
			replyMsg = new Message(null, new Integer(1), e.getMessage());
		}

		return replyMsg;
	}

	/**
	 * Private method to get the id of the maps of specific city
	 * @param params - {@link ArrayList} contains city name
	 * @return {@link ArrayList} - an {@link ArrayList} of type int representing the city's maps' id
	 * that need update after the management response
	 */
	private ArrayList<Integer> getToApproveMapsID(ArrayList<Object> params){
		// Variables
		ArrayList<Integer> maps = new ArrayList<Integer>();
		ResultSet	       rs	= null;

		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare SELECT query
			String sql = "SELECT m1.mapID as \"id\" " + 
					"FROM Maps m1 \n" + 
					"WHERE m1.cityname = ? AND m1.mapID IN \n" + 
					"(SELECT a.mapID as \"id\" FROM Maps a \n" + 
					"WHERE m1.mapname = a.mapname AND m1.cityname = a.cityname AND \r\n" + 
					"NOT EXISTS(SELECT 1 FROM Maps b WHERE b.mapname = a.mapname AND b.cityname = a.cityname "
					+ "AND b.is_active = 1 AND a.is_active = 0))";

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			// check if query succeeded
			if(!rs.next()) {
				return null;
			}

			rs.beforeFirst(); // Return cursor to the start of the first row
			
			// Read data
			while (rs.next())
				maps.add(new Integer(rs.getInt("id")));
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

		return maps;
	}
}
