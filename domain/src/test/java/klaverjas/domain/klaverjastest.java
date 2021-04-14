package klaverjas.domain;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class klaverjastest {

    @Nested
    @DisplayName("Initialization of klaverjassen")
    class klaverjas_intialize {

        /* Create player, which will create the mancala board */
        KlaverjasImpl klaverjas = new KlaverjasImpl();

        @Test
        @DisplayName("Four players generated")
        void Team_setup() {
            assertEquals(4, klaverjas.getPlayers().length);
        }

        @Test
        @DisplayName("Players are divided in two teams")
        void Player_setup() {
            assertEquals(4, klaverjas.getPlayers().length);
            assertEquals(6, klaverjas.getPlayers()[0].getTeam());
            assertEquals(7, klaverjas.getPlayers()[1].getTeam());
            assertEquals("player1", klaverjas.getPlayers()[0].getName());
            assertEquals("player2", klaverjas.getPlayers()[1].getName());
            assertEquals("player3", klaverjas.getPlayers()[2].getName());
            assertEquals("player4", klaverjas.getPlayers()[3].getName());
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


}