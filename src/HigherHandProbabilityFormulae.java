
import java.util.Vector;

//Class for calculating probabilities of achieving higher poker hands.
class HigherHandProbabilityFormulae extends ProbabilityFormulae { //Inheritance

	private Vector yourCards;
	private Vector communityCards;

	public HigherHandProbabilityFormulae(Vector cards) {
		super(cards);
		// Constructor that separates the given cards into 'your' cards and community cards.
		yourCards = new Vector(2); // Vector storing your cards
		communityCards = new Vector(5); // Vector storing community cards
		// Separating the first two cards as 'your' cards
		for (int i=0; i<2; i++)
			yourCards.add(cards.elementAt(i));
		// The remaining cards are treated as community cards
		for (int i=2; i<7; i++)
			communityCards.add(cards.elementAt(i));
	}

	// All these methods are PlaceHolder Values and will be replaced with actual calculations from the probability class
	public double calcStraightFlush() {
		
		return 1;
	}

	public double calcFlush() {
		return 1;
	}

	public double calcFullHouse() {
		return 1;
	}

	public double calcXofaKind(int x) {
		return 1;
	}

	public double calcStraight() {
		return 1;
	}

	public double calcTwoPair() {
		return 1;
	}

}

