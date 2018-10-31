package risk.game;

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
	
	public void initialize() {
		playerIndex = 0;
		currentPhase = STARTUP;
		currentPlayer = players[playerIndex];
		setChanged();
		notifyObservers();
	}

	
	public void nextPhase() {
		
		// During startup cycle.
		if (currentPhase == STARTUP && playerIndex % players.length != players.length - 1) {
			currentPlayer = players[(++playerIndex) % players.length];
		}
		// During game cycle.
		else {
			if (currentPhase == FORTIFICATION || currentPhase == STARTUP) {
				currentPlayer = players[(++playerIndex) % players.length];
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
	
	public void addArmy(String territory, int army) {
		currentPlayer.addArmy(territory, army);
		setChanged();
		notifyObservers();
		
		if (currentPlayer.getUnassignedArmy() == 0) {
			nextPhase();
		}
	}
	
	public void fortify(String start, String dest, int army) {
		currentPlayer.addArmy(dest, army);
		currentPlayer.removeArmy(start, army);
		nextPhase();
	}
	
}
