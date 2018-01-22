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
		return this.graph.clone();
	}

	@Override
	public IArc createArc(INode src, INode tar, IArcType type) {
		return this.graph.createArc(src, tar, type);
	}

	@Override
	public IArcType createArcType(String name) {
		return this.graph.createArcType(name);
	}

	@Override
	public INode createNode(INodeType type) {
		return this.graph.createNode(type);
	}

	@Override
	public INodeType createNodeType(String name) {
		return this.graph.createNodeType(name);
	}

	@Override
	public void deleteArc(IArc arc) {
		this.graph.deleteArc(arc);
	}

	@Override
	public void deleteNode(INode node) {
		this.graph.deleteNode(node);
	}

	@Override
	public IArcType getArcType(String name) {
		return this.graph.getArcType(name);
	}

	@Override
	public Set<IArcType> getArcTypes() {
		return this.graph.getArcTypes();
	}

	@Override
	public List<IArc> getArcs() {
		return this.graph.getArcs();
	}

	@Override
	public List<IArc> getArcsOfType(IArcType type) {
		return this.graph.getArcsOfType(type);
	}

	@Override
	public INodeType getNodeType(String name) {
		return this.graph.getNodeType(name);
	}

	@Override
	public Set<INodeType> getNodeTypes() {
		return this.graph.getNodeTypes();
	}

	@Override
	public List<INode> getNodes() {
		return this.graph.getNodes();
	}

	@Override
	public List<INode> getNodesOfType(INodeType type) {
		return this.graph.getNodesOfType(type);
	}

	@Override
	public void giveArcNewSource(IArc arc, INode src) {
		this.graph.giveArcNewSource(arc, src);
	}

	@Override
	public void giveArcNewTarget(IArc arc, INode tar) {
		this.graph.giveArcNewTarget(arc, tar);
	}

	@Override
	public boolean hasArcType(String name) {
		return this.graph.hasArcType(name);
	}

	@Override
	public boolean hasCanged() {
		return this.graph.hasCanged();
	}

	@Override
	public boolean hasNodeType(String name) {
		return this.graph.hasNodeType(name);
	}

	@Override
	public void removeUnusesedNodes() {
		this.graph.removeUnusesedNodes();
	}

	@Override
	public String showGraph() {
		return this.graph.showGraph();
	}

	@Override
	public void replaceNode(INode oldNode, INode newNode, boolean deleteArcs) {
		this.graph.replaceNode(oldNode, newNode, deleteArcs);
	}

	@Override
	public long getChangeCounter() {
		return this.graph.getChangeCounter();
	}

	@Override
	public void increaseChangeCounter() {
		this.graph.increaseChangeCounter();

	}

}
