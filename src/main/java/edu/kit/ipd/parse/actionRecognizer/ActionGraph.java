package edu.kit.ipd.parse.actionRecognizer;

import java.util.LinkedList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.luna.graph.IArc;
import edu.kit.ipd.parse.luna.graph.IArcType;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.graph.INodeType;
import edu.kit.ipd.parse.luna.graph.ParseGraph;
import edu.kit.ipd.parse.luna.tools.ConfigManager;

/**
 * Graph used by actionRecognizer
 * 
 * @author ouyue
 *
 */
public class ActionGraph extends AdapterGraph {

    private static final Logger logger = LoggerFactory.getLogger(ActionGraph.class);
    private Properties props = ConfigManager.getConfiguration(ActionRecognizer.class);

    // The list of tokens in the input. Order same as in the input (determined
    // by the "NEXT" arc).
    private LinkedList<INode> tokens;

    // The list of predicates in the graph. Also contains the new added
    // predicates because of "and".
    private LinkedList<Predicate> predicates;

    // The list of actions in the graph.
    private LinkedList<Action> actions;

    /**
     * Initializes the tokens and adds Arc & Node attributes to the graph
     * 
     * @param graph
     */
    public ActionGraph(IGraph graph) {
        super(graph);
        this.initTokens();
        this.initActions();
        this.predicates = new LinkedList<Predicate>();
        this.addNodeArcAttributes();
    }

    /**
     * Initializes the tokens (a linkedList) with the nodes of the graph. The
     * order is determined by the "NEXT" arc.
     */
    private void initTokens() {
        this.tokens = new LinkedList<INode>();
        // first node of graph.
        INode currentNode = ((ParseGraph) graph).getFirstUtteranceNode();
        // size of graph
        int nodeCount = graph.getNodes().size();
        // adds first node to tokens
        this.tokens.add(currentNode);
        for (int i = 0; i < nodeCount - 1; i++) {
            IArc nextArc = currentNode.getOutgoingArcsOfType(graph.getArcType(props.getProperty("RELATION_ARC_TYPE")))
                    .iterator().next();
            currentNode = nextArc.getTargetNode();
            this.tokens.add(currentNode);

        }
    }

    /**
     * Initializes the actions according to the instructionNumber in the tokens.
     */
    private void initActions() {
        this.actions = new LinkedList<Action>();
        for (int i = 0; i < getInstructionCount(); i++) {
            this.actions.add(new Action());
        }
        // Adds tokens from graph to the actions according to
        // instructionNumber.
        for (INode token : this.tokens) {
            int instructionNr = (int) token.getAttributeValue("instructionNumber");
            this.actions.get(instructionNr).addToken(token);
        }
    }

    /**
     * Adds an attribute "role" to the node type "token", which is the type of
     * Enum "Role", containing 6 roles. Adds a node type "newPredicate". Then
     * adds attribute "value" to it, which is a string. Adds an arc type
     * "betweenToken", then adds an attribute "betweenRole" to it, which is the
     * type of Enum "BetweenRole", containing 3 betweenRole types.
     *
     */
    private void addNodeArcAttributes() {
        INodeType tokenType;
        if (graph.hasNodeType(props.getProperty("TOKEN_NODE_TYPE"))) {
            tokenType = graph.getNodeType(props.getProperty("TOKEN_NODE_TYPE"));
            // Every token has a role: Actor, Predicate, Parameter (what, who,
            // where, when, why, how)
            tokenType.addAttributeToType(Role.class.getName(), "role");
        } else {
            logger.error("\"" + props.getProperty("TOKEN_NODE_TYPE") + "\" " + "node type doesn't exist!");
        }

        INodeType newPredicateType;
        if (graph.hasNodeType(props.getProperty("NEWPREDICATE_NODE_TYPE"))) {
            newPredicateType = graph.getNodeType(props.getProperty("NEWPREDICATE_NODE_TYPE"));
        } else {
            newPredicateType = graph.createNodeType(props.getProperty("NEWPREDICATE_NODE_TYPE"));
        }
        newPredicateType.addAttributeToType("String", "value");

        IArcType relationType;
        if (graph.hasArcType(props.getProperty("BETWEENTOKEN_ARC_TYPE"))) {
            relationType = graph.getArcType(props.getProperty("BETWEENTOKEN_ARC_TYPE"));
        } else {
            relationType = graph.createArcType(props.getProperty("BETWEENTOKEN_ARC_TYPE"));
        }
        relationType.addAttributeToType(BetweenRole.class.getName(), "type");

    }

    /**
     * Returns the token values of all nodes in a String.
     * 
     * @return String repesentation of all nodes, only including the values.
     */
    public String getNodeValuesAsString() {
        StringBuilder sb = new StringBuilder();
        for (INode node : graph.getNodes()) {
            sb.append(node.getAttributeValue("value"));
            sb.append(" ");
        }
        return sb.toString();

    }

    /**
     * Gets tokens in the graph. Order same as in the input (determined by the
     * "NEXT" arc).
     * 
     * @return tokens in the graph.
     */
    public LinkedList<INode> getTokens() {
        return this.tokens;
    }

    /**
     * Gets actions in the graph.
     * 
     * @return
     */
    public LinkedList<Action> getActions() {
        return this.actions;
    }

    /**
     * Gets predicates in the graph. Also the new added predicates because of
     * "and".
     * 
     * @return
     */
    public LinkedList<Predicate> getPredicates() {
        return this.predicates;
    }

    /**
     * Adds a predicate to the predicate list in the graph.
     * 
     * @param predicate
     */
    public void addPredicate(Predicate predicate) {
        this.predicates.add(predicate);
    }

    /**
     * Returns the count of instructions in the actionGraph (token list) The
     * instruction count equals the instructionNumber of the last token + 1.
     * 
     * @return the count of instructions in the actionGraph.
     */
    public int getInstructionCount() {
        INode lastNode = getTokens().getLast();
        int lastInstructionNum = (int) lastNode.getAttributeValue("instructionNumber");
        return (lastInstructionNum + 1);
    }

    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node: \n");
        for (INode node : graph.getNodes()) {
            sb.append(node.getAttributeValue("value") + "(" + node.getAttributeValue("role") + ") ");
        }
        sb.append("\n\nArc: \n");

        for (IArc arc : graph.getArcs()) {
            if (arc.getType().equals(graph.getArcType(props.getProperty("BETWEENTOKEN_ARC_TYPE")))) {
                sb.append(arc.getSourceNode().getAttributeValue("value") + "--" + arc.getAttributeValue("type") + "-->"
                        + arc.getTargetNode().getAttributeValue("value") + "\n");
            }
        }

        return sb.toString();
    }

}
