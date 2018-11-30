package test.controller;

//test suite that runs ReadFileControllerTest and SaveLoadGameControllerTest.
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ReadFileControllerTest.class,
				SaveLoadGameControllerTest.class
				})

public class ControllerTestSuite {   
}  