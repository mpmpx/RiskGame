package risk.controller;

import javax.swing.JOptionPane;

import risk.game.Player;
import risk.gui.CardExchangeView;
import risk.gui.DominationView;
import risk.gui.MainFrame;
import risk.gui.MapDisplayPanel;
import risk.gui.PhaseView;
/**
 * GUI Controller to define action performed according to different users' action.
 */
public class GUIController {

	private static GUIController controller; 
	private MainFrame mainFrame;
	
	/**
	 * Get the instance of this class.
	 * @return an instance of this class.
	 */
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
	
	/**
	 * Method to get phase view
	 * @return getPhaseView
	 *		get PhaseView from getGamePanel function which is from mainFrame
	 */
	public PhaseView getPhaseView() {
		return mainFrame.getGamePanel().getPhaseView();
	}
	
	/**
	 * Method to get map Display Panel
	 * @return MapDisplayPanel
	 *		get MapDisplayPanel from getGamePanel function which is from mainFrame
	 */
	public MapDisplayPanel getMapDisplayPanel() {
		return mainFrame.getGamePanel().getMapDisplayPanel();
	}
	
	/**
	 * Method to get DominationView
	 * @return DominationView
	 *		get DominationView from getGamePanel function which is from mainFrame
	 */
	public DominationView getDominationView() {
		return mainFrame.getGamePanel().getDominationView();
	}
	
	/**
	 * Method to get getCardExchangeView
	 * @return CardExchangeView
	 *		get CardExchangeView from getGamePanel function which is from mainFrame
	 */
	public CardExchangeView getCardExchangeView() {
		return mainFrame.getGamePanel().getCardExchangeView();
	}
	
	/**
	 * Method to notify which player has won the game
	 * @param winner
	 */
	public void win(Player winner) {
		
		JOptionPane.showMessageDialog(null, winner.getName() + " won the game");
		System.exit(0);
	}
}
