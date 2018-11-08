package risk.game;

import java.util.LinkedList;
import java.util.Random;
/**
 * This class defines the Cards that player can exchange to troops
 *
 */
public class Cards {

	public final static int INFANTRY = 0;
	public final static int CAVALRY = 1;
	public final static int CANNON = 2;
	
	private LinkedList<Integer> cards;
	
	/**
	 * Constructor for Card class
	 */
	public Cards() {
		cards = new LinkedList<Integer>();
	}
	
	/**
	 * method to add card 
	 * 
	 * @param value of the card
	 */
	public void addCard(int value) {
		cards.add(value);
	}
	
	/**
	 * If player conquered at least one territory during the attack phase, he
	 * will be given a card
	 * 
	 */
	public void getCard() {
		Random r = new Random();
		addCard(r.nextInt(3));
	}
	
	/**
	 * To get the hand card
	 * 
	 * @return the hand card with int type with a linked list 
	 */
	public LinkedList<Integer> getAllCards() {
		return cards;
	}
	
	/**
	 * To get the size of the hand card
	 * 
	 * @return the size of the hand card with int type
	 */
	public int getSize() {
		return cards.size();
	}
	
	/**
	 * method to remove cards
	 * @param cards
	 */
	public void removeCards(LinkedList<Integer> cards) {
		for (Integer card : cards) {
			this.cards.remove(card);
		}
	}
}
