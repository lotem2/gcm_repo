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
	private Integer m_numOfMembers;
	private Integer m_numOfRenew;
	private Integer m_numOfViews;
	private Integer m_numOfDownloads;
	private Integer m_numOfOTP;
	private Integer m_numOfLTP;

	/**
	 * Constructor that builds the Report entity
	 * @param cityName - city's name
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
		m_cityName = cityName;
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
	 * @return String - number of long time purchases
	 */
	public Integer getNumOfLTP() { return m_numOfLTP; }

	/**
	 * Get total number of one time purchases
	 * @return Integer - number of one time purchases
	 */
	public Integer getNumOfOTP() { return m_numOfOTP; }

	/**
	 * Get total number of subscribers
	 * @return Integer - total number of subscribers
	 */
	public Integer getNumOfMembers() { return m_numOfMembers; }

	/**
	 * Get total number of subscribers who renewd their subscription
	 * @return Integer -  total number of subscribers who renewd
	 * their subscription
	 */
	public Integer getNumOfRenew() { return m_numOfRenew; }

	/**
	 * Get total number of subscribers' views
	 * @return Integer - total number of subscribers' views
	 */
	public Integer getNumOfViews() { return m_numOfViews; }

	/**
	 * Get total number of subscribers' downloads
	 * @return Integer - total number of subscribers' downloads
	 */
	public Integer getNumOfDownloads() { return m_numOfDownloads; }
	
	/**
	 * Override of {@link Object}'s toString method
	 */
	@Override
	public String toString() {
			return "Report - City name: " + this.getCityName() + " OTP: " + this.getNumOfOTP() + " LTP: " + this.getNumOfLTP() 
			+ " members: " + this.getNumOfMembers() + " renews: " + this.getNumOfRenew() + " views: " +this.getNumOfViews()
			+ " downloads: " + this.getNumOfDownloads();
	}
}