package br.com.sann.service.search.wikipedia;

import java.util.List;

import jeeves.utils.Xml;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jdom.Element;

/**
 * 
 * @author Fabio
 * @author Hamon
 */
public class WikipediaCategorySearcher {

	private WikipediaCategoryParser wikiCategoryParser;
	
	public WikipediaCategorySearcher() {
		wikiCategoryParser = new WikipediaCategoryParser();
	}
	
	public static final String WIKIPEDIA_URL = "http://en.wikipedia.org/w/";

	public List<String> getCategory(String categoryName) {
		categoryName = categoryName.replaceAll(" ", "_");
		String queryUrl = "api.php?action=query&prop=categories&format=xml&titles=Category:"
				+ categoryName;
		Element response = search(queryUrl);
		return wikiCategoryParser.getCategories(response);
	}

	public List<String> getCategoryPages(String categoryName) {
		categoryName = categoryName.replaceAll(" ", "_");
		String queryUrl = "api.php?action=query&list=categorymembers&format=xml&cmtitle=Category:"
				+ categoryName;
		Element response = search(queryUrl);
		return wikiCategoryParser.getCategoryMembers(response);
	}

	public List<String> getCategorySubcats(String categoryName) {
		categoryName = categoryName.replaceAll(" ", "_");
		String queryUrl = "api.php?action=query&list=categorymembers&cmtype=subcat|page&format=xml&cmtitle=Category:"
				+ categoryName;
		Element response = search(queryUrl);
		return wikiCategoryParser.getCategoryMembers(response);
	}

	private Element search(String queryUrl) {
		try {

			GetMethod method = new GetMethod(WIKIPEDIA_URL + queryUrl);

			HttpClient client = new HttpClient();
			client.executeMethod(method);
			Element response = Xml.loadStream(method.getResponseBodyAsStream());
			return response;
		} catch (Exception e) {
			System.err
					.println("Não foi possível oberter as categorias dessa consulta: "
							+ queryUrl);
		}
		return null;
	}

}
