package risk.game;

import java.util.HashMap;
import java.util.LinkedList;

public class RiskMap {
	public final static int MAX_CONTINENT = 32;
	public final static int MAX_COUNTRY = 255;
	public final static int MAX_ADJACENT_COUNTRIES = 10;
	
	private static RiskMap riskMap;
	private String imagePath;
	private HashMap<String, Continent> continentMap;
	private HashMap<String, Territory> territoryMap;
	private HashMap<String, LinkedList<String>> edgeMap;
	
	
	
	public static RiskMap getInstance() {
		if (riskMap == null) {
			riskMap = new RiskMap();
		}
		
		return riskMap;
	}
	
	private RiskMap() {
		imagePath = null;
		continentMap = new HashMap<String, Continent>();
		territoryMap = new HashMap<String, Territory>();
		edgeMap = new HashMap<String, LinkedList<String>>();
	}
	
	public void clear() {
		imagePath = null;
		continentMap.clear();
		territoryMap.clear();
		edgeMap.clear();
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public void addContinent(Continent newContinent) {
		continentMap.put(newContinent.getName(), newContinent);
	}
	
	public HashMap<String, Continent> getContinentMap() {
		return continentMap;
	}
	
	public void addTerritory(Territory territory) {
		territoryMap.put(territory.getName(), territory);
	}
	
	public void updateTerritory(Territory territory) {
		territoryMap.replace(territory.getName(), territory);
	}
	
	public HashMap<String, Territory> getTerritoryMap() {
		return territoryMap;
	}
	
	public void addLink(String territory, LinkedList<String> adjacentList) {
		edgeMap.put(territory, adjacentList);
	}
	
	public void addLink(HashMap<String, LinkedList<String>> newEdgeMap) {
		this.edgeMap = newEdgeMap;
	}
	
	public HashMap<String, LinkedList<String>> getEdgeMap() {
		return edgeMap;
	}
}
