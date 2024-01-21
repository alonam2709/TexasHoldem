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
	//private JButton saveGameButton; // New button to save the game
	private HandLogQueue handLogQueue;
	//private JButton showLogsButton; // Button to show the last 10 hands
	//private HandLogQueue handLogQueue; // Queue in order to store the last 10 hands
	//private static final String LOG_FILE_NAME = "hand_logs.dat";

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
		//saveGameButton = new JButton("Save Game");
		handLogQueue = new HandLogQueue();
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
		//outputLogsToTextButton.addActionListener(this);
		
	}
	/*
	private void saveGame() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saved_game.dat"))) {
			oos.writeObject(hand);
			oos.writeObject(cardsButtons.getButtonStates());
			oos.writeObject(cardsDisplay.getDisplayedCard());
			oos.writeObject(display.getProbabilities());
			System.out.println("Game saved successfully.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to save the game.");
		}
	}


	 */
/*
	private void outputHandLogsToTextFile() {
		System.out.println("Outputting Hand Logs to Text...");
		HandLogQueue loadedQueue = HandLogQueue.loadFromFile(LOG_FILE_NAME);

		if (loadedQueue != null) {
			try (PrintWriter writer = new PrintWriter(new FileWriter("hand_logs.txt"))) {
				for (LogEntry entry : loadedQueue.getLogEntries()) {
					writer.println(entry.toString() + "\n");
				}
				System.out.println("Hand logs have been successfully output to 'hand_logs.txt'.");
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Failed to output hand logs to the text file.");
			}
		} else {
			System.out.println("No hand logs loaded.");
		}
	}
 */
	private void outputHandLogsToTextFile() {
		Iterable<LogEntry> handLogs = handLogQueue.getLogEntries();
		String fileName = "hand_logs.txt";

		try (FileWriter writer = new FileWriter(fileName)) {
			// Iterate over the hand logs and write them to the text file
			for (LogEntry logEntry : handLogs) {
				writer.write(logEntry.toString());
				writer.write("\n"); // Add a newline between entries
			}

			JOptionPane.showMessageDialog(this, "Hand logs saved to " + fileName,
					"Hand Logs Saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to save hand logs to " + fileName,
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
/*
	private void displayHandLogs() {
		System.out.println("Displaying Hand Logs...");

		// Load the hand logs from the file
		HandLogQueue loadedQueue = HandLogQueue.loadFromFile(LOG_FILE_NAME);

		if (loadedQueue != null) {
			JFrame logFrame = new JFrame("Hand Logs");
			JTextArea logArea = new JTextArea();
			logArea.setEditable(false);

			// Iterate over the loaded logs
			for (HandLogEntry entry : loadedQueue) {
				logArea.append(entry.toString() + "\n\n");
			}

			logFrame.add(new JScrollPane(logArea));
			logFrame.setSize(400, 600); // Set preferred size
			logFrame.setVisible(true);
		} else {
			System.out.println("No hand logs loaded.");
		}
	}

 */

}
