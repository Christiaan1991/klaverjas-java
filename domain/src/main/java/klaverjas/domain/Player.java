package klaverjas.domain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
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

    public void playCard(int i) {
        playedCard = hand.get(i);
        hand.remove(i);
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