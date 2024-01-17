import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
/*
 Class representing the options panel for configuring how probabilities are displayed.
 This includes options for the format of probability display and the number of decimal places.
 */
public abstract class ProbabilityOptions extends JPanel
  implements ActionListener, ChangeListener {

	private static final int INIT_DECIMAL_PLACES = 2;// Initial number of decimal places
	private static final String TITLE = "Prob Opts"; // Title for the options panel
	private static final String HIDE = "hide"; // Label for the hide checkbox
	private static final String DECIMAL_PLACES = "dps";// Label for decimal places


	protected JRadioButton[] formatRadios; // Radio buttons for format selection
	private ButtonGroup buttonGroup;// Group for radio buttons
	protected JCheckBox hideCheckBox; // Checkbox to hide or show probabilities
	private JPanel numOfDecimalPlaces; // Panel for choosing number of decimal places
	protected JSpinner numOfDecimalPlacesSpinner; // Spinner for decimal places

    //Constructor for ProbabilityOptions. Initializes the components and layout.
	public ProbabilityOptions() {
		setLayout( new GridLayout(5,1) );
		setBorder( new TitledBorder(TITLE) );
		// Initialize and set up radio buttons for selecting probability format
		formatRadios = new JRadioButton[3];

		buttonGroup = new ButtonGroup();
		for (int i=0; i<3; i++) {
			// Create radio buttons for each probability format
			formatRadios[i] = new JRadioButton(
					ProbabilityDisplay.toString(
							getFormat(i)));
			formatRadios[i].addActionListener(this); // Add action listener
			add(formatRadios[i]); // Add to panel
			buttonGroup.add(formatRadios[i]);// Add to button group
		}
		// Initialize and set up hide checkbox
		hideCheckBox = new JCheckBox(HIDE, false);
		hideCheckBox.addActionListener(this); // Add action listener
		add(hideCheckBox); // Add to panel
		// Create and configure spinner for selecting number of decimal places
		SpinnerNumberModel numOfDecimalPlacesModel = new SpinnerNumberModel(
				INIT_DECIMAL_PLACES, // initial value
				0, // Minimum Value
				16, // Maximum Value
				1); // Incremments
		ProbabilityDisplay.setDecimalPlaces(INIT_DECIMAL_PLACES);
		numOfDecimalPlacesSpinner = new JSpinner(numOfDecimalPlacesModel);
		numOfDecimalPlacesSpinner.addChangeListener(this);// Add change listener

		// Set up panel for decimal places
		numOfDecimalPlaces = new JPanel();
		numOfDecimalPlaces.setLayout(new FlowLayout());
		numOfDecimalPlaces.add(new JLabel(DECIMAL_PLACES)); // Label for decimal places
		numOfDecimalPlaces.add(numOfDecimalPlacesSpinner); // Add spinner
		add(numOfDecimalPlaces); // Add to main panel

		// Set default format selection
		formatRadios[0].setSelected(true);
		formatRadioSelected(0); // Update display format based on selection
	}
	//Handles the selection of a probability format radio button.
	public void formatRadioSelected(int i) {
		ProbabilityDisplay.setFormat( getFormat(i) );
	}
	// Gets the probability format based on the given index.
	private int getFormat(int i) {
		switch(i) {
		case 0 : return ProbabilityDisplay.DECIMAL;
		case 1 : return ProbabilityDisplay.FRACTION;
		case 2 : return ProbabilityDisplay.PERCENT;
		default : System.out.println("ERROR"); return -1;
		}
	}
	
	
}
