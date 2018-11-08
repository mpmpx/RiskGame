package risk.game;

import java.util.LinkedList;
import java.util.Observable;
/**
 * This class is showing the current phase of conquest game and current player
 */
public class Phase extends Observable {

	public final static int STARTUP = -1;
	public final static int REINFORCEMENT = 0;
	public final static int ATTACK = 1;
	public final static int FORTIFICATION = 2;
	
	private int currentPhase;
	private Player currentPlayer;
	private Player[] players;
	private int playerIndex; 
	private int exchangeBonusArmy;
	
	private Territory attacker;
	private Territory defender;
	
	/**
	 * Initialize the contents of the phase.
	 */
	public void initialize() {
		playerIndex = 0;
		exchangeBonusArmy = 5;
		currentPhase = STARTUP;
		currentPlayer = players[playerIndex];
		attacker = null;
		defender = null;
		setChanged();
		notifyObservers();
	}

	/**
	 * Method to get into next Phase of the game.
	 */
	public void nextPhase() {
		
		// During startup cycle.
		if (currentPhase == STARTUP && playerIndex % players.length != players.length - 1) {
			playerIndex = (playerIndex + 1) % players.length;
			currentPlayer = players[playerIndex];
		}
		// During game cycle.
		else {
			if (currentPhase == FORTIFICATION || currentPhase == STARTUP) {
				playerIndex = (playerIndex + 1) % players.length;
				currentPlayer = players[playerIndex];
				currentPlayer.getCard();
			}
		
			currentPhase = (currentPhase + 1) % 3;
		}
		
		switch (currentPhase) {
			case REINFORCEMENT : {
				currentPlayer.reinforcement();
				break;
			}
			case ATTACK : {
				currentPlayer.attack();
				break;
			}
			case FORTIFICATION : {
				currentPlayer.fortification();
				break;
			}
		}
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Method to add players.
	 * 
	 */
	public void addPlayers(Player[] players) {
		this.players = players;
	}
	
	/**
	 * Method to get current phase when players in the game
	 * 
	 * @return currentPhase
	 */
	public int getCurrentPhase() {
		return currentPhase;
	}
	
	/**
	 * Method to get current players in the game
	 * 
	 * @return current player
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Method to get all players
	 * 
	 * @return players
	 */
	public Player[] getPlayers() {
		return players;
	}
	
	/**
	 * To set attack
	 * 
	 * @param attacker
	 *            indicate whether the player is an attacker during this game
	 * @param defender
	 *            indicate  whether the player is a defender during this game
	 */
	public void setAttack(Territory attacker, Territory defender) {
		this.attacker = attacker;
		this.defender = defender;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * To set attacker
	 * 
	 * @param attacker
	 *            indicate the player is an attacker during this game
	 */
	public void setAttacker(Territory attacker) {
		this.attacker = attacker;
	}
	
	/**
	 * Method to get get attacker in this territory
	 * 
	 * @return attacker
	 */
	public Territory getAttacker() {
		return attacker;
	}
	
	/**
	 * To set defender
	 * 
	 * @param defender
	 *            indicate the player is an defender during this game
	 */
	public void setDefender(Territory defender) {
		this.defender = defender;
	}
	
	/**
	 * Method to get get defender in this territory
	 * 
	 * @return defender
	 */
	public Territory getDefender() {
		return defender;
	}
	
	/**
	 * Method to add army in certain territory
	 * @param territory
	 *            indicate the territory that needs to add army
	 * @param army
	 *            indicate the current player 
	 */
	public void addArmy(String territory, int army) {
		currentPlayer.addArmy(territory, army);
		setChanged();
		notifyObservers();
		
		if (currentPlayer.getUnassignedArmy() == 0 && (currentPhase == STARTUP || currentPhase == REINFORCEMENT)) {
			nextPhase();
		}
	}
	
	/**
	 * Method to fortify army 
	 * @param start
	 * @param dest
	 * @param army
	 */
	public void fortify(String start, String dest, int army) {
		currentPlayer.addArmy(dest, army);
		currentPlayer.removeArmy(start, army);
		nextPhase();
	}
	
	/**
	 * Method to set the result of an attack
	 * @param attackerCasulties
	 * @param defenderCasulties
	 */
	public void setAttackResult(int attackerCasulties, int defenderCasulties) {
		for (int i = 0; i < players.length; i++) {
			if (players[i].getColor().equals(defender.getOwner())) {
				players[i].killArmy(defender.getName(), defenderCasulties);
				break;
			}
		}
		
		currentPlayer.killArmy(attacker.getName(), attackerCasulties);
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Method to conquer a territory
	 * @param army
	 *		the number of army during conquer
	 */
	public void conquerTerritory(int army) {
		currentPlayer.conquer();
		
		for (int i = 0; i < players.length; i++) {
			if (players[i].getColor().equals(defender.getOwner())) {
				players[i].removeTerritory(defender);
				players[i].attack();
				
				if (players[i].getTotalArmy() == 0) {
					Player[] newPlayers = new Player[players.length - 1];
					if (playerIndex > i) {
						playerIndex--;
					}
					
					for (int j = 0; j < i; j++) {
						newPlayers[j] = players[j];
					}
					
					for (int j = i + 1; j < players.length; j++) {
						newPlayers[j - 1] = players[j];
					}
					players = newPlayers;
					
				}
				
				break;
			}
		}
		
		attacker.removeArmy(army);
		defender.setArmy(army);
		defender.setOwner(currentPlayer.getColor());
		RiskMap.getInstance().updateTerritory(defender);
		currentPlayer.addTerritory(defender);
		currentPlayer.attack();
		attacker = null;
		defender = null;
		setChanged();
		notifyObservers();

		checkAttackPhase();
	}
	
	/**
	 * Method to check whether it is an attack phase.
	 * @return true if the current phase is attack phase
	 * 	and return false if it is not.
	 */
	public boolean checkAttackPhase() {		
		if (currentPhase == Phase.ATTACK) {
			for (Territory territory : currentPlayer.getTerritoryMap().values()) {
				if (territory.getArmy() > 1) {
					if (!currentPlayer.getAttackableMap().get(territory.getName()).isEmpty()) {
						return true;
					}
				}
			}
		
			nextPhase();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Method to exchange cards
	 * @param cards
	 */
	public void exchangeCards(LinkedList<Integer> cards) {
		currentPlayer.addExchangeBonusArmy(exchangeBonusArmy);
		exchangeBonusArmy += 5;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Method to exchange BonusArmy 
	 * @param exchangeBonusArmy
	 */
	public int getExchangeBonusArmy() {
		return exchangeBonusArmy;
	}
}
