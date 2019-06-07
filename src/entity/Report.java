package entity;

/**
 * This class represents an activity report which allows managing editors
 * to track the activity on GCM system according to a specified period
 * of time
 */
public class Report implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String m_cityName;
	private int m_numOfMembers;
	private int m_numOfRenew;
	private int m_numOfViews;
	private int m_numOfDownloads;
	private int m_numOfOTP;
	private int m_numOfLTP;

	/**
	 * Constructor that builds the Report entity
	 * @param name - city's name
	 * @param numOfMembers - number of subscribed members
	 * @param numOfRenew - the number of time users renewed their subscription
	 * @param numOfViews - the total number of views per each city maps
	 * @param numOfDownloads - the total number of downloads per each city maps
	 * @param numOfOTP - the total number of one time purchases
	 * @param numOfLTP - the total number of long time purchases
	 */
	public Report (String cityName, int numOfMembers, int numOfRenew,  int numOfViews, int numOfDownloads,
			int numOfOTP, int numOfLTP)
	{
		m_cityName =cityName;
		m_numOfOTP = numOfOTP;
		m_numOfLTP = numOfLTP;
		m_numOfMembers = numOfMembers;
	    m_numOfRenew = numOfRenew;
	    m_numOfViews = numOfViews;
		m_numOfDownloads = numOfDownloads;

	}

	/* Getters */

	/**
	 * Get city name
	 * @return String - city's name
	 */
	public String getCityName() { return m_cityName; }

	/**
	 * Get total number of long time purchases
	 * @return Integer - number of long time purchases
	 */
	public int getNumOfMaps() { return m_numOfLTP; }

	/**
	 * Get total number of short time purchases
	 * @return Integer - number of short time purchases
	 */
	public int getNumOfOTP() { return m_numOfOTP; }

	/**
	 * Get total number of subscribers
	 * @return Integer - total number of subscribers
	 */
	public int getNumOfMembers() { return m_numOfMembers; }

	/**
	 * Get total number of subscribers who renewd their subscription
	 * @return Integer -  total number of subscribers who renewd
	 * their subscription
	 */
	public int getNumOfRenew() { return m_numOfRenew; }

	/**
	 * Get total number of subscribers' views
	 * @return Integer - total number of subscribers' views
	 */
	public int getNumOfViews() { return m_numOfViews; }

	/**
	 * Get total number of subscribers' downloads
	 * @return Integer - total number of subscribers' downloads
	 */
	public int getNumOfDownloads() { return m_numOfDownloads; }
}