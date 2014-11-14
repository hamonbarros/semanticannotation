package br.com.sann.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.sann.domain.Extractor;
import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SpatialData;
import br.com.sann.domain.Sumary;
import br.com.sann.process.SpatialDataListProcess;
import br.com.sann.service.FeatureService;
import br.com.sann.service.OntologyConceptService;
import br.com.sann.service.impl.FeatureServiceImpl;
import br.com.sann.service.impl.OntologyConceptServiceImpl;
import br.com.sann.service.processing.text.BagOfWords;
import br.com.sann.service.processing.text.PreProcessingText;
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
		
		log = Logger.getLogger(Main.class);
		log.info("Realizando a leitura das feature types... ");
		FeatureService service = new FeatureServiceImpl();
		List<SpatialData> spatialDataList = service.recoverAllSpatialData();
		log.info("Leitura das feature types realizada com sucesso!");
		
		SpatialDataListProcess process = new SpatialDataListProcess();
		process.execute(spatialDataList);

//		DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
//		
//		log = Logger.getLogger(Main.class);
//		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//		log.info("Iniciando o processamento...");
//		log.info("Data Inicial: " + df.format(new Date()));
//		
//		InputStream in = new Main().getClass().getClassLoader()
//				.getResourceAsStream("config.properties");  
//		Properties props = new Properties();  
//		props.load(in);
//		in.close();
//		String path = props.getProperty("PATH_TREETAGGER");
//
////		mapedClassesOrCategories(path);
//		extractSimilarity(path);
//		
//		log.info("Fim do processamento!");
//		log.info("Data Final: " + df.format(new Date()));
//		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

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
//		PrintWriter similarityConsolidated = null;
		PrintWriter similarity = null;
		try {
			DateFormat df = new SimpleDateFormat("YYYYMMddHHmm");
			String dateFormated = df.format(new Date());

//			similarityConsolidated = new PrintWriter(new FileWriter(new File(
//					"Similarity_consolidated" + dateFormated + ".csv")));
			similarity = new PrintWriter(new FileWriter(new File("Similarity" + dateFormated + ".txt")));

//			similarityConsolidated.println("Title|Title Tokenizing|Type|Concept|Cosine|URL Wikipedia|URL Wikipedia Search|Bag of Words");

			String previousTitle = "";
			log.info("Inicio da consulta das classes e categorias na dbpedia que contenham relevância com os títulos...");
			
			StringBuffer storeBagsOfWords = new StringBuffer();
			BagOfWords bw = null;
			Integer countTitleWithoutConcepts = 0;
			Sumary sumary = new Sumary();
			
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
					
					executeSimilarity(previousTitle, bw.extractWordList(storeBagsOfWords.toString()), 
							/*similarityConsolidated, */similarity, concepts, sumary);
					
					// Cria uma nova instância quando o título é diferente do anterior.
					storeBagsOfWords = new StringBuffer();
					storeBagsOfWords.append(bw.extractTextProperties());
					previousTitle = title;

				}
								
			}
			
			if (!previousTitle.isEmpty() && bw != null) {				
				executeSimilarity(previousTitle, bw.extractWordList(storeBagsOfWords.toString()), 
						/*similarityConsolidated, */similarity, concepts, sumary);
			}
			
			similarity.println(sumary.toString());
			
			log.info("Finalização da consulta das classes e categorias na dbpedia que contenham relevância com os títulos.");
			
//			similarityConsolidated.flush();
			similarity.flush();

//			similarityConsolidated.close();
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
	 * @param sumary 
	 * @param concepts 
	 * @return 1 se o titulo não possuir nenhum conceito relevante, ou 0, caso contrário.
	 * @throws IOException Exceção lançada de ID.
	 */
	public static void executeSimilarity(String title, String bagOfWords, /*PrintWriter outConsolidated, */PrintWriter out, List<OntologyConcept> ontologyConcepts, Sumary sumary) 
			throws IOException {
		
		log.info("[INICIO] Início do processamento para o título: " + title);
		
		SearcherConceptysDBPedia searcherConceptys = new SearcherConceptysDBPedia();
		List<Extractor> extractorList = searcherConceptys.searchClassesOrCategories(title);
		List<String> classesUpThreshold = new LinkedList<String>();
		List<String> categoriesUpThreshold = new LinkedList<String>();
		
		if(!extractorList.isEmpty()) {

			out.println("Título do Feature Type: " + title);
			boolean wasAnnotated = false;
			for (Extractor extractor : extractorList) {

				if (!extractor.getClasses().isEmpty()) {
					extractor.setSimilarityClasses(executeCossineSimilarity(extractor.getClasses(), 
						SearcherConceptysDBPedia.CLASS,	bagOfWords, title, extractor.getTitle()/*, outConsolidated*/));
					extractor.setOntologyClasses(getSimilaryConcepts(ontologyConcepts, extractor.getSimilarityClasses()));
					classesUpThreshold.addAll(extractor.getSimilarityClasses());
				}
				if (!extractor.getCategories().isEmpty()) {
					extractor.setSimilarityCategories(executeCossineSimilarity(extractor.getCategories(), 
						SearcherConceptysDBPedia.CATEGORY,	bagOfWords, title, extractor.getTitle()/*, outConsolidated*/));
					extractor.setOntologyCategories(getSimilaryConcepts(ontologyConcepts, extractor.getSimilarityCategories()));
					categoriesUpThreshold.addAll(extractor.getSimilarityCategories());
				}
				
				if (!extractor.getSimilarityClasses().isEmpty() || !extractor.getSimilarityCategories().isEmpty()) {
					out.println("Token: " + extractor.getTitle());
					out.println("Categorias: " + printStringList(extractor.getSimilarityCategories()));
					out.println("Categorias similares: " + printStringList(extractor.getOntologyCategories()));
					out.println("Conceitos: " + printStringList(extractor.getSimilarityClasses()));
					out.println("Conceitos similares: " + printStringList(extractor.getOntologyClasses()));
					out.println("");
				}
				sumary.summarizeResults(extractor);
				if(!extractor.getOntologyClasses().isEmpty() || !extractor.getOntologyCategories().isEmpty()) {
					wasAnnotated = true;
				}
			}
			sumary.setCountFeature(1);
			if(!wasAnnotated) {
				sumary.setCountFeatureNotAnnotated(1);
			}
			out.println("");
		}
		
		log.info("[FIM] Fim do processamento para o título: " + title);
		
	}
	
	/**
	 * Método que faz o processamento necessário para extrair a similaridade entre uma 
	 * bagOfWords e um determinado texto.
	 * 
	 * @param title O título do feature typde.
	 * @param bagOfWords O texto a ser comparado com a bagOfWords.
	 * @param ontologyConcepts 
	 * @throws IOException Exceção lançada de ID.
	 */
	public static Set<String> executeSimilarity(String title, String bagOfWords, List<OntologyConcept> ontologyConcepts) 
			throws IOException {
			
		SearcherConceptysDBPedia searcherConceptys = new SearcherConceptysDBPedia();
		List<Extractor> extractorList = searcherConceptys.searchClassesOrCategories(title);
		Set<String> concepts = new HashSet<String>();
		
		if(!extractorList.isEmpty()) {

			for (Extractor extractor : extractorList) {

				if (!extractor.getClasses().isEmpty()) {
					extractor.setSimilarityClasses(executeCossineSimilarity(extractor.getClasses(), 
						SearcherConceptysDBPedia.CLASS,	bagOfWords, title, extractor.getTitle()));
					extractor.setOntologyClasses(getSimilaryConcepts(ontologyConcepts, extractor.getSimilarityClasses()));
					concepts.addAll(extractor.getOntologyClasses());
				}
				if (!extractor.getCategories().isEmpty()) {
					extractor.setSimilarityCategories(executeCossineSimilarity(extractor.getCategories(), 
						SearcherConceptysDBPedia.CATEGORY,	bagOfWords, title, extractor.getTitle()));
					extractor.setOntologyCategories(getSimilaryConcepts(ontologyConcepts, extractor.getSimilarityCategories()));
					concepts.addAll(extractor.getOntologyCategories());
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
	private static Set<String> executeCossineSimilarity(Set<String> concepts, String type, 
			String bagOfWords, String title, String token/*, PrintWriter outConsolidated*/) throws IOException {
		
		Set<String> conceptsUpThreshold = new HashSet<String>();
		for (String concept : concepts) {
			if (!isConceptDefault(concept)) {										
				SearcherWikipedia searcherText = new SearcherWikipedia(concept);
				String wikiText = searcherText.getText();
				double cosineSimilarity = 0.0;
				if (!bagOfWords.isEmpty() && wikiText != null && !wikiText.isEmpty()) {
					cosineSimilarity = CosineDocumentSimilarity
							.getCosineSimilarity(bagOfWords, wikiText);
				}
//				String line = title + "|" + token + "|" + type + "|" + concept + "|" 
//						+ cosineSimilarity + "|" + searcherText.getWikiUrl() + 
//						"|" + searcherText.getUrl() + "|" + bagOfWords; 
//				outConsolidated.println(line);
				if (cosineSimilarity >= THRESHOLD_COSINE) {
					conceptsUpThreshold.add(concept);
				}
			}
		}
		return conceptsUpThreshold;
	}
	
	/**
	 * Imprime o toString de um conjunto lista sem os parênteses.
	 * @param set O conjunto a ser impresso.
	 * @return A string do conjunto sem os parênteses.
	 */
	private static String printStringList(Set<String> set) {
		
		String setReturn = set.toString();
		setReturn = setReturn.replace("[", "");
		setReturn = setReturn.replace("]", "");
		
		return setReturn;		
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
	 * @param concept Os conceitos a serem pesquisados.
	 * @return O conjunto de conceitos compatíveis ao conceito passado.
	 */
	private static Set<String> getSimilaryConcepts(List<OntologyConcept> ontologyConcepts, Set<String> concepts) {
		
		PreProcessingText preprocessing = new PreProcessingText();
		Set<String> similaryConcepts = new HashSet<String>();
		for (String concept : concepts) {		
			String coveredConcept = preprocessing.preProcessingWithoutExtractScale(concept);
			for (OntologyConcept ontologyConcept : ontologyConcepts) {
				if (ontologyConcept.getNormalizedName().equalsIgnoreCase(coveredConcept) || 
						ontologyConcept.getConceptName().equalsIgnoreCase(coveredConcept)) {				
//				if (ontologyConcept.getNormalizedName().equalsIgnoreCase(concept) || 
//						ontologyConcept.getConceptName().equalsIgnoreCase(concept)) {
					similaryConcepts.add(ontologyConcept.getConcept());
					break;
				}
			}
		}
		return similaryConcepts;
	}
	
}
