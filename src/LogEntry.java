import java.util.Vector;

class LogEntry  {
    // String to store the description of the poker hand
    private String handDescription;
    // Vector to store ProbabilityData objects for each hand type
    private Vector<ProbabilityData> probabilities;

    // Constructor to initialize LogEntry with a hand description and probabilities
    public LogEntry(String handDescription, Vector<ProbabilityData> probabilities) {
        this.handDescription = handDescription;
        this.probabilities = probabilities;
    }

    // Getter method for hand description
    public String getHandDescription() {
        return handDescription;
    }

    // Getter method for probabilities
    public Vector<ProbabilityData> getProbabilities() {
        return probabilities;
    }

    // Override toString method to return a string representation of the LogEntry
    @Override
    public String toString() {
        // StringBuilder used for efficient string concatenation
        StringBuilder sb = new StringBuilder(handDescription + "\nProbabilities:\n");
        // Loop through each ProbabilityData object and append its string representation
        for (ProbabilityData pd : probabilities) {
            sb.append(pd.toString()).append("\n");
        }
        // Return the complete string representation of the LogEntry
        return sb.toString();
    }
}