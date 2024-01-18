import java.io.Serializable;

class ProbabilityData implements Serializable {
    private String handType;
    private double probability;

    public ProbabilityData(String handType, double probability) {
        this.handType = handType;
        this.probability = probability;
    }

    @Override
    public String toString() {
        return handType + ": " + String.format("%.2f%%", probability * 100);
    }
}