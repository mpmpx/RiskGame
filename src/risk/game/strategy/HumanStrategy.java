package risk.game.strategy;

import java.io.Serializable;

import risk.game.Player;

public class HumanStrategy implements Strategy, Serializable{
	
	private Player player;
	private Behavior behavior;
	
	public HumanStrategy(Player player) {
		this.player = player;
		behavior = Behavior.HUMAN;
	}
	
	@Override
	public void startup() {
		return;
	}

	@Override
	public void reinforce() {
		return;
	}

	@Override
	public void attack() {
		return;
	}

	@Override
	public void fortify() {
		return;
	}

	@Override
	public Behavior getBehavior() {
		return behavior;
	}

}
