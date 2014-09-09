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
	 * M�todo construtor.
	 */
	public SearcherCategoriesDBPedia() {
		preProcessing = new PreProcessingText();
	}

	/**
	 * Realiza a busca de categorias referentes ao texto na dbpedia.
	 * 
	 * @param text O texto a ser consultado.
	 * @return O texto pr�-processado e a lista de categorias referentes ao mesmo.
	 */
	public Map<String, Set<String>> searchClassesOrCategories(String text) {
		
		Map<String, Set<String>> retorno = new HashMap<String, Set<String>>();
		
		List<String> nounsAndAdjectives = preProcessing.preProcessing(text);
		String titleToken = preProcessing.tokensToString(nounsAndAdjectives);
		titleToken = preProcessing.tokenizingTextWithUppercase(titleToken);
		
		if (!nounsAndAdjectives.isEmpty()) {					
			SearcherDBpediaLookup seacher = new SearcherDBpediaLookup(titleToken);
			if (!seacher.getClasses().isEmpty() || !seacher.getCategories().isEmpty()) {
				if (!seacher.getClasses().isEmpty()) {
					retorno.put(titleToken, seacher.getClasses());
				} else {					
					retorno.put(titleToken, seacher.getCategories());
				}
			} else {
				int amountCombinationPossible = nounsAndAdjectives.size()-1;
				retorno = searchCombination(titleToken, amountCombinationPossible);					
				if (retorno.isEmpty()) {
					retorno.put(titleToken, new LinkedHashSet<String>());
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * M�todo que faz a busca das categorias na dbpedia a partir de combina��es poss�veis dos tokens 
	 * at� encontrar alguma categoria ou esgotar as possibilidades de combina��es.
	 * 
	 * @param titleToken O texto tokenizado.
	 * @param amountCombinationPossible A quantidade de combina��es poss�veis.
	 * @return Um mapa contento o texto tokenizado e as suas respectivas categorias.
	 */
	public Map<String, Set<String>> searchCombination(String titleToken, int amountCombinationPossible) {
		
		Map<String, Set<String>> retorno = new HashMap<String, Set<String>>();
		
		boolean find = false;
		while (!find && amountCombinationPossible > 0) {
			Combination c = new Combination(titleToken.split(" "), amountCombinationPossible);
			List<String> combinations = c.combine();
			for (String comb : combinations) {
				SearcherDBpediaLookup searcher = new SearcherDBpediaLookup(comb);
				if(!searcher.getClasses().isEmpty() || !searcher.getCategories().isEmpty()) {
					find = true;
					if (!searcher.getClasses().isEmpty()) {
						retorno.put(comb, searcher.getClasses());
					} else {						
						retorno.put(comb, searcher.getCategories());
					}
				}
			}
			amountCombinationPossible--;
		}	
		return retorno;
	}
}