package klaverjas.api;

import java.io.IOException;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import klaverjas.api.models.*;
import klaverjas.domain.KlaverjasException;
import klaverjas.domain.KlaverjasImpl;
import klaverjas.domain.Card;

@Path("/pick")
public class PickTrumpKlaverjas {
    @POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialize(
			@Context HttpServletRequest request, 
			TrumpInput trumpinput) throws Exception {
		HttpSession session = request.getSession(true);
		KlaverjasImpl klaverjas = (KlaverjasImpl) session.getAttribute("klaverjas");
		String namePlayer1 = (String) session.getAttribute("player1");
		String namePlayer2 = (String) session.getAttribute("player2");
		String namePlayer3 = (String) session.getAttribute("player3");
		String namePlayer4 = (String) session.getAttribute("player4");
		Integer trump = trumpinput.getSuit();

		//apply move to the played card
		klaverjas.pickTrump(trump);

		//setattribute, and create klaverjas from API to send to server
		session.setAttribute("klaverjas", klaverjas);
		var output = new Klaverjas(klaverjas, namePlayer1, namePlayer2, namePlayer3, namePlayer4);

		return Response.status(200).entity(output).build();
	}
}
