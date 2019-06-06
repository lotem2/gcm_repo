package entity;

public class Report {
	
	private String m_cityName;

	private int m_numOfMembers;
	private int m_numOfRenew;
	private int m_numOfViews;
	private int m_numOfDownloads;
	private int m_numOfOTP;
	private int m_numOfLTP;

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
	public String getCityName() { return m_cityName; }
	public int getNumOfMaps() { return m_numOfLTP; }
	public int getNumOfOTP() { return m_numOfOTP; }
	public int getNumOfMembers() { return m_numOfMembers; }
	public int getNumOfRenew() { return m_numOfRenew; }
	public int getNumOfViews() { return m_numOfViews; }
	public int getNumOfDownloads() { return m_numOfDownloads; }
}
