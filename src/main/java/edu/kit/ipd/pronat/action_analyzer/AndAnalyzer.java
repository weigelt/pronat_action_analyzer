package edu.kit.ipd.pronat.action_analyzer;

import java.util.Properties;

import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.tools.ConfigManager;

public class AndAnalyzer {

	private ActionGraph actionGraph;
	private Action action;
	// The action that comes before this action.
	private Action priorAction;
	// The action that comes after this action.
	private Action laterAction;

	private Properties props = ConfigManager.getConfiguration(ActionAnalyzer.class);

	public AndAnalyzer(ActionGraph actionGraph, Action action, Action priorAction, Action laterAction) {
		this.actionGraph = actionGraph;
		this.action = action;
		this.priorAction = priorAction;
		this.laterAction = laterAction;

	}

	public void execute() {
		checkForSituation2();
		checkForSituation3();
	}

	/**
	 * if predicate exists, only the actor is missing, then the actor = actor in the
	 * prior action.
	 */
	private void checkForSituation2() {
		if (action.getParameterMap().get(Role.PREDICATE).size() != 0 &&
        action.getParameterMap().get(Role.ACTOR).size() == 0
				&& priorAction != null && priorAction.getParameterMap().get(Role.ACTOR).size() != 0) {
        action.getParameterMap().put(Role.ACTOR, priorAction.getParameterMap().get(Role.ACTOR));
		}
	}

	/**
	 * When there is no predicate in the current action. predicate = predicate in
	 * prior or later action, which is combined with an "and"
	 */
	private void checkForSituation3() {
		Action coherentInstruction = null;
		// when there is no predicate in the current action.
		if (action.getParameterMap().get(Role.PREDICATE).size() == 0) {
			// finds the action combined according to the position of "and"
			coherentInstruction = action.getTokens().getFirst().getAttributeValue("value").equals("and") ?
          priorAction
					: laterAction != null && laterAction.getTokens().getFirst().getAttributeValue("value").equals("and")
							? laterAction
							: null;
			if (coherentInstruction != null) {
				// predicate of current action = copy of the predicate from
				// the combined action
				for (INode pNode : coherentInstruction.getParameterMap().get(Role.PREDICATE)) {
					INode newPNode = actionGraph.createNode(actionGraph.getNodeType(props.getProperty("NEWPREDICATE_NODE_TYPE")));
					newPNode.setAttributeValue("value", pNode.getAttributeValue("value") + "'");
            action.addTokenToMap(newPNode, Role.PREDICATE);
            actionGraph.getTokens().add(newPNode);
				}
				// other parameters of the combined action are also
				// assigned to this action
				for (Role role : action.getParameterMap().keySet()) {
					if (action.getParameterMap().get(role).size() == 0
							&& coherentInstruction.getParameterMap().get(role).size() != 0) {
              action.getParameterMap().put(role, coherentInstruction.getParameterMap().get(role));
					}
				}

			}

		}
	}

}
