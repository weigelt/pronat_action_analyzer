package edu.kit.ipd.pronat.action_analyzer;

/**
 * Used for SimpleActionGraph. Represents an arc of type "betweenToken", which
 * is used by ActionGraph. Only contains the source node, the target node and
 * the type
 * 
 * @author ouyue
 *
 */
public class SimpleActionArc {

	// Using String representation due to simplicity of sample solution
	// creation. In sample solution, the arcs only have to specify the name of
	// the nodes.
	private SimpleActionNode sourceNode;
	private SimpleActionNode targetNode;
	private BetweenRole type;

	public SimpleActionArc() {

	}

	public SimpleActionArc(SimpleActionNode sourceNode, SimpleActionNode targetNode, BetweenRole type) {
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.type = type;
	}

	@Override
	public boolean equals(Object other) {
		boolean b1 = ((SimpleActionArc) other).getSourceNode().getPosition() == sourceNode.getPosition();
		boolean b2 = ((SimpleActionArc) other).getTargetNode().getPosition() == targetNode.getPosition();
		boolean b3 = ((SimpleActionArc) other).type == type;

		return b1 && b2 && b3;

	}

	public SimpleActionNode getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(SimpleActionNode sourceNode) {
		this.sourceNode = sourceNode;
	}

	public SimpleActionNode getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(SimpleActionNode targetNode) {
		this.targetNode = targetNode;
	}

	public BetweenRole getType() {
		return type;
	}

	public void setType(BetweenRole type) {
		this.type = type;
	}

	@Override public String toString() {
		return sourceNode.getPosition() + "." + sourceNode.getValue() + "--" + type + "-->" + targetNode
        .getPosition()
				+ "." + targetNode.getValue();
	}
}
