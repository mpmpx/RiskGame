package risk.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * The panel of the game
 *
 */
public class GamePanel extends JPanel {
    private MapDisplayPanel mapDisplayPanel;
    private PhaseView phaseView;
    private DominationView dominationView;
	private CardExchangeView cardExchangeView;
    	
	/**
	 * Constructor method
	 */
	public GamePanel() {
		mapDisplayPanel = new MapDisplayPanel();
		phaseView = new PhaseView();
		dominationView = new DominationView();
		cardExchangeView = new CardExchangeView();
		phaseView.setCardExchangeView(cardExchangeView);
	}
	/**
	 * Initialize the contents of the panel
	 * 
	 */
	public void initialize() {
		mapDisplayPanel.initialize();
		phaseView.initialize();
		dominationView.initialize();

		JScrollPane scroll = new JScrollPane(mapDisplayPanel);
		scroll.setPreferredSize(new Dimension(750, 750));
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getViewport().setViewPosition(new Point(200, 200));	
		add(scroll);		
		
		add(dominationView);
		add(phaseView);
	}
	/**
	 * Return the current phase view
	 * 
	 * @return phaseView
	 */
	public PhaseView getPhaseView() {
		return phaseView;
	}
	/**
	 * Return the current map panel
	 * 
	 * @return mapDisplayPanel
	 */
	public MapDisplayPanel getMapDisplayPanel() {
		return mapDisplayPanel;
	}
	/**
	 * Return the current domination view
	 * 
	 * @return dominationView
	 */
	public DominationView getDominationView() {
		return dominationView;
	}
	/**
	 * Return the current card Exchane view
	 * 
	 * @return cardExchangeView
	 */
	public CardExchangeView getCardExchangeView() {
		return cardExchangeView;
	}	
}
