package risk.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import risk.game.*;
import risk.controller.*;

public class MapDisplayPanel extends JPanel implements Observer{

	private Player currentPlayer;
	private int currentPhase;
	private RiskMap map;
	private Territory selectedTerritory;
	private BufferedImage image;
	private HashMap<String, Territory> territoryMap;
	private HashMap<Point, Territory> locationMap;
	private HashMap<String, LinkedList<Territory>> reachableMap;	
	private HashMap<String, LinkedList<Territory>> attackableMap;
	
	public MapDisplayPanel() {
		map = RiskMap.getInstance();
		currentPlayer = null;
		currentPhase = Phase.STARTUP;
		selectedTerritory = null;
		image = null;
		territoryMap = new HashMap<String, Territory>();
		locationMap = new HashMap<Point, Territory>();
	}
	
	public void initialize() {
		territoryMap = map.getTerritoryMap();
		for (Territory territory : territoryMap.values()) {
			locationMap.put(territory.getLocation(), territory);
		}
		
		try {
			image = ImageIO.read(new File(map.getImagePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		
		paintImage();
		repaint();
		
		this.addMouseListener(new Listener());
	}
	
	@Override
	public void update(Observable obs, Object arg) {
		currentPhase = ((Phase) obs).getCurrentPhase();
		currentPlayer = ((Phase) obs).getCurrentPlayer();

		if (currentPhase == Phase.FORTIFICATION) {
			reachableMap = ((Phase) obs).getCurrentPlayer().getReachableMap();
		}
		repaint();
	}
	
	private void paintImage() {
		try {
			image = ImageIO.read(new File(map.getImagePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (Territory territory : territoryMap.values()) {
			paintTerritory(territory.getX(), territory.getY(), territory.getOwner());
		}
	}
	
	
	private void paintTerritory(int x, int y, Color color) {
		Stack<Point> stack = new Stack<Point>();
		int originColor = image.getRGB(x, y);
		
		stack.push(new Point(x, y));
		while (!stack.isEmpty()) {
			Point currentPoint = stack.pop();
			int pointColor = image.getRGB(currentPoint.x, currentPoint.y);
			if (pointColor != Color.black.getRGB() && (pointColor == originColor || pointColor == Color.white.getRGB())) {
			
				image.setRGB(currentPoint.x, currentPoint.y, color.getRGB());
				stack.push(new Point(currentPoint.x + 1, currentPoint.y));
				stack.push(new Point(currentPoint.x - 1, currentPoint.y));
				stack.push(new Point(currentPoint.x, currentPoint.y + 1));
				stack.push(new Point(currentPoint.x, currentPoint.y - 1));
			}
		}		
	}
	
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        g.drawImage(image, 0, 0, null);
        drawArmyNumber(g);
    }	
	
	private void drawArmyNumber(Graphics g) {
		for (Territory territory : territoryMap.values()) {
			g.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 12));
			g.setColor(Color.white);
			g.drawString(territory.getArmy() + "", territory.getX(), territory.getY());
		}
	}
		
	private Territory selectTerritory(int x, int y) {
		Stack<Point> stack = new Stack<Point>();
		stack.push(new Point(x, y));
		LinkedList<Point> visited = new LinkedList<Point>();
		
		while (!stack.isEmpty()) {
			Point currentLocation = stack.pop();
			
			if (currentLocation.getX() > image.getWidth() - 1 || 
					currentLocation.getX() < 0 ||
					currentLocation.getY() > image.getHeight() - 1 ||
					currentLocation.getY() < 0) {
				return null;
			}
			
			if (visited.contains(currentLocation) ){
				continue;
			}
			else {
				visited.add(currentLocation);
			}
			
			if (image.getRGB(currentLocation.x, currentLocation.y) != Color.black.getRGB()) {		
				if (locationMap.containsKey(currentLocation)) {
					return locationMap.get(currentLocation);
				}
				
				stack.push(new Point(currentLocation.x + 1, currentLocation.y));
				stack.push(new Point(currentLocation.x - 1, currentLocation.y));
				stack.push(new Point(currentLocation.x, currentLocation.y + 1));
				stack.push(new Point(currentLocation.x, currentLocation.y - 1));
			}
		}
		
		return null;	
	}
	
	private class Listener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			Territory territory = selectTerritory(e.getX(), e.getY());
			
			switch (currentPhase) {
				case Phase.STARTUP : {
					startupClick(territory);
					break;
				}
				case Phase.REINFORCEMENT : {
					reinforcementClick(territory);
					break;
				}
				case Phase.ATTACK : {
					attackClick(territory);
					break;
				}
				case Phase.FORTIFICATION : {
					fortificationClick(territory);
					break;
				}
			}
		}

		private void startupClick(Territory territory) {
			if (territory != null && currentPlayer.getTerritoryMap().containsValue(territory)) {
				
				SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, currentPlayer.getUnassignedArmy(), 1);;
				JSpinner spinner = new JSpinner(spinnerModel);
				Object[] message = {
						"Set armies (0 - " + currentPlayer.getUnassignedArmy() + ")", 
						spinner
				};
				
				int result = JOptionPane.showConfirmDialog(null, message, "Set armies", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					GameController.getInstance().setArmy(territory, (int) spinner.getValue());
					return;
				}
				else {
					return;
				}
			}
		}
		
		private void reinforcementClick(Territory territory) {
			startupClick(territory);
		}
		
		private void attackClick(Territory territory) {

		}
		
		private void fortificationClick(Territory territory) {
			if (territory != null && currentPlayer.getTerritoryMap().containsValue(territory)) {
				
				// Select departure territory.
				if (selectedTerritory == null) {
				    // Territory with only one army is not able to be selected.
					// Territory with no reachable territories is not able to be selected.
					if (territory.getArmy() == 1 || reachableMap.get(territory.getName()).isEmpty()) {
						return;
					}
					
					selectedTerritory = territory;
					paintTerritory(territory.getX(), territory.getY(), territory.getOwner().darker());
					repaint();
				}
				// Select arrival country.
				else {
					
					// if select departure country itself.
					if (selectedTerritory == territory || !reachableMap.get(selectedTerritory.getName()).contains(territory)) {
						selectedTerritory = null;
						paintImage();
						repaint();
					}
					else {
						SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, selectedTerritory.getArmy() - 1, 1);;
						JSpinner spinner = new JSpinner(spinnerModel);
						Object[] message = {
								selectedTerritory.getName() + "(" + selectedTerritory.getArmy() + ") to "
										+ territory.getName() + "(" + territory.getArmy() +")", 
								spinner
						};
						
						int result = JOptionPane.showConfirmDialog(null, message, "Fortification", JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							GameController.getInstance().fortify(selectedTerritory, territory, (int) spinner.getValue());
							paintTerritory(selectedTerritory.getX(), selectedTerritory.getY(), selectedTerritory.getOwner());
							selectedTerritory = null;
							repaint();
						}
					}
					
				}
			}
			else {
				if (selectedTerritory != null) {
					selectedTerritory = null;
					paintImage();
					repaint();
				}
			}
		}
		
		
		@Override
		public void mouseEntered(MouseEvent arg0) {			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {			
		}
		
	}
}
