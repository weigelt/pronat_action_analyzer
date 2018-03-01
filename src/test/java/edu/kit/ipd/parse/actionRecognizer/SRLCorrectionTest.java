package edu.kit.ipd.parse.actionRecognizer;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.parse.graphBuilder.GraphBuilder;
import edu.kit.ipd.parse.luna.data.MissingDataException;
import edu.kit.ipd.parse.luna.data.PrePipelineData;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.graph.INodeType;
import edu.kit.ipd.parse.luna.pipeline.PipelineStageException;
import edu.kit.ipd.parse.luna.tools.StringToHypothesis;
import edu.kit.ipd.parse.shallownlp.ShallowNLP;
import edu.kit.ipd.parse.srlabeler.SRLabeler;

public class SRLCorrectionTest {

	private static ShallowNLP snlp;
	private static GraphBuilder graphBuilder;
	private static SRLabeler srLabeler;
	private PrePipelineData ppd;
	private static IGraph pg;
	private static INodeType nodeType;
	private static ActionRecognizer ar;

	@BeforeClass
	public static void setUp() {
		graphBuilder = new GraphBuilder();
		graphBuilder.init();
		srLabeler = new SRLabeler();
		srLabeler.init();
		snlp = new ShallowNLP();
		snlp.init();
		ar = new ActionRecognizer();
		ar.init();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSRLCorrectionThemeRole() {
		ppd = new PrePipelineData();
		ppd.setMainHypothesis(StringToHypothesis.stringToMainHypothesis("the robot grabs a cup meanwhile the cat goes to the fridge"));
		pg = executePreviousStages(ppd);
		ar.setGraph(pg);
		ar.exec();
		System.out.println(ar.getGraph().showGraph());
		for (INode node : ar.getGraph().getNodes()) {
			System.out.println(node.toString());
		}

	}

	private IGraph executePreviousStages(PrePipelineData ppd) {
		try {
			snlp.exec(ppd);
			srLabeler.exec(ppd);
			graphBuilder.exec(ppd);
		} catch (PipelineStageException e) {
			e.printStackTrace();
		}

		try {
			return ppd.getGraph();
		} catch (MissingDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
