package test;

// test suite that runs GameTestSuite and ControllerTestSuite
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.controller.ControllerTestSuite;
import test.game.GameTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ GameTestSuite.class,
				ControllerTestSuite.class
				})

public class TestSuite {   
}  