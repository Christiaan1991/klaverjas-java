import React, { useState } from "react";
import type { GameState } from "../gameState";
import { Klaverjassen } from "./Klaverjassen";
import socket, {userId} from "../SocketProvider";
import "./StartGame.css";

type StartGameProps = {
    setGameState(newGameState: GameState): void;
}


/**
 * Allows the player to enter their name. A name is required to start the game!
 */
export function StartGame({ setGameState }: StartGameProps) {

    const [player, setPlayer] = useState();
    const [errorMessage, setErrorMessage] = useState("");

    async function tryStartGame(e: React.FormEvent) {
        e.preventDefault(); // Prevent default browser behavior of submitting forms

        if (!player) {
            setErrorMessage("A name is required!");
            return;
        }

        setErrorMessage("");
        
        try {

            const payLoad = {
                "method": "join",
                "userId": userId,
                "name": player
            }

            socket.send(JSON.stringify(payLoad));

            socket.onmessage = message =>{
                const response = JSON.parse(message.data);

                console.log(response.name + " has joined the game!")
                console.log(response.NumberOfPlayers +" players are in game!");
                

                // if(response.NumberOfPlayers < 4){
                //     return ( 
                //         <body>
                //         <h1>Welcome to Christiaan's klaverjas application!</h1>
                //         <div>{"Waiting for other players..."}</div>
                //         </body>
                //     )
                // }

                if(response.NumberOfPlayers == 4){
                    console.log("We start the game!!")
                    const gameState = response.gamestate;
                    setGameState(gameState);
                    console.log(gameState);
                }
            };

        } catch (error) {
            console.log("response not ok!")
            console.error(error.toString());
        }

    }

    return (
        <body>
        <h1>Welcome to Christiaan's klaverjas application!</h1>
            <form onSubmit={(e) => tryStartGame(e)}>
                <input value={player}
                    placeholder="Player name"
                    onChange={(e) => setPlayer(e.target.value)}
                />

                <p className="errorMessage">{errorMessage}</p>

                <button className="startGameButton" type="submit">
                    Play Klaverjassen!
                </button>
            </form>
        </body>
    )
}