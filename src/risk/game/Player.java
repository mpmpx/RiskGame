package risk.game;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
/**
 * This class represents all of the data and funcionality that a player would
 * have. All of the actions that a player would do eventually comes back to this
 * class.
 */
public class Player{

	private String name;
	private Color color;
	private Cards cards;
	
	private HashMap<String, Territory> territoryMap;
	private LinkedList<Continent> controlledContinent;
	private HashMap<String, LinkedList<Territory>> reachableMap;
	private HashMap<String, LinkedList<Territory>> attackableMap;
	private int ownedTerritoryNum;
	private int totalArmy;
	private int unassignedArmy;
	private int freeArmy;
	
	private boolean hasConquered;
	/**
	 * Constructor method
	 * 
	 * @param name
	 *            the player name with String type
	 */
	public Player(String name) {
		this.name = name;
		color = PlayerColor.nextColor();
		cards = new Cards();
		territoryMap = new HashMap<String, Territory>();
		controlledContinent = new LinkedList<Continent>();
		reachableMap = new HashMap<String, LinkedList<Territory>>();
		attackableMap = new HashMap<String, LinkedList<Territory>>();
		ownedTerritoryNum = 0;
		totalArmy = 0;
		unassignedArmy = 0;
		freeArmy = 0;
		hasConquered = false;
	}
	/**
	 * Method to reinforce by implementing the selected strategy
	 */
	public void reinforcement() {
		getReinforcement();
	}
	/**
	 * Method to attack by implementing the selected strategy
	 */
	public void attack() {
		attackableMap.clear();
		for (Territory territory : territoryMap.values()) {
			attackableMap.put(territory.getName(), getAttackableList(territory));
		}
	}
	/**
	 * Method to fortify by implementing the selected strategy
	 */
	public void fortification() {
		reachableMap.clear();
		for (Territory territory : territoryMap.values()) {
			reachableMap.put(territory.getName(), getReachableList(territory));
		}
	}
	/**
	 * Method to update continent
	 */
	private void updateControlledContinent() {
		LinkedList<String> territoryList = new LinkedList<String>();
		controlledContinent.clear();
		
		for (Territory territory : territoryMap.values()) {
			territoryList.add(territory.getName());
		}
		
		for (Continent continent : RiskMap.getInstance().getContinentMap().values()) {
			if (continent.isOwned(territoryList)) {
				controlledContinent.add(continent);
			}
		}
	}
	
	private void getReinforcement() {
		unassignedArmy = 0;
		freeArmy = 0;

		freeArmy = (int) Math.max(Math.floor(ownedTerritoryNum / 3), 3);
		unassignedArmy  += freeArmy;
		
		for (Continent continent : controlledContinent) {
			unassignedArmy += continent.getValue();
		}
		
		totalArmy += unassignedArmy;
	}
	
	public HashMap<String, LinkedList<Territory>> getAttackableMap() {
		return attackableMap;
	}
	
	private LinkedList<Territory> getAttackableList(Territory territory) {
		LinkedList<Territory> attackableList = new LinkedList<Territory>();
		HashMap<String, LinkedList<String>> edgeMap = RiskMap.getInstance().getEdgeMap();
		
		if (territory.getArmy() > 1)
		for (String adjacent : edgeMap.get(territory.getName())) {
			Territory adjacentTerritory = RiskMap.getInstance().getTerritoryMap().get(adjacent);
			if (!territoryMap.containsKey(adjacentTerritory.getName())) {
				attackableList.add(RiskMap.getInstance().getTerritoryMap().get(adjacent));
			}
		}
		
		return attackableList;
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
			totalArmy++;
			RiskMap.getInstance().updateTerritory(territory);
		}
		
		//unassignedArmy -= ownedTerritoryNum;
	}
	
	public String getName() {
		return name;
	}
	/**
	 * To get the player's color
	 * 
	 * @return the player color with Color type
	 */
	public Color getColor() {
		return color;
	}
	/**
	 * To add a territory to the player's territory list
	 * 
	 * @param territory
	 *            a territory that need to be added with Country type
	 */
	public void addTerritory(Territory territory) {
		territoryMap.put(territory.getName(), territory);
		ownedTerritoryNum++;
		updateControlledContinent();
	}
	/**
	 * To remove a territory from the player's territory list
	 * 
	 * @param territory
	 *            a territory that need to be removed with territory type
	 */
	public void removeTerritory(Territory territory) {
		totalArmy -= territory.getArmy();
		territoryMap.remove(territory.getName());
		ownedTerritoryNum--;
		updateControlledContinent();
	}
	
	public Territory findTerritory(int x, int y) {
		for (Territory territory : territoryMap.values()) {
			if (territory.getShape().contains(new Point(x, y))) {
				return territory;
			}
			
		}
		return null;
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
	
	public void killArmy(String territory, int army) {
		removeArmy(territory, army);
		totalArmy -= army;
	}
	
	public boolean hasConquered() {
		return hasConquered;
	}
	
	public void conquer() {
		hasConquered = true;
	}
	
	public void getCard() {
		if (hasConquered) {
			cards.getCard();
			hasConquered = false;
		}
	}
	
	public Cards getCardSet() {
		return cards;
	}
	
	public void addExchangeBonusArmy(int army) {
		totalArmy += army;
		unassignedArmy += army;
	}
}
