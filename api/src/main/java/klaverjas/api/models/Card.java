package klaverjas.api.models;

public class Card {
	private final int rank;
	private final int suit;
	private int points;
	private String name;

	public Card(int index, int rank, int suit){
		this.rank = rank;
		this.suit = suit;
		this.index = index;

		String[] ranks = {"7", "8", "9", "J", "Q", "K", "10", "A"};
		char[] suits = {'\u2663', '\u2666', '\u2764', '\u2660'};
		name = ranks[this.rank] + " " + suits[this.suit];

		//determine points card is worth
		if(rank < 2){ points = 0; }
		else if(rank == 3){ points = 2 ;}
		else if(rank == 4){ points = 3 ;}
		else if(rank == 5){ points = 4 ;}
		else if(rank == 6){ points = 10 ;}
		else if(rank == 7){ points = 11 ;}

	}

	//We need to do something with this
	public Card(int index){
		this.index = index;
		rank = 1;
		suit = 1;
		name = "No card";

	}

	private int index;
	public int getIndex() { return index; }

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