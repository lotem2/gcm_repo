package entity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

import com.sun.glass.ui.Size;

import javafx.scene.image.Image;

/**
 * Entity that represents a Map object in the GCM system
 */
public class Map implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private String cityname;
	private String mapname;
	private byte[] image;
	private boolean is_active;
	private ArrayList<Site> sites = new ArrayList<Site>() ;

	/**
	 * Constructor that builds the Map entity
	 * @param id - id of the map as it appears in database
	 * @param mapname - map's name
	 * @param description - map's description
	 * @param cityname - the city the map belongs to
	 * @param sites - {@link ArrayList} of type {@link Site} list of sites that appear in the map
	 * @param is_active - boolean representing if current map is displayed or not
	 * @param image - a byte array that represents the map's image
	 */
	public Map(int id, String mapname, String description, 
			String cityname, ArrayList<Site> sites, byte[] image, boolean is_active) {
		this.id = id;
		this.mapname = mapname;
		this.description = description;
		this.cityname = cityname;
		this.sites = sites;
		this.image = image;
		this.is_active = is_active;
	}

	/**
	 * Get map's id
	 * @return int
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Set map's id
	 * @param id - id of map
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * Get map's name
	 * @return String
	 */
	public String getName() {
		return mapname;
	}

	/**
	 * Set map's name
	 * @param mapname - given map name
	 */
	public void setName(String mapname) {
		this.mapname = mapname;
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
	 * @param description - given description
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
	 * @param cityname - given city name
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
	 * @param sites {@link ArrayList} of type {@link Site} representing list of sites
	 */
	public void setSites(ArrayList<Site> sites) {
		this.sites = sites;
	}

	/**
	 * Get is_active status of the map
	 * @return boolean
	 */
	public boolean getIsActive() {
		return is_active;
	}
	
	/**
	 * Get map's image as byte array
	 * @return byte array
	 */
	public byte[] getImageAsByte() {
		return this.image;
	}

	/**
	 * Method to turn image to byte array to be saved in database
	 * @param path - image's path
	 */
	public void setImage(String path) {
		if (path == null)
			image = null;
		else {
			File file = new File(path); // try to open the file in path
			try {
					image = Files.readAllBytes(file.toPath()); // reads content of image as byte
				} catch (Exception e) {
					e.printStackTrace();
					image = null;
				}
			}
	}
	
	/**
	 * Get the Map's image
	 * @return Image - map's image that was read from a file
	 */
	public Image getMapImage() {
		return (new Image(new ByteArrayInputStream(image)));
	}
	
	/**
	 * Override of {@link Object}'s toString method
	 */
	@Override
	public String toString() {
		if(sites != null) {
			String str = "=========================================================================" + 
					System.getProperty("line.separator") +
					"						  " + mapname +"						 		  " + System.getProperty("line.separator") +
					"	Description: " + description + System.getProperty("line.separator") +
					"	City: " + cityname + System.getProperty("line.separator") + 
					"	List of sites:" + "\n=========================================" 
					+ System.getProperty("line.separator"); 
			
			for (Site site : sites) {
				str += site.toString();
				str += System.getProperty("line.separator");
			}
			
			return str;
		}

		return "";
	}
}
