import React, {useState} from "react";
import type { GameState } from "../gameState";
import socket from "../SocketProvider";
import "./PickTrump.css";
import "./Cards.css";

type PlayProps = {
    gameState: GameState;
    setGameState(newGameState: GameState): void;
}

type GameProps = {gameState: GameState;}
type CardDisplayProps = {suit: number, rank: number};


export function PickTrump({ gameState, setGameState }: PlayProps) {

    const [errorMessage, setErrorMessage] = useState("");

    return (
        <body>
            <Board/>

            <ScoreTable/>

            <div className="status">{getStatus()}, please select a Trump!</div>

            <p className="errorMessage"><b>{errorMessage}</b></p>
        </body>
    )

    function Board(){
        return  <div className="board">
                     <div className="p1">
                         <div>{gameState.players[gameState.ws_id % 4].name}</div>
                         <div>{gameState.players[gameState.ws_id % 4].cards.reverse().map((card) => 
                             <DisplayCard suit={card.suit} rank={card.rank} />)}
                         </div>
                     </div>
                     <div className="p2">
                         <div>{gameState.players[(gameState.ws_id+1) % 4].name}</div>
                         {gameState.players[(gameState.ws_id+1) % 4].cards.reverse().map((card) => 
                         <p><BackCardSide/></p>)}
                     </div>
                     <div className="p3">
                         <div>{gameState.players[(gameState.ws_id+2) % 4].name}</div>
                         <div>{gameState.players[(gameState.ws_id+2) % 4].cards.reverse().map((card) => 
                             <BackCard/>)}
                         </div>                    
                     </div>
                     <div className="p4">
                         <div>{gameState.players[(gameState.ws_id+3) % 4].name}</div>
                         {gameState.players[(gameState.ws_id+3) % 4].cards.reverse().map((card) => 
                         <p><BackCardSide/></p>)}                           
                     </div>
                     <div className="p9">
                         <button className = "block" onClick={(e) => pickTrump(e, 0)}>{"\u2666"}</button>
                         <button className = "block" onClick={(e) => pickTrump(e, 1)}>{"\u2663"}</button>
                         <button className = "block" onClick={(e) => pickTrump(e, 2)}>{"\u2660"}</button>
                         <button className = "block" onClick={(e) => pickTrump(e, 3)}>{"\u2665"}</button>
                     </div>  
                </div>

    }

    function ScoreTable(){
        return  <table className="scoretable">
                    <tr>
                        <th>{gameState.players[0].name} and {gameState.players[2].name}</th>
                        <th>{gameState.players[1].name} and {gameState.players[3].name}</th>
                    </tr>
                     <tr>
                        {getTrumpScore(0,2) == 0 ? <td>{getScore(0,2)}</td> : <td>{getScore(0,2)} + {getTrumpScore(0,2)}</td>}
                        {getTrumpScore(1,3) == 0 ? <td>{getScore(1,3)}</td> : <td>{getScore(1,3)} + {getTrumpScore(1,3)}</td>}
                    </tr>
                    <tr>
                        <td>{gameState.team1score}</td>
                        <td>{gameState.team2score}</td>
                    </tr>
                </table>
    }

    function getScore(p1, p2){
        return gameState.players[p1].score + gameState.players[p2].score
    }

    function getTrumpScore(p1, p2){
        return gameState.players[p1].trumpscore + gameState.players[p2].trumpscore
    }

    function DisplayCard({suit, rank} : CardDisplayProps){
        return <button className={"Card" + rank + suit + " Display"}></button>
    }

    function BackCard(){
        return <button className="backCard"></button>
    }

    function BackCardSide(){
        return <button className="backCard Side"></button>
    }

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
            };
        } catch (error) {
            console.error(error.toString());
        }

    }

    function getStatus(){
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

        return status;
    }
}