/**
 * Subclass of the Card class, models cards used in game.Overrides the compareTo() method
 * from Card class
 * 
 * @author Abhigyan Kashyap
 *
 */
public class BigTwoCard extends Card{

	/**
	 * Creates instance of BigTwoCard by calling the constructor(super) of Card class.
	 * 
	 * @param suit Int value between 0 and 3 representing the suit
	 * @param rank Int value between 0 and 12 representing the rank
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit,rank);
	}
	
	/**
	 * Locally makes adjustments for change in the rank order.
	 * Overrides compareTo of card class. 
	 * Places A and 2 at higher rank.
	 * 
	 * @param card Card object with which user compares
	 */
	public int compareTo(Card card) {
		
		int thisRank = this.rank;
		int cardRank = card.rank;
		if(cardRank==0) cardRank=13;
		else if(cardRank==1) cardRank=14;
		if(thisRank==0) thisRank=13;
		else if(thisRank==1) thisRank=14;
		if(cardRank>thisRank) return -1;
		if(cardRank<thisRank) return 1;
		if(card.suit> this.suit) return -1;
		if(card.suit< this.suit) return 1;
		return 0;
	}
}
