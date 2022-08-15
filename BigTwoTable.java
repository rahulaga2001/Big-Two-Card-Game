import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class BigTwoTable implements CardGameTable{
	
	public BigTwoTable(CardGame bigTwo) {
		this.game = (BigTwoClient) bigTwo;
		selected = new boolean[13];
		setActivePlayer(game.getCurrentIdx());
		GUISetup();
		
	}
	
	private BigTwoClient game;
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private JTextArea chatArea;
	private JTextField chatTypingArea;
	private Image[][] cardImages;
	private Image cardBackImage;
	private Image[] avatars;
	
	/**
	 * Sets the index of the active player (setter)
	 * @param activePlayer int value, denotes index of the active player
	 */
	public void setActivePlayer(int activePlayer) {
		this.activePlayer=activePlayer;
	}
	
	/**
	 * Returns active player
	 * 
	 * @return int active player
	 * 
	 */
	public int getActivePlayer() {
		return this.activePlayer;
	}

	/**
	 * Returns array of indices of selected cards
	 * 
	 * @return array of indices of selected cards
	 * 
	 */
	public int[] getSelected() {
		int i=0,count=0;
		while (i < selected.length) {
			if(selected[i] == true) count++;
			i++;
		}
		if (count==0) return null;
		int[] result = new int[count];
		int j=0;i=0;
		while (i < selected.length) {
			if(selected[i] == true) result[j++]=i;
			i++;
		}
		return result;
	}

	/**
	 * Resets the list of selected cards to an empty list
	 */
	public void resetSelected() {
		for(int i=0;i<selected.length;i++) {
			selected[i] = false;
		}
	}
	
	/**
	 * Repaints the GUI
	 */
	public void repaint() {
		resetSelected();
		frame.repaint();
		bigTwoPanel.revalidate();
	}
	
	/**
	 * A method that prints the specified string to the message area of the card game table.
	 * 
	 * @param msg the string to be printed to the message area of the card game table.
	 */
	public void printMsg(String msg) {
		msgArea.append(msg+"\n");
	}
	
	/**
	 * Clears the message area
	 */
	public void clearMsgArea() {
		msgArea.setText(null);
	}

	public void printChatMsg(String msg) {
		chatArea.append(msg+"\n");
	}
	
	/**
	 * Clears the message area
	 */
	public void clearChatMsgArea() {
		chatArea.setText("");
	}
	
	
	/**
	 * Resets the GUI
	 */
	public void reset() {
		this.resetSelected();
		this.clearMsgArea();
		this.enable();
	}
	
	/**
	 * Enables interactions
	 */
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}
	
	/**
	 * Disables interactions
	 */
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}
	
	private void GUISetup() {

		//avatars and cards
		avatars = new Image[4];
		avatars[0] = new ImageIcon("media/avatars/A.png").getImage();
		avatars[1] = new ImageIcon("media/avatars/B.png").getImage();
		avatars[2] = new ImageIcon("media/avatars/C.png").getImage();
		avatars[3] = new ImageIcon("media/avatars/D.png").getImage();
		
		char[] suit = {'d','c','h','s'};
		char[] rank = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
		
		cardBackImage = new ImageIcon("media/cards/b.gif").getImage();

		cardImages = new Image[4][13];
		for(int i=0;i<4;i++) {
			for(int j=0;j<13;j++) {
				String location = new String();
				location="media/cards/" + rank[j] + suit[i] + ".gif";
				cardImages[i][j] = new ImageIcon(location).getImage();
			}
		}
		
		//frame
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setTitle("Big Two Game");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//menu
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		JMenu menu = new JMenu("The Big Two Game, with the Big Four (Bad WW1 pun intended)");
		menuBar.add(menu);

		JMenuItem connect = new JMenuItem("Connect");
		connect.addActionListener(new ConnectMenuItemListener());
		menu.add(connect);

		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitMenuItemListener());
		menu.add(quit);

		JPanel texts = new JPanel();
		texts.setLayout(new BoxLayout(texts, BoxLayout.PAGE_AXIS));
		
	
		//texts
		msgArea = new JTextArea(40,40);
		Font font = new Font("TimesRoman",Font.BOLD,20);
		msgArea.setFont(font);
		msgArea.setEnabled(false);
		DefaultCaret caret = (DefaultCaret)msgArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scroll= new JScrollPane (msgArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		//chats
		chatArea = new JTextArea(40,40);
		chatArea.setEnabled(false);
		DefaultCaret caretChat =(DefaultCaret)chatArea.getCaret();
		caretChat.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollChat = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel chat =new JPanel();
	    chat.setLayout(new FlowLayout());
		chat.add(new JLabel("Message:"));
		chatTypingArea = new MyTextField(40);
	    chatTypingArea.getDocument().putProperty("filterNewlines",Boolean.TRUE);
	    chatTypingArea.setPreferredSize(new Dimension(200, 40));
	    chat.add(chatTypingArea);

		texts.add(scroll);
	    texts.add(scrollChat);
	    texts.add(chat);

		//buttons
		JPanel lowerPanel=new JPanel();

		playButton=new JButton("Play");
		playButton.addActionListener(new PlayButtonListener());
		lowerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		lowerPanel.add(playButton);

		passButton=new JButton("Pass");
		passButton.addActionListener(new PassButtonListener());
		lowerPanel.add(Box.createHorizontalStrut(20));
		lowerPanel.add(passButton);
		
		if(game.getPlayerID() != activePlayer) {
	    	lowerPanel.setEnabled(false);
			playButton.setEnabled(false);
			passButton.setEnabled(false);
	    }
	    else {
	    	lowerPanel.setEnabled(true);
			playButton.setEnabled(true);
			passButton.setEnabled(true);
		}
		
		//play area
		bigTwoPanel=new BigTwoPanel();
		bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel, BoxLayout.Y_AXIS));

		

		frame.getContentPane().add(BorderLayout.EAST, texts);
		frame.add(BorderLayout.CENTER, bigTwoPanel);
		frame.add(BorderLayout.SOUTH, lowerPanel);
		frame.setSize(1600,900);
		frame.setVisible(true);
	}



	/**
	 * Inner class that extends JPanel class, implements the MouseListener interface
	 * Overrides paintComponent() to draw table
	 * Implements mouseClicked()to handle mouse clicks
	 * 
	 * @author Abhigyan Kashyap
	 */
	class BigTwoPanel extends JPanel implements MouseListener{

		private static final long serialVersionUID = 1L;
		//avatar
		private int avatarXC = 20;
		private int avatarYC = 40;
		//player text
		private int nameXC = 20;
		private int nameYC = 20;
		//cards
		private int cardXC = 160;
		private int cardWide = 40;
		private int baseCardYC = 40;
		private int liftedCardYC = 20;
		private int cardIncrease = 160;
		//lines
		private int lineDimXC = 160;
		private int lineDimYC = 1600;
		

		/**
		 * BigTwoPanel default constructor
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
		}
		
		/**
		 * Draws avatars, text, cards
		 * 
		 * @param g Provided by system to allow drawing.
		 */
		public void paintComponent(Graphics g){
			//background
			super.paintComponent(g);
			this.setBackground(Color.GREEN.darker().darker().darker().darker());
			
		    //the four avatars and their cards
			for(int playerCount=0;playerCount<game.getNumOfPlayers();playerCount++) {
                //drawing the table
                if(playerCount==game.getPlayerID()){
                    if(playerCount==activePlayer) g.setColor(Color.RED);
                    else g.setColor(Color.WHITE);
                    g.drawString(game.getPlayerList().get(playerCount).getName(),nameXC,nameYC+lineDimXC*playerCount);
                }

                else if(playerCount!=activePlayer){
                    g.setColor(Color.WHITE);
                    g.drawString(game.getPlayerList().get(playerCount).getName(),nameXC,nameYC+lineDimXC*playerCount);
                }
                else {
                    g.setColor(Color.RED);
                    g.drawString(game.getPlayerList().get(playerCount).getName(),nameXC,nameYC+lineDimXC*playerCount); 
                }
                g.setColor(Color.WHITE);
                g.drawImage(avatars[playerCount],avatarXC,avatarYC+160*playerCount,this);
                g.drawLine(0,lineDimXC*(playerCount+1),lineDimYC,lineDimXC*(playerCount+1));

				//displaying cards accordingly
			    if (playerCount!=game.getPlayerID()){
					for (int i=0;i<game.getPlayerList().get(playerCount).getCardsInHand().size();i++){
			    		g.drawImage(cardBackImage,cardXC+cardWide*i, baseCardYC+cardIncrease*playerCount,this);
			    	}
			    }
			    else
			    	for (int i=0;i<game.getPlayerList().get(playerCount).getNumOfCards(); i++){
						if (selected[i]) g.drawImage(cardImages[game.getPlayerList().get(playerCount).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(playerCount).getCardsInHand().getCard(i).getRank()], cardXC+cardWide*i, liftedCardYC+cardIncrease*playerCount, this);
			    		else g.drawImage(cardImages[game.getPlayerList().get(playerCount).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(playerCount).getCardsInHand().getCard(i).getRank()], cardXC+cardWide*i, baseCardYC+cardIncrease*playerCount, this);	
			    	}
			}
			//last hand
		    g.drawString("Last Hand on Table", nameXC, 660);
		    if (game.getHandsOnTable().isEmpty()==false){
		    	int handSize = game.getHandsOnTable().size();
		    	g.drawString("Hand Type:\n " + game.getHandsOnTable().get(handSize - 1).getType(), 10, 700);
				for (int i=0;i<game.getHandsOnTable().get(handSize-1).size();i++)
					g.drawImage(cardImages[game.getHandsOnTable().get(handSize-1).getCard(i).getSuit()][game.getHandsOnTable().get(handSize-1).getCard(i).getRank()], 160 + 40*i, 700, this);
				g.drawString("Played by "+game.getHandsOnTable().get(handSize-1).getPlayer().getName(), 10, 725);
		    }
		    repaint();
		}

		/** 
		 * Method used to control all mouse click events
		 * Overrides MouseClicked method of JPanel
		 * 
		 * @param e MouseEvent object which has been used to get the coordinates of the mouseClick
		 */
		public void mouseClicked(MouseEvent e) {
				int cardCount = game.getPlayerList().get(activePlayer).getNumOfCards();
				int toCheck = cardCount-1;
				boolean flag = false; 
				
				if ((cardXC+toCheck*40)<=e.getX() && e.getX()<=(73+cardXC+toCheck*40)) {
					if(selected[toCheck]==false && (baseCardYC+cardIncrease*activePlayer)<=e.getY() && e.getY()<=(97+baseCardYC+cardIncrease*activePlayer)){
						flag = true;
						selected[toCheck] = true;
					} 
					else if(selected[toCheck] && (liftedCardYC+cardIncrease*activePlayer)<=e.getY() && e.getY()<=(97+liftedCardYC+cardIncrease*activePlayer)){
						flag = true;
						selected[toCheck] = false;
					}
				}

				for (toCheck =cardCount-2; toCheck>=0&&!flag;toCheck--) {
					if ((cardXC+toCheck*cardWide)<=e.getX() && e.getX()<=(cardXC+(toCheck+1)*cardWide)) {
						if(selected[toCheck]==false && (baseCardYC+cardIncrease*activePlayer)<=e.getY() && e.getY()<=(97+baseCardYC+cardIncrease*activePlayer)){
							flag = true;
							selected[toCheck] = true;
						} 
						else if(selected[toCheck] && (liftedCardYC+cardIncrease*activePlayer)<=e.getY() && e.getY()<=(97+liftedCardYC+cardIncrease*activePlayer)){
							flag = true;
							selected[toCheck] = false;
						}
					}
					else if ((cardXC+(toCheck+1)*cardWide)<=e.getX() && e.getX()<=(cardXC+toCheck*cardWide+73) && (baseCardYC+cardIncrease*activePlayer)<=e.getY() && e.getY()<=(97+baseCardYC+cardIncrease*activePlayer)) {
						if (selected[toCheck]==false && selected[toCheck+1]) {
							flag = true;
							selected[toCheck] = true;
						}
					}
					else if ((cardXC+(toCheck+1)*cardWide)<=e.getX() && e.getX()<=(cardXC+toCheck*cardWide+73) && (liftedCardYC+cardIncrease*activePlayer)<=e.getY() && e.getY()<=(97+liftedCardYC+cardIncrease*activePlayer)) {
						if (selected[toCheck]==false && selected[toCheck+1]) {
							flag = true;
							selected[toCheck] = true;
						}
					}
				}
				this.repaint();
		}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

	}

	

	/**
	 * Inner Class that implements the ActionListener interface
	 * Implements the actionPerformed() method from the ActionListener interface for "play" button 
	 * Calls the makeMove() method of CardGame object to make a move on click
	 * 
	 * @author Abhigyan Kashyap
	 */
	class PlayButtonListener implements ActionListener{
		/**
		 * Overriddes from the ActionListener Interface 
		 * Performs requisite function on click
		 * 
		 * @param e ActionEvent object to detect user interaction
		 */
		public void actionPerformed(ActionEvent e) {
			if (getSelected() == null)	printMsg("Choose cards!\n");
			else	game.makeMove(activePlayer, getSelected());
			repaint();
		}
	}
	/**
	 * Inner Class that implements the ActionListener interface
	 * Implements the actionPerformed() method from the ActionListener interface for "pass" button 
	 * Calls the makeMove() method of CardGame object to make a move on click
	 * 
	 * @author Abhigyan Kashyap
	 */
	class PassButtonListener implements ActionListener{
		/**
		 * Overriddes from the ActionListener Interface 
		 * Performs requisite function on click
		 * 
		 * @param e ActionEvent object to detect user interaction
		 */
		public void actionPerformed(ActionEvent e) {
			game.makeMove(activePlayer, null);
			repaint();
		}	
	}

	/**
	 * Inner Class that implements the ActionListener interface
	 * Implements the actionPerformed() method from the ActionListener interface for "quit" button
	 * 
	 * @author Abhigyan Kashyap
	 */
	class QuitMenuItemListener implements ActionListener{
		/**
		 * Overriddes from the ActionListener Interface 
		 * Performs requisite function on click
		 * 
		 * @param e ActionEvent object to detect user interaction
		 */
		public void actionPerformed(ActionEvent e) {
			System.exit(0);	
		}
	}

	class ConnectMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(game.isConnected()==false) {
				reset();
				game.makeConnection();
			}
		}
	}

	class MyTextField extends JTextField implements ActionListener{

		public MyTextField(int i) {
			super(i);
			addActionListener(this);
		}
		
		/**
		 * The function overrides the ActionPerformed function in ActionListener interface to detect 
		 * the user interaction on the object and carry out necessary functions.
		 *  
		 * @param e This is a ActionEvent object to detect if some user interaction was performed on the given object.
		 */
		public void actionPerformed(ActionEvent e) {
			String chatMsg = getText();
			CardGameMessage message = new CardGameMessage(CardGameMessage.MSG,-1,chatMsg);
			game.sendMessage(message);
			this.setText("");
		}
	}
		
}