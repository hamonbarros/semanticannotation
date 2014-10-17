package br.com.sann.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SpatialData;
import br.com.sann.service.FeatureService;
import br.com.sann.service.OntologyConceptService;
import br.com.sann.service.impl.FeatureServiceImpl;
import br.com.sann.service.impl.OntologyConceptServiceImpl;
import br.com.sann.service.processing.text.BagOfWords;
import br.com.sann.service.search.dbpedia.SearcherConceptysDBPedia;
import br.com.sann.service.search.wikipedia.SearcherWikipedia;
import br.com.sann.service.similarity.CosineDocumentSimilarity;

public class Main {	

	private static final double THRESHOLD_COSINE = 0.1;
	public static Logger log;
	/**
	 * Método principal.
	 * 
	 * @param args Os argumentos de entrada.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		
		log = Logger.getLogger(Main.class);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		log.info("Iniciando o processamento...");
		log.info("Data Inicial: " + df.format(new Date()));
		
		InputStream in = new Main().getClass().getClassLoader()
				.getResourceAsStream("config.properties");  
		Properties props = new Properties();  
		props.load(in);
		in.close();
		String path = props.getProperty("PATH_TREETAGGER");

//		mapedClassesOrCategories(path);
		extractSimilarity(path);
		
		log.info("Fim do processamento!");
		log.info("Data Final: " + df.format(new Date()));
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

	}
	

	/**
	 * Método que extrai a similaridade entre os campos textuais das feature types de 
	 * uma IDE e os textos da respectivas páginas da wikipédia.
	 * 
	 * @param pathTreeTagger O caminho da instalação do treeTagger.
	 */
	private static void extractSimilarity(String pathTreeTagger) {

		log.info("Realizando a leitura das feature types... ");
		FeatureService service = new FeatureServiceImpl();
		List<SpatialData> spatialDataList = service.recoverAllSpatialData();
		log.info("Leitura das feature types realizada com sucesso!");
		log.info("Realizando a leitura dos conceitos ontológicos ... ");
		OntologyConceptService conceptService = new OntologyConceptServiceImpl();
		List<OntologyConcept> concepts = conceptService.recoverAllOntologyConcept();
		log.info("Leitura dos conceitos ontológicos realizada com sucesso!");
		PrintWriter similarityConsolidated = null;
		PrintWriter similarity = null;
		try {
			DateFormat df = new SimpleDateFormat("YYYYMMddHHmm");
			String dateFormated = df.format(new Date());

			similarityConsolidated = new PrintWriter(new FileWriter(new File(
					"Similarity_consolidated" + dateFormated + ".csv")));
			similarity = new PrintWriter(new FileWriter(new File("Similarity" + dateFormated + ".txt")));

			similarityConsolidated.println("Title|Title Tokenizing|Type|Concept|Cosine|URL Wikipedia|URL Wikipedia Search|Bag of Words");

			String previousTitle = "";
			log.info("Inicio da consulta das classes e categorias na dbpedia que contenham relevância com os títulos...");
			
			StringBuffer storeBagsOfWords = new StringBuffer();
			BagOfWords bw = null;
			Integer countTitleWithoutConcepts = 0;
			
			for (SpatialData spatialData : spatialDataList) {
//			for(int i=0; i<30; i++){
				String title = spatialData.getTitle();
				
				bw = new BagOfWords(spatialData);
				
				// Tratamento necessário para o ínicio do processo. Quando a storeBagsOfWords ainda está vazia.
				if (title.equals(previousTitle)) {					
					storeBagsOfWords.append(" ");
					storeBagsOfWords.append(bw.extractTextProperties());
					
				} else {
					
					if (previousTitle.equals("")) {
						storeBagsOfWords.append(bw.extractTextProperties());
						previousTitle = title;
						continue;						
					}
					
					countTitleWithoutConcepts += executeSimilarity(previousTitle, 
							bw.extractWordList(storeBagsOfWords.toString()), similarityConsolidated, similarity, concepts);
					
					// Cria uma nova instância quando o título é diferente do anterior.
					storeBagsOfWords = new StringBuffer();
					storeBagsOfWords.append(bw.extractTextProperties());
					previousTitle = title;

				}
								
			}
			
			if (!previousTitle.isEmpty() && bw != null) {				
				countTitleWithoutConcepts += executeSimilarity(previousTitle, bw.extractWordList(storeBagsOfWords.toString()), 
						similarityConsolidated, similarity, concepts);
			}
			
			similarity.println("--------------------- SUMÁRIO ---------------------");
			similarity.println("Títulos verificados:           " + spatialDataList.size());
			similarity.println("Títulos conceitualizados:      " + (spatialDataList.size() - countTitleWithoutConcepts));
			similarity.println("Títulos não conceitualizados:  " + countTitleWithoutConcepts);
			
			log.info("Finalização da consulta das classes e categorias na dbpedia que contenham relevância! com os títulos.");
			
			similarityConsolidated.flush();
			similarity.flush();

			similarityConsolidated.close();
			similarity.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que faz o processamento necessário para extrair a similaridade entre uma 
	 * bagOfWords e um determinado texto.
	 * 
	 * @param title O título do feature typde.
	 * @param bagOfWords O texto a ser comparado com a bagOfWords.
	 * @param outConsolidated Arquivo onde estão sendo impressos os resultados consolidados.
	 * @param out Arquivo onde estão sendo impressos os resultados resumidos.
	 * @param concepts 
	 * @return 1 se o titulo não possuir nenhum conceito relevante, ou 0, caso contrário.
	 * @throws IOException Exceção lançada de ID.
	 */
	private static Integer executeSimilarity(String title, String bagOfWords, PrintWriter outConsolidated, PrintWriter out, List<OntologyConcept> ontologyConcepts) 
			throws IOException {
		
		log.info("[INICIO] Início do processamento para o título: " + title);
		
		SearcherConceptysDBPedia searcherConceptys = new SearcherConceptysDBPedia();
		Map<String, Map<String, Set<String>>> tokensMap = searcherConceptys.searchClassesOrCategories(title);
		List<String> classesUpThreshold = new LinkedList<String>();
		List<String> categoriesUpThreshold = new LinkedList<String>();
		
		if(!tokensMap.isEmpty()) {
			
			String titleToken = tokensMap.keySet().iterator().next();
			Map<String, Set<String>> classesAndCategoriesMap = tokensMap.get(titleToken);
			if (!classesAndCategoriesMap.isEmpty()) {
				out.println("Título do Feature Type: " + title);
				for (String token : tokensMap.keySet()) {
					classesAndCategoriesMap = tokensMap.get(token);
					
					Set<String> classes = classesAndCategoriesMap.get(SearcherConceptysDBPedia.CLASS);
					List<String> classesCosineSimilarity = executeCossineSimilarity(classes, 
							SearcherConceptysDBPedia.CLASS,	bagOfWords, title, token, outConsolidated);
					classesUpThreshold.addAll(classesCosineSimilarity);

					Set<String> categories = classesAndCategoriesMap.get(SearcherConceptysDBPedia.CATEGORY);
					List<String> categoriesCosineSimilarity = executeCossineSimilarity(categories, 
							SearcherConceptysDBPedia.CATEGORY, bagOfWords, title, token, outConsolidated);
					categoriesUpThreshold.addAll(categoriesCosineSimilarity);
					if (!classesCosineSimilarity.isEmpty() || !categoriesCosineSimilarity.isEmpty()) {
						out.println("Token: " + token);
						out.println("Categorias: " + printStringList(categoriesCosineSimilarity));
						out.println("Categorias similares: " + printStringList(getSimilaryConcepts(ontologyConcepts, categoriesCosineSimilarity)));
						out.println("Conceitos: " + printStringList(classesCosineSimilarity));
						out.println("Conceitos similares: " + printStringList(getSimilaryConcepts(ontologyConcepts, classesCosineSimilarity)));
						out.println("");
					}
				}
				out.println("");
			}
		}
		
		log.info("[FIM] Fim do processamento para o título: " + title);

		if(categoriesUpThreshold.isEmpty() && classesUpThreshold.isEmpty()) {
			return 1;
		}
		return 0;
		
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
	private static List<String> executeCossineSimilarity(Set<String> concepts, String type, 
			String bagOfWords, String title, String token, PrintWriter outConsolidated) throws IOException {
		
		List<String> conceptsUpThreshold = new ArrayList<String>();
		for (String concept : concepts) {
			if (!isConceptDefault(concept)) {										
				SearcherWikipedia searcherText = new SearcherWikipedia(concept);
				String wikiText = searcherText.getText();
				double cosineSimilarity = 0.0;
				if (!bagOfWords.isEmpty() && wikiText != null && !wikiText.isEmpty()) {
					cosineSimilarity = CosineDocumentSimilarity
							.getCosineSimilarity(bagOfWords, wikiText);
				}
				String line = title + "|" + token + "|" + type + "|" + concept + "|" 
						+ cosineSimilarity + "|" + searcherText.getWikiUrl() + 
						"|" + searcherText.getUrl() + "|" + bagOfWords; 
				outConsolidated.println(line);
				if (cosineSimilarity >= THRESHOLD_COSINE) {
					conceptsUpThreshold.add(concept);
				}
			}
		}
		return conceptsUpThreshold;
	}
	
	/**
	 * Método utilitário para formatar um valor doble com 3 casas de precisão.
	 * @param x O valor em double.
	 * @return O double formatado.
	 */
	private static String format(double x) {  
	    return String.format(Locale.ENGLISH, "%.3f", x);  
	} 
	
	/**
	 * Imprime o toString de uma lista sem os parênteses.
	 * @param list A lista a ser impressa.
	 * @return Uma lista sem os parênteses.
	 */
	private static String printStringList(List<String> list) {
		
		String listReturn = list.toString();
		listReturn = listReturn.replace("[", "");
		listReturn = listReturn.replace("]", "");
		
		return listReturn;		
	}
	
	/**
	 * Método para identificar se o token passado corresponde a um coceito padrão.
	 * @param token O token a ser verificado.
	 * @return True se corresponder, ou false, caso contrátio.
	 */
	private static boolean isConceptDefault(String token) {
		
		if ("owl#Thing".equals(token)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Método para extrair os conceitos de ontologias cadastrados que são similares ao conceito passado.
	 * 
	 * @param concepts A lista de conceitos cadastrados.
	 * @param concept O conceito a ser pesquisado.
	 * @return A lista de conceitos compatíveis ao conceito passado.
	 */
	private static List<String> getSimilaryConcepts(List<OntologyConcept> ontologyConcepts, List<String> concepts) {
		
		List<String> similaryConcepts = new ArrayList<String>();
		for (OntologyConcept ontologyConcept : ontologyConcepts) {
			for (String concept : concepts) {				
				if (ontologyConcept.getNormalizedName().equalsIgnoreCase(concept) || 
						ontologyConcept.getConceptName().equalsIgnoreCase(concept)) {
					similaryConcepts.add(ontologyConcept.getConcept());
					break;
				}
			}
		}
		return similaryConcepts;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	//TODO Métodos em desuso
	
	/**
	 * Método que gera os arquivos contendo o mapeamento ou não dos serviços 
	 * de uma IDE com as respectivas categorias da dbpedia.
	 * 
	 * @param pathTreeTagger O caminho da instalação do treeTagger.
	 */
	private static void mapedClassesOrCategories(String pathTreeTagger) {

		log.info("Realizando a leitura das feature types... ");
		FeatureService service = new FeatureServiceImpl();
		List<SpatialData> spatialDataList = service.recoverAllSpatialData();
		log.info("Leitura das feature types realizada com sucesso!");
		PrintWriter titlesCategorized = null;
		PrintWriter titleUncategorized = null;
		try {
			DateFormat df = new SimpleDateFormat("YYYYMMddHHmm");
			String dateFormated = df.format(new Date());

			titlesCategorized = new PrintWriter(new FileWriter(new File(
					"CategorizedTitles" + dateFormated + ".csv")));
			titleUncategorized = new PrintWriter(new FileWriter(new File(
					"UncategorizedTitles" + dateFormated + ".csv")));

			titlesCategorized.println("Title|Title Tokenizing|Classe/Category");
			titleUncategorized.println("Title|Title Tokenizing");

			String previousTitle = "";
			log.info("Inicio da consulta das classes ou categorias na dbpedia...");
			SearcherConceptysDBPedia searcher = new SearcherConceptysDBPedia();
			for (SpatialData spatialData : spatialDataList) {
				String title = spatialData.getTitle();
				if (!title.equals(previousTitle)) {		
					BagOfWords bw = new BagOfWords(spatialData);
					System.out.println(bw.extractWordList());
					
					Map<String, Map<String, Set<String>>> tokensMap = searcher.searchClassesOrCategories(title);
					
					if(!tokensMap.isEmpty()) {
						String titleToken = tokensMap.keySet().iterator().next();
						Map<String, Set<String>> classesAndCategoriesMap = tokensMap.get(titleToken);
						if (!classesAndCategoriesMap.isEmpty()) {
							for (String token : tokensMap.keySet()) {
								classesAndCategoriesMap = tokensMap.get(token);
								
								Set<String> classes = classesAndCategoriesMap.get(SearcherConceptysDBPedia.CLASS);
								if (!classes.isEmpty()) {									
									String line = title + "|" + token;
									for (String c : classes) {
										line += "|" + c;
									}
									titlesCategorized.println(line);
								}
								
								Set<String> categories = classesAndCategoriesMap.get(SearcherConceptysDBPedia.CATEGORY);
								if (!classes.isEmpty()) {									
									String line = title + "|" + token;
									for (String category : categories) {
										line += "|" + category;
									}
									titlesCategorized.println(line);
								}
							}
						} else {
							String line = title + "|" + titleToken;
							titleUncategorized.println(line);								
						}
					}					
					previousTitle = title;
				}
								
			}
			log.info("Finalização da consulta das classes ou categorias na dbpedia!");
			
			titlesCategorized.flush();
			titleUncategorized.flush();

			titlesCategorized.close();
			titleUncategorized.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
