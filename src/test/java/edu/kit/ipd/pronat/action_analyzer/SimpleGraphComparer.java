package edu.kit.ipd.pronat.action_analyzer;

/**
 * compares two SimpleActionGraphs
 * 
 * @author ouyue
 *
 */
public class SimpleGraphComparer {

	private SimpleActionGraph graph;
	private SimpleActionGraph sampleGraph;

	/**
	 * 
	 * @param graph
	 * @param sampleGraph
	 */
	public SimpleGraphComparer(SimpleActionGraph graph, SimpleActionGraph sampleGraph) {
		this.graph = graph;
		this.sampleGraph = sampleGraph;
	}

	public double roleAccuracy() {
		int counter = 0;
		// graph1 and graph2 should have the same number of nodes
		int nodeNr = Math.min(graph.getNodes().size(), sampleGraph.getNodes().size());
		for (int i = 0; i < nodeNr; i++) {
			if (graph.getNodes().get(i).getRole() == sampleGraph.getNodes().get(i).getRole()) {
				counter++;
			}
		}
		return (double) counter / (double) Math.max(graph.getNodes().size(), sampleGraph.getNodes().size());
	}

	public double arcPrecision() {
		return (double) truePositivesArcCount() / (double) graph.getArcs().size();
	}

	public double arcRecall() {
		return (double) truePositivesArcCount() / (double) sampleGraph.getArcs().size();
	}

	private int truePositivesArcCount() {
		int counter = 0;
		for (SimpleActionArc arc : graph.getArcs()) {
			if (sampleGraph.containsArc(arc)) {
				counter++;
			}
		}
		return counter;
	}
}
