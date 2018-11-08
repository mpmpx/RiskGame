package risk.controller;

import javax.swing.JOptionPane;

import risk.game.Player;
import risk.gui.CardExchangeView;
import risk.gui.DominationView;
import risk.gui.MainFrame;
import risk.gui.MapDisplayPanel;
import risk.gui.PhaseView;

public class GUIController {

	private static GUIController controller; 
	private MainFrame mainFrame;
	
	public static GUIController getInstance() {
		if (controller == null) {
			controller = new GUIController();
		}
		
		return controller;
	}
	/**
	 * Constructor Method
	 */
	private GUIController() {
		mainFrame = MainFrame.getInstance();
	}
	
	public PhaseView getPhaseView() {
		return mainFrame.getGamePanel().getPhaseView();
	}
	
	public MapDisplayPanel getMapDisplayPanel() {
		return mainFrame.getGamePanel().getMapDisplayPanel();
	}
	
	public DominationView getDominationView() {
		return mainFrame.getGamePanel().getDominationView();
	}
	
	public CardExchangeView getCardExchangeView() {
		return mainFrame.getGamePanel().getCardExchangeView();
	}
	
	public void win(Player winner) {
		
		JOptionPane.showMessageDialog(null, winner.getName() + " won the game");
		System.exit(0);
	}
}
