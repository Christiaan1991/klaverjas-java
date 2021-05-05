package klaverjas.ws;

import com.google.gson.Gson;
import klaverjas.api.models.Klaverjas;
import klaverjas.domain.KlaverjasImpl;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class EventSocket extends WebSocketAdapter
{
    private final static HashMap<String, EventSocket> sockets = new HashMap<>(); //all connections that are made
    private final CountDownLatch closureLatch = new CountDownLatch(1);
    private Session session;
    private String userId;
    private final static HashMap<String, ArrayList<String>> games = new HashMap<>();
    private ArrayList<String> clients = new ArrayList<>();
    private KlaverjasImpl klaverjas;

    @Override
    public void onWebSocketConnect(Session session){

        this.session = session;

        //create new userId
        this.userId = this.getMyUserId();

        // map this unique ID to this connection
        EventSocket.sockets.put(this.userId, this);

        //create new gamesetup
        Gamesetup game = new Gamesetup();
        game.setUserId(userId);
        game.setMethod("connect");

        //we package the Gamesetup obj back into json, and send it to the client who created the room!
        Gson gson2 = new Gson();
        String payload = gson2.toJson(game);


        // broadcast this new connection (with its unique ID) to all connected clients
        for (EventSocket dstSocket : EventSocket.sockets.values()) {
            dstSocket.sendToClient(payload);
        }

    }

    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);

        try {
            Gson gson = new Gson();
            Gamesetup setup = gson.fromJson(message, Gamesetup.class);

            if(setup.getMethod().equals("join")){

                //get userId
                String userId = setup.getUserId();
                //generate new game ID
                String name = setup.getName(); //for test

                //add name to the list of clients
                clients.add(name);

                //we set the number of connected clients in the Gamesetup
                setup.setNumberOfPlayers(clients.size());
                setup.setName();
                System.out.println(clients.size());

                if(setup.getNumberOfPlayers() == 4){
                    System.out.println("We can start the game here!");
                    setup.setGamestate(createNewKlaverjasGame(clients));
                }
                sendToAllClients(setup);
            }

            else if(setup.getMethod().equals("pick")){

                //System.out.println("Troef gekozen!");
                setup = pickTrump(setup, clients);
                sendToAllClients(setup);
            }

            else if(setup.getMethod().equals("play")){

                //System.out.println("Performing move");
                setup = performMove(setup, clients);
                sendToAllClients(setup);
            }

        } catch(NumberFormatException e){
            System.out.println("Something went wrong!");
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode, reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
        closureLatch.countDown();
    }

    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        //cause.printStackTrace(System.err);
    }

    public void awaitClosure() throws InterruptedException
    {
        System.out.println("Awaiting closure from remote");
        closureLatch.await();
    }

    private String getMyUserId() {
        // unique ID from this class' hash code
        return Integer.toHexString(this.hashCode());
    }

    private void sendToClient(String str) {
        try {
            this.session.getRemote().sendString(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Klaverjas createNewKlaverjasGame(ArrayList<String> clients) {
        klaverjas = new KlaverjasImpl();
        String namePlayer1 = clients.get(0);
        String namePlayer2 = clients.get(1);
        String namePlayer3 = clients.get(2);
        String namePlayer4 = clients.get(3);

        //shuffle cards and deal to players
        klaverjas.getDeck().shuffleDeck();
        klaverjas.deal();

        return new Klaverjas(klaverjas, namePlayer1, namePlayer2, namePlayer3, namePlayer4);

    }

    public Gamesetup performMove(Gamesetup setup, ArrayList<String> clients){
        Integer suit = setup.getSuit();
        Integer rank = setup.getRank();

        try {
            //apply move to the played card
            klaverjas.move(rank, suit);
        } catch (Exception e){
            System.out.println("move cannot be performed");
        }

        String namePlayer1 = clients.get(0);
        String namePlayer2 = clients.get(1);
        String namePlayer3 = clients.get(2);
        String namePlayer4 = clients.get(3);

        //create new klaverjas state, and set gameState
        setup.setGamestate(new Klaverjas(klaverjas, namePlayer1, namePlayer2, namePlayer3, namePlayer4));
        return setup;
    }

    public Gamesetup pickTrump(Gamesetup setup, ArrayList<String> clients){
        Integer trump = setup.getTrump();

        //apply move to the played card
        klaverjas.pickTrump(trump);

        String namePlayer1 = clients.get(0);
        String namePlayer2 = clients.get(1);
        String namePlayer3 = clients.get(2);
        String namePlayer4 = clients.get(3);

        //create new klaverjas state, and set gameState
        setup.setGamestate(new Klaverjas(klaverjas, namePlayer1, namePlayer2, namePlayer3, namePlayer4));
        return setup;
    }

    public void sendToAllClients(Gamesetup setup){
        //we package the Gamesetup obj back into json, and send it to the client who created the room!
        Gson gson2 = new Gson();
        String payload = gson2.toJson(setup);

        for (EventSocket dstSocket : EventSocket.sockets.values()) {
            dstSocket.sendToClient(payload);
        }
    }

}
