package student_player;

import bohnenspiel.BohnenspielBoardState;
import bohnenspiel.BohnenspielMove;
import bohnenspiel.BohnenspielPlayer;
import student_player.mytools.MonteCarloTree;

//The above import are attached to an implementation of the Das Bohnenspiel game written by a T.A. which I do not have permission to release
public class StudentPlayer extends BohnenspielPlayer {

	public StudentPlayer() { super("von Schlumein Eater das Happinoos"); }

    public BohnenspielMove chooseMove(BohnenspielBoardState board_state)
    {
       	//Initialize MonteCarloTree object which controls the logic of a Monte Carlo Tree search for one move choice
    	MonteCarloTree tree = new MonteCarloTree((BohnenspielBoardState) board_state.clone(), player_id, 1);
       	
    	//Complete tree for 685 milliseconds, Exploration constant = 1.05, Spread per Expansion = 80
        //Spread per expansion: each iteration of this modified Monte Carlo Search exapnds every child node of the parent, and simulated that now 80 times. 
    	tree.complete(685, 1.05, 80);
    	
    	//Return Move
    	return tree.getBestMove();
    }
}