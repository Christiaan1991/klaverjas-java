package klaverjas.api.models;

public class GameStatus {
    boolean endOfGame;
    public boolean getEndOfGame() { return endOfGame; }
    
    String winner;
    public String getWinner() { return winner; }

    public GameStatus(klaverjas.domain.Klaverjas klaverjas) {
        this.endOfGame = klaverjas.isEndOfGame();
        int winner = klaverjas.getWinner();
        if(winner == klaverjas.NO_TEAM) {
            this.winner = null;
        } else if(winner == klaverjas.TEAM_ONE) {
            this.winner = "Team 1";
        } else if(winner == klaverjas.TEAM_TWO) {
            this.winner = "Team 2";
        } else {
            this.winner = "Team 1 "  + "and" + "Team 2";
        }
    }
}