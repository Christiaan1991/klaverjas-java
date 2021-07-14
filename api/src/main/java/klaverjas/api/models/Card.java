package klaverjas.api.models;

public class Card {
	private final int rank;
	private final int suit;
	private final int value;
	private boolean trump;

	public Card(){
		this.rank = -1;
		this.suit = -1;
		this.value = 0;
		this.trump = false;
	}

	public Card(int rank, int suit, boolean trump){

		this.rank = rank;
		this.suit = suit;
		this.trump = trump;
		if(!trump){
			if(rank == 0){ value = 0;} //7
			else if(rank == 1){ value = 1;} //8
			else if(rank == 2){ value = 2;} //9
			else if(rank == 3){ value = 6;} //10
			else if(rank == 4){ value = 3;} //J
			else if(rank == 5){ value = 4;} //Q
			else if(rank == 6){ value = 5;} //K
			else { value = 7;} //A
		}

		else{
			//increase value so trump always wins
			if(rank == 0) { value = 8; } //7
			else if(rank == 1){ value = 9; } //8
			else if(rank == 2){ value = 14; } //9
			else if(rank == 3){ value = 12; } //10
			else if(rank == 4){ value = 15; } //J
			else if(rank == 5){ value = 10; } //Q
			else if(rank == 6){ value = 11; } //K
			else { value = 13; } //A
		}
	}
}