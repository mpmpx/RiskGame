package risk.game;

import java.awt.Color;
import java.awt.Point;

/**
 * This class handle territory linked relationship, and manage to set and get
 * territories names.
 */
public class Territory {

	private String name;
	private String continentName;
	private Point location;
	private Color owner;
	private int army;
	/**
	 * constructor method.
	 */
	public Territory() {
		name = null;
		continentName = null;
		location = null;
		owner = null;
		army = 0;
	}
	/**
	 * constructor method.
	 * @param name
	 *		the point of the location
	 * @param location
	 *		the name in String type
	 */
	
	public Territory(String name, Point location) {
		this.name = name;
		continentName = null;
		this.location = location;
		army = 0;
	}
	/**
	 * To set the territory's name.
	 * 
	 * @param name
	 *            the name that is wanted to input.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * To get a territory's name.
	 * 
	 * @return the territory's name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * To set the continent's name.
	 * 
	 * @param continentName
	 *            the name that is wanted to input.
	 */
	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}
	/**
	 * To get a continent's name.
	 * 
	 * @return the continent's name.
	 */
	public String getContinentName() {
		return continentName;
	}
	/**
	 * To set the location's point.
	 * 
	 * @param location
	 *            the point that is located.
	 */
	public void setLocation(Point location) {
		this.location = location;
	}
	/**
	 * To set the location's point.
	 * 
	 * @param x,y
	 *            the new point that is located in type Int.
	 */
	public void setLocation(int x, int y) {
		location = new Point(x, y);
	}
	/**
	 * To get a location's point.
	 * 
	 * @return the location.
	 */
	public Point getLocation() {
		return location;
	}
	/**
	 * To get a location's x value.
	 * 
	 * @return the x in int.
	 */
	public int getX() {
		return location.x;
	}
	/**
	 * To get a location's y value.
	 * 
	 * @return the y in int.
	 */
	public int getY() {
		return location.y;
	}
	/**
	 * To set the owner
	 * 
	 * @param owner
	 *            set the owner using type color
	 */
	public void setOwner(Color owner) {
		this.owner = owner;
	}
	/**
	 * To get a the owner according Color object .
	 * 
	 * @return the owner .
	 */
	public Color getOwner() {
		return owner;
	}
	/**
	 * To set the Army
	 * 
	 * @param army
	 *            set the owner with type int
	 */
	public void setArmy(int army) {
		this.army = army;
	}
	/**
	 * To get Army.
	 * 
	 * @return the army .
	 */
	public int getArmy() {
		return army;
	}
	/**
	 * To add the additional Army
	 * 
	 * @param army
	 *            add additional army with int type
	 */
	public void addArmy(int army) {
		this.army += army;
	}
	/**
	 * To remove the Army
	 * 
	 * @param army
	 *            remove army with int type
	 */
	public void removeArmy(int army) {
		this.army -= army;
	}
}
