package risk.gui;

import java.awt.Color;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import risk.controller.ReadFileController;
import risk.game.RiskMap;
import risk.game.Territory;

public class GamePanel extends JPanel {
    private MapDisplayPanel mapDisplayPanel;
    private PhaseView phaseView;
	
	public GamePanel() {
		mapDisplayPanel = new MapDisplayPanel();
		phaseView = new PhaseView();
	
	}

	public void initialize() {
		mapDisplayPanel.initialize();
		phaseView.initialize();
		add(mapDisplayPanel);
		add(phaseView);
	}
	
	public PhaseView getPhaseView() {
		return phaseView;
	}
	
	public MapDisplayPanel getMapDisplayPanel() {
		return mapDisplayPanel;
	}
}
