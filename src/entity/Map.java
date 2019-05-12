package entity;

import java.util.ArrayList;

public class Map {
	private String description;
	private ArrayList<Site> sites = new ArrayList<Site>() ;

	public Map(String description, ArrayList<Site> sites) {
		this.description = description;
		this.sites = sites;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<Site> getSites() {
		return sites;
	}

	public void setSites(ArrayList<Site> sites) {
		this.sites = sites;
	}
}
