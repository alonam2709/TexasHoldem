import java.util.Vector;

class YourProbabilityFormulae extends ProbabilityFormulae { //Inheritance

	// Constructor which takes a Vector of cards as input
	public YourProbabilityFormulae(Vector<Card> cards) {
		super(cards); // Calls the superclass constructor with the provided cards
	}

	// Method to calculate the probability of getting a Straight Flush
	public double calcStraightFlush() {
		double sum = 0.0;

		// Loop through each suit
		for (int suit = Card.DIAMONDS; suit <= Card.SPADES; suit++) {
			// Loop through card numbers from 5 (inclusive) to Ace (inclusive)
			for (int number = Card.FIVE; number <= Card.ACE; number++) {
				int cardsInStraightFlush = 0;

				// Special case for low-ace straight (A-2-3-4-5)
				if (number == Card.FIVE) {
					// Check for Ace in the same suit for the low-ace straight
					if (contains(Card.ACE, suit, cards)) cardsInStraightFlush++;
					// Check for 2, 3, 4, 5 in the same suit
					for (int i = Card.TWO; i <= Card.FIVE; i++) {
						if (contains(i, suit, cards)) cardsInStraightFlush++;
					}
				} else {
					// Check for consecutive numbers in the same suit for a regular straight flush
					for (int i = number - 4; i <= number; i++) {
						if (contains(i, suit, cards)) cardsInStraightFlush++;
					}
				}

				// If 5 or more cards are in the straight flush, return a probability of 1
				if (cardsInStraightFlush >= 5) return 1.0;

				final int CARDS_IN_STRAIGHT_FLUSH = cardsInStraightFlush;
				final int NEEDED = 5 - CARDS_IN_STRAIGHT_FLUSH;

				// If the needed cards plus current cards exceed 7, continue to the next iteration
				if (NEEDED + cards.size() > 7) continue;

				double numerator = 1.0; // Numerator for probability calculation
				double denominator = 1.0; // Denominator for probability calculation

				// Calculate the denominator for the probability
				for (int i = 0; i < NEEDED; i++) {
					denominator *= (52 - cards.size() - i);
				}

				// Sum up the probability of obtaining a straight flush
				sum += MyMath.nCr(7 - cards.size(), NEEDED) * numerator / denominator;
			}
		}

		return sum; // Return the total probability of getting a straight flush
	}

	// Calculate the probability of getting a Flush
	public double calcFlush() {
		double sum = 0.0;

		// Iterate through each suit
		for (int suit = Card.DIAMONDS; suit <= Card.SPADES; suit++) {
			// Count the number of cards of the current suit
			final int CARDS_IN_FLUSH = count(ANY_NUMBER, suit, cards);
			// Calculate the number of cards needed to complete the Flush
			final int NEEDED = 5 - CARDS_IN_FLUSH;

			// Skip the calculation if more cards are needed than available
			if (NEEDED + cards.size() > 7) continue;
			// If Flush is already achieved, return a probability of 1
			if (NEEDED <= 0) return 1.0;

			double numerator = 1.0;
			double denominator = 1.0;

			// Calculate the probability considering the remaining cards in the deck
			for (int i = 0; i < NEEDED; i++) {
				numerator *= (13 - CARDS_IN_FLUSH - i); // Remaining suit cards
				denominator *= (52 - cards.size() - i); // Remaining total cards
			}

			// Add the probability for the current suit to the sum
			sum += MyMath.nCr(7 - cards.size(), NEEDED) * numerator / denominator;
		}

		return sum; // Return the total probability for all suits
	}

	// Calculate the probability of getting a Full House
	public double calcFullHouse() {
		// Count the pairs and three-of-a-kinds in the current hand
		final int PAIRS = countXofaKind(2, cards);
		final int TRIPS = countXofaKind(3, cards);

		// Check for a Full House with one three-of-a-kind and at least one pair
		if (TRIPS == 1 && PAIRS >= 1) return 1.0;
		// Check for a Full House with two three-of-a-kinds
		if (TRIPS == 2) return 1.0;

		// Probability calculations based on the number of cards in hand
		switch (cards.size()) {
			case 2: {
				if (PAIRS == 1) {
					// Probability of getting two different cards to form a Full House
					return (2376.0 / 264845.0);
				} else {
					// Probability with only two cards and nothing special in hand
					return (9207.0 / 5296900.0);
				}
			}
			case 5: {
				if (TRIPS == 1) {
					// Probability with a three-of-a-kind already in hand
					return (195.0 / 1081.0);
				}
				switch (PAIRS) {
					case 2:
						// Probability with two pairs in hand
						return (90.0 / 1081.0);
					case 1:
						// Probability with one pair in hand
						return (18.0 / 1081.0);
					default:
						// No chance of Full House with this setup
						return 0.0;
				}
			}
			case 6: {
				if (TRIPS == 1) {
					// Only one card needed to complete Full House
					return (3.0 / 23.0);
				}
				// Check different pair combinations
				switch (PAIRS) {
					case 3:
					case 2:
						// Probabilities with different numbers of pairs in hand
						return (3.0 / 23.0);
					default:
						// No chance of Full House with this setup
						return 0.0;
				}
			}
			case 7:
				// With 7 cards, if no Full House yet, it's impossible
				return 0.0;
			default:
				// Error case, should not occur in normal circumstances
				System.out.println("ERROR: YourProbabilityFormulae.calcFullHouse()");
				return -1;
		}
	}

	// Calculate the probability of getting 'X' of a Kind (e.g., Pair, Three of a Kind, etc.)
	public double calcXofaKind(int x) {
		double sum = 0.0;

		// Iterate through each card in the current hand
		for (int i = 0; i < cards.size(); i++) {
			// Get the number (rank) of the current card
			final int NUMBER = ((Card) cards.elementAt(i)).getNumber();
			// Calculate the number of additional cards needed to achieve 'X' of a Kind
			final int NEEDED = x - count(NUMBER, ANY_SUIT, cards);

			// Skip calculation if more cards are needed than available
			if (NEEDED + cards.size() > 7) continue;
			// If 'X' of a Kind is already achieved, return a probability of 1
			if (NEEDED <= 0) return 1.0;

			// Initialize numerator for probability calculation
			double numerator = MyMath.factorial(NEEDED);
			// Initialize denominator for probability calculation
			double denominator = 1.0;

			// Calculate the denominator considering the remaining cards in the deck
			for (int k = 0; k < NEEDED; k++) {
				denominator *= (52 - cards.size() - k);
			}

			// Calculate and add the probability for this specific card number
			// MyMath.nCr calculates the combination (number of ways to choose 'NEEDED' cards from the remaining)
			sum += MyMath.nCr(7 - cards.size(), NEEDED) * numerator / denominator;
		}

		return sum; // Return the total probability of achieving 'X' of a Kind
	}

	// Calculate the probability of getting a Straight
	public double calcStraight() {
		double sum = 0.0;

		// Loop through possible highest cards of a straight from 5 to Ace
		for (int number = Card.FIVE; number <= Card.ACE; number++) {
			int cardsInStraight = 0;

			// Special case: Low-ace straight (A-2-3-4-5)
			if (number == Card.FIVE) {
				// Check for Ace in any suit
				if (contains(Card.ACE, ANY_SUIT, cards)) cardsInStraight++;
				// Check for 2, 3, 4, 5 in any suit
				for (int i = Card.TWO; i <= Card.FIVE; i++) {
					if (contains(i, ANY_SUIT, cards)) cardsInStraight++;
				}
			} else {
				// Regular case: Check for consecutive numbers in any suit
				for (int i = number - 4; i <= number; i++) {
					if (contains(i, ANY_SUIT, cards)) cardsInStraight++;
				}
			}

			// If 5 or more cards are in the straight, return probability 1
			if (cardsInStraight >= 5) return 1.0;

			// Calculate the number of cards needed to complete the Straight
			final int CARDS_IN_STRAIGHT = cardsInStraight;
			final int NEEDED = 5 - CARDS_IN_STRAIGHT;

			// Skip the iteration if more cards are needed than available
			if (NEEDED + cards.size() > 7) continue;

			// Numerator for probability calculation (fixed to 1)
			double numerator = Math.pow(4, NEEDED); // 4 possibilities for each card rank

			// Denominator for probability calculation
			double denominator = 1.0;
			for (int i = 0; i < NEEDED; i++) {
				denominator *= (52 - cards.size() - i); // Total remaining cards
			}

			// Add the probability of achieving the straight with this highest card
			sum += MyMath.nCr(7 - cards.size(), NEEDED) * numerator / denominator;
		}

		return sum; // Return the total probability of getting a Straight
	}

	// Calculate the probability of getting Two Pair
	public double calcTwoPair() {
		// If there's already a Three of a Kind, Two Pair is not possible
		if (countXofaKind(3, cards) > 0) return 0.0;

		// Count the number of pairs in the hand
		final int PAIRS = countXofaKind(2, cards);

		// If there are already two pairs (or more), the probability of Two Pair is 1
		if (PAIRS >= 2) return 1.0;

		// Probability calculations based on the number of cards in hand
		switch (cards.size()) {
			case 2: {
				if (PAIRS == 0) {
					// Probability of getting two different pairs with only two cards in hand
					return (594.0 / 52969.0);
				}
				if (PAIRS == 1) {
					// Probability of getting another pair with one pair already in hand
					return (38016.0 / 264845.0);
				}
			}
			case 5: {
				if (PAIRS == 0) {
					// Probability of getting two pairs with five cards and no pairs yet
					return (90.0 / 1081.0);
				}
				if (PAIRS == 1) {
					// Probability of getting another pair with one pair already in hand and three cards to draw
					return (378.0 / 1081.0);
				}
			}
			case 6: {
				if (PAIRS == 0) {
					// Not possible to get Two Pair with no pairs and only one card left to draw
					return 0.0;
				}
				if (PAIRS == 1) {
					// Probability of getting another pair with one pair already in hand and two cards to draw
					return (6.0 / 23.0);
				}
			}
			case 7: {
				// With 7 cards, if no Two Pair yet, it's impossible
				return 0.0;
			}
			default:
				// Error case, should not occur in normal circumstances
				System.out.println("ERROR: YourProbabilityFormulae.calcTwoPair()");
				return -1;
		}
	}
}