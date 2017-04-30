package student_player.mytools;

import java.util.Timer;

import bohnenspiel.BohnenspielBoardState;
import bohnenspiel.BohnenspielMove;

public class MonteCarloTree {

	//Tree requires a null object root node, Board state at time of decision and id of player (To test who won) 
	MonteCarloNode root;
	BohnenspielBoardState originalBoard;
	int playerId;
	
	public MonteCarloTree(BohnenspielBoardState pOriginalBoard, int pPlayerId, int explorationsPerExpansion){
		root = new MonteCarloNode(null, null);
		
		//Using the root node, the MonteCarlo expands the root node for the first iteration. This is required so that the complete method will work
		MonteCarloTools.MCexpansion(root, (BohnenspielBoardState) pOriginalBoard.clone(), pPlayerId, explorationsPerExpansion);
		
		originalBoard = pOriginalBoard;
		playerId = pPlayerId;
	}
	
	//Continually selects expands and simulates for the allotted time
	public void complete(int time, double explorationFactor, int explorationsPerExpansion){
		
		//Forces expansions to occur in specific time limit
		long start = System.currentTimeMillis();
		long end = start + time;
		
		//Until time limit is over, Select node ot expand then expand node
		while(System.currentTimeMillis() < end){
			MonteCarloNode selectedNode = MonteCarloTools.MCselect(root, explorationFactor);
			MonteCarloTools.MCexpansion(selectedNode, (BohnenspielBoardState) originalBoard.clone(), playerId, explorationsPerExpansion);
		}		
	}
	
	//Returns the best move according to simulations by searching direct children of root
	public BohnenspielMove getBestMove(){
	
		double bestSoFar = -1;
		BohnenspielMove bestMove = null;
		
		for(MonteCarloNode node : root.getChildren() ){
			
			double nodeValue = node.value();
			
			if(nodeValue > bestSoFar){
				bestSoFar = nodeValue;
				bestMove = node.getMove();
			}			
		}
		
		return bestMove;
	}
	
}
