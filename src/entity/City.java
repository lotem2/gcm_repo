package entity;

import java.util.ArrayList;

public class City {	
	private ArrayList<Map> maps = new ArrayList<Map>() ;
	private int cityCounter;
	private float price;
	
	public City(ArrayList<Map> maps, int cityCounter, float price) {
		this.maps = maps;
		this.cityCounter = cityCounter;
		this.price = price;
	}

	public ArrayList<Map> getMaps() {
		return maps;
	}
	
	public void setMaps(ArrayList<Map> maps) {
		this.maps = maps;
	}
	
	public int getCityCounter() {
		return cityCounter;
	}
	
	public void setCityCounter(int cityCounter) {
		this.cityCounter = cityCounter;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}

}
