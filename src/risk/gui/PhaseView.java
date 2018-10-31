package risk.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import risk.game.*;
import risk.controller.*;

public class PhaseView extends JPanel implements Observer{

	private JButton nextButton;
	private Player currentPlayer;
	private int currentPhase;
	private JLabel remainingArmyLabel;
	private JLabel reinforcementInfo;
	private JLabel attckerLabel;
	private JLabel defenderLabel;
	private JLabel fortificationLabel;
	
	public PhaseView() {
		setLayout(new GridBagLayout());
		nextButton = new JButton("Next");
		remainingArmyLabel = new JLabel();
		reinforcementInfo = new JLabel();
		attckerLabel = new JLabel();
		defenderLabel = new JLabel();
		fortificationLabel = new JLabel();

		setPreferredSize(new Dimension(MainFrame.WIDTH, 200));
		nextButton.addActionListener(new ButtonListener());

	}
	
	public void initialize() {

	}
	
	@Override
	public void update(Observable obs, Object arg) {
		currentPhase = ((Phase) obs).getCurrentPhase();
		currentPlayer = ((Phase) obs).getCurrentPlayer();
		GridBagConstraints c = new GridBagConstraints();
		
		clear();
		Border colorBorder = BorderFactory.createLineBorder(currentPlayer.getColor(), 10);
		switch (currentPhase) {
			case Phase.STARTUP : {
				Border titleBorder = BorderFactory.createTitledBorder(colorBorder, currentPlayer.getName() + 
						" - Startup Phase", TitledBorder.CENTER, TitledBorder.TOP);
				setBorder(titleBorder);
				
				this.remainingArmyLabel.setText("Remaining unassgined armies: " + currentPlayer.getUnassignedArmy());
							
				//c.weightx = 5;
				c.weightx = 10;
				c.weighty = 10;
				c.gridx = 0;
				c.gridy = 0;
				add(remainingArmyLabel, c);
				
				c.weightx = 1;
				c.weighty = 1;
				c.gridx = 1;
				c.gridy = 1;
				add(nextButton, c);
				
				revalidate();
				repaint();
				break;
			}
			case Phase.REINFORCEMENT : {
				LinkedList<Continent> controlledContinent = currentPlayer.getControlledContinent();
				Border titleBorder = BorderFactory.createTitledBorder(colorBorder, currentPlayer.getName() +
						" - Reinforcement Phase", TitledBorder.CENTER, TitledBorder.TOP);
				setBorder(titleBorder);
				
				remainingArmyLabel.setText("Remaining unassgined armies: " + currentPlayer.getUnassignedArmy());
				
				String reinforcementInfoMsg = "Free armies: " + currentPlayer.getFreeArmy() + " ";
				for (Continent continent : controlledContinent) {
					reinforcementInfoMsg += "Own " + continent.getName() + ": " + continent.getValue() + " ";
				}
				reinforcementInfo.setText(reinforcementInfoMsg);
				
				c.weightx = 10;
				c.weighty = 10;
				c.gridx = 0;
				c.gridy = 0;
				add(remainingArmyLabel, c);
				
				c.weightx = 1;
				c.weighty = 1;
				c.gridx = 1;
				c.gridy = 1;
				add(nextButton, c);
				
				revalidate();
				repaint();
				break;
			}
			case Phase.ATTACK : {
				Border titleBorder = BorderFactory.createTitledBorder(colorBorder, currentPlayer.getName() +
						" - Attack Phase", TitledBorder.CENTER, TitledBorder.TOP);
				setBorder(titleBorder);
				
				c.gridx = 2;
				c.gridy = 2;
				add(nextButton, c);
				
				revalidate();
				repaint();
				break;
			}
			case Phase.FORTIFICATION : {
				Border titleBorder = BorderFactory.createTitledBorder(colorBorder, currentPlayer.getName() + 
						" - Fortification Phase", TitledBorder.CENTER, TitledBorder.TOP);
				setBorder(titleBorder);
				
				fortificationLabel.setText("Move armies from one of your countries to another reachable one.");
				
				c.weightx = 10;
				c.weighty = 10;
				c.gridx = 0;
				c.gridy = 0;
				add(fortificationLabel, c);
				
				c.weightx = 1;
				c.weighty = 1;
				c.gridx = 1;
				c.gridy = 1;
				add(nextButton, c);
				
				revalidate();
				repaint();
				break;
			}
		}
	}
	
	private void clear() {
		this.removeAll();
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (currentPhase) {
				case Phase.STARTUP : {
					if (currentPlayer.getUnassignedArmy() != 0) {
						JOptionPane.showMessageDialog(null, "You have to set all unassigned armies.");
						return;
					} 
					else {
						GameController.getInstance().nextPhase();
					}
					
					break;
				}
				case Phase.REINFORCEMENT : {
					if (currentPlayer.getUnassignedArmy() != 0) {
						JOptionPane.showMessageDialog(null, "You have to set all unassigned armies.");
						return;
					} 
					else {
						GameController.getInstance().nextPhase();
					}
					break;
				}
				case Phase.ATTACK : {
					GameController.getInstance().nextPhase();
					break;
				}
				case Phase.FORTIFICATION : {
					int result = JOptionPane.showConfirmDialog(null, "Do you want to skip this phase?", null, JOptionPane.YES_NO_OPTION);
					
					if (result == JOptionPane.YES_OPTION) {
						GameController.getInstance().nextPhase();
					}	
					break;
				}
			}
			
		}
		
	}


}
