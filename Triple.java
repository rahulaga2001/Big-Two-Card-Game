/**
 * Subclass of Hand class, used to model a Triple hand
 * Overrides isValid, getTopCard and getType methods it inherits from Hand
 * 
 * @author Abhigyan Kashyap
 *
 */
public class Triple extends Hand{

	/**
	 * Constructor, calls the constructor of Hand superclass.
	 * 
	 * @param player Player who plays hand
	 * @param cards  List of card played
	 */	
	public Triple(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	/**
	 * Returns the top card of hand
	 * 
	 * @return Card at top
	 */
	public Card getTopCard() {
		return this.getCard(2);
	}
	
	/**
	 * Checks if hand is valid
	 * 
	 * @return true or false depending if hand is valid
	 */
	public boolean isValid() {
		if(this.size()==3) {
			if((this.getCard(0).getRank() == this.getCard(1).getRank())&&(this.getCard(1).getRank() == this.getCard(2).getRank()))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns type of hand
	 * 
	 * @return String hand type
	 */
	public String getType() {
		return "Triple" ;
	}
}