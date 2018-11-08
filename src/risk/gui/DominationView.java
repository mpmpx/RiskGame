package risk.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import risk.game.*;
/**
 * This class is the GUI for the Domination View Panel
 */
public class DominationView extends JPanel implements Observer{

	Player[] players;
	LinkedList<JTextArea> textAreaList;
	/**
	 * Constructor method
	 * 
	 */
	public DominationView() {
		players = new Player[6];
		textAreaList = new LinkedList<JTextArea>();
	}
	/**
	 * Initialization method
	 * 
	 */
	public void initialize() {
		BufferedImage image = RiskMap.getInstance().getImage();
		setPreferredSize(new Dimension(200, 750));
		setLayout(new GridLayout(6, 1));
	}
	/**
	 * Method to update the domination view
	 * @param obs
	 *            obs with Oberservale type
	 * @param arg1
	 *            arg1 with Object type
	 */
	@Override
	public void update(Observable obs, Object object) {
		players = ((Phase) obs).getPlayers();
		textAreaList.clear();
		for (Player player : players) {
			JTextArea textArea = new JTextArea();
			Border colorBorder = BorderFactory.createLineBorder(player.getColor(), 2);
			
			String content = "";
			content += player.getName() + ":\n";
			content += ("Map controlled: " + 
					player.getTerritoryMap().size() * 100 / RiskMap.getInstance().getTerritoryMap().size() + "%\n");
			
			content += ("Continents controlled: \n");
			for (Continent continent : player.getControlledContinent()) {
				content += "    " + continent.getName() + "\n";
			}
			content += "\n";
			
			content += ("Total number of armies: " + player.getTotalArmy());
			
			textArea.setBorder(colorBorder);
			textArea.setText(content);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setEditable(false);		
			textAreaList.add(textArea);
		}
		
		updateView();
	}
	/**
	 * Method to update the domination view
	 */
	private void updateView() {
		removeAll();
		revalidate();
		for (JTextArea textArea : textAreaList) {
		    JScrollPane scroll = new JScrollPane (textArea);	
		    scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMinimum());
		    scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
		    add(scroll);
		}
		revalidate();
		repaint();
	}

}
