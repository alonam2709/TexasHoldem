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
import java.util.Vector;

/*
 * Main class for a Texas Hold'em poker game application.
 * This class sets up the game's user interface and handles the game's logic.
 */
public class TexasHoldem extends Applet
  implements ActionListener {

	private static final String TITLE =
		"Texas Hold'em";
	
	private static final String ICON_FILENAME =
		"images/icons/small_icon.gif";

	private CardsButtons cardsButtons; // Panel for card buttons
	private CardsDisplay cardsDisplay; // Panel for displaying cards
	private Display display; // Display for showing game information
	private JButton resetButton; // Button to reset the game
	private JLabel handStrengthLabel; // Label to display hand strength
	private HandLogList handLogList;

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

	    // Successful exit on window close events
	    jf.addWindowListener( new WindowAdapter()
			{
				public void windowClosing( WindowEvent e )
				{
					hep.destroy();
					System.exit(0);
				}
            });

	    ImageIcon icon = new ImageIcon(ICON_FILENAME);
		jf.setIconImage(icon.getImage());
		jf.setResizable(false);
		jf.getContentPane().add(hep);
		jf.pack();
		
		// centre on screen
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
	
	public void init() {

		hand = new Hand();
		// Initialize the hand strength label and add it to the GUI
		handStrengthLabel = new JLabel("           0%");
		// Setup for cardsButtons, cardsDisplay, display, etc.
		// Implementation of init method.
		cardsButtons = new CardsButtons() {
			public void actionPerformed(ActionEvent e) {
				if (hand.isComplete()) return;
				Card c = null;
				if (e.getSource() == dealRandom) c = dealRandom();
				else c = getSelected(e);
				cardsDisplay.setCard(c);
				hand.addCard(c);
				updateDisplay();
				
			}
		};
		
		cardsDisplay = new CardsDisplay();

		display = new Display();

		resetButton = new JButton("Reset");
		handLogList = new HandLogList();
		JButton outputLogsToTextButton = new JButton("Load Log"); // Renamed button
		outputLogsToTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputHandLogsToTextFile();
			}
		});
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		buttonsPanel.add(new JLabel("Hand Strength:"));
		buttonsPanel.add(handStrengthLabel);
		buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		buttonsPanel.add(outputLogsToTextButton);
		//buttonsPanel.add(saveGameButton);
		buttonsPanel.add(resetButton, BorderLayout.SOUTH);
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		
		this.setLayout( new BorderLayout() );
		this.add(cardsButtons, BorderLayout.NORTH);
		this.add(cardsDisplay, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.WEST);
		this.add(display, BorderLayout.SOUTH);
		
		resetButton.addActionListener(this);

	}
	private void outputHandLogsToTextFile() {
		Iterable<LogEntry> handLogs = handLogList.getLogEntries();
		String fileName = "hand_logs.txt";

		try (FileWriter writer = new FileWriter(fileName)) {
			// Iterate over the hand logs and write them to the text file
			for (LogEntry logEntry : handLogs) {
				writer.write(logEntry.toString());
				writer.write("\n"); // Add a newline between entries
			}

			// Save the game state
			handLogList.saveToFile(fileName);

			JOptionPane.showMessageDialog(this, "Hand logs and game state saved to " + fileName,
					"Hand Logs and Game State Saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to save hand logs and game state to " + fileName,
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void reset() {
		cardsButtons.reset();
		cardsDisplay.reset();
		display.reset();
		hand = new Hand();
	}

	public void actionPerformed(ActionEvent e) {
		
		if ( e.getSource().equals(resetButton) ) {
			reset();
		}
	}

	private void updateDisplay() {
		// Update the hand strength display if there are exactly two cards
		if (hand.cards.size() >= 2) {
			double strength = hand.calculatePreFlopHandStrength();
			handStrengthLabel.setText(String.format("%.2f%%", strength));
		} else {
			handStrengthLabel.setText("N/A");
		}
		if ( hand.isComplete() ) {
			display.setComplete(Hand.toString(hand.getBestHand()));
		}
		if ( hand.requiresUpdate() ) {
			display.setAllDisplaysEnabled(true);
			display.setProbabilities( hand.getProbabilities() );
		}
		else {
			display.setAllDisplaysEnabled(false);
		}

	}
}
