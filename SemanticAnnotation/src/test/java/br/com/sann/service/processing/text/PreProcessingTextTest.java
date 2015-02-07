package br.com.sann.service.processing.text;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class PreProcessingTextTest extends TestCase {

	private PreProcessingText preProcessing;
	private List<String> tokens;
	
	protected void setUp() throws Exception {
		super.setUp();
		preProcessing = new PreProcessingText();
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

}
