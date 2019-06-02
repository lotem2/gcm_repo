package entity;

import java.util.ArrayList;

public class Route {
	private ArrayList<Site> sites = new ArrayList<Site>() ;
	private String description;
	
	public Route(ArrayList<Site> sites, String description) {
		super();
		this.sites = sites;
		this.description = description;
	}
	
	public ArrayList<Site> getSites() {
		return sites;
	}
	public void setSites(ArrayList<Site> sites) {
		this.sites = sites;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
