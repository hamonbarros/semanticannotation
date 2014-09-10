package br.com.sann.service.search.dbpedia;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.sann.service.processing.text.PreProcessingText;
import br.com.sann.util.Combination;

public class SearcherCategoriesDBPedia {
	
	private PreProcessingText preProcessing;
	
	/**
	 * Método construtor.
	 */
	public SearcherCategoriesDBPedia() {
		preProcessing = new PreProcessingText();
	}

	/**
	 * Realiza a busca de categorias referentes ao texto na dbpedia.
	 * 
	 * @param text O texto a ser consultado.
	 * @return O texto pré-processado e a lista de categorias referentes ao mesmo.
	 */
	public Map<String, Set<String>> searchClassesOrCategories(String text) {
		
		Map<String, Set<String>> retorno = new HashMap<String, Set<String>>();
		
		List<String> nounsAndAdjectives = preProcessing.preProcessing(text);
		String titleToken = preProcessing.tokensToString(nounsAndAdjectives);
		String titleTokenWithoutUppercase = preProcessing.tokenizingTextWithUppercase(titleToken);
		
		if (!nounsAndAdjectives.isEmpty()) {					
			SearcherDBpediaLookup seacher = new SearcherDBpediaLookup(titleTokenWithoutUppercase);
			if (!seacher.getClasses().isEmpty() || !seacher.getCategories().isEmpty()) {
				if (!seacher.getClasses().isEmpty()) {
					retorno.put(titleTokenWithoutUppercase, seacher.getClasses());
				} else {					
					retorno.put(titleTokenWithoutUppercase, seacher.getCategories());
				}
			} else {
				int amountCombinationPossible = nounsAndAdjectives.size()-1;
				retorno = searchCombination(titleTokenWithoutUppercase, amountCombinationPossible);	
				if (retorno.isEmpty()) {
					retorno = searchText(text);
				}
				if (retorno.isEmpty()) {
					retorno.put(titleTokenWithoutUppercase, new LinkedHashSet<String>());
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método que faz a busca das categorias na dbpedia a partir de combinações possíveis dos tokens 
	 * até encontrar alguma categoria ou esgotar as possibilidades de combinações.
	 * 
	 * @param titleToken O texto tokenizado.
	 * @param amountCombinationPossible A quantidade de combinações possíveis.
	 * @return Um mapa contento o texto tokenizado e as suas respectivas categorias.
	 */
	public Map<String, Set<String>> searchCombination(String titleToken, int amountCombinationPossible) {
		
		Map<String, Set<String>> mapReturn = new HashMap<String, Set<String>>();
		
		boolean find = false;
		while (!find && amountCombinationPossible > 0) {
			Combination c = new Combination(titleToken.split(" "), amountCombinationPossible);
			List<String> combinations = c.combine();
			for (String comb : combinations) {
				SearcherDBpediaLookup searcher = new SearcherDBpediaLookup(comb);
				if(!searcher.getClasses().isEmpty() || !searcher.getCategories().isEmpty()) {
					find = true;
					if (!searcher.getClasses().isEmpty()) {
						mapReturn.put(comb, searcher.getClasses());
					} else {						
						mapReturn.put(comb, searcher.getCategories());
					}
				}
			}
			amountCombinationPossible--;
		}	
		return mapReturn;
	}
	
	/**
	 * Faz a busca na dbpedia pelos substantivos e adjetivos do texto.
	 * @param text O texto a ser pesquisado.
	 * @return O mapa do título e o cojunto de classes ou categorias da dbpedia.
	 */
	public Map<String, Set<String>> searchText(String text) {
		
		Map<String, Set<String>> mapReturn = new HashMap<String, Set<String>>();
		List<String> tokens = preProcessing.tokenizingText(text);
		List<String> nounsAndAjectives = preProcessing.extractNounsAndAdjectives(tokens);
		String title = preProcessing.tokensToString(nounsAndAjectives);
		
		SearcherDBpediaLookup seacher = new SearcherDBpediaLookup(title);		
		if (!seacher.getClasses().isEmpty() || !seacher.getCategories().isEmpty()) {
			if (!seacher.getClasses().isEmpty()) {
				mapReturn.put(title, seacher.getClasses());
			} else {					
				mapReturn.put(title, seacher.getCategories());
			}
		}		
		return mapReturn;		
	}
	
}
