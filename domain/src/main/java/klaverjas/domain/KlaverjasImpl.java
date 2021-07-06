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
    private int hasslag;

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

    public void sortHands(){
        for(int i = 0; i < 4; i++){
            players[i].sortHand(players[i].getHand());
        }
    }

    public void setPickedSuit(int suit){ picked_suit = suit; }

    public int getPickedTrump() { return picked_trump; }

    public int getWinner(){ return winner; }

    public Player[] getPlayers(){ return players; }

    @Override
    public int getTeam1Score(){ return team1_score; }

    @Override
    public int getTeam2Score(){ return team2_score; }

    @Override
    public boolean getCorrectMove(){ return correctMove; }

    public boolean isPickedTrump(int trump){
        if(trump == picked_trump){ return true; }
        else{ return false;}
    }

    public boolean isPickedSuit(int suit){
        if(suit == picked_suit){ return true; }
        else{ return false;}
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

        //try and play the card!
        players[hasTurn].playCard(rank, suit, picked_trump);

        if(isMoveAllowed(players[hasTurn].getPlayedCard())){ //if move is allowed, we determine the highest value of all the played cards, go to next player, and say correctmove!
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
                setTeamScore(); //determine if team is nat/pit, we add points from players to team, and set players scores back to 0
                picked_trump = 100; //reset picked_trump
                nextRoundTurn();
                getDeck().shuffleDeck(); //shuffle deck
                deal(); //deal cards
            }
        }
        else { //move not allowed, so put playedcard back in the hand
            players[hasTurn].getHand().add(players[hasTurn].getPlayedCard());
            players[hasTurn].resetPlayerCard();
            correctMove = false; //no correct move, needs to be feedbacked to the front-end
        }

    }

    public void determineHighestValue(){
        Integer newhighestvalue = players[hasTurn].getPlayedCard().getValue(); //to keep track of highest value card
        if(newhighestvalue > highestvalue){
            highestvalue = newhighestvalue;
            hasslag = hasTurn; //slag is for this player, determines if you have to trump yes or no
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

    public boolean isHigherValueInHand(){
        for(Card card: players[hasTurn].getHand()){
            if(card.getValue() > highestvalue){
                return true;
            }
        }
        return false;
    }

    public void resetPlayerCards(){
        players[0].resetPlayerCard();
        players[1].resetPlayerCard();
        players[2].resetPlayerCard();
        players[3].resetPlayerCard();

    }

    public void setTeamScore() {
        if(isNat()){
            //we add the score to the other team players!
            players[(hasRoundTurn+1) % 4].addScore(players[hasRoundTurn].getScore() + players[hasRoundTurn].getTrumpScore());
            players[(hasRoundTurn+3) % 4].addScore(players[(hasRoundTurn+2) % 4].getScore() + players[(hasRoundTurn+2) % 4].getTrumpScore());

            //set score of players who are nat to 0
            players[hasRoundTurn].resetScores();
            players[(hasRoundTurn+2) % 4].resetScores();
        }

        if(hasPit()){
            //add 100 extra points to score
            players[hasRoundTurn].addScore(100);
        }

        //add scores from players to each team, and reset score and trump score of each player
        team1_score = getTeamScore(0);
        team2_score = getTeamScore(1);

        //reset players scores
        players[0].resetScores();
        players[1].resetScores();
        players[2].resetScores();
        players[3].resetScores();
    }

    public boolean isSuitInHand(int picked_suit){

        for(Card card: players[hasTurn].getHand() ){
            if(card.getSuit() == picked_suit){
                return true;
            }
        }
        return false;
    }

    public boolean isMoveAllowed(Card card) throws Exception {
        if (hasTurn == hasSlagTurn) { //player's slagturn determines the suit
            setPickedSuit(card.getSuit());
            return true; //move is always allowed
        }
        else {//other player turn than the player who started the slag
            if (isPickedSuit(card.getSuit())) { //a card with the suit corresponding to picked_suit
                if (isPickedTrump(card.getSuit())) { //if the suit is the trump suit
                    if (card.getValue() < highestvalue && isHigherValueInHand()) { //if played card value is lower than the highest value on table, and high is avaiable in hand
                        return false; //move not allowed
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
            }
            else if (isSuitInHand(picked_suit)) { //if you have picked_suit in hand, you always have to follow!
                return false;
            }
            else if (!isSuitInHand(picked_suit)) { //suit is not in players hand

                if (Math.abs(hasTurn - hasslag) == 2) { //if slag is on his mate, player can play any card
                    return true;
                }

                else if (isSuitInHand(picked_trump)) { //else, if player has trump cards in hand
                    if (card.getSuit() == picked_trump) { //if suit of picked card is trump
                        if (card.getValue() < highestvalue && isHigherValueInHand()) { //if card is played lower in value, and higher value is still in hand
                            return false; //now allowed
                        } else if (card.getValue() > highestvalue) {
                            return true; //higher trump than played card, so allowed!
                        } else if (!isHigherValueInHand()) {
                            return false; //we are not allowed to play trump cards which are lower than the played trump, unless only trump cards are present in hand
                        }
                    }

                    else if(card.getSuit() != picked_trump && !isHigherValueInHand()) { //if played card is not trump, but we don't have any higher card, we can play it!
                        return true;
                    }
                    else if (card.getSuit() != picked_trump && isHigherValueInHand()) { //if we have higher trump in hand, we cannot play no trump
                        return false;
                    }

                }

                else if (!isSuitInHand(picked_trump)) { //if player has no trump, he can play any card!
                    return true;
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

    //sort cards by rank and suit, from low to high
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
        if(isEndOfRound()) {
            return true;
        } else{
            return false;
        }
    }

    public boolean isNat(){

        //calculate total score
        int score = getTeamScore(hasRoundTurn);
        int score2 = getTeamScore(hasRoundTurn + 1);
        if(score <= score2){ //team who hasRoundTurn did not get enough points!
            return true;
        }
        else{
            return false;
        }
    }

    public boolean hasPit(){

        //calculate total score
        int score2 = getTeamScore(hasRoundTurn + 1);

        if(score2 == 0){
            return true;
        }
        else{
            return false;
        }

        //tegenpit is just 162 points, not 100 points extra
    }

    public int getTeamScore(int team){
        return players[team].getScore() + players[team].getTrumpScore() + players[(team + 2) % 4].getScore() + players[(team + 2) % 4].getTrumpScore();
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

