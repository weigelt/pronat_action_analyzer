package edu.kit.ipd.parse.actionRecognizer;

import java.util.LinkedList;

import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.luna.agent.AbstractAgent;

@MetaInfServices(AbstractAgent.class)
public class ActionRecognizer extends AbstractAgent {

	private static final Logger logger = LoggerFactory.getLogger(ActionRecognizer.class);
	private ActionGraph actionGraph;

	public ActionRecognizer() {
		setId("actionRecognizer");
	}

	@Override
	public void init() {
	}

	@Override
	protected void exec() {
		if (!checkExecutedbefore()) {
			System.out.println();
			logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Action Recognizer starts execution!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

			// actionGraph has the attribute role, new Predicate token type and new
			// arcs.
			actionGraph = new ActionGraph(graph);

			LinkedList<Action> actions = actionGraph.getActions();
			for (int i = 0; i < actions.size(); i++) {
				RoleIdentifier roleIdentifier = new RoleIdentifier(actionGraph, actions.get(i));
				roleIdentifier.execute();

				AndAnalyser andAnalyser = new AndAnalyser(actionGraph, actions.get(i), i == 0 ? null : actions.get(i - 1),
						i == actions.size() - 1 ? null : actions.get(i + 1));
				andAnalyser.execute();

				actionGraph.addPredicate(actions.get(i).createPredicate());
			}

			ArcBuilder arcBuilder = new ArcBuilder(actionGraph);
			arcBuilder.execute();

			// System.out.println();
			// System.out.println("Results:");
			// this.actionGraph.printTokensWithRole();
			// this.actionGraph.printAllBetweenTokenArcs();
			// for (Predicate p: this.actionGraph.getPredicates()) {
			// System.out.println(p);
			// }
			// for(Instruction i: this.actionGraph.getInstructions()) {
			// System.out.println(i);
			// }

			logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Action Recognizer ends execution!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println();
		}
	}

	private boolean checkExecutedbefore() {
		if (graph.getNodeType("token").containsAttribute("role", Role.class.getName())) {
			logger.info("Executed before, exiting!");
			return true;
		} else {
			return false;
		}
	}

	public ActionGraph getActionGraph() {
		return actionGraph;
	}

}
