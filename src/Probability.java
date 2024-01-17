public class Probability {

	public int hand; // // An integer representing a specific type of poker hand
	public double probability; // The probability of obtaining this hand
	
	public Probability(int hand, double probability) { //Constructor for Probability class
		// Check if the hand type is valid
		if (!Hand.isValid(hand)) System.out.println(
				"ERROR : Probability() invalid hand="+hand);
		// Check if the probability value is valid (i.e., between 0 and 1 inclusive)
		if (probability<0 || probability>1) System.out.println(
				"ERROR : Probability() invalid probability="+probability);
		// Assign values to the instance variables
		this.hand = hand;
		this.probability = probability;
	}
}
