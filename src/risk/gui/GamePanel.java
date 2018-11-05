package risk.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GamePanel extends JPanel {
    private MapDisplayPanel mapDisplayPanel;
    private PhaseView phaseView;
    private DominationView dominationView;
	private CardExchangeView cardExchangeView;
    
	public GamePanel() {
		mapDisplayPanel = new MapDisplayPanel();
		phaseView = new PhaseView();
		dominationView = new DominationView();
		cardExchangeView = new CardExchangeView();
		phaseView.setCardExchangeView(cardExchangeView);
	}

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
	
	public PhaseView getPhaseView() {
		return phaseView;
	}
	
	public MapDisplayPanel getMapDisplayPanel() {
		return mapDisplayPanel;
	}
	
	public DominationView getDominationView() {
		return dominationView;
	}
	
	public CardExchangeView getCardExchangeView() {
		return cardExchangeView;
	}	
}
