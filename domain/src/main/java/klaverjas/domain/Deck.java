package mancala.domain;

/*
 Make your own mancala implementation using your design.
 You can take this stub as an example how to make a
 class inside a package and how to test it.
*/

public class Player {
	public static final int NONVALID = 2;
	public static final int WINNER = 3;

	private Bowl first_bowl;

	/* default constructor */
	public Player(int player_id){
		first_bowl = new Bowl(this, player_id,0, 4); //create first bowl with player_id, bowl_id 0 and 4 stones, and more bowls from there
	}

	//second constructor
	public Player(Player first_player, int player_id){
		first_bowl = new Bowl(first_player,player_id, 0, 4); //create first bowl with player_id, bowl_id 0 and 4 stones, and more bowls from there
	}

	public Bowl getFirstBowl() {return first_bowl;}

	public Kalaha getKalahaFirst(){return getFirstBowl().findBowl(5).getKalaha();}

	public Player getOpponent(){return getKalahaFirst().getNextPlayer();}

	public Bowl getFirstBowlOpponent(){return getKalahaFirst().getNextPlayer().getFirstBowl();}

	public boolean check(Bowl bowl){
		try{
			if(!bowl.hasStones()){/* if bowl has no stones */
				check(bowl.goNextBowl()); //check next bowl
			}
			else{//bowl has stones
				return true;
			}
		}
		catch(NullPointerException e){//all bowls are checked, no more exist
			return false;
		}
		return false; //will never reach
	}

	public int turn(int bowlNo, int hasTurn){
		//first check if all bowls of player are empty
		//something wrong here
//		if(!check(first_bowl)){
//			System.out.println("All bowls are empty!");
//			return WINNER;
//		}


		Bowl picked_bowl = first_bowl.findBowl(bowlNo); //find correct bowl

		//First check if move is allowed!
		if(picked_bowl.hasStones()){
			int out = picked_bowl.move(hasTurn);
			return out; //take stones on hand, empty bowl and distribute stones
		}
		return NONVALID;
	}
}