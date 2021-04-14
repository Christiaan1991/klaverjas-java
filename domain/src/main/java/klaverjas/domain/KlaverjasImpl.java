package klaverjas.domain;

public class KlaverjasImpl implements Klaverjas {
    private int winner = NO_TEAM;

    private Player[] players = new Player[4];
    private int hasTurn;
    private Deck deck;

    public KlaverjasImpl(){
        //create team 1 and team 2
        players[0] = new Player("player1", TEAM_ONE);
        players[1] = new Player("player2", TEAM_TWO);
        players[2] = new Player("player3", TEAM_ONE);
        players[3] = new Player("player4", TEAM_TWO);

        //create a deck of cards
        deck = new Deck();

        //start with player1 has turn, so player 4 is 'dealer'
        hasTurn = PLAYER_ONE;
    }

    public int getWinner(){ return winner; }

    public Player[] getPlayers(){ return players; }

    public boolean isPlayersTurn(int player) {
        if(player == hasTurn){
            return true;
        }
        else{
            return false;
        }
    }

    public void nextTurn(){
        int newTurn = 0;
        if(hasTurn == PLAYER_ONE){
            newTurn = PLAYER_TWO;
        } else if(hasTurn == PLAYER_TWO){
            newTurn = PLAYER_THREE;
        } else if(hasTurn == PLAYER_THREE){
            newTurn = PLAYER_FOUR;
        } else if(hasTurn == PLAYER_FOUR){
            newTurn = PLAYER_ONE;
        }
        hasTurn = newTurn;

    }

    //public void playCard(){}

    public boolean isEndOfGame(){
        return false;
    }

    public Deck getDeck(){ return deck; }

    public void dealPerThree(){
        int i = hasTurn;
        int dealtCards = 0;

        while(dealtCards != 12){
            for(int j = 0; j < 3; j++){
                players[i % 4].addToHand(deck.dealCard());
                dealtCards++;
            }
            i++;
        }
    }

    public void dealPerTwo(){
        int i = hasTurn;
        int dealtCards = 0;

        while(dealtCards != 8){
            for(int j = 0; j < 2; j++){
                players[i % 4].addToHand(deck.dealCard());
                dealtCards++;
            }
            i++;
        }
    }

    public void deal(){
        dealPerThree();
        dealPerTwo();
        dealPerThree();

    }

}

