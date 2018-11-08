package risk.game;

import java.util.HashMap;
import java.util.LinkedList;
/**
 * 
 * This class create methods set and get Continents names and values 
 *
 */
public class Continent {

	private String name;
	private int value;
	private LinkedList<String> territoryList;
	
	/**
	 * constructor method with incoming parameters.
	 * 
	 * @param name
	 *            continent name with String type
	 * @param bonus
	 *            a continent's value after conquest it
	 */
	public Continent(String name, int value) {
		this.name = name;
		this.value = value;
		territoryList = new LinkedList<String>();
	}
	
	/**
	 * method to get a continent's name
	 * 
	 * @return continent's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * method to get a continent's value.
	 * 
	 * @return continent's value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * method to add new territory
	 * 
	 * @param newTerritory
	 *            if the new territory is not on the territoryList
	 *		then add the new territoy's name to the territoryList
	 */
	public void addTerritory(Territory newTerritory) {
		if (!territoryList.contains(newTerritory.getName())) {
			territoryList.add(newTerritory.getName());	
		}
	}
	
	/**
	 * method to remove one territory
	 * 
	 * @param territory
	 *            to remove a territory from the territoryList
	 */
	public void removeTerritory(Territory territory) {
		territoryList.remove(territory.getName());
	}
	
	/**
	 * method to get a list of the territoy's Map 
	 * 
	 * @return territoryList with type LinkedList
	 */
	public LinkedList<String> getTerritoryMap() {
		return territoryList;
	}
	
	/**
	 * method to check if the territoryList is owned
	 * @param territoryList
	 * @return true or false
	 */
	public boolean isOwned(LinkedList<String> territoryList) {
		HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
		
		for (String territory : this.territoryList) {
			visited.put(territory, false);
		}
		
		for (String territory : territoryList) {
			visited.replace(territory, true);
		}

		return !visited.containsValue(false);
	}
	
}

