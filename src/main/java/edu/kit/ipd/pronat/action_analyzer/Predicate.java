package edu.kit.ipd.pronat.action_analyzer;

import java.util.LinkedList;

import edu.kit.ipd.parse.luna.graph.INode;

/**
 * A predicate is a list of tokens which build a predicate. For example: has
 * done
 * 
 * @author ouyue
 *
 */
public class Predicate {
	//the tokens in the predicate
	private LinkedList<INode> tokens;
	//the action which contains the predicate. Each action has and only has one predicate.
	private Action action;

	public Predicate(LinkedList<INode> tokens, Action action) {
		this.tokens = tokens;
		this.action = action;
	}

	/**
	 * Sets the action of the predicate
	 * 
	 * @param action
	 *            the action of the predicate
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * Returns the action which contains the predicate.
	 * 
	 * @return the action which contains the predicate.
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Returns the number of the tokens in the predicate.
	 * 
	 * @return
	 */
	public int size() {
		return tokens.size();
	}

	/**
	 * Returns the tokens in the predicate.
	 * 
	 * @return the tokens in the predicate.
	 */
	public LinkedList<INode> getTokens() {
		return tokens;
	}

	/**
	 * Sets the tokens in the predicate.
	 * 
	 * @param tokens
	 */
	public void setTokens(LinkedList<INode> tokens) {
		this.tokens = tokens;
	}

	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		for (INode token : tokens) {
			sb.append(token.getAttributeValue("value"));
			sb.append(" ");
		}
		return sb.toString();
	}
}
