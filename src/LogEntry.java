import java.io.Serializable;
import java.util.Vector;

class LogEntry implements Serializable {
    private String handDescription;
    private Vector<ProbabilityData> probabilities;

    public LogEntry(String handDescription, Vector<ProbabilityData> probabilities) {
        this.handDescription = handDescription;
        this.probabilities = probabilities;
    }

    public String getHandDescription() {
        return handDescription;
    }

    public Vector<ProbabilityData> getProbabilities() {
        return probabilities;
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