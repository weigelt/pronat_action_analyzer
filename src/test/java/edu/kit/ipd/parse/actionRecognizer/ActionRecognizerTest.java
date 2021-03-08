package edu.kit.ipd.parse.actionRecognizer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;

import edu.kit.ipd.parse.ner.NERTagger;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.parse.graphBuilder.GraphBuilder;
import edu.kit.ipd.parse.luna.data.MissingDataException;
import edu.kit.ipd.parse.luna.data.PrePipelineData;
import edu.kit.ipd.parse.luna.data.token.Token;
import edu.kit.ipd.parse.luna.graph.IArc;
import edu.kit.ipd.parse.luna.graph.IArcType;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.graph.INodeType;
import edu.kit.ipd.parse.luna.graph.ParseGraph;
import edu.kit.ipd.parse.luna.pipeline.PipelineStageException;
import edu.kit.ipd.parse.luna.tools.StringToHypothesis;
import edu.kit.ipd.parse.shallownlp.ShallowNLP;
import edu.kit.ipd.parse.srlabeler.SRLabeler;
import edu.kit.ipd.parse.tokenizing.Tokenizer;

public class ActionRecognizerTest {

	ActionRecognizer recognizer;
	//	static Tokenizer tokenizer;
	static ShallowNLP snlp;
	static SRLabeler srLabeler;
	static GraphBuilder graphBuilder;
	static NERTagger nert;

	Token[] actual;
	IGraph graph;
	PrePipelineData ppd;

	private static final String RELATION_ARC_TYPE = "relation";
	private static final String TOKEN_NODE_TYPE = "token";

	@Before
	public void before() {
		recognizer = new ActionRecognizer();
		//		tokenizer = new Tokenizer();
		//		tokenizer.init();
		snlp = new ShallowNLP();
		snlp.init();
		srLabeler = new SRLabeler();
		srLabeler.init();
		nert = new NERTagger();
		nert.init();
		graphBuilder = new GraphBuilder();
		graphBuilder.init();
	}

	@Test
	public void testActionRecognizer() {

		System.out.println(
				"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Test ActionRecognizer starts!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(
				"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Test ActionRecognizer starts!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		String input = "Put the juice in the kitchen";

		ppd = new PrePipelineData();
		ppd.setMainHypothesis(StringToHypothesis.stringToMainHypothesis(input));
		try {
			//tokenizer.exec(ppd);
			snlp.exec(ppd);
			srLabeler.exec(ppd);
			nert.exec(ppd);
			graphBuilder.exec(ppd);
		} catch (PipelineStageException e1) {
			e1.printStackTrace();
		}

		try {

			graph = ppd.getGraph();
		} catch (MissingDataException e) {
			e.printStackTrace();
		}

		printSRLGraph(graph);

		for (INode node : graph.getNodes()) {
			System.out.println("Token: " + node.getAllAttributeNamesAndValuesAsPair());
		}

		// System.out.println("original Graph: " + graph.showGraph());

		recognizer.setGraph(graph.clone());
		recognizer.init();
		recognizer.exec();

		System.out.println(recognizer.getActionGraph());

		//        System.out.println("-----");
		//
		SimpleActionGraph simpleGraph = new SimpleActionGraph(recognizer.getActionGraph());
		System.out.println(simpleGraph);

		// System.out.println("altered Graph: " +
		// this.recognizer.getGraph().showGraph());
		for (INode node : recognizer.getActionGraph().getNodes()) {
			System.out.println("Token: " + node.getAllAttributeNamesAndValuesAsPair());
		}

		System.out
				.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Test ActionRecognizer ends!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out
				.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Test ActionRecognizer ends!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

	}

	private void printSRLGraph(IGraph graph) {
		String prettyPrint = "SRLGraph: " + " {\n";

		Iterator<? extends IArc> e = graph.getArcs().iterator();
		while (e.hasNext()) {
			IArc arc = e.next();
			INode src = arc.getSourceNode();
			INode trg = arc.getTargetNode();
			prettyPrint = prettyPrint.concat(src.getAllAttributeValues().size() == 0 ? src.getType().getName()
					: (String) src.getAttributeValue(src.getAttributeNames().get(0)));
			prettyPrint = prettyPrint.concat(" ---" + (arc.getAllAttributeValues().size() == 0 ? arc.getType()
					: arc.getAllAttributeValues().size() == 1 ? arc.getAttributeValue(arc.getAttributeNames().get(0))
							: arc.getAttributeValue(SRLabeler.IOBES) + "-" + arc.getAttributeValue(SRLabeler.ROLE_VALUE_NAME) + ", [PB: "
									+ arc.getAttributeValue(SRLabeler.PROPBANK_ROLE_DESCRIPTION) + "; VN:"
									+ arc.getAttributeValue(SRLabeler.VN_ROLE_NAME) + "; Conf="
									+ arc.getAttributeValue(SRLabeler.ROLE_CONFIDENCE_NAME) + "]"));
			prettyPrint = prettyPrint.concat(" --->" + (trg.getAllAttributeValues().size() == 0 ? trg.getType().getName()
					: (String) trg.getAttributeValue(trg.getAttributeNames().get(0))) + "\n");
		}

		Iterator<INode> u = graph.getNodes().iterator();
		while (u.hasNext()) {
			INode node = u.next();
			if (node.getIncomingArcs().isEmpty() && node.getOutgoingArcs().isEmpty()) {
				prettyPrint = prettyPrint.concat(node.getAllAttributeValues().size() == 0 ? node.getType().getName()
						: node.getAttributeValue(node.getAttributeNames().get(0)) + "\n");
			}
		}
		prettyPrint = prettyPrint.concat("}\n");
		System.out.println(prettyPrint);
	}

	// @Test //the input is a string. Doesn't work with SRLabeler.
	public void testActionRecognizerWithString() {

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!Test ActionRecognizer!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!Test ActionRecognizer!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		String input = "Armar came and he brought me an orange juice";

		try {
			actual = snlp.parse(input, null);
		} catch (IOException | URISyntaxException | InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Tokens: " + Arrays.deepToString(actual));
		graph = snlp.createParseGraph(actual);
		System.out.println("original Graph: " + graph.showGraph());

		recognizer.setGraph(graph.clone());
		recognizer.init();
		recognizer.exec();
		System.out.println("altered Graph: " + recognizer.getGraph().showGraph());

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!Test ActionRecognizer!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!Test ActionRecognizer!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

	}

	// @Test
	public void testSNLP() {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!Test SNLP begin!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!Test SNLP begin!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		String input = "Armar brought me an orange juice and a popcorn";
		try {
			actual = snlp.parse(input, null);
		} catch (IOException | URISyntaxException | InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("SNLP Tokens: " + Arrays.deepToString(actual));
		graph = snlp.createParseGraph(actual);
		System.out.println("SNLP original Graph: " + graph.showGraph());

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!Test SNLP end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!Test SNLP end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

	}

	// @Test //good now :)
	public void outgoingArcsOfType() {
		IGraph graph = new ParseGraph();
		INodeType tokenType;
		if (graph.hasNodeType(TOKEN_NODE_TYPE)) {
			tokenType = graph.getNodeType(TOKEN_NODE_TYPE);
		} else {
			tokenType = graph.createNodeType(TOKEN_NODE_TYPE);
		}
		IArcType relationType;
		if (graph.hasArcType(RELATION_ARC_TYPE)) {
			relationType = graph.getArcType(RELATION_ARC_TYPE);
		} else {
			relationType = graph.createArcType(RELATION_ARC_TYPE);
		}

		INode tok1 = graph.createNode(tokenType);
		INode tok2 = graph.createNode(tokenType);
		INode tok3 = graph.createNode(tokenType);
		tokenType.addAttributeToType("String", "value");

		tok1.setAttributeValue("value", "Ben");
		tok2.setAttributeValue("value", "killed");
		tok3.setAttributeValue("value", "Han");

		IArc arc1 = graph.createArc(tok1, tok2, relationType);
		IArc arc2 = graph.createArc(tok2, tok3, relationType);
		relationType.addAttributeToType("String", "arcValue");

		arc1.setAttributeValue("arcValue", "one to two");
		arc2.setAttributeValue("arcValue", "two to three");

		// for(INode node: graph.getNodes()) {
		// System.out.println(node.getAllAttributeNamesAndValuesAsPair());
		// }
		// for(IArc arc: graph.getArcs()) {
		// System.out.println(arc.getAllAttributeNamesAndValuesAsPair());
		// }
		System.out.println("arcs in original graph:");
		INode currentNode = graph.getNodes().iterator().next();
		for (int i = 0; i < 2; i++) {
			IArc nextArc = currentNode.getOutgoingArcsOfType(relationType).iterator().next();
			System.out.println(nextArc.getAllAttributeNamesAndValuesAsPair());
			currentNode = nextArc.getTargetNode();
		}

		IGraph cloneGraph = graph.clone();
		System.out.println("arcs in cloned graph:");
		INode cloneCurrentNode = graph.getNodes().iterator().next();
		for (int i = 0; i < 2; i++) {
			IArc nextArc = cloneCurrentNode.getOutgoingArcsOfType(relationType).iterator().next();
			System.out.println(nextArc.getAllAttributeNamesAndValuesAsPair());
			cloneCurrentNode = nextArc.getTargetNode();
		}

	}

	// @Test //good now :)
	public void addAttributeToType() {
		IGraph graph = new ParseGraph();
		INodeType tokenType;
		if (graph.hasNodeType(TOKEN_NODE_TYPE)) {
			tokenType = graph.getNodeType(TOKEN_NODE_TYPE);
		} else {
			tokenType = graph.createNodeType(TOKEN_NODE_TYPE);
		}
		IArcType relationType;
		if (graph.hasArcType(RELATION_ARC_TYPE)) {
			relationType = graph.getArcType(RELATION_ARC_TYPE);
		} else {
			relationType = graph.createArcType(RELATION_ARC_TYPE);
		}

		INode tok1 = graph.createNode(tokenType);
		INode tok2 = graph.createNode(tokenType);
		INode tok3 = graph.createNode(tokenType);
		tokenType.addAttributeToType("String", "value");

		tok1.setAttributeValue("value", "Ben");
		tok2.setAttributeValue("value", "killed");
		tok3.setAttributeValue("value", "Han");

		tokenType.addAttributeToType(Role.class.getName(), "role");
		System.out.println("Add \"role\" attribute to token type in the original graph: ");
		for (INode node : graph.getNodes()) {
			System.out.println(node.getAllAttributeNamesAndValuesAsPair());
		}

		System.out.println("Add \"role2\" attribute to token type in the cloned graph: ");
		IGraph clonedGraph = graph.clone();
		INodeType tokenTypeInClonedGraph = clonedGraph.getNodeType(TOKEN_NODE_TYPE);
		tokenTypeInClonedGraph.addAttributeToType(Role.class.getName(), "role2");

		for (INode node : clonedGraph.getNodes()) {
			System.out.println(node.getAllAttributeNamesAndValuesAsPair());
		}

		System.out.println("Add \"role3\" attribute to token type in the cloned graph in a bad way: ");
		for (INode node : clonedGraph.getNodes()) {
			INodeType type = node.getType();
			if (type.getName() != TOKEN_NODE_TYPE) {
				continue;
			}
			type.addAttributeToType(Role.class.getName(), "role3");
		}

		for (INode node : clonedGraph.getNodes()) {
			System.out.println(node.getAllAttributeNamesAndValuesAsPair());
		}

	}

	// @Test //good now :)
	public void getArcs() {
		IGraph graph = new ParseGraph();
		INodeType tokenType;
		if (graph.hasNodeType(TOKEN_NODE_TYPE)) {
			tokenType = graph.getNodeType(TOKEN_NODE_TYPE);
		} else {
			tokenType = graph.createNodeType(TOKEN_NODE_TYPE);
		}
		IArcType relationType;
		if (graph.hasArcType(RELATION_ARC_TYPE)) {
			relationType = graph.getArcType(RELATION_ARC_TYPE);
		} else {
			relationType = graph.createArcType(RELATION_ARC_TYPE);
		}

		INode tok1 = graph.createNode(tokenType);
		INode tok2 = graph.createNode(tokenType);
		INode tok3 = graph.createNode(tokenType);
		tokenType.addAttributeToType("String", "value");

		tok1.setAttributeValue("value", "Ben");
		tok2.setAttributeValue("value", "killed");
		tok3.setAttributeValue("value", "Han");

		IArc arc1 = graph.createArc(tok1, tok2, relationType);
		IArc arc2 = graph.createArc(tok2, tok3, relationType);
		relationType.addAttributeToType("String", "arcValue");

		arc1.setAttributeValue("arcValue", "one to two");
		arc2.setAttributeValue("arcValue", "two to three");

		for (IArc arc : graph.getArcs()) {
			System.out.println("getArcs: from " + arc.getSourceNode().getAttributeValue("value") + " to "
					+ arc.getTargetNode().getAttributeValue("value") + "   TYPE: " + arc.getType().getName() + " "
					+ arc.getAllAttributeNamesAndValuesAsPair());

		}

	}
}
