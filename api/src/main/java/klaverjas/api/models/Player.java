package klaverjas.api.models;

public class Player {


	public Player(klaverjas.domain.Klaverjas klaverjas,
				  String name, boolean isFirstTeam, int player) {
		this.name = name;
		team = isFirstTeam ? "team1" : "team2";
		if(player == klaverjas.PLAYER_ONE){ type = "player1"; }
		if(player == klaverjas.PLAYER_TWO){ type = "player2"; }
		if(player == klaverjas.PLAYER_THREE){ type = "player3"; }
		if(player == klaverjas.PLAYER_FOUR){ type = "player4"; }

        hasTurn = klaverjas.isPlayersTurn(player);
		this.cards = new Card[8];
		int noOfCards = klaverjas.getPlayers()[0].getHand().size();
		for(int i = 0; i < noOfCards; ++i) {
			this.cards[i] = new Card(klaverjas.getPlayers()[player].getHand().get(i).getRank(), klaverjas.getPlayers()[player].getHand().get(i).getSuit());
		}
    }
    
    String name;
	public String getName() { return name; }
	
	String type;
	public String getType() { return type; }

	String team;
	public String getTeam() { return team; }

	boolean hasTurn;
	public boolean getHasTurn() { return hasTurn; }

	Card[] cards;
	public Card[] getCards() { return cards; }
}