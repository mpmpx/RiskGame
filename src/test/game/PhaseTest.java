package test.game;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import risk.game.Phase;

import org.junit.Test;

public class PhaseTest {

	Phase phase;
	
	// Printing method.
	public static void printMsg(String msg) {
		System.out.println(msg);
	}
	
	// Before test.
	@BeforeClass
	public static void beforeTest() {
		printMsg("Start to test Phase class.");
	}
	
	// Before each test case.
	@Before
	public void before() {
		phase = new Phase();
	}
	
	// Test the nextPhase method.
	@Test
	public void phaseTest() {
		assertEquals(Phase.STARTUP, phase.getCurrentPhase());
		phase.nextPhase();
		assertEquals(Phase.REINFORCEMENT, phase.getCurrentPhase());
		phase.nextPhase();
		assertEquals(Phase.ATTACK, phase.getCurrentPhase());
		phase.nextPhase();
		assertEquals(Phase.FORTIFICATION, phase.getCurrentPhase());
		phase.nextPhase();
		assertEquals(Phase.REINFORCEMENT, phase.getCurrentPhase());
	}
	
	// After test.
	@AfterClass
	public static void afterTest() {
		printMsg("Finish testing Phase class.");
		printMsg("");
	}	
	

}
