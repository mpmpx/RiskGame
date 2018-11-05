package risk.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import risk.controller.GameController;
import risk.game.*;

public class CardExchangeView extends JPanel implements Observer {

	private Player currentPlayer;
	private Cards cards;
	private LinkedList<JButton> buttonList;
	private int[] selectedCards = new int[3]; 
	private int exchangeBonusArmy;
	
	private JButton exchangeButton;
	private JButton clearButton;
	private JButton exitButton;
	
	public CardExchangeView() {
		buttonList = new LinkedList<JButton>();
		exchangeButton = new JButton("Exchange");
		exchangeButton.addActionListener(new ExchangeListener());
		exchangeButton.setEnabled(false);		
		
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ClearListener());
		clearButton.setEnabled(false);
		
		exitButton = new JButton("Exit");
		
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();		
				getRootPane().getParent().setVisible(false);
			}
			
		});
		
		setLayout(new GridBagLayout());
		for (int i = 0; i < 3; i++) {
			selectedCards[i] = 0;
		}
	}
	
	@Override
	public void update(Observable obs, Object arg1) {
		currentPlayer = ((Phase) obs).getCurrentPlayer();
		cards = currentPlayer.getCardSet();
		exchangeBonusArmy = ((Phase) obs).getExchangeBonusArmy();
		updateView();
	}

	private void reset() {
		for (JButton button: buttonList) {
			if (button.getText() != " ") {
				button.setEnabled(true);
			}
		}
		for (int i = 0; i < 3; i++) {
			selectedCards[i] = 0;
		}
		exchangeButton.setEnabled(false);
		clearButton.setEnabled(false);
	}
	
	private void clear() {
		buttonList.clear();
		
		for (int i = 0; i < 3; i++) {
			selectedCards[i] = 0;
		}
	}
	
	private void updateView() {
		clear();
		
		if (cards.getSize() >= 5) {
			exitButton.setEnabled(false);
		} else {
			exitButton.setEnabled(true);
		}
		
		if (cards != null) {
			for (Integer card : cards.getAllCards()) {
				JButton btn = new JButton();
				switch (card) {
					case Cards.CAVALRY : {
						btn.setName("Cavalry");
						btn.setText("Cavalry");
						break;
					}
					case Cards.INFANTRY : {
						btn.setName("Infantry");
						btn.setText("Infantry");
						break;
					}
					case Cards.CANNON : {
						btn.setName("Cannon");
						btn.setText("Cannon");
						break;
					}
				}
				btn.setPreferredSize(new Dimension(100, 200));
				btn.addActionListener(new CardListener());
				buttonList.add(btn);
			}
			
			for (int i = buttonList.size(); i < 5; i++) {
				JButton btn = new JButton(" ");
				btn.setPreferredSize(new Dimension(100, 200));
				btn.setEnabled(false);
				btn.setOpaque(false);
				btn.setContentAreaFilled(false);
				buttonList.add(btn);
			}
		}
		
		removeAll();
		
		GridBagConstraints c = new GridBagConstraints();
		for (int i = 0; i < buttonList.size(); i++) {
			c.weightx = 1;
			c.gridx = 0 + i;
			c.gridy = 0;
			add(buttonList.get(i), c);
		}
		
		c.weightx = 0;
		c.weighty = 1;
		//c.gridwidth = (int) Math.floor(buttonList.size() / 3);
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 1;
		add(exchangeButton, c);
		
		c.gridx = 2;
		c.gridy = 1;
		add(clearButton, c);
		
		c.gridx = 4;
		c.gridy = 1;
		add(exitButton, c);
		
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 2;
		add(new JLabel("Exchange cards for " + exchangeBonusArmy + " armies"), c);
		
		revalidate();
		repaint();
	}
	
	private boolean isCardSetValid() {
		for (int i = 0; i < 3; i++) {
			if (selectedCards[i] == 3) {
				return true;
			}
		}
		
		if (selectedCards[0] == 1 && selectedCards[1] == 1 && selectedCards[2] == 1 ) {
			return true;
		}
		
		return false;
	}
	
	private class ExchangeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			LinkedList<Integer> exchangedCards = new LinkedList<Integer>();
			
			if (isCardSetValid() == false) {
				JOptionPane.showMessageDialog(null, "You cannot exchange selected cards.");
				reset();
			}
			else {
				for (int i = 0 ; i < 3; i++) {
					for (int j = 0; j < selectedCards[i]; j++) {
						exchangedCards.add(i);
					}
				}
				
				cards.removeCards(exchangedCards);
				reset();
				GameController.getInstance().exchangeCards(exchangedCards);
			}
		}
	}
	
	private class ClearListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			reset();
		}
	}
	
	private class CardListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			if (btn.isEnabled()) {
				if (selectedCards[0] + selectedCards[1] + selectedCards[2] < 3) {
					switch (btn.getName()) {
						case "Cavalry" :{
							selectedCards[Cards.CAVALRY]++;
							break;
						}
						case "Infantry": {
							selectedCards[Cards.INFANTRY]++;
							break;
						}
						case "Cannon": {
							selectedCards[Cards.CANNON]++;
							break;
						}
					}
					btn.setEnabled(false);
					clearButton.setEnabled(true);
					
					if (selectedCards[0] + selectedCards[1] + selectedCards[2] == 3) {
						exchangeButton.setEnabled(true);
					}
				}
			}
		}
	}
}
