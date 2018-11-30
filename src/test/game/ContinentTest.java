package test.game;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.LinkedList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.Continent;
import risk.game.Territory;

public class ContinentTest {
	String newName = "Africa";
	int newValue = 6;
	Continent continent = new Continent(newName, newValue);
	
	// Before test.
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test Continent.");
	}
	
	// Before each test case
	@Test
	public void testSetName() {
		assertEquals(newName, continent.getName());
		System.out.println("Successfully set the name of continent with " + newName + ".");
	}
	
	// Test adding a territory to a continent.
	@Test
	public void addTerritoryTest() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Territory newTerritory = new Territory(name,location);
		
		continent.addTerritory(newTerritory);
		assertEquals(true, continent.getTerritoryList().contains(name) );
		assertEquals(false, continent.getTerritoryList().contains(nameNew) );
		System.out.println("Successfully add Territory! " );
	}
	
	// Test removing a territory from a continent.
	@Test
	public void removeTerritoryTest() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Point locationNew= new Point(200,200);
		Territory newTerritory = new Territory(name,location);
		Territory newTerritoryNew= new Territory(nameNew,locationNew);
		
		continent.addTerritory(newTerritory);
		continent.addTerritory(newTerritoryNew);
		assertEquals(true, continent.getTerritoryList().contains(name) );
		assertEquals(true, continent.getTerritoryList().contains(nameNew) );
		
		continent.removeTerritory(newTerritory);
		assertEquals(false, continent.getTerritoryList().contains(name) );
		assertEquals(true, continent.getTerritoryList().contains(nameNew) );
		
		System.out.println("Successfully remove Territory! " );
	}
	
	// Test whether a continent's all belonging territories 
	// are in a given list of territories. 
	@Test
	public void isOwnedTest() {
		String name = "Africa";
		String nameNew = "Africa1";
		Point location = new Point(100,100);
		Point locationNew= new Point(200,200);
		Territory newTerritory = new Territory(name,location);
		Territory newTerritoryNew= new Territory(nameNew,locationNew);
		
		continent.addTerritory(newTerritory);
		continent.addTerritory(newTerritoryNew);
		
		LinkedList<String> territoryList = new LinkedList<String>();
		territoryList.push(name);
		territoryList.push(nameNew);
		
		assertEquals(true,continent.isOwned(territoryList));

		System.out.println("Successfully isOwned! " );
	}
	
	// After test
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing Continent.");
		System.out.println();
	}
}
