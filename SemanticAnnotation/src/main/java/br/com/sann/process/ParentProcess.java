package br.com.sann.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.domain.Extractor;
import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SemanticAnnotation;
import br.com.sann.domain.SemanticAnnotationAttribute;
import br.com.sann.domain.SpatialData;
import br.com.sann.domain.Sumary;
import br.com.sann.service.FeatureService;
import br.com.sann.service.OntologyConceptService;
import br.com.sann.service.impl.FeatureServiceImpl;
import br.com.sann.service.impl.OntologyConceptServiceImpl;
import br.com.sann.service.impl.SemanticAnnotationServiceImpl;
import br.com.sann.service.processing.text.PreProcessingText;
import br.com.sann.service.search.dbpedia.SearcherConceptysDBPedia;
import br.com.sann.service.search.wikipedia.SearcherWikipedia;
import br.com.sann.service.similarity.CosineDocumentSimilarity;
import br.com.sann.util.Combination;
import br.com.sann.util.ParserHtml;
import br.com.sann.util.ParserHtmlException;

public class ParentProcess {

	private static final String TERM_YAGO = "yago";
	private static final double THRESHOLD_COSINE = 0.1;
	
	private PreProcessingText preprocessing;
	
	public ParentProcess() {
		preprocessing = PreProcessingText.getInstance();
	}
	
	/**
	 * Método que faz o processamento necessário realizar a anotação semântica via casamento
	 * de strings.
	 * 
	 * @param spatialData O feture type.
	 * @param title O título do feature type.
	 * @param bagOfWords O texto a ser comparado com a bagOfWords.
	 * @param outConsolidated Arquivo onde estão sendo impressos os resultados consolidados.
	 * @param out Arquivo onde estão sendo impressos os resultados resumidos.
	 * @param sumary 
	 * @param concepts 
	 * @return 1 se o titulo não possuir nenhum conceito relevante, ou 0, caso contrário.
	 * @throws IOException Exceção lançada de IO.
	 */
	public void executeSannWords(List<Extractor> extractorList, SpatialData spatialData, String title, PrintWriter out, 
			Sumary sumary) throws IOException {
		
		List<String> classesUpThreshold = new LinkedList<String>();
		List<String> categoriesUpThreshold = new LinkedList<String>();
		FeatureService featureService = new FeatureServiceImpl();
		
		if(!extractorList.isEmpty()) {
			out.println("------------------------------------------------------------------------------------");
			out.println("Título do Feature Type: " + title);
			out.println();
			boolean wasAnnotated = false;
			for (Extractor extractor : extractorList) {

				if (!extractor.getSimilarityClasses().isEmpty()) {
					extractor.setOntologyClasses(getSimilaryConcepts(extractor.getSimilarityClasses(), 
							extractor.getTitle()));
					classesUpThreshold.addAll(extractor.getSimilarityClasses());
				}
				if (!extractor.getSimilarityCategories().isEmpty()) {
					extractor.setOntologyCategories(getSimilaryConcepts(extractor.getSimilarityCategories(), 
							extractor.getTitle()));
					categoriesUpThreshold.addAll(extractor.getSimilarityCategories());
				}
				
				if (!extractor.getSimilarityClasses().isEmpty() || !extractor.getSimilarityCategories().isEmpty()) {
					out.println("Token: " + extractor.getTitle());
					out.println("Categorias: " + printStringList(extractor.getSimilarityCategories()));
					out.println("Categorias similares: " + printStringList(extractConceptNames(extractor.getOntologyCategories())));
					out.println("Conceitos: " + printStringList(extractor.getSimilarityClasses()));
					out.println("Conceitos similares: " + printStringList(extractConceptNames(extractor.getOntologyClasses())));
					out.println("");
				}
				sumary.summarizeResults(extractor);
				if(!extractor.getOntologyClasses().isEmpty() || !extractor.getOntologyCategories().isEmpty()) {
					annotatefeatures(extractor, spatialData);
					wasAnnotated = true;
				}
			}
			sumary.setCountFeature(1);
			if(!wasAnnotated) {
				sumary.setCountFeatureNotAnnotated(1);
			} else {
				spatialData.setAnnotated(Boolean.TRUE);
				spatialData.setTypeAnnotation(SpatialData.SANN_WORDS);
				featureService.updateSpatialData(spatialData);				
			}
			out.println("--------------------------------------");
			out.println("Features anotados: " + (sumary.getCountFeature() - sumary.getCountFeatureNotAnnotated()));
			out.println("Features não anotados: " + sumary.getCountFeatureNotAnnotated());
			out.println("--------------------------------------");
			out.println("");
			out.flush();
		}		
	}	
	
	/**
	 * Método que faz o processamento necessário para extrair a similaridade entre uma 
	 * bagOfWords e um determinado texto.
	 * 
	 * @param spatialData O feture type.
	 * @param title O título do feature type.
	 * @param bagOfWords O texto a ser comparado com a bagOfWords.
	 * @param outConsolidated Arquivo onde estão sendo impressos os resultados consolidados.
	 * @param out Arquivo onde estão sendo impressos os resultados resumidos.
	 * @param sumary 
	 * @param concepts 
	 * @return 1 se o titulo não possuir nenhum conceito relevante, ou 0, caso contrário.
	 * @throws IOException Exceção lançada de IO.
	 */
	public void executeSimilarity(SpatialData spatialData, String title, String bagOfWords, PrintWriter out, 
			Sumary sumary) throws IOException {
		
		SearcherConceptysDBPedia searcherConceptys = new SearcherConceptysDBPedia();
		List<Extractor> extractorList = searcherConceptys.searchClassesOrCategories(title);
		List<String> classesUpThreshold = new LinkedList<String>();
		List<String> categoriesUpThreshold = new LinkedList<String>();
		FeatureService featureService = new FeatureServiceImpl();
		
		if(!extractorList.isEmpty()) {
			out.println("------------------------------------------------------------------------------------");
			out.println("Título do Feature Type: " + title);
			out.println();
			boolean wasAnnotated = false;
			for (Extractor extractor : extractorList) {

				if (!extractor.getClasses().isEmpty()) {
					extractor.setSimilarityClasses(executeCossineSimilarity(extractor.getClasses(), 
						SearcherConceptysDBPedia.CLASS,	bagOfWords, title, extractor.getTitle()));
					extractor.getSimilarityClasses().add(title.replaceAll(" ", ""));
					extractor.setOntologyClasses(getSimilaryConcepts(extractor.getSimilarityClasses(), 
							extractor.getTitle()));
					classesUpThreshold.addAll(extractor.getSimilarityClasses());
				}
				if (!extractor.getCategories().isEmpty()) {
					extractor.setSimilarityCategories(executeCossineSimilarity(extractor.getCategories(), 
						SearcherConceptysDBPedia.CATEGORY,	bagOfWords, title, extractor.getTitle()));
					extractor.setOntologyCategories(getSimilaryConcepts(extractor.getSimilarityCategories(), 
							extractor.getTitle()));
					categoriesUpThreshold.addAll(extractor.getSimilarityCategories());
				}
				
				if (!extractor.getSimilarityClasses().isEmpty() || !extractor.getSimilarityCategories().isEmpty()) {
					out.println("Token: " + extractor.getTitle());
					out.println("Categorias: " + printStringList(extractor.getSimilarityCategories()));
					out.println("Categorias similares: " + printStringList(extractConceptNames(extractor.getOntologyCategories())));
					out.println("Conceitos: " + printStringList(extractor.getSimilarityClasses()));
					out.println("Conceitos similares: " + printStringList(extractConceptNames(extractor.getOntologyClasses())));
					out.println("");
				}
				sumary.summarizeResults(extractor);
				if(!extractor.getOntologyClasses().isEmpty() || !extractor.getOntologyCategories().isEmpty()) {
					annotatefeatures(extractor, spatialData);
					wasAnnotated = true;
				}
			}
			sumary.setCountFeature(1);
			if(!wasAnnotated) {
				sumary.setCountFeatureNotAnnotated(1);
			} else {
				spatialData.setAnnotated(Boolean.TRUE);
				spatialData.setTypeAnnotation(SpatialData.SANN_WORDS);
				featureService.updateSpatialData(spatialData);				
			}
			out.println("--------------------------------------");
			out.println("Features anotados: " + (sumary.getCountFeature() - sumary.getCountFeatureNotAnnotated()));
			out.println("Features não anotados: " + sumary.getCountFeatureNotAnnotated());
			out.println("--------------------------------------");
			out.println("");
			out.flush();
		}		
	}	
	
	/**
	 * Método que faz o processamento necessário para extrair a similaridade entre uma 
	 * bagOfWords e um determinado texto.
	 * 
	 * @param spatialData O feture type.
	 * @param title O título do feature type.
	 * @param bagOfWords O texto a ser comparado com a bagOfWords.
	 * @param outConsolidated Arquivo onde estão sendo impressos os resultados consolidados.
	 * @param out Arquivo onde estão sendo impressos os resultados resumidos.
	 * @param sumary 
	 * @param concepts 
	 * @return 1 se o titulo não possuir nenhum conceito relevante, ou 0, caso contrário.
	 * @throws IOException Exceção lançada de IO.
	 */
	public List<Extractor> executeSimilarityYago(SpatialData spatialData, String title, String bagOfWords, PrintWriter out, 
			Sumary sumary) throws IOException {
			
		SearcherConceptysDBPedia searcherConceptys = new SearcherConceptysDBPedia();
		List<Extractor> extractorList = searcherConceptys.searchClassesOrCategories(title);
		List<String> classesUpThreshold = new LinkedList<String>();
		List<String> categoriesUpThreshold = new LinkedList<String>();
		FeatureService featureService = new FeatureServiceImpl();
		
		if(!extractorList.isEmpty()) {

			out.println("------------------------------------------------------------------------------------");
			out.println("Título do Feature Type: " + title);
			out.println();
			boolean wasAnnotated = false;
			for (Extractor extractor : extractorList) {

				if (!extractor.getClasses().isEmpty()) {
					extractor.setSimilarityClasses(executeCossineSimilarity(extractor.getClasses(), 
						SearcherConceptysDBPedia.CLASS,	bagOfWords, title, extractor.getTitle()));
					extractor.getSimilarityClasses().add(extractor.getTitle());
					extractor.setOntologyClasses(getSimilaryConceptsYago(extractor.getSimilarityClasses(), 
							extractor.getClassesAndCategoriesURL()));
					classesUpThreshold.addAll(extractor.getSimilarityClasses());
				}
				if (!extractor.getCategories().isEmpty()) {
					extractor.setSimilarityCategories(executeCossineSimilarity(extractor.getCategories(), 
						SearcherConceptysDBPedia.CATEGORY,	bagOfWords, title, extractor.getTitle()));
					extractor.setOntologyCategories(getSimilaryConceptsYago(extractor.getSimilarityCategories(),
							extractor.getClassesAndCategoriesURL()));
					categoriesUpThreshold.addAll(extractor.getSimilarityCategories());
				}
				
				if (!extractor.getSimilarityClasses().isEmpty() || !extractor.getSimilarityCategories().isEmpty()) {
					out.println("Token: " + extractor.getTitle());
					out.println("Categorias: " + printStringList(extractor.getSimilarityCategories()));
					out.println("Categorias similares: " + printStringList(extractConceptNames(extractor.getOntologyCategories())));
					out.println("Conceitos: " + printStringList(extractor.getSimilarityClasses()));
					out.println("Conceitos similares: " + printStringList(extractConceptNames(extractor.getOntologyClasses())));
					out.println("");
				}
				sumary.summarizeResults(extractor);
				if(!extractor.getOntologyClasses().isEmpty() || !extractor.getOntologyCategories().isEmpty()) {
					annotatefeatures(extractor, spatialData);
					wasAnnotated = true;
				}
			}
			if(wasAnnotated) {
				sumary.setCountFeature(1);
				spatialData.setAnnotated(Boolean.TRUE);
				spatialData.setTypeAnnotation(SpatialData.SANN_LOD);
				featureService.updateSpatialData(spatialData);				
				out.println("--------------------------------------");
				out.println("Features anotados: " + (sumary.getCountFeature() - sumary.getCountFeatureNotAnnotated()));
				out.println("Features não anotados: " + sumary.getCountFeatureNotAnnotated());
				out.println("--------------------------------------");
				out.println("");
			}
			out.flush();
		}		
		return extractorList;
	}
	
	/**
	 * Método para persistir as anotações semânticas construídas na base de dados.
	 * @param extractor O extrator contendo as ontologias anotadas.
	 * @param spatialData O feature type que está sendo anotado.
	 */
	private void annotatefeatures(Extractor extractor, SpatialData spatialData) {
		SemanticAnnotationServiceImpl service = new SemanticAnnotationServiceImpl();
		Set<SemanticAnnotation> semanticAnnotations = new HashSet<SemanticAnnotation>();
		Set<OntologyConcept> concepts = new HashSet<OntologyConcept>();
		concepts.addAll(extractor.getOntologyClasses());
		concepts.addAll(extractor.getOntologyCategories());
		if (!concepts.isEmpty()) {
			for (OntologyConcept concept : concepts) {
				SemanticAnnotation sann = new SemanticAnnotation(spatialData, concept);
				semanticAnnotations.add(sann);
			}
		}
		service.saveSemanticAnnotations(semanticAnnotations);
	}

	/**
	 * Método que faz o processamento necessário para extrair a similaridade entre uma 
	 * bagOfWords e um determinado texto.
	 * 
	 * @param title O título do feature typde.
	 * @param bagOfWords O texto a ser comparado com a bagOfWords.
	 * @throws IOException Exceçã£o lançada de IO.
	 */
	public Set<String> executeSimilarity(String title, String bagOfWords) 
			throws IOException {
			
		SearcherConceptysDBPedia searcherConceptys = new SearcherConceptysDBPedia();
		List<Extractor> extractorList = searcherConceptys.searchClassesOrCategories(title);
		Set<String> concepts = new HashSet<String>();
		
		if(!extractorList.isEmpty()) {

			for (Extractor extractor : extractorList) {
			
				if (!extractor.getClasses().isEmpty()) {
					extractor.setSimilarityClasses(executeCossineSimilarity(extractor.getClasses(), 
						SearcherConceptysDBPedia.CLASS,	bagOfWords, title, extractor.getTitle()));
					extractor.getSimilarityClasses().add(title.replaceAll(" ", ""));
					extractor.setOntologyClasses(getSimilaryConcepts(extractor.getSimilarityClasses(), 
							extractor.getTitle()));
					concepts.addAll(extractConceptNames(extractor.getOntologyClasses()));
				}
				if (!extractor.getCategories().isEmpty()) {
					extractor.setSimilarityCategories(executeCossineSimilarity(extractor.getCategories(), 
						SearcherConceptysDBPedia.CATEGORY,	bagOfWords, title, extractor.getTitle()));
					extractor.setOntologyCategories(getSimilaryConcepts(extractor.getSimilarityCategories(), 
							extractor.getTitle()));
					concepts.addAll(extractConceptNames(extractor.getOntologyCategories()));
				}
			}
		}
			
		return concepts;
		
	}
	
	/**
	 * Método que faz o processamento necessário para extrair a similaridade entre uma 
	 * bagOfWords e um determinado texto.
	 * 
	 * @param title O título do feature typde.
	 * @param bagOfWords O texto a ser comparado com a bagOfWords.
	 * @throws IOException Exceção lançada de IO.
	 */
	public Set<String> executeSimilarityYago(String title, String bagOfWords) 
			throws IOException {
			
		SearcherConceptysDBPedia searcherConceptys = new SearcherConceptysDBPedia();
		List<Extractor> extractorList = searcherConceptys.searchClassesOrCategories(title);
		Set<String> concepts = new HashSet<String>();
		
		if(!extractorList.isEmpty()) {

			for (Extractor extractor : extractorList) {
			
				if (!extractor.getClasses().isEmpty()) {
					extractor.setSimilarityClasses(executeCossineSimilarity(extractor.getClasses(), 
						SearcherConceptysDBPedia.CLASS,	bagOfWords, title, extractor.getTitle()));
					extractor.setOntologyClasses(getSimilaryConceptsYago(extractor.getSimilarityClasses(), 
							extractor.getClassesAndCategoriesURL()));
					concepts.addAll(extractConceptNames(extractor.getOntologyClasses()));
				}
				if (!extractor.getCategories().isEmpty()) {
					extractor.setSimilarityCategories(executeCossineSimilarity(extractor.getCategories(), 
						SearcherConceptysDBPedia.CATEGORY,	bagOfWords, title, extractor.getTitle()));
					extractor.setOntologyCategories(getSimilaryConceptsYago(extractor.getSimilarityCategories(),
							extractor.getClassesAndCategoriesURL()));
					concepts.addAll(extractConceptNames(extractor.getOntologyCategories()));
				}
			}
		}
			
		return concepts;
		
	}
	
	/**
	 * Método para extrair a similaridade dos cossenos entre a bagofwords e a informação 
	 * textual das páginas da wikipedia de cada um dos conceitos passados. Também é realizada
	 * a filtragem dos conceitos relevantes a partir de um threshold sobre os cossenos.
	 * 
	 * @param concepts Os conceitos que serão acessados na wikipedia.
	 * @param type O tipo do conceito (classe ou categoria).
	 * @param bagOfWords A  bagofwords a ser comparada.
	 * @param title O título do feature type a ser impresso no arquivo de saída.
	 * @param token O token que foi realizada a busca na dbpedia.
	 * @param outConsolidated O arquivo de saída a ser impressa as informações.
	 * @return Uma lista contendo os conceitos que utrapassaram o threshold.
	 * @throws IOException Exceção lançada caso haja algum problema na extração do cosseno.
	 */
	private Set<String> executeCossineSimilarity(Set<String> concepts, String type, 
			String bagOfWords, String title, String token) throws IOException {
		
		Set<String> conceptsUpThreshold = new HashSet<String>();
		for (String concept : concepts) {
			if (!isConceptDefault(concept)) {										
				SearcherWikipedia searcherText = new SearcherWikipedia(concept);
				String wikiText = searcherText.getText();
				if (concept.endsWith("s") && (wikiText == null || wikiText.isEmpty())) {
					String conceptWithoutS = concept.substring(0, concept.length()-1);
					searcherText = new SearcherWikipedia(conceptWithoutS);
				}
				double cosineSimilarity = 0.0;
				if (!bagOfWords.isEmpty() && wikiText != null && !wikiText.isEmpty()) {
					cosineSimilarity = CosineDocumentSimilarity
							.getCosineSimilarity(bagOfWords, wikiText);
				}
				if (cosineSimilarity >= THRESHOLD_COSINE) {
					conceptsUpThreshold.add(concept);
				}
			}
		}
		return conceptsUpThreshold;
	}
	
	/**
	 * Extrai os nomes dos conceitos do conjunto de conceitos ontológicos.
	 * @param concepts O conjunto dos conceitos ontológicos.
	 * @return Os nomes dos conceitos.
	 */
	private Set<String> extractConceptNames(Set<OntologyConcept> concepts) {
		
		Set<String> conceptNames = new HashSet<String>();
		for (OntologyConcept concept : concepts) {
			conceptNames.add(concept.getConcept());
		}
		
		return conceptNames;		
	}
	
	/**
	 * Imprime o toString de um conjunto lista sem os parênteses.
	 * @param set O conjunto a ser impresso.
	 * @return A string do conjunto sem os parênteses.
	 */
	private String printStringList(Set<String> set) {
		
		String setReturn = set.toString();
		setReturn = setReturn.replace("[", "");
		setReturn = setReturn.replace("]", "");
		
		return setReturn;		
	}
	
	/**
	 * Método para identificar se o token passado corresponde a um coceito padrão.
	 * @param token O token a ser verificado.
	 * @return True se corresponder, ou false, caso contrário.
	 */
	private boolean isConceptDefault(String token) {
		
		if ("owl#Thing".equals(token)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Método para extrair os conceitos de ontologias cadastrados que são similares ao conceito passado.
	 * 
	 * @param concept Os conceitos a serem pesquisados.
	 * @param title O título/token consultado.
	 * @return O conjunto de conceitos compatíveis ao conceito passado.
	 */
	private Set<OntologyConcept> getSimilaryConcepts(Set<String> concepts, String title) {
		
		PreProcessingText preprocessing = PreProcessingText.getInstance();
		Set<OntologyConcept> similaryConcepts = new HashSet<OntologyConcept>();
		concepts.add(preprocessing.preProcessingWithoutExtractScale(title));
		
		OntologyConceptService conceptService = new OntologyConceptServiceImpl();
		
		for (String concept : concepts) {	
			if (!concept.equals("")) {				
				String conceptWithoutSpace = concept.replaceAll("\'", "").replaceAll(" ", "");		
				List<String> tokens = preprocessing.tokenizingText(concept);
				Set<String> tokensConcept = preprocessing.preProcessing(concept);
				String coveredConcept = preprocessing.tokensToString(tokensConcept);
				coveredConcept = coveredConcept.replaceAll("\'", "");
				List<OntologyConcept> ontologyConcepts = null;
				if (!coveredConcept.equals("")) {					
					ontologyConcepts = conceptService.recoveryOntolgyConceptByTerm(
							coveredConcept);
				}
				
				if (ontologyConcepts == null || ontologyConcepts.isEmpty() && !conceptWithoutSpace.equals("")) {
					ontologyConcepts = conceptService.recoveryOntolgyConceptByTerm(
							conceptWithoutSpace);
				}
				
				if ((ontologyConcepts == null || ontologyConcepts.isEmpty()) && tokens.size() > 1) {
					ontologyConcepts = recoveryOntologyConceptByToken(concept, conceptService);
				}
				
				if ((ontologyConcepts == null || ontologyConcepts.isEmpty()) && concept.charAt(concept.length()-1) == 's') {
					String conceptWithoutS  = concept.substring(0, concept.length()-1);
					ontologyConcepts = conceptService.recoveryOntolgyConceptByTerm(
							conceptWithoutS.replaceAll("\'", " "));
				}
				
				if (ontologyConcepts != null && !ontologyConcepts.isEmpty()) {				
					for (OntologyConcept ontologyConcept : ontologyConcepts) {
						if (ontologyConcept.getNormalizedName().equalsIgnoreCase(coveredConcept) || 
								ontologyConcept.getConceptName().equalsIgnoreCase(coveredConcept) ||
								ontologyConcept.getNormalizedName().equalsIgnoreCase(concept) ||
								ontologyConcept.getConceptName().equalsIgnoreCase(concept) ||
								ontologyConcept.getConceptName().toLowerCase().indexOf(concept.toLowerCase()) > 0 ||
								ontologyConcept.getNormalizedName().toLowerCase().indexOf(concept.toLowerCase()) > 0 ||
								conceptWithoutSpace.toLowerCase().indexOf(ontologyConcept.getConceptName().toLowerCase()) >= 0 ||
								conceptWithoutSpace.toLowerCase().indexOf(ontologyConcept.getNormalizedName().toLowerCase()) >= 0) {				
							similaryConcepts.add(ontologyConcept);
						}
					}
				}
			}
		}
		return similaryConcepts;
	}
	
	/**
	 * Método para extrair os conceitos de ontologias cadastrados que são similares ao conceito passado.
	 * 
	 * @param concepts Os conceitos a serem pesquisados.
	 * @param classesAndCategorisURL O mapa das urls dos conceitos a serem pesquisados.
	 * @return O conjunto de conceitos compatíveis ao conceito passado.
	 */
	private Set<OntologyConcept> getSimilaryConceptsYago(Set<String> concepts, Map<String, 
			List<String>> classesAndCategorisURL) {
		
		Set<OntologyConcept> similaryConcepts = new HashSet<OntologyConcept>();
		
		OntologyConceptService conceptService = new OntologyConceptServiceImpl();
		
		for (String concept : concepts) {	
			if (!concept.equals("")) {	
				List<String> urlsDbPedia = classesAndCategorisURL.get(concept);
				if (urlsDbPedia != null) {
					for (String urlDbPedia : urlsDbPedia) {
						Set<String> urlYago = extractUrlsYago(urlDbPedia);
						similaryConcepts.addAll(conceptService.recoveryOntologiesByURIs(urlYago));
					}
				}
				
				
				// SimilarityConceitosYago.txt
				
//				List<OntologyConcept> ontologyConcepts = conceptService
//						.recoveryOntolgyConceptByTerm(concept);
//				if (ontologyConcepts != null && !ontologyConcepts.isEmpty()) {				
//					similaryConcepts.addAll(ontologyConcepts);
//				}
				
				// SimilarityConceitosYagoToken.txt
				
//				List<OntologyConcept> ontologyConcepts = conceptService
//						.recoveryOntolgyConceptByTerm(concept);
//				if (ontologyConcepts.isEmpty()) {					
//					String[] tokensConcept = concept.split(" ");
//					if (tokensConcept.length > 1) {					
//						Combination c = new Combination(tokensConcept, tokensConcept.length - 1);
//						List<String> combinations = c.combine();
//						for (String token : tokensConcept) {
//							ontologyConcepts.addAll(conceptService.recoveryOntolgyConceptByTerm(token));
//						}
//					}				
//				}
//				if (ontologyConcepts != null && !ontologyConcepts.isEmpty()) {				
//					similaryConcepts.addAll(ontologyConcepts);
//				}
				
				// SimilarityConceitosYagoTokenFiltro.txt
				
//				List<OntologyConcept> ontologyConcepts = conceptService
//						.recoveryOntolgyConceptByTerm(concept);
//				if (ontologyConcepts.isEmpty()) {					
//					String[] tokensConcept = concept.split(" ");
//					if (tokensConcept.length > 1) {					
//						Combination c = new Combination(tokensConcept, tokensConcept.length - 1);
//						List<String> combinations = c.combine();
//						for (String token : tokensConcept) {
//							ontologyConcepts.addAll(conceptService.recoveryOntolgyConceptByTerm(token));
//						}
//					}				
//				}
//				if (ontologyConcepts != null && !ontologyConcepts.isEmpty()) {				
//					for (OntologyConcept ontologyConcept : ontologyConcepts) {
//						if (ontologyConcept.getNormalizedName().equalsIgnoreCase(concept) ||
//								ontologyConcept.getConceptName().equalsIgnoreCase(concept) ||
//								ontologyConcept.getConceptName().toLowerCase().indexOf(concept.toLowerCase()) > 0 ||
//								ontologyConcept.getNormalizedName().toLowerCase().indexOf(concept.toLowerCase()) > 0) {				
//							similaryConcepts.add(ontologyConcept);
//						}
//					}					
//				}						
			}
		}
		return similaryConcepts;
	}	

	private Set<String> extractUrlsYago(String urlDbPedia) {
		try {
			ParserHtml parser = new ParserHtml();
			parser.readUrl(urlDbPedia);
			return parser.getUrlsDbpediaWithTerm(TERM_YAGO);
		} catch (ParserHtmlException e) {
			return new HashSet<String>();
		}
	}

	/**
	 * Método para tentar localizar algum conceito ontológico a partir dos token
	 * de um título composto por mais de uma palavra.
	 * 
	 * @param tokensConcept A lista de token que compõe o título.
	 * @return Os conceitos ontologicos que foram encontrados.
	 */
	private List<OntologyConcept> recoveryOntologyConceptByToken(
			String tokensConcept, OntologyConceptService conceptService) {

		List<OntologyConcept> ontologyConceptsReturn = new ArrayList<OntologyConcept>();
		String[] titleToken = tokensConcept.split(" ");
		int amountCombinationPossible = titleToken.length - 1;
		boolean find = false;
		while (!find && amountCombinationPossible > 0) {
			Combination c = new Combination(titleToken, amountCombinationPossible);
			List<String> combinations = c.combine();
			for (String comb : combinations) {
				List<OntologyConcept> ontologyConcepts = conceptService.recoveryOntolgyConceptByTerm(
						comb.replaceAll("\'", " "));
				if(ontologyConcepts != null && !ontologyConcepts.isEmpty()) {
					find = true;
					ontologyConceptsReturn.addAll(ontologyConcepts);
				} else if (comb.endsWith("s")) {
					comb = comb.substring(0, comb.length()-1);
					ontologyConcepts = conceptService.recoveryOntolgyConceptByTerm(
							comb.replaceAll("\'", " "));
					if(ontologyConcepts != null && !ontologyConcepts.isEmpty()) {
						find = true;
						ontologyConceptsReturn.addAll(ontologyConcepts);
					}
				}
			}
			amountCombinationPossible--;
		}			
		if (find && amountCombinationPossible >= 1) {
			ontologyConceptsReturn.addAll(recoveryRemainingTerms(tokensConcept, 
					ontologyConceptsReturn, conceptService));
		}
		return ontologyConceptsReturn;
	}
	
	/**
	 * Método para buscar os termos do título que ainda não foram associados a nenhum conceito.
	 * 
	 * @param titleToken O título a ser verificado.
	 * @param listReturn A lista de tokens que já possuem conceitos associados.
	 * @return A lista de conceitos associados aos tokens que ainda não tinham sido recuperados.
	 */
	private List<OntologyConcept> recoveryRemainingTerms(String title,
			List<OntologyConcept> listReturn, OntologyConceptService conceptService) {

		List<OntologyConcept> concepts = new ArrayList<OntologyConcept>();
		
		PreProcessingText preprocessing = PreProcessingText.getInstance();
		List<String> titleTokens = preprocessing.tokenizingText(title);
		Set<String> returnTokens = preprocessing.tokenizingTextList(getTitles(listReturn));
		for (String token : titleTokens) {
			if (!returnTokens.contains(token)) {
				List<OntologyConcept> ontologyConcepts = conceptService
						.recoveryOntolgyConceptByTerm(token.replaceAll("\'", " "));
				if(ontologyConcepts != null && !ontologyConcepts.isEmpty()) {
					concepts.addAll(ontologyConcepts);
				}			
			}
		}
		return concepts;
	}

	/**
	 * Extrai os nomes dos conceitos ontológicos de uma lista.
	 * 
	 * @param listReturn A lista de conceitos ontológicos.
	 * @return A lista de nomes dos conceitos.
	 */
	private List<String> getTitles(List<OntologyConcept> listReturn) {
		
		List<String> listResult = new ArrayList<String>();
		for (OntologyConcept concept : listReturn) {
			listResult.add(concept.getConceptName());
		}		
		return listResult;
	}

	// ATRIBUTOS
	
	/**
	 * Método para extrair os conceitos de ontologias cadastrados que representam o atributo de um serviço.
	 * 
	 * @param attributeService O atributo do serviço.
	 * @return O conjunto de conceitos compatíveis ao atributo passado.
	 */
	public Set<OntologyConcept> getSimilaryConcepts(AttributeSpatialData attributeService) {
		
		Set<OntologyConcept> similaryConcepts = new HashSet<OntologyConcept>();
		PreProcessingText preprocessing = PreProcessingText.getInstance();
		String nameAttr = attributeService.getName().replaceAll("_", " ");		
		String nameAttrProcessing = preprocessing.tokenizingTextWithUppercase(nameAttr);
		
		OntologyConceptService conceptService = new OntologyConceptServiceImpl();
		
		if (!nameAttr.equals("")) {				
			List<OntologyConcept> ontologyConcepts = conceptService.recoveryOntolgyConceptByTerm(nameAttr);
			
			if ((ontologyConcepts == null || ontologyConcepts.isEmpty()) && !nameAttr.equals(nameAttrProcessing)) {
				ontologyConcepts = conceptService.recoveryOntolgyConceptByTerm(nameAttrProcessing);
			}
			
			if ((ontologyConcepts == null || ontologyConcepts.isEmpty())) {
				ontologyConcepts = recoveryOntologyConceptByAttribute(nameAttrProcessing, conceptService);
			}
			
			if (ontologyConcepts != null && !ontologyConcepts.isEmpty()) {						
				similaryConcepts.addAll(ontologyConcepts);
			}
		}
		return similaryConcepts;
	}

	/**
	 * Método para tentar localizar algum conceito ontológico a partir dos token
	 * do nome de um atributo composto por mais de uma palavra.
	 * 
	 * @param nameAttribute O nome do atributo.
	 * @return Os conceitos ontologicos que foram encontrados.
	 */
	private List<OntologyConcept> recoveryOntologyConceptByAttribute(String nameAttribute, 
			OntologyConceptService conceptService) {

		List<OntologyConcept> ontologyConceptsReturn = new ArrayList<OntologyConcept>();
		String[] tokens = nameAttribute.split(" ");
		int amountCombinationPossible = tokens.length - 1;
		boolean find = false;
		while (!find && amountCombinationPossible > 0) {
			Combination c = new Combination(tokens, amountCombinationPossible);
			List<String> combinations = c.combine();
			for (String comb : combinations) {
				if (comb.length() > 2) {					
					List<OntologyConcept> ontologyConcepts = conceptService.recoveryOntolgyConceptByTerm(comb);
					if (ontologyConcepts == null) {
						ontologyConcepts = conceptService.recoveryOntolgyConceptByTerm(preprocessing.stemming(comb));
					}
					if(ontologyConcepts != null && !ontologyConcepts.isEmpty()) {
						find = true;
						ontologyConceptsReturn.addAll(ontologyConcepts);
					}
				}
			}
			amountCombinationPossible--;
		}			
		return ontologyConceptsReturn;
	}
	
	/**
	 * Popula o conjunto de anotações semânticas dos atributos dos serviços com os conceitos similares.
	 * @param attribute O atributo anotado.
	 * @param concepts Os conceitos similares.
	 * @return O conjunto de anotações semânticas dos atributos.
	 */
	public Set<SemanticAnnotationAttribute> setSemanticAnnotationAttr(AttributeSpatialData attribute, 
			Set<OntologyConcept> concepts) {
		Set<SemanticAnnotationAttribute> sanns = new HashSet<SemanticAnnotationAttribute>();
		for (OntologyConcept concept : concepts) {
			SemanticAnnotationAttribute sann = new SemanticAnnotationAttribute();
			sann.setAttributeService(attribute);
			sann.setOntologyConcept(concept);
			sanns.add(sann);
		}
		return sanns;
	}
	
	public static void main(String[] args) {
		OntologyConceptServiceImpl ocs = new OntologyConceptServiceImpl();
		List<OntologyConcept> list = ocs.recoveryOntolgyConceptByTerm("water");
		for (OntologyConcept ontologyConcept : list) {
			System.out.print(ontologyConcept.getId() + ",");
		}
	}

}
