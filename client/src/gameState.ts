
export interface GameState {
    players: [ Player, Player, Player, Player ]; // a player array contains exactly four Players
    gameStatus: {
        endOfGame: boolean;
    };
}

interface Player {
    name: string;
    cards: Card[];
    team: "team1" | "team2"; // player can be in team1 or in team 2
    type: "player1" | "player2" | "player3" | "player4"; //player can be player1, player2, player3 or player4
    hasTurn: boolean;
}

interface Card {
    name: string;
    index: number;
}
