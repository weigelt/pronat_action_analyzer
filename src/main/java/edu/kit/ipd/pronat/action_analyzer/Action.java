package edu.kit.ipd.pronat.action_analyzer;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;

import edu.kit.ipd.parse.luna.graph.INode;

/**
 * An action contains an action
 * 
 * @author ouyue
 *
 */
public class Action {

	private LinkedList<INode> tokens;

	// Maps roles to nodes.
	private Map<Role, LinkedList<INode>> parameterMap;

	// The predicate in the action.
	private Predicate predicate;

	//    private Properties props = ConfigManager.getConfiguration(ActionAnalyzer.class);

	public Action() {
		tokens = new LinkedList<INode>();
		initMap();

	}

	public Action(LinkedList<INode> tokens) {
		this.tokens = tokens;
		initMap();

	}

	/**
	 * Adds a token to an action.
	 * 
	 * @param token
	 *            token to be added to the action
	 */
	public void addToken(INode token) {
		tokens.add(token);
	}

	/**
	 * Returns the tokens in an action.
	 * 
	 * @return the tokens in an action.
	 */
	public LinkedList<INode> getTokens() {
		return tokens;
	}

	public boolean containsToken(INode token) {
		return tokens.contains(token);
	}

	/**
	 * Returns the instructionNumber of the tokens in the action. The tokens should
	 * all have the same instructionNumber.
	 * 
	 * @return the instructionNumber of the tokens in the action.
	 */
	public int getInstructionNumber() {
		return (int) tokens.getFirst().getAttributeValue("instructionNumber");
	}

	/**
	 * Initializes a map which maps roles (except predicates) to lists of nodes,
	 * which plays the according role.
	 */
	public void initMap() {
		parameterMap = new EnumMap<Role, LinkedList<INode>>(Role.class);
		parameterMap.put(Role.ACTOR, new LinkedList<INode>());
		parameterMap.put(Role.PREDICATE, new LinkedList<INode>());
		parameterMap.put(Role.WHAT, new LinkedList<INode>());
		parameterMap.put(Role.WHEN, new LinkedList<INode>());
		parameterMap.put(Role.WHERE, new LinkedList<INode>());
		parameterMap.put(Role.WHO, new LinkedList<INode>());
		parameterMap.put(Role.WHY, new LinkedList<INode>());
		parameterMap.put(Role.HOW, new LinkedList<INode>());

	}

	/**
	 * Adds a node with a given role to the map.
	 * 
	 * @param node
	 *            node to be added to the map.
	 * @param role
	 *            role of the node.
	 */
	public void addTokenToMap(INode node, Role role) {
		parameterMap.get(role).add(node);
	}

	/**
	 * Returns map from Roles to lists of nodes.
	 * 
	 * @return
	 */
	public Map<Role, LinkedList<INode>> getParameterMap() {
		return parameterMap;
	}

	/**
	 * Can only be called after the map is initialized (roles are identified)
	 * Initializes the predicate of the action with the info in map.
	 * 
	 * @return the predicate of the action
	 */
	public Predicate createPredicate() {
		predicate = new Predicate(parameterMap.get(Role.PREDICATE), this);
		return predicate;
	}

	/**
	 * Returns the predicate of the action.
	 * 
	 * @return the predicate of the action.
	 */
	public Predicate getPredicate() {
		return predicate;
	}

	/**
	 * Returns the action as a string, which contains the values of the tokens in
	 * the action
	 * 
	 * @return the instruciton as a string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (INode node : tokens) {
			sb.append(node.getAttributeValue("value"));
			sb.append(" ");
		}
		return sb.toString();
	}
}
