package klaverjas.domain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int team;
    List<Card> hand = new ArrayList<Card>();
    private int score;

    public Player(String name, int team){
        this.name = name;
        this.team = team;
        score = 0;
    }

    public int getScore() { return score; }

    public String getName() {
        return name;
    }

    public int getTeam() { return team; }

    public List<Card> getHand() {
        return hand;
    }

    public void addToHand(Card card){ hand.add(card); }

    public void playCard() { }
}