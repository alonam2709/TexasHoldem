import java.util.Vector;

/*
Class representing a poker hand and its probability calculations.
It supports the addition of cards, calculation of the best hand, and
 determination of probabilities for various poker hand types.
 */
class Hand {

	// Constants representing different types of poker hands.
	public static final int HIGH_CARD = 300;
	public static final int ONE_PAIR = 301;
	public static final int TWO_PAIR = 302;
	public static final int THREE_OF_A_KIND = 303;
	public static final int STRAIGHT = 304;
	public static final int FLUSH = 305;
	public static final int FULL_HOUSE = 306;
	public static final int FOUR_OF_A_KIND = 307;
	public static final int STRAIGHT_FLUSH = 308;
	// Vector to store cards in the hand.
	public Vector cards;
	// Probability calculation strategy used for this hand.
	private ProbabilityFormulae probabilityFormulae;
	//Converts a hand type to its string representation.
	public static String toString(int hand) {
		switch(hand) {
		case HIGH_CARD : return "High Card";
		case ONE_PAIR : return "One Pair";
		case TWO_PAIR : return "Two Pair";
		case THREE_OF_A_KIND : return "Three of a kind";
		case STRAIGHT : return "Straight";
		case FLUSH : return "Flush";
		case FULL_HOUSE : return "Full House";
		case FOUR_OF_A_KIND : return "Four of a kind";
		case STRAIGHT_FLUSH : return "Straight Flush";
		default : return "ERROR: Hand.toString()";
		}
	}
	//  Checks if a given hand type is valid.
	public static boolean isValid(int hand) {
		switch(hand) {
		case HIGH_CARD : // fall through
		case ONE_PAIR : // fall through
		case TWO_PAIR : // fall through
		case THREE_OF_A_KIND : // fall through
		case STRAIGHT : // fall through
		case FLUSH : // fall through
		case FULL_HOUSE : // fall through
		case FOUR_OF_A_KIND : // fall through
		case STRAIGHT_FLUSH : return true;
		default : return false;
		}
		
	}
	
// Initializes card vector and probability calculation formula.
	public Hand() {
		cards = new Vector(7,0);
		probabilityFormulae = new YourProbabilityFormulae(cards);
	}
	// Implementation for adding a card to the hand
	public void addCard(Card c) {
		if (cards.size() < 7) {
			cards.addElement(c);
		}
	}
	//Checks if the hand requires an update based on the number of cards.
	public boolean requiresUpdate() {
		return (cards.size()==2 || cards.size()>=5 );
	}
	// Implementation for checking if the hand is complete
	public boolean isComplete() {
		return (cards.size()==7);
	}
	
	public int getBestHand() {
		// Implementation for determining the best hand
		ProbabilityFormulae probs = new YourProbabilityFormulae(cards);
		Probability p;

		for (int hand = Hand.STRAIGHT_FLUSH; hand>=Hand.HIGH_CARD; hand--) {
			p = calculate(hand, probs);
			if (p.probability == 1.0) return p.hand;
		}
		
		System.out.println("ERROR: Hand.getBestHand()");
		return -1;
	}

	
	public Probability[] getProbabilities() {
		//Calculates the probabilities for various poker hands.
		Probability[] probs = new Probability[8];
		int calculateTop_ = probs.length;

		if (isComplete()) {
			int bestHand = this.getBestHand();
			calculateTop_ = Hand.STRAIGHT_FLUSH - bestHand;

			probs[calculateTop_] =
				calculate( bestHand,
						new HigherHandProbabilityFormulae(cards) );
			for (int i=calculateTop_+1; i<probs.length; i++) {
				probs[i] = new Probability(Hand.STRAIGHT_FLUSH-i, 0.);
			}
		}

		// not necessary to be in order
		for (int i=0; i<calculateTop_; i++) {
			probs[i] = calculate(Hand.STRAIGHT_FLUSH-i, probabilityFormulae);
		}
		
		return probs;
	}
	
	private Probability calculate(int hand, ProbabilityFormulae probs) {
		// Implementation for calculating the probability of a specific hand
		switch(hand) {
		case Hand.STRAIGHT_FLUSH :
			return new Probability( Hand.STRAIGHT_FLUSH,
					probs.calcStraightFlush() );
		case Hand.FOUR_OF_A_KIND :
			return new Probability( Hand.FOUR_OF_A_KIND, 
					probs.calcXofaKind(4) );
		case Hand.FULL_HOUSE :
			return new Probability( Hand.FULL_HOUSE, 
					probs.calcFullHouse() );
		case Hand.FLUSH :
			return new Probability( Hand.FLUSH, 
					probs.calcFlush() );
		case Hand.STRAIGHT :
			return new Probability( Hand.STRAIGHT, 
					probs.calcStraight() );
		case Hand.THREE_OF_A_KIND :
			return new Probability( Hand.THREE_OF_A_KIND, 
					probs.calcXofaKind(3) );
		case Hand.TWO_PAIR :
			return new Probability( Hand.TWO_PAIR, 
					probs.calcTwoPair() );
		case Hand.ONE_PAIR :
			return new Probability( Hand.ONE_PAIR, 
					probs.calcXofaKind(2) );
		case Hand.HIGH_CARD : // must have at least high card
			return new Probability( Hand.HIGH_CARD, 1.0 );
		default :
			System.out.println("ERROR : Hand.calculate()");
			return null;
		}
		
	}
	//Calculating Pre-Flop Strength based on of [
	public double calculatePreFlopHandStrength() {
		if (cards.size() < 2) {
			return 0; // Ensure there are at least two cards for pre-flop strength calculation
		}

		Card card1 = (Card) cards.elementAt(0);
		Card card2 = (Card) cards.elementAt(1);

		// Convert the card numbers to their poker values
		int card1Value = convertCardValueToPokerValue(card1.getNumber());
		int card2Value = convertCardValueToPokerValue(card2.getNumber());

		// Calculate the total points based on card values
		int cardValuePoints = card1Value + card2Value;

		// Add points for a pair
		int pairPoints = 0;
		if (card1.getNumber() == card2.getNumber()) {
			pairPoints = 10; // Add points if cards form a pair
		}

		// Add points if the cards are suited
		int suitedPoints = 0;
		if (card1.getSuit() == card2.getSuit()) {
			suitedPoints = 5; // Add points for suited cards
		}

		// Calculate total points
		int totalPoints = cardValuePoints + pairPoints + suitedPoints;

		// Normalize to a percentage; the maximum points would be for a pair of Aces
		return (double) totalPoints / (38) * 100;
	}

	private int convertCardValueToPokerValue(int cardNumber) {
		// Method to convert card constants to poker values for the HandStrength Method
		switch (cardNumber) {
			case Card.TWO: return 2;
			case Card.THREE: return 3;
			case Card.FOUR: return 4;
			case Card.FIVE: return 5;
			case Card.SIX: return 6;
			case Card.SEVEN: return 7;
			case Card.EIGHT: return 8;
			case Card.NINE: return 9;
			case Card.TEN: return 10;
			case Card.JACK: return 11;
			case Card.QUEEN: return 12;
			case Card.KING: return 13;
			case Card.ACE: return 14;
			default: return 0; // Default value for invalid card numbers
		}
	}

}
