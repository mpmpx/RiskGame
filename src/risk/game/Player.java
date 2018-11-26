package risk.game;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;

import risk.game.strategy.Strategy;

/**
 * This class maintains all of the data and functionality that a player would
 * have. 
 */
public class Player implements Serializable {
	
	private String name;
	private Color color;
	private Cards cards;
	private Strategy strategy;
	
	private HashMap<String, Territory> territoryMap;
	private HashMap<Continent, Boolean> controlledContinent;
	private int ownedTerritoryNum;
	private int totalArmy;
	private int unassignedArmy;
	private int freeArmy;
	
	private HashMap<Territory, LinkedList<Territory>> attackableMap;
	private HashMap<Territory, LinkedList<Territory>> reachableMap;
	private PriorityQueue<Integer> dice;
	
	private boolean hasConquered;
	
	/**
	 * Creates a player and set a string as its name.
	 * @param name a string that is to be the player's name.
	 */
	public Player(String name) {
		this.name = name;
		color = PlayerColor.nextColor();
		cards = new Cards();
		territoryMap = new HashMap<String, Territory>();
		controlledContinent = new HashMap<Continent, Boolean>();
		reachableMap = new HashMap<Territory, LinkedList<Territory>>();
		attackableMap = new HashMap<Territory, LinkedList<Territory>>();
		dice = new PriorityQueue<Integer>(Collections.reverseOrder());
		ownedTerritoryNum = 0;
		totalArmy = 0;
		unassignedArmy = 0;
		freeArmy = 0;
		hasConquered = false;
	}

	/**
	 * Sets the type of strategy to this player.
	 * @param strategy a strategy that is to be the strategy of this player.
	 */
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	/**
	 * Returns the strategy of this player.
	 * @return the strategy of this player.
	 */
	public Strategy getStrategy() {
		return strategy;
	}
	
	/**
	 * Sets the color which represents this player.
	 * @param color the color which represents this player.
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Returns the color of this player.
	 * @return the color of this player.
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Returns the player's name.
	 * @return the player's name.
	 */
	
	public String getName() {
		return name;
	}
	

	/**
	 * Sets controlled continents of this player.
	 * @param continentList a list of continents that is to be set
	 * as controlled continents of this player.
	 */
	public void setContinents(LinkedList<Continent> continentList) {
		for (Continent continent : continentList) {
			controlledContinent.put(continent, false);
		}
	}
	
	/**
	 * Sets unassigned armies to this player.
	 * @param army the number of army that is to be set as 
	 * unassigned armies of this player.
	 */
	public void setUnsignedArmy(int army) {
		unassignedArmy = army;
		totalArmy += army;
	}
	
	/**
	 * Uses the startup strategy.
	 */
	public void startup() {
		strategy.startup();
	}
	
	/**
	 * Uses the reinforcement strategy.
	 */
	public void reinforcement() {
		strategy.reinforce();
	}
	
	/**
	 * Uses the attack strategy.
	 */
	public void attack() {
		strategy.attack();
	}
	
	/**
	 * Uses the fortification strategy.
	 */
	public void fortification() {
		strategy.fortify();
	}
	
	/**
	 * This player gets reinforcement. The number of reinforcement is based on
	 * the number of controlled territories and continents of this player.
	 */
	public void getReinforcement() {
		unassignedArmy = 0;
		freeArmy = 0;

		freeArmy = (int) Math.max(Math.floor(ownedTerritoryNum / 3), 3);
		unassignedArmy  += freeArmy;
		
		for (Continent continent : controlledContinent.keySet()) {
			if (controlledContinent.get(continent)) {
				unassignedArmy += continent.getValue();
			}
		}
		
		totalArmy += unassignedArmy;
	}
	
	/**
	 * Places unassigned armies to the given territory.
	 * @param territory the territory that is to be reinforced by armies.
	 * @param armyNum the number of armies that is to be assigned to the given
	 * territory.
	 */
	public void placeUnassignedArmy(Territory territory, int armyNum) {
		territory.addArmy(armyNum);
		unassignedArmy -= armyNum;
	}

	/**
	 * Updates the hash map containing all enemies' territories that the current
	 * player can attack.
	 */
	public void updateAttackableMap() {
		attackableMap = new HashMap<Territory, LinkedList<Territory>>();

		for (Territory territory : territoryMap.values()) {
			attackableMap.put(territory, getAttackableList(territory));
		}
	}
	
	/**
	 * Returns a list of territories which is able to be attacked by the given territory.
	 * @param territory a list of territories which is able to be attacked by this territory
	 * will be returned.
	 * @return attackableList a list of territories which is able to be attacked by the 
	 * given territory.
	 */
	private LinkedList<Territory> getAttackableList(Territory territory) {
		LinkedList<Territory> attackableList = new LinkedList<Territory>();
		
		if (territory.getArmy() > 1) {
			for (Territory adjacent : territory.getAdjacent().values()) {
				if (!territoryMap.values().contains(adjacent)) {
					attackableList.add(adjacent);
				}
			}	
		}
		return attackableList;
	}
	
	/**
	 * Returns attackable hash map.
	 * @return attackable hash map.
	 */
	public HashMap<Territory, LinkedList<Territory>> getAttackableMap() {
		return attackableMap;
	}
	
	/**
	 * Attacks a territory of enemy by one of territory.
	 * @param attackingTerritory the attacking territory.
	 * @param defendingTerritory the enemy's attacked territory.
	 * @param attackerDiceNum number of dice the attacker used.
	 * @param defenderDiceNum number of dice the defender used.
	 */
	public void attack(Territory attackingTerritory, Territory defendingTerritory, int attackerDiceNum, int defenderDiceNum) {
		Player attacker = this;
		Player defender = defendingTerritory.getOwner();
		
		attacker.rollDice(attackerDiceNum);
		defender.rollDice(defenderDiceNum);

		PriorityQueue<Integer> attackerDice = new PriorityQueue<Integer> (attacker.getDice());
		PriorityQueue<Integer> defenderDice = new PriorityQueue<Integer> (defender.getDice());
		
		while (!attackerDice.isEmpty() && !defenderDice.isEmpty()) {
			if (attackerDice.remove() > defenderDice.remove()) {
				defender.removeArmy(defendingTerritory.getName(), 1);
			}
			else {
				attacker.removeArmy(attackingTerritory.getName(), 1);
			}
		}
		
		
		if (defendingTerritory.getArmy() == 0) {
			defender.removeTerritory(defendingTerritory);
			defendingTerritory.setOwner(attacker);
			defendingTerritory.setColor(attacker.getColor());
			attacker.addTerritory(defendingTerritory);
			hasConquered = true;
		}
		
		updateAttackableMap();
	}
	
	/**
	 * Updates controlled continents.
	 */
	private void updateControlledContinent() {
		LinkedList<String> territoryList = new LinkedList<String>();
		
		for (Territory territory : territoryMap.values()) {
			territoryList.add(territory.getName());
		}
		
		for (Continent continent : controlledContinent.keySet()) {
			if (continent.isOwned(territoryList)) {
				controlledContinent.replace(continent, true);
			}
		}
	}
	
	/**
	 * Update the hash map contains all territories which is able to be connected
	 * to a territory by a path.
	 */
	public void updateReachableMap() {
		reachableMap = new HashMap<Territory, LinkedList<Territory>>();
		
		for (Territory territory : territoryMap.values()) {
			reachableMap.put(territory, getReachableList(territory));
		}		
	}
	
	/**
	 * Returns a list of territories which are able to be connected by the given territory by a path.
	 * @param territory a list of territories which are able to be connected by this territory by a path
	 * will be returned.
	 * @return reachableList a list of territories which are able to be connected by the given 
	 * territory by a path.
	 */
	private LinkedList<Territory> getReachableList(Territory territory) {
		LinkedList<Territory> reachableList = new LinkedList<Territory>();
		Stack<Territory> stack = new Stack<Territory>();
		
		if (territory.getArmy() > 1) {
			for (Territory adjacent : territory.getAdjacent().values()){
				if (territoryMap.containsValue(adjacent)) {
					stack.push(adjacent);
				}
			}
		
			while (!stack.isEmpty()) {
				Territory currentTerritory = stack.pop();
				reachableList.add(currentTerritory);
			
				for (Territory adjacent : currentTerritory.getAdjacent().values()){
					if (territoryMap.containsValue(adjacent)) {
						if (adjacent != territory && !reachableList.contains(adjacent)) {
							stack.push(adjacent);
						}
					}
				}
			}
		}
		
		return reachableList;
	}

	/**
	 * Returns the reachable hash map.
	 * @return the reachable hash map.
	 */
	public HashMap<Territory, LinkedList<Territory>> getReachableMap() {
		return reachableMap;
	}
	
	/**
	 * Moves armies from one territory to another one in fortification phase.
	 * @param start moves armies from this territory.
	 * @param dest moves armies to this territory.
	 * @param armyNum the number of armies that are to be moved.
	 */
	public void fortify(Territory start, Territory dest, int armyNum) {
		moveArmy(start, dest, armyNum);
		
		if (hasConquered) {
			cards.getCard();
			hasConquered = false;
		}
	}
	

	/**
	 * Adds a new territory.
	 * @param territory the territory that is to be added.
	 */
	public void addTerritory(Territory territory) {
		territoryMap.put(territory.getName(), territory);
		totalArmy += territory.getArmy();
		ownedTerritoryNum++;
		updateControlledContinent();
	}
	
	/**
	 * Removes a territory.
	 * @param territory the territory that is to be removed.
	 */
	public void removeTerritory(Territory territory) {
		totalArmy -= territory.getArmy();
		territoryMap.remove(territory.getName());
		ownedTerritoryNum--;
		updateControlledContinent();
	}
	
	/**
	 * Returns the territory based on given coordinates.
	 * @param x x coordinate of the territory.
	 * @param y y coordinate of the territory.
	 * @return the territory with given x and y coordinates.
	 */
	public Territory getTerritory(int x, int y) {
		for (Territory territory : territoryMap.values()) {
			if (territory.getShape().contains(new Point(x, y))) {
				return territory;
			}
			
		}
		return null;
	}
	
	/**
	 * Returns the hash map contains all territory controlled by this player.
	 * @return territoryMap the territory hash map.
	 */
	public HashMap<String, Territory> getTerritoryMap() {
		return territoryMap;
	}
	
	/**
	 * Returns number of unassigned armies.
	 * @return number of unassigned armies. 
	 */
	public int getUnassignedArmy() {
		return unassignedArmy;
	}
	
	/**
	 * Returns the number of total armies.
	 * @return the number of total armies.
	 */
	public int getTotalArmy() {
		return totalArmy;
	}
	
	/**
	 * Returns the number of armies given by system in the reinforcement phase.
	 * @return the number of armies given by system in the reinforcement phase.
	 */
	public int getFreeArmy() {
		return freeArmy;
	}
	
	/**
	 * Returns the hash map contains the controlled continents.
	 * @return the hash map contains the controlled continents.
	 */
	public HashMap<Continent, Boolean> getControlledContinent() {
		return controlledContinent;
	}
	
	/**
	 * Removes armies from a territory.
	 * @param territory the territory which removes some armies.
	 * @param armyNum the number of armies that are to be removed from the given territory.
	 */
	public void removeArmy(String territory, int armyNum) {
		territoryMap.get(territory).removeArmy(armyNum);
		totalArmy -= armyNum;
	}
	
	/**
	 * Moves armies from one territory to another territory.
	 * @param start moves armies from this territory.
	 * @param dest moves armies to this territory.
	 * @param armyNum number of armies to be moved.
	 */
	public void moveArmy(Territory start, Territory dest, int armyNum) {
		start.removeArmy(armyNum);
		dest.addArmy(armyNum);
		updateAttackableMap();
	}
	
	
	/**
	 * Exchange hand cards and receives bonus armies.
	 * @param exchangeCards cards that are to be exchanged.
	 * @param exchangeBonusArmy the number of armies assigned to this player.
	 */
	public void exchangeCards(LinkedList<Integer> exchangeCards, int exchangeBonusArmy) {
		cards.removeCards(exchangeCards);
		totalArmy += exchangeBonusArmy;
		unassignedArmy += exchangeBonusArmy;
	}
	
	/**
	 * Returns player's hand cards.
	 * @return player's hand cards.
	 */
	public Cards getCardSet() {
		return cards;
	}
	
	/**
	 * Gets a new card.
	 */
	public void getNewCard() {
		if (hasConquered) {
			cards.getCard();
		}
		hasConquered = false;
	}
	
	/**
	 * Rolls some dice and returns the result.
	 * @param diceNum the number of dice.
	 */
	private void rollDice(int diceNum) {
		dice.clear();
		Random r = new Random();
		for (int i = 0; i < diceNum; i++) {
			dice.add(r.nextInt(6) + 1);
		}
		
	}
	
	/**
	 * Returns results of dice rolling by attacker.
	 * @return results of dice rolling by attacker.
	 */
	public PriorityQueue<Integer> getDice() {
		return dice;
	}
	
	/**
	 * Returns a random territory from a list of territories.
	 * @param territories a list of territories.
	 * @return a random territory from a list of territories.
	 */
	public Territory getRandomTerritory(ArrayList<Territory> territories) {
		Random r = new Random();
		if (territories.size() == 0) {
			return null;
		}
		
		return territories.get(r.nextInt(territories.size()));
	}
	
	/**
	 * Returns a random territory.
	 * @return a random territory.
	 */
	public Territory getRandomTerritory() {
		ArrayList<Territory> territoryArray = new ArrayList<Territory> (territoryMap.values());
		Random r = new Random();
		
		return territoryArray.get(r.nextInt(territoryArray.size()));
	}
	
	/**
	 * Returns the strongest territory of the player.
	 * @return the strongest territory of the player.
	 */
	public Territory getStrongestTerritory() {
		Territory strongestTerritory = null;
		
		for (Territory territory : territoryMap.values()) {
			if (strongestTerritory == null) {
				strongestTerritory = territory;
			}
			
			if (territory.getArmy() > strongestTerritory.getArmy()) {
				strongestTerritory = territory;
			}
		}
		
		
		return strongestTerritory;
	}
	
	/**
	 * Returns the weakest territory of the player.
	 * @return the weakest territory of the player.
	 */
	public Territory getWeakestTerritory() {
		Territory weakestTerritory = null;
		
		for (Territory territory : territoryMap.values()) {
			if (weakestTerritory == null) {
				weakestTerritory = territory;
			}
			
			if (territory.getArmy() < weakestTerritory.getArmy()) {
				weakestTerritory = territory;
			}
		}
		
		
		return weakestTerritory;
	}
	
}
