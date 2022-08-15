import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.JOptionPane;

/**
 * Public class representing BigTwo object contains all variables and member functions, including main
 */

public class BigTwoClient implements CardGame, NetworkGame {

    /**
	 * Constructor to initialize an object of BigTwoClient and initialize variables
	 */
    public BigTwoClient(){
	
		playerList = new ArrayList<CardGamePlayer>();
        for(int i=0;i<4;i++){
            CardGamePlayer player = new CardGamePlayer();
            playerList.add(player);
        }
        handsOnTable = new ArrayList<Hand>();
        table = new BigTwoTable(this);
        numOfPlayers = this.playerList.size();
        currentIdx= 0;
        this.setPlayerID(0);
		
		String playerName = JOptionPane.showInputDialog("Enter Name: ");
		if (playerName==null){
            this.table.printMsg("Enter Name: \n");
        } 
        else{
			this.setServerIP("127.0.0.1");
            this.setServerPort(2396);
            this.setPlayerName(playerName);
			this.makeConnection();
        }
        this.table.disable();
    }
    
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx;
    private BigTwoTable table;
    

    /** 
     * Method to get the number of players (getter)
     * 
     * @return Integer stating number of players.
     */
    public int getNumOfPlayers() {
        return playerList.size();
    }
    
    /**
     * Method to retrieve the deck (getter)
     * 
     * @return Deck object with the current deck of cards
     */
    public Deck getDeck() {
            return deck;
    }
    
    /**
     * Method to retrieve the players list (getter)
     * 
     * @return An ArrayList containing the list of players
     */
    public ArrayList<CardGamePlayer> getPlayerList(){
        return playerList;      
    }
    
    /**
     * Method to show current hand on table (getter)
     * 
     * @return An ArrayList having the cards played
     */
    public ArrayList<Hand> getHandsOnTable(){
        return handsOnTable;
    }
    
    /**
     * Method getting the index of the active player (getter)
     * 
     * @return Int type
     */
    public int getCurrentIdx(){
        return this.currentIdx; 
    }
    
    /**
     * Method getting the id of the player (getter)
     * 
     * @return Int type
     */
    public int getPlayerID(){
        return this.playerID;
    }

    /**
     * Method getting the Name of the player (getter)
     * 
     * @return String type
     */
    public String getPlayerName() {
		return this.playerName;
	}

    /**
     * Method getting the server IP (getter)
     * 
     * @return String type
     */
    public String getServerIP() {
		return this.serverIP;
    }
    
    /**
     * Method getting the server Port (getter)
     * 
     * @return String type
     */
    public int getServerPort() {
		return serverPort;
	}

    /**
     * Method setting the player ID (setter)
     * 
     */
    public void setPlayerID(int playerID) {
		this.playerID = playerID;
    }
    
    /**
     * Method setting the name (setter)
     * 
     */
    public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

    /**
     * Method setting the server IP (setter)
     * 
     */
    public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
    
    /**
     * Method setting the server Port (setter)
     * 
     */
    public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
 
    /**
     * Starts game with shuffled deck of cards
     * 
     * @param Deck BigTwoDeck object: the shuffled deck of cards required
     */
    
    public void start(Deck deck) {
        //clear table, remove player cards
        handsOnTable.clear();
        for(int i=0;i<4;i++)
            playerList.get(i).getCardsInHand().removeAllCards();
        //allocating cards
        int player = 0;
        for(int i = 0;i<52;i++) {
            playerList.get(player%4).addCard(deck.getCard(i));
            player++;
        }
        //sorting cards
        for(int i=0;i<4;i++)
        playerList.get(i).getCardsInHand().sort();

        //identification of 3 of diamonds
        Card StarterCard = new Card(0,2);
        for(int i=0;i<4;i++) {
            if(playerList.get(i).getCardsInHand().contains(StarterCard)){
                currentIdx= i;
                table.setActivePlayer(currentIdx);
                break;
            }
        }
        table.printMsg("Everyone is here. "+getPlayerList().get(currentIdx).getName() + "'s turn");
        table.repaint();
    }


    /**
	 * Makes move
	 * 
	 * @param playerID playerID of the player making move
	 * @param cardIdx Indices of the cards selected
	 */
    public void makeMove(int playerID, int[] cardIdx){
        sendMessage(new CardGameMessage(CardGameMessage.MOVE,-1,cardIdx));
    }


    /**
	 * Checks move is valid or not
	 * 
	 * @param playerID playerID of the player making move
	 * @param cardIdx Indices of the cards selected
	 */
    public void checkMove(int playerID, int [] cardIdx){

        CardList cardList = new CardList();
        Card StarterCard = new Card(0,2);
        boolean isValid = true;

        if(cardIdx != null) {
                
            cardList = this.getPlayerList().get(playerID).play(cardIdx);
            Hand hand = composeHand(playerList.get(playerID), cardList);

            if(handsOnTable.isEmpty()) {
                if(hand.isEmpty()!=true && hand.contains(StarterCard) && hand.isValid()) isValid = true; 
                else isValid = false;
            }
            
            else {
                if(handsOnTable.get(handsOnTable.size()-1).getPlayer() != playerList.get(playerID))
                    if(hand.isEmpty()!=true)isValid = hand.beats(handsOnTable.get(handsOnTable.size()-1));
                    else isValid = false;
                else{
                    if(hand.isEmpty()) isValid = false;
                    else isValid = true;
                } 
                
            }
            
            if(isValid){
                for(int i=0;i<cardList.size();i++)
                    playerList.get(playerID).getCardsInHand().removeCard(cardList.getCard(i));
                table.printMsg("{" + hand.getType() + "} " + hand);
                handsOnTable.add(hand);
                currentIdx++; currentIdx%=4;
                table.setActivePlayer(currentIdx);
                table.printMsg(getPlayerList().get(currentIdx).getName() + "'s turn");
            }
            else{
                table.printMsg(cardList +" <= Not a legal Move!!!");
            }
        }
        else {
            if(handsOnTable.isEmpty()!=true && handsOnTable.get(handsOnTable.size()-1).getPlayer() != playerList.get(playerID)) {
                isValid = true;
                currentIdx++; currentIdx%=4;
                table.printMsg("{Pass}");
                table.setActivePlayer(currentIdx);
                table.printMsg(getPlayerList().get(currentIdx).getName() + "'s turn");
                
            }
            else {
                isValid= false;
                table.printMsg("Not a legal Move!!!"); 
            }
        }
        table.repaint();

        if(endOfGame()) {
            String toPrint = "";
            table.repaint();
            toPrint = "Game ends";
            table.setActivePlayer(-1);
            for(int i=0; i<playerList.size();i++){
                if(playerList.get(i).getCardsInHand().size()!=0)
                    toPrint = toPrint+getPlayerList().get(currentIdx).getName()+ " has "+playerList.get(i).getCardsInHand().size()+" cards in hand";
                else
                    toPrint =toPrint+getPlayerList().get(currentIdx).getName()+" wins the game";
            }
            table.disable();
            table.printMsg(toPrint);
		    JOptionPane.showMessageDialog(null,toPrint);
		    CardGameMessage ready=new CardGameMessage(CardGameMessage.READY,-1,null);
		    sendMessage(ready);
        }
    }

    /**
     * Method checking for end of game
     * 
     * @return boolean true if the game ends else false
     */
    public boolean endOfGame() {
        for(int i=0;i<playerList.size();i++){
            if(playerList.get(i).getNumOfCards() == 0) return true;
        }
        return false;
    }
    

    /**
     * Method returning a valid hand from all the list of cards played
     * 
     * @param player CardGamePlayer object: stores the list of players
     * @param cards CardList object: stores list of cards played
     * 
     * @return Hand Type of hand
     */
    
    public static Hand composeHand(CardGamePlayer player, CardList cards){
		
		if(cards == null)	return null;
		Hand check = null;
		int cardNum = cards.size();
		if(cardNum == 1) {
			check = new Single(player, cards);
			if( check.isValid()) return check;
		} 
		if(cardNum == 2) {
			check = new Pair(player, cards);
			if( check.isValid()) return check;
		} 
		if(cardNum == 3) {
			check = new Triple(player, cards);
			if( check.isValid()) return check;
		} 
		if (cardNum == 5){
			check = new StraightFlush(player,cards);
			if(check.isValid()) return check;
			check = new FullHouse(player,cards);
			if(check.isValid()) return check;
			check = new Quad(player,cards);
			if (check.isValid()) return check;
			check = new Flush(player,cards);
			if (check.isValid()) return check;
			check = new Straight(player,cards);
			if (check.isValid()) return check;
		}
		return check;
	}


    public boolean isConnected() {
		if(!sock.isClosed()) {
			return true;
		}
		return false;
    }
    
    /**
	 * Public function, makes the connection client and server
	 */
    public void makeConnection(){
        if(sock==null || sock.isClosed()==true){
            try{

                sock=new Socket(this.getServerIP(),this.getServerPort());
                oos=new ObjectOutputStream(sock.getOutputStream());
                Runnable j =new ServerHandler();
                Thread t =new Thread(j);t.start();
                sendMessage(new CardGameMessage(CardGameMessage.JOIN,-1,this.getPlayerName()));
				sendMessage(new CardGameMessage(CardGameMessage.READY,-1,null));
            
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    /**
	 * Public function parsing the messages client receives
     * */
    public void parseMessage(GameMessage message){
        if(message.getType()== CardGameMessage.MOVE){
            checkMove(message.getPlayerID(),(int[]) message.getData());
            table.repaint();
        }
        else if(message.getType()== CardGameMessage.PLAYER_LIST){
            for(int i=0;i<this.getNumOfPlayers();i++)
                if(((String[])message.getData())[i] != null) 
                {   this.setPlayerID(message.getPlayerID());
                    this.getPlayerList().get(i).setName(((String[]) message.getData())[i]);
                }
            table.repaint();
        }
        else if(message.getType()== CardGameMessage.START){
            deck = (Deck)message.getData();
            table.enable();
            System.out.println(table.getActivePlayer() +" + "+getPlayerID());
            this.start(deck);
            table.repaint();
        }
        else if(message.getType()== CardGameMessage.JOIN){
            this.getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
            table.printMsg("Player "+playerList.get(message.getPlayerID()).getName()+" has joined game.\n");
            this.table.repaint();
        }
        else if(message.getType()== CardGameMessage.READY){
            table.printMsg("Player "+playerList.get(message.getPlayerID()).getName()+" is ready.\n");
            this.table.repaint();
        }
        else if(message.getType()== CardGameMessage.FULL){
            table.printMsg("Sorry, this table is full.");
            try{
                this.sock.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(message.getType()== CardGameMessage.MSG){
            table.printChatMsg((String)message.getData());
        }
        else if(message.getType()== CardGameMessage.QUIT){
            table.printMsg("Player "+playerList.get(message.getPlayerID()).getName()+" has left game.\n");
            playerList.get(message.getPlayerID()).setName("");
            if(!this.endOfGame()){
                table.disable();
                this.sendMessage(new CardGameMessage(4,-1,null));
                for(int i=0;i<4;i++) playerList.get(i).removeAllCards();
            }
            table.repaint();
        }
    }

    /**
	 * Public function, sends message to the server
	 * 
	 */
    public void sendMessage(GameMessage message){
        try{
            oos.writeObject(message);
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
	 * Inner class that handles functions with the server
	 * @author Abhigyan Kashyap
	 *
	 */
    public class ServerHandler implements Runnable{
        public void run(){
            try{
                ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
                CardGameMessage message;
                while(sock.isClosed()==false){
                    message= (CardGameMessage)ois.readObject();
                    if(message!=null) parseMessage(message);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Main creates BigTwoClient object.
     * 
     * @param args Not used
     */
    public static void main(String args[]){
        BigTwoClient client =new BigTwoClient();
    }
}