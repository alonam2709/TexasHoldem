import java.util.Vector;

/*
 * Abstract class providing a template for calculating probabilities of different poker hands.
 * This class includes methods for counting specific cards and checking for certain conditions
 * within a set of cards.
 */
public abstract class ProbabilityFormulae {
	// Constants to represent any number or any suit in a card deck
	protected static final int ANY_NUMBER = 400;
	protected static final int ANY_SUIT = 401;
	// Vector to store the cards for probability calculations
	protected Vector cards;

	public ProbabilityFormulae( Vector cards ) {
		this.cards = cards;
	}

	protected int count(int number, int suit, Vector cards) {
		int count = 0;

		// If we are counting any card of a specific suit
		if (number == ANY_NUMBER) {
			for (int i = 0; i < cards.size(); i++) {
				// Check if the current card's suit matches the specified suit
				if (((Card) cards.elementAt(i)).getSuit() == suit)
					count++; // Increment count if suit matches
			}
			return count; // Return the total count of cards with the specified suit
		}

		// If we are counting any card of a specific number
		if (suit == ANY_SUIT) {
			for (int i = 0; i < cards.size(); i++) {
				// Check if the current card's number matches the specified number
				if (((Card) cards.elementAt(i)).getNumber() == number)
					count++; // Increment count if number matches
			}
			return count; // Return the total count of cards with the specified number
		}

		// If we are counting cards of a specific number and suit
		for (int i = 0; i < cards.size(); i++) {
			Card c = (Card) cards.elementAt(i);
			// Check if both the number and suit of the current card match
			if (c.getNumber() == number && c.getSuit() == suit)
				count++; // Increment count if both number and suit match
		}

		return count; // Return the total count of cards matching both number and suit
	}
	
	protected int countXofaKind(int x, Vector cards) {
		
		int count = 0;
		
		for (int i=0; i<cards.size(); i++) {
			Card c = (Card) cards.elementAt(i);
			if (count(c.getNumber(), ANY_SUIT, cards)==x)
				count++;
		}
		
		return count/x; // because it would have counted each e.g. pair twice
	}
	
	protected boolean contains(int number, int suit, Vector cards) {
		
		if (number == ANY_NUMBER) {
			for (int i=0; i<cards.size(); i++) {
				if (((Card) cards.elementAt(i)).getSuit() == suit)
					return true;
			}
		}
		
		if (suit == ANY_SUIT) {
			for (int i=0; i<cards.size(); i++) {
				if (((Card) cards.elementAt(i)).getNumber() == number)
					return true;
			}
		}

		for (int i=0; i<cards.size(); i++) {
			Card c = (Card) cards.elementAt(i);
			if (c.getNumber() == number &&
					c.getSuit() == suit)
				return true;
		}
		
		// else
		return false;
	}
	
	public abstract double calcStraightFlush();
	public abstract double calcFlush();
	public abstract double calcFullHouse();
	public abstract double calcXofaKind(int x);
	public abstract double calcStraight();
	public abstract double calcTwoPair();
}
