package test.game;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.PlayerColor;

public class PlayerColorTest {
	
	// Before test.
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test PlayerColor.");
	}
	
	// Test nextColor method.
	@Test
	public void testNextColor() {
		PlayerColor.reset();
		assertEquals(Color.red, PlayerColor.nextColor());
		System.out.println("Successfully test set nextColor.");
	}
	
	// After test.
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing PlayerColor.");
		System.out.println();
	}
}
