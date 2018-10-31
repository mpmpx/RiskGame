package risk.controller;

import java.util.HashMap;
import java.util.Random;

import risk.game.*;

public class GameController {

	private static GameController controller;
	
	private Player[] players;
	private Player currentPlayer;
	private Phase phase;
	private RiskMap map;
	private HashMap<String, Territory> territoryMap;
	
	public static GameController getInstance() {
		if (controller == null) {
			controller = new GameController();
		}
		return controller;
	}
	
	private GameController() {
		players = null;
		currentPlayer = null;
		phase = null;
		map = null;
		territoryMap = new HashMap<String, Territory>();
	}
	
	public void initailize(int numPlayer) {
		map = RiskMap.getInstance();
		territoryMap = map.getTerritoryMap();
		players = new Player[numPlayer];
		for (int i = 0; i < numPlayer; i++) {
			players[i] = new Player("player" + (i + 1));
		}
		
		for (Territory territory : territoryMap.values()) {
			Random r = new Random();
			Player player = players[r.nextInt(players.length)];
			territory.setOwner(player.getColor());
			player.addTerritory(territory);
		}
		setInitialArmy(numPlayer);
		
		phase = new Phase();
		phase.addPlayers(players);
		phase.addObserver(GUIController.getInstance().getPhaseView());
		phase.addObserver(GUIController.getInstance().getMapDisplayPanel());
		phase.initialize();	
	}
	
	private void setInitialArmy(int numPlayer) {

		for (int i = 0; i < numPlayer; i++) {
			switch (numPlayer) {
				case 2 : {
					players[i].setUnsignedArmy(40);
					break;
				}
				case 3 : {
					players[i].setUnsignedArmy(35);
					break;
				}
				case 4 : {
					players[i].setUnsignedArmy(30);
					break;
				}
				case 5 : {
					players[i].setUnsignedArmy(25);
					break;
				}
				case 6 : {
					players[i].setUnsignedArmy(20);
					break;
				}
			}			
		}

	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void nextPhase() {
		phase.nextPhase();
	}
	
	public void fortify(Territory start, Territory dest, int army) {
		phase.fortify(start.getName(), dest.getName(), army);
	}
	
	public void setArmy(Territory territory, int army) {
		phase.addArmy(territory.getName(), army);
	}
	
}
