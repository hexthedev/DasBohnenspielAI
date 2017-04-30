package student_player.mytools;

public enum NodeType {
	MAX, MIN;
	
	//If MAX returns MIN, and vice versa
	public static NodeType getOpposite(NodeType type){
		if(type == NodeType.MAX){
			return NodeType.MIN;
		} else if(type == NodeType.MIN) {
			return NodeType.MAX;
		} else {
			return null;
		}
	}
}
