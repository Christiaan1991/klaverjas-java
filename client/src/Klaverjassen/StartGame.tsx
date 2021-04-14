import React, { useState } from "react";
import type { GameState } from "../gameState";
import "./StartGame.css";

type StartGameProps = {
    setGameState(newGameState: GameState): void;
}

/**
 * Allows the players to enter their name. A name is required for both players. They can't have the same names.
 */
export function StartGame({ setGameState }: StartGameProps) {

    const [errorMessage, setErrorMessage] = useState("");
    const [playerOne, setPlayerOne] = useState("");
    const [playerTwo, setPlayerTwo] = useState("");
    const [playerThree, setPlayerThree] = useState("");
    const [playerFour, setPlayerFour] = useState("");

    async function tryStartGame(e: React.FormEvent) {
        e.preventDefault(); // Prevent default browser behavior of submitting forms
        if (!playerOne) {
            setErrorMessage("A name is required for player 1");
            return;
        }
        if (!playerTwo) {
            setErrorMessage("A name is required for player 2");
            return;
        }
        if (!playerThree) {
            setErrorMessage("A name is required for player 3");
            return;
        }
        if (!playerFour) {
            setErrorMessage("A name is required for player 4");
            return;
        }
        if (playerOne === playerTwo) {
            setErrorMessage("Each player should have a unique name");
            return;
        }
        setErrorMessage("");
        
        try {
            const response = await fetch('/klaverjas/api/start', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ nameplayer1: playerOne, nameplayer2: playerTwo, nameplayer3: playerThree, nameplayer4: playerFour })
            });

            if (response.ok) {
                console.log("response ok!")
                const gameState = await response.json();
                setGameState(gameState);
            } else {
                console.log("response not ok!")
                console.error(response.statusText);
            }
        } catch (error) {
            console.log("response still not ok!")
            console.error(error.toString());
        }
    }

    return (
        <form onSubmit={(e) => tryStartGame(e)}>
            <input value={playerOne}
                placeholder="Player 1 name"
                onChange={(e) => setPlayerOne(e.target.value)}
            />

            <input value={playerTwo}
                placeholder="Player 2 name"
                onChange={(e) => setPlayerTwo(e.target.value)}
            />

            <input value={playerThree}
                placeholder="Player 3 name"
                onChange={(e) => setPlayerThree(e.target.value)}
            />

            <input value={playerFour}
                placeholder="Player 4 name"
                onChange={(e) => setPlayerFour(e.target.value)}
            />

            <p className="errorMessage">{errorMessage}</p>

            <button className="startGameButton" type="submit">
                Play Klaverjassen!
            </button>
        </form>
    )
}