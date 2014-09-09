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

import br.com.sann.domain.SpatialData;
import br.com.sann.service.feature.FeatureService;
import br.com.sann.service.feature.impl.FeatureServiceImpl;
import br.com.sann.service.search.dbpedia.SearcherCategoriesDBPedia;

public class Main {	

	/**
	 * Método principal.
	 * 
	 * @param args Os argumentos de entrada.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		InputStream in = new Main().getClass().getClassLoader()
				.getResourceAsStream("config.properties");  
		Properties props = new Properties();  
		props.load(in);
		in.close();
		String path = props.getProperty("PATH_TREETAGGER");

		mapedCategories(path);

	}

	/**
	 * Método que gera os arquivos contendo o mapeamento ou não dos serviços 
	 * de uma IDE com as respectivas categorias da wikipédia.
	 * 
	 * @param pathTreeTagger O caminho da instalação do treeTagger.
	 */
	private static void mapedCategories(String pathTreeTagger) {

		FeatureService service = new FeatureServiceImpl();
		List<SpatialData> spatialDataList = service.recoverAllSpatialData();
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
			SearcherCategoriesDBPedia searcher = new SearcherCategoriesDBPedia();
//			for (SpatialData spatialData : spatialDataList) {
			for(int i=0; i<30; i++){
				String title = spatialDataList.get(i).getTitle();
//				String title = "Annotation (1:250K)";
				if (!title.equals(previousTitle)) {		
					
					Map<String, Set<String>> classesOrCategoriesMap = searcher.searchClassesOrCategories(title);
					
					if(!classesOrCategoriesMap.isEmpty()) {
						String titleToken = classesOrCategoriesMap.keySet().iterator().next();
						Set<String> categories = classesOrCategoriesMap.get(titleToken);
						if (!categories.isEmpty()) {
							for (String token : classesOrCategoriesMap.keySet()) {
								categories = classesOrCategoriesMap.get(titleToken);
								String line = title + "|" + token + "|";
								for (String category : categories) {
									line += category + "-";
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
			titlesCategorized.flush();
			titleUncategorized.flush();

			titlesCategorized.close();
			titleUncategorized.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
