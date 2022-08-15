import java.util.Arrays;
/**
 * Subclass of Hand class, used to model a Straight hand
 * Overrides isValid, getTopCard and getType methods it inherits from Hand
 * 
 * @author Abhigyan Kashyap
 *
 */
public class Straight extends Hand{

	/**
	 * Constructor, calls the constructor of Hand superclass.
	 * 
	 * @param player Player who plays hand
	 * @param cards  List of card played
	 */	
	public Straight(CardGamePlayer player, CardList card){
		super(player, card);
	}
	
	/**
	 * Returns the top card of hand
	 * 
	 * @return Card at top
	 */
	public Card getTopCard(){
		sort();
		return this.getCard(4);
		
	}
	
	/**
	 * Checks if hand is valid
	 * 
	 * @return true or false depending if hand is valid
	 */
	public boolean isValid(){
		
		if(this.size()!=5) return false;
		this.sort();
		int[] cardsInHand= {this.getCard(0).getRank(),this.getCard(1).getRank(),this.getCard(2).getRank(),this.getCard(3).getRank(),this.getCard(4).getRank()};
		
		for(int i=0;i<4;i++) {
			if (cardsInHand[i]==0) cardsInHand[i]=13;
			else if (cardsInHand[i]==1) cardsInHand[i]=14;
		}
		
		Arrays.sort(cardsInHand);
		for(int i=0;i<3;i++) {
			if(cardsInHand[i]!=cardsInHand[i+1]-1) return false;
		}
		return true;
		
	}
	
	/**
	 * Returns type of hand
	 * 
	 * @return String hand type
	 */
	public String getType() {
		return "Straight" ;
	}
}