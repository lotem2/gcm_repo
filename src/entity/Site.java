package entity;

import common.*;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Entity that represents a Site object in the GCM system
 */
public class Site implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private String name;
	private String cityname;
	private Classification classification;
	private String description;
	private boolean isAccessible;
	private float visitDuration;
	private Point2D.Double location;

	/**
	 * Constructor that builds the User entity
	 * @param name - Site's name
	 * @param cityname - The Site's city name
	 * @param classification - The site's classification
	 * @param description - The site's description
	 * @param isAccessible - The site's accessibility
	 * @param visitTime - The user's visit time
	 * @param location - The site's location
	 */
	public Site(String name, String cityname, Classification classification, String description,
			boolean isAccessible, float visitTime, Point2D.Double location) {
		this.name = name;
		this.cityname = cityname;
		this.classification = classification;
		this.description = description;
		this.isAccessible = isAccessible;
		this.visitDuration = visitTime;
		this.location = location;
	}

	/**
	 * Get site's name
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set site's name
	 * @param name - given name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the site's city name
	 * @return String
	 */
	public String getcityName() {
		return cityname;
	}
	
	/**
	 * Set site's classification
	 * @param cityname - given city name
	 */
	public void setcityName(String cityname) {
		this.cityname = cityname;
	}
	
	/**
	 * Get site's classification
	 * @return {@link classification}
	 */
	public Classification getClassification() {
		return classification;
	}

	/**
	 * @param classification - Set site's classification
	 */
	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	/**
	 * Get site's visit duration
	 * @return Float
	 */
	public float getVisitTime() {
		return visitDuration;
	}

	/**
	 * @param visitTime - Set site's visit duration
	 */
	public void setVisitTime(float visitTime) {
		this.visitDuration = visitTime;
	}

	/**
	 * Get site's description
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description - Set site's description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Check whether site is accessible
	 * @return Boolean
	 */
	public boolean isAccessible() {
		return isAccessible;
	}

	/**
	 * Get site's location
	 * @return Point
	 */
	public Point2D.Double getLocation() {
		return location;
	}

	/**
	 * Override of {@link Object}'s toString method
	 */
	@Override
	public String toString() {
		return "Site name: " + name + System.getProperty("line.separator") +
				" City name: " + cityname + System.getProperty("line.separator") + "Classification: " + classification +
				System.getProperty("line.separator") + "Description: " + description + System.getProperty("line.separator") + 
				"Is accessible: "  + isAccessible + System.getProperty("line.separator") + 
				"Visit duration: " + visitDuration + System.getProperty("line.separator") + "Location: " + location + System.getProperty("line.separator");
	}

}
