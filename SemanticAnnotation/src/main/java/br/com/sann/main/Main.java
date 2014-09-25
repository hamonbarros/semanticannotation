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
	 * M�todo principal.
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
	 * M�todo que gera os arquivos contendo o mapeamento ou n�o dos servi�os 
	 * de uma IDE com as respectivas categorias da wikip�dia.
	 * 
	 * @param pathTreeTagger O caminho da instala��o do treeTagger.
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

			similarity.println("Title|Title Tokenizing|Classe/Category|Cosine|URL Wikipedia|URL Wikipedia Search|Bag of Words");

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
					
					executeSimilarity(title, bw, storeBagsOfWords.toString(), similarity);
					
					storeBagsOfWords = new StringBuffer();
					storeBagsOfWords.append(bw.extractTextProperties());
					previousTitle = title;

				}
								
			}
			
			if (!previousTitle.isEmpty() && bw != null) {				
				executeSimilarity(previousTitle, bw, storeBagsOfWords.toString(), similarity);
			}
			
			log.info("Finaliza��o da consulta das classes ou categorias na dbpedia!");
			
			similarity.flush();

			similarity.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * M�todo que executa o processamento necess�rio para extrair a similaridade entre uma 
	 * bagOfWords e um determinado texto.
	 * 
	 * @param title O t�tulo do feature typde.
	 * @param bw A bagOfWords que cont�m os valores das propriedades texto do feature type.
	 * @param text O texto a ser comparado com a bagOfWords.
	 * @param out Arquivo onde est�o sendo impressos os resultados.
	 * @throws IOException Exce��o lan�ada de ID.
	 */
	private static void executeSimilarity(String title, BagOfWords bw, String text, PrintWriter out) 
			throws IOException {
		
		SearcherCategoriesDBPedia searcherCategories = new SearcherCategoriesDBPedia();
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
							String bwText = bw.extractWordList(text);
							double cosineSimilarity = 0.0;
							if (bw != null && !bwText.isEmpty() && wikiText != null && !wikiText.isEmpty()) {
								cosineSimilarity = CosineDocumentSimilarity
										.getCosineSimilarity(bwText, wikiText);
							}
							String line = title + "|" + token + "|" + classOrCategory + "|" 
									+ cosineSimilarity + "|" + searcherText.getWikiUrl() + 
									"|" + searcherText.getUrl() + "|" + bwText; 
							out.println(line);
						}
					}
				}
			}
		}
	}
	
	/**
	 * M�todo para identificar se o token passado corresponde a uma classe padr�o.
	 * @param token O token a ser verificado.
	 * @return True se corresponder, ou false, caso contr�tio.
	 */
	private static boolean isClassDefault(String token) {
		
		if ("%CLASS%".equals(token) || "%CATEGORY%".equals(token) || "owl#Thing".equals(token)) {
			return true;
		}
		return false;
	}
	
	/**
	 * M�todo que extrai a similaridade entre os campos textuais das feature types de 
	 * uma IDE e os textos da respectivas p�ginas da wikip�dia.
	 * 
	 * @param pathTreeTagger O caminho da instala��o do treeTagger.
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
			log.info("Finaliza��o da consulta das classes ou categorias na dbpedia!");
			
			titlesCategorized.flush();
			titleUncategorized.flush();

			titlesCategorized.close();
			titleUncategorized.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
