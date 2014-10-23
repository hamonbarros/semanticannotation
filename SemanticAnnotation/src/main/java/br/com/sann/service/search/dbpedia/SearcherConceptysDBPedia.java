package br.com.sann.service.search.dbpedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.sann.domain.Extractor;
import br.com.sann.service.processing.text.PreProcessingText;
import br.com.sann.util.Combination;

public class SearcherConceptysDBPedia {
	
	public static final String CATEGORY = "CATEGORY";
	public static final String CLASS = "CLASS";
	private PreProcessingText preProcessing;
	
	/**
	 * Método construtor.
	 */
	public SearcherConceptysDBPedia() {
		preProcessing = new PreProcessingText();
	}

	/**
	 * Realiza a busca de classes e categorias referentes ao texto na dbpedia.
	 * 
	 * @param text O texto a ser consultado.
	 * @return O texto pré-processado e a lista de classes e categorias referentes ao mesmo.
	 */
	public List<Extractor> searchClassesOrCategories(String text) {
		
		List<Extractor> listReturn = new ArrayList<Extractor>();
		
		List<String> nounsAndAdjectives = preProcessing.preProcessing(text);
		
		String titleToken = preProcessing.tokensToString(nounsAndAdjectives);
		String titleTokenWithoutUppercase = preProcessing.tokenizingTextWithUppercase(titleToken);
		
		if (!nounsAndAdjectives.isEmpty()) {					
			SearcherDBpediaLookup searcher = new SearcherDBpediaLookup(titleTokenWithoutUppercase);
			if (!searcher.getClasses().isEmpty() || !searcher.getCategories().isEmpty()) {
							
				Extractor extractor = new Extractor();
				extractor.setTitle(titleTokenWithoutUppercase);
				extractor.setClasses(searcher.getClasses());
				extractor.setCategories(searcher.getCategories());
				listReturn.add(extractor);
				
			} else {
				int amountCombinationPossible = nounsAndAdjectives.size()-1;
				listReturn = searchCombination(titleTokenWithoutUppercase, amountCombinationPossible);	
				if (listReturn.isEmpty()) {
					Extractor extractor = searchText(text);
					if (extractor != null) {
						listReturn.add(extractor);
					}					
				}
			}
		}
		
		return listReturn;
	}
	
	/**
	 * Método que faz a busca das classes e categorias na dbpedia a partir de combinações possíveis dos tokens 
	 * até encontrar alguma ou esgotar as possibilidades de combinações.
	 * 
	 * @param titleToken O texto tokenizado.
	 * @param amountCombinationPossible A quantidade de combinações possíveis.
	 * @return Um lista de extratores contento o texto tokenizado e as suas respectivas classes e categorias.
	 */
	public List<Extractor> searchCombination(String titleToken, int amountCombinationPossible) {
		
		List<Extractor> listReturn = new ArrayList<Extractor>();
			
		boolean find = false;
		while (!find && amountCombinationPossible > 0) {
			Combination c = new Combination(titleToken.split(" "), amountCombinationPossible);
			List<String> combinations = c.combine();
			for (String comb : combinations) {
				SearcherDBpediaLookup searcher = new SearcherDBpediaLookup(comb);
				if(!searcher.getClasses().isEmpty() || !searcher.getCategories().isEmpty()) {
					find = true;
			
					Extractor extractor = new Extractor();
					extractor.setTitle(comb);
					extractor.setCategories(searcher.getCategories());
					extractor.setClasses(searcher.getClasses());
					listReturn.add(extractor);
				}
			}
			amountCombinationPossible--;
		}			
//		if (find && amountCombinationPossible >= 1) {
//			listReturn.addAll(searchRemainingTerms(titleToken, listReturn));
//		}
		
		return listReturn;
	}
	
	/**
	 * Método para buscar os termos do título que ainda não foram resolvidos.
	 * 
	 * @param titleToken
	 * @param listReturn
	 * @return A lista de extratores recuperados.
	 */
	private List<Extractor> searchRemainingTerms(String title,
			List<Extractor> listReturn) {

		List<Extractor> extractors = new ArrayList<Extractor>();
		
		PreProcessingText preprocessing = new PreProcessingText();
		List<String> titleTokens = preprocessing.tokenizingText(title);
		Set<String> returnTokens = preprocessing.tokenizingTextList(getTitles(listReturn));
		for (String token : titleTokens) {
			if (!returnTokens.contains(token)) {
				SearcherDBpediaLookup searcher = new SearcherDBpediaLookup(token);
				if(!searcher.getClasses().isEmpty() || !searcher.getCategories().isEmpty()) {			
					Extractor extractor = new Extractor();
					extractor.setTitle(token);
					extractor.setCategories(searcher.getCategories());
					extractor.setClasses(searcher.getClasses());
					extractors.add(extractor);
				}				
			}
		}
		return extractors;
	}

	/**
	 * Extrai os títulos dos extratores de uma lista.
	 * 
	 * @param extractors A lista de extratores.
	 * @return A lista de títulos dos extratores.
	 */
	private List<String> getTitles(List<Extractor> extractors) {
		
		List<String> listResult = new ArrayList<String>();
		for (Extractor extractor : extractors) {
			listResult.add(extractor.getTitle());
		}
		
		return listResult;
	}

	/**
	 * Faz a busca na dbpedia pelos substantivos e adjetivos do texto.
	 * @param text O texto a ser pesquisado.
	 * @return Um extrator contento o titulo passado e as suas respectivas classes/categorias.
	 */
	public Extractor searchText(String text) {
		
		List<String> tokens = preProcessing.tokenizingText(text);
		List<String> nounsAndAjectives = preProcessing.extractNounsAndAdjectives(tokens);
		String title = preProcessing.tokensToString(nounsAndAjectives);
		
		SearcherDBpediaLookup searcher = new SearcherDBpediaLookup(title);		
		if (!searcher.getClasses().isEmpty() || !searcher.getCategories().isEmpty()) {		
			Extractor extractor = new Extractor();
			extractor.setTitle(title);
			extractor.setClasses(searcher.getClasses());
			extractor.setCategories(searcher.getCategories());
			return extractor;
		}		
		return null;		
	}
	
}
