package br.com.sann.service.search.dbpedia;

import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.sann.domain.Extractor;

import junit.framework.TestCase;

public class SearcherCategoriesDBPediaTest extends TestCase {
	
	private SearcherConceptysDBPedia seacher;
	
	public void setUp() throws Exception {
		super.setUp();
		seacher = new SearcherConceptysDBPedia();
	}

	public void testSearchCategoriesSucess() {
		List<Extractor> retorno = seacher.searchClassesOrCategories("Aboriginal Lands");
		assertFalse(retorno.isEmpty());
	}
	
	public void testSearchCategoriesFail() {
		List<Extractor> retorno = seacher.searchClassesOrCategories("Reeeerrrrewq");
		assertTrue(retorno.isEmpty());
	}

	public void testSearchCombination() {
		List<Extractor> retorno = seacher.searchCombination("Aboriginal Lands Water", 2);
		assertFalse(retorno.isEmpty());
	}

}
