import java.util.Arrays;
/**
 * Subclass of Hand class, used to model a FullHouse hand
 * Overrides isValid, getTopCard and getType methods it inherits from Hand
 * 
 * @author Abhigyan Kashyap
 *
 */
public class FullHouse extends Hand{

	/**
	 * Constructor, calls the constructor of Hand superclass.
	 * 
	 * @param player Player who plays hand
	 * @param cards  List of card played
	 */	
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	/**
	 * Returns the top card of hand
	 * 
	 * @return Card at top
	 */
	public Card getTopCard(){
		int[] cardsInHand= new int[5];
		for (int i=0;i<5;i++) {
			if(this.getCard(i).getRank()==0) cardsInHand[i] = 13;
			else if(this.getCard(i).getRank()==1) cardsInHand[i] = 14;
			else cardsInHand[i] = this.getCard(i).getRank();
		}
		
		Arrays.sort(cardsInHand);
		if(cardsInHand[4]>=13) cardsInHand[4]-=13;
		int reqCard = 0;
		for(int i=1;i<5;i++){
			if(this.getCard(i).getRank() == cardsInHand[4]) reqCard = i;
		}
		return this.getCard(reqCard);
	}
	
	/**
	 * Checks if hand is valid
	 * 
	 * @return true or false depending if hand is valid
	 */
	public boolean isValid() {
		if(this.size()!=5) return false;
		this.sort();
		if ((this.getCard(0).getRank() == this.getCard(1).getRank()) && (this.getCard(1).getRank() == this.getCard(2).getRank()) && (this.getCard(3).getRank() == this.getCard(4).getRank())) return true;
		if ((this.getCard(0).getRank() == this.getCard(1).getRank()) && (this.getCard(2).getRank() == this.getCard(3).getRank()) && (this.getCard(3).getRank() == this.getCard(4).getRank())) return true;
		return false;
	}
	
	/**
	 * Returns type of hand
	 * 
	 * @return String hand type
	 */
	public String getType(){
		return "FullHouse" ;
	}
}
