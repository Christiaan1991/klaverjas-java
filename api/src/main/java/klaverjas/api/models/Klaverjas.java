package klaverjas.api.models;

// This package is a collection of DTO's (data transfer objects).
// A DTO is a simple datastructure which models the
// data your web API sends back to the client. The Java
// objects will be converted to JSON objects.
public class Klaverjas {
    public Klaverjas(klaverjas.domain.Klaverjas klaverjas,
                     String namePlayer1, String namePlayer2, String namePlayer3, String namePlayer4) {
        players = new Player[4];
        players[0] = new Player(klaverjas, namePlayer1, true, klaverjas.PLAYER_ONE);
        players[1] = new Player(klaverjas, namePlayer2, false, klaverjas.PLAYER_TWO);
        players[2] = new Player(klaverjas, namePlayer3, true, klaverjas.PLAYER_THREE);
        players[3] = new Player(klaverjas, namePlayer4, false, klaverjas.PLAYER_FOUR);
        gameStatus = new GameStatus(klaverjas, namePlayer1, namePlayer2, namePlayer3, namePlayer4);
        team1score = klaverjas.getTeam1Score();
        team2score = klaverjas.getTeam2Score();
        correctmove = klaverjas.getCorrectMove();
        trump = klaverjas.getPickedTrump();
    }

    boolean correctmove;
    public boolean getCorrectMove() { return correctmove; }

    Player[] players;
    public Player[] getPlayers() { return players; }

    int team1score;
    public int getTeam1score() { return team1score; }

    int team2score;
    public int getTeam2score() { return team2score; }

    GameStatus gameStatus;
    public GameStatus getGameStatus() { return gameStatus; }

    int trump;
    public int getPickedTrump() { return trump; }
}