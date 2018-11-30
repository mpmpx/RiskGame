package test.game;

import static org.junit.Assert.*;

import java.awt.Point;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.controller.ReadFileController;
import risk.game.Continent;
import risk.game.Player;
import risk.game.RiskMap;
import risk.game.Territory;

public class PlayerTest {
	
	String name;
	Player player;
	
	// Before test.
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test Player.");
	}
	
	// Before each test case.
	@Before
	public void before() {
		name = "lili";
		player = new Player(name);
	}
	
	// Test setUnsignedArmy.
	@Test
	public void setUnsignedArmyTest() {
		int num = 10;
		player.setUnsignedArmy(num);
		assertEquals(num, player.getUnassignedArmy());
		System.out.println("Successfully test setUnsignedArmy.");
	}
	
	// Test getTotalArmyTest.
	@Test
	public void getTotalArmyTest() {
		int num = 10;
		player.setUnsignedArmy(num);
		player.setUnsignedArmy(num);
		assertEquals(num + num, player.getTotalArmy());
		System.out.println("Successfully test getTotalArmy.");
	}
	
	// Test addExchangeBonusArmy.
	@Test
	public void addExchangeBonusArmyTest() {
		int num = 10;

		player.setUnsignedArmy(num);
		player.exchangeCards(new LinkedList<Integer>(), num);
		
		assertEquals(num + num, player.getTotalArmy());
		assertEquals(num + num, player.getUnassignedArmy());
		System.out.println("Successfully test addExchangeBonusArmy.");
	}
	
	// Test getReinforcement.
	@Test
	public void getReinforcementTest() {
		Continent continent = new Continent("continent", 5);
		LinkedList<Continent> continentList = new LinkedList<Continent>();
		
		continentList.add(continent);
		player.setContinents(continentList);
		for (int i = 0; i < 14; i++) {
			Territory territory = new Territory();
			territory.setName(i + "");
			territory.setContinentName("continent");
			continent.addTerritory(territory);
			player.addTerritory(territory);
		}

		player.getReinforcement();
		assertEquals(9, player.getTotalArmy());
		System.out.println("Successfully test the calculation of number of reinforcement armies.");
	}
	
	// Test addArmy method.
	@Test
	public void addArmyTest() {
		String name = "Africa";
		Point location = new Point(100,100);
		Territory newTerritory = new Territory(name,location);
		int num = 5;

		player.addTerritory(newTerritory);
		player.setUnsignedArmy(num);
		assertEquals(5, player.getUnassignedArmy());
		player.placeUnassignedArmy(newTerritory, 2);		
		
		assertEquals(3, player.getUnassignedArmy());
		System.out.println("Successfully test addArmy.");
	}
	
	// Test moveArmy method.
	@Test
	public void moveArmyTest() {
		Territory startTerritory = new Territory("Africa",  new Point(100,100));
		Territory destTerritory= new Territory("Asia", new Point(200,200));
		Player player = new Player("player");
		startTerritory.addArmy(10);
		destTerritory.addArmy(5);
		player.addTerritory(startTerritory);
		player.addTerritory(destTerritory);
		
		player.moveArmy(startTerritory, destTerritory, 3);
		assertEquals(7, startTerritory.getArmy());
		assertEquals(8, destTerritory.getArmy());
		System.out.println("Successfully test moveArmy.");

	}
	
	// Test addTerritory method.
	@Test
	public void addTerritoryTest() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Point locationNew= new Point(200,200);
		Territory newTerritory = new Territory(name,location);
		Territory newTerritoryNew= new Territory(nameNew,locationNew);

		player.addTerritory(newTerritory);
		player.addTerritory(newTerritoryNew);

		assertEquals(true, player.getTerritoryMap().containsKey(name));
		assertEquals(true, player.getTerritoryMap().containsKey(nameNew));
	
		System.out.println("Successfully test addTerritory.");
	}
	
	// Test removeTerritory method.
	@Test
	public void removeTerritoryTest() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Point locationNew= new Point(200,200);
		Territory newTerritory = new Territory(name,location);
		Territory newTerritoryNew= new Territory(nameNew,locationNew);

		player.addTerritory(newTerritory);
		player.addTerritory(newTerritoryNew);
		

		assertEquals(true, player.getTerritoryMap().containsKey(name));
		assertEquals(true, player.getTerritoryMap().containsKey(nameNew));
		
		assertEquals(2, player.getTerritoryMap().size());
		
		player.removeTerritory(newTerritoryNew);
		
		assertEquals(1, player.getTerritoryMap().size());
		
		assertEquals(true, player.getTerritoryMap().containsKey(name));
		assertEquals(false, player.getTerritoryMap().containsKey(nameNew));
	
		System.out.println("Successfully test removeTerritory.");
	}
	
	// Test conquering a territory.
	@Test
	public void conquerTest() {
		Territory attackingTerritory = new Territory("attacker", new Point(0, 0));
		Territory defendingTerritory = new Territory("defender", new Point(0, 1));
		
		Player attacker = new Player("attacker");
		Player defender = new Player("defender");
		
		attacker.addTerritory(attackingTerritory);
		attackingTerritory.setOwner(attacker);
		attackingTerritory.setArmy(10000);
		
		defender.addTerritory(defendingTerritory);
		defendingTerritory.setOwner(defender);
		defendingTerritory.setArmy(1);
		
		while (defendingTerritory.getArmy() > 0) {
			attacker.attack(attackingTerritory, defendingTerritory, Math.min(3, attackingTerritory.getArmy() - 1), Math.min(2, defendingTerritory.getArmy()));
		}
		
		assertEquals(attacker, defendingTerritory.getOwner());
		System.out.println("Successfully test conquering a territory");
	}
	
	// Test startup phase.
	@Test
	public void startupTest() {
		player = new Player("player");
		player.setUnsignedArmy(20);
		player.addTerritory(new Territory("a", new Point(0, 1)));
		player.addTerritory(new Territory("b", new Point(0, 2)));
		HashMap<String, Territory> territoryMap = player.getTerritoryMap();
		player.placeUnassignedArmy(territoryMap.get("a"), 12);
		player.placeUnassignedArmy(territoryMap.get("b"), 8);
		
		assertEquals(0, player.getUnassignedArmy());
		System.out.println("Successfully test validation of startup phase.");
	}
	
	// Test fortification phase.
	@Test
	public void fortificationTest() {
		RiskMap map = new RiskMap();
		ReadFileController controller = new ReadFileController();
		
		String rootDir = (Paths.get("").toAbsolutePath().toString());
		player = new Player("player");
		HashMap<Territory, LinkedList<Territory>> result = new HashMap<Territory, LinkedList<Territory>>();
		
		try {
			map = controller.readFile(rootDir +"\\src\\test\\Africa.map.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HashMap<String, Territory> territoryMap = map.getTerritoryMap();
		player.addTerritory(territoryMap.get("Egypt"));
		player.addTerritory(territoryMap.get("North Sudan"));
		player.addTerritory(territoryMap.get("South Sudan"));
		
		player.placeUnassignedArmy(territoryMap.get("Egypt"), 10);
		player.placeUnassignedArmy(territoryMap.get("North Sudan"), 10);
		player.placeUnassignedArmy(territoryMap.get("South Sudan"), 10);

		LinkedList<Territory> list1  = new LinkedList<Territory>();
		list1.add(territoryMap.get("North Sudan"));
		list1.add(territoryMap.get("South Sudan"));
		result.put(territoryMap.get("Egypt"), list1);
		
		LinkedList<Territory> list2  = new LinkedList<Territory>();
		list2.add(territoryMap.get("Egypt"));
		list2.add(territoryMap.get("South Sudan"));
		result.put(territoryMap.get("North Sudan"), list2);
		
		LinkedList<Territory> list3  = new LinkedList<Territory>();
		list3.add(territoryMap.get("North Sudan"));
		list3.add(territoryMap.get("Egypt"));
		result.put(territoryMap.get("South Sudan"), list3);
		player.updateReachableMap();
		assertEquals(result, player.getReachableMap());
		System.out.println("Successfully test validation of fortification phase.");
	}
	
	// Test Rolling dice.
	@Test
	public void rollDiceTest() {
		Territory attackingTerritory = new Territory("attacker", new Point(0, 0));
		Territory defendingTerritory = new Territory("defender", new Point(0, 1));
		
		Player attacker = new Player("attacker");
		Player defender = new Player("defender");
		
		attacker.addTerritory(attackingTerritory);
		attackingTerritory.setOwner(attacker);
		attackingTerritory.setArmy(10000);
		
		defender.addTerritory(defendingTerritory);
		defendingTerritory.setOwner(defender);
		defendingTerritory.setArmy(1);
		
		attacker.attack(attackingTerritory, defendingTerritory, Math.min(3, attackingTerritory.getArmy() - 1), Math.min(2, defendingTerritory.getArmy()));
		assertEquals(Math.min(3, attackingTerritory.getArmy() - 1), attacker.getDice().size());
	}
	
	// After test.
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing Player.");
		System.out.println();
	}
}
