package test.controller;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import risk.controller.ReadFileController;

// Test cases for ReadFileController
public class ReadFileControllerTest {

	ReadFileController controller;
	String rootDir;

	// Before the test
	@BeforeClass
	public static void beforeTest() {
		System.out.println("Start to test ReadFileController.");
	}
	
	// Before each test case
	@Before
	public void before() {
		controller = new ReadFileController();
		rootDir = (Paths.get("").toAbsolutePath().toString());
	}
	
	// Test map validation by reading Africa_non_connected_graph.map
	@Test
	public void testMapValidation() {
		try {
			controller.readFile(rootDir + "\\src\\test\\Africa_non_connected_graph.map");
		}
		catch (Exception e) {
			assertEquals("The map contained in the file is not a connected graph.", e.getMessage());
			System.out.println("Successfully test validation of \"Africa_non_connected_graph.map\" - " +e.getMessage());
		}
	}
	
	// Test reading an invalid map file which misses a continent.
	@Test
	public void testMissingContinent() {
		try {
			controller.readFile(rootDir +"\\src\\test\\Africa_miss_continent.map.");
		}
		catch (Exception e) {
			assertEquals("Continent of Angola: The Congo is invalid.", e.getMessage());
			System.out.println("Successfully test missing continent in \"Africa_miss_continent.map\" - " +e.getMessage());
		}	
	}
	
	// Test reading an invalid map file which misses a country.
	@Test
	public void testMissingCountry() {
		try {
			controller.readFile(rootDir + "\\src\\test\\Africa_miss_country.map");
		}
		catch (Exception e) {
			assertEquals("Adjacent territory of Algeria: Morocco is invalid.", e.getMessage());
			System.out.println("Successfully test missing country in \"Africa_miss_country.map\" - " +e.getMessage());
		}
	}
	
	// Test map validation by reading Africa_continent_not_connected.map.
	@Test
	public void testContinentNotConnected() {
		try {
			controller.readFile(rootDir + "\\src\\test\\Africa_continent_not_connected.map");
		}
		catch (Exception e) {
			assertEquals("Northern Africa is not a connected subgraph.", e.getMessage());
			System.out.println("Successfully test validation of continents being connceted subgraph in \"Africa_continent_not_connected.map\" - " +e.getMessage());
		}
	}
	
	// After test
	@AfterClass
	public static void afterTest() {
		System.out.println("Finish testing ReadFileController.");
		System.out.println();
	}
}
