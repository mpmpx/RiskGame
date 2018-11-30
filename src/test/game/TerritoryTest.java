package test.game;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.LinkedList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import risk.game.Territory;

import org.junit.Test;

public class TerritoryTest {
	Territory newTerritory = new Territory();

	// Printing method.
	public static void printMsg(String msg) {
		System.out.println(msg);
	}
	
	// Before test.
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test Territory.");
	}
	
	// Test setName method.
	@Test
	public void setNameTest() {	
		String name = "lolo";
		newTerritory.setName(name);

		assertEquals(name, newTerritory.getName()); 
		System.out.println("Successfully testsetName."); 
	}
	
	// Test setArmy method.
	@Test
	public void setArmyTest() {	
		int Army = 5;
		newTerritory.setArmy(Army);

		assertEquals(Army, newTerritory.getArmy()); 
		System.out.println("Successfully testsetArmy."); 
	}
	
	// Test addArmy method.
	@Test
	public void addArmyTest() {	
		int Army = 5;
		newTerritory.setArmy(Army);
		newTerritory.addArmy(Army);

		assertEquals(Army+Army, newTerritory.getArmy()); 
		System.out.println("Successfully addArmy."); 
	}
	
	// Test setLocation method.
	@Test
	public void setLocationTest() {	
		Point newPoint = new Point(100,200); 
		newTerritory.setLocation(newPoint);
		
		assertEquals(200, newTerritory.getY()); 
		assertEquals(100, newTerritory.getX()); 
		System.out.println("Successfully setLocation."); 
	}
	
	// Test getLocation method.
	@Test
	public void getLocationTest() {	
		Point newPoint = new Point(100,200); 
		newTerritory.setLocation(newPoint);
	
		assertEquals(newPoint, newTerritory.getLocation()); 
		System.out.println("Successfully getLocation."); 
	}
	
	// Test revomeArmy method.
	@Test
	public void revomeArmyTest() {	
		newTerritory.setArmy(10);
		assertEquals(10, newTerritory.getArmy());
		
		newTerritory.removeArmy(5);
		assertEquals(5, newTerritory.getArmy());
		
		System.out.println("Successfully revomeArmy."); 
	}
	
	// Test setShape method.
	@Test
	public void setShapeTest() {	
		LinkedList<Point> shape = new LinkedList<Point> ();
		Point newPoint1 = new Point(100,201); 
		Point newPoint2 = new Point(100,202); 
		Point newPoint3 = new Point(100,203); 
			
		shape.add(newPoint1);
		shape.add(newPoint2);
		shape.add(newPoint3);

		
		newTerritory.setShape(shape);
		boolean bstate1 = newTerritory.contains(newPoint1);
		assertEquals(true, bstate1);
		boolean bstate2 = newTerritory.contains(newPoint2);
		assertEquals(true, bstate2);
		boolean bstate3 = newTerritory.contains(newPoint3);
		assertEquals(true, bstate3);
		
		System.out.println("Successfully setShape."); 
	}
	
	// After test
	@AfterClass
	public static void afterTest() {
		printMsg("Finish testing Territory.");
		printMsg("");
	}	
	

}

