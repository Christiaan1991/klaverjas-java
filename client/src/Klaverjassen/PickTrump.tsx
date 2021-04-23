import React, {useState} from "react";
import type { GameState } from "../gameState";
import "./PickTrump.css";

type PlayProps = {
    gameState: GameState;
    setGameState(newGameState: GameState): void;
}

export function PickTrump({ gameState, setGameState }: PlayProps) {

    const [errorMessage, setErrorMessage] = useState("");
	
	async function pickTrump(e: React.FormEvent, picked_trump) {
        e.preventDefault(); 

        if(picked_trump == null) {//check if picked card is allows!
            setErrorMessage("Pick a trump!");
            return;
        }

        try {
            const response = await fetch('klaverjas/api/pick', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({suit: picked_trump})
            });

            if (response.ok) {
                const gameState = await response.json();
                setGameState(gameState);
                console.log(gameState);

            } else {
                console.error(response.statusText);
            }
        } catch (error) {
            console.error(error.toString());
        }

    }

    console.log(gameState)

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
                        <div>{gameState.players[0].name}</div>
                        <table className="player1">
                            <tbody>
                            <tr>{gameState.players[0].cards.reverse().map((card) => 
                            <td><button className="cards"> {card.name} </button></td>)}
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div className="p2">
                        <div>{gameState.players[1].name}</div>
                         <table className="player2">
                            <tbody>
                            <td>{gameState.players[1].cards.reverse().map((card) => 
                            <tr><button className="cards-side"> {card.name} </button></tr>)}
                            </td>
                            </tbody>
                        </table>
                    </div>
                    <div className="p3">
                        <div>{gameState.players[2].name}</div>
                         <table className="player3">
                            <tbody>
                            <tr>{gameState.players[2].cards.reverse().map((card) => 
                            <td><button className="cards"> {card.name} </button></td>)}
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div className="p4">
                        <div>{gameState.players[3].name}</div>
                         <table className="player4">
                            <tbody>
                            <td>{gameState.players[3].cards.reverse().map((card) => 
                            <tr><button className="cards-side"> {card.name} </button></tr>)}
                            </td>
                            </tbody>
                        </table>
                    </div>

                    <div className="p5">
                        <div>{gameState.players[0].playedCard == null ? <button className="cards" >{"no played card"}</button> : <button className="cards" >{gameState.players[0].playedCard.name}</button>}</div>
                    </div>
                    <div className="p6">
                        <div>{gameState.players[1].playedCard == null ? <button className="cards-side" >{"no played card"}</button> : <button className="cards" >{gameState.players[1].playedCard.name}</button>}</div>
                    </div>
                    <div className="p7">
                        <div>{gameState.players[2].playedCard == null ? <button className="cards" >{"no played card"}</button> : <button className="cards" >{gameState.players[2].playedCard.name}</button>}</div>
                    </div>
                    <div className="p8">
                        <div>{gameState.players[3].playedCard == null ? <button className="cards-side" >{"no played card"}</button> : <button className="cards" >{gameState.players[3].playedCard.name}</button>}</div>
                    </div>
                </div>   

                <div className="picktrumpbuttons">
                    <button className = "diamonds" onClick={(e) => pickTrump(e, 0)}>{"diamonds \u2666"}</button>
                    <button className = "clubs" onClick={(e) => pickTrump(e, 1)}>{"clubs \u2663"}</button>
                    <button className = "spades" onClick={(e) => pickTrump(e, 2)}>{"spades \u2660"}</button>
                    <button className = "hearts" onClick={(e) => pickTrump(e, 3)}>{"hearts \u2665"}</button>
                </div>    
            </div>
        </body>
    )
}

