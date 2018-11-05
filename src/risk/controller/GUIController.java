package risk.controller;

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
	
	private GUIController() {
		mainFrame = MainFrame.getInstance();
	}
	
	public PhaseView getPhaseView() {
		return mainFrame.getGamePanel().getPhaseView();
	}
	
	public MapDisplayPanel getMapDisplayPanel() {
		return mainFrame.getGamePanel().getMapDisplayPanel();
	}
}
