
export interface GameState {
    players: [ Player, Player, Player, Player ]; // a player array contains exactly four Players
    gameStatus: {
        endOfGame: boolean;
    };
    team1score: number;
    team2score: number;
    correctMove: boolean;
    pickedTrump: number;
}

interface Player {
    name: string;
    team: "team1" | "team2"; // player can be in team1 or in team 2
    type: "player1" | "player2" | "player3" | "player4"; //player can be player1, player2, player3 or player4
    hasTurn: boolean;
    hasSlagTurn: boolean;
    hasRoundTurn: boolean;
    score: number;
    cards: Card[];
    playedCard: Card;
}

interface Card {
    rank: number;
    suit: number;
    name: string;
    trump: boolean;
}
