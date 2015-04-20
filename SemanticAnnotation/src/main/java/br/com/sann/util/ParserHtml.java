package br.com.sann.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.sann.main.Main;

/**
 * Classe para realizar a leitura de uma página HTML.
 * 
 * @author Hamon
 *
 */
public class ParserHtml {
	
	private Document document;
	
	Logger log = Logger.getLogger(ParserHtml.class);
	
	/**
	 * Construtor padrão.
	 */
	public ParserHtml() {

	}

	/**
	 * Faz a leitura de uma URL passada como parâmetro.
	 * 
	 * @param url
	 *            A URL a ser lida.
	 * @throws ParserHtmlException
	 *             Exceção lançada caso haja algum problema na leitura da url.
	 */
	public void readUrl(String url) throws ParserHtmlException {
		try {
			this.document = Jsoup.connect(url).get();
		} catch (Exception e) {
			String message = "ParserHtml - Erro ao tentar conectar a url: " + url;
			log.error(message);
			throw new ParserHtmlException(message);
		}
	}
	
	/**
	 * Retorna os elementos da URL que possuem o nome do estilo passado como parâmetro.
	 * 
	 * @param className O nome do estilo.
	 * @return Os elementos que possuem o nome do estilo.
	 */
	private Elements getElementsByClass(String className) {
		Elements elements = document.getElementsByClass(className);
		return elements;
	}
	
	/**
	 * Retorna as urls da dbpedia que possuem os atributos rev com o valor "owl:sameAs"
	 * ou rel com o valor "rdf:type" e que contenham no meio do valor o termo passado
	 * como parâmetro.
	 * 
	 * @param term O termo a ser comparado.
	 * @return As urls da dbpedia.
	 */
	public Set<String> getUrlsDbpediaWithTerm(String term) {
		
		Set<String> result = new HashSet<String>();
		Elements uris = getElementsByClass("uri");
		for (Element uri : uris) {
			String rev = uri.attr("rev");
			String rel = uri.attr("rel");
			if ((rev != null && rev.equals("owl:sameAs")) || (rel != null && rel.equals("rdf:type"))) {
				String href = uri.attr("href");
				if (href.indexOf(term) >= 0) {						
					result.add(href);
				}
			}				
		}		
		return result;
	}
	
	public static void main(String[] args) {
		
		try {
			ParserHtml parser = new ParserHtml();
			parser.readUrl("http://dbpedia.org/page/Category:Subdi");
			for (String uri : parser.getUrlsDbpediaWithTerm("yago")) {
				System.out.println(uri);
			}
		} catch (ParserHtmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
