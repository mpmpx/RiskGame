package risk.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import risk.game.*;
import risk.controller.*;

public class PhaseView extends JPanel implements Observer{

	private int currentPhase;
	private CardExchangeView cardExchangeView;
	private JButton nextButton;
	private Player currentPlayer;
	private JLabel remainingArmyLabel;
	private JLabel reinforcementInfo;
	private JLabel attackerLabel;
	private JLabel defenderLabel;
	private JLabel fortificationLabel;
	private JSpinner attackerSpinner;
	private JSpinner defenderSpinner;
	private JLabel attackerDiceLabel;
	private JLabel defenderDiceLabel;
	
	JDialog exchangeDialog;
	
	private Territory attacker;
	private Territory defender;
	
	public PhaseView() {
		setLayout(new GridBagLayout());
		
		cardExchangeView = new CardExchangeView();
		nextButton = new JButton("Next");
		remainingArmyLabel = new JLabel();
		reinforcementInfo = new JLabel();
		attackerLabel = new JLabel();
		defenderLabel = new JLabel();
		fortificationLabel = new JLabel();
		attackerSpinner = new JSpinner();
		defenderSpinner = new JSpinner();
		attackerDiceLabel = new JLabel();
		defenderDiceLabel = new JLabel();
		
		attacker = null;
		defender = null;
		
		setPreferredSize(new Dimension(MainFrame.WIDTH, 200));
		nextButton.addActionListener(new ButtonListener());


		
	}
	
	public void initialize() {
		exchangeDialog = new JDialog(MainFrame.getInstance(), "Card Exchange", true);

		
		exchangeDialog.setPreferredSize(new Dimension(600, 300));
		exchangeDialog.setLocationRelativeTo(null);
		exchangeDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		exchangeDialog.setResizable(false);
		exchangeDialog.setContentPane(cardExchangeView);
		exchangeDialog.pack();
	}
	
	public void setCardExchangeView(CardExchangeView cardExchangeView) {
		this.cardExchangeView = cardExchangeView;

	}
	
	@Override
	public void update(Observable obs, Object arg) {
		currentPhase = ((Phase) obs).getCurrentPhase();
		currentPlayer = ((Phase) obs).getCurrentPlayer();
		attacker = ((Phase) obs).getAttacker();
		defender = ((Phase) obs).getDefender();

		if (attacker != null) {
			attackerLabel.setText("attacker: " + attacker.getName());
		} 
		else {
			attackerLabel.setText("attacker: ");
		}
		
		if (defender != null) {
			defenderLabel.setText("defender: " + defender.getName());
		}
		else {
			defenderLabel.setText("defender: ");
		}
		
		updateView();
	}
	
	private void updateView() {
		removeAll();
		switch (currentPhase) {
			case Phase.STARTUP : {
				updateStartupPhaseView();
				break;
			}
			case Phase.REINFORCEMENT : {
				updateReinforcementPhaseView();
				break;
			}
			case Phase.ATTACK : {
				updateAttackPhaseView();
				break;
			}
			case Phase.FORTIFICATION : {
				updateFortificationPhaseView();
				break;
			}
		}
		
		revalidate();
		repaint();	
	}
	
	private void updateStartupPhaseView() {
		GridBagConstraints c = new GridBagConstraints();
		Border colorBorder = BorderFactory.createLineBorder(currentPlayer.getColor(), 10);

		Border titleBorder = BorderFactory.createTitledBorder(colorBorder, currentPlayer.getName() + 
				" - Startup Phase", TitledBorder.CENTER, TitledBorder.TOP);
		setBorder(titleBorder);
		remainingArmyLabel.setText("Remaining unassgined armies: " + currentPlayer.getUnassignedArmy());
					
		c.weightx = 10;
		c.weighty = 10;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		add(remainingArmyLabel, c);
		
		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 1;
		add(nextButton, c);		
	}
	
	private void updateReinforcementPhaseView() {
		GridBagConstraints c = new GridBagConstraints();
		Border colorBorder = BorderFactory.createLineBorder(currentPlayer.getColor(), 10);

		JButton exchangeButton = new JButton("Exchange Cards");
		LinkedList<Continent> controlledContinent = currentPlayer.getControlledContinent();
		Border titleBorder = BorderFactory.createTitledBorder(colorBorder, currentPlayer.getName() +
				" - Reinforcement Phase", TitledBorder.CENTER, TitledBorder.TOP);
		setBorder(titleBorder);
		

		exchangeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				exchangeDialog.setVisible(true);
			}
			
		});
		remainingArmyLabel.setText("Remaining unassgined armies: " + currentPlayer.getUnassignedArmy());

		String reinforcementInfoMsg = "Free armies: " + currentPlayer.getFreeArmy() + " ";
		for (Continent continent : controlledContinent) {
			reinforcementInfoMsg += "Own " + continent.getName() + ": " + continent.getValue() + " ";
		}
		reinforcementInfo.setText(reinforcementInfoMsg);
		
		c.weightx = 10;
		c.weighty = 10;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		add(remainingArmyLabel, c);

		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 1;
		add(exchangeButton, c);
		
		c.gridx = 1;
		c.gridy = 2;
		add(nextButton, c);
		
		if (currentPlayer.getCardSet().getSize() >= 5 && !exchangeDialog.isVisible()) {
			exchangeDialog.setVisible(true);
		}
	}
	
	private void updateAttackPhaseView() {
		GridBagConstraints c = new GridBagConstraints();
		Border colorBorder = BorderFactory.createLineBorder(currentPlayer.getColor(), 10);
		
		Border titleBorder = BorderFactory.createTitledBorder(colorBorder, currentPlayer.getName() +
				" - Attack Phase", TitledBorder.CENTER, TitledBorder.TOP);
		setBorder(titleBorder);
		JButton rollButton = new JButton("roll");
		JButton alloutButton = new JButton("all-out");
		JButton stopButton = new JButton("stop");
		
		rollButton.addActionListener(new AttackPhaseButtonListener());
		alloutButton.addActionListener(new AttackPhaseButtonListener());
		stopButton.addActionListener(new AttackPhaseButtonListener());
		
		if (attacker != null && defender != null) {
			
			if (attacker.getArmy() == 1) {
				attackerSpinner.setEnabled(false);
				defenderSpinner.setEnabled(false);
				
				rollButton.setEnabled(false);
				alloutButton.setEnabled(false);
				stopButton.setEnabled(false);
				GameController.getInstance().setAttack(null,  null);
				if (GameController.getInstance().checkAttackPhase() == false) {
					attackerDiceLabel.setText(null);
					defenderDiceLabel.setText(null);
					return;
				}
			} 
			else {
				SpinnerNumberModel attackerSpinnerModel = new SpinnerNumberModel(Math.min(3, attacker.getArmy() - 1), 
																			1, Math.min(3, attacker.getArmy() - 1), 1);
				SpinnerNumberModel defenderSpinnerModel = new SpinnerNumberModel(Math.min(2, defender.getArmy()), 
																			1, Math.min(2, defender.getArmy()), 1);
				attackerSpinner.setModel(attackerSpinnerModel);
				defenderSpinner.setModel(defenderSpinnerModel);
				attackerSpinner.setEnabled(true);
				defenderSpinner.setEnabled(true);
			
				rollButton.setEnabled(true);
				alloutButton.setEnabled(true);
				stopButton.setEnabled(true);
			}
		} else {
			attackerSpinner.setEnabled(false);
			defenderSpinner.setEnabled(false);
			
			rollButton.setEnabled(false);
			alloutButton.setEnabled(false);
			stopButton.setEnabled(false);
		}
		
		
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 0;
		add(attackerSpinner, c);
		
		c.gridx = 0;
		c.gridy = 1;
		add(defenderSpinner, c);
		
		c.weightx = 1;
		c.weighty = 2;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		add(attackerLabel, c);
		
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 1;
		add(defenderLabel, c);
		
		c.gridx = 2;
		c.gridy = 0;
		add(attackerDiceLabel, c);

		c.gridx = 2;
		c.gridy = 1;
		add(defenderDiceLabel, c);

		
		c.weightx = 1;
		c.weighty = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		add(rollButton, c);
		
		c.weightx = 1;
		c.weighty = 3;
		c.gridx = 1;
		c.gridy = 2;
		add(alloutButton, c);
		
		c.weightx = 1;
		c.weighty = 3;
		c.gridx = 2;
		c.gridy = 2;
		add(stopButton, c);
		
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		c.gridwidth = 1;
		c.gridx = 3;
		c.gridy = 3;
		add(nextButton, c);
	}
	
	private void updateFortificationPhaseView() {
		GridBagConstraints c = new GridBagConstraints();
		Border colorBorder = BorderFactory.createLineBorder(currentPlayer.getColor(), 10);

		Border titleBorder = BorderFactory.createTitledBorder(colorBorder, currentPlayer.getName() + 
				" - Fortification Phase", TitledBorder.CENTER, TitledBorder.TOP);
		setBorder(titleBorder);
		
		fortificationLabel.setText("Move armies from one of your countries to another reachable one.");
		
		c.weightx = 10;
		c.weighty = 10;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		add(fortificationLabel, c);
		
		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 1;
		add(nextButton, c);		
	}
	
	private void rollDice(int attackerDiceNum, int defenderDiceNum) {
		int attackerCasulties = 0;
		int defenderCasulties = 0;
		
		Random dice = new Random();
		PriorityQueue<Integer> attackerDice = new PriorityQueue<Integer>(3, Collections.reverseOrder());
		PriorityQueue<Integer> defenderDice = new PriorityQueue<Integer>(2, Collections.reverseOrder());
				
		for (int i = 0; i < attackerDiceNum; i++) {
			attackerDice.add(dice.nextInt(6) + 1);
		}
		
		for (int i = 0; i < defenderDiceNum; i++) {
			defenderDice.add(dice.nextInt(6) + 1);
		}
		
		String attackerDiceResult = "<html>";
		String defenderDiceResult = "<html>";
		
		while (!attackerDice.isEmpty() && !defenderDice.isEmpty()) {
			if (attackerDice.peek() > defenderDice.peek()) {
				attackerDiceResult += ("<font color='red'>"+ attackerDice.peek().toString()+ "</font>   ");
				defenderDiceResult += defenderDice.peek() + "   ";
				defenderCasulties++;
			} 
			else {
				defenderDiceResult += ("<font color='red'>"+ defenderDice.peek().toString()+ "</font>   ");
				attackerDiceResult += attackerDice.peek() + "   ";
				attackerCasulties++;
			}
			attackerDice.remove();
			defenderDice.remove();
		}
		
		while (!attackerDice.isEmpty()) {
			attackerDiceResult += attackerDice.remove() + "   ";
		}
		attackerDiceResult += "</html>";
		
		while (!defenderDice.isEmpty()) {
			defenderDiceResult += defenderDice.remove() + "   ";
		}
		defenderDiceResult += "</html>";
		
		attackerDiceLabel.setText(attackerDiceResult);
		defenderDiceLabel.setText(defenderDiceResult);
		
		updateView();
		
		if (defender.getArmy() == defenderCasulties) {
			GameController.getInstance().setAttackResult(attackerCasulties, 0);
			
			SpinnerNumberModel spinnerModel = new SpinnerNumberModel(attacker.getArmy() - 1, 
					attackerDiceNum, attacker.getArmy() - 1, 1);
			
			JSpinner spinner = new JSpinner(spinnerModel);
			Object[] message = {
					"You conquered " + defender.getName(),
					"Set armies in the conquered territory (" + attackerDiceNum + " - " + (attacker.getArmy() - 1) + ")",
					spinner
			};
			

		    Object[] options = {"OK"};
			int result = JOptionPane.showOptionDialog(null, message, "Conquered Territory", 
					JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (result == JOptionPane.OK_OPTION) {
				attackerDiceLabel.setText(null);
				defenderDiceLabel.setText(null);
				GameController.getInstance().conquerTerritory((int) spinner.getValue());
			} else {
				attackerDiceLabel.setText(null);
				defenderDiceLabel.setText(null);
				GameController.getInstance().conquerTerritory((int) spinner.getValue());

			}
			
		} else {
			GameController.getInstance().setAttackResult(attackerCasulties, defenderCasulties);
		}
		
	}
	
	private class AttackPhaseButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonText = ((JButton) e.getSource()).getText();
	
			if (buttonText.equals("roll")) {
				rollDice((int) attackerSpinner.getValue(), (int) defenderSpinner.getValue());
			} else if (buttonText.equals("all-out")) {
				while(attacker != null && defender != null) {
					rollDice(Math.min(3, attacker.getArmy() - 1), Math.min(2, defender.getArmy()));
				}
				
			} else if (buttonText.equals("stop")) {
				attackerDiceLabel.setText(null);
				defenderDiceLabel.setText(null);
				GameController.getInstance().setAttack(null, null);
			}
			
		}
		
		
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
					int result = JOptionPane.showConfirmDialog(null, "Do you want to skip attack phase?", null, JOptionPane.YES_NO_OPTION);
					
					if (result == JOptionPane.YES_OPTION) {
						GameController.getInstance().setAttack(null, null);
						attackerDiceLabel.setText(null);
						defenderDiceLabel.setText(null);
						GameController.getInstance().nextPhase();
					}	
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
