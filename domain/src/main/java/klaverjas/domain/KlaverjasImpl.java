package klaverjas.domain;

import java.util.ArrayList;
import java.util.List;

public class KlaverjasImpl implements Klaverjas {
    private int winner = NO_TEAM;

    private Player[] players = new Player[4];
    private Deck deck;

    private int hasTurn; //in turn during a slag
    private int hasRoundTurn; //to remember who started the round
    private int hasSlagTurn; //to remember who started the slag

    private boolean correctMove;
    private int team1_score;
    private int team2_score;
    private int picked_suit;
    private int picked_trump;

    public KlaverjasImpl(){
        //create team 1 and team 2
        players[0] = new Player(TEAM_ONE);
        players[1] = new Player(TEAM_TWO);
        players[2] = new Player(TEAM_ONE);
        players[3] = new Player(TEAM_TWO);

        //create a deck of cards
        deck = new Deck();

        //set team scores to 0
        team1_score = 0;
        team2_score = 0;

        //Round starts with player1
        hasRoundTurn = PLAYER_ONE;

        //slag starts with the player who starts the round
        hasSlagTurn = hasRoundTurn;

        //the one who starts the slag, start with the turn
        hasTurn = hasSlagTurn;
    }

    public void setPickedSuit(int suit){ picked_suit = suit; }

    public void setPickedTrump(int suit){ picked_trump = suit; }

    public int getPickedTrump() { return picked_trump; }

    public int getWinner(){ return winner; }

    public Player[] getPlayers(){ return players; }

    @Override
    public int getTeam1Score(){ return team1_score; }

    @Override
    public int getTeam2Score(){ return team2_score; }

    @Override
    public boolean getCorrectMove(){ return correctMove; }

    public void move(Integer rank, Integer suit) {

        //first check if the pickedCard is actually in players hand!
        if(!players[hasTurn].inHand(rank, suit)){ //if not in hand, say correctmove is false, and return back to API
            System.out.println("Card is not in players hand!");
            correctMove = false;
            return;
        }

        if(isMoveAllowed(suit)){ //if move is allowed, we play the card, go to next player, and say correctmove!
            players[hasTurn].playCard(rank, suit);
            nextTurn();
            correctMove = true;

            if(isEndOfSlag()){ //if end of slag
                int winningplayerid = compareCards();       //determine the winner
                calculatePoints(players[winningplayerid]);  //add up points to the winner
                resetPlayerCards();                         //remove playedCards
                hasSlagTurn = winningplayerid;              //winner has starts with new slag
                hasTurn = hasSlagTurn;                      //person who starts slag starts the turn
            }

            if(isEndOfRound()){ //if end of round
                setTeamScore(); //we add points from players to team, and set player score back to 0
                nextRoundTurn();
                getDeck().shuffleDeck(); //shuffle deck
                deal(); //deal cards
            }
        }
        else{
            correctMove = false; //no correct move, needs to be feedbacked to the front-end
        }

    }

    public boolean isEndOfSlag(){
        if(hasTurn == hasSlagTurn){//at end of slag, person who started slag has the turn
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isEndOfRound(){
        if(players[hasTurn].hand.size() == 0){//at end of round, person who now has the turn does not have any cards in his hand!
            return true;
        }
        else{
            return false;
        }
    }

    public void resetPlayerCards(){
        players[0].resetPlayerCard();
        players[1].resetPlayerCard();
        players[2].resetPlayerCard();
        players[3].resetPlayerCard();

    }

    public void setTeamScore() {
        team1_score = players[0].getScore() + players[2].getScore();
        team2_score = players[1].getScore() + players[3].getScore();
        players[0].resetScore();
        players[1].resetScore();
        players[2].resetScore();
        players[3].resetScore();
    }

    public boolean checkHandSuit(int picked_suit){

        for(Card card: players[hasTurn].getHand() ){
            if(card.getSuit() == picked_suit){ //if this is true, player cannot perform move
                return true;
            }
        }
        return false; //else, suit is not in his hand, so he can play any card
    }

    public boolean isMoveAllowed(Integer suit){
        if(hasTurn == hasSlagTurn){ //player's slagturn determines the suit
            setPickedSuit(suit);
            return true; //move is allowed
        }
        else {//other player turn than the player who started the slag
            if(suit == picked_suit){ //a card with the suit corresponding to picked_suit, move is correct
                return true;
            }
            else if(!checkHandSuit(picked_suit)){ //suit is not in players hand, so player can play any card
                return true;
            }
            else{
                return false; //move not allowed!
            }
        }

    }

    public int compareCards(){
        int maxRank = 0;
        int playerwon = 0;
        for(int i = 0; i < 4; i++){
            int playedcardrank = players[i].getPlayedCard().getRank();
            int playedcardsuit = players[i].getPlayedCard().getSuit();
            if(playedcardrank > maxRank && playedcardsuit == picked_suit){
                maxRank = playedcardrank;
                playerwon = i;
            }
        }
        return playerwon; //this player has the highest rank card
    }

    public void calculatePoints(Player player){
        for(int i = 0; i < 4; i++){
            player.addScore(players[i].getPlayedCard().getPoints());
        }
    }

    @Override
    public boolean isPlayersTurn(int player) {
        if(player == hasTurn){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean isPlayersSlagTurn(int player) {
        if(player == hasSlagTurn){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean isPlayersRoundTurn(int player) {
        if(player == hasRoundTurn){
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

    public void nextRoundTurn(){ //create one method for both turns
        int newTurn = 0;
        if(hasRoundTurn == PLAYER_ONE){
            newTurn = PLAYER_TWO;
        } else if(hasRoundTurn == PLAYER_TWO){
            newTurn = PLAYER_THREE;
        } else if(hasRoundTurn == PLAYER_THREE){
            newTurn = PLAYER_FOUR;
        } else if(hasRoundTurn == PLAYER_FOUR){
            newTurn = PLAYER_ONE;
        }
        hasRoundTurn = newTurn;
        hasSlagTurn = hasRoundTurn; //reset slagturn
        hasTurn = hasSlagTurn; //reset turn
    }

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

