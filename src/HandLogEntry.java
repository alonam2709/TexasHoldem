import java.io.Serializable;
import java.util.Vector;

class HandLogEntry implements Serializable {
    private String handDescription;
    private Vector<ProbabilityData> probabilities;

    public HandLogEntry(String handDescription, Vector<ProbabilityData> probabilities) {
        this.handDescription = handDescription;
        this.probabilities = probabilities;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(handDescription + "\nProbabilities:\n");
        for (ProbabilityData pd : probabilities) {
            sb.append(pd.toString()).append("\n");
        }
        return sb.toString();
    }
}