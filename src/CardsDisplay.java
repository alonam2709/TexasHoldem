import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
/*The CardsDisplay class manages
 the graphical representation of the cards using JLabels with ImageIcons, allowing for dynamic updates
 to the card display during the game.
 */
class CardsDisplay extends JPanel {

	private JPanel pocket; // Panel for personal hand of User
	private JPanel community; // Panel for community cards
	private JLabel[] cardLabels; // array of labels to display card images
	public static final Color GREEN = new Color(0,128,0);
	private static final String CARDS_IMAGES_PATH =
		"images/cards/";
	
	private ImageIcon nullImage; //Placeholder image for an empty card slot
	
	private int nextCard; // Index for the next card to be added
	
	public CardsDisplay() {

		// Initialize the placeholder image
		nullImage = new ImageIcon(
			CARDS_IMAGES_PATH+"null.gif");

		// Initialize card labels with the placeholder image
		cardLabels = new JLabel[7];
		for (int i=0; i<cardLabels.length; i++)
		{
			cardLabels[i] = new JLabel(nullImage);
		}
		
	   // Set up the pocket panel
		pocket = new JPanel();
		pocket.setBorder( new TitledBorder("Pocket") );
		pocket.setLayout( new GridLayout(1,2) );
		pocket.setBackground( GREEN );

		// Add two card labels to the pocket panel
		pocket.add( cardLabels[0] );
		pocket.add( cardLabels[1] );

		// Set up the community panel
		community = new JPanel();
		community.setLayout( new GridLayout(1,7) );
		community.setBorder( new TitledBorder("Community") );
		community.setBackground( GREEN );

		// Add five card labels to the community panel, separated by empty labels
		community.add( cardLabels[2] );
		community.add( cardLabels[3] );
		community.add( cardLabels[4] );
		community.add( new JLabel() ); // Empty label for spacing
		community.add( cardLabels[5] );
		community.add( new JLabel() );  // Another empty label for spacing
		community.add( cardLabels[6] );

		// Configure the layout of the CardsDisplay panel and add the pocket and community panels
		this.setLayout( new FlowLayout() );
		this.add( pocket );
		this.add( community );
		
		nextCard = 0; // Initialize the index for the next card
	}
	// Method to set a card image in the next available card label.
	public void setCard(Card c) {
		
		// Validation, Ensure that there are no more than 7 cards
		if (nextCard>=7) return;
		// Create an ImageIcon for the card and set it to the next card label
		ImageIcon i = new ImageIcon(
				CARDS_IMAGES_PATH +
				Card.toString(c.getNumber()) + "of" +
				Card.toString(c.getSuit()) + ".gif");
		
		cardLabels[nextCard++].setIcon( i );

	}
	//Method to reset the card display by setting all card labels to the placeholder image
	public void reset() {
		for (int i=0; i<cardLabels.length; i++) {
			cardLabels[i].setIcon( nullImage );
		}
		
		nextCard = 0;
	}
}
