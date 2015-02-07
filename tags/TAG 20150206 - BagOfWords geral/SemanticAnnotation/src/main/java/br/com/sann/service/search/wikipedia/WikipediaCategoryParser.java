package br.com.sann.service.search.wikipedia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import br.com.sann.util.DomElementManager;

/**
 * Classe que irá recuperar as categorias de uma determinada página da wikipedia.
 * @author Fabio
 * @author Hamon
 */
public class WikipediaCategoryParser {

	public List<String> getCategories(Element queryResponse) {
		List<String> result = new ArrayList();
		try {
			String path = "query/pages/page/categories";

			Element categoriesElement = DomElementManager.findElementByPath(
					queryResponse, path);
			if (categoriesElement != null) {
				Iterator<Element> categories = DomElementManager
						.findElementSetByPath(categoriesElement, "cl")
						.iterator();
				while (categories.hasNext()) {
					Element category = categories.next();
					String categoryName = DomElementManager.findAtributte(
							category, "title").getValue();
					int pos = categoryName.indexOf(":");
					result.add(categoryName.substring(pos + 1));
				}
			}

		} catch (Exception e) {
			System.err
					.println("Não foi possível encontrar as categorias do elemento: "
							+ queryResponse);
		}

		return result;
	}

	public List<String> getCategoryMembers(Element queryResponse) {
		List<String> result = new ArrayList();
		try {
			String path = "query/categorymembers";

			Element categoriesElement = DomElementManager.findElementByPath(
					queryResponse, path);
			if (categoriesElement != null) {
				Iterator<Element> categories = DomElementManager
						.findElementSetByPath(categoriesElement, "cm")
						.iterator();
				while (categories.hasNext()) {
					Element category = categories.next();
					String categoryName = DomElementManager.findAtributte(
							category, "title").getValue();
					int pos = categoryName.indexOf(":");
					result.add(categoryName.substring(pos + 1));
				}
			}

		} catch (Exception e) {
			System.err
					.println("Não foi possível encontrar os membros das categorias do elemento: "
							+ queryResponse);
		}

		return result;
	}

}
