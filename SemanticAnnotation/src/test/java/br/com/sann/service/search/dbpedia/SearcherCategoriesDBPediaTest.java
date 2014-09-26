package br.com.sann.service.search.dbpedia;

import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

public class SearcherCategoriesDBPediaTest extends TestCase {
	
	private SearcherCategoriesDBPedia seacher;
	
	public void setUp() throws Exception {
		super.setUp();
		seacher = new SearcherCategoriesDBPedia();
	}

	public void testSearchCategoriesSucess() {
		Map<String, Map<String, Set<String>>> retorno = seacher.searchClassesOrCategories("Aboriginal Lands");
		assertFalse(retorno.isEmpty());
	}
	
	public void testSearchCategoriesFail() {
		Map<String, Map<String, Set<String>>> retorno = seacher.searchClassesOrCategories("Reeeerrrrewq");
		assertTrue(retorno.get(retorno.keySet().iterator().next()).isEmpty());
	}

	public void testSearchCombination() {
		Map<String, Map<String, Set<String>>> retorno = seacher.searchCombination("Aboriginal Lands Water", 2);
		assertFalse(retorno.isEmpty());
	}

}
