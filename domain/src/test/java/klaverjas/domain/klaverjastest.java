package klaverjas.domain;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

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
            assertEquals(4, klaverjas.getPlayers().length);
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
            assertEquals("7 of Clubs", card.toString());
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
        void first_player_move() {
            klaverjas.getDeck().shuffleDeck();
            klaverjas.deal();

            //player 1 has not played a card yet
            assertNull(klaverjas.getPlayers()[0].getPlayedCard());

            //player plays a move
            Card card = klaverjas.getPlayers()[0].getHand().get(0);
            klaverjas.move(0);

            //player 1 has played a card
            assertTrue(klaverjas.getPlayers()[0].hasPlayedCard());

            //the 0th card played by player1 is the same as the 0th card in the slag list
            assertEquals(card, klaverjas.getPlayers()[0].getPlayedCard());

            //player 1 only has 7 cards now in his hand
            assertEquals(7, klaverjas.getPlayers()[0].getHand().size());

            //player2 now has the turn
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));
        }
    }


    @Nested
    @DisplayName("End of a slag of klaverjassen")
    class end_of_a_slag_of_klaverjassen {

        KlaverjasImpl klaverjas = new KlaverjasImpl();

        @Test
        @DisplayName("We play a full slag of klaverjassen")
        void Full_slag() {

            //setup hands of players
            Card[] cards1 = { new Card(4,0), new Card(6,1) };
            Card[] cards2 = { new Card(5,0), new Card(2,1) };
            Card[] cards3 = { new Card(6,0), new Card(3,1) };
            Card[] cards4 = { new Card(7,0), new Card(5,1) };
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(0);
            assertTrue(klaverjas.getPlayers()[0].hasPlayedCard());

            klaverjas.move(0);
            assertTrue(klaverjas.getPlayers()[1].hasPlayedCard());

            klaverjas.move(0);
            assertTrue(klaverjas.getPlayers()[2].hasPlayedCard());

            klaverjas.move(0);

            //played cards are deleted, so no more playedCards
            assertFalse(klaverjas.getPlayers()[0].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[1].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[2].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[3].hasPlayedCard());

            //player 4 wins slag, getting 28 points for team 2
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(0, klaverjas.getPlayers()[1].getScore());
            assertEquals(0, klaverjas.getPlayers()[2].getScore());
            assertEquals(28, klaverjas.getPlayers()[3].getScore());

            //player 4 now has back the turn
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_FOUR));
        }

        @Test
        @DisplayName("Playing card of other suit is not allowed when you can follow")
        void Slag_not_allowed() {

            //setup hands of players
            Card[] cards1 = { new Card(5,0), new Card(3,2) };
            Card[] cards2 = { new Card(4,0), new Card(2,2) };
            Card[] cards3 = { new Card(2,0), new Card(1,2) };
            Card[] cards4 = { new Card(7,1), new Card(1,0) };
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(0);
            klaverjas.move(0);
            klaverjas.move(0);
            klaverjas.move(0);

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
        void Slag_allowed_othersuite() {

            //setup hands of players
            Card[] cards1 = { new Card(5,0), new Card(3,2) };
            Card[] cards2 = { new Card(4,0), new Card(2,2) };
            Card[] cards3 = { new Card(2,0), new Card(1,2) };
            Card[] cards4 = { new Card(7,1), new Card(1,3) };
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(0);
            klaverjas.move(0);
            klaverjas.move(0);
            klaverjas.move(0);

            //All players play an allowed move, removing played cards
            assertFalse(klaverjas.getPlayers()[0].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[1].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[2].hasPlayedCard());
            assertFalse(klaverjas.getPlayers()[3].hasPlayedCard());

            //player 1 wins slag, getting 18 points
            assertEquals(18, klaverjas.getPlayers()[0].getScore());

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
        void teamsGetScore() {

            //setup hands of players
            Card[] cards1 = {new Card(4, 0)};
            Card[] cards2 = {new Card(5, 0)};
            Card[] cards3 = {new Card(6, 0)};
            Card[] cards4 = {new Card(7, 0)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(0);
            klaverjas.move(0);
            klaverjas.move(0);
            klaverjas.move(0);

            //player 4 wins slag and ends rounds, removing score from players and added to team
            assertEquals(0, klaverjas.getPlayers()[0].getScore());
            assertEquals(0, klaverjas.getPlayers()[1].getScore());
            assertEquals(0, klaverjas.getPlayers()[2].getScore());
            assertEquals(0, klaverjas.getPlayers()[3].getScore());
            assertEquals(0, klaverjas.getTeam1Score());
            assertEquals(28, klaverjas.getTeam2Score());
        }
        @Test
        @DisplayName("When round ends, deck is shuffled and dealt")
        void playersGetNewCards() {
            //setup hands of players
            Card[] cards1 = {new Card(4, 0)};
            Card[] cards2 = {new Card(5, 0)};
            Card[] cards3 = {new Card(6, 0)};
            Card[] cards4 = {new Card(7, 0)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(0);
            klaverjas.move(0);
            klaverjas.move(0);
            klaverjas.move(0);

            //every player has now 8 cards again in hand, dealt from shuffled deck
            assertEquals(8, klaverjas.getPlayers()[0].getHand().size());
            assertEquals(8, klaverjas.getPlayers()[1].getHand().size());
            assertEquals(8, klaverjas.getPlayers()[2].getHand().size());
            assertEquals(8, klaverjas.getPlayers()[3].getHand().size());
        }
        @Test
        @DisplayName("When round ends, turn is given to player right of player who has hasRoundTurn")
        void correctRoundTurn(){
            //setup hands of players
            Card[] cards1 = {new Card(4, 0)};
            Card[] cards2 = {new Card(5, 0)};
            Card[] cards3 = {new Card(6, 0)};
            Card[] cards4 = {new Card(7, 0)};
            klaverjas.getPlayers()[0].setHand(cards1);
            klaverjas.getPlayers()[1].setHand(cards2);
            klaverjas.getPlayers()[2].setHand(cards3);
            klaverjas.getPlayers()[3].setHand(cards4);

            klaverjas.move(0);
            klaverjas.move(0);
            klaverjas.move(0);
            klaverjas.move(0);

            //player 2 now starts new round, since player 1 started before
            assertTrue(klaverjas.isPlayersTurn(Klaverjas.PLAYER_TWO));
        }

    }


}