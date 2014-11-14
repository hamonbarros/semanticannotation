package br.com.sann.service.search.dbpedia;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
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

import br.com.sann.main.Main;

public class SearcherParentConcepts extends DefaultHandler {
	
	private Set<String> parentsCategories;
	private Set<String> parentsClasses;
	
	private String elementName;
	private String elementParentName;

	public SearcherParentConcepts(String url) {

		Logger log = Logger.getLogger(Main.class);

		HttpClient httpClient = new HttpClient();

		HttpMethod httpMethod = new GetMethod(url);
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
				log.error("DBPedia - Erro HTTP ao tentar conectar a url: " + url);
				searched = true;
			} catch (IOException ioe) {
				log.error("DBPedia - Erro ao tentar conectar a url: " + url);
				searched = false;
				try {
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					log.error("DBPedia - falha no temporizador da thread");
					searched = true;
				}
			} catch (ParserConfigurationException pce) {
				log.error("DBPedia - Não foi possível ler o retorno da consulta a url \"" + url + "\".");
				searched = true;
			} catch (SAXException se) {
				log.error("DBPedia - Não foi possível instancia o leitor do retorno da url: " + url);
				searched = true;
			}
		}
		httpMethod.releaseConnection();
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
		if (currentName.equalsIgnoreCase("html")) {			
			parentsCategories = new LinkedHashSet<String>();
			parentsClasses = new LinkedHashSet<String>();
		}
		elementName = currentName;
	}

//	/**
//	 * Método sobrescrito da superclasse que finaliza a leitura do XML de retorno.
//	 * 
//	 * @param uri A URI.
//	 * @param localName O nome local.
//	 * @param currentName A tag atual sendo lida.
//	 */
//	@Override
//	public void endElement(String uri, String localName, String currentName)
//			throws SAXException {
//		if (currentName.equalsIgnoreCase("html")) {
//			parentsCategories.addAll(tempCategories);
//			classes.addAll(tempClasses);
//		}
//	}

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

		if ("Category".equals(elementName)) {
			elementParentName = elementName;
		}
		if ("Label".equals(elementName) && "Category".equals(elementParentName)) {
			if (s != null && !s.isEmpty()) {
				parentsCategories.add(s);
			}
			elementParentName = "";
		}
		if ("Class".equals(elementName)) {
			elementParentName = elementName;
		}
		if ("Label".equals(elementName) && "Class".equals(elementParentName)) {
			if (s != null && !s.isEmpty()) {
				parentsClasses.add(s);
			}
			elementParentName = "";
		}
	}
	
	/**
	 * Recupera a lista de supercategorias recuperadas na DBPedia a partir da url consultada.
	 * 
	 * @return  A lista de supercategorias recuperadas na DBPedia.
	 */
	public Set<String> getParentCategories() {
		return this.parentsCategories;
	}
	
	/**
	 * Recupera a lista de superclasses recuperadas na DBPedia a partir da url consultada.
	 * 
	 * @return A lista de superclasses recuperadas na DBPedia.
	 */
	public Set<String> getParentClasses() {
		return this.parentsClasses;
	}
	
	public static void main(String[] args) {
		SearcherParentConcepts s = new SearcherParentConcepts("http://dbpedia.org/resource/Category:Plants");
		System.out.println("Categorias: " + s.getParentCategories());
		System.out.println("Classes: " + s.getParentClasses());
	}
}
