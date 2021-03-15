package edu.kit.ipd.pronat.action_analyzer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;

import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.graph.IArc;
import edu.kit.ipd.parse.luna.tools.ConfigManager;

/**
 * SimpleActionGraph only contains specific information about the action Graph:
 * tokens with value(name) and role, arcs with type.
 * 
 * @author ouyue
 *
 */
public class SimpleActionGraph {

	private Properties props = ConfigManager.getConfiguration(ActionAnalyzer.class);

	// all the nodes in the graph, only containing values and roles. Ordered by
	// the presense in sentence.
	private LinkedList<SimpleActionNode> nodes;

	// only the betweentoken arcs. Ordered by nothing xD
	private LinkedList<SimpleActionArc> arcs;

	public SimpleActionGraph() {
      nodes = new LinkedList<SimpleActionNode>();
      arcs = new LinkedList<SimpleActionArc>();
	}

	public SimpleActionGraph(ActionGraph graph) {
      nodes = new LinkedList<SimpleActionNode>();
      arcs = new LinkedList<SimpleActionArc>();

		HashMap<INode, SimpleActionNode> map = new HashMap<INode, SimpleActionNode>();
		int counter = 0;
		for (INode node : graph.getTokens()) {
			SimpleActionNode simpleNode = new SimpleActionNode();
			simpleNode.setValue((String) node.getAttributeValue("value"));
			if (node.getAttributeValue("role") != null) {
				simpleNode.setRole((Role) node.getAttributeValue("role"));
			}
			simpleNode.setPosition(counter);
        nodes.add(simpleNode);
			map.put(node, simpleNode);
			counter++;
		}

		for (IArc arc : graph.getArcs()) {
			if (arc.getType().equals(graph.getArcType(props.getProperty("BETWEENTOKEN_ARC_TYPE")))) {
				SimpleActionArc simpleArc = new SimpleActionArc();
				simpleArc.setSourceNode(map.get(arc.getSourceNode()));
				simpleArc.setTargetNode(map.get(arc.getTargetNode()));
				simpleArc.setType((BetweenRole) arc.getAttributeValue("type"));
          arcs.add(simpleArc);
			}
		}
	}

	public boolean containsArc(SimpleActionArc simpleArc) {
		for (SimpleActionArc arc : arcs) {
			if (arc.equals(simpleArc)) {
				return true;
			}
		}
		return false;
	}

	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Nodes: \n");
		for (SimpleActionNode node : nodes) {
			sb.append(node + " ");
		}
		sb.append("\n\nArcs: \n");
		for (SimpleActionArc arc : arcs) {
			sb.append(arc + "\n");
		}

		return sb.toString();
	}

	//    public String toSampleSolutionFormat(String name) {
	//        
	//        StringBuilder sb = new StringBuilder();
	//        sb.append("<example name=\"" + name + "\">");
	//        for (SimpleActionNode node : this.nodes) {
	//            sb.append(node.getValue() + " ");
	//        }
	//        sb.append("</example>\n");
	//        
	//        sb.append("<role name=\"" + name + "\">");
	//        for (SimpleActionNode node : this.nodes) {
	//            sb.append(node.getPosition() + "." + node.getValue() + "(" + node.getRole() + ") ");
	//        }
	//        sb.append("</role>\n");
	//        
	//        sb.append("<arc name=\"" + name + "\">");
	//        for (SimpleActionArc arc : this.arcs) {
	//            sb.append(arc.getSourceNode().getPosition() + "-" + arc.getTargetNode().getPosition() + "(");
	//            switch (arc.getType()) {
	//            case PREDICATE_TO_PARA:
	//                sb.append("pp) ");
	//                break;
	//            case INSIDE_CHUNK:
	//                sb.append("ic) ");
	//                break;
	//            case NEXT_ACTION:
	//                sb.append("na) ");
	//                break;
	//                
	//            }
	//        }
	//        sb.append("</arc>\n\n");
	//
	//        return sb.toString();
	//        
	//    }

	/**
	 * Adds the role solution contained in text to the graph
	 * 
	 * @param text
	 *            contains solution to roles
	 */
	public void addsRolesWithSolution(String text) {
		String[] nrValueRoles = text.split("\\s+");
		for (String nrValueRole : nrValueRoles) {
			String[] nrValue_Role = nrValueRole.split("\\(");
			String[] nr_Value = nrValue_Role[0].split("\\.");
			int position = Integer.parseInt(nr_Value[0]);
			String value = nr_Value[1];
			Role role = null;
			if (nrValue_Role.length > 1 && nrValue_Role[1].length() > 1) {
				String roleAsString = nrValue_Role[1].substring(0, nrValue_Role[1].length() - 1);
				switch (roleAsString.toLowerCase()) {
				case "actor":
					role = Role.ACTOR;
					break;
				case "predicate":
					role = Role.PREDICATE;
					break;
				case "who":
					role = Role.WHO;
					break;
				case "what":
					role = Role.WHAT;
					break;
				case "when":
					role = Role.WHEN;
					break;
				case "where":
					role = Role.WHERE;
					break;
				case "how":
					role = Role.HOW;
					break;
				case "why":
					role = Role.WHY;
					break;
				case "null":
					break;
				default:
					break;
				}
			}

        addNode(new SimpleActionNode(value, role, position));
		}

	}

	/**
	 * Adds the arc solution contained in text to the graph
	 * 
	 * @param text
	 *            contains solution to arcs
	 */
	public void addsArcsWithSolution(String text) {
		if (text.equals("")) {
			return;
		}
		String[] nodeNodeTypes = text.split("\\s+");
		for (String nodeNodeType : nodeNodeTypes) {
			String[] nodeNode_Type = nodeNodeType.split("\\(");
			String sourceNodeNr = nodeNode_Type[0].split("-")[0];
			String targetNodeNr = nodeNode_Type[0].split("-")[1];
			String arcTypeAsString = nodeNode_Type[1].substring(0, nodeNode_Type[1].length() - 1);
			BetweenRole arcType = null;
			switch (arcTypeAsString) {
			case "pp":
				arcType = BetweenRole.PREDICATE_TO_PARA;
				break;
			case "ic":
				arcType = BetweenRole.INSIDE_CHUNK;
				break;
			case "na":
				arcType = BetweenRole.NEXT_ACTION;
				break;
			default:
				break;
			}
        addArc(new SimpleActionArc(getNodes().get(Integer.parseInt(sourceNodeNr)), getNodes().get(Integer.parseInt(targetNodeNr)), arcType));

		}
	}

	public void addNode(SimpleActionNode node) {
      nodes.add(node);
	}

	public void addArc(SimpleActionArc arc) {
      arcs.add(arc);
	}

	public LinkedList<SimpleActionNode> getNodes() {
		return nodes;
	}

	public void setNodes(LinkedList<SimpleActionNode> nodes) {
		this.nodes = nodes;
	}

	public LinkedList<SimpleActionArc> getArcs() {
		return arcs;
	}

	public void setArcs(LinkedList<SimpleActionArc> arcs) {
		this.arcs = arcs;
	}

}
