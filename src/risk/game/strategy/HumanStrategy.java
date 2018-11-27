package risk.game.strategy;

import java.io.Serializable;

import risk.game.Player;
/**
 * The Human Strategy that implements strategy and serializable operation
 */
public class HumanStrategy implements Strategy, Serializable{
	
	private Player player;
	private Behavior behavior;
	/**
	 * Constructor for HumanStrategy
	 */
	public HumanStrategy(Player player) {
		this.player = player;
		behavior = Behavior.HUMAN;
	}
	/**
	 * Method to startup.
	 */
	@Override
	public void startup() {
		return;
	}
	/**
	 * Method to reinforce.
	 */
	@Override
	public void reinforce() {
		return;
	}
	/**
	 * Method to attack.
	 */
	@Override
	public void attack() {
		return;
	}
	/**
	 * Method to fortify
	 */
	@Override
	public void fortify() {
		return;
	}
	/**
	  * Method to get its behavior
	  * 
	  * @return the behavior in Behavior type
	  */
	@Override
	public Behavior getBehavior() {
		return behavior;
	}

}
