package klaverjas.domain;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Klaverjas implementation")
class klaverjastest {

    @Nested
    @DisplayName("Initialization of klaverjassen")
    class klaverjas_intialize {

        /* Create the game klaverjas */
        KlaverjasImpl klaverjas = new KlaverjasImpl();

        @Test
        @DisplayName("Four players generated")
        void Team_setup() {
            assertEquals(4, klaverjas.getPlayers().length);
        }

        @Test
        @DisplayName("Players are divided in two teams, have names and have a score")
        void Player_setup() {
            assertEquals(6, klaverjas.getPlayers()[0].getTeam());
            assertEquals(7, klaverjas.getPlayers()[1].getTeam());
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(0, klaverjas.getPlayers()[1].getScore());
        }
    }

    @Nested
    @DisplayName("Initialization of a round of klaverjassen")
    class klaverjas_intialize_round {

        KlaverjasImpl klaverjas = new KlaverjasImpl();

        @Test
        @DisplayName("klaverjas game creates a deck of 32 cards")
        void Deck_setup() {

            assertEquals(32, klaverjas.getDeck().getCards().length);
            assertEquals(0, klaverjas.getDeck().getCurrentCard());
        }

        @Test
        @DisplayName("First card of an unshuffled deck is the 7 of Clubs")
        void Deck_deal_unshuffled() {
            Card card = klaverjas.getDeck().dealCard();

            assertEquals(0, card.getRank());
            assertEquals(0, card.getSuit());
            assertEquals(0, card.getPoints());
            assertEquals(1, klaverjas.getDeck().getCurrentCard());
        }

        @Test
        @DisplayName("klaverjas game can shuffle deck, and resets current card")
        void Deck_shuffle() {

            klaverjas.getDeck().shuffleDeck();

            assertEquals(32, klaverjas.getDeck().getCards().length);
            assertEquals(0, klaverjas.getDeck().getCurrentCard());
        }

        @Test
        @DisplayName("klaverjas can deal cards to the players")
        void Deck_deal() {

            klaverjas.deal();

            assertEquals(8, klaverjas.getPlayers()[0].getHand().size());
            assertEquals(8, klaverjas.getPlayers()[1].getHand().size());
            assertEquals(8, klaverjas.getPlayers()[2].getHand().size());
            assertEquals(8, klaverjas.getPlayers()[3].getHand().size());

        }
    }

    @Nested
    @DisplayName("a slag of klaverjassen")
    class klaverjas_slag_of_klaverjassen {

        KlaverjasImpl klaverjas = new KlaverjasImpl();


        @Test
        @DisplayName("First player who has turn plays 0th card")
        void first_player_move() throws Exception {
            klaverjas.getDeck().shuffleDeck();
            klaverjas.deal();

            //player 1 has not played a card yet
            assertNull(klaverjas.getPlayers()[0].getPlayedCard());

            //player plays an allowed move
            Card card = klaverjas.getPlayers()[0].getHand().get(0);

            klaverjas.move(card.getRank(), card.getSuit());

            //player 1 has played a card
            assertTrue(klaverjas.getPlayers()[0].hasPlayedCard());

            //the 0th card played by player1 is the not same as the 0th card in the slag list, since objects cannot be transferred
            assertNotEquals(card, klaverjas.getPlayers()[0].getPlayedCard());

            //player 1 only has 7 cards now in his hand
            assertEquals(7, klaverjas.getPlayers()[0].getHand().size());

            //player2 now has the turn
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));
        }

        @Test
        @DisplayName("When first player plays card which he does not hold, he cannot play it")
        void check_move() throws Exception {
            //setup hands of players
            Card[] cards1 = {new Card(4, 0), new Card(6, 1)};

            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.move(4, 1);

            assertFalse(klaverjas.getPlayers()[0].hasPlayedCard());
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_ONE));
        }

        @Test
        @DisplayName("When other player plays card which he does not hold, he cannot play it")
        void check_move_otherplayers() throws Exception {
            //setup hands of players
            Card[] cards1 = {new Card(4, 0), new Card(6, 1)};
            Card[] cards2 = {new Card(5, 0), new Card(2, 1)};
            Card[] cards3 = {new Card(6, 1), new Card(3, 1)};
            Card[] cards4 = {new Card(7, 0), new Card(5, 1)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(4, 0); //allowed
            assertEquals(3, klaverjas.getPlayers()[0].getPlayedCard().getValue());
            assertEquals(0, klaverjas.getPlayers()[0].getPlayedCard().getSuit());
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));

            klaverjas.move(2, 1); //not allowed
            assertFalse(klaverjas.getPlayers()[1].hasPlayedCard());
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));

            klaverjas.move(5, 0); //allowed
            assertEquals(4, klaverjas.getPlayers()[1].getPlayedCard().getValue());
            assertEquals(0, klaverjas.getPlayers()[1].getPlayedCard().getSuit());
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_THREE));

            klaverjas.move(2, 0); //allowed, but not in hand
            assertFalse(klaverjas.getPlayers()[2].hasPlayedCard());
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_THREE));

            klaverjas.move(6, 1); //allowed, since no suit is hand
            assertTrue(klaverjas.getPlayers()[2].hasPlayedCard());
            assertEquals(5, klaverjas.getPlayers()[2].getPlayedCard().getValue());
            assertEquals(1, klaverjas.getPlayers()[2].getPlayedCard().getSuit());
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_FOUR));
        }
    }


    @Nested
    @DisplayName("End of a slag of klaverjassen")
    class end_of_a_slag_of_klaverjassen {

        KlaverjasImpl klaverjas = new KlaverjasImpl();

        @Test
        @DisplayName("We play a full slag of klaverjassen")
        void Full_slag() throws Exception {

            //setup hands of players
            Card[] cards1 = {new Card(3, 0), new Card(6, 1)};
            Card[] cards2 = {new Card(4, 0), new Card(2, 1)};
            Card[] cards3 = {new Card(6, 0), new Card(3, 1)};
            Card[] cards4 = {new Card(7, 0), new Card(5, 1)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(3, 0); //10
            assertTrue(klaverjas.getPlayers()[0].hasPlayedCard());

            klaverjas.move(4, 0); //J
            assertTrue(klaverjas.getPlayers()[1].hasPlayedCard());

            klaverjas.move(6, 0); //K
            assertTrue(klaverjas.getPlayers()[2].hasPlayedCard());

            klaverjas.move(7, 0); //A

            //played cards are deleted, so no more playedCards
            assertFalse(klaverjas.getPlayers()[0].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[1].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[2].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[3].hasPlayedCard());

            //player 4 wins slag, getting 28 points for team 2
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(0, klaverjas.getPlayers()[1].getScore());
            assertEquals(0, klaverjas.getPlayers()[2].getScore());
            assertEquals(11 + 10 + 4 + 2, klaverjas.getPlayers()[3].getScore());

            //player 4 now has back the turn
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_FOUR));
        }

        @Test
        @DisplayName("Playing card of other suit is not allowed when you can follow")
        void Slag_not_allowed() throws Exception {

            //setup hands of players
            Card[] cards1 = {new Card(5, 0), new Card(3, 2)};
            Card[] cards2 = {new Card(4, 0), new Card(2, 2)};
            Card[] cards3 = {new Card(2, 0), new Card(1, 2)};
            Card[] cards4 = {new Card(7, 1), new Card(1, 0)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(5, 0); //to check that this works as well!
            klaverjas.move(4, 0);
            klaverjas.move(2, 0);
            klaverjas.move(7, 1);

            //All players play an allowed move, except player 4
            assertTrue(klaverjas.getPlayers()[0].hasPlayedCard());
            assertTrue(klaverjas.getPlayers()[1].hasPlayedCard());
            assertTrue(klaverjas.getPlayers()[2].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[3].hasPlayedCard());

            //move is not correct, return false
            assertFalse(klaverjas.getCorrectMove());
        }

        @Test
        @DisplayName("Playing card of other suit is allowed if you can't play other cards")
        void Slag_allowed_othersuite() throws Exception {

            //setup hands of players
            Card[] cards1 = {new Card(5, 0), new Card(3, 2)};
            Card[] cards2 = {new Card(4, 0), new Card(2, 2)};
            Card[] cards3 = {new Card(2, 0), new Card(1, 2)};
            Card[] cards4 = {new Card(7, 1), new Card(1, 3)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(5, 0); //Q
            klaverjas.move(4, 0); //J
            klaverjas.move(2, 0); //9
            klaverjas.move(7, 1); //A

            //All players play an allowed move, removing played cards
            assertFalse(klaverjas.getPlayers()[0].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[1].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[2].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[3].hasPlayedCard());

            //player 1 wins slag, getting 16 points
            assertEquals(11 + 3 + 2, klaverjas.getPlayers()[0].getScore());

            //player 1 now has back the turn
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_ONE));
        }

    }

    @Nested
    @DisplayName("End of a round of klaverjassen")
    class end_of_a_round_of_klaverjassen {

        KlaverjasImpl klaverjas = new KlaverjasImpl();

        @Test
        @DisplayName("When round ends, teams get score")
        void teamsGetScore() throws Exception {

            //setup hands of players
            Card[] cards1 = {new Card(0, 0)};
            Card[] cards2 = {new Card(2, 0)};
            Card[] cards3 = {new Card(4, 0)};
            Card[] cards4 = {new Card(7, 0)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(0, 0);
            klaverjas.move(2, 0);
            klaverjas.move(4, 0);
            klaverjas.move(7, 0);

            //player 4 wins slag and ends rounds, removing score from players and added to team
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(0, klaverjas.getPlayers()[1].getScore());
            assertEquals(0, klaverjas.getPlayers()[2].getScore());
            assertEquals(0, klaverjas.getPlayers()[3].getScore());
            assertEquals(0, klaverjas.getTeam1Score());
            assertEquals(11 + 2 + 10, klaverjas.getTeam2Score());
        }

        @Test
        @DisplayName("When round ends, deck is shuffled and dealt")
        void playersGetNewCards() throws Exception {
            //setup hands of players
            Card[] cards1 = {new Card(4, 0)};
            Card[] cards2 = {new Card(5, 0)};
            Card[] cards3 = {new Card(6, 0)};
            Card[] cards4 = {new Card(7, 0)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(4, 0);
            klaverjas.move(5, 0);
            klaverjas.move(6, 0);
            klaverjas.move(7, 0);

            //every player has now 8 cards again in hand, dealt from shuffled deck
            assertEquals(8, klaverjas.getPlayers()[0].getHand().size());
            assertEquals(8, klaverjas.getPlayers()[1].getHand().size());
            assertEquals(8, klaverjas.getPlayers()[2].getHand().size());
            assertEquals(8, klaverjas.getPlayers()[3].getHand().size());
        }

        @Test
        @DisplayName("When round ends, turn is given to player right of player who has hasRoundTurn")
        void correctRoundTurn() throws Exception {
            //setup hands of players
            Card[] cards1 = {new Card(4, 0)};
            Card[] cards2 = {new Card(5, 0)};
            Card[] cards3 = {new Card(6, 0)};
            Card[] cards4 = {new Card(7, 0)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(4, 0);
            klaverjas.move(5, 0);
            klaverjas.move(6, 0);
            klaverjas.move(7, 0);

            //player 2 now starts new round, since player 1 started before
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));
        }

    }

    @Nested
    @DisplayName("Setting up Trump")
    class Setting_up_trump_klaverjassen {

        KlaverjasImpl klaverjas = new KlaverjasImpl();

        @Test
        @DisplayName("Picking trump changes rank of trump cards")
        void changerank() {

            //setup hands of players
            Card[] cards1 = {new Card(3, 1), new Card(4, 1)}; //J
            Card[] cards2 = {new Card(2, 1), new Card(4, 2)}; //9
            Card[] cards3 = {new Card(4, 1), new Card(4, 3)}; //10
            Card[] cards4 = {new Card(7, 1), new Card(4, 0)}; //A

            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.pickTrump(1); //1 is now trump

            //check that the cards have changed in value
            assertEquals(12, klaverjas.getPlayers()[0].getHand().get(0).getValue());
            assertEquals(14, klaverjas.getPlayers()[1].getHand().get(0).getValue());
            assertEquals(15, klaverjas.getPlayers()[2].getHand().get(0).getValue());
            assertEquals(13, klaverjas.getPlayers()[3].getHand().get(0).getValue());

        }

        @Test
        @DisplayName("Playing a round of trump gives different amount of points")
        void teamsGetScore() throws Exception {

            //setup hands of players
            Card[] cards1 = {new Card(3, 1), new Card(4, 1)}; //J
            Card[] cards2 = {new Card(2, 1), new Card(4, 2)}; //9
            Card[] cards3 = {new Card(6, 1), new Card(4, 3)}; //10
            Card[] cards4 = {new Card(7, 1), new Card(4, 0)}; //A

            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            //1 is now trump
            klaverjas.pickTrump(1);

            //play move
            klaverjas.move(3, 1); //10, 10 points
            klaverjas.move(2, 1); //9, 14 poins
            klaverjas.move(6, 1); //K, 4 points
            klaverjas.move(7, 1); //A, 11 points

            //player 1 wins slag, getting different amount of points
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(10 + 14 + 4 + 11, klaverjas.getPlayers()[1].getScore());
            assertEquals(0, klaverjas.getPlayers()[2].getScore());
            assertEquals(0, klaverjas.getPlayers()[3].getScore());

        }

        @Test
        @DisplayName("Players have to overtrump when trump is asked")
        void overtrump() throws Exception {

            //setup hands of players
            Card[] cards1 = {new Card(5, 1), new Card(4, 1)}; //J
            Card[] cards2 = {new Card(2, 1), new Card(0, 1)}; //9
            Card[] cards3 = {new Card(3, 1), new Card(4, 3)}; //10
            Card[] cards4 = {new Card(7, 1), new Card(4, 0)}; //A

            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            //1 is now trump
            klaverjas.pickTrump(1);

            //play move
            klaverjas.move(5, 1); //Q, 3 points
            klaverjas.move(0, 1); //not allowed
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));
            klaverjas.move(2, 1); //9, 14 points
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_THREE));

            klaverjas.move(3, 1); //10, 10 points
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_FOUR));
            klaverjas.move(7, 1); //A, 11 points

            //player 2 wins slag, getting different amount of points
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(38, klaverjas.getPlayers()[1].getScore());
            assertEquals(0, klaverjas.getPlayers()[2].getScore());
            assertEquals(0, klaverjas.getPlayers()[3].getScore());

        }

        @Test
        @DisplayName("player has to play trump if he cannot follow and slag is on opponent")
        void opponent_has_to_play_trump() throws Exception {
            //setup hands of players
            Card[] cards1 = {new Card(4, 1), new Card(4, 0)};
            Card[] cards2 = {new Card(5, 2), new Card(0, 0)};
            Card[] cards3 = {new Card(6, 1), new Card(4, 0)};
            Card[] cards4 = {new Card(7, 1), new Card(4, 0)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.pickTrump(0);

            klaverjas.move(4, 1); //allowed

            klaverjas.move(5, 2); //not allowed, still player2 turn
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));

            klaverjas.move(0, 0); //allowed
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_THREE));
            klaverjas.move(6, 1); //allowed //K
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_FOUR));
            klaverjas.move(7, 1); //allowed
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));

            //player 2 wins slag, getting different amount of points
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(17, klaverjas.getPlayers()[1].getScore());
            assertEquals(0, klaverjas.getPlayers()[2].getScore());
            assertEquals(0, klaverjas.getPlayers()[3].getScore());
        }

        @Test
        @DisplayName("player does not have to play trump if slag is on mate")
        void mate_does_not_have_to_trump() throws Exception {
            //setup hands of players
            Card[] cards1 = {new Card(4, 1), new Card(4, 0)};
            Card[] cards2 = {new Card(2, 1), new Card(0, 0)};
            Card[] cards3 = {new Card(6, 2), new Card(4, 0)};
            Card[] cards4 = {new Card(1, 1), new Card(4, 0)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.pickTrump(0);

            klaverjas.move(4, 1); //allowed J, 2 points
            klaverjas.move(2, 1); //allowed 9, 0 points
            klaverjas.move(6, 2); //allowed, since we dont have to trump because slag is on the mate, K, 4 points
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_FOUR));

            klaverjas.move(1, 1); //allowed

            //player 1 wins slag
            assertEquals(2 + 4, klaverjas.getPlayers()[0].getScore());
            assertEquals(0, klaverjas.getPlayers()[1].getScore());
            assertEquals(0, klaverjas.getPlayers()[2].getScore());
            assertEquals(0, klaverjas.getPlayers()[3].getScore());
        }

        @Test
        @DisplayName("if opponent plays trump, you have to over-trump")
        void if_hand_to_opponent_overtrump() throws Exception {
            //setup hands of players
            Card[] cards1 = {new Card(4, 1), new Card(4, 2)};
            Card[] cards2 = {new Card(3, 2), new Card(5, 0)};
            Card[] cards3 = {new Card(1, 0), new Card(7, 0)};
            Card[] cards4 = {new Card(2, 2), new Card(0, 0), new Card(3, 3)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.pickTrump(0);
            klaverjas.move(4, 1); //allowed J, 2
            assertEquals(2, klaverjas.getPlayers()[0].getPlayedCard().getPoints());
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));
            klaverjas.move(5, 0); //allowed Q troef, 3
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_THREE));
            klaverjas.move(1, 0); //not allowed
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_THREE));
            klaverjas.move(7, 0); //allowed A, 11
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_FOUR));

            klaverjas.move(3, 3); //allowed (for now), 10


            //player 3 wins slag
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_THREE));
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(0, klaverjas.getPlayers()[1].getScore());
            assertEquals(2 + 3 + 11 + 10, klaverjas.getPlayers()[2].getScore());
            assertEquals(0, klaverjas.getPlayers()[3].getScore());
        }

        @Test
        @DisplayName("mate does not have to over-trump")
        void mate_does_not_have_to_overtrump() throws Exception {
            Card[] cards1 = {new Card(4, 1), new Card(4, 2)};
            Card[] cards2 = {new Card(3, 2), new Card(5, 0)};
            Card[] cards3 = {new Card(2, 1), new Card(7, 0)};
            Card[] cards4 = {new Card(2, 2), new Card(2, 0)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.pickTrump(0);
            klaverjas.move(4, 1); //allowed J suit
            klaverjas.move(5, 0); //allowed Q troef
            klaverjas.move(2, 1); //allowed 9 suit
            klaverjas.move(2, 2); //allowed 9 other suit while 9 troef in hand

            //player 2 wins slag
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(5, klaverjas.getPlayers()[1].getScore());
            assertEquals(0, klaverjas.getPlayers()[2].getScore());
            assertEquals(0, klaverjas.getPlayers()[3].getScore());

        }

//        @Test
//        @DisplayName("If no choice, you can undertrump")
//        void you_can_undertrump() throws Exception{
//            Card[] cards1 = {new Card(4, 1),new Card(4, 2)};
//            Card[] cards2 = {new Card(3, 2),new Card(5, 0)};
//            Card[] cards3 = {new Card(2, 2),new Card(7, 0)};
//            Card[] cards4 = {new Card(1, 0),new Card(0, 0)};
//            klaverjas.getPlayers()[0].setHand(cards1);
//            klaverjas.getPlayers()[1].setHand(cards2);
//            klaverjas.getPlayers()[2].setHand(cards3);
//            klaverjas.getPlayers()[3].setHand(cards4);
//
//            klaverjas.pickTrump(0);
//            klaverjas.move(4,1); //allowed J suit
//            klaverjas.move(5,0); //allowed Q troef
//            klaverjas.move(7,0); //allowed A troef
//            klaverjas.move(0,0); //allowed 7 troef since no other troef
//
//            //player 2 wins slag
//            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_THREE));
//            assertEquals(0, klaverjas.getPlayers()[0].getScore());
//            assertEquals(0, klaverjas.getPlayers()[1].getScore());
//            assertEquals(0, klaverjas.getPlayers()[2].getScore());
//            assertEquals(0, klaverjas.getPlayers()[3].getScore());
//
//        }

    }

    @Nested
    @DisplayName("Roem, Stuk, sort, laatste slag, Nat, Pit and 4 of kind")
    class Specific_trump_things {

        KlaverjasImpl klaverjas = new KlaverjasImpl();

        @Test
        @DisplayName("Roem 20 points")
        void Roem() throws Exception {

            //setup hands of players
            Card[] cards1 = {new Card(4, 1), new Card(6, 1)}; //J
            Card[] cards2 = {new Card(3, 1), new Card(4, 0)}; //10
            Card[] cards3 = {new Card(7, 1), new Card(4, 3)}; //A
            Card[] cards4 = {new Card(5, 1), new Card(5, 0)}; //Q

            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.pickTrump(0); //1 is now trump
            //play move
            klaverjas.move(4, 1); //J, 2 points
            klaverjas.move(3, 1); //10, 10 poins
            klaverjas.move(7, 1); //A, 11 points
            klaverjas.move(5, 1); //Q, 3 points

            //player 3 wins slag, getting different amount of points
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(0, klaverjas.getPlayers()[1].getScore());
            assertEquals(11 + 2 + 3 + 10, klaverjas.getPlayers()[2].getScore());
            assertEquals(20, klaverjas.getPlayers()[2].getTrumpScore());
            assertEquals(0, klaverjas.getPlayers()[3].getScore());
        }

        @Test
        @DisplayName("Sorting")
        void Sort() {

            Card[] cards = klaverjas.getDeck().getCards();

            klaverjas.sortByRank(cards);

            //no cards lost
            assertEquals(32, cards.length);

            //from high to low, so first rank is A of diamonds
            assertEquals(7, cards[0].getRank());
            assertEquals(3, cards[0].getSuit());
            assertEquals(6, cards[1].getRank());
            assertEquals(3, cards[1].getSuit());
            assertEquals(5, cards[2].getRank());
            assertEquals(3, cards[2].getSuit());
            //next suit, A of clubs
            assertEquals(7, cards[8].getRank());
            assertEquals(2, cards[8].getSuit());

        }

        @Test
        @DisplayName("Roem 50 points")
        void Roem_50() throws Exception {

            //setup hands of players
            Card[] cards1 = {new Card(4, 1), new Card(6, 1)}; //J
            Card[] cards2 = {new Card(6, 1), new Card(4, 0)}; //K
            Card[] cards3 = {new Card(7, 1), new Card(4, 3)}; //A
            Card[] cards4 = {new Card(5, 1), new Card(5, 0)}; //Q

            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.pickTrump(0); //1 is now trump
            //play move
            klaverjas.move(4, 1); //J, 2 points
            klaverjas.move(6, 1); //K, 4 poins
            klaverjas.move(7, 1); //A, 11 points
            klaverjas.move(5, 1); //Q, 3 points

            //including Roem, + 50 points!

            //player 3 wins slag, getting different amount of points
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(0, klaverjas.getPlayers()[1].getScore());
            assertEquals(11 + 4 + 3 + 2, klaverjas.getPlayers()[2].getScore());
            assertEquals(50, klaverjas.getPlayers()[2].getTrumpScore());
            assertEquals(0, klaverjas.getPlayers()[3].getScore());
        }

        @Test
        @DisplayName("Stuk 20 points")
        void Stuk() throws Exception {

            //setup hands of players
            Card[] cards1 = {new Card(0, 1), new Card(6, 1)}; //J
            Card[] cards2 = {new Card(5, 1), new Card(4, 0)}; //K
            Card[] cards3 = {new Card(6, 1), new Card(4, 3)}; //A
            Card[] cards4 = {new Card(2, 1), new Card(5, 0)}; //Q

            klaverjas.pickTrump(1);

            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.pickTrump(1); //1 is now trump
            //play move
            klaverjas.move(0, 1); //7, 0 points
            klaverjas.move(5, 1); //Q, 3 poins
            klaverjas.move(6, 1); //K, 4 points
            klaverjas.move(2, 1); //9, 14 points

            //including stuk, + 20 points!

            //player 3 wins slag, getting different amount of points
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(0, klaverjas.getPlayers()[1].getScore());
            assertEquals(0, klaverjas.getPlayers()[2].getScore());
            assertEquals(3 + 4 + 14, klaverjas.getPlayers()[3].getScore());
            assertEquals(20, klaverjas.getPlayers()[3].getTrumpScore());
        }

        @Test
        @DisplayName("Roem + stuk 70 points")
        void Roem_Stuk() throws Exception {

            //setup hands of players
            Card[] cards1 = {new Card(4, 1), new Card(0, 1)}; //J
            Card[] cards2 = {new Card(6, 1), new Card(4, 0)}; //K
            Card[] cards3 = {new Card(7, 1), new Card(4, 3)}; //A
            Card[] cards4 = {new Card(5, 1), new Card(5, 0)}; //Q

            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.pickTrump(1); //1 is now trump
            //play move
            klaverjas.move(4, 1); //J, 20 points
            klaverjas.move(6, 1); //K, 4 poins
            klaverjas.move(7, 1); //A, 11 points
            klaverjas.move(5, 1); //Q, 3 points

            //including Roem + Stuk, 70 points!

            //player 3 wins slag, getting different amount of points
            assertEquals(11 + 4 + 3 + 20, klaverjas.getPlayers()[0].getScore());
            assertEquals(70, klaverjas.getPlayers()[0].getTrumpScore());
            assertEquals(0, klaverjas.getPlayers()[1].getScore());
            assertEquals(0, klaverjas.getPlayers()[2].getScore());
            assertEquals(0, klaverjas.getPlayers()[3].getScore());
        }

        @Test
        @DisplayName("Four of kind 100 points")
        void Four_of_kind() throws Exception {

            //setup hands of players
            Card[] cards1 = {new Card(4, 1)}; //J
            Card[] cards2 = {new Card(4, 2)}; //J
            Card[] cards3 = {new Card(4, 0)}; //J
            Card[] cards4 = {new Card(4, 3)}; //J

            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.pickTrump(1); //0 is now trump
            //play move
            klaverjas.move(4, 1); //J, 2 points
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));
            klaverjas.move(4, 2); //J, 20 poins
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_THREE));
            klaverjas.move(4, 0); //J, 2 points
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_FOUR));
            klaverjas.move(4, 3); //J, 2 points
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));

            //player 3 wins slag, getting different amount of points
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(0, klaverjas.getPlayers()[1].getScore());
            assertEquals(0, klaverjas.getPlayers()[2].getScore());
            assertEquals(0, klaverjas.getPlayers()[3].getScore());
        }

        @Test
        @DisplayName("Nat")
        void Nat() throws Exception {

            klaverjas.setPlayersRoundTurn(0);

            //setup hands of players
            klaverjas.getPlayers()[0].setScore(80);
            klaverjas.getPlayers()[1].setScore(102);
            klaverjas.getPlayers()[2].setScore(0);
            klaverjas.getPlayers()[3].setScore(0);

            klaverjas.setTeamScore();

            //team 1 is nat, giving all the points to team 2
            assertEquals(0, klaverjas.getTeam1Score());
            assertEquals(182, klaverjas.getTeam2Score());

        }

        @Test
        @DisplayName("Pit")
        void Pit() throws Exception {

            klaverjas.setPlayersRoundTurn(0);

            //setup hands of players
            klaverjas.getPlayers()[0].setScore(182);
            klaverjas.getPlayers()[1].setScore(0);
            klaverjas.getPlayers()[2].setScore(0);
            klaverjas.getPlayers()[3].setScore(0);

            klaverjas.setTeamScore();

            //team 1 is nat, giving all the points to team 2
            assertEquals(282, klaverjas.getTeam1Score());
            assertEquals(0, klaverjas.getTeam2Score());
        }

        @Test
        @DisplayName("Laatste slag")
        void Laatste_slag() throws Exception {

            //setup hands of players
            Card[] cards1 = {new Card(4, 1)}; //J
            Card[] cards2 = {new Card(3, 1)}; //10
            Card[] cards3 = {new Card(7, 1)}; //A
            Card[] cards4 = {new Card(5, 1)}; //Q

            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.pickTrump(0); //1 is now trump
            //play move
            klaverjas.move(4, 1); //J, 2 points
            klaverjas.move(3, 1); //10, 10 poins
            klaverjas.move(7, 1); //A, 11 points
            klaverjas.move(5, 1); //Q, 3 points

            //player 3 wins slag, and gets 10 points extra for the laatste slag, plus 20 for roem and plus 100 for pit
            assertEquals(11 + 10 + 2 + 3 + 10 + 20 + 100, klaverjas.getTeam1Score());
            assertEquals(0, klaverjas.getTeam2Score());

        }
    }
}


