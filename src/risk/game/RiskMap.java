package risk.game;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class represents all of the data and functionality that related to map
 */

public class RiskMap {
	public final static int MAX_CONTINENT = 32;
	public final static int MAX_COUNTRY = 255;
	public final static int MAX_ADJACENT_COUNTRIES = 10;
	
	private static RiskMap riskMap;
	private BufferedImage image;
	private HashMap<String, Continent> continentMap;
	private HashMap<String, Territory> territoryMap;
	private HashMap<String, LinkedList<String>> edgeMap;
	
	
	/**
	 * Create a new and empty map when riskMap is null.
	 * @return riskMap
	 */
	public static RiskMap getInstance() {
		if (riskMap == null) {
			riskMap = new RiskMap();
		}
		
		return riskMap;
	}
	
	/**
	 * a private RiskMap constructor
	 * 
	 */
	private RiskMap() {
		image = null;
		continentMap = new HashMap<String, Continent>();
		territoryMap = new HashMap<String, Territory>();
		edgeMap = new HashMap<String, LinkedList<String>>();
	}
	
	/**
	 * Initial function of the class, normalize the local variables.
	 */
	public void clear() {
		image = null;
		continentMap.clear();
		territoryMap.clear();
		edgeMap.clear();
	}
	
	/**
	 * save the image 
	 * 
	 * @param image
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	/**
	 * Return the current Image.
	 * 
	 * @return The current Image.
	 */
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * Add a qualified continent to the local variables.
	 * 
	 * @param newContinent
	 *            The input continent.
	 */
	public void addContinent(Continent newContinent) {
		continentMap.put(newContinent.getName(), newContinent);
	}
	
	/**
	 * Get the current Image.
	 * 
	 * @return continentMap.
	 */
	public HashMap<String, Continent> getContinentMap() {
		return continentMap;
	}
	
	/**
	 * Add a qualified territory to the local variables.
	 * 
	 * @param territory
	 *            The input territory.
	 * 
	 */
	public void addTerritory(Territory territory) {
		territoryMap.put(territory.getName(), territory);
	}
	
	/**
	 * updating the territory information, and replace it with thhe new territory name.
	 * 
	 * @param territory
	 */
	public void updateTerritory(Territory territory) {
		territoryMap.replace(territory.getName(), territory);
	}
	
	/**
	 * Get the territory map.
	 * 
	 * @return territoryMap.
	 */
	public HashMap<String, Territory> getTerritoryMap() {
		return territoryMap;
	}
	
	/**
	 * method building connections among territories
	 * 
	 * @param territory
	 * @param adjacentList
	 */
	public void addLink(String territory, LinkedList<String> adjacentList) {
		edgeMap.put(territory, adjacentList);
	}
	
	/**
	 * method building connections among territories
	 * 
	 * @param newEdgeMap
	 */
	public void addLink(HashMap<String, LinkedList<String>> newEdgeMap) {
		this.edgeMap = newEdgeMap;
	}
	
	/**
	 * Get the edge map.
	 * 
	 * @return edgeMap.
	 */
	public HashMap<String, LinkedList<String>> getEdgeMap() {
		return edgeMap;
	}
}
