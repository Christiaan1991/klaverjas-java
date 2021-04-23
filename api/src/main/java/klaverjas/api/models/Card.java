package klaverjas.api.models;

public class Card {
	private final int rank;
	private final int suit;
	private String name;
	private int picked_suit;
	private boolean trump;

	public Card(int rank, int suit, int picked_suit){
		this.rank = rank;
		this.suit = suit;

		String[] ranks = {"7", "8", "9", "10", "J", "Q", "K", "A"};
		char[] suits = {'\u2662', '\u2663', '\u2664', '\u2665'}; //diamonds, clubs, spades, hearts
		name = ranks[this.rank] + " " + suits[this.suit];
		trump = isTrump(suit);

	}

	public int getRank() {
		return rank;
	}

	public int getSuit() {
		return suit;
	}

	public String getName() { return name; }

	public boolean isTrump(int suit){
		if(picked_suit == suit){
			return true;
		}
		else{
			return false;
		}
	}
}