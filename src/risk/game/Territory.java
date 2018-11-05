package risk.game;

import java.awt.Color;
import java.awt.Point;

public class Territory {

	private String name;
	private String continentName;
	private Point location;
	private Color owner;
	private int army;
	
	public Territory() {
		name = null;
		continentName = null;
		location = null;
		owner = null;
		army = 0;
	}
	
	public Territory(String name, Point location) {
		this.name = name;
		continentName = null;
		this.location = location;
		army = 0;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}
	
	public String getContinentName() {
		return continentName;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
	
	public void setLocation(int x, int y) {
		location = new Point(x, y);
	}
	
	public Point getLocation() {
		return location;
	}
	
	public int getX() {
		return location.x;
	}
	
	public int getY() {
		return location.y;
	}
	
	public void setOwner(Color owner) {
		this.owner = owner;
	}
	
	public Color getOwner() {
		return owner;
	}
	
	public void setArmy(int army) {
		this.army = army;
	}
	
	public int getArmy() {
		return army;
	}
	
	public void addArmy(int army) {
		this.army += army;
	}
	
	public void removeArmy(int army) {
		this.army -= army;
	}
}
