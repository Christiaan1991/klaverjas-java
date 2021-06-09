package klaverjas.ws;

public class Input {

    String method;
    String userId;
    Integer trump;
//    Integer NumberOfPlayers;
    String name;
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
//    public Integer getNumberOfPlayers() { return NumberOfPlayers; }
    public String getName() { return name; }

}
