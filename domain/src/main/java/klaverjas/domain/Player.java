package klaverjas.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private int team;
    public List<Card> hand = new ArrayList<Card>();
    private int score;
    private int trumpscore;
    private Card playedCard;

    public Player(int team){
        this.team = team;
        score = 0;
        trumpscore = 0;
    }

    public void addScore(int points) {
        score = score + points;
    }

    public void addTrumpScore(int trumppoints) { trumpscore = trumpscore + trumppoints;}

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score;}

    public int getTrumpScore() { return trumpscore; }

    public void resetScores() { score = 0; trumpscore = 0;}

    public int getTeam() { return team; }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(Card[] cards){
        for(int i = 0; i < cards.length; i++){
            addToHand(cards[i]);
        }
    }

    //sort cards by rank and suit, from low to high
    public void sortHand(List<Card> hand){
        for(int i = 0; i < hand.size(); i++){
            for(int j = i+1; j < hand.size(); j++){
                if(hand.get(i).getValue() < hand.get(j).getValue()){ //if rank card 1 is lower than rank card 2
                    Collections.swap(hand, i, j);
                }
                if(hand.get(i).getSuit() < hand.get(j).getSuit()){ //if suit card 1 is lower than suit card 2
                    Collections.swap(hand, i, j);
                }
            }
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