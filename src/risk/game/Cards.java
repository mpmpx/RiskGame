package risk.game;

import java.util.LinkedList;
import java.util.Random;

public class Cards {

	public final static int INFANTRY = 0;
	public final static int CAVALRY = 1;
	public final static int CANNON = 2;
	
	private LinkedList<Integer> cards;
	
	public Cards() {
		cards = new LinkedList<Integer>();
	}
	
	public void addCard(int value) {
		cards.add(value);
	}

	public void getCard() {
		Random r = new Random();
		addCard(r.nextInt(3));
	}
	
	public LinkedList<Integer> getAllCards() {
		return cards;
	}
	
	public int getSize() {
		return cards.size();
	}
	
	public void removeCards(LinkedList<Integer> cards) {
		for (Integer card : cards) {
			this.cards.remove(card);
		}
	}
}
