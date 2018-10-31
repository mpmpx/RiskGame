package test;
import static org.junit.Assert.*;

import org.junit.Test;

import risk.controller.*;

public class JUnitTest1 {

	ReadFileController readFileController = new ReadFileController();
	
	@Test
	public void test() {
		try {
			readFileController.readFile("C:\\Users\\Admin\\Desktop\\SOEN 6441 Project\\Map files\\Africa\\Africa.bmp");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
