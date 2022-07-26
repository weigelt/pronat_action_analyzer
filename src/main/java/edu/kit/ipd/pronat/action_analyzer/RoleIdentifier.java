package edu.kit.ipd.pronat.action_analyzer;

import java.util.Iterator;

import edu.kit.ipd.parse.luna.graph.IArc;
import edu.kit.ipd.parse.luna.graph.INode;

/**
 * Identifies roles of tokens in ONE action. Evaluates the role for each token
 * and adds it to the attribute "role" of the token.
 *
 * @author ouyue
 *
 */
public class RoleIdentifier {

	private final ActionGraph actionGraph;
	private final Action action;
	//	private final Properties props = ConfigManager.getConfiguration(ActionAnalyzer.class);

	public RoleIdentifier(ActionGraph actionGraph, Action action) {
		this.actionGraph = actionGraph;
		this.action = action;
	}

	/**
	 * Sets the role attribute of the token if null
	 *
	 * @param token
	 *            token whose role is to be set
	 * @param role
	 *            the role of the token
	 */
	private static void setRole(INode token, Role role) {
		if (token.getAttributeValue("role") == null) {
			token.setAttributeValue("role", role);
		}

	}

	/**
	 * Executes Role Identifier. Identifies roles with chunk tags, name entity, srl,
	 */
	public void execute() {
		identifyWithSRL();
		identifyWithNER();
		//		identifyWithSENNA();
		identifyWithPOS();
		identifyWithChunk();
		addTokenToMap();

	}

	/**
	 * Adds tokens to the map in the action according to the role of each token.
	 */
	private void addTokenToMap() {
		for (INode token : action.getTokens()) {
			Role role = (Role) token.getAttributeValue("role");
			if (role != null) {
				action.addTokenToMap(token, (Role) token.getAttributeValue("role"));
			}
		}

	}

	/**
	 * Identifies roles with pos tags.
	 */
	private void identifyWithPOS() {
		for (INode token : action.getTokens()) {
			String posName = (String) token.getAttributeValue("pos");
			if (posName.equals("PRP")) {
				if (token.getAttributeValue("value").toString().equalsIgnoreCase("i")
						|| token.getAttributeValue("value").toString().equalsIgnoreCase("you")
						|| token.getAttributeValue("value").toString().equalsIgnoreCase("he")
						|| token.getAttributeValue("value").toString().equalsIgnoreCase("she")
						|| token.getAttributeValue("value").toString().equalsIgnoreCase("we")
						|| token.getAttributeValue("value").toString().equalsIgnoreCase("they")) {
					setRole(token, Role.ACTOR);
				} else if (!token.getAttributeValue("value").toString().equalsIgnoreCase("it")) {
					setRole(token, Role.WHO);
				} else if (token.getAttributeValue("value").toString().equalsIgnoreCase("it")) {
					setRole(token, Role.WHAT); // TODO: neu
				}

			}

		}
	}

	/**
	 * Identifies roles with chunk tags.
	 */
	private void identifyWithChunk() {
		for (INode token : action.getTokens()) {
			String chunkName = (String) token.getAttributeValue("chunkName");
			String pos = (String) token.getAttributeValue("pos");
			if (chunkName.equals("ADVP")) {
				setRole(token, Role.HOW);
			} else if (chunkName.equals("NP") && !pos.equals("CC")) {
				setRole(token, Role.WHAT);
			}
		}
	}

	private void identifyWithNER() {
		for (INode token : action.getTokens()) {
			if (token.getAttributeValue("ner") == null) {
				continue;
			}
			if (token.getAttributeValue("ner").toString().equalsIgnoreCase("S-PER")
					|| token.getAttributeValue("ner").toString().equalsIgnoreCase("PERSON")) {
				setRole(token, Role.WHO);
			} else if (token.getAttributeValue("ner").toString().equalsIgnoreCase("S-LOC")
					|| token.getAttributeValue("ner").toString().equalsIgnoreCase("LOCATION")) {
				setRole(token, Role.WHERE);
			}
		}
	}

	/**
	 * Uses the SRLabeler role (A0, V, ...) of a token to identify the role (actor,
	 * what, ...) of this token
	 */
	private void identifyWithSRL() {
		Iterator<? extends IArc> e = actionGraph.getArcs().iterator();
		while (e.hasNext()) {
			IArc arc = e.next();
			if (!actionGraph.hasArcType("srl") || !arc.getType().getName().equals("srl")) {
				continue;
			}
			INode src = arc.getSourceNode();
			INode trg = arc.getTargetNode();
			String srlRole = (String) arc.getAttributeValue("role");
			if (action.containsToken(src) && arc.getAllAttributeValues().size() > 1) {
				if (srlRole.equals("A0")) {
					setRole(trg, Role.ACTOR);
				} else if (srlRole.equals("V")) {
					setRole(trg, Role.PREDICATE);
				} else if (srlRole.equals("AM-LOC") && !trg.getAttributeValue("chunkName").equals("PP")) {
					setRole(trg, Role.WHERE);
				} else if (srlRole.equals("AM-TMP")) {
					setRole(trg, Role.WHEN);
				}
			}
		}
	}

	/**
	 * Evaluates the role for each token and adds it to the attribute "role" of the
	 * token. so far hardcoded and ALSO identifies the actionInstructionNumber
	 */
	// public void executeHardcoded() {
	// for (INode token : action.getTokens()) {
	//
	// switch ((String) token.getAttributeValue("value")) {
	// case "Armar":
	// setRole(token, Role.ACTOR);
	// break;
	// case "came":
	// setRole(token, Role.PREDICATE);
	// break;
	// case "he":
	// setRole(token, Role.ACTOR);
	// break;
	// case "brought":
	// setRole(token, Role.PREDICATE);
	// break;
	// case "me":
	// setRole(token, Role.WHO);
	// break;
	// case "an":
	// setRole(token, Role.WHAT);
	// break;
	// case "orange":
	// setRole(token, Role.WHAT);
	// break;
	// case "juice":
	// setRole(token, Role.WHAT);
	// break;
	//
	// }
	// }
	// }
}
