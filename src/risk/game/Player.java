package risk.game;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class Player{

	private String name;
	private Color color;
	private HashMap<String, Territory> territoryMap;
	private LinkedList<Continent> controlledContinent;
	private HashMap<String, LinkedList<Territory>> reachableMap;
	private HashMap<String, LinkedList<Territory>> attackableMap;
	private int ownedTerritoryNum;
	private int totalArmy;
	private int unassignedArmy;
	private int freeArmy;
	
	public Player(String name) {
		this.name = name;
		color = PlayerColor.nextColor();
		territoryMap = new HashMap<String, Territory>();
		controlledContinent = new LinkedList<Continent>();
		reachableMap = new HashMap<String, LinkedList<Territory>>();
		ownedTerritoryNum = 0;
		totalArmy = 0;
		unassignedArmy = 0;
		freeArmy = 0;
	}
	
	public void reinforcement() {
		getReinforcement();
	}
	
	public void attack() {
	}
	
	public void fortification() {
		reachableMap.clear();
		for (Territory territory : territoryMap.values()) {
			reachableMap.put(territory.getName(), getReachableList(territory));
		}
	}
	
	private void getReinforcement() {
		LinkedList<String> territoryList = new LinkedList<String>();
		LinkedList<Continent> continentList = new LinkedList<Continent>(RiskMap.getInstance().getContinentMap().values());

		unassignedArmy = 0;
		freeArmy = 0;
		
		for (Territory territory : territoryMap.values()) {
			territoryList.add(territory.getName());
		}
		
		freeArmy = (int) Math.max(Math.floor(ownedTerritoryNum / 3), 3);
		unassignedArmy  += freeArmy;
		controlledContinent.clear();
		for (Continent continent : continentList) {
			if (continent.isOwned(territoryList)) {
				unassignedArmy += continent.getValue();
				controlledContinent.add(continent);
			}
		}

		totalArmy += unassignedArmy;
	}
	
	public HashMap<String, LinkedList<Territory>> getReachableMap() {
		return reachableMap;
	}
	
	private LinkedList<Territory> getReachableList(Territory territory) {
		LinkedList<Territory> reachableList = new LinkedList<Territory>();
		Stack<Territory> stack = new Stack<Territory>();
		HashMap<String, LinkedList<String>> edgeMap = RiskMap.getInstance().getEdgeMap();
		
		for (String adjacent : edgeMap.get(territory.getName())){
			if (territoryMap.containsKey(adjacent)) {
				stack.push(territoryMap.get(adjacent));
			}
		}
		
		while (!stack.isEmpty()) {
			Territory currentTerritory = stack.pop();
			reachableList.add(currentTerritory);
			
			for (String adjacent : edgeMap.get(currentTerritory.getName())){
				if (territoryMap.containsKey(adjacent)) {
					if (!adjacent.equals(territory.getName()) && !reachableList.contains(territoryMap.get(adjacent))) {
						stack.push(territoryMap.get(adjacent));
					}
				}
			}
		}
		
		return reachableList;
	}
	
	public void setUnsignedArmy(int army) {
		unassignedArmy = army;
		totalArmy += army;
		for (Territory territory : territoryMap.values()) {
			territory.addArmy(1);
			RiskMap.getInstance().updateTerritory(territory);
		}
		
		unassignedArmy -= ownedTerritoryNum;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void addTerritory(Territory territory) {
		territoryMap.put(territory.getName(), territory);
		ownedTerritoryNum++;
	}
	
	public HashMap<String, Territory> getTerritoryMap() {
		return territoryMap;
	}
	
	public int getUnassignedArmy() {
		return unassignedArmy;
	}
	
	public int getTotalArmy() {
		return totalArmy;
	}
	
	public int getFreeArmy() {
		return freeArmy;
	}
	
	public LinkedList<Continent> getControlledContinent() {
		return controlledContinent;
	}
	
	public void addArmy(String territory, int army) {
		territoryMap.get(territory).addArmy(army);
		RiskMap.getInstance().updateTerritory(territoryMap.get(territory));
		unassignedArmy -= army;
	}
	
	public void removeArmy(String territory, int army) {
		territoryMap.get(territory).removeArmy(army);
		RiskMap.getInstance().updateTerritory(territoryMap.get(territory));
	}
	

	

	

}
