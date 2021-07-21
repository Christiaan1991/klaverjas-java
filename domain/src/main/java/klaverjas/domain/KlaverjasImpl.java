package klaverjas.domain;

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
    private int picked_trump = 100; //value which is not realistic, until set!
    private int highestvalue = 0;
    private int hasSlag;
    private int limitscore = 100;

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

    public int getPickedTrump() { return picked_trump; }

    public int getWinner(){ return winner; }

    public Player[] getPlayers(){ return players; }

    @Override
    public int getTeam1Score(){ return team1_score; }

    public void setTeam1Score(int team1_score){
        this.team1_score = team1_score;
    }

    public void setTeam2Score(int team2_score){
        this.team2_score = team2_score;
    }

    @Override
    public int getTeam2Score(){ return team2_score; }

    @Override
    public boolean getCorrectMove(){ return correctMove; }

    public boolean isPickedTrump(int trump){
        if(trump == picked_trump){ return true; }
        return false;
    }

    public boolean isPickedSuit(int suit){
        if(suit == picked_suit){ return true; }
        return false;
    }

    public void pickTrump(int trump){
        picked_trump = trump;

        //change all trump cards to trump
        for(Player player: players){
            for(Card card: player.getHand()){
                if(card.getSuit() == picked_trump){
                    card.setTrump(); //set all these cards as trump cards in all players hand!
                }
            }
        }
    }

    public void move(Integer rank, Integer suit) throws Exception {

        //first check if the pickedCard is actually in players hand!
        if(!players[hasTurn].inHand(rank, suit)){ //if not in hand, say correctmove is false, and return back to API
            System.out.println("Card is not in players hand!");
            correctMove = false;
            return;
        }

        if(isMoveAllowed(rank, suit)){ //if move is allowed, we determine the highest value of all the played cards, go to next player, and say correctmove!
            players[hasTurn].playCard(rank, suit, picked_trump);
            determineHighestValue();
            nextTurn();
            correctMove = true;

            if(isEndOfSlag()){ //if end of slag
                int winningplayerid = compareCards();       //determine the winner
                calculatePoints(players[winningplayerid]);  //add up points to the winner
                resetPlayerCards();                         //remove playedCards
                highestvalue = 0;                           //reset highest value card
                hasSlagTurn = winningplayerid;              //winner has starts with new slag
                hasTurn = hasSlagTurn;                      //person who starts slag starts the turn
            }

            if(isEndOfRound()){ //if end of round
                determineTeamScores(); //determine if team is nat/pit, we add points from players to team, and set players scores back to 0
                picked_trump = 100; //reset picked_trump
                if(isEndOfGame()){ //check if end of game
                    System.out.println("Klaverjas game ends!");
                }

                nextRoundTurn();
                getDeck().shuffleDeck(); //shuffle deck
                deal(); //deal card
            }
        }
        else { //move not allowed, so put playedcard back in the hand
            correctMove = false; //no correct move, needs to be feedbacked to the front-end
        }

    }

    public void determineHighestValue(){
        Integer newhighestvalue = players[hasTurn].getPlayedCard().getValue(); //to keep track of highest value card
        if(newhighestvalue > highestvalue){
            highestvalue = newhighestvalue;
            hasSlag = hasTurn; //slag is for this player, determines if you have to trump yes or no
        }
    }

    public boolean isEndOfSlag(){
        if(hasTurn == hasSlagTurn){ return true; } //at end of slag, person who started slag has the turn
        return false;
    }

    public boolean isEndOfRound(){
        if(players[hasTurn].hand.size() == 0){ return true; } //at end of round, person who now has the turn does not have any cards in his hand!
        return false;
    }

    public boolean hasHigherValueInHand(){
        for(Card card: players[hasTurn].getHand()){
            if(card.getValue() > highestvalue){
                return true;
            }
        }
        return false;
    }

    public void resetPlayerCards(){
        for(Player player : players){
            player.resetPlayerCard();
        }

    }

    public void determineTeamScores() {
        if(isNat()){
            //we add the score to the other team players!
            players[(hasRoundTurn+1) % 4].addScore(players[hasRoundTurn].getScore() + players[hasRoundTurn].getTrumpScore());
            players[(hasRoundTurn+3) % 4].addScore(players[(hasRoundTurn+2) % 4].getScore() + players[(hasRoundTurn+2) % 4].getTrumpScore());

            //set score of players who are nat to 0
            players[hasRoundTurn].resetScores();
            players[(hasRoundTurn+2) % 4].resetScores();
        }

        if(hasPit()){ players[hasRoundTurn].addScore(100); }

        //add scores from players to each team, and reset score and trump score of each player
        team1_score += getTeamScore(0);
        team2_score += getTeamScore(1);

        //reset players scores
        for(Player player : players){ player.resetScores(); }
    }

    public boolean hasSuitInHand(int picked_suit){

        for(Card card: players[hasTurn].getHand() ){
            if(card.getSuit() == picked_suit){ return true; }
        }
        return false;
    }

    public boolean isMoveAllowed(Integer rank, Integer suit) throws Exception {

        //create a new card which we use to check if move is allowed
        Card card = new Card(rank, suit);
        if(suit == picked_trump){ card.setTrump(); }

        if (hasTurn == hasSlagTurn) { //player's slagturn determines the suit
            setPickedSuit(suit);
            return true; //move is always allowed
        }

        else {//other player turn than the player who started the slag
            if (isPickedSuit(suit) && !isPickedTrump(suit)) { return true; }//a card with the suit corresponding to picked_suit and not a trump

            if (isPickedSuit(suit) && isPickedTrump(suit)) { //if the suit is the trump suit
                    if (card.getValue() < highestvalue && hasHigherValueInHand()) { return false; }  //if played card value is lower than the highest value on table, and high is avaiable in hand (overtroeven)
                    else { return true; }
            }

            if (!isPickedSuit(suit) && hasSuitInHand(picked_suit)) { return false; } //if you don't follow picked suit, but you have picked_suit in hand

            else { //player plays card not equal to picked suit, and does not have picked suit in hand

                if (Math.abs(hasTurn - hasSlag) == 2) { return true; } //if slag is on his mate, player can play any card

                else if (Math.abs(hasTurn - hasSlag) != 2 && !hasSuitInHand(picked_trump)) { return true;}//if player has no trump, player can play any card!

                else if (Math.abs(hasTurn - hasSlag) != 2 && hasSuitInHand(picked_trump)) { //else, if player has trump cards in hand

                    if (suit == picked_trump) { //if suit of picked card is trump
                        if (card.getValue() < highestvalue && hasHigherValueInHand()) { //if card is played lower in value, and higher value is still in hand
                            return false; //now allowed
                        } else if (card.getValue() > highestvalue) {
                            return true; //higher trump than played card, so allowed!
                        } else if (card.getValue() < highestvalue && !hasHigherValueInHand()) {
                            return true; //we are not allowed to play trump cards which are lower than the played trump, unless only trump cards are present in hand
                        }
                    }

                    else if(card.getSuit() != picked_trump && !hasHigherValueInHand()) { //if played card is not trump, but we don't have any higher card, we can play it!
                        return true;
                    }
                    else if (card.getSuit() != picked_trump && hasHigherValueInHand()) { //if we have higher trump in hand, we cannot play no trump
                        return false;
                    }

                }

            }
        }
        throw new Exception("IsMoveAllowed method should not come here");
    }

    public int compareCards(){
        int maxValue = 0;
        int playerwon = 0;
        for(int i = 0; i < 4; i++){
            int playedcardvalue = players[i].getPlayedCard().getValue();
            int playedcardsuit = players[i].getPlayedCard().getSuit();
            if(playedcardvalue > maxValue && (playedcardsuit == picked_suit || playedcardsuit == picked_trump)){
                maxValue = playedcardvalue;
                playerwon = i;
            }
        }
        return playerwon; //this player has the highest rank card
    }

    public void calculatePoints(Player player){
        for(int i = 0; i < 4; i++){
            player.addScore(players[i].getPlayedCard().getPoints());
        }
        if(hasRoem20()){//if there is roem
            player.addTrumpScore(20);
        }

        if(hasRoem50()){//if there is roem
            player.addTrumpScore(30); //add 30 more to the previous roem!
        }
        if(hasStuk()){//if there is stuk
            player.addTrumpScore(20);
        }

        if(hasFourOfKind()){
            player.addTrumpScore(100);
        }
        if(hasLastSlag()){//if player get last slag
            player.addScore(10);
        }

    }

    public Card[] sortByRank(Card[] cards){
        for(int i = 0; i < cards.length; i++){
            for(int j = i+1; j < cards.length; j++){
                if(cards[i].getRank() < cards[j].getRank()){ //if rank card 1 is lower than rank card 2
                    Card temp = cards[i]; //switch cards
                    cards[i] = cards[j];
                    cards[j] = temp;
                }

                if(cards[i].getSuit() < cards[j].getSuit()){ //if suit card 1 is lower than suit card 2
                    Card temp = cards[i]; //switch cards
                    cards[i] = cards[j];
                    cards[j] = temp;
                }
            }
        }
        return cards;
    }

    public boolean hasRoem20(){
        Card[] playedcards = {players[0].getPlayedCard(), players[1].getPlayedCard(), players[2].getPlayedCard(), players[3].getPlayedCard()};
        playedcards = sortByRank(playedcards);

        for(int j = 0; j < 2; j++){
            if(playedcards[j].getSuit() == playedcards[j+1].getSuit() && playedcards[j].getSuit() == playedcards[j+2].getSuit()){
                if(playedcards[j].getRank() == playedcards[j+1].getRank() + 1 && playedcards[j].getRank() == playedcards[j+2].getRank() + 2){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasRoem50(){
        Card[] playedcards = {players[0].getPlayedCard(), players[1].getPlayedCard(), players[2].getPlayedCard(), players[3].getPlayedCard()};
        playedcards = sortByRank(playedcards);

        if(playedcards[0].getSuit() == playedcards[1].getSuit() && playedcards[0].getSuit() == playedcards[2].getSuit() && playedcards[0].getSuit() == playedcards[3].getSuit()){
            if(playedcards[0].getRank() == playedcards[1].getRank() + 1 && playedcards[0].getRank() == playedcards[2].getRank() + 2 && playedcards[0].getRank() == playedcards[3].getRank() + 3){
                return true;
            }
        }
        return false;
    }

    public boolean hasStuk(){
        Card[] playedcards = {players[0].getPlayedCard(), players[1].getPlayedCard(), players[2].getPlayedCard(), players[3].getPlayedCard()};
        playedcards = sortByRank(playedcards);

        for(int i = 0; i<3; i++){
            if(playedcards[i].getSuit() == picked_trump && playedcards[i+1].getSuit() == picked_trump && playedcards[i].getRank() == 6 && playedcards[i+1].getRank() == 5){
                return true; //K Q of troef gives 20 points extra
            }
        }
        return false;
    }

    public boolean hasFourOfKind(){
        Card[] playedcards = {players[0].getPlayedCard(), players[1].getPlayedCard(), players[2].getPlayedCard(), players[3].getPlayedCard()};
        playedcards = sortByRank(playedcards);

        if(playedcards[0].getRank() == playedcards[1].getRank() && playedcards[0].getRank() == playedcards[2].getRank() && playedcards[0].getRank() == playedcards[3].getRank()){
            return true;
        }
        return false;
    }

    public boolean hasLastSlag(){
        //if laatste slag, player has no cards anymore in hand
        if(isEndOfRound()) { return true; }
        return false;
    }

    public boolean isNat(){
        //calculate total score
        int score = players[(hasRoundTurn) % 4].getScore() + players[(hasRoundTurn + 2) % 4].getScore();;
        int score2 = players[(hasRoundTurn + 1) % 4].getScore() + players[(hasRoundTurn + 3) % 4].getScore();
        if(score <= score2){ return true; } //team who hasRoundTurn did not get enough points!
        return false;
    }

    public boolean hasPit(){
        //calculate total score
        int score2 = players[(hasRoundTurn + 1) % 4].getScore() + players[(hasRoundTurn + 3) % 4].getScore();
        if(score2 == 0){ return true; }
        return false;
    }

    public int getTeamScore(int team){
        return players[team].getScore() + players[team].getTrumpScore() + players[(team + 2) % 4].getScore() + players[(team + 2) % 4].getTrumpScore();
    }

    @Override
    public boolean isPlayersTurn(int player) {
        if(player == hasTurn){ return true; }
            return false;
    }

    @Override
    public boolean isPlayersSlagTurn(int player) {
        if(player == hasSlagTurn){ return true; }
            return false;
    }

    @Override
    public boolean isPlayersRoundTurn(int player) {
        if(player == hasRoundTurn){ return true; }
        return false;
    }

    public void setPlayersTurn(int player) { hasTurn = player;}

    public void setPlayersRoundTurn(int player) { hasRoundTurn = player;}

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
        int newTurn;
        if(hasRoundTurn == PLAYER_ONE){
            newTurn = PLAYER_TWO;
        } else if(hasRoundTurn == PLAYER_TWO){
            newTurn = PLAYER_THREE;
        } else if(hasRoundTurn == PLAYER_THREE){
            newTurn = PLAYER_FOUR;
        } else {
            newTurn = PLAYER_ONE;
        }
        hasRoundTurn = newTurn;
        hasSlagTurn = hasRoundTurn; //reset slagturn
        hasTurn = hasSlagTurn; //reset turn
    }

    public boolean isEndOfGame(){
        if(team1_score >= limitscore){
            winner = TEAM_ONE;
            return true;
        }
        else if(team2_score >= limitscore){
            winner = TEAM_TWO;
            return true;
        }
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

