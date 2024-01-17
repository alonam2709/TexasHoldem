import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
//The Display class is a Java Swing panel that presents probabilities of various poker hands to the client.
class Display extends JPanel {

	private ProbabilityDisplay[] probDisplays; // // Array of ProbabilityDisplay for showing probabilities of different hands
	private ProbabilityOptions probOptions; // Panel for user to select probability options
	private JPanel probs; //// Panel to hold the ProbabilityDisplays

	private static final int DISPLAY_TOP_ = 8; // Number of top probabilities to display
	private static final String YOUR_PROBS =
		"Probabilities of you achieving:";
	private static final String YOU_HAVE =
		"Your Hand:";

	public Display() {
		// Setup of the main panel layout
		setLayout( new BorderLayout() );
		// Initialize and configure ProbabilityOptions component
		probOptions = new ProbabilityOptions() {
			// Event handlers for actions in the ProbabilityOptions panel
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				// Handling of events in ProbabilityOptions
				if (source == hideCheckBox) { // hiding the probabilites
					Display.this.hide(hideCheckBox.isSelected());
				}
				else for (int i=0; i<formatRadios.length; i++) { // for changing the display format
					if (source == formatRadios[i]) {
						formatRadioSelected(i);
					}
				}
				
				updateAllDisplays();
			}
			// //Handling of changes in ProbabilityOptions (e.g., decimal places)
			public void stateChanged(ChangeEvent e) {
				ProbabilityDisplay.setDecimalPlaces(
					((Integer)numOfDecimalPlacesSpinner.getValue()).intValue());
				
				updateAllDisplays();
			}
		};
		// Setup of the probabilities display panel
		probs = new JPanel();
		probs.setLayout( new GridLayout(2,(int)Math.ceil(DISPLAY_TOP_/2.0)) );
		probs.setBorder( new TitledBorder(YOUR_PROBS) );

		probDisplays = new ProbabilityDisplay[DISPLAY_TOP_];

		// Initialize and add ProbabilityDisplays to the panel
		for (int i=0; i<DISPLAY_TOP_ ; i++) {
			probDisplays[i] = new ProbabilityDisplay(
					Hand.toString(getHand(i)));
			probs.add(probDisplays[i]);
		}
		 // Adding components to the main panel
		add(probOptions, BorderLayout.WEST);
		add(probs, BorderLayout.CENTER);
	}
	
	public void reset() { // Reset the probabilities display
		probs.setBorder( new TitledBorder(YOUR_PROBS) );
		for (int i=0; i<DISPLAY_TOP_ ; i++) {
			probDisplays[i].reset();
		}
	}
	// Hide or show probability displays based on user selection
	public void hide(boolean hidden) {
		ProbabilityDisplay.setHidden(hidden);
	}
	// Set the probabilities for the different hands in the display
	public void setProbabilities( Probability[] probs ) {
		for(int i=0; i<probs.length; i++) {
			probDisplays[getDisplay(probs[i].hand)]
					.setProbability(probs[i].probability);
		}
	}
	// Update all ProbabilityDisplays with the latest data
	private void updateAllDisplays() {
		for (int i=0; i<DISPLAY_TOP_; i++) {
			probDisplays[i].updateProbabilityDisplay();
		}
	}
	// Enable or disable all ProbabilityDisplays
	public void setAllDisplaysEnabled(boolean enabled) {
		for (int i=0; i<DISPLAY_TOP_; i++) {
			probDisplays[i].setProbabilityEnabled(enabled);
		}
	}
	// Get the hand type based on its index
	private int getHand(int i) {
		switch(i) {
		case 0 : return Hand.STRAIGHT_FLUSH;
		case 1 : return Hand.FOUR_OF_A_KIND;
		case 2 : return Hand.FULL_HOUSE;
		case 3 : return Hand.FLUSH;
		case 4 : return Hand.STRAIGHT;
		case 5 : return Hand.THREE_OF_A_KIND;
		case 6 : return Hand.TWO_PAIR;
		case 7 : return Hand.ONE_PAIR;
		case 8 : return Hand.HIGH_CARD;
		default : System.out.println("ERROR : Display.getHand("+i+")");
			return -1;
		}
	}
 	// Get the display index based on the hand type
	private int getDisplay(int i) {
		switch(i) {
		case Hand.STRAIGHT_FLUSH : return 0;
		case Hand.FOUR_OF_A_KIND : return 1;
		case Hand.FULL_HOUSE : return 2;
		case Hand.FLUSH : return 3;
		case Hand.STRAIGHT : return 4;
		case Hand.THREE_OF_A_KIND : return 5;
		case Hand.TWO_PAIR : return 6;
		case Hand.ONE_PAIR : return 7;
		case Hand.HIGH_CARD : return 8;
		default : System.out.println("ERROR : Display.getDisplay("+i+")");
			return -1;
		}
	}
	// Set the display title and reset displays when a hand is complete
	public void setComplete(String yourHand) {
		probs.setBorder( new TitledBorder(
				YOU_HAVE+" "+yourHand+". ") );
		for (int j=0; j<DISPLAY_TOP_; j++) {
			probDisplays[j].reset();
		}

	}
}
