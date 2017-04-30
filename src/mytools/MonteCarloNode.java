package student_player.mytools;

import java.util.ArrayList;
import java.util.Stack;

import bohnenspiel.BohnenspielMove;

public class MonteCarloNode {

	//The move taken to get to this node
	private BohnenspielMove move;
	
	//The times this move was taken, and the times this move won when taken
	private int timesWon;
	private int timesTaken;
	
	//Holds the parent node and all children of this node
	private MonteCarloNode parent;
	private ArrayList<MonteCarloNode> children;
	
	//Is this a player move or an opponent move
	private NodeType nodeType;

	
	//CONSTRUCTOR
	public MonteCarloNode(MonteCarloNode pParent, BohnenspielMove pMove){
		timesWon = 0;
		timesTaken = 0;
		
		//This piece of code forces root nodes to initialize as MIN nodes, so that their children (moves taken by the player) are MAX nodes.
		//MAX nodes mesure effectiveness by maximizing wins, while MIN nodes want to minimize wins
		if(pParent == null){
			nodeType = NodeType.MIN;
		} else {
			parent = pParent;
			nodeType = NodeType.getOpposite(parent.nodeType);
		}
				
		children = new ArrayList<MonteCarloNode>();
		move = pMove;
	}
	
	
	
	//AI METHODS
	
	//Q(s,a) = value. This is the value of a node based on wins vs total games. 
	public double value(){
		if(nodeType == NodeType.MAX){
			return ((double)timesWon)/((double)timesTaken);
		} else if (nodeType == NodeType.MIN){
			return ((double)timesTaken - timesWon)/((double)timesTaken);
		} else {
			System.out.println("-----SHOULD NEVER HAPPEN. NODE IS NEITHER MIN OR MAX-----");
			return -1;
		}
	}
	
	//Q(s,a) + c sqrt(log(n(s)/n(s,a))) (Different for min nodes)
	//The above equation is a classic Monte Carlo equation that balances exploration and exploitation
	public double policyValue(double c){
		return value() + c * Math.sqrt(Math.log(parent.getTimesTaken())/timesTaken);
	}
	
	//Puts path to node inside pathholder
	public void pathToNode(Stack<BohnenspielMove> pathHolder){
		if(!isRoot()){
			pathHolder.push(move);
			parent.pathToNode(pathHolder);
		}
	}
	
	//Adds a child to this node
	public void addChild(BohnenspielMove move){
		children.add(new MonteCarloNode(this, move));
	}
	
	//updates the current node with new wins vs. plays information then updates the parent.
	public void update(int wins, int plays){
		timesWon += wins;
		timesTaken += plays;
		
		if(!isRoot()){
			parent.update(wins, plays);
		}
	}
	
	//GETTER METHODS
	public int getTimesTaken(){
		return timesTaken;
	}
	
	public NodeType getNodeType(){
		return nodeType;
	}
	
	public ArrayList<MonteCarloNode> getChildren(){
		return children;
	}
	
	public BohnenspielMove getMove(){
		return move;
	}
		
	
	//QUERY METHODS
	public boolean isRoot(){
		return parent == null;
	}
	
	public boolean isLeaf(){
		return children.size() == 0;
	}	
	
}
