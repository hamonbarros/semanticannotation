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

	public static final String WIKIPEDIA_URL = "http://en.wikipedia.org/w/";
	private StringBuffer text;
	private PreProcessingText preProcessing;

	public SearcherWikipedia(String title) {
		
		text = new StringBuffer();
		Logger log = Logger.getLogger(Main.class);
		preProcessing = new PreProcessingText();
		
		title = title.replaceAll(" ", "_");
		HttpClient httpClient = new HttpClient();
		String url = WIKIPEDIA_URL + 
				"api.php?action=query&prop=revisions&rvprop=content&format=xml&redirects&titles=" + title;
		HttpMethod httpMethod = new GetMethod(url);

		try {
			httpClient.executeMethod(httpMethod);
			InputStream in = httpMethod.getResponseBodyAsStream();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(in, this);

		} catch (HttpException he) {
			log.error("Http error connecting to lookup.dbpedia.org");
		} catch (IOException ioe) {
			log.error("Unable to connect to lookup.dbpedia.org");
		} catch (ParserConfigurationException pce) {
			log.error("Do not was possible to read the return of the query to the term \"" + title + "\".");
		} catch (SAXException se) {
			log.error("Do not was possible to instantiate the reader of the return.");
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
	
	public static void main(String[] args) {
		SearcherWikipedia s = new SearcherWikipedia("military conflict");
		System.out.println(s.getText().trim());
	}
}
