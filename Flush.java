import java.util.Arrays;
/**
 * Subclass of Hand class, used to model a Flush hand
 * Overrides isValid, getTopCard and getType methods it inherits from Hand
 * 
 * @author Abhigyan Kashyap
 *
 */
public class Flush extends Hand{
	
	/**
	 * Constructor, calls the constructor of Hand superclass.
	 * 
	 * @param player Player who plays hand
	 * @param cards  List of card played
	 */	
	public Flush(CardGamePlayer player,CardList cards){
		super(player, cards);
	}
	
	/**
	 * Returns the top card of hand
	 * 
	 * @return Card at top
	 */
	public Card getTopCard(){
		return this.getCard(4);
		
	}
	
	/**
	 * Checks if hand is valid
	 * 
	 * @return true or false depending if hand is valid
	 */
	public boolean isValid() {
		if (this.size() != 5) return false;
		if ((this.getCard(0).getSuit() == this.getCard(1).getSuit()) && (this.getCard(1).getSuit() == this.getCard(2).getSuit()) && (this.getCard(2).getSuit() == this.getCard(3).getSuit()) && (this.getCard(3).getSuit() == this.getCard(4).getSuit())) return true;
		return false;
	}
	
	/**
	 * Returns type of hand
	 * 
	 * @return String hand type
	 */
	public String getType() {
		return "Flush";	
	}
	
}