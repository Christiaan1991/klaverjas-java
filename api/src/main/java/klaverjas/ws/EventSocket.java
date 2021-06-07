package klaverjas.ws;

import com.google.gson.Gson;
import klaverjas.api.models.Klaverjas;
import klaverjas.domain.KlaverjasImpl;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class EventSocket extends WebSocketAdapter
{
    private static HashMap<EventSocket, String> sockets = new HashMap<>(); //all connections that are made
    private final CountDownLatch closureLatch = new CountDownLatch(1);
    private Session session;
    private String userId;
    private static HashMap<EventSocket, String[]> players = new HashMap<>();
    private final static KlaverjasImpl klaverjas = new KlaverjasImpl();
    private static int numOfConnectedPlayers = 0;

    @Override
    public void onWebSocketConnect(Session session){

        this.session = session;

        //create new userId
        this.userId = this.getMyUserId();

        // map this unique ID to this connection
        EventSocket.sockets.put(this, this.userId);

        //create new gamesetup for this connection
        Gamesetup game = new Gamesetup();
        game.setUserId(userId);
        game.setMethod("connect");

        //we package the Gamesetup obj back into json, and send it to the client who created the room!
        Gson gson2 = new Gson();
        String payload = gson2.toJson(game);


        // broadcast this new connection (with its unique ID) to all connected clients
        for (EventSocket dstSocket : EventSocket.sockets.keySet()) {
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
                String name = setup.getName();

                String[] playerKey = {name, String.valueOf(numOfConnectedPlayers)};

                //add name to the list of clients
                players.put(this, playerKey);

                //we set the number of connected clients in the Gamesetup
                setup.setNumberOfPlayers(players.size());
                setup.setName();
                numOfConnectedPlayers++;

                if(setup.getNumberOfPlayers() == 4){
                    System.out.println("We can start the game here!");
                    setup.setGamestate(createNewKlaverjasGame());
                }
                sendToAllClients(setup);
            }

            else if(setup.getMethod().equals("pick")){
                //check if websocket matches with players turn
                if(klaverjas.isPlayersTurn(Integer.parseInt(players.get(this)[1]))){
                    setup = pickTrump(setup);
                    sendToAllClients(setup);
                } else{
                    System.out.println("move not allowed by this player!");
                }

            }

            else if(setup.getMethod().equals("play")){
                if(klaverjas.isPlayersTurn(Integer.parseInt(players.get(this)[1]))) {
                    setup = performMove(setup);
                    sendToAllClients(setup);
                } else{
                    System.out.println("move not allowed by this player!");
                }
            }

        } catch(NumberFormatException e){
            System.out.println("Something went wrong!");
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode, reason);

        //remove from sockets list and clients list
        sockets.remove(this);

        //if socket is also in players, remove it in player as well!
        if(players.containsKey(this)){
            players.remove(this);
        }

        System.out.println(sockets);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
        closureLatch.countDown();
    }

    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
    }

    public void awaitClosure() throws InterruptedException
    {
        System.out.println("Awaiting closure from remote");
        closureLatch.await();
    }

    private String getMyUserId() {
        return Integer.toHexString(this.hashCode());
    }

    private void sendToClient(String str) {
        try {
            this.session.getRemote().sendString(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Klaverjas createNewKlaverjasGame() {

        //shuffle cards and deal to players
        klaverjas.getDeck().shuffleDeck();
        klaverjas.deal();

        String[] names = getAllNames();
        return new Klaverjas(klaverjas, names[0], names[1], names[2], names[3]);

    }

    public Gamesetup performMove(Gamesetup setup){
        Integer suit = setup.getSuit();
        Integer rank = setup.getRank();

        try {
            //apply move to the played card
            klaverjas.move(rank, suit);
        } catch (Exception e){
            System.out.println("move cannot be performed");
        }

        //create new klaverjas state, and set gameState
        String[] names = getAllNames();
        setup.setGamestate(new Klaverjas(klaverjas, names[0], names[1], names[2], names[3]));
        return setup;
    }

    public Gamesetup pickTrump(Gamesetup setup){

        Integer trump = setup.getTrump();

        //apply move to the played card
        klaverjas.pickTrump(trump);

        //create new klaverjas state, and set gameState
        String[] names = getAllNames();
        setup.setGamestate(new Klaverjas(klaverjas, names[0], names[1], names[2], names[3]));
        return setup;
    }

    public void sendToAllClients(Gamesetup setup){
        //we package the Gamesetup obj back into json, and send it to the client who created the room!
        Gson gson2 = new Gson();
        String payload = gson2.toJson(setup);

        for (EventSocket dstSocket : EventSocket.sockets.keySet()) {
            dstSocket.sendToClient(payload);
        }
    }

    public String[] getAllNames(){
        String[] names = new String[4];
        int i = 0;

        for (String[] values : players.values()){
            names[i] = values[0];
            i++;
        }
        return names;
    }

}
