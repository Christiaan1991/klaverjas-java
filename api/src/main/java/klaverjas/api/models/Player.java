package klaverjas.api.models;

public class Player {

	public Player(klaverjas.domain.Klaverjas klaverjas,
				  String name, boolean isFirstTeam, int player, int ws_id) {
		this.name = name;
		team = isFirstTeam ? "team1" : "team2";
		if(player == klaverjas.PLAYER_ONE){ type = "player1"; }
		if(player == klaverjas.PLAYER_TWO){ type = "player2"; }
		if(player == klaverjas.PLAYER_THREE){ type = "player3"; }
		if(player == klaverjas.PLAYER_FOUR){ type = "player4"; }

        hasTurn = klaverjas.isPlayersTurn(player);
		score = klaverjas.getPlayers()[player].getScore();
		trumpscore = klaverjas.getPlayers()[player].getTrumpScore();

		if(klaverjas.getPlayers()[player].hasPlayedCard()) {
			klaverjas.domain.Card playedcard = klaverjas.getPlayers()[player].getPlayedCard();
			playedCard = new Card(playedcard.getRank(), playedcard.getSuit(), playedcard.isTrump());
		}

		int noOfCards = klaverjas.getPlayers()[player].getHand().size();
		cards = new Card[noOfCards];

		//if the player number is the same as the client, we want to show the cards!
		if(player == ws_id){
			for(int i = 0; i < noOfCards; i++) {
				klaverjas.domain.Card card = klaverjas.getPlayers()[player].getHand().get(i);
				cards[i] = new Card(card.getRank(), card.getSuit(), card.isTrump());
			}
		}
		else{
			for(int i = 0; i < noOfCards; i++) {
				//create empty card
				cards[i] = new Card();
			}
		}

    }
    
    String name;
	String type;
	String team;
	boolean hasTurn;
	int score;
	int trumpscore;
	Card[] cards;
	Card playedCard;

}