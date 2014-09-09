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
	 * Método de inicialização do teste.
	 */
	@Override
	protected void setUp() throws Exception {
		
		super.setUp();
		
		String[] listStr = {"Geospatial", "Infrastructure", "Semantic", "Annotation"};
		combination = new Combination(listStr, 3);
	}

	/**
	 * Método de finalização do teste.
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		combination = null;
	}

	/**
	 * Testa o método hasNext();
	 */
	public void testHasNext() {
		assertTrue(combination.hasNext());
		for (int i = 0; i < 8; i++) {
			combination.next();
		}
		assertFalse(combination.hasNext());
	}

	/**
	 * Testa o método getOutLength.
	 */
	public void testGetOutLength() {
		assertEquals(3, combination.getOutLength());
	}

	/**
	 * Testa o método next().
	 */
	public void testNext() {
		String[] next = combination.next();
		next = combination.next();
		assertEquals("Infrastructure", next[0]);
	}
	
	/**
	 * Testa o método combine();
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

}
