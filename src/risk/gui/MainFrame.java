package risk.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import risk.controller.ReadFileController;
import risk.game.Game;
import risk.game.RiskMap;

/**
 * Main frame that contains all panels and components.
 */
public class MainFrame extends JFrame {
	
	public final static int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width * 6 / 10;
	public final static int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height * 9 / 10;
	public final static String MENU_PANEL = "Menu Panel";
	public final static String GAME_PANEL = "Game Panel";
	
	private static MainFrame mainFrame;
	private JPanel contentPanel;
	private HashMap<String, JPanel> panels;
	
	private Game module;
	private JButton startButton;
	
	private final String[] playerbehavior = {"None", "Human", "Aggressive", "Benevolent", "Random", "Cheater"};
	private JTextField mapFilePath;
	private JComboBox[] playersBox;
	
	private File selectedFile;
	
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
		
		mapFilePath = new JTextField();
		mapFilePath.setEditable(false);
		mapFilePath.setPreferredSize(new Dimension(100,20));
		playersBox = new JComboBox[6];
		for (int i = 0; i < 6; i++) {
			playersBox[i] = new JComboBox(playerbehavior);
		}
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
		startButton.addActionListener(new StartListener());
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
	
	/**
	 * Methods of the start button's action listener.
	 */
	private class StartListener implements ActionListener {
		RiskMap map;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton loadMapButton = new JButton("Load Map");
			loadMapButton.addActionListener(new LoadMapListener());

			Object[] message = { loadMapButton, mapFilePath, 
					"player1", playersBox[0], 
					"player2", playersBox[1],
					"player3", playersBox[2],
					"player4", playersBox[3],
					"player5", playersBox[4],
					"player6", playersBox[5] };

			int playerDialogResult = JOptionPane.showConfirmDialog(null, message, "Setup", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			
			if (playerDialogResult == JOptionPane.OK_OPTION) {
				LinkedList<String> behaviors = new LinkedList<String>();
				for (int i = 0; i < 6; i++) {
					if ((String)playersBox[i].getSelectedItem() != "None") {
						behaviors.add((String)playersBox[i].getSelectedItem());
					}
				}
				
				if (behaviors.size() < 2 || mapFilePath.getText().length() == 0) {
					JOptionPane.showMessageDialog(null, "Please select a map and more than 2 players.");
					return;
				}
				ReadFileController readFileController = new ReadFileController();
				try {
					map = readFileController.readFile(selectedFile.getAbsolutePath());
				} catch (Exception exception) {
					JOptionPane.showMessageDialog(null, exception.getMessage());
					exception.printStackTrace();
					return;
				}
				module.setMap(map);
				module.setPlayers(behaviors);
				module.distributeTerritories();
				((GamePanel) panels.get(GAME_PANEL)).initialize();
				module.start();
				((CardLayout) contentPanel.getLayout()).show(contentPanel, GAME_PANEL);
				
			} 
			
			mapFilePath.setText(null);
			for (int i = 0; i < 6; i++) {
				playersBox[i].setSelectedIndex(0);
			}
		}
	}

	/**
	 * Customized ActionListener for "Load Map" button.
	 */
	private class LoadMapListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			fileChooser.setDialogTitle("Select a map file");
			fileChooser.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Map file", "map");
			fileChooser.addChoosableFileFilter(filter);

			int returnValue = fileChooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				mapFilePath.setText(selectedFile.getPath());
			}

		}
	}
}
