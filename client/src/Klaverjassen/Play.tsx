import React, {useState} from "react";
import type { GameState } from "../gameState";
import "./Play.css";

type PlayProps = {
    gameState: GameState;
    setGameState(newGameState: GameState): void;
}

export function Play({ gameState, setGameState }: PlayProps) {
	
	const [cardnum, setCard] = useState("");
	const [errorMessage, setErrorMessage] = useState("");

	async function pickIndex(e: React.FormEvent, cardnum) {
        e.preventDefault();

        try {
            const response = await fetch('klaverjas/api/move', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({index: cardnum})
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
	
    console.log(gameState)

    return (
        <div>
            <p>{gameState.players[0].name} and {gameState.players[2].name} vs {gameState.players[1].name} and {gameState.players[3].name}</p>
            <div className="status">Turn: {status}</div>

            <p>{gameState.players[0].name}</p>
            <p>{gameState.players[0].cards.reverse().map((card) => 
                <button className="cards" onClick={(e) => pickIndex(e, card.index)} > {card.name} </button>)} </p>

            <p>{gameState.players[1].name}</p>
            <p>{gameState.players[1].cards.reverse().map((card) => 
                <button className="cardsplayerleft" onClick={(e) => pickIndex(e, card.index)} > {card.name} </button>)} </p>

            <p>{gameState.players[2].name}</p>
            <p>{gameState.players[2].cards.reverse().map((card) => 
                <button className="cardplayeropposite" onClick={(e) => pickIndex(e, card.index)} > {card.name} </button>)} </p>

            <p>{gameState.players[3].name}</p>
            <p>{gameState.players[3].cards.reverse().map((card) => 
                <button className="cardsplayerright" onClick={(e) => pickIndex(e, card.index)} > {card.name} </button>)} </p>


            
        </div>
    )
}