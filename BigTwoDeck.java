/**
 * Subclass of the Deck class, models deck used in game.Overrides the initialize() method
 * from Deck class
 * 
 * @author Abhigyan Kashyap
 *
 */
public class BigTwoDeck extends Deck{
	
	/**
	 * Initializes the deck of cards
	 */
	public void initialize() {
		removeAllCards();
		for(int i=0;i<4;i++)
			for(int j=0;j<13;j++) {
				BigTwoCard card = new BigTwoCard(i,j);
				addCard(card);
			}
	}
	
}
