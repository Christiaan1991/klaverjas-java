package klaverjas.domain;

public class Card {
    private int rank;
    private int suit;
    private int value;
    private int points;
    private boolean istrump = false;

    public Card(Integer rank, Integer suit){
        this.rank = rank; //7, 8, 9, J, Q, K, 10, A
        this.suit = suit; //Clubs, Spades, Hearts or Diamonds

        if(!istrump){
            value = rank;
        }

        if(value <= 2){ points = 0;}
        else if(value == 3){ points = 2;}
        else if(value == 4){ points = 3;}
        else if(value == 5){ points = 4;}
        else if(value == 6){ points = 10;}
        else if(value == 7){ points = 11;}
    }

    public boolean isTrump(){ return istrump; }

    public void setTrump() {
        istrump = true;

        //increase value so trump always wins
        if(rank == 0) { value = 8; } //7
        else if(rank == 1){ value = 9; } //8
        else if(rank == 2){ value = 14; points = 14; } //9
        else if(rank == 3){ value = 15; points = 20; } //J
        else if(rank == 4){ value = 10; } //Q
        else if(rank == 5){ value = 11; } //K
        else if(rank == 6){ value = 12; } //10
        else if(rank == 7){ value = 13; } //A
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

    public int getValue() { return value; }

}
