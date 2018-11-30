package test.controller;

import static org.junit.Assert.*;

import java.awt.Point;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.controller.SaveLoadGameController;
import risk.game.Game;
import risk.game.Territory;

public class SaveLoadGameControllerTest {

	SaveLoadGameController controller;
	String rootDir;
	String fileName;
	Game game;
	Territory attacker, defender;
	
	// Before test.
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test SaveLoadGameController.");
	}
	
	// Before each test case	
	@Before
	public void before() {
		controller = new SaveLoadGameController();
		rootDir = (Paths.get("").toAbsolutePath().toString());
		fileName = rootDir + "\\src\\test\\Save.save";
		game = new Game();
		attacker = new Territory("attacker", new Point(0,0));
		defender = new Territory("defender", new Point(0,1));
		game.setupAttack(attacker, defender);
	}
	
	// Test saving and loading functions by save and load a game object.
	@Test
	public void testSaveLoad() {

		controller.saveGame(fileName, game);
		assertEquals(attacker.getName(), controller.loadGame(fileName).getAttacker().getName());
		assertEquals(defender.getName(), controller.loadGame(fileName).getDefender().getName());

		System.out.println("Successfully test save and load a game.");
	}
	
	// After test
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing SaveLoadGameController.");
		System.out.println();
	}
}
