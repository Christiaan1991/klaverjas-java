package klaverjas.api.models;

// This package is a collection of DTO's (data transfer objects).
// A DTO is a simple datastructure which models the
// data your web API sends back to the client. The Java
// objects will be converted to JSON objects.
public class Klaverjas {
    public Klaverjas(klaverjas.domain.Klaverjas klaverjas,
                     String[] names, int ws_id) {

        //ID send by websocket
        this.ws_id = ws_id;

        players = new Player[4];
        players[0] = new Player(klaverjas, names[0], true, 0, ws_id);
        players[1] = new Player(klaverjas, names[1], false, 1, ws_id);
        players[2] = new Player(klaverjas, names[2], true, 2, ws_id);
        players[3] = new Player(klaverjas, names[3], false, 3, ws_id);

        gameStatus = new GameStatus(klaverjas);
        team1score = klaverjas.getTeam1Score();
        team2score = klaverjas.getTeam2Score();
        correct_move = klaverjas.getCorrectMove();
        picked_trump = klaverjas.getPickedTrump();
    }

    boolean correct_move;
    Player[] players;
    int ws_id;
    int team1score;
    int team2score;
    GameStatus gameStatus;
    int picked_trump;
}