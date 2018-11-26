package risk.game.strategy;

/**
 * The strategy interface;
 */
public interface Strategy {
	
	public enum Behavior {
		HUMAN, AGGRESSIVE, BENEVOLENT, RANDOM, CHEATER
	};
	
	Behavior getBehavior();
	void startup();
	void reinforce();
	void attack();
	void fortify();
}
