package klaverjas.domain;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int team;
    List<Card> hand = new ArrayList<Card>();
    private int score;
    private Card playedCard;

    public Player(int team){
        this.team = team;
        score = 0;
    }

    public void addScore(int points) {
        score = score + points;
    }

    public int getScore() { return score; }

    public void resetScore() { score = 0;}

    public int getTeam() { return team; }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(Card[] cards){
        for(int i = 0; i < cards.length; i++){
            addToHand(cards[i]);
        }
    }

    public void addToHand(Card card){ hand.add(card); }

    public boolean inHand(Integer rank, Integer suit){
        for(Card card: hand){
            if(card.getRank() == rank && card.getSuit() == suit){ //pickedCard is in hand!
                return true;
            }
        }
        return false;
    }

    public void playCard(Integer rank, Integer suit, Integer pickedtrump) {
        playedCard = new Card(rank, suit); //not yet a trump card
        if(playedCard.getSuit() == pickedtrump){
            playedCard.setTrump(); //we set the played card as a trump card
        }
        hand.removeIf(i -> i.getRank() == rank && i.getSuit() == suit);
    }

    public boolean hasPlayedCard(){
        if(playedCard!=null){
            return true;
        }
        else{
            return false;
        }
    }

    public Card getPlayedCard() { return playedCard; }

    public void resetPlayerCard() { playedCard = null; }
}