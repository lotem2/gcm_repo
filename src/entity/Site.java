package entity;

import common.*;
import java.awt.Point;

public class Site {
	private String name;
	private Classification classification;
	private String description;
	private boolean isAccessible;
	private float visitDuration;
	private Point location;

	public Site(String name, Classification classification, String description, boolean isAccessible, float visitTime,
			Point location) {
		this.name = name;
		this.classification = classification;
		this.description = description;
		this.isAccessible = isAccessible;
		this.visitDuration = visitTime;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	public float getVisitTime() {
		return visitDuration;
	}

	public void setVisitTime(float visitTime) {
		this.visitDuration = visitTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAccessible() {
		return isAccessible;
	}

	public Point getLocation() {
		return location;
	}

}
