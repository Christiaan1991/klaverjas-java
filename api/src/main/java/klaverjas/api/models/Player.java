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
		hasSlagTurn = klaverjas.isPlayersSlagTurn(player);
		hasRoundTurn = klaverjas.isPlayersRoundTurn(player);
		score = klaverjas.getPlayers()[player].getScore();

		if(klaverjas.getPlayers()[player].hasPlayedCard()) {
			playedCard = new Card(klaverjas.getPlayers()[player].getPlayedCard().getRank(), klaverjas.getPlayers()[player].getPlayedCard().getSuit());
		}

		int noOfCards = klaverjas.getPlayers()[player].getHand().size();
		cards = new Card[noOfCards];
		for(int i = 0; i < noOfCards; i++) {
			cards[i] = new Card(klaverjas.getPlayers()[player].getHand().get(i).getRank(), klaverjas.getPlayers()[player].getHand().get(i).getSuit());
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

	boolean hasSlagTurn;
	public boolean getHasSlagTurn() { return hasSlagTurn; }

	boolean hasRoundTurn;
	public boolean getHasRoundTurn() { return hasRoundTurn; }

	int score;
	public int getScore() { return score; }

	Card[] cards;
	public Card[] getCards() { return cards; }

	Card playedCard;
	public Card getPlayedCard() { return playedCard; }
}