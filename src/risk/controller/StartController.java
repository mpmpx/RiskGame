package risk.controller;

public class StartController {
	/**
	 * Main method to startup  controller.
	 * @param args command line parameters
	 */
	public static void main(String[] args) {
		GameController gameController = GameController.getInstance();
		GUIController guiController = GUIController.getInstance();
	}
}
