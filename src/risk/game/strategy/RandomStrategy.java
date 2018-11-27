package risk.game.strategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import risk.game.Cards;
import risk.game.Player;
import risk.game.Territory;
/**
 * The Random Strategy that implements strategy and serializable operation
 */
public class RandomStrategy implements Strategy, Serializable {
	
	private Player player;
	private Behavior behavior;
	/**
 	 * Constructor for RandomStrategy
 	 * @param player
 	 *            the desired player the user want to use
 	 */
	public RandomStrategy(Player player) {
		this.player = player;
		behavior = Behavior.RANDOM;
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
		Random r = new Random();
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
		while (player.getUnassignedArmy() != 0) {
			Territory randomTerritory = player.getRandomTerritory();
			player.placeUnassignedArmy(randomTerritory, r.nextInt(player.getUnassignedArmy()) + 1);
		}
	}
	/**
	 * Method to attack.
	 */
	@Override
	public void attack() {
		player.updateAttackableMap();
		ArrayList<Territory> attackableTerritory = new ArrayList<Territory>();
		for (LinkedList<Territory> attackableList : player.getAttackableMap().values()) {
			for (Territory territory : attackableList) {
				if (!attackableTerritory.contains(territory)) {
					attackableTerritory.add(territory);
				}
			}
		}
		
		Territory randomTerritory = player.getRandomTerritory(attackableTerritory);
		Territory attacker = getAttacker(randomTerritory);
		while (attacker == null) {
			return;
		}
		
		Random r = new Random();
		int attackTimes = r.nextInt(5) + 1;
		
		while (randomTerritory.getOwner() != player && attacker.getArmy() > 1 && attackTimes-- > 0) {
			int attackerDiceNum = Math.min(3, attacker.getArmy() - 1);
			int defenderDiceNum = Math.min(2, randomTerritory.getArmy());
			player.attack(attacker, randomTerritory, attackerDiceNum, defenderDiceNum);
			if (randomTerritory.getArmy() == 0) {
				player.moveArmy(attacker, randomTerritory, attackerDiceNum);
			}			
		}
	}

	/**
	 * Method to fortify
	 */
	@Override
	public void fortify() {
		player.updateReachableMap();
		ArrayList<Territory> startTerritoryList = new ArrayList<Territory>();
		for (Territory territory : player.getReachableMap().keySet()) {
			if (!player.getReachableMap().get(territory).isEmpty()) {
				startTerritoryList.add(territory);
			}
		}
		
		Territory randomStartTerritory = player.getRandomTerritory(startTerritoryList);
		if (randomStartTerritory == null) {
			return;
		}
		
		Territory randomDestTerritory = player.getRandomTerritory(new ArrayList<Territory>(player.getReachableMap().get(randomStartTerritory)));
		Random r = new Random();
		
		player.moveArmy(randomStartTerritory, randomDestTerritory, r.nextInt(randomStartTerritory.getArmy() - 1) + 1);
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
	/**
	 * Method to get territory which can be attacked
	 * 
	 * @return the defender that got which is territory type
	 */
	private Territory getAttacker(Territory defender) {
		if (defender == null) {
			return null;
		}
		
		for (Territory territory : defender.getAdjacent().values()) {
			if (territory.getOwner() == player && territory.getArmy() > 1) {
				return territory;
			}
		}
		
		return null;
	}
}
