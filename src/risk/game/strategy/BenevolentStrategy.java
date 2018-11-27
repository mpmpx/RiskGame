package risk.game.strategy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

import risk.game.Player;
import risk.game.Territory;
/**
 * The Benevolent Strategy that implements strategy and serializable operation
 */
public class BenevolentStrategy implements Strategy, Serializable {
	
	private Player player;
	private Behavior behavior;
	/**
	 * Constructor for BenevolentStrategy
	 * @param player
	 *            the desired player the user want to use
	 */
	public BenevolentStrategy(Player player) {
		this.player = player;
		behavior = Behavior.BENEVOLENT;
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
		Territory weakestTerritory = player.getWeakestTerritory();

		player.getReinforcement();				
		player.placeUnassignedArmy(weakestTerritory, player.getUnassignedArmy());
	}
	/**
	 * Method to attack.
	 */
	@Override
	public void attack() {
		return;
	}
	/**
	 * Method to fortify
	 */
	@Override
	public void fortify() {
		player.updateReachableMap();
		HashMap<Territory, LinkedList<Territory>> reachableMap = player.getReachableMap();
		Territory weakestTerritory = player.getWeakestTerritory();
		Territory fortificationTerritory = null;

		
		for (Territory territory : reachableMap.keySet()) {
			if (reachableMap.get(territory).contains(weakestTerritory)) {
				if (fortificationTerritory == null) {
					fortificationTerritory = territory;
				}
				
				if (fortificationTerritory.getArmy() < territory.getArmy()) {
					fortificationTerritory = territory;
				}
			}
		}
		
		if (fortificationTerritory != null) {
			player.moveArmy(fortificationTerritory,  weakestTerritory, fortificationTerritory.getArmy() - 1);
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
