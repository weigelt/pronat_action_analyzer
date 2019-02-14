package edu.kit.ipd.parse.actionRecognizer;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.parse.graphBuilder.GraphBuilder;
import edu.kit.ipd.parse.luna.data.MissingDataException;
import edu.kit.ipd.parse.luna.data.PrePipelineData;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.pipeline.PipelineStageException;
import edu.kit.ipd.parse.luna.tools.StringToHypothesis;
import edu.kit.ipd.parse.ner.NERTagger;
import edu.kit.ipd.parse.shallownlp.ShallowNLP;
import edu.kit.ipd.parse.srlabeler.SRLabeler;

public class SRLCorrectionTest {

	private static ShallowNLP snlp;
	private static GraphBuilder graphBuilder;
	private static SRLabeler srLabeler;
	private static IGraph pg;
	private static ActionRecognizer ar;
	private static NERTagger nert;
	private PrePipelineData ppd;

	@BeforeClass
	public static void setUp() {
		graphBuilder = new GraphBuilder();
		graphBuilder.init();
		srLabeler = new SRLabeler();
		srLabeler.init();
		snlp = new ShallowNLP();
		snlp.init();
		nert = new NERTagger();
		nert.init();
		ar = new ActionRecognizer();
		ar.init();
	}

	private static IGraph executePreviousStages(PrePipelineData ppd) {
		try {
			snlp.exec(ppd);
			srLabeler.exec(ppd);
			graphBuilder.exec(ppd);
			nert.exec(ppd);
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

}
