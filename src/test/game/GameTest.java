package test.game;

import static org.junit.Assert.*;

import java.awt.Point;
import java.nio.file.Paths;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.controller.ReadFileController;
import risk.game.Game;
import risk.game.Player;
import risk.game.RiskMap;
import risk.game.Territory;
import risk.game.strategy.Strategy.Behavior;

public class GameTest {

	private Game game;
	
	// Before test.
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test Game.");
	}
	
	// Before each test case
	@Before
	public void before() {
		game = new Game();
	}
	
	// Test getting the current player.
	@Test
	public void currentPlayerTest() {
		String rootDir = (Paths.get("").toAbsolutePath().toString());
		ReadFileController controller = new ReadFileController();
		RiskMap map = new RiskMap();
		
		try {
			map = controller.readFile(rootDir + "\\src\\test\\Africa.map");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		LinkedList<String> behaviors = new LinkedList<String>();
		behaviors.add("Human");
		behaviors.add("Human");
		behaviors.add("Human");
		game.setMap(map);
		game.setPlayers(behaviors);
		game.distributeTerritories();
		
		game.start();
		assertEquals("player1", game.getCurrentPlayer().getName());
		game.nextPhase();
		assertEquals("player2", game.getCurrentPlayer().getName());
		game.nextPhase();
		assertEquals("player3", game.getCurrentPlayer().getName());
		game.nextPhase();
		assertEquals("player1", game.getCurrentPlayer().getName());

		System.out.println("Succeesfully test getCurrerentPlayer.");
	}
	
	// Test attacker and defender validation.
	@Test
	public void attackerDefenderTest() {
		Territory attacker = new Territory("attacker", new Point(0, 0));
		Territory defender = new Territory("defender", new Point(0, 1));
		
		game.setupAttack(attacker, defender);
		assertEquals(attacker, game.getAttacker());
		assertEquals(defender, game.getDefender());
		System.out.println("Succeesfully test attacker and defender validation.");
	}
	
	// Test the end of the game.
	@Test
	public void endGameTest() {
		String rootDir = (Paths.get("").toAbsolutePath().toString());
		ReadFileController controller = new ReadFileController();
		RiskMap map = new RiskMap();
		
		try {
			map = controller.readFile(rootDir + "\\src\\test\\Africa.map");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		LinkedList<String> behaviors = new LinkedList<String>();
		behaviors.add("Cheater");
		behaviors.add("Aggressive");
	
		game.setMap(map);
		game.setPlayers(behaviors);
		game.distributeTerritories();
		game.start();
		assertEquals(Behavior.CHEATER, game.getWinner().getStrategy().getBehavior());
		System.out.println("Succeesfully test the end of the game.");
	}
	
	// Test the tournament mode.
	// Maps: Africa.map, 3D Cliff.map
	// Players: Cheater, Benevolent, Random
	// Game number: 3
	// Max Turns: 50
	@Test
	public void tournamentModeTest() {
		String rootDir = (Paths.get("").toAbsolutePath().toString());
		LinkedList<String> playerBehaviors = new LinkedList<String>();
		LinkedList<String> mapPathList = new LinkedList<String>();
		int gameNum = 3;
		int maxTurnNum = 50;

		playerBehaviors.add("Cheater");
		playerBehaviors.add("Benevolent");
		playerBehaviors.add("Random");
		
		mapPathList.add(rootDir +"\\src\\test\\Africa.map.");
		mapPathList.add(rootDir +"\\src\\test\\3D Cliff.map.");
		String[][] result = new String[mapPathList.size()][gameNum];

		for (int i = 0; i < mapPathList.size(); i++) {
			ReadFileController controller = new ReadFileController();
			RiskMap map = null;
			try {
				map = controller.readFile(mapPathList.get(i));
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(null, exception.getMessage());
				exception.printStackTrace();
				return;
			}
			
			for (int j = 0; j < gameNum; j++) {
				map.reset();
				Game game = new Game();
				game.setMap(map);
				game.setMaxTurn(maxTurnNum);
				game.setPlayers(playerBehaviors);
				game.distributeTerritories();
				game.start();
				
				Player winner = game.getWinner();
				if (winner == null) {
					result[i][j] = "DRAW";
				}
				else {
					result[i][j] = winner.getStrategy().getBehavior().toString();
				}
			}
		}
		
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				assertEquals("CHEATER", result[i][j]);
			}
		}
		System.out.println("Succeesfully test the tournament mode.");
	}
	
	// After test
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing Game.");
		System.out.println();
	}
}
