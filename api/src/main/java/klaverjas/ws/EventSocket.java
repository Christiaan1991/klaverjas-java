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
    private static final HashMap<EventSocket, String> sockets = new HashMap<>(); //all connections that are made
    private final CountDownLatch closureLatch = new CountDownLatch(1);
    private Session session;

    private static final HashMap<EventSocket, String[]> players = new HashMap<>();
    private final static KlaverjasImpl klaverjas = new KlaverjasImpl();
    private static int numOfConnectedPlayers = 0;

    @Override
    public void onWebSocketConnect(Session session){

        this.session = session;

        //create new userId
        String userId = this.getMyUserId();

        // map this unique ID to this connection
        EventSocket.sockets.put(this, userId);

        //create new reponse
        Response response = new Response();
        response.setUserId(userId);

        //we package the Gamesetup obj back into json, and send it to the client who created the room!
        Gson gson2 = new Gson();
        String payload = gson2.toJson(response);


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
            Input input = gson.fromJson(message, Input.class);

            if(input.getMethod().equals("join")){

                //generate new game ID
                String name = input.getName();

                //add name to the list of players, and give each connection a name and which player
                String[] playerKey = {name, String.valueOf(numOfConnectedPlayers)};
                players.put(this, playerKey);
                numOfConnectedPlayers++;

                System.out.println(players);

                if(players.size() != 4){
                    //we set the number of connected clients in the Gamesetup
                    Response response = new Response();
                    response.setNumberOfPlayers(players.size());
                    response.setName(name);
                    sendResponse(response);
                }

                else{
                    System.out.println("We can start the game here!");
                    System.out.println(klaverjas);
                    klaverjas.getDeck().shuffleDeck();
                    klaverjas.deal();
                    klaverjas.sortHands();

                    sendGameState();
                }

            }

            else if(input.getMethod().equals("pick")){
                //check if websocket matches with players turn
                if(klaverjas.isPlayersTurn(Integer.parseInt(players.get(this)[1]))){
                    pickTrump(input);
                    sendGameState();
                } else{
                    System.out.println("move not allowed by this player!");
                }

            }

            else if(input.getMethod().equals("play")){
                if(klaverjas.isPlayersTurn(Integer.parseInt(players.get(this)[1]))) {
                    performMove(input);
                    sendGameState();
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

    public void performMove(Input input){
        Integer suit = input.getSuit();
        Integer rank = input.getRank();

        try {
            //apply move to the played card
            klaverjas.move(rank, suit);
        } catch (Exception e){
            System.out.println("move cannot be performed");
        }
    }

    public void pickTrump(Input input){

        Integer trump = input.getTrump();

        //apply move to the played card
        klaverjas.pickTrump(trump);
        klaverjas.sortHands();

    }

    public void sendResponse(Response response){

        for (EventSocket dstSocket : EventSocket.players.keySet()) {

            //we package the reponse back into json, and send it to the client!
            Gson gson2 = new Gson();
            String payload = gson2.toJson(response);
            dstSocket.sendToClient(payload);
        }
    }

    public void sendGameState(){

        //create new response
        Response response = new Response();
        response.setNumberOfPlayers(players.size());

        for (EventSocket dstSocket : EventSocket.players.keySet()) {

            //for every websocket, we create a seperate gameState response
            response.setGamestate(new Klaverjas(klaverjas, getAllNames(), Integer.parseInt(players.get(dstSocket)[1])));

            //we package the reponse back into json, and send it to the client!
            Gson gson2 = new Gson();
            String payload = gson2.toJson(response);

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
