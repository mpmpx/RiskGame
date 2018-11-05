
package risk.game;

import java.util.LinkedList;
import java.util.Observable;

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
	
	public void addPlayers(Player[] players) {
		this.players = players;
	}
	
	public int getCurrentPhase() {
		return currentPhase;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	public void setAttack(Territory attacker, Territory defender) {
		this.attacker = attacker;
		this.defender = defender;
		setChanged();
		notifyObservers();
	}
	
	public void setAttacker(Territory attacker) {
		this.attacker = attacker;
	}
	
	public Territory getAttacker() {
		return attacker;
	}
	
	public void setDefender(Territory defender) {
		this.defender = defender;
	}
	
	public Territory getDefender() {
		return defender;
	}
	
	public void addArmy(String territory, int army) {
		currentPlayer.addArmy(territory, army);
		setChanged();
		notifyObservers();
		
		if (currentPlayer.getUnassignedArmy() == 0 && (currentPhase == STARTUP || currentPhase == REINFORCEMENT)) {
			nextPhase();
		}
	}
	
	public void fortify(String start, String dest, int army) {
		currentPlayer.addArmy(dest, army);
		currentPlayer.removeArmy(start, army);
		nextPhase();
	}
	
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
	
	public void exchangeCards(LinkedList<Integer> cards) {
		//currentPlayer.getCardSet().removeCards(cards);
		currentPlayer.addExchangeBonusArmy(exchangeBonusArmy);
		exchangeBonusArmy += 5;
		setChanged();
		notifyObservers();
	}
	
	public int getExchangeBonusArmy() {
		return exchangeBonusArmy;
	}
}