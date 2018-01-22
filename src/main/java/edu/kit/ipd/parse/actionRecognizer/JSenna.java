package edu.kit.ipd.parse.actionRecognizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import edu.kit.ipd.parse.senna_wrapper.Senna;
import edu.kit.ipd.parse.senna_wrapper.WordSennaResult;

public class JSenna {

	//The output of senna according to the input sentence
	private LinkedList<String[]> result;

	public JSenna() {
		this.result = new LinkedList<String[]>();
	}

	private File getTempFilePath(String input) {
		File temp = null;
		try {
			temp = File.createTempFile("tempfile", ".tmp");

			BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
			bw.write(input);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * Turns a sentence into a table of information for each token in the
	 * sentence
	 *
	 * @param input
	 *            the sentence
	 * @return a table of information for each token in the sentence as String
	 */
	public String enter(String input) {
		File tmpFile = getTempFilePath(input);
		Senna sw = new Senna(new String[] { "-usrtokens" });
		List<WordSennaResult> out = null;
		try {
			out = sw.parse(tmpFile);
		} catch (IOException | URISyntaxException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String results = "";

		for (WordSennaResult wordSennaResult : out) {
			results += wordSennaResult.getWord() + " " + wordSennaResult.getAnalysisResults() + "\n";
			String[] resArray = new String[wordSennaResult.getAnalysisResults().length + 1];
			resArray[0] = wordSennaResult.getWord();
			for (int i = 1; i < resArray.length; i++) {
				resArray[i] = wordSennaResult.getAnalysisResults()[i - 1];
			}
			result.add(resArray);
		}

		//        Process process = executeProcess(input);
		//        Scanner scanner = new Scanner(process.getInputStream());
		//        String results = "";
		//        while (scanner.hasNext()) {
		//            String nextLine = scanner.nextLine();
		//            results += nextLine + "\n";
		//            this.result.add(nextLine.split("\\s+"));
		//        }
		//        scanner.close();
		return results;
	}

	/**
	 * Returns the ouput of Senna in a List of String array
	 *
	 * @return the output of Senna
	 */
	public LinkedList<String[]> getResults() {
		return this.result;
	}

	//    private Process executeProcess(String input) {
	//        String[] command = { "/bin/bash", "-c",
	//                "/Users/ouyue/Dropbox/0_Bachelorarbeit/senna/senna -path /Users/ouyue/Dropbox/0_Bachelorarbeit/senna/ < "
	//                        + getTempFilePath(input) };
	//        ProcessBuilder pb = new ProcessBuilder(command);
	//        Process process = null;
	//        try {
	//            process = pb.start();
	//            process.waitFor();
	//        } catch (IOException | InterruptedException e) {
	//            e.printStackTrace();
	//        }
	//        return process;
	//    }

}
