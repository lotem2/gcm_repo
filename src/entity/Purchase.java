package entity;

import java.time.LocalDate;

public class Purchase implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum PurchaseType {
	    SHORT_TERM_PURCHASE,
	    LONG_TERM_PURCHASE
	}
	
	// TODO: GUI will handle expiration date - validation and calculation 
	
	// Variables
	private final String m_cityName;
	private final PurchaseType m_purchaseType;
	private final LocalDate m_purchaseDate;
	private final LocalDate m_expirationDate;
	private int m_renewCounter = 0;
	private int m_Views 	   = 0;
	private int m_Downloads    = 0;
	private float m_price 	   = 0;
	
	/* Constructor */
	public Purchase(String cityname, PurchaseType type, LocalDate date, LocalDate expidate, 
			int renew, int views, int downloads, float price) {
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
	public String getCityName() { return m_cityName; }
	public PurchaseType getPurchaseType() { return m_purchaseType; }
	public LocalDate getPurchaseDate() { return m_purchaseDate; }
	public LocalDate getExpirationDate() { return m_expirationDate; }
	public int getRenewCounter() { return m_renewCounter; }
	public int getViews() { return m_Views; }
	public int getDownloads() { return m_Downloads; }
	public float getPrice() { return m_price; }
}
