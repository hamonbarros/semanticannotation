package br.com.sann.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

		mapedClassesOrCategories(path);
//		extractSimilarity(path);
		
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
		PrintWriter similarity = null;
		try {
			DateFormat df = new SimpleDateFormat("YYYYMMddHHmm");
			String dateFormated = df.format(new Date());

			similarity = new PrintWriter(new FileWriter(new File(
					"Similarity" + dateFormated + ".csv")));

			similarity.println("Title|Title Tokenizing|Classe/Category|Cosine|Bag of Words|Wikipedia Text");

			String previousTitle = "";
			log.info("Inicio da consulta das classes ou categorias na dbpedia...");
			SearcherCategoriesDBPedia searcherCategories = new SearcherCategoriesDBPedia();
//			for (SpatialData spatialData : spatialDataList) {
			for(int i=0; i<30; i++){
				String title = spatialDataList.get(i).getTitle();
//				String title = "Wetlands - Polygons (1:20K)";
				if (!title.equals(previousTitle)) {		
					BagOfWords bw = new BagOfWords(spatialDataList.get(i));
					System.out.println(bw.extractWordList());
					
					Map<String, Set<String>> classesOrCategoriesMap = searcherCategories.searchClassesOrCategories(title);
					
					if(!classesOrCategoriesMap.isEmpty()) {
						String titleToken = classesOrCategoriesMap.keySet().iterator().next();
						Set<String> classesOrCategories = classesOrCategoriesMap.get(titleToken);
						if (!classesOrCategories.isEmpty()) {
							for (String token : classesOrCategoriesMap.keySet()) {
								classesOrCategories = classesOrCategoriesMap.get(token);
								for (String classOrCategory : classesOrCategories) {
									if (!isClassDefault(classOrCategory)) {										
										SearcherWikipedia searcherText = new SearcherWikipedia(classOrCategory);
										String wikiText = searcherText.getText();
										String bwText = bw.extractWordList();
										double cosineSimilarity = 0.0;
										if (bw != null && !bwText.isEmpty() && wikiText != null && !wikiText.isEmpty()) {
											cosineSimilarity = CosineDocumentSimilarity
													.getCosineSimilarity(bwText, wikiText);
										}
										String line = title + "|" + token + "|" + classOrCategory + "|" 
												+ cosineSimilarity + "|" + bwText + "|" + wikiText; 
										similarity.println(line);
									}
								}
							}
						}
					}					
					previousTitle = title;
				}
								
			}
			log.info("Finalização da consulta das classes ou categorias na dbpedia!");
			
			similarity.flush();

			similarity.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método para identificar se o token passado corresponde a uma classe padrão.
	 * @param token O token a ser verificado.
	 * @return True se corresponder, ou false, caso contrátio.
	 */
	private static boolean isClassDefault(String token) {
		
		if ("%CLASS%".equals(token) || "%CATEGORY%".equals(token) || "owl#Thing".equals(token)) {
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
					
					Map<String, Set<String>> classesOrCategoriesMap = searcher.searchClassesOrCategories(title);
					
					if(!classesOrCategoriesMap.isEmpty()) {
						String titleToken = classesOrCategoriesMap.keySet().iterator().next();
						Set<String> classesOrCategories = classesOrCategoriesMap.get(titleToken);
						if (!classesOrCategories.isEmpty()) {
							for (String token : classesOrCategoriesMap.keySet()) {
								classesOrCategories = classesOrCategoriesMap.get(token);
								String line = title + "|" + token;
								for (String classOrCategory : classesOrCategories) {
									line += "|" + classOrCategory;
//							    System.out.println(line);
								}
								titlesCategorized.println(line);
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
