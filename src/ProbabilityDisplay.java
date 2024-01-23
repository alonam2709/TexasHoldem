import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import java.awt.Color;
/*
 * Class for displaying the probability of a specific poker hand.
 * It supports various formats for displaying probabilities, including decimal, fraction, and percent.
 */
public class ProbabilityDisplay extends JPanel {
	// Constants for different probability display formats
	public static final int DECIMAL = 400;
	public static final int FRACTION = 401;
	public static final int PERCENT = 402;
	// Default and special labels for probability
	private static final String DEFAULT = "###";
	private static final String GUARANTEED = "guaranteed";
	private static final String IMPOSSIBLE = "impossible";

	private JLabel probability; // Label to display the probability
	private static int format; // Current format for displaying probabilities
	private static int decimalPlaces; // Number of decimal places to display
	private static boolean isHidden; // Flag to hide the probability

	private double prob; // The probability value
	private String name; // The name of the hand
	
	public ProbabilityDisplay( String name ) { //Constructor for the class
		this.name = name;
		this.setBorder( new TitledBorder(name) );
		this.probability = new JLabel(DEFAULT);
		setProbabilityEnabled(false);
		ProbabilityDisplay.format = DECIMAL;
		ProbabilityDisplay.decimalPlaces = 2;
		this.probability.setForeground(Color.BLACK);

		this.add(probability, SwingConstants.CENTER);
	}

	public static String toString( int formatType ) {
		switch(formatType) {
		case DECIMAL : return "decimal";
		case FRACTION : return "fraction";
		case PERCENT : return "percent";
		default : return "ERROR";
		}
	}
	// Implementation of reset method
	public void reset() {
		this.probability.setText(DEFAULT);
		setProbabilityEnabled(false);
		this.setBorder( new TitledBorder(name) );
//		this.setBorder( new TitledBorder(
//				BorderFactory.createRaisedBevelBorder(),
//				name) );
		setVisible(true);
	}
	// Implementation of setting and updating probability
	public void setProbability(double prob) {
		this.prob = prob;
		updateProbabilityDisplay();
	}
	// Implementation of updating the probability display
	public void setProbabilityEnabled(boolean enabled) {
		this.probability.setEnabled(enabled);
	}

	// Implementation of updating the probability display
	public void updateProbabilityDisplay() {
		String s = format(prob);
		if (s.equals(GUARANTEED)) {
			//this.probability.setEnabled(false);
			this.probability.setForeground(Color.GREEN); // Set text color to green
		} else if (s.equals(IMPOSSIBLE)) {
			//setProbabilityEnabled(false);
			this.probability.setForeground(Color.RED); // Set text color to red
		} else {
			this.probability.setForeground(Color.BLACK); // Set text color to black for other cases
		}
		this.probability.setText(s);
	}
	// Implementation of formatting the probability value
	private String format(double prob) {
		if (isHidden) return "hidden";
		if (prob == 1.0) return GUARANTEED;
		if (prob == 0.0) return IMPOSSIBLE;
		
		switch(format) {
		case DECIMAL : return cutDecimalPlaces(prob);
		case FRACTION : return "1/"+cutDecimalPlaces(1.0/prob);
		case PERCENT : return cutDecimalPlaces(100*prob)+"%";
		default : return "ERROR";
		}
	}
	// Static methods to set format, decimal places, and hidden state
	public static void setFormat( int f ) {
		ProbabilityDisplay.format = f;
	}

	public static void setDecimalPlaces( int dp ) {
		ProbabilityDisplay.decimalPlaces = dp;
	}

	public static void setHidden( boolean h ) {
		ProbabilityDisplay.isHidden = h;
	}

	// Method that cuts double to number of decimal places.
	// Not rounding off
	private String cutDecimalPlaces(double d) {
		// Convert the double to a string
		String s = Double.toString(d);

		// Find the position of the exponent 'E' in scientific notation (if present)
		int exp = s.indexOf("E");
		// If exponent is present, store the exponent part for later use, else store an empty string
		String end = (exp == -1) ? "" : s.substring(exp);

		// Find the position of the decimal point
		int decimalPoint = s.indexOf(".");
		// If there's no decimal point (integer value), return the string as is
		if (decimalPoint == -1) return s;

		// Calculate the offset to determine where to cut the string
		// If decimalPlaces is 0, offset is 0, else it's decimalPlaces + 1 to include the decimal point itself
		int offset = (decimalPlaces == 0) ? 0 : decimalPlaces + 1;

		// Calculate the index to cut the string
		// If there's no exponent, cut at the minimum of (decimal point + offset) and string length
		// If there is an exponent, cut before the exponent part starts
		int i = (exp == -1)
				? Math.min(decimalPoint + offset, s.length())
				: Math.min(decimalPoint + offset, exp);

		// Return the substring from start to the calculated index, and append the exponent part if present
		return s.substring(0, i) + end;
	}

}
