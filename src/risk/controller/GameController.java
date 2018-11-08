package risk.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import risk.game.*;
/**
 * Class acting as the Game's controller, 
 * to define action performed according to different users' action.
 */
public class GameController {

	private static GameController controller;
	
	private Player[] players;
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
		phase.addObserver(GUIController.getInstance().getCardExchangeView());
		phase.addObserver(GUIController.getInstance().getDominationView());
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
	
	public void nextPhase() {
		phase.nextPhase();
	}
	
	public void fortify(Territory start, Territory dest, int army) {
		phase.fortify(start.getName(), dest.getName(), army);
	}
	
	public void setArmy(Territory territory, int army) {
		phase.addArmy(territory.getName(), army);
	}
	
	public void setAttack(Territory attacker, Territory defender) {
		phase.setAttack(attacker, defender);
	}
	
	public void setAttackResult(int attackerCasulties, int defenderCasulties) {
		phase.setAttackResult(attackerCasulties, defenderCasulties);
	}
	
	public void conquerTerritory(int army) {
		phase.conquerTerritory(army);
		if (phase.getCurrentPlayer().getTerritoryMap().values().size() == RiskMap.getInstance().getTerritoryMap().values().size()) {
			GUIController.getInstance().win(phase.getCurrentPlayer());
		}
	}
	
	public boolean checkAttackPhase() {
		return phase.checkAttackPhase();	
	}	
	
	public void exchangeCards(LinkedList<Integer> cards) {
		phase.exchangeCards(cards);
	}
}
