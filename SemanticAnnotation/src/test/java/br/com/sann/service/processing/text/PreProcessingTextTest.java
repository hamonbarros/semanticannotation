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

		assertTrue(tokens.containsAll(preProcessing.preProcessing("Aboriginal Lands")));
		System.out.println(preProcessing.preProcessing("Conservation Areas"));
		
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

	public void testTokenizingTextWithUppercase() {
		String text = "GeologicFormationURI";
		assertEquals("Geologic Formation URI", preProcessing.tokenizingTextWithUppercase(text));
		
		text = "GeologicFormationURITest";
		assertEquals("Geologic Formation URI Test", preProcessing.tokenizingTextWithUppercase(text));
		
		text = "geologicFormationType";
		assertEquals("geologic Formation Type", preProcessing.tokenizingTextWithUppercase(text));
		
		text = "geo";
		assertEquals("geo", preProcessing.tokenizingTextWithUppercase(text));
		
		text = "";
		assertEquals("", preProcessing.tokenizingTextWithUppercase(text));
		
		text = null;
		assertEquals(null, preProcessing.tokenizingTextWithUppercase(text));
	}
	
	public void testStemming() {
		String text = "Area del contorno del embalse";
		assertEquals("Area del contorno del embals", preProcessing.stemming(text));
		
		text = "Corrected Value";
		assertEquals("Correct Valu", preProcessing.stemming(text));
		
		text = "Diameter";
		assertEquals("Diamet", preProcessing.stemming(text));
	}

	public void testExtractPunctuation() {
		String text = "Area:, (del.) [contorno]];., {{del: (embalse.) [Area del] contorno;. {del} embals,}";
		assertEquals("Area del contorno del embalse Area del contorno del embals", preProcessing.extractPunctuation(text));
	}
}
