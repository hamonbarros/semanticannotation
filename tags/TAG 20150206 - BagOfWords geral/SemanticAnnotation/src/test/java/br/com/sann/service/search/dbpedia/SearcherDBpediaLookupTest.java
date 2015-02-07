package br.com.sann.service.search.dbpedia;

import junit.framework.TestCase;

public class SearcherDBpediaLookupTest extends TestCase {

	public void testGetCategoriesSucess() {
		SearcherDBpediaLookup sc = new SearcherDBpediaLookup("Water");
		assertFalse(sc.getCategories().isEmpty());
	}

	public void testGetCategoriesFail() {
		SearcherDBpediaLookup sc = new SearcherDBpediaLookup("RRRRGGGGGSSSS");
		assertTrue(sc.getCategories().isEmpty());
	}

}
