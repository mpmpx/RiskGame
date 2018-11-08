package risk.gui;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import risk.controller.GameController;
import risk.controller.ReadFileController;

/**
 * Main frame that contains all panels and components.
 */
public class MainFrame extends JFrame{
	public final static int WIDTH = 1000;
	public final static int HEIGHT = 1000;
	public final static String MENU_PANEL = "Menu Panel";
	public final static String GAME_PANEL = "Game Panel";
	
	private static MainFrame mainFrame;
	private JPanel menuPanel;
	private GamePanel gamePanel;
	private JPanel contentPanel;
	/**
	 * To get the instance of the main frame
	 * 
	 * @return the frame
	 */
	public static MainFrame getInstance() {
		if (mainFrame == null) {
			mainFrame = new MainFrame();
		}
		
		return mainFrame;
	}
	/**
	 * Constructor of MainFrame
	 */
	private MainFrame() {
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Risk");
		setLocationRelativeTo(null);
		contentPanel = (JPanel) getContentPane();
		contentPanel.setLayout(new CardLayout());
		
		createMenuPanel();
		gamePanel = new GamePanel();
		contentPanel.add(menuPanel, MENU_PANEL);
		contentPanel.add(gamePanel, GAME_PANEL);
		((CardLayout)contentPanel.getLayout()).show(contentPanel, MENU_PANEL);
		
		setVisible(true);		
	}
	
	/**
	 * To get the game panel
	 * 
	 * @return the panel 
	 */
	public GamePanel getGamePanel() {
		return gamePanel;
	}
	
	/**
	 * Method to create a Meun Panel with a Jpanel
	 * 
	 */
	private void createMenuPanel() {
		menuPanel = new JPanel();

		// Set a layout of the menu panel.
		menuPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ButtonListener());
		menuPanel.add(startButton, c);
	}
	
	/**
	 * Method to implement a button listener
	 * 
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JSlider slider = new JSlider(2, 6);
			slider.setMajorTickSpacing(1);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			Object[] message = {
					"Set the number of players (2 - 6)",
					slider
			};
			
			int playerDialogResult = JOptionPane.showConfirmDialog(null, message
					, "Setup", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			if (playerDialogResult == JOptionPane.OK_OPTION) {
				try {
					setMap();
				} catch (Exception exception) {
					JOptionPane.showMessageDialog(null, exception.getMessage());
					return;
				}
				
				GameController.getInstance().initailize(slider.getValue());
				gamePanel.initialize();
				((CardLayout)contentPanel.getLayout()).show(contentPanel, GAME_PANEL);
			}
			else {
				return;
			}
		}
		
		/**
		 * Method to set up the map file
		 * 
		 * @throws IOException
		 */
		private void setMap() throws Exception{
			JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			fileChooser.setDialogTitle("Select a map file");
			fileChooser.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Map file", "map");
			fileChooser.addChoosableFileFilter(filter);
			
			int returnValue = fileChooser.showOpenDialog(null);
			
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();	
				ReadFileController readFileController = new ReadFileController();
				readFileController.readFile(selectedFile.getAbsolutePath());
			}
			else {
				throw new Exception("You did not select a map file.");
			}
			
		}
		
	}
}
