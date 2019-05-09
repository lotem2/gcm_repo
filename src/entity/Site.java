package entity;

import java.awt.Point;

public class Site {	
	private String name;
	private int classification;
	private String description;
	private boolean isAccesiable;
	private float visitTime;
	private Point location;
	
	public Site(String name, int classification, String description, boolean isAccesiable, float visitTime,
			Point location) {
		this.name = name;
		this.classification = classification;
		this.description = description;
		this.isAccesiable = isAccesiable;
		this.visitTime = visitTime;
		this.location = location;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getClassification() {
		return classification;
	}

	public void setClassification(int classification) {
		this.classification = classification;
	}
	
	public float getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(float visitTime) {
		this.visitTime = visitTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAccesiable() {
		return isAccesiable;
	}

	public Point getLocation() {
		return location;
	}
	
}
