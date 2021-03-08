package edu.kit.ipd.parse.actionRecognizer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.kit.ipd.parse.luna.data.MissingDataException;
import edu.kit.ipd.parse.luna.data.PrePipelineData;
import edu.kit.ipd.parse.luna.data.token.Token;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.pipeline.PipelineStageException;
import edu.kit.ipd.parse.shallownlp.ShallowNLP;
import edu.kit.ipd.parse.srlabeler.SRLabeler;
import edu.kit.ipd.parse.tokenizing.Tokenizer;

import static org.junit.Assert.*;

public class Evaluation {

	ActionRecognizer recognizer;
	//    static Tokenizer tokenizer;
	static ShallowNLP snlp;
	static SRLabeler srLabeler;
	HashMap<String, SimpleActionGraph> graphSolutionMap;
	HashMap<String, String> instructionMap;
	HashMap<String, Accuracy> accuracyMap;

	double averageRoleAccuracy;
	double averageArcPrecision;
	double averageArcRecall;

	Token[] actual;
	IGraph graph;
	PrePipelineData ppd;

	@Before
	public void setUp() {

      recognizer = new ActionRecognizer();
		//        tokenizer = new Tokenizer();
		//        tokenizer.init();
		snlp = new ShallowNLP();
		snlp.init();
		srLabeler = new SRLabeler();
		srLabeler.init();

      graphSolutionMap = new HashMap<String, SimpleActionGraph>();
      instructionMap = new HashMap<String, String>();
      accuracyMap = new HashMap<String, Accuracy>();

		try {
			File file = new File(Evaluation.class.getResource("/examplesWithSolutions.xml").toURI());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			NodeList examples = doc.getElementsByTagName("example");
			for (int i = 0; i < examples.getLength(); i++) {
				Element node = (Element) examples.item(i);
				String name = node.getAttribute("name");
				String text = node.getTextContent();
          instructionMap.put(name, text);
          graphSolutionMap.put(name, new SimpleActionGraph());
			}

			NodeList roleSolutions = doc.getElementsByTagName("role");
			for (int i = 0; i < roleSolutions.getLength(); i++) {
				Element node = (Element) roleSolutions.item(i);
				String name = node.getAttribute("name");
				String text = node.getTextContent();

          graphSolutionMap.get(name).addsRolesWithSolution(text);
			}

			NodeList arcSolutions = doc.getElementsByTagName("arc");
			for (int i = 0; i < arcSolutions.getLength(); i++) {
				Element node = (Element) arcSolutions.item(i);
				String name = node.getAttribute("name");
				String text = node.getTextContent();

          graphSolutionMap.get(name).addsArcsWithSolution(text);
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@After
	public void after() {
		printAccuracyMap();
	}

	@Ignore
	@Test
	public void test1() {
		//         test("1");
		//         test("2");
		//         test("3");
		//         test("4");
		//         test("5");
		//         test("6");
		//         test("7");
		//         test("8");
		//         test("9");
		//         test("10");
		//         test("11");
		//         test("12");

		// test("t1");
		// test("t2");
		// test("t3");
		// test("t4");
		// test("t5");
		// test("t6");
		// test("t7");
		// test("t8");
		// test("t9");
		// test("t10");
		// test("t11");
		// test("t12");
		// test("t13");
		// test("t14");
		// test("t15");
		// test("t16");
		// test("t17");
		// test("t18");
		// test("t19");
		// test("t20");
		// test("t21");
		// test("t22");
		// test("t23");
		// test("t24");
		// test("t25");
		// test("t26");
		// test("t27");
		// test("t28");
		// test("t29");
		// test("t30");
		// test("t31");
		// test("t32");
		// test("t33");
		// test("t34");
		// test("t35");
		// test("t36");
		// test("t37");
		// test("t38");
		// test("t39");
		// test("t40");
		// test("t41");
		// test("t42");
		// test("t43");
		// test("t44");
		// test("t45");
		// test("t46");
		// test("t47");
		// test("t48");
		// test("t49");
		// test("t50");
		// test("t51");
		// test("t52");
		// test("t53");
		// test("t54");
		// test("t55");
		// test("t56");
		// test("t57");
		// test("t58");
		// test("t59");
		// test("t60");
		// test("t61");
		// test("t62");
		// test("t63");
		// test("t64");
		// test("t65");
		// test("t66");
		// test("t67");
		// test("t68");
		// test("t69");
		// test("t70");
		// test("t71");
		// test("t72");
		// test("t73");
		// test("t74");
		// test("t75");
		// test("t76");
		// test("t77");
		// test("t78");
		// test("t79");
		// test("t80");
		// test("t81");
		// test("t82");
		// test("t83");
		// test("t84");
		// test("t85");
		// test("t86");
		// test("t87");
		// test("t88");
		// test("t89");
		// test("t90");
		// test("t91");
		// test("t92");
		// test("t93");
		// test("t94");
		// test("t95");
		// test("t96");
		// test("t97");
		// test("t98");
		// test("t99");
		// test("t100");
		// test("t101");
		// test("t102");
		// test("t103");
		// test("t104");
		// test("t105");
		// test("t106");
		// test("t107");
		// test("t108");
		// test("t109");
		// test("t110");
		// test("t111");
		// test("t112");
		// test("t113");

		// test("h1");
		// test("h2");
		// test("h3");
		// test("h4");
		// test("h5");
		// test("h6");
		// test("h7");
		// test("h8");
		// test("h9");
		// test("h10");
		// test("h11");
		// test("h12");
		// test("h13");
		// test("h14");
		// test("h15");
		// test("h16");
		// test("h17");
		// test("h18");
		// test("h19");
		// test("h20");
		// test("h21");
		// test("h22");
		// test("h23");
		// test("h24");
		// test("h25");
		// test("h26");
		// test("h27");
		// test("h28");
		// test("h29");
		// test("h30");
		// test("h31");
		// test("h32");
		// test("h33");
		// test("h34");
		// test("h35");
		// test("h36");
		// test("h37");
		// test("h38");
		// test("h39");
		// test("h40");
		// test("h41");
		// test("h42");
		// test("h43");
		// test("h44");
		// test("h45");
		// test("h46");
		// test("h47");
		// test("h48");
		// test("h49");
		// test("h50");
		// test("h51");
		// test("h52");
		// test("h53");
		// test("h54");
		// test("h55");
		// test("h56");
		// test("h57");
		// test("h58");
		// test("h59");
		// test("h60");
		// test("h61");
		// test("h62");
		// test("h63");
		// test("h64");
		// test("h65");
		// test("h66");
		// test("h67");
		// test("h68");
		// test("h69");
		// test("h70");
		// test("h71");
		// test("h72");
		// test("h73");
		// test("h74");
		// test("h75");
		// test("h76");
		// test("h77");
		// test("h78");
		// test("h79");
		// test("h80");
		// test("h81");
		// test("h82");
		// test("h83");
		// test("h84");
		// test("h85");
		// test("h86");
		// test("h87");
		// test("h88");
		// test("h89");
		// test("h90");
		// test("h91");
		// test("h92");
		// test("h93");
		// test("h94");
		// test("h95");
		// test("h96");
		// test("h97");
		// test("h98");
		// test("h99");
		// test("h100");
		// test("h101");
		// test("h102");
		// test("h103");
		test("h104");
		test("h105");
		test("h106");
		// test("h107");
		// test("h108");
		// test("h109");
		// test("h110");
		// test("h111");
		// test("h112");
		test("h113");

	}

	public void printAccuracyMap() {
		double roleAccSum = 0;
		double arcPreSum = 0;
		double arcRecSum = 0;
		System.out.println("overview: role accuracy  |  arc precision  |  arc recall");
		for (Map.Entry<String, Accuracy> entry : accuracyMap.entrySet()) {
			roleAccSum += entry.getValue().getRoleAccuracy();
			arcPreSum += entry.getValue().getArcPrecision();
			arcRecSum += entry.getValue().getArcRecall();
			System.out.print(entry.getKey() + "            ");
			System.out.println(entry.getValue());
		}

		int size = accuracyMap.size();
		System.out.println();
		System.out.println("size " + size);
		Accuracy average = new Accuracy(roleAccSum / size, arcPreSum / size, arcRecSum / size);
		System.out.println("average:        " + average);

	}

	@Ignore
	public void test(String name) {
		ppd = new PrePipelineData();
		ppd.setTranscription(instructionMap.get(name));
		SimpleActionGraph resultGraph = executeActionRecognizer(ppd);
		SimpleActionGraph solutionGraph = graphSolutionMap.get(name);

		System.out.println("---------------------------------- Test " + name + " -----------------------------------");
		System.out.println("Results:");
		System.out.println(resultGraph);
		// System.out.println("Results in sample solution format:");
		// System.out.println(resultGraph.toSampleSolutionFormat(name));
		System.out.println("Sample solution:");
		System.out.println(solutionGraph);

		SimpleGraphComparer comparer = new SimpleGraphComparer(resultGraph, solutionGraph);
		System.out.println("Role accuracy: " + comparer.roleAccuracy() * 100 + "%");
		System.out.println("Arc precision: " + comparer.arcPrecision() * 100 + "%");
		System.out.println("Arc recall: " + comparer.arcRecall() * 100 + "%");
		System.out.println("-------------------------------------------------------------------------------------");
      accuracyMap.put(name, new Accuracy(comparer.roleAccuracy(), comparer.arcPrecision(), comparer.arcRecall()));

	}

	private SimpleActionGraph executeActionRecognizer(PrePipelineData ppd) {
		try {
			//            tokenizer.exec(ppd);
			snlp.exec(ppd);
			srLabeler.exec(ppd);
		} catch (PipelineStageException e1) {
			e1.printStackTrace();
		}
		try {
			graph = ppd.getGraph();
		} catch (MissingDataException e) {
			e.printStackTrace();
		}
		recognizer.setGraph(graph.clone());
		recognizer.init();
		recognizer.exec();
		return new SimpleActionGraph(recognizer.getActionGraph());
	}

}
