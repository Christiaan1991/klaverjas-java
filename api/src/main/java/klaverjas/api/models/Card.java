package klaverjas.api.models;

public class Card {
	private final int rank;
	private final int suit;
	private int points;
	private Card testcard;
	private String name;


	public Card(int rank, int suit){
		this.rank = rank;
		this.suit = suit;

		String[] ranks = {"7", "8", "9", "Jack", "Queen", "King", "10", "Ace"};
		String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
		name = ranks[this.rank] + " of " + suits[this.suit];

		//determine points card is worth
		if(rank < 2){ points = 0; }
		else if(rank == 3){ points = 2 ;}
		else if(rank == 4){ points = 3 ;}
		else if(rank == 5){ points = 4 ;}
		else if(rank == 6){ points = 10 ;}
		else if(rank == 7){ points = 11 ;}

	}

	public int getPoints() {
		return points;
	}

	public int getRank() {
		return rank;
	}

	public int getSuit() {
		return suit;
	}

	public boolean isCard(){
		if(this instanceof Card){
			return true;
		}
		else{
			return false;
		}
	}

	public String getName() { return name; }
}