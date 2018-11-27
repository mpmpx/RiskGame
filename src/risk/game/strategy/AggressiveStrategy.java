package risk.game.strategy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import risk.game.Cards;
import risk.game.Player;
import risk.game.Territory;
/**
 * An Aggressive Strategy that implements strategy and serializable operation
 */
public class AggressiveStrategy implements Strategy , Serializable{

	private Player player;
	private Behavior behavior;
	/**
	 * Constructor of AggressiveStratege
	 * @param Player 
	 *		the player to use strategy
	 */
	public AggressiveStrategy(Player player) {
		this.player = player;
		behavior = Behavior.AGGRESSIVE;
	}
	/**
	 * Method to startup 
	 */
	@Override
	public void startup() {
		for (Territory territory : player.getTerritoryMap().values()) {
			for (Territory adj : territory.getAdjacent().values()) {
				if (adj.getOwner() != player) {
					player.placeUnassignedArmy(territory, player.getUnassignedArmy());
					return;
				}
			}
		}
		
	}
	/**
	 * Method to reinforce.
	 */
	@Override
	public void reinforce() {
		Territory strongestTerritory = player.getStrongestTerritory();
		Cards handCard = player.getCardSet();
		
		if (handCard.getSize() == 5) {
			LinkedList<Integer> exchangeCards = new LinkedList<Integer>();
			for (int i = 0; i < 3; i++) {
				exchangeCards.add(handCard.getAllCards().get(i));
			}
			
			player.exchangeCards(exchangeCards, Cards.exchangeArmy);
			Cards.exchangeArmy += 5;
		}
		
		player.getReinforcement();			
		try {
			player.placeUnassignedArmy(strongestTerritory, player.getUnassignedArmy());
	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Method to attack.
	 */
	@Override
	public void attack() {
		Territory strongestTerritory = player.getStrongestTerritory();
		player.updateAttackableMap();
		
		while (!player.getAttackableMap().get(strongestTerritory).isEmpty()) {
			Random r = new Random();
			LinkedList<Territory>attackableList = player.getAttackableMap().get(strongestTerritory);
			Territory targetTerritory = attackableList.get(r.nextInt(attackableList.size()));
			
			
			
			while (targetTerritory.getOwner() != player && strongestTerritory.getArmy() > 1) {
				int attackerDiceNum = Math.min(3, strongestTerritory.getArmy() - 1);
				int defenderDiceNum = Math.min(2, targetTerritory.getArmy());
				player.attack(strongestTerritory, targetTerritory, attackerDiceNum, defenderDiceNum);
				
				if (targetTerritory.getArmy() == 0) {
					player.moveArmy(strongestTerritory, targetTerritory, strongestTerritory.getArmy() - 1);
					strongestTerritory = targetTerritory;
				}
			}
			
			player.updateAttackableMap();
		}
		
	}
	/**
	 * Method to fortify
	 */
	@Override
	public void fortify() {
		player.updateReachableMap();
		HashMap<Territory, LinkedList<Territory>> reachableMap = player.getReachableMap();
		Territory strongestTerritory = player.getStrongestTerritory();
		Territory fortificationTerritory = null;

		
		for (Territory territory : reachableMap.keySet()) {
			if (reachableMap.get(territory).contains(strongestTerritory)) {
				if (fortificationTerritory == null) {
					fortificationTerritory = territory;
				}
				
				if (fortificationTerritory.getArmy() < territory.getArmy()) {
					fortificationTerritory = territory;
				}
			}
		}
		
		if (fortificationTerritory != null) {
			player.moveArmy(fortificationTerritory, strongestTerritory, fortificationTerritory.getArmy() - 1);
		}
		
	}
	
	/**
	 * Method to get its behavior
	 * 
	 * @return the behavior that got which is Behavior type
	 */
	@Override
	public Behavior getBehavior() {
		return behavior;
	}

}
