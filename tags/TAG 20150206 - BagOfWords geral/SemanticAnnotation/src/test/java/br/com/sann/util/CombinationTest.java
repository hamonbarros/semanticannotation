package br.com.sann.util;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Classe de teste de {@link Combination}.
 * 
 * @author Hamon
 *
 */
public class CombinationTest extends TestCase {

	private Combination combination;
	
	/**
	 * M�todo de inicializa��o do teste.
	 */
	@Override
	protected void setUp() throws Exception {
		
		super.setUp();
		
		String[] listStr = {"Geospatial", "Infrastructure", "Semantic", "Annotation"};
		combination = new Combination(listStr, 3);
	}

	/**
	 * M�todo de finaliza��o do teste.
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		combination = null;
	}

	/**
	 * Testa o m�todo hasNext();
	 */
	public void testHasNext() {
		assertTrue(combination.hasNext());
		for (int i = 0; i < 8; i++) {
			combination.next();
		}
		assertFalse(combination.hasNext());
	}

	/**
	 * Testa o m�todo getOutLength.
	 */
	public void testGetOutLength() {
		assertEquals(3, combination.getOutLength());
	}

	/**
	 * Testa o m�todo next().
	 */
	public void testNext() {
		String[] next = combination.next();
		next = combination.next();
		assertEquals("Infrastructure", next[0]);
	}
	
	/**
	 * Testa o m�todo combine();
	 */
	public  void testCombine() {
		List<String> combs = combination.combine();
		assertEquals(4, combs.size());
		List<String> combinations = new LinkedList<String>();
		combinations.add("Geospatial Infrastructure Semantic");
		combinations.add("Geospatial Semantic Annotation");
		combinations.add("Geospatial Infrastructure Annotation");
		combinations.add("Geospatial Infrastructure Semantic");
		combinations.add("Infrastructure Semantic Annotation");
		assertTrue(combs.containsAll(combinations));
	}

	/**
	 * Testa o m�todo combineVizinhos().
	 */
	public void testCombineVizinhos() {
		String[] listStr = {"Boundary", "Major", "Drainag", "Water", "Survey", "Canada"};
		combination = new Combination(listStr, 4);
		List<String> combs = combination.combineVizinhos();
		assertEquals(3, combs.size());
		List<String> combinations = new LinkedList<String>();
		combinations.add("Boundary Major Drainag Water");
		combinations.add("Major Drainag Water Survey");
		combinations.add("Drainag Water Survey Canada");
		assertTrue(combs.containsAll(combinations));

		combination = new Combination(listStr, 3);
		combs = combination.combineVizinhos();
		assertEquals(4, combs.size());

		combination = new Combination(listStr, 2);
		combs = combination.combineVizinhos();
		assertEquals(5, combs.size());

		combination = new Combination(listStr, 1);
		combs = combination.combineVizinhos();
		assertEquals(6, combs.size());

	}
}
