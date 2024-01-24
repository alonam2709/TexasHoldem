import java.applet.Applet;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;


/*
 * Main class for a Texas Hold'em poker game application.
 * This class sets up the game's user interface and handles the game's logic.
 */
public class TexasHoldem extends Applet
  implements ActionListener {

	private static final String TITLE = "Texas Hold'em";
	private static final String ICON_FILENAME = "images/icons/small_icon.gif";

	// Swing components for the game interface
	private CardsButtons cardsButtons; // Panel with buttons for each card
	private CardsDisplay cardsDisplay; // Panel for displaying selected cards
	private Display display; // Display for showing game probabilities and information
	private JButton resetButton; // Button to reset the game state
	private JLabel handStrengthLabel; // Label to display the strength of the player's hand
	private HandLogList handLogList; // Component to manage hand logs

	private Hand hand; // Represents the player's hand
	
	private static boolean isRunningAsApplet = true;  // Flag to check if running as an applet

	/*
	 * Main method to run the application as a standalone program.
	 */

	public static void main(String[] args) {
		
		isRunningAsApplet = false;
		
		JFrame jf = new JFrame(TITLE);
		final TexasHoldem hep = new TexasHoldem();
		hep.init();

		// Setup window listener for closing operations
	    jf.addWindowListener( new WindowAdapter()
			{
				public void windowClosing( WindowEvent e )
				{
					hep.destroy();
					System.exit(0);
				}
            });
		// Set application icon and configure the window
	    ImageIcon icon = new ImageIcon(ICON_FILENAME);
		jf.setIconImage(icon.getImage());
		jf.setResizable(false);
		jf.getContentPane().add(hep);
		jf.pack();

		// Center the window on the user's screen
		Dimension screenSize =
			Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =
			jf.getContentPane().getPreferredSize(); 
		jf.setLocation( (int) (screenSize.getWidth() - frameSize.getWidth())/2, 
				(int) (screenSize.getHeight() - frameSize.getHeight())/2);

		jf.show();
		//Initializes the applet or application.
		// Sets up the UI components and initializes the game state.
		
	}
	// Initializes the game's user interface components
	public void init() {

		hand = new Hand();
		// Initialize the hand strength label and add it to the GUI
		handStrengthLabel = new JLabel("           0%");
		// Setup for cardsButtons, cardsDisplay, display, etc.
		// Setup for the panel with buttons for each card
		cardsButtons = new CardsButtons() {
			// Define the action to be performed when a card button is clicked
			public void actionPerformed(ActionEvent e) {
				// Check if the hand is already complete (all cards dealt)
				if (hand.isComplete()) return;
				// Exit the method if the hand is complete
				Card c = null;
				// Check if the source of the action event is the 'dealRandom' button
				if (e.getSource() == dealRandom) c = dealRandom();
					// Deal a random card if the 'dealRandom' button was pressed
				else c = getSelected(e);
				// Otherwise, get the card selected by the user
				// Display the selected or random card in the CardsDisplay panel
				cardsDisplay.setCard(c);
				// Add the selected or random card to the player's hand
				hand.addCard(c);
				// Update the display (e.g., hand strength, probabilities) based on the current hand state
				updateDisplay();
				
			}
		};
		// Initialize the CardsDisplay panel which shows the cards selected by the player
		cardsDisplay = new CardsDisplay();
		// Initialize the Display panel which shows game information and probabilities
		display = new Display();
		// Initialize the reset button for resetting the game state
		resetButton = new JButton("Reset");
		// Initialize the HandLogList which manages the logs of hands played
		handLogList = new HandLogList();
		// Initialize the button for outputting hand logs to a text file
		JButton outputLogsToTextButton = new JButton("Load Log");
		// Add an action listener to the button for handling click events
		outputLogsToTextButton.addActionListener(new ActionListener() {
			// Call the method to output hand logs to a text file when the button is clicked
			public void actionPerformed(ActionEvent e) {
				outputHandLogsToTextFile();
			}
		});
		// Initialize a panel to hold various buttons and labels
		JPanel buttonsPanel = new JPanel();
		// Set the layout of the panel to arrange components vertically
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		// Add a label for hand strength to the panel
		buttonsPanel.add(new JLabel("Hand Strength:"));
		// Add the hand strength label (showing the numerical strength of the hand)
		buttonsPanel.add(handStrengthLabel);
		// Add some vertical space between components in the panel
		buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		// Add the button for outputting hand logs to the panel
		buttonsPanel.add(outputLogsToTextButton);
		// Add the reset button to the bottom of the panel
		buttonsPanel.add(resetButton, BorderLayout.SOUTH);
		// Set a border for the panel for visual aesthetics
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		// Set the layout of the TexasHoldem applet to BorderLayout
		this.setLayout(new BorderLayout());
		// Add the CardsButtons panel to the top of the layout
		this.add(cardsButtons, BorderLayout.NORTH);
		// Add the CardsDisplay panel to the center of the layout
		this.add(cardsDisplay, BorderLayout.CENTER);
		// Add the buttons panel to the west (left side) of the layout
		this.add(buttonsPanel, BorderLayout.WEST);
		// Add the Display panel to the bottom of the layout
		this.add(display, BorderLayout.SOUTH);

		// Add an action listener to the reset button to handle click events
		resetButton.addActionListener(this);

	}
	private void outputHandLogsToTextFile() { // Method to output the hand logs to a text file
		// Retrieve an iterable collection of LogEntry objects from the handLogList
		Iterable<LogEntry> handLogs = handLogList.getLogEntries();
		// Define the name of the file where logs will be saved
		String fileName = "hand_logs.txt";

		// Use try-with-resources to handle the FileWriter resource automatically
		try (FileWriter writer = new FileWriter(fileName)) {
			// Iterate over each LogEntry in the handLogs
			for (LogEntry logEntry : handLogs) {
				// Write the string representation of each LogEntry to the file
				writer.write(logEntry.toString());
				// Add a newline after each entry for readability in the text file
				writer.write("\n");
			}

			// After writing all logs, save the current game state to the file
			handLogList.saveToFile(fileName);

			// Display an information dialog to inform the user that logs are saved
			JOptionPane.showMessageDialog(this,
					"Hand logs and game state saved to " + fileName,
					"Hand Logs and Game State Saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException ex) {
			// In case of an IOException, print the stack trace for debugging
			ex.printStackTrace();
			// Show an error dialog to inform the user about the failure
			JOptionPane.showMessageDialog(this,
					"Failed to save hand logs and game state to " + fileName,
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Method to reset the game to its initial state
	private void reset() {
		// Call the reset method on the cardsButtons object
		// This clears any selected state on the card buttons
		cardsButtons.reset();

		// Call the reset method on the cardsDisplay object
		// This clears the displayed cards from the UI
		cardsDisplay.reset();

		// Call the reset method on the display object
		// This resets the game information and probabilities display
		display.reset();

		// Reinitialize the hand object for a new game
		// This starts a new hand for the player
		hand = new Hand();
	}

	// Method to handle action events generated by UI components
	public void actionPerformed(ActionEvent e) {
		// Check if the source of the action event is the resetButton
		if (e.getSource().equals(resetButton)) {
			// If the resetButton was clicked, call the reset method
			reset();
		}
	}

	// Method to update various display elements based on the current state of the hand
	private void updateDisplay() {
		// Check if there are at least two cards in the hand
		if (hand.cards.size() >= 2) {
			// Calculate the pre-flop hand strength
			double strength = hand.calculatePreFlopHandStrength();
			// Update the hand strength label with the calculated strength as a percentage
			handStrengthLabel.setText(String.format("%.2f%%", strength));
		} else {
			// If there are less than two cards, set the hand strength label to "N/A"
			handStrengthLabel.setText("N/A");
		}

		// Check if the hand is complete (all cards have been dealt)
		if (hand.isComplete()) {
			// If the hand is complete, update the display to show the best hand obtained
			display.setComplete(Hand.toString(hand.getBestHand()));
		}

		// Check if the hand requires an update (e.g., after a new card is added)
		if (hand.requiresUpdate()) {
			// Enable all displays and update the probabilities on the display
			display.setAllDisplaysEnabled(true);
			display.setProbabilities(hand.getProbabilities());
		} else {
			// If no update is requireed, disable all displays
			display.setAllDisplaysEnabled(false);
		}
	}
}
