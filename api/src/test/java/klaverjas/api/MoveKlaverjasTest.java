package klaverjas.api;

import klaverjas.domain.KlaverjasException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.*;
import jakarta.ws.rs.core.*;

import klaverjas.api.models.*;
import klaverjas.domain.KlaverjasImpl;

public class MoveKlaverjasTest {

    @Test
    public void PerformingAMoveShouldBeAllowed() {
        var response = startKlaverjas("Mario", "Luigi", "Bowser", "Peach");
        assertEquals(200, response.getStatus());
    }

    private Response startKlaverjas(String namePlayer1, String namePlayer2, String namePlayer3, String namePlayer4) {
        var servlet = new StartKlaverjas();
        var request = createRequestContext();
        var input = playerInput(namePlayer1, namePlayer2, namePlayer3, namePlayer4);
        return servlet.initialize(request, input);
    }

    private Response moveKlaverjas(Integer index) {
        var servlet = new MoveKlaverjas();
        var request = createRequestContextForMove();


        var input = cardInput(index);
        return servlet.initialize(request, input);
    }

    private HttpServletRequest createRequestContextForMove() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(session);

        var klaverjas = new KlaverjasImpl();

        klaverjas.getDeck().shuffleDeck();
        klaverjas.deal();
        String namePlayer1 = "Mario";
        String namePlayer2 = "Luigi";
        String namePlayer3 = "Bowser";
        String namePlayer4 = "Peach";

        session.setAttribute("klaverjas", klaverjas);
        session.setAttribute("player1", namePlayer1);
        session.setAttribute("player2", namePlayer2);
        session.setAttribute("player3", namePlayer3);
        session.setAttribute("player4", namePlayer4);

        return request;
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

    private CardInput cardInput(Integer index) {
        var input = new CardInput();
        input.setIndex(index);
        return input;
    }
}