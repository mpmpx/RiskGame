package test.game;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GameTest.class,
				CardsTest.class,
				ContinentTest.class,
				PlayerTest.class,
				PlayerColorTest.class,
				RiskMapTest.class,
				TerritoryTest.class,
				PhaseTest.class
				})

public class GameTestSuite {   
}  