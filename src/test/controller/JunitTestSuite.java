package test.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
//@SuiteClasses({readFileTest.class,GameControllerTest.class})
@SuiteClasses({readFileTest.class,
				validateContinentTest.class,
				validateConnectedGraphTest.class,
				validateTerritoryTest.class})

public class JunitTestSuite {   
}  
