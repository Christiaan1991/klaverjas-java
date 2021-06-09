import React, {useState} from "react";
import type { GameState } from "../gameState";
import socket from "../SocketProvider";
import "./PickTrump.css";

type PlayProps = {
    gameState: GameState;
    setGameState(newGameState: GameState): void;
}




export function PickTrump({ gameState, setGameState }: PlayProps) {

    const [errorMessage, setErrorMessage] = useState("");
    console.log(name);
	
	async function pickTrump(e: React.FormEvent, picked_trump) {
        e.preventDefault(); 

        try {
            const payLoad = {
                "method": "pick",
                "trump": picked_trump,
            }

            socket.send(JSON.stringify(payLoad));
            socket.onmessage = message =>{
                const response = JSON.parse(message.data);

                console.log(response.name + " has picked " + response.gamestate.picked_trump);
                const gameState = response.gamestate;
                setGameState(gameState);
                console.log(gameState);
            };
        } catch (error) {
            console.error(error.toString());
        }

    }

    if(gameState.players[0].hasTurn){
    	status = gameState.players[0].name;
    }
    else if(gameState.players[1].hasTurn){
    	status = gameState.players[1].name;
    }
    else if(gameState.players[2].hasTurn){
        status = gameState.players[2].name;
    }
    else if(gameState.players[3].hasTurn){
        status = gameState.players[3].name;
    }

    return (
        <body>
            <div>
                <table className="scoretable">
                    <tr>
                        <th>{gameState.players[0].name} and {gameState.players[2].name}</th>
                        <th>{gameState.players[1].name} and {gameState.players[3].name}</th>
                    </tr>
                     <tr>
                        <td>{gameState.players[0].score + gameState.players[2].score}</td>
                        <td>{gameState.players[1].score + gameState.players[3].score}</td>
                    </tr>
                    <tr>
                        <td>{gameState.team1score}</td>
                        <td>{gameState.team2score}</td>
                    </tr>
                </table>
                
                <div className="status">{status}, please select a Trump!</div>

                <p className="errorMessage"><b>{errorMessage}</b></p>

                <div className="grid-container">
                    <div className="p1">
                        <div>{gameState.players[gameState.ws_id % 4].name}</div>
                        <table className="player1">
                            <tbody>
                            <tr>{gameState.players[gameState.ws_id % 4].cards.reverse().map((card) => 
                            <td><button className="cards"> {card.name} </button></td>)}
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div className="p2">
                        <div>{gameState.players[(gameState.ws_id+1) % 4].name}</div>
                         <table className="player2">
                            <tbody>
                            <td>{gameState.players[(gameState.ws_id+1) % 4].cards.reverse().map((card) => 
                            <tr><button className="cards-side"> {card.name} </button></tr>)}
                            </td>
                            </tbody>
                        </table>
                    </div>
                    <div className="p3">
                        <div>{gameState.players[(gameState.ws_id+2) % 4].name}</div>
                         <table className="player3">
                            <tbody>
                            <tr>{gameState.players[(gameState.ws_id+2) % 4].cards.reverse().map((card) => 
                            <td><button className="cards"> {card.name} </button></td>)}
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div className="p4">
                        <div>{gameState.players[(gameState.ws_id+3) % 4].name}</div>
                         <table className="player4">
                            <tbody>
                            <td>{gameState.players[(gameState.ws_id+3) % 4].cards.reverse().map((card) => 
                            <tr><button className="cards-side"> {card.name} </button></tr>)}
                            </td>
                            </tbody>
                        </table>
                    </div>
                <div className="p9">
                    <button className = "block" onClick={(e) => pickTrump(e, 0)}>{"\u2666"}</button>
                    <button className = "block" onClick={(e) => pickTrump(e, 1)}>{"\u2663"}</button>
                    <button className = "block" onClick={(e) => pickTrump(e, 2)}>{"\u2660"}</button>
                    <button className = "block" onClick={(e) => pickTrump(e, 3)}>{"\u2665"}</button>
                </div>  

                </div>   

  
            </div>
        </body>
    )
}

