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

import br.com.sann.domain.SpatialData;
import br.com.sann.service.feature.FeatureService;
import br.com.sann.service.feature.impl.FeatureServiceImpl;
import br.com.sann.service.processing.text.BagOfWords;
import br.com.sann.service.search.dbpedia.SearcherCategoriesDBPedia;
import br.com.sann.service.search.wikipedia.SearcherWikipedia;
import br.com.sann.service.similarity.CosineDocumentSimilarity;

public class Main {	

	public static Logger log;
	/**
	 * Método principal.
	 * 
	 * @param args Os argumentos de entrada.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
		
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
	 * Método que gera os arquivos contendo o mapeamento ou não dos serviços 
	 * de uma IDE com as respectivas categorias da wikipédia.
	 * 
	 * @param pathTreeTagger O caminho da instalação do treeTagger.
	 */
	private static void extractSimilarity(String pathTreeTagger) {

		log.info("Realizando a leitura das feature types... ");
		FeatureService service = new FeatureServiceImpl();
		List<SpatialData> spatialDataList = service.recoverAllSpatialData();
		log.info("Leitura das feature types realizada com sucesso!");
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
			log.info("Inicio da consulta das classes ou categorias na dbpedia...");
			
			StringBuffer storeBagsOfWords = new StringBuffer();
			BagOfWords bw = null;
			
			for (SpatialData spatialData : spatialDataList) {
//			for(int i=0; i<30; i++){
				String title = spatialData.getTitle();
//				String title = "Wetlands - Polygons (1:20K)";

				bw = new BagOfWords(spatialData);
				
				if (title.equals(previousTitle)) {
					
					storeBagsOfWords.append(" ");
					storeBagsOfWords.append(bw.extractTextProperties());
					
				} else {
					
					if (previousTitle.equals("")) {
						storeBagsOfWords.append(bw.extractTextProperties());
						previousTitle = title;
						continue;						
					}
					
					executeSimilarity(previousTitle, bw, storeBagsOfWords.toString(), similarityConsolidated, similarity);
					
					storeBagsOfWords = new StringBuffer();
					storeBagsOfWords.append(bw.extractTextProperties());
					previousTitle = title;

				}
								
			}
			
			if (!previousTitle.isEmpty() && bw != null) {				
				executeSimilarity(previousTitle, bw, storeBagsOfWords.toString(), similarityConsolidated, similarity);
			}
			
			log.info("Finalização da consulta das classes ou categorias na dbpedia!");
			
			similarityConsolidated.flush();
			similarity.flush();

			similarityConsolidated.close();
			similarity.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que executa o processamento necessário para extrair a similaridade entre uma 
	 * bagOfWords e um determinado texto.
	 * 
	 * @param title O título do feature typde.
	 * @param bw A bagOfWords que contém os valores das propriedades texto do feature type.
	 * @param text O texto a ser comparado com a bagOfWords.
	 * @param outConsolidated Arquivo onde estão sendo impressos os resultados consolidados.
	 * @param out Arquivo onde estão sendo impressos os resultados resumidos.
	 * @throws IOException Exceção lançada de ID.
	 */
	private static void executeSimilarity(String title, BagOfWords bw, String text, 
			PrintWriter outConsolidated, PrintWriter out) 
			throws IOException {
		
		SearcherCategoriesDBPedia searcherCategories = new SearcherCategoriesDBPedia();
		Map<String, Map<String, Set<String>>> tokensMap = searcherCategories.searchClassesOrCategories(title);
		
		if(!tokensMap.isEmpty()) {
			String titleToken = tokensMap.keySet().iterator().next();
			Map<String, Set<String>> classesAndCategoriesMap = tokensMap.get(titleToken);
			if (!classesAndCategoriesMap.isEmpty()) {
				List<String> classesUpThreshold = new LinkedList<String>();
				List<String> categoriesUpThreshold = new LinkedList<String>();
				for (String token : tokensMap.keySet()) {
					classesAndCategoriesMap = tokensMap.get(token);
					Set<String> classes = classesAndCategoriesMap.get(SearcherCategoriesDBPedia.CLASS);
					for (String clas : classes) {
						if (!isClassDefault(clas)) {										
							SearcherWikipedia searcherText = new SearcherWikipedia(clas);
							String wikiText = searcherText.getText();
							String bwText = bw.extractWordList(text);
							double cosineSimilarity = 0.0;
							if (bw != null && !bwText.isEmpty() && wikiText != null && !wikiText.isEmpty()) {
								cosineSimilarity = CosineDocumentSimilarity
										.getCosineSimilarity(bwText, wikiText);
							}
							String line = title + "|" + token + "|Class|" + clas + "|" 
									+ cosineSimilarity + "|" + searcherText.getWikiUrl() + 
									"|" + searcherText.getUrl() + "|" + bwText; 
							outConsolidated.println(line);
							if (cosineSimilarity >= 0.1) {
								classesUpThreshold.add(clas + " (" + format(cosineSimilarity) + ")");
							}
						}
					}
					Set<String> categories = classesAndCategoriesMap.get(SearcherCategoriesDBPedia.CATEGORY);
					for (String category : categories) {
						if (!isClassDefault(category)) {										
							SearcherWikipedia searcherText = new SearcherWikipedia(category);
							String wikiText = searcherText.getText();
							String bwText = bw.extractWordList(text);
							double cosineSimilarity = 0.0;
							if (bw != null && !bwText.isEmpty() && wikiText != null && !wikiText.isEmpty()) {
								cosineSimilarity = CosineDocumentSimilarity
										.getCosineSimilarity(bwText, wikiText);
							}
							String line = title + "|" + token + "|Category|" + category + "|" 
									+ cosineSimilarity + "|" + searcherText.getWikiUrl() + 
									"|" + searcherText.getUrl() + "|" + bwText; 
							outConsolidated.println(line);
							if (cosineSimilarity >= 0.1) {
								categoriesUpThreshold.add(category + " (" + format(cosineSimilarity) + ")");
							}
						}
					}
				}
				out.println("Título do Feature Type: " + title);
				out.println("Categorias: " + printStringList(categoriesUpThreshold));
				out.println("Conceitos: " + printStringList(classesUpThreshold));
				out.println("");
			}
		}
	}
	
	public static String format(double x) {  
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
	 * Método para identificar se o token passado corresponde a uma classe padrão.
	 * @param token O token a ser verificado.
	 * @return True se corresponder, ou false, caso contrátio.
	 */
	private static boolean isClassDefault(String token) {
		
		if ("owl#Thing".equals(token)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Método que extrai a similaridade entre os campos textuais das feature types de 
	 * uma IDE e os textos da respectivas páginas da wikipédia.
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
			SearcherCategoriesDBPedia searcher = new SearcherCategoriesDBPedia();
//			for (SpatialData spatialData : spatialDataList) {
			for(int i=0; i<30; i++){
				String title = spatialDataList.get(i).getTitle();
//				String title = "Wetlands - Polygons (1:20K)";
				if (!title.equals(previousTitle)) {		
					BagOfWords bw = new BagOfWords(spatialDataList.get(i));
					System.out.println(bw.extractWordList());
					
					Map<String, Map<String, Set<String>>> tokensMap = searcher.searchClassesOrCategories(title);
					
					if(!tokensMap.isEmpty()) {
						String titleToken = tokensMap.keySet().iterator().next();
						Map<String, Set<String>> classesAndCategoriesMap = tokensMap.get(titleToken);
						if (!classesAndCategoriesMap.isEmpty()) {
							for (String token : tokensMap.keySet()) {
								classesAndCategoriesMap = tokensMap.get(token);
								
								Set<String> classes = classesAndCategoriesMap.get(SearcherCategoriesDBPedia.CLASS);
								if (!classes.isEmpty()) {									
									String line = title + "|" + token;
									for (String c : classes) {
										line += "|" + c;
									}
									titlesCategorized.println(line);
								}
								
								Set<String> categories = classesAndCategoriesMap.get(SearcherCategoriesDBPedia.CATEGORY);
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
//							System.out.println(line);
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
