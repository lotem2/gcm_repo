package entity;

import java.util.ArrayList;

/**
 * Entity that represents a City object in the GCM system
 */
public class City implements java.io.Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private ArrayList<Map> maps = new ArrayList<Map>() ;
	private ArrayList<Route> routes = new ArrayList<Route>();
	private int cityCounter;
	private float price;
	
	/**
	 * Constructor that builds the City entity
	 * @param name - city's name
	 * @param description - city's description
	 * @param maps - {@link ArrayList} of type {@link Map} represents city's maps
	 * @param routes - {@link ArrayList} of type {@link Route} represents city's routes
	 * @param cityCounter - represents number of purchases of the city by clients
	 * @param price - represents the price of city
	 */
	public City(String name, String description, ArrayList<Map> maps, 
			ArrayList<Route> routes, int cityCounter, float price) {
		this.name = name;
		this.description = description;
		this.maps = maps;
		this.cityCounter = cityCounter;
		this.price = price;
	}

	/**
	 * Get city name
	 * @return String - city's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set city's name
	 * @param name - city's name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get city's description
	 * @return String - city's name
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set city's description
	 * @param description - city's description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get city's routes
	 * @return {@link ArrayList} of type {@link Route}
	 */
	public ArrayList<Route> getRoutes() {
		return routes;
	}

	/**
	 * Set city's routes' list
	 * @param routes - city's routes
	 */
	public void setRoutes(ArrayList<Route> routes) {
		this.routes = routes;
	}

	/**
	 * Get city's maps
	 * @return {@link ArrayList} of type {@link Map}
	 */
	public ArrayList<Map> getMaps() {
		return maps;
	}

	/**
	 * Set city's maps' list
	 * @param maps - city's maps
	 */
	public void setMaps(ArrayList<Map> maps) {
		this.maps = maps;
	}
	
	/**
	 * Get city's number of purchases
	 * @return int
	 */
	public int getCityCounter() {
		return cityCounter;
	}

	/**
	 * Set city's number of purchases
	 * @param cityCounter - number of purchases
	 */
	public void setCityCounter(int cityCounter) {
		this.cityCounter = cityCounter;
	}

	/**
	 * Get city's price
	 * @return float
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * Set city's price
	 * @param price - new price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}
}
