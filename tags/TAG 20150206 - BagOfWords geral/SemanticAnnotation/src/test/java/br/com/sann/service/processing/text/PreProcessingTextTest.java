package br.com.sann.service.processing.text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class PreProcessingTextTest extends TestCase {

	private PreProcessingText preProcessing;
	private List<String> tokens;
	
	protected void setUp() throws Exception {
		super.setUp();
		preProcessing = PreProcessingText.getInstance();
		tokens = new LinkedList<String>();
		tokens.add("Aborigin");
		tokens.add("Land");
	}

	public void testPreProcessing() {

		assertEquals(tokens, preProcessing.preProcessing("Aboriginal Lands"));
		
	}

	public void testTokensToString() {

		assertEquals("Aborigin Land", preProcessing.tokensToString(tokens));
	}
	
	public void testGetWordsWithYFinish() {
		List<String> result = preProcessing.getWordsWithYFinish("boundaries germany river ocean survay");
		assertEquals(3, result.size());
		
		result = preProcessing.getWordsWithYFinish("coastal England river ocean");
		assertEquals(0, result.size());
		
		result = preProcessing.getWordsWithYFinish(null);
		assertEquals(0, result.size());
	}
	
	public void testReplaceSimilarWordWithYFinish() {
		List<String> tokens = new ArrayList<String>();
		tokens.add("boundari");
		tokens.add("germani");
		tokens.add("river");
		tokens.add("ocean");
		tokens.add("surva");
		
		String word = "boundary";
		preProcessing.replaceSimilarWordWithYFinish(word, tokens);
		assertEquals(word, tokens.get(0));
		
		word = "germany";
		preProcessing.replaceSimilarWordWithYFinish(word, tokens);
		assertEquals(word, tokens.get(1));		
	
		word = "survay";
		preProcessing.replaceSimilarWordWithYFinish(word, tokens);
		assertEquals(word, tokens.get(4));
		
		System.out.println(tokens);
	}

}
