package br.com.sann.service.search.dbpedia;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.sann.domain.DBpediaCategory;
import br.com.sann.domain.DBpediaClass;
import br.com.sann.main.Main;

/**
 * Classe para fazer a pesquisar termos no serviço da DBPedia.
 * 
 * @author Hamon
 *
 */
public class SearcherDBpediaLookup extends DefaultHandler {

	private Set<DBpediaCategory> categories;
	private Set<DBpediaCategory> tempCategories;
	private Set<DBpediaClass> classes;
	private Set<DBpediaClass> tempClasses;
	
	private String elementName;
	private String elementBrotherName;
	private String elementParentName;
	private String elementBrotherValue;

	public SearcherDBpediaLookup(String term) {

		Logger log = Logger.getLogger(Main.class);
		
		categories = new LinkedHashSet<DBpediaCategory>();
		classes = new LinkedHashSet<DBpediaClass>();
		HttpClient httpClient = new HttpClient();

		String prefixSearchURL = "http://lookup.dbpedia.org/api/search.asmx/PrefixSearch?QueryString=";
		String keywordSearchURL = "http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryString=";
		String dbpediaTerm = term.replaceAll(" ", "+"); 
		HttpMethod httpMethod = new GetMethod(prefixSearchURL + dbpediaTerm);
		boolean searched = false;
		while (!searched) {	
			try {
				httpClient.executeMethod(httpMethod);
				InputStream in = httpMethod.getResponseBodyAsStream();
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				saxParser.parse(in, this);
				searched = true;
			} catch (HttpException he) {
				log.error("DBPedia - Erro HTTP ao tentar conectar o lookup.dbpedia.org - Termo: " + term);
				executeTimer(log, searched);
			} catch (IOException ioe) {
				log.error("DBPedia - Erro ao tentar conectar o lookup.dbpedia.org - Termo: " + term);
				executeTimer(log, searched);
			} catch (ParserConfigurationException pce) {
				log.error("DBPedia - Não foi possível ler o retorno da consulta ao termo \"" + term + "\".");
				executeTimer(log, searched);
			} catch (SAXException se) {
				log.error("DBPedia - Não foi possível instancia o leitor do retorno - Termo: " + term);
				executeTimer(log, searched);
			}
		}
		httpMethod.releaseConnection();
	}

	private void executeTimer(Logger log, boolean searched) {
		searched = false;
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			log.error("DBPedia - falha no temporizador da thread");
			searched = true;
		}
	}
	
	/**
	 * Método sobrescrito da superclasse que inicia a leitura do XML de retorno.
	 * 
	 * @param uri A URI.
	 * @param localName O nome local.
	 * @param currentName A tag atual sendo lida.
	 */
	@Override
	public void startElement(String uri, String localName, String currentName,
			Attributes attributes) throws SAXException {
		if (currentName.equalsIgnoreCase("result")) {
			tempCategories = new LinkedHashSet<DBpediaCategory>();
			tempClasses = new LinkedHashSet<DBpediaClass>();
		}
		elementName = currentName;
	}

	/**
	 * Método sobrescrito da superclasse que finaliza a leitura do XML de retorno.
	 * 
	 * @param uri A URI.
	 * @param localName O nome local.
	 * @param currentName A tag atual sendo lida.
	 */
	@Override
	public void endElement(String uri, String localName, String currentName)
			throws SAXException {
		if (currentName.equalsIgnoreCase("result")) {
			categories.addAll(tempCategories);
			classes.addAll(tempClasses);
		}
	}

	/**
	 * Método sobrescrito da superclasse que faz a leitura das tags do XML de retorno.
	 * 
	 * @param ch O array de caracteres do XML retornado.
	 * @param start O indice de início da leitura no array.
	 * @param length O tamanho da palavra a ser lida.
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = new String(ch, start, length).trim();
		
		if ("Result".equals(elementName)) {
			elementParentName = elementName;
		}
		if ("Label".equals(elementName) && "Result".equals(elementParentName)) {
			elementBrotherName = elementName;
			if (s != null && !s.isEmpty()) {				
				elementBrotherValue = s;
			}
		}
		if ("URI".equals(elementName) && "Result".equals(elementParentName) && "Label".equals(elementBrotherName)) {
			if (s != null && !s.isEmpty()) {
				DBpediaClass clas = new DBpediaClass(elementBrotherValue, s);
				tempClasses.add(clas);
			}
			elementParentName = "";
			elementBrotherName = "";
		}
		if ("Category".equals(elementName)) {
			elementParentName = elementName;
		}
		if ("Label".equals(elementName) && "Category".equals(elementParentName)) {
			elementBrotherName = elementName;
			if (s != null && !s.isEmpty()) {				
				elementBrotherValue = s;
			}
		}
		if ("URI".equals(elementName) && "Category".equals(elementParentName) && "Label".equals(elementBrotherName)) {
			if (s != null && !s.isEmpty()) {
				DBpediaCategory cat = new DBpediaCategory(elementBrotherValue, s);
				tempCategories.add(cat);
			}
			elementParentName = "";
			elementBrotherName = "";
		}
		if ("Class".equals(elementName)) {
			elementParentName = elementName;
		}
		if ("Label".equals(elementName) && "Class".equals(elementParentName)) {
			elementBrotherName = elementName;
			if (s != null && !s.isEmpty()) {				
				elementBrotherValue = s;
			}
		}
		if ("URI".equals(elementName) && "Class".equals(elementParentName) && "Label".equals(elementBrotherName)) {
			if (s != null && !s.isEmpty()) {
				DBpediaClass clas = new DBpediaClass(elementBrotherValue, s);
				tempClasses.add(clas);
			}
			elementParentName = "";
			elementBrotherName = "";
		}
	}
	
	/**
	 * Recupera a lista de categorias recuperadas na DBPedia a partir do termo consultado.
	 * 
	 * @return  A lista de categorias recuperadas na DBPedia.
	 */
	public Set<DBpediaCategory> getCategories() {
		return this.categories;
	}
	
	/**
	 * Recupera a lista de classes recuperadas na DBPedia a partir do termo consultado.
	 * 
	 * @return A lista de classes recuperadas na DBPedia.
	 */
	public Set<DBpediaClass> getClasses() {
		return this.classes;
	}
	
}
