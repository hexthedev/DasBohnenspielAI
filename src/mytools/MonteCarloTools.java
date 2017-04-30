package student_player.mytools;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import bohnenspiel.BohnenspielBoardState;
import bohnenspiel.BohnenspielMove;

public class MonteCarloTools {

	private static Random rand = new Random(); 
	
	//Selects a node for expansion
	 public static MonteCarloNode MCselect(MonteCarloNode node, double explorationFactor){
			
			//We are searching for a leaf node, so this recursively searches tree until leaf is found
	    	if(node.isLeaf()){
	    		return node;
	    	}
	    	
	    	//Searches for the best node to search for. Then recursively searchs the new node.
	    	double bestToExplore = -1;
	    	MonteCarloNode winningNode = null;

	    	for(MonteCarloNode lNode : node.getChildren()){
	    		   		
	    		double policyValue = lNode.policyValue(explorationFactor); 
	    		
	    		if(policyValue > bestToExplore){
	    			bestToExplore = policyValue;
	    			winningNode = lNode;
	    		}
	    	}

	    	return MCselect(winningNode, explorationFactor);
	    }
	    
	    //Expands a node and simulates games of all children
	    public static void MCexpansion(MonteCarloNode node, BohnenspielBoardState board, int playerId, int simulationsPerExpansion){
	    	
	    	//Initialize path to test node
	    	Stack<BohnenspielMove> pathToNode = new Stack<BohnenspielMove>();
	    	node.pathToNode(pathToNode);
	    	
	    	//Clone Game board for simulation which will be starting at the root, then move to the test node
	    	BohnenspielBoardState newBoard = (BohnenspielBoardState) board.clone();
	    	
	    	while(!pathToNode.isEmpty()){
	    		newBoard.move(pathToNode.pop());
	    	}
	    	
	    	//Get the legal moves from this point
	    	ArrayList<BohnenspielMove> moves = newBoard.getLegalMoves();
	    	
	    	//For each legal move, add that move as a child to the node we're simulating from
	    	for(BohnenspielMove move : moves){
	    		node.addChild(move);
	    	}
	    	
	    	//For each child of the simulating node, run a simulationsPerExpansion amount of simulations
	    	for(MonteCarloNode lNode : node.getChildren()){
	    		
	    		// Make a cloned board and move to appropriate node
	    		BohnenspielBoardState toPlay = (BohnenspielBoardState) newBoard.clone();
	    		toPlay.move(lNode.getMove());		
	    		
	    		int wins = 0;
	    		
	    		for(int i = 0; i< simulationsPerExpansion; i++){
	    			//Simulate a game
	    			int winningPlayer =MCsimulate((BohnenspielBoardState)toPlay.clone());
		    		
		    		//Determine the winner
	    			if(playerId == winningPlayer){
	    				wins++;
	    			}
	    		}
	    		
	    		// Update nodes in tree with new win statistics
	    		MonteCarloTools.MCbackpropagate(lNode, wins, simulationsPerExpansion);
	    	}
    		
	    }
	    
	    //Simulates games through random moves
	    public static int MCsimulate(BohnenspielBoardState board){
	    
	    	while(!(board.gameOver())){         
	    		ArrayList<BohnenspielMove> moves = board.getLegalMoves();
	            board.move(moves.get(rand.nextInt(moves.size())));
	    	}
	    	  	
	    	return board.getWinner();	
	    }
	    
	    //Backpropagates results of moves
	    public static void MCbackpropagate(MonteCarloNode node, int wins, int plays){
	    	node.update(wins, plays);
	    }
	
}
