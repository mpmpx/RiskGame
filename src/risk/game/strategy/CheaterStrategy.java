package risk.game.strategy;

import java.io.Serializable;
import java.util.LinkedList;

import risk.game.Player;
import risk.game.Territory;
/**
 * The Cheater Strategy that implements strategy and serializable operation
 */
public class CheaterStrategy implements Strategy, Serializable {
	
	private Player player;
	private Behavior behavior;
	/**
 	 * Constructor for CheaterStrategy
 	 * @param player
 	 *            the desired player the user want to use
 	 */
	public CheaterStrategy(Player player) {
		this.player = player;
		behavior = Behavior.CHEATER;
	}
	/**
	 * Method to startup
	 */
	@Override
	public void startup() {
		while (player.getUnassignedArmy() != 0) {
			Territory randomTerritory = player.getRandomTerritory();
			player.placeUnassignedArmy(randomTerritory, 1);
		}
	}
	/**
	 * Method to reinforce.
	 */
	@Override
	public void reinforce() {
		for (Territory territory : player.getTerritoryMap().values()) {
			player.setUnsignedArmy(territory.getArmy());
			player.placeUnassignedArmy(territory, player.getUnassignedArmy());
		}
	}
	/**
	 * Method to attack
	 */
	@Override
	public void attack() {
		player.updateAttackableMap();
		
		for (LinkedList<Territory> attackableList : player.getAttackableMap().values()) {
			for (Territory territory : attackableList) {
				if (territory.getOwner() != player) {
					territory.getOwner().removeTerritory(territory);
					territory.setOwner(player);
					territory.setArmy(1);
					territory.setColor(player.getColor());
					player.addTerritory(territory);
				}
			}
		}
	}
	/**
	 * Method to fortify
	 */
	@Override
	public void fortify() {
		player.updateAttackableMap();
		for (Territory territory : player.getTerritoryMap().values()) {
			for (Territory adjacent : territory.getAdjacent().values()) {
				if (adjacent.getOwner() != player) {
					player.setUnsignedArmy(territory.getArmy()); 
					player.placeUnassignedArmy(territory, player.getUnassignedArmy());
					continue;
				}
			}
		}
	}
	/**
  	* Method to get its behavior
 	 * 
 	 * @return the behavior in Behavior type
 	 */
	@Override
	public Behavior getBehavior() {
		return behavior;
	}

}
