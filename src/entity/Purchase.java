package entity;

import java.time.LocalDate;

/**
 * Entity that represents a Purchase object in the GCM system
 */
public class Purchase implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Enum which represents the type of purchase - one time/limited time
	 */
	public enum PurchaseType {
	    SHORT_TERM_PURCHASE,
	    LONG_TERM_PURCHASE
	}

	// Variables
	private int m_id;
	private String m_username;
	private String m_cityName;
	private PurchaseType m_purchaseType;
	private LocalDate m_purchaseDate;
	private LocalDate m_expirationDate;
	private int m_renewCounter = 0;
	private int m_Views 	   = 0;
	private int m_Downloads    = 0;
	private float m_price 	   = 0;
	
	/**
	 * Constructor that builds the Purchase entity
	 * @param id - the id of the purchase as displayed in database
	 * @param username - the user who did the purchase
	 * @param cityname - the city the user purchased
	 * @param type - type of purchase
	 * @param date - data the purchase was done
	 * @param expidate - the expiry date of the purchase
	 * @param renew - represents the number of times user renew the current purchase
	 * @param views - represents the number of times the user watched the collection of maps
	 * @param downloads - represents the number of times the user downloaded the collection of maps of this purchase
	 * @param price - the price the user paid for this purchase
	 */
	public Purchase(int id, String username, String cityname, PurchaseType type, LocalDate date, LocalDate expidate, 
			int renew, int views, int downloads, float price) {
		m_id = id;
		m_username = username;
		m_cityName = cityname;
		m_purchaseType = type;
		m_purchaseDate = date;
		m_expirationDate = expidate;
		m_renewCounter = renew;
		m_Views = views;
		m_Downloads = downloads;
		m_price = price;
	}
	
	/* Getters */
	/**
	 * Get the purchase's id
	 * @return int
	 */
	public int getID() { return m_id; }

	/**
	 * get user name who did the purchase
	 * @return String
	 */
	public String getUserName() { return m_username; }

	/**
	 * get city name of this purchase
	 * @return String
	 */
	public String getCityName() { return m_cityName; }

	/**
	 * Get type of purchase
	 * @return PurchaseType
	 */
	public PurchaseType getPurchaseType() { return m_purchaseType; }

	/**
	 * Get the date the purchase was done
	 * @return LocalDate
	 */
	public LocalDate getPurchaseDate() { return m_purchaseDate; }

	/**
	 * Set the date the purchase was done
	 * @param newdate - new date to set
	 */
	public void setPurchaseDate(LocalDate newdate) { m_purchaseDate = newdate; }

	/**
	 * Get the expiry date for this purchase
	 * @return LocalDate
	 */
	public LocalDate getExpirationDate() { return m_expirationDate; }

	/**
	 * Set the expiry date for this purchase
	 * @param newdate - new expiry date to set
	 */
	public void setExpirationDate(LocalDate newdate) { m_expirationDate = newdate; }

	/**
	 * get number renew for this purchase
	 * @return int
	 */
	public int getRenewCounter() { return m_renewCounter; }

	/**
	 * Get number of views of this purchases
	 * @return int
	 */
	public int getViews() { return m_Views; }

	/**
	 * Get number of downloads of this purchase's collection of maps
	 * @return int
	 */
	public int getDownloads() { return m_Downloads; }

	/**
	 * Get the price of the purchase
	 * @return float
	 */
	public float getPrice() { return m_price; }

	/**
	 * Set the price of the purchase
	 * @param newprice - new price to set
	 */
	public void setPrice(float newprice) { m_price = newprice; }

	/**
	 * Override of {@link Object}'s toString method
	 */
	@Override
	public String toString() {
		return "User name: " + this.getUserName() + "<br>" + 
			   "City name: " + this.getCityName() + "<br>" +
			   "Purchase type: " + this.getPurchaseType() + "<br>" +
			   "Purchase date: " + this.getPurchaseDate() + "<br>" +
			   "Expirtaion date: " + this.getExpirationDate() + "<br>" +
			   "Price: " + this.getPrice();
	}
}
