package test.controller;


import risk.controller.*;

import static org.junit.Assert.assertEquals;

import org.junit.After; 
import org.junit.AfterClass; 
import org.junit.Before; 
import org.junit.BeforeClass; 
import org.junit.Test;

import com.sun.xml.internal.bind.v2.runtime.Name;

import risk.game.Continent;
import risk.game.Player; 
public class readFileTest { 

	ReadFileController newReadFileController = new ReadFileController();
	String Name = "/Users/cys/map/Africa.map";

	@Test
	public void testreadFile() throws Exception { 
		System.out.println("Successfully test readFile." );
		try {
				newReadFileController.readFile(Name);
			}
		catch (Exception e) {
			System.out.println(e.getMessage() );
		}
		
		System.out.println("Successfully test readFile." ); 
	} 
	
}
