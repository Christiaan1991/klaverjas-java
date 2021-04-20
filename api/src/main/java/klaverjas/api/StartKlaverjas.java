package klaverjas.api;

import java.io.IOException;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import klaverjas.api.models.*;
import klaverjas.domain.KlaverjasImpl;

@Path("/start")
public class StartKlaverjas {
    @POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialize(
			@Context HttpServletRequest request, 
			PlayerInput players) {

        var klaverjas = new KlaverjasImpl();
        String namePlayer1 = players.getNameplayer1();
		String namePlayer2 = players.getNameplayer2();
		String namePlayer3 = players.getNameplayer3();
		String namePlayer4 = players.getNameplayer4();

		//shuffle cards and deal to players
		klaverjas.getDeck().shuffleDeck();
		klaverjas.deal();
		
        HttpSession session = request.getSession(true);
        session.setAttribute("klaverjas", klaverjas);
        session.setAttribute("player1", namePlayer1);
        session.setAttribute("player2", namePlayer2);
		session.setAttribute("player3", namePlayer3);
		session.setAttribute("player4", namePlayer4);

		var output = new Klaverjas(klaverjas, namePlayer1, namePlayer2, namePlayer3, namePlayer4);
		return Response.status(200).entity(output).build();
	}
}
