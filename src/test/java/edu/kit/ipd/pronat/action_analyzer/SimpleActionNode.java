package edu.kit.ipd.pronat.action_analyzer;

/**
 * Used for SimpleActionGraph. Represents a node, only containing the value of
 * the token and the role
 *
 * @author ouyue
 *
 */
public class SimpleActionNode {

	private String value;
	private Role role;
	private int position;

	public SimpleActionNode() {

	}

	public SimpleActionNode(String value, Role role, int position) {
		this.value = value;
		this.role = role;
		this.position = position;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override public String toString() {
		return position + "." + value + "(" + role + ")";
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
