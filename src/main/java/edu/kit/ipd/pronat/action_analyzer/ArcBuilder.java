package edu.kit.ipd.pronat.action_analyzer;

import java.util.LinkedList;
import java.util.Properties;

import edu.kit.ipd.parse.luna.graph.IArc;
import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.tools.ConfigManager;

/**
 * Adds arcs type of "betweenToken" betwenn tokens. The arcs have an attribute
 * "betweenRole", which can be between predicates (NEXT_ACTION), from predicates
 * to actors/ parameters (PREDICATE_TO), or inside an actor/ parameter
 * (INSIDE_CHUNK)
 *
 * @author ouyue
 *
 */
public class ArcBuilder {

	private ActionGraph actionGraph;

	private Properties props = ConfigManager.getConfiguration(ActionAnalyzer.class);

	public ArcBuilder(ActionGraph actionGraph) {
		this.actionGraph = actionGraph;
	}

	/**
	 * Should be called after roles are assigned to tokens, maps are created for
	 * instructions and instructions are assigned to predicates. Adds arcs betwenn
	 * tokens. The arcs are type of "betweenToken" The arcs have an attribute
	 * "betweenRole", which can be between predicates (NEXT_ACTION), from predicates
	 * to actors/ parameters (PREDICATE_TO), or inside an actor/ parameter
	 * (INSIDE_CHUNK)
	 */
	public void execute() {
		LinkedList<Predicate> predicates = actionGraph.getPredicates();
		for (Predicate predicate : predicates) {
			// Add "INSIDE_CHUNK" Arcs within chunks and add
			// "PREDICATE_TO_PARA"
			// Arcs from predicate to parameters.

			for (LinkedList<INode> chunk : predicate.getAction().getParameterMap().values()) {
				if (chunk != null) {
					addInsideChunkArcs(chunk);
					if (!chunk.equals(predicate.getAction().getParameterMap().get(Role.PREDICATE))) {
						addPredicateToArcs(predicate, chunk);
					}
				}

			}
		}
		addNextActionArcs(predicates);

	}

	/**
	 * Adds "INSIDE_CHUNK" Arcs within a chunk.
	 * 
	 * @param chunk
	 *            a list of nodes containing a chunk. The chunk should play exactly
	 *            ONE role.
	 */
	private void addInsideChunkArcs(LinkedList<INode> chunk) {
		for (int i = 0; i < chunk.size() - 1; i++) {
			IArc arc = actionGraph.createArc(chunk.get(i), chunk.get(i + 1),
					actionGraph.getArcType(props.getProperty("BETWEENTOKEN_ARC_TYPE")));
			arc.setAttributeValue("type", BetweenRole.INSIDE_CHUNK);
		}
	}

	/**
	 * Adds "PREDICATE_TO_PARA" Arcs from a predicate to a parameter. A
	 * "PREDICATE_TO_PARA" arc starts at the beginning of the predicate and ends at
	 * the beginning of the parameter.
	 * 
	 * @param predicate
	 *            a predicate
	 * @param para
	 *            a parameter. actor / who / where / ... / how
	 */
	private void addPredicateToArcs(Predicate predicate, LinkedList<INode> para) {
		if (predicate.size() > 0 && para.size() > 0) {
			IArc arc = actionGraph.createArc(predicate.getTokens().getFirst(), para.getFirst(),
					actionGraph.getArcType(props.getProperty("BETWEENTOKEN_ARC_TYPE")));
			arc.setAttributeValue("type", BetweenRole.PREDICATE_TO_PARA);
		}
	}

	/**
	 * Adds "NEXT_ACTION" Arcs which combine predicates. A "NEXT_ACTION" arc starts
	 * at the beginning of the preceding predicate and ends at the beginning of the
	 * succeeding predicate.
	 * 
	 * @param predicates
	 *            a list of predicates contained in several actions.
	 */
	private void addNextActionArcs(LinkedList<Predicate> predicates) {
		for (int i = 0; i < predicates.size() - 1; i++) {
			if (predicates.get(i).getTokens().size() > 0 && predicates.get(i + 1).getTokens().size() > 0) {
				IArc arc = actionGraph.createArc(predicates.get(i).getTokens().getFirst(), predicates.get(i + 1).getTokens().getFirst(),
						actionGraph.getArcType(props.getProperty("BETWEENTOKEN_ARC_TYPE")));
				arc.setAttributeValue("type", BetweenRole.NEXT_ACTION);
			}

		}
	}

}
