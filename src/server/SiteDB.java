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
	 * Get Map's sites according to the map's name provided by the client
	 * @param params - Contain {@link Action} type and the map's name of the requested map
	 * @return {@link Message} - Contain {@link ArrayList} of the maps's sites or failure message
	 */
	public Message getSites(ArrayList<Object> params){
		// Variables
		ArrayList<Site>   sites = new ArrayList<Site>();
		ArrayList<Object> data  = new ArrayList<Object>();
		ResultSet         rs 	= null;
						
		try {
			// Connect to DB
			SQLController.Connect();							

			// Prepare statement to get current client's details
			String sql = "SELECT * FROM Sites WHERE mapID = ?";

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
}
