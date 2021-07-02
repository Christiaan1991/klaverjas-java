import React, {useState} from "react";
import type { GameState } from "../gameState";
import socket from "../SocketProvider"
import "./Play.css";
import "./Cards.css";
import "./PickTrump.css";

type PlayProps = {
    gameState: GameState;
    setGameState(newGameState: GameState): void;
}
type CardDisplayProps = {suit: number, rank: number};

export function Play({ gameState, setGameState }: PlayProps) {
	
	const [Cardrank, setCardRank] = useState();
    const [Cardsuit, setCardSuit] = useState();
	const [errorMessage, setErrorMessage] = useState("");

    console.log(gameState);
    return (
        <body>
            <Board/>

            <ScoreTable/>

            <div className="trump">{getTrump()}</div>

            <div className="status">{getStatus()}, It is your turn!</div>

            <p className="errorMessage"><b>{errorMessage}</b></p>
        </body>


    )

    function Board(){
        return  <div className="board">
                     <div className="p1">
                         <div>{gameState.players[gameState.ws_id % 4].name}</div>
                         <div>{gameState.players[gameState.ws_id % 4].cards.reverse().map((card) => 
                             <DisplayCard suit={card.suit} rank={card.rank}/>)}
                         </div>
                     </div>
                     <div className="p2">
                         <div>{gameState.players[(gameState.ws_id+1) % 4].name}</div>
                         {gameState.players[(gameState.ws_id+1) % 4].cards.reverse().map((card) => 
                         <p><BackCardSide /></p>)}
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
                    <div className="p5">
                        <div>{gameState.players[(gameState.ws_id) % 4].playedCard == null ? <PlayedEmptyCard/> : <PlayedCard suit={gameState.players[(gameState.ws_id) % 4].playedCard.suit} rank={gameState.players[(gameState.ws_id) % 4].playedCard.rank} />}</div>
                    </div>
                    <div className="p6">
                        <div>{gameState.players[(gameState.ws_id+1) % 4].playedCard == null ? <PlayedEmptyCardSide/> : <PlayedCardSide suit={gameState.players[(gameState.ws_id+1) % 4].playedCard.suit} rank={gameState.players[(gameState.ws_id+1) % 4].playedCard.rank} />}</div>
                    </div>
                    <div className="p7">
                        <div>{gameState.players[(gameState.ws_id+2) % 4].playedCard == null ? <PlayedEmptyCard/> : <PlayedCard suit={gameState.players[(gameState.ws_id+2) % 4].playedCard.suit} rank={gameState.players[(gameState.ws_id+2) % 4].playedCard.rank} />}</div>
                    </div>
                    <div className="p8">
                        <div>{gameState.players[(gameState.ws_id+3) % 4].playedCard == null ? <PlayedEmptyCardSide/> : <PlayedCardSide suit={gameState.players[(gameState.ws_id+3) % 4].playedCard.suit} rank={gameState.players[(gameState.ws_id+3) % 4].playedCard.rank} />}</div>
                    </div>
{/*                    <div className="p9">
                        <button className="block" >{getTrump()}</button>
                    </div>*/}
                </div>

    }

    function ScoreTable(){
        return  <table className="scoretable">
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
    }

    function DisplayCard({suit, rank} : CardDisplayProps){
        return <button className={"Card" + rank + suit + " Display"} onClick={(e) => pickCard(e, rank, suit)}></button>
    }

    function BackCard(){
        return <button className="backCard"></button>
    }

    function BackCardSide(){
        return <button className="backCard Side"></button>
    }

    function PlayedCard({suit, rank} : CardDisplayProps){
        return <button className={"Card" + rank + suit}></button>
    }

    function PlayedCardSide({suit, rank} : CardDisplayProps){
        return <button className={"Card" + rank + suit + " Side"}></button>
    }

    function PlayedEmptyCard(){
        return <button className={"EmptyCard"}></button>
    }

    function PlayedEmptyCardSide(){
        return <button className={"EmptyCardSide"}></button>
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

    function getTrump(){

        var trumpstring;
        if(gameState.picked_trump == 100){
            trumpstring = "no trump picked!";
        }
        else if(gameState.picked_trump == 0){
            trumpstring = "\u2662";
        }
        else if(gameState.picked_trump == 1){
            trumpstring = "\u2663";
        }
        else if(gameState.picked_trump == 2){
            trumpstring = "\u2664";
        }
        else if(gameState.picked_trump == 3){
            trumpstring = "\u2665";
        }
        
        return trumpstring;
    }

    function getPlayedCard(player: any){
        return gameState.players[player].playedCard;

    }

    async function pickCard(e: React.FormEvent, rank: any, suit: any) {
        e.preventDefault(); 

        setErrorMessage("");
        try {
            const payLoad = {
                "method": "play",
                "rank": rank,
                "suit": suit,
            }

            socket.send(JSON.stringify(payLoad));
            socket.onmessage = message =>{
                const response = JSON.parse(message.data);

                
                const gameState = response.gamestate;
                setGameState(gameState);
            };
        } catch (error) {
            setErrorMessage(error.toString());
        }

    }
}
     
            

