package klaverjas.ws;

import klaverjas.api.models.Klaverjas;

public class Gamesetup {

    String method;
    String userId;
    Integer trump;
    Integer NumberOfPlayers;
    String name;
    Klaverjas gamestate;
    Integer rank;
    Integer suit;

    public Integer getTrump() { return trump;}

    public String getUserId() {
        return userId;
    }

    public String getMethod() {
        return method;
    }

    public Integer getRank() { return rank; }

    public Integer getSuit() { return suit; }

    public Integer getNumberOfPlayers() { return NumberOfPlayers; }

    public String getName() { return name; }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNumberOfPlayers(Integer numberOfPlayers) { NumberOfPlayers = numberOfPlayers; }

    public void setName() { this.name = name; }

    public void setGamestate(Klaverjas gamestate) {
        this.gamestate = gamestate;
    }

    public void setTrump(Integer trump) { this.trump = trump; }
}
