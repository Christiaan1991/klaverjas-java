package klaverjas.api.models;

public class Card {
	private final int rank;
	private final int suit;
	private String name;

	public Card(int rank, int suit){
		this.rank = rank;
		this.suit = suit;

		String[] ranks = {"7", "8", "9", "J", "Q", "K", "10", "A"};
		char[] suits = {'\u2663', '\u2666', '\u2764', '\u2660'};
		name = ranks[this.rank] + " " + suits[this.suit];

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