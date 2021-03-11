package edu.kit.ipd.parse.actionRecognizer;

import java.util.List;
import java.util.Set;

import edu.kit.ipd.parse.luna.graph.IArc;
import edu.kit.ipd.parse.luna.graph.IArcType;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.graph.INodeType;

public class AdapterGraph implements IGraph {

	protected IGraph graph;

	public AdapterGraph(IGraph graph) {
		this.graph = graph;
	}

	@Override
	public IGraph clone() {
		return graph.clone();
	}

	@Override
	public IArc createArc(INode src, INode tar, IArcType type) {
		return graph.createArc(src, tar, type);
	}

	@Override
	public IArcType createArcType(String name) {
		return graph.createArcType(name);
	}

	@Override
	public INode createNode(INodeType type) {
		return graph.createNode(type);
	}

	@Override
	public INodeType createNodeType(String name) {
		return graph.createNodeType(name);
	}

	@Override
	public void deleteArc(IArc arc) {
		graph.deleteArc(arc);
	}

	@Override
	public void deleteNode(INode node) {
		graph.deleteNode(node);
	}

	@Override
	public IArcType getArcType(String name) {
		return graph.getArcType(name);
	}

	@Override
	public Set<IArcType> getArcTypes() {
		return graph.getArcTypes();
	}

	@Override
	public List<IArc> getArcs() {
		return graph.getArcs();
	}

	@Override
	public List<IArc> getArcsOfType(IArcType type) {
		return graph.getArcsOfType(type);
	}

	@Override
	public INodeType getNodeType(String name) {
		return graph.getNodeType(name);
	}

	@Override
	public Set<INodeType> getNodeTypes() {
		return graph.getNodeTypes();
	}

	@Override
	public List<INode> getNodes() {
		return graph.getNodes();
	}

	@Override
	public List<INode> getNodesOfType(INodeType type) {
		return graph.getNodesOfType(type);
	}

	@Override
	public void giveArcNewSource(IArc arc, INode src) {
		graph.giveArcNewSource(arc, src);
	}

	@Override
	public void giveArcNewTarget(IArc arc, INode tar) {
		graph.giveArcNewTarget(arc, tar);
	}

	@Override
	public boolean hasArcType(String name) {
		return graph.hasArcType(name);
	}

	@Override
	public boolean hasCanged() {
		return graph.hasCanged();
	}

	@Override
	public long getLastChangedMillis() {
		return graph.getLastChangedMillis();
	}

	@Override
	public boolean hasNodeType(String name) {
		return graph.hasNodeType(name);
	}

	@Override
	public void removeUnusesedNodes() {
		graph.removeUnusesedNodes();
	}

	@Override
	public String showGraph() {
		return graph.showGraph();
	}

	@Override
	public void replaceNode(INode oldNode, INode newNode, boolean deleteArcs) {
		graph.replaceNode(oldNode, newNode, deleteArcs);
	}

	@Override
	public long getChangeCounter() {
		return graph.getChangeCounter();
	}

	@Override
	public void increaseChangeCounter() {
		graph.increaseChangeCounter();

	}

}
