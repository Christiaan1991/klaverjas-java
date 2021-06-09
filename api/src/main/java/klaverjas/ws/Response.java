package klaverjas.ws;
import klaverjas.api.models.Klaverjas;

public class Response {

    String userId;
    Integer NumberOfPlayers;
    String name;
//    Integer trump;
    Klaverjas gamestate;

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setNumberOfPlayers(Integer numberOfPlayers) { NumberOfPlayers = numberOfPlayers; }
    public void setName(String name) { this.name = name; }
    public void setGamestate(Klaverjas gamestate) {
        this.gamestate = gamestate;
    }
//    public void setTrump(Integer trump) { this.trump = trump; }

}
