package gui;

public class SearchTable {
	private  String cityName ,siteName ,description;

	public SearchTable(String cityName, String siteName, String description) {
		super();
		this.cityName = cityName;
		this.siteName = siteName;
		this.description = description;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
