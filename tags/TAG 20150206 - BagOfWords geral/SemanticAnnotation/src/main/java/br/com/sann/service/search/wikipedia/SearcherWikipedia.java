package br.com.sann.service.search.wikipedia;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.sann.main.Main;
import br.com.sann.service.processing.text.PreProcessingText;

/**
 * Classe para recuperar o conteúdo de uma página na wikipedia.
 * 
 * @author Hamon
 */
public class SearcherWikipedia extends DefaultHandler {

	public static final String WIKIPEDIA_URL_SEARCH = "http://en.wikipedia.org/w/";
	public static final String WIKIPEDIA_URL = "http://en.wikipedia.org/wiki/";
	
	private StringBuffer text;
	private String url;
	private String wikiUrl;
	private PreProcessingText preProcessing;

	public SearcherWikipedia(String title) {
		
		text = new StringBuffer();
		Logger log = Logger.getLogger(Main.class);
		preProcessing = new PreProcessingText();
		
		title = title.replaceAll(" ", "_");
		HttpClient httpClient = new HttpClient();
		setUrl(WIKIPEDIA_URL_SEARCH + 
				"api.php?action=query&prop=revisions&rvprop=content&format=xml&redirects&titles=" + title);
		setWikiUrl(WIKIPEDIA_URL + title);
		
		HttpMethod httpMethod = new GetMethod(getUrl());

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
				log.error("Wikipedia - Erro HTTP ao tentar conectar o lookup.dbpedia.org - Título: " + title);
				executeTimer(log, searched);
			} catch (IOException ioe) {
				log.error("Wikipedia - Erro ao tentar conectar o lookup.dbpedia.org - Título: " + title);
				executeTimer(log, searched);
			} catch (ParserConfigurationException pce) {
				log.error("Wikipedia - Não foi possível ler o retorno da consulta ao título \"" + title + "\".");
				executeTimer(log, searched);
			} catch (SAXException se) {
				log.error("Wikipedia - Não foi possível instancia o leitor do retorno - Título: " + title);
				executeTimer(log, searched);
			}
		}
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
	 * Método sobrescrito da superclasse que faz a leitura das tags do XML de retorno.
	 * 
	 * @param ch O array de caracteres do XML retornado.
	 * @param start O indice de início da leitura no array.
	 * @param length O tamanho da palavra a ser lida.
	 */
	@Override
	public void characters(char[] ch, int start, int length) {
		text.append(new String(ch, start, length).trim());
	}

	/**
	 * Método para retornar o texto contido na pagina da wikipedia.
	 * 
	 * @return O texto existente na página da wikipedia.
	 */
	public String getText() {
		List<String> nounsAndAdjectives = preProcessing.preProcessing(text.toString());
		return preProcessing.tokensToString(nounsAndAdjectives).trim();
	}
	
	/**
	 * Recupera a url que está sendo consultada.
	 * @return A url consultada.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Define a url a ser consultada.
	 * @param url A nova url.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Recupera a URL da Wikipedia referente ao termo pesquisado.
	 * @return A URL do termo pesquisado.
	 */
	public String getWikiUrl() {
		return wikiUrl;
	}

	/**
	 * Define a URL da Wikipedia referente ao termo pesquisado.
	 * @param wikiUrl A nova URL do termo pesquisado.
	 */
	public void setWikiUrl(String wikiUrl) {
		this.wikiUrl = wikiUrl;
	}

	public static void main(String[] args) {
		SearcherWikipedia s = new SearcherWikipedia("military conflict");
		System.out.println(s.getText().trim());
	}
}
