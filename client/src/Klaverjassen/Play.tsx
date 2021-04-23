import React, {useState} from "react";
import type { GameState } from "../gameState";
import "./Play.css";

type PlayProps = {
    gameState: GameState;
    setGameState(newGameState: GameState): void;
}

export function Play({ gameState, setGameState }: PlayProps) {
	
	const [Cardrank, setCardRank] = useState();
    const [Cardsuit, setCardSuit] = useState();
	const [errorMessage, setErrorMessage] = useState("");

	async function pickCard(e: React.FormEvent, card: any, turn: any) {
        e.preventDefault(); 
        if(!turn) {//check if picked card is allows!
            setErrorMessage("Move not allowed, play your own Card!");
            return;
        }
        console.log(card);
        setErrorMessage("");

        try {
            const response = await fetch('klaverjas/api/move', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({rank: card.rank, suit: card.suit})
            });

            if (response.ok) {
                const gameState = await response.json();
                setGameState(gameState);

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

    var trumpstring;

    if(gameState.pickedTrump == 100){
        trumpstring = "no trump picked!";
    }
    else if(gameState.pickedTrump == 0){
        trumpstring = "\u2662";
    }
    else if(gameState.pickedTrump == 1){
        trumpstring = "\u2663";
    }
    else if(gameState.pickedTrump == 2){
        trumpstring = "\u2664";
    }
    else if(gameState.pickedTrump == 3){
        trumpstring = "\u2665";
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
                
                <div className="status">Turn: {status}</div>

                <p className="errorMessage"><b>{errorMessage}</b></p>

                <div className="grid-container">
                    <div className="p1">
                        <div>{gameState.players[0].name}</div>
                        <table className="player1">
                            <tbody>
                            <tr>{gameState.players[0].cards.reverse().map((card) => 
                            <td><button className="cards" onClick={(e) => pickCard(e, card, gameState.players[0].hasTurn)} > {card.name} </button></td>)}
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div className="p2">
                        <div>{gameState.players[1].name}</div>
                         <table className="player2">
                            <tbody>
                            <td>{gameState.players[1].cards.reverse().map((card) => 
                            <tr><button className="cards-side" onClick={(e) => pickCard(e, card, gameState.players[1].hasTurn)} > {card.name} </button></tr>)}
                            </td>
                            </tbody>
                        </table>
                    </div>
                    <div className="p3">
                        <div>{gameState.players[2].name}</div>
                         <table className="player3">
                            <tbody>
                            <tr>{gameState.players[2].cards.reverse().map((card) => 
                            <td><button className="cards" onClick={(e) => pickCard(e, card, gameState.players[2].hasTurn)} > {card.name} </button></td>)}
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div className="p4">
                        <div>{gameState.players[3].name}</div>
                         <table className="player4">
                            <tbody>
                            <td>{gameState.players[3].cards.reverse().map((card) => 
                            <tr><button className="cards-side" onClick={(e) => pickCard(e, card, gameState.players[3].hasTurn)} > {card.name} </button></tr>)}
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
                    <div className="p9">
                        <button className="block" >{trumpstring}</button>
                    </div>

                </div>       
            </div>
        </body>
    )
}

