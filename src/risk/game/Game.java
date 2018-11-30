package risk.game;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Observable;
import java.util.PriorityQueue;
import java.util.Random;

import risk.game.strategy.*;
import risk.game.strategy.Strategy;
import risk.game.strategy.Strategy.Behavior;

/**
 * The Game class simulates game process of the Risk game. It maintains status of game including 
 * current playing phase and player, and simulates game process and players' moves.
 */
public class Game extends Observable implements Serializable{
 	
	private Phase phase;
	private int playerNum;
	private Player[] players;
	private Player currentPlayer;
	private LinkedList<Player> playerQueue;
	private RiskMap map;
	private int exchangeBonusArmy;
	private Territory attacker;
	private Territory defender;
	private Player winner;
	private int maxTurn;
	private int turn;
	
	private PriorityQueue<Integer> attackerDice;
	private PriorityQueue<Integer> defenderDice;
	
	/**
	 * Creates a game with initial exchange bonus armies with 5.
	 */
	public Game() {
		playerNum = 0;
		exchangeBonusArmy = 5;
		maxTurn = -1;
		turn = 0;
		playerQueue = new LinkedList<Player>();
		attackerDice = new PriorityQueue<Integer>(Collections.reverseOrder());
		defenderDice = new PriorityQueue<Integer>(Collections.reverseOrder());
	}
	

	/**
	 * Sets players to the game with player's behaviors.
	 * @param behaviors a list of behaviors of players.
	 */
	public void setPlayers (LinkedList<String> behaviors) {
		playerNum = behaviors.size();
		players = new Player[playerNum];

		
		for (int i = 0; i < playerNum; i++) {
			Strategy strategy = null;
			players[i] = new Player("player" + (i + 1));
			switch (behaviors.get(i)) {
				case "Human" : {
					strategy = new HumanStrategy(players[i]);
					break;
				}
				case "Aggressive": {
					strategy = new AggressiveStrategy(players[i]);
					break;
				}
				case "Benevolent": {
					strategy = new BenevolentStrategy(players[i]);
					break;
				}
				case "Random": {
					strategy = new RandomStrategy(players[i]);
					break;
				}
				case "Cheater": {
					strategy = new CheaterStrategy(players[i]);
					break;
				}
			}
			
			players[i].setStrategy(strategy);
			playerQueue.add(players[i]);
			
			LinkedList<Continent> continentList = new LinkedList<Continent>();
			for (Continent continent : map.getContinentMap().values()) {
				continentList.add(continent);
			}
			players[i].setContinents(continentList);
			
		}
		
		currentPlayer = players[0];
	}
	
	/**
	 * Sets the maximum turns of this game.
	 * @param maxTurn a number that is to be the maximum turns of this game.
	 */
	public void setMaxTurn(int maxTurn) {
		this.maxTurn = maxTurn;
	}
	
	/**
	 * Returns number of players of this game.
	 * @return number of players of this game.
	 */
	public int getPlayerNum () {
		return playerNum;
	}
	
	/**
	 * Returns all players of this game.
	 * @return an array contains all players of this game.
	 */
	public Player[] getPlayers() {
		return players;
	}
	
	/**
	 * Sets the map of this game.
	 * @param map a map that is to be the map of this game.
	 */
	public void setMap(RiskMap map) {
		this.map = map;
	}
	
	/**
	 * Returns the map of this game.
	 * @return the map of this game.
	 */
	public RiskMap getMap() {
		return map;
	}
	
	/**
	 * Returns the winner of the game.
	 * @return the winner of the game.
	 */
	public Player getWinner() {
		return winner;
	}
	
	/**
	 * Randomly distributes all territories to players. 
	 */
	public void distributeTerritories() {
		for (Territory territory : map.getTerritoryMap().values()) {
			Random r = new Random();
			Player player = players[r.nextInt(playerNum)];
			territory.setColor(player.getColor());
			territory.setOwner(player);
			territory.setArmy(1);
			player.addTerritory(territory);
		}
	}
	
	/**
	 * Starts the game. Create the phase and assign initial armies to players.
	 */
	public void start() {
		phase = new Phase();
		setInitialArmy();
		
		if (currentPlayer.getStrategy().getBehavior() != Behavior.HUMAN) {
			AIMove();
		}
	}
	
	/**
	 * Sets initial armies to players. Number of initial armies is based on
	 * number of players.
	 */
	public void setInitialArmy() {
		for (int i = 0; i < playerNum; i++) {
			switch (playerNum) {
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
				default : {
					break;
				}
			}			
		}
		update();
	}
	
	/**
	 * Proceeds game to next phase.
	 */
	public void nextPhase() {		
		switch (phase.getCurrentPhase()) {
			case Phase.STARTUP : {
				currentPlayer = nextPlayer();
				if (currentPlayer == players[0]) {
					phase.nextPhase();
					turn++;
				}
				break;
			}
			case Phase.REINFORCEMENT : {
				phase.nextPhase();
				break;
			}
			case Phase.ATTACK : {
				attackerDice.clear();
				defenderDice.clear();
				phase.nextPhase();
				break;
			}
			case Phase.FORTIFICATION : {
				currentPlayer.getNewCard();
				phase.nextPhase();
				currentPlayer = nextPlayer();
				turn++;
				
				break;
			}
			default : {
				break;
			}
		}
		
		if (turn == maxTurn) {
			return;
		}
		
		if (currentPlayer.getStrategy().getBehavior() == Behavior.HUMAN) {
			playerMove();
		}
		else {
			AIMove();
		}
		
	}
	
	/**
	 * Set the status of this observable class as changed and notify
	 * its observers.
	 */
	private void update() {
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Executes methods when it is turn to a human player.
	 */
	private void playerMove() {
		switch (phase.getCurrentPhase()) {
			case Phase.REINFORCEMENT : {
				currentPlayer.getReinforcement();
				break;
			}
			case Phase.ATTACK : {
				currentPlayer.updateAttackableMap();
				break;
			}
			case Phase.FORTIFICATION : {
				currentPlayer.updateReachableMap();
				break;
			}
			default : {
				break;
			}
		}
		update();
	}
	
	/**
	 * Executes methods when it is turn to an AI player.
	 */
	private void AIMove() {
		switch (phase.getCurrentPhase()) {
			case Phase.STARTUP : {
				currentPlayer.startup();
				break;
			}
			case Phase.REINFORCEMENT : {
				currentPlayer.reinforcement();
				break;
			}
			case Phase.ATTACK : {
				currentPlayer.attack();
				
				for (int i = 0; i < players.length; i++) {
					if (players[i].getTotalArmy() == 0) {
						if (playerQueue.contains(players[i])) {
							playerNum--;
							playerQueue.remove(players[i]);
						}
					}
				}
				
				
				if (playerQueue.size() == 1) {
					winner = currentPlayer;
					update();
					return;
				}
				
				break;
		}
			case Phase.FORTIFICATION : {
				currentPlayer.fortification();
				break;
			}
		}
		
		update();
		nextPhase();
	}
	
	/**
	 * Returns the current phase.
	 * @return the current phase.
	 */
	public int getCurrentPhase() {
		return phase.getCurrentPhase();
	}	
	
	/**
	 * Proceeds to next player's turn.
	 * @return
	 */
	private Player nextPlayer() {
		Player player = playerQueue.pop();
		playerQueue.add(player);
		return playerQueue.peek();
	}
	
	/**
	 * Returns the current player.
	 * @return the current player.
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Reinforces given number of armies to given territory of the current player.
	 * @param territory the territory that is to be reinforced.
	 * @param armyNum the number of armies to be added to a territory.
	 */
	public void placeUnassignedArmy(Territory territory, int armyNum) {
		currentPlayer.placeUnassignedArmy(territory, armyNum);
		if (currentPlayer.getUnassignedArmy() == 0) {
			nextPhase();
		}
		update();
	}
	
	/**
	 * Sets up an attack with attacker and defender.
	 * @param attacker the territory commits to attack.
	 * @param defender the territory defends itself from an attack.
	 */
	public void setupAttack(Territory attacker, Territory defender) {
		this.attacker = attacker;
		this.defender = defender;
		update();
	}
	
	/**
	 * Returns attacking territory.
	 * @return attacking territory.
	 */
	public Territory getAttacker() {
		return attacker;
	}
	
	/**
	 * Returns defending territory.
	 * @return defending territory.
	 */
	public Territory getDefender() {
		return defender;
	}
	
	/**
	 * Checks whether the current player is able to commit to attack.
	 * @return true if the current player is able to commit to attack.
	 * Otherwise, false.
	 */
	public boolean checkAttackPhase() {
		for (LinkedList<Territory> attackableList : currentPlayer.getAttackableMap().values()) {
			if (!attackableList.isEmpty()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Attacking territory attacks defending territory. 
	 * @param attackerDiceNum number of dice used by attacker.
	 * @param defenderDiceNum number of dice used by defender.
	 */
	public void attack(int attackerDiceNum, int defenderDiceNum) {
		Player defendingPlayer= defender.getOwner();
		currentPlayer = attacker.getOwner();
		
		currentPlayer.attack(attacker, defender, attackerDiceNum, defenderDiceNum);
		attackerDice = currentPlayer.getDice();
		defenderDice = defendingPlayer.getDice();
		
		if (defender.getOwner() == currentPlayer) {
			attacker = null;
			defender = null;
		} else {
			if (checkAttackPhase() == false) {
				nextPhase();
				return;
			}
		}
		
		if (defendingPlayer.getTotalArmy() == 0) {
			playerQueue.remove(defendingPlayer);
			playerNum--;
		}
		
		update();
		
		if (players.length == 1) {
			winner = currentPlayer;
		}
	}
	


	/**
	 * Returns results of dice rolling by attacker.
	 * @return results of dice rolling by attacker.
	 */
	public PriorityQueue<Integer> getAttackerDice() {
		return attackerDice;
	}
	
	/**
	 * Returns results of dice rolling by defender.
	 * @return results of dice rolling by defender.
	 */
	public PriorityQueue<Integer> getDefenderDice() {
		return defenderDice;
	}
	

	
	/**
	 * Moves armies from the attacking territory to the recently conquered territory.
	 * @param attacker the attacking territory.
	 * @param defender the recently conquered territory.
	 * @param armyNum number of armies that is to be moved from the attacking territory
	 * to the conquered territory.
	 */
	public void conquer(Territory attacker, Territory defender, int armyNum) {
		currentPlayer.moveArmy(attacker, defender, armyNum);		
		update();
		
		if (checkAttackPhase() == false) {
			nextPhase();
		}
	}
	
	/**
	 * The current player moves armies from one territory to another territory.
	 * @param departureTerritory moves armies from this territory.
	 * @param arrivalTerritory moves armies to this territory.
	 * @param armyNum number of armies that is to be moved.
	 */
	public void fortify(Territory departureTerritory, Territory arrivalTerritory, int armyNum) {
		currentPlayer.fortify(departureTerritory, arrivalTerritory, armyNum);
		update();
	}
	

	
	/**
	 * The current player exchanges a set of cards and receives bonus armies.
	 * @param cards a set of hand cards that are to be exchanged.
	 */
	public void exchangeCards(LinkedList<Integer> cards) {
		currentPlayer.exchangeCards(cards, exchangeBonusArmy);
		exchangeBonusArmy += 5;
		update();
	}
	
	/**
	 * Returns current number of exchange bonus armies.
	 * @return current number of exchange bonus armies.
	 */
	public int getExchangeBonusArmy() {
		return exchangeBonusArmy;
	}
	
	/**
	 * Load the game.
	 */
	public void load() {
		update();
	}
}
