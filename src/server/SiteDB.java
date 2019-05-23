package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
	 * Get Map's sites or Route's sites according to the map/route description provided by the client
	 * @param params - Contain {@link Action} type and the map/route description of the requested map
	 * @return {@link Message} - Contain {@link ArrayList} of sites or failure message
	 */
	public Message getSites(ArrayList<Object> params){
		// Variables
		ArrayList<Site>   sites = new ArrayList<Site>();
		ArrayList<Object> data  = new ArrayList<Object>();
		ResultSet         rs 	= null;
		String sql;
				
		try {
			// Connect to DB
			SQLController.Connect();							
			
			// Check action required - get sites by route or by map
			if(params.get(1).toString().equals("Map")) {
				// Prepare statement to get map's sites
				sql = "SELECT s.name as \"name\", s.cityname as \"cityname\", s.classification as \"classification\", "+
						 " s.description as \"description\", s.accessible as \"accessible\", " +
						 " s.visitDuration as \"visitDuration\", s.location as \"location\" " +
						 " FROM Sites s, Maps m, Bridge b" +
						 " WHERE m.mapID = b.mapID AND s.siteID AND m.description = ?";
			}
			else {
				// Prepare statement to get route's sites
				sql = "SELECT s.name as \"name\", s.cityname as \"cityname\", s.classification as \"classification\", "+
							 " s.description as \"description\", s.accessible as \"accessible\", " +
							 " s.visitDuration as \"visitDuration\", s.location as \"location\" " +
							 " FROM Sites s, Routes r, BridgeSRC b" +
							 " WHERE r.mapID = b.mapID AND s.siteID AND r.description = ?";
			}

			params.remove(1);	// Remove Map/Route identifier 

			// Execute sql query, get results
			rs = SQLController.ExecuteQuery(sql, params);

			if (!rs.next()) {
				throw new Exception("Could not find sites for the requested map");
			}
			
			while (rs.next()) {
				// Reads location coordinates
				String xLocation = rs.getString("location".split(",")[0]);
				String yLocation = rs.getString("location".split(",")[1]);

				// Construct a new Site object
				Site currentSite = new Site(
						rs.getString("name"),
						rs.getString("cityname"),
						Classification.valueOf(rs.getString("classification").toUpperCase()),
						rs.getString("description"),
						rs.getBoolean("accessible"),
						rs.getFloat("visitDuration"),
						new Point(Integer.parseInt(xLocation), Integer.parseInt(yLocation)));

				// Adds the site to the list
				sites.add(currentSite);
			}

			data.add(new Integer(0));
			data.add(sites);
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
			SQLController.Disconnect(rs);
		}

		return new Message(null, data);
	}
	
	/**
	 * Edit a specific site's details 
	 * @param params - Contain {@link Action} site's new details
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message EditSite(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();

		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare statement to insert new user
			String sql = "UPDATE Sites SET name = ?, classification = ?, description = ?, accessible = ?, " +
						 "visitDuration = ?, location = ? " +
						 "WHERE name = ?";

			// Execute sql query, get number of changed rows
			int changedRows = SQLController.ExecuteUpdate(sql, params);

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
	 * Add a new site to database
	 * @param params - Contain {@link Action} new site's details
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public Message AddSite(ArrayList<Object> params) {
		// Variables
		ArrayList<Object> data = new ArrayList<Object>();

		try {
			// Connect to DB
			SQLController.Connect();

			// Prepare statement to insert new user
			String sql = "INSERT INTO Sites (`name`, `cityname`, `classification`, `description`, `accessible`, " +
						 "`visitDuration`, `location`) VALUES (?, ?, ?, ?, ?, ?, ?)" +
						 "WHERE name = ?";

			// Execute sql query, get number of changed rows
			int changedRows = SQLController.ExecuteUpdate(sql, params);

			// Check if update was successful - result should be greater than zero
			if (changedRows == 0) {
				 throw new Exception("Site was not added successfully.");
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
}
