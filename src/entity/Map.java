package entity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Entity that represents a Map object in the GCM system
 */
public class Map {
	private int id;
	private String description;
	private String cityname;
	private String mapname;
	private byte[] image;
	private ArrayList<Site> sites = new ArrayList<Site>() ;

	/**
	 * Constructor that builds the Map entity
	 * @param id - id of the map as it appears in database
	 * @param mapname - map's name
	 * @param description - map's description
	 * @param cityname - the city the map belongs to
	 * @param sites - {@link ArrayList} of type {@link Site} list of sites that appear in the map
	 * @param image - a byte array that represents the map's image
	 */
	public Map(int id, String mapname, String description, String cityname, ArrayList<Site> sites, byte[] image) {
		this.id = id;
		this.mapname = mapname;
		this.description = description;
		this.cityname = cityname;
		this.sites = sites;
		this.image = image;
	}

	/**
	 * Get map's id
	 * @return int
	 */
	public int getID() {
		return id;
	}

	/**
	 * Get map's description
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set map's description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the city name the map belongs to
	 * @return String
	 */
	public String getCityName() {
		return cityname;
	}

	/**
	 * Set the city the map belongs to
	 * @param city name
	 */
	public void setCityName(String cityname) {
		this.cityname = cityname;
	}

	/**
	 * Get the sites that appear in the map
	 * @return {@link ArrayList} of type {@link Site}
	 */
	public ArrayList<Site> getSites() {
		return sites;
	}

	/**
	 * Set the list of sites that belong to the map
	 * @param {@link ArrayList} of type {@link Site} representing list of sites
	 */
	public void setSites(ArrayList<Site> sites) {
		this.sites = sites;
	}

	/**
	 * Get map's image as byte array
	 * @return byte array
	 */
	public byte[] getImageAsByte() {
		return this.image;
	}

	/**
	 * Override of {@link Object}'s toString method
	 */
	@Override
	public String toString() {
		return super.toString() + "\nDescription: " + description + "\nNumber of sites: " + sites.size();
	}
}
