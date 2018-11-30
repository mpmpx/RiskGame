package risk.game.strategy;

/**
 * The strategy interface;
 */
public interface Strategy {
	/**
	 * This documents "Behavior"
	 */
	public enum Behavior {
		HUMAN, AGGRESSIVE, BENEVOLENT, RANDOM, CHEATER
	};
	
	/**
  	* Method to get its behavior.
  	* @return the behavior.
  	*/ 	
	Behavior getBehavior();
	void startup();
	void reinforce();
	void attack();
	void fortify();
}
