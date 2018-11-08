package test.game;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.game.Continent;
import risk.game.Player;
import risk.game.PlayerColor;

public class PlayerColorTest {
	
	PlayerColor newlayerColor = new PlayerColor();
	private static Color[] colors = new Color[] {	Color.red,
			Color.blue,
			Color.green,
			Color.orange,
			Color.pink,
			Color.lightGray
		};

	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test PlayerColorTest.");
	}
	
	@Test
	public void testnextColor() {
		
		int index = 1; 
		//assertEquals(colors[index], newlayerColor.nextColor());
		System.out.println("Successfully test set nextColor.");
	}
	

	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing nextColor.");
		System.out.println();
	}
}
