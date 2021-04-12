package mancala.domain;

// Your test class should be in the same
// package as the class you're testing.
// Usually the test directory mirrors the
// main directory 1:1. So for each class in src/main,
// there is a class in src/test.

// Import our test dependencies. We import the Test-attribute
// and a set of assertions.

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MancalaImplTest {

    /* Create player, which will create the mancala board */
    MancalaImpl mancala = new MancalaImpl();

    @Nested
    @DisplayName("Initialization of mancala")
    class Mancala_intialize {

        @Test
        @DisplayName("Setup of the Board")
        void Player_setup() {
            assertAll("start playing field",
                    () -> assertEquals(4, mancala.getStonesForPit(0)),
                    () -> assertEquals(4, mancala.getStonesForPit(1)),
                    () -> assertEquals(4, mancala.getStonesForPit(2)),
                    () -> assertEquals(4, mancala.getStonesForPit(3)),
                    () -> assertEquals(4, mancala.getStonesForPit(4)),
                    () -> assertEquals(4, mancala.getStonesForPit(5)),
                    () -> assertEquals(0, mancala.getStonesForPit(6)),
                    () -> assertEquals(4, mancala.getStonesForPit(7)),
                    () -> assertEquals(4, mancala.getStonesForPit(8)),
                    () -> assertEquals(4, mancala.getStonesForPit(9)),
                    () -> assertEquals(4, mancala.getStonesForPit(10)),
                    () -> assertEquals(4, mancala.getStonesForPit(11)),
                    () -> assertEquals(4, mancala.getStonesForPit(12)),
                    () -> assertEquals(0, mancala.getStonesForPit(13)));
        }
    }

    @Test
    void MancalaMoveFirstPit() throws MancalaException {

        MancalaImpl mancala = new MancalaImpl();
        mancala.playPit(0);
        assertAll("Board setup by checking Bowl ID's",
                () -> assertEquals(0, mancala.getStonesForPit(0)),
                () -> assertEquals(5, mancala.getStonesForPit(1)),
                () -> assertEquals(5, mancala.getStonesForPit(2)),
                () -> assertEquals(5, mancala.getStonesForPit(3)),
                () -> assertEquals(5, mancala.getStonesForPit(4)),
                () -> assertEquals(4, mancala.getStonesForPit(5)));
    }

    @Test
    void MancalaMoveFifthPit() throws MancalaException {

        MancalaImpl mancala = new MancalaImpl();
        mancala.playPit(5);
        assertAll("Board setup by checking Bowl ID's",
                () -> assertEquals(0, mancala.getStonesForPit(5)),
                () -> assertEquals(1, mancala.getStonesForPit(6)),
                () -> assertEquals(5, mancala.getStonesForPit(7)),
                () -> assertEquals(5, mancala.getStonesForPit(8)),
                () -> assertEquals(5, mancala.getStonesForPit(9)),
                () -> assertEquals(4, mancala.getStonesForPit(10)));
    }

    @Test
    void MancalaMoveOpponentPit() throws MancalaException {

        MancalaImpl mancala = new MancalaImpl();
        mancala.playPit(0);
        mancala.playPit(7);
        assertAll("Board setup by checking Bowl ID's",
                () -> assertEquals(0, mancala.getStonesForPit(7)),
                () -> assertEquals(5, mancala.getStonesForPit(8)),
                () -> assertEquals(5, mancala.getStonesForPit(9)),
                () -> assertEquals(5, mancala.getStonesForPit(10)),
                () -> assertEquals(5, mancala.getStonesForPit(11)),
                () -> assertEquals(4, mancala.getStonesForPit(12)));
    }

    @Test
    void MancalaMoveOpponentPitThroughMancala() throws MancalaException {

        MancalaImpl mancala = new MancalaImpl();
        mancala.playPit(0);
        mancala.playPit(11);
        assertAll("Board setup by checking Bowl ID's",
                () -> assertEquals(0, mancala.getStonesForPit(11)),
                () -> assertEquals(5, mancala.getStonesForPit(12)),
                () -> assertEquals(1, mancala.getStonesForPit(13)),
                () -> assertEquals(1, mancala.getStonesForPit(0)),
                () -> assertEquals(6, mancala.getStonesForPit(1)),
                () -> assertEquals(5, mancala.getStonesForPit(2)));
    }

    @Test
    void MancalaMovePlayerTurn() throws MancalaException {

        Mancala mancala = new MancalaImpl();

        //player 1 turn
        assertTrue(mancala.isPlayersTurn(1));
        mancala.playPit(1);

        //player 2 turn
        assertTrue(mancala.isPlayersTurn(2));
        mancala.playPit(8);

        //player 1 turn
        //perform move which is not allowed!
        assertTrue(mancala.isPlayersTurn(1));
        mancala.playPit(9);

        //make sure that it is still player 1 turn!
        assertTrue(mancala.isPlayersTurn(1));

    }


    @Test
    void MancalaMoveOwnMancalaNotAllowed() throws MancalaException {

        MancalaImpl mancala = new MancalaImpl();
        assertThrows(NullPointerException.class,
                ()->{
                    mancala.playPit(6);
                    mancala.playPit(12);
                });
    }

    @Test
    void MancalaMoveToOwnKalaha() throws MancalaException {

        MancalaImpl mancala = new MancalaImpl();
        mancala.playPit(2);
        assertAll("Board setup by checking Bowl ID's",
                () -> assertEquals(0, mancala.getStonesForPit(2)),
                () -> assertEquals(5, mancala.getStonesForPit(3)),
                () -> assertEquals(5, mancala.getStonesForPit(4)),
                () -> assertEquals(5, mancala.getStonesForPit(5)),
                () -> assertEquals(1, mancala.getStonesForPit(6)),
                () -> assertEquals(4, mancala.getStonesForPit(7)));
    }
}