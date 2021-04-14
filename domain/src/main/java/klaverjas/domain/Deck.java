package klaverjas.domain;

public class Deck {
    public static final int NCARDS = 32;
    public static final int NSHUFFLE = 100;
    private int currentCard;

    private Card[] cards;

    public Deck(){
        cards = new Card[NCARDS];
        currentCard = 0; //used as a counter to keep track which card to deal

        int i = 0;
        for(int rank = 0; rank < 8; rank++){
            for(int suit = 0; suit < 4; suit++){
                cards[i++] = new Card(rank, suit);
            }
        }
    }

    public int getCurrentCard() { return currentCard; }

    public Card[] getCards() {
        return cards;
    }

    public Card dealCard(){ return cards[currentCard++]; }

    public void shuffleDeck(){
        for(int n = 0; n < NSHUFFLE; n++){
            int old_pos = (int)(NCARDS * Math.random());
            int new_pos = (int)(NCARDS * Math.random());

            Card tmp = cards[old_pos]; //pick random card
            cards[old_pos] = cards[new_pos];
            cards[new_pos] = tmp;
        }
        //after shuffle, reset currentCard to deal
        currentCard = 0;

    }

}