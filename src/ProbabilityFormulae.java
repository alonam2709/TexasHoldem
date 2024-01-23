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

	// Counts the number of X-of-a-Kind in the hand (e.g., pairs, triplets)
	protected int countXofaKind(int x, Vector cards) {
		int count = 0; // Initialize count

		// Iterate over each card in the hand
		for (int i = 0; i < cards.size(); i++) {
			Card c = (Card) cards.elementAt(i); // Get the card at the current index

			// Check if the current card is part of an X-of-a-Kind
			if (count(c.getNumber(), ANY_SUIT, cards) == x) {
				count++; // Increment the count if it is part of an X-of-a-Kind
			}
		}

		return count / x; // Adjust count to avoid double counting each set of X-of-a-Kind
	}

	// Checks if a card with specific number and suit is in the hand
	protected boolean contains(int number, int suit, Vector cards) {
		// Check for any card of the specified suit
		if (number == ANY_NUMBER) {
			for (int i = 0; i < cards.size(); i++) {
				if (((Card) cards.elementAt(i)).getSuit() == suit) {
					return true; // Return true if a card of the suit is found
				}
			}
		}

		// Check for any card of the specified number
		if (suit == ANY_SUIT) {
			for (int i = 0; i < cards.size(); i++) {
				if (((Card) cards.elementAt(i)).getNumber() == number) {
					return true; // Return true if a card of the number is found
				}
			}
		}

		// Check for a specific card with the given number and suit
		for (int i = 0; i < cards.size(); i++) {
			Card c = (Card) cards.elementAt(i);
			if (c.getNumber() == number && c.getSuit() == suit) {
				return true; // Return true if the specific card is found
			}
		}

		return false; // Return false if no matching card is found
	}
	
	public abstract double calcStraightFlush();
	public abstract double calcFlush();
	public abstract double calcFullHouse();
	public abstract double calcXofaKind(int x);
	public abstract double calcStraight();
	public abstract double calcTwoPair();
}
