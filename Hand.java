/**
 * Subclass of the CardList class, models hand of cards used in game.
 * 
 * @author Abhigyan Kashyap
 *
 */
public abstract class Hand extends CardList{
	
	/**
	 * Player who plays hand
	 */
	private CardGamePlayer player;
	
	/**
	 * Constructor that gives value to player and cards
	 * 
	 * @param player Player who played hand
	 * @param cards List of cards played
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for(int i=0;i<cards.size();i++)
			this.addCard(cards.getCard(i));
	}
	
	/**
	 * Gets player who played hand (getter)
	 * 
	 * @return player of the current hand object
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	
	/**
	 * Returns the top card of the hand
	 * 
	 * @return returns the top card
	 */
	public Card getTopCard() {
		return null;
	}
	
	/**
	 * Compares hands and checks if this hand beats the hand send through the argument
	 * 
	 * @param Hand Hand that needs to be compared
	 * 
	 * @return true if this hand beats hand sent as an argument, else false
	 */
	public boolean beats(Hand hand) {
		String[] Ranks= {"Straight", "Flush","FullHouse","Quad", "StraightFlush"};
		
		if(hand.size()==1 && this.size()==1 && hand.isValid() && this.isValid()) {
			if(this.getTopCard().compareTo(hand.getTopCard()) == 1) return true;
		}
		
		else if(hand.size()==2 && this.size()==2 && hand.isValid() && this.isValid()) {
			if(this.getTopCard().compareTo(hand.getTopCard()) == 1) return true;
		}
		
		else if(hand.size()==3 && this.size()==3 && hand.isValid() && this.isValid()) {
			if(this.getTopCard().compareTo(hand.getTopCard()) == 1) return true;
		}
		
		else if(hand.size()==5 && this.size()==5 && hand.isValid() && this.isValid()) {
			
			if(this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) == 1) return true;
			
			if(this.getType()== Ranks[4]) return true;
			
			else if(this.getType()== Ranks[3] && hand.getType()!=Ranks[4]) return true;
			
			else if(this.getType()== Ranks[2] && hand.getType()!=Ranks[4] && hand.getType()!=Ranks[3]) return true;
			
			else if(this.getType()== Ranks[1] && hand.getType()!=Ranks[4] && hand.getType()!=Ranks[3] && hand.getType()!=Ranks[2]) return true;
			
		}
		
		return false;
	}
	
	//Abstract
	/**
	 * Checks if the concerned hand is valid or not
	 * 
	 * @return true if required hand, else false
	 */
	public abstract boolean isValid();

	/**
	 * Checks type of hand
	 * 
	 * @return String stating hand type
	 */
	public abstract String getType();
	
}
