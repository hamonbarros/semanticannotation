package br.com.sann.service.search.dbpedia;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import br.com.sann.domain.DBpediaCategory;
import br.com.sann.domain.DBpediaClass;
import br.com.sann.domain.Extractor;
import br.com.sann.main.Main;
import br.com.sann.performance.test.ExpectedResult;
import br.com.sann.service.ontology.InvalidOntologyPathException;
import br.com.sann.service.ontology.OntologyParser;
import br.com.sann.service.ontology.ResourceNotFoundException;
import br.com.sann.service.processing.text.PreProcessingText;
import br.com.sann.service.search.wikipedia.WikipediaCategorySearcher;
import br.com.sann.util.Combination;

public class SearcherConceptysDBPedia {
	
	public static final String CATEGORY = "CATEGORY";
	public static final String CLASS = "CLASS";
	private PreProcessingText preProcessing;
	private String pathOwlDBPedia = "";
	
	/**
	 * Método construtor.
	 */
	public SearcherConceptysDBPedia() {
		preProcessing = new PreProcessingText();
		loadPropertyOwlDBPedia();
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
				extractor.setClasses(extractLabelClass(searcher.getClasses()));
				extractor.setCategories(extractLabelCategory(searcher.getCategories()));
				associateClassesAndCategories(extractor);
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
	 * Realiza a busca de super classes e super categorias referentes ao texto na dbpedia.
	 * 
	 * @param text O texto a ser consultado.
	 * @return O texto pré-processado e a lista de super classes e super categorias referentes ao mesmo.
	 */
	public List<Extractor> searchSuperClassesOrSuperCategories(String text) {
		
		List<Extractor> listReturn = new ArrayList<Extractor>();
		
		List<String> nounsAndAdjectives = preProcessing.preProcessing(text);
		
		String titleToken = preProcessing.tokensToString(nounsAndAdjectives);
		String titleTokenWithoutUppercase = preProcessing.tokenizingTextWithUppercase(titleToken);
		
		if (!nounsAndAdjectives.isEmpty()) {					
			SearcherDBpediaLookup searcher = new SearcherDBpediaLookup(titleTokenWithoutUppercase);
			if (!searcher.getClasses().isEmpty() || !searcher.getCategories().isEmpty()) {
							
				Extractor extractor = new Extractor();
				extractor.setTitle(titleTokenWithoutUppercase);
				extractor.setClasses(extractLabelClass(searcher.getClasses()));
				extractor.setCategories(extractLabelCategory(searcher.getCategories()));
				associateClassesAndCategories(extractor);
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
	
	public List<String> searchSuperClasses(List<DBpediaClass> dbPediaClasses) {
		
		List<String> result = new ArrayList<String>();
		for (DBpediaClass clas : dbPediaClasses) {
			String uri = clas.getUrl();
			SearcherParentConcepts searcher = new SearcherParentConcepts(uri);
		}
		
		return result;
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
			
		while (amountCombinationPossible > 0) {
			Combination c = new Combination(titleToken.split(" "), amountCombinationPossible);
			List<String> combinations = c.combine();
			for (String comb : combinations) {
				SearcherDBpediaLookup searcher = new SearcherDBpediaLookup(comb);
				if(!searcher.getClasses().isEmpty() || !searcher.getCategories().isEmpty()) {
			
					Extractor extractor = new Extractor();
					extractor.setTitle(comb);
					extractor.setCategories(extractLabelCategory(searcher.getCategories()));
					extractor.setClasses(extractLabelClass(searcher.getClasses()));
					associateClassesAndCategories(extractor);
					listReturn.add(extractor);
				}
			}
			amountCombinationPossible--;
		}			
		
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
					extractor.setCategories(extractLabelCategory(searcher.getCategories()));
					extractor.setClasses(extractLabelClass(searcher.getClasses()));
					associateClassesAndCategories(extractor);
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
			extractor.setClasses(extractLabelClass(searcher.getClasses()));
			extractor.setCategories(extractLabelCategory(searcher.getCategories()));
			associateClassesAndCategories(extractor);
			return extractor;
		}		
		return null;		
	}
	
	/**
	 * Método para extrair os labels das categorias.
	 * @param categories As categoias a serem exploradas.
	 * @return Os labels das categorias
	 */
	private Set<String>  extractLabelCategory(Set<DBpediaCategory> categories) {
		
		Set<String> result = new HashSet<String>();
		for (DBpediaCategory category : categories) {
			result.add(category.getLabel());
		}
		return result;
	}
	
	/**
	 * Método para extrair os labels das classes.
	 * @param categories As categoias a serem exploradas.
	 * @return Os labels das categorias
	 */
	private Set<String>  extractLabelClass(Set<DBpediaClass> clas) {
		
		Set<String> result = new HashSet<String>();
		for (DBpediaClass c : clas) {
			result.add(c.getLabel());
		}
		return result;
	}
	
	/**
	 * Método para carregar a propriedade que identifica a localização do
	 * OWL da DBPedia.
	 */
	private void loadPropertyOwlDBPedia() {
		try {
			InputStream in = new Main().getClass().getClassLoader().getResourceAsStream("config.properties");  
			Properties props = new Properties();  
			props.load(in);
			in.close();
			pathOwlDBPedia = props.getProperty("PATH_OWL_DBPEDIA");
		} catch (IOException e) {
			System.err.println("Não foi possíviel localizar o OWL da DBPedia.");
		}
	}
	
	/**
	 * Recupera os conceitos associados com um determinado conceito.
	 * 
	 * @param concept O conceito a ser verificado.
	 * @return A lista de conceitos associados.
	 */
	private List<String> recoveryAssociateConcepts(String concept) {
		
		List<String> result = new ArrayList<String>();
		try {
			OntologyParser parser = new OntologyParser(pathOwlDBPedia,
					OntologyParser.LOCAL_FILE_MODE, OntologyParser.SIMPLE_MODEL);
			result.addAll(parser.listSuperclasses(concept));
		} catch (InvalidOntologyPathException e) {
			System.err.println("Não foi possível carregar o OWL da DBPedia.");;
		} catch (ResourceNotFoundException e) {
			System.err.println("Não foi possível encontrar recursos referente ao conceito " + concept);
		}
		return result;
	}
	
	/**
	 * Método para acrescentar os conceitos associados ao conceitos.
	 * @param concepts Os conceitos.
	 * @return Os conceitos e os seus respectivos associados.
	 */
	private Set<String> associateConcepts(Set<String> concepts) {
		
		Set<String> result = new HashSet<String>();
		result.addAll(concepts);
		for (String concept : concepts) {
			result.addAll(recoveryAssociateConcepts(concept.replaceAll(" ", "")));
		}
		return result;		
	}
	
	/**
	 * Recupera as categorias associadas com uma determinada categoria.
	 * 
	 * @param category A categoria a ser verificada.
	 * @return o conjunto de categorias associadas.
	 */
	private Set<String> recoveryAssociateCategories(String category) {
		
		Set<String> result = new HashSet<String>();
		try {
			WikipediaCategorySearcher searcher = new WikipediaCategorySearcher();
			result.addAll(searcher.getCategoryPages(category));
		} catch (Exception e) {
			System.err.println("Não foi possível encontrar recursos referente à categoria " + category);
		}
		return result;
	}
	/**
	 * Método para acrescentar as categorias associadas às categorias recuperadas.
	 * @param concepts Os conceitos.
	 * @return Os conceitos e os seus respectivos associados.
	 */
	private Set<String> associateCategories(Set<String> categories) {
		
		Set<String> result = new HashSet<String>();
		result.addAll(categories);
		for (String category : categories) {
			result.addAll(recoveryAssociateCategories(category));
		}
		return result;		
	}
	
	/**
	 * Acrecenta os conceitos associadosas classes e categorias.
	 * 
	 * @param extractor O extrator.
	 */
	public void associateClassesAndCategories(Extractor extractor) {
		
//		if (!extractor.getClasses().isEmpty()) {
//			Set<String> classesAndAssociated = associateConcepts(extractor.getClasses());
//			extractor.setClasses(classesAndAssociated);
//		}
//		if (!extractor.getCategories().isEmpty()) {
//			Set<String> categoriesAndAssociated = associateCategories (extractor.getCategories());
//			extractor.setCategories(categoriesAndAssociated);
//		}
	}
	
	public static void main(String[] args) {
		SearcherConceptysDBPedia s = new SearcherConceptysDBPedia();
		List<Extractor> extractorList = s.searchClassesOrCategories("Island");
		for (Extractor extractor : extractorList) {
			System.out.println(extractor.getTitle());
			System.out.println("Classes " + extractor.getClasses().size());
			System.out.println("Categorias " + extractor.getCategories().size());
			System.out.println();
		}
	}

}
