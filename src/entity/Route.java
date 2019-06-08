package entity;

import java.util.ArrayList;

/**
 * Entity that represents a Route object in the GCM system
 */
public class Route implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String cityname;
	private ArrayList<Site> sites = new ArrayList<Site>();
	private String description;
	
	/**
	 * Constructor that builds the Route entity
	 * @param id - id of the route as it appears in database
	 * @param cityname - the city the route belongs to
	 * @param sites - {@link ArrayList} of type {@link Site} list of sites that appear in the route
	 * @param description - route's description
	 */
	public Route(int id, String cityname, ArrayList<Site> sites, String description) {
		this.id = id;
		this.cityname = cityname;
		this.sites = sites;
		this.description = description;
	}

	/**
	 * Get route's id
	 * @return int
	 */
	public int getID() {
		return id;
	}

	/**
	 * Get the city name the route belongs to
	 * @return String
	 */
	public String getCityName() {
		return cityname;
	}

	/**
	 * Set the city the route belongs to
	 * @param city name
	 */
	public void setCityName(String cityname) {
		this.cityname = cityname;
	}

	/**
	 * Get the sites that appear in the route
	 * @return {@link ArrayList} of type {@link Site}
	 */
	public ArrayList<Site> getSites() {
		return sites;
	}

	/**
	 * Set the list of sites that belong to the route
	 * @param {@link ArrayList} of type {@link Site} representing list of sites
	 */
	public void setSites(ArrayList<Site> sites) {
		this.sites = sites;
	}

	/**
	 * Get route's description
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set route's description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Override of {@link Object}'s toString method
	 */
	@Override
	public String toString() {
		return "Route - ID: " + this.getID() + "City name: " + this.getCityName() +
			"Sites: " + this.getSites() + " Description: " + this.getDescription();
	}
}
