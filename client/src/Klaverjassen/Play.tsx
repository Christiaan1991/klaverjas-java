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
    sortCards();

    return (
        <body>
            <Board/>

            <ScoreTable/>

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
                         <p>{gameState.players[(gameState.ws_id+2) % 4].cards.reverse().map((card) => 
                             <BackCard/>)}
                         </p>                    
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
                    <div className="p9">
                        <div className="trump">{getTrump()}</div>
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


    function getScore(p1: number, p2: number){
        return gameState.players[p1].score + gameState.players[p2].score
    }

    function getTrumpScore(p1: number, p2: number){
        return gameState.players[p1].trumpscore + gameState.players[p2].trumpscore
    }

    function sortCards(){
        let cards = gameState.players[gameState.ws_id % 4].cards;

        for(let i = 0; i < cards.length; i++){
            for(let j = i+1; j < cards.length; j++){
                if(cards[i].value < cards[j].value){
                    let temp = cards[i];
                    cards[i] = cards[j];
                    cards[j] = temp;
                }

                if(cards[i].suit < cards[j].suit){
                    let temp = cards[i];
                    cards[i] = cards[j];
                    cards[j] = temp;
                }
            }
        }
        
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
     
            

