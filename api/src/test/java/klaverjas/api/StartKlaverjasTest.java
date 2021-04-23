package klaverjas.api;

import klaverjas.domain.KlaverjasException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.*;
import jakarta.ws.rs.core.*;

import klaverjas.api.models.*;
import klaverjas.domain.KlaverjasImpl;

public class StartKlaverjasTest {

    @Test
    public void startingKlaverjassenShouldBeAllowed() {
        var response = startKlaverjas("Mario", "Luigi", "Bowser", "Peach");
        assertEquals(200, response.getStatus());
    }

    @Test
    public void startingKlaverjassenReturnsAGameWithoutAWinner() {
        var response = startKlaverjas("Mario", "Luigi", "Bowser", "Peach");
        var entity = (Klaverjas)response.getEntity();
        var gameState = entity.getGameStatus();
        assertFalse(gameState.getEndOfGame());
        assertNull(gameState.getWinner());
    }

    @Test
    public void startingKlaverjassenReturnsThePlayerData() {
        var response = startKlaverjas("Mario", "Luigi", "Bowser", "Peach");
        var entity = (Klaverjas)response.getEntity();
        var players = entity.getPlayers();
        assertEquals(4, players.length);
        assertEquals("Mario", players[0].getName());
        assertEquals("Luigi", players[1].getName());
        assertEquals("Bowser", players[2].getName());
        assertEquals("Peach", players[3].getName());
    }

    @Test
    public void startingKlaverjassenReturnsCardsToPlayers() {
        var response = startKlaverjas("Mario", "Luigi", "Bowser", "Peach");
        var entity = (Klaverjas)response.getEntity();
        var players = entity.getPlayers();
        assertEquals(8, players[0].getCards().length);
        assertEquals(8, players[1].getCards().length);
        assertEquals(8, players[2].getCards().length);
        assertEquals(8, players[3].getCards().length);

        assertTrue(players[0].getHasTurn());
        assertFalse(players[1].getHasTurn());
        assertFalse(players[2].getHasTurn());
        assertFalse(players[3].getHasTurn());
    }

    @Test
    public void startingKlaverjassenStartsANewSession() {
        startKlaverjas("Mario", "Luigi", "Bowser", "Peach");
        verify(request).getSession(true);
    }


    @Test
    public void startingKlaverjassenSavesTheNewGameInASession() {
        startKlaverjas("Mario", "Luigi", "Bowser", "Peach");
        verify(session).setAttribute(eq("klaverjas"), any(KlaverjasImpl.class));
    }

    @Test
    public void startingMancalaSavesTheNamesInASession() {
        startKlaverjas("Mario", "Luigi", "Bowser", "Peach");
        verify(session).setAttribute("player1", "Mario");
        verify(session).setAttribute("player2", "Luigi");
        verify(session).setAttribute("player3", "Bowser");
        verify(session).setAttribute("player4", "Peach");
    }

    private Response startKlaverjas(String namePlayer1, String namePlayer2, String namePlayer3, String namePlayer4) {
        var servlet = new StartKlaverjas();
        var request = createRequestContext();
        var input = playerInput(namePlayer1, namePlayer2, namePlayer3, namePlayer4);
        return servlet.initialize(request, input);
    }

    private HttpServletRequest createRequestContext() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(session);
        return request;
    }

    private HttpServletRequest request;
    private HttpSession session;

    private PlayerInput playerInput(String namePlayer1, String namePlayer2, String namePlayer3, String namePlayer4) {
        var input = new PlayerInput();
        input.setNameplayer1(namePlayer1);
        input.setNameplayer2(namePlayer2);
        input.setNameplayer3(namePlayer3);
        input.setNameplayer4(namePlayer4);
        return input;
    }
}