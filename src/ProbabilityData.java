import java.io.Serializable;

class ProbabilityData implements Serializable {
    // String to store the type of poker hand (e.g., Flush, Straight)
    private String handType;
    // Double to store the probability of achieving this hand type
    private double probability;

    // Constructor to initialize ProbabilityData with a hand type and its probability
    public ProbabilityData(String handType, double probability) {
        this.handType = handType;
        this.probability = probability;
    }

    // Override toString method to return a string representation of ProbabilityData
    @Override
    public String toString() {
        // Formats the probability as a percentage with two decimal places
        return handType + ": " + String.format("%.2f%%", probability * 100);
    }
}