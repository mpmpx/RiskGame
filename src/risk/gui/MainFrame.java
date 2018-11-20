package risk.gui;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import risk.controller.ReadFileController;
import risk.game.Game;
import risk.game.RiskMap;

/**
 * Main frame that contains all panels and components.
 */
public class MainFrame extends JFrame implements ActionListener{
	public final static int WIDTH = 1000;
	public final static int HEIGHT = 1000;
	public final static String MENU_PANEL = "Menu Panel";
	public final static String GAME_PANEL = "Game Panel";
	
	private static MainFrame mainFrame;
	private JPanel contentPanel;
	private HashMap<String, JPanel> panels;
	
	private Game module;
	private JButton startButton;
	
	/**
	 * Gets the instance of the MainFrame.
	 * @return the instance of the MainFrame.
	 */
	public static MainFrame getInstance() {
		if (mainFrame == null) {
			mainFrame = new MainFrame();
		}
		
		return mainFrame;
	}
	/**
	 * Creates the main frame of this application.
	 */
	private MainFrame() {
		panels = new HashMap<String, JPanel>();
		
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Risk");
		setLocationRelativeTo(null);
		contentPanel = (JPanel) getContentPane();
		contentPanel.setLayout(new CardLayout());
		createMenu();	
		
		((CardLayout) contentPanel.getLayout()).show(contentPanel,  MENU_PANEL);
		setVisible(true);
	}
	
	/**
	 * Creates the menu of this application which allows users to redirect to specific pages.
	 */
	public void createMenu() {
		JPanel menuPanel = new JPanel();
		// Set a layout of the menu panel.
		menuPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		menuPanel.add(startButton, c);
		panels.put(MENU_PANEL, menuPanel);
		addPanel(menuPanel, MENU_PANEL);
	}
	
	/**
	 * Adds a new panel to the main frame.
	 * @param panel a JPanel that is to be added to the main frame.
	 * @param name the name of the panel.
	 */
	public void addPanel(JPanel panel, String name) {
		panels.put(name, panel);
		contentPanel.add(panel, name);
	}
	
	/**
	 * Changes the current displaying panel according to the given panel's name.
	 * @param name the panel's name.
	 */
	public void setCurrentPanel(String name) {
		((CardLayout)contentPanel.getLayout()).show(contentPanel, name);
	}
	
	/**
	 * Sets the module of the main frame.
	 * @param module a Game that is to be the module of the main frame.
	 */
	public void setModule(Game module) {
		this.module = module;
	}
	

	/**
	 * Methods of the start button's action listener.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		JSlider slider = new JSlider(2, 6);
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		Object[] message = { "Set the number of players (2 - 6)", slider };

		int playerDialogResult = JOptionPane.showConfirmDialog(null, message, "Setup", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (playerDialogResult == JOptionPane.OK_OPTION) {
			try {
				loadMap();
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(null, exception.getMessage());
				return;
			}

			module.setPlayers(slider.getValue());
			module.distributeTerritories();
			((GamePanel) panels.get(GAME_PANEL)).initialize();
			module.start();
			((CardLayout) contentPanel.getLayout()).show(contentPanel, GAME_PANEL);
		} else {
			return;
		}
	}

	/**
	 * Reads map file and store data into a RiskMap, then sets the map to the module.
	 * @throws IOException if map is invalid.
	 */
	private void loadMap() throws Exception {
		RiskMap map;
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		fileChooser.setDialogTitle("Select a map file");
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Map file", "map");
		fileChooser.addChoosableFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			ReadFileController readFileController = new ReadFileController();
			map = readFileController.readFile(selectedFile.getAbsolutePath());
		} else {
			throw new Exception("You did not select a map file.");
		}

		module.setMap(map);
	}

}
