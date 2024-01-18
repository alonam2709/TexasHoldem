import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// Abstract class representing a panel with cards and buttons
abstract class CardsButtons extends JPanel
  implements ActionListener {
	// 2D array to store buttons for each card
	private JButton[][] cardButton;
	private JPanel buttonsPanel;
	protected JButton dealRandom;	//button to deal random card
	// Path to the images for card suits
	private static final String SUITS_IMAGES_PATH =
		"images/suits/";


	 // Constructor for CardsButtons. Sets up the layout and initializes buttons for cards.

	public CardsButtons() {
		super( new BorderLayout() );

		dealRandom = new JButton("Deal random"); // Button to deal a random card

		dealRandom.addActionListener(this);
		// Panel to hold card buttons in a grid layout
		buttonsPanel = new JPanel( new GridLayout(5,14) );
		// Initialize the buttons for each card
		cardButton = new JButton[13][4];
		// Adding labels for card ranks
		buttonsPanel.add( new JLabel("", JLabel.CENTER) );
		for (int i=2; i<11; i++)
			buttonsPanel.add( new JLabel(""+i, JLabel.CENTER) );
		buttonsPanel.add( new JLabel("J", JLabel.CENTER) );
		buttonsPanel.add( new JLabel("Q", JLabel.CENTER) );
		buttonsPanel.add( new JLabel("K", JLabel.CENTER) );
		buttonsPanel.add( new JLabel("A", JLabel.CENTER) );
		// Adding suit images and card buttons to the panel
		for (int j=0; j<cardButton[0].length; j++) {
			String suit = Card.toString(getSuit(j));
			buttonsPanel.add( new JLabel(
					new ImageIcon(SUITS_IMAGES_PATH+suit+".gif") ));
			for (int i=0; i<cardButton.length; i++) {
				cardButton[i][j] = new JButton();
				cardButton[i][j].addActionListener(this);
				buttonsPanel.add(cardButton[i][j]);
			}
		}
		// Adding the dealRandom button and buttonsPanel to the main panel
		add(dealRandom, BorderLayout.WEST);
		add(buttonsPanel, BorderLayout.CENTER);

	}
	//Method to identify the card selected by the user.
	Card getSelected(ActionEvent e) {
		Object source = e.getSource();
		
		int number=-1;
		int suit=-1;

		for (int j=0; j<cardButton[0].length; j++) {
			for (int i=0; i<cardButton.length; i++) {
				if (source == cardButton[i][j]) {
					number = i;
					suit = j;
					break;
				}					
			}
		}

		if (number == -1 || suit == -1) {
			System.out.println("ERROR");
			return null;
		}
		
		cardButton[number][suit].setEnabled(false);
		return new Card( getNumber(number), getSuit(suit) ); //Card object representing the selected card or null if no valid selection

	}
	//Method to deal a random card.
	Card dealRandom() {
		int suit;
		int number;
		
		do {
			number = (int) Math.floor(Math.random()*13);
			suit = (int) Math.floor(Math.random()*4);
		} while (!cardButton[number][suit].isEnabled());
		
		cardButton[number][suit].setEnabled(false);
		return new Card( getNumber(number), getSuit(suit) );
	}

	// Method to get the suit based on an integer value
	private int getSuit( int i ) {
		switch(i) {
		case 0 : return Card.DIAMONDS;
		case 1 : return Card.CLUBS;
		case 2 : return Card.HEARTS;
		case 3 : return Card.SPADES;
		default : System.out.println("ERROR");
                  return -1;
		}
	}
	// Method to get the number based on an integer value
	private int getNumber( int i ) {
		switch(i) {
		case 0 : return Card.TWO;
		case 1 : return Card.THREE;
		case 2 : return Card.FOUR;
		case 3 : return Card.FIVE;
		case 4 : return Card.SIX;
		case 5 : return Card.SEVEN;
		case 6 : return Card.EIGHT;
		case 7 : return Card.NINE;
		case 8 : return Card.TEN;
		case 9 : return Card.JACK;
		case 10 : return Card.QUEEN;
		case 11 : return Card.KING;
		case 12 : return Card.ACE;
		default : System.out.println("ERROR");
		          return -1;
		}
	}
	//Method to reset all card buttons to enabled (i.e., selectable)
	public void reset() {
		for (int j=0; j<cardButton[0].length; j++) {
			for (int i=0; i<cardButton.length; i++) {
				cardButton[i][j].setEnabled(true);
			}
		}
	}
}
