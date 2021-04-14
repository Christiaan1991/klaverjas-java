package klaverjas.api;

import java.io.IOException;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import klaverjas.api.models.*;
import klaverjas.domain.KlaverjasException;
import klaverjas.domain.KlaverjasImpl;

@Path("/move")
public class MoveKlaverjas {
    @POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialize(
			@Context HttpServletRequest request, 
			CardInput indexinput) throws KlaverjasException {
		HttpSession session = request.getSession(true);
		KlaverjasImpl klaverjas = (KlaverjasImpl) session.getAttribute("mancala");
		String namePlayer1 = klaverjas.getPlayers()[0].getName();
		String namePlayer2 = klaverjas.getPlayers()[1].getName();
		String namePlayer3 = klaverjas.getPlayers()[2].getName();
		String namePlayer4 = klaverjas.getPlayers()[3].getName();

		//something happens with the CardInput Integer (card number)

		session.setAttribute("klaverjas", klaverjas);
		session.setAttribute("player1", namePlayer1);
		session.setAttribute("player2", namePlayer2);
		session.setAttribute("player3", namePlayer3);
		session.setAttribute("player3", namePlayer4);

		var output = new Klaverjas(klaverjas, namePlayer1, namePlayer2, namePlayer3, namePlayer4);

		return Response.status(200).entity(output).build();
	}
}
