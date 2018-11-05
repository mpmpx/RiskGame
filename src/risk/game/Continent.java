package risk.game;

import java.util.HashMap;
import java.util.LinkedList;

public class Continent {

	private String name;
	private int value;
	private LinkedList<String> territoryList;
	
	public Continent(String name, int value) {
		this.name = name;
		this.value = value;
		territoryList = new LinkedList<String>();
	}
	
	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}
	
	public void addTerritory(Territory newTerritory) {
		if (!territoryList.contains(newTerritory.getName())) {
			territoryList.add(newTerritory.getName());	
		}
	}
	
	public void removeTerritory(Territory territory) {
		territoryList.remove(territory.getName());
	}
	
	public LinkedList<String> getTerritoryMap() {
		return territoryList;
	}
	
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

