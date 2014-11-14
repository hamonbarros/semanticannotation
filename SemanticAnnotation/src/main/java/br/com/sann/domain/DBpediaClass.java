package br.com.sann.domain;

/**
 * Classe para armazenar as informações das classes encontradas na dbpedia.
 * 
 * @author Hamon
 */
public class DBpediaClass {

	private String label;
	private String url;
	
	public DBpediaClass(String label, String url) {
		this.label = label;
		this.url = url;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
