package klaverjas.domain;

public interface Klaverjas {
	public static final int NO_TEAM = 5;
	public static final int TEAM_ONE = 6;
	public static final int TEAM_TWO = 7;
	public static final int PLAYER_ONE = 0;
	public static final int PLAYER_TWO = 1;
	public static final int PLAYER_THREE = 2;
	public static final int PLAYER_FOUR = 3;

	/**
	 * Method indicating if the first player has the next turn or not.
	 * If player 1 is not in turn, then player 2 is in turn.
     * @param The player which you want to know the turn for.
	 * @return True if the first player has the next turn, false if it's the turn of the other player.
	 */
	boolean isPlayersTurn(int player);

	/**
	 * Method for retrieving whether the game has ended or not.
	 * 
	 * @return True is the game has ended otherwise False.
	 */
	boolean isPlayersSlagTurn(int player);

	boolean isPlayersRoundTurn(int player);

	boolean isEndOfGame();

	/**
	 * Method for retrieving the player that has won the game.
	 * 
	 * @return Integer value representing which player(s) (if any) won the game.
	 */
	int getWinner();

	Player[] getPlayers();

	int getTeam1Score();

	int getTeam2Score();

	boolean getCorrectMove();

}