package br.com.sann.service.processing.text;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.sann.domain.Service;
import br.com.sann.domain.SpatialData;

/**
 * Classe utilitária para a montagem das BagOfWords contendo os atributos
 * textuais das entidades SpatialData e Service.
 * 
 * @author Hamon
 *
 */
public class BagOfWords {
	
	private SpatialData spatialData;
	
	/**
	 * Construtor.
	 * 
	 * @param spatialData A entidade cujos atributos textuais serão explorados.
	 */
	public BagOfWords(SpatialData spatialData) {
		
		this.spatialData = spatialData;
	}
	
	/**
	 * Extrais os atributos textuais de uma SpatialData.
	 * 
	 * @return Os atributos textuais de uma SpatialData.
	 */
	public String extractTextProperties() {
		
		String text = "";
		
		if (spatialData != null) {			
			text += spatialData.getTitle() == null ? "" : spatialData.getTitle();
			text += spatialData.getKeywords() == null ? "" : (" " + spatialData.getKeywords());
			text += spatialData.getName() == null ? "" : (" " + spatialData.getName());
			text += spatialData.getTextDescription() == null ? "" : (" " + spatialData.getTextDescription());
			
			Service serv = spatialData.getService();
			if (serv != null) {				
				text += serv.getKeywords() == null ? "" : (" " + serv.getKeywords());
				text += serv.getName() == null ? "" : (" " + serv.getName());
				text += serv.getTextDescription() == null ? "" : (" " + serv.getTextDescription());
			}
		}
		return text;
	}
	
	/**
	 * Retorna os nomes e os adjetivos exitentes na BagOfWords construída
	 * com os atributos textuais do SpatialData.
	 * 
	 * @return A lista de substantivos e adjetivos da BagOfWords.
	 */
	public String extractWordList() {
		
		PreProcessingText preProcessing = PreProcessingText.getInstance();
		List<String> nounsAndAdjectives = preProcessing.preProcessing(extractTextProperties());
		Set<String> tokenSet = new HashSet<String>();
		tokenSet.addAll(nounsAndAdjectives);
		return preProcessing.tokensToString(tokenSet);
		
	}
	
	/**
	 * Retorna os nomes e os adjetivos exitentes em um determinado texto.
	 * 
	 * @param text O texto a ser processado.
	 * @return A lista de substantivos e adjetivos do texto.
	 */
	public String extractWordList(String text) {
		
		PreProcessingText preProcessing = PreProcessingText.getInstance();
		text = preProcessing.extractUnderline(text);
		text = preProcessing.tokenizingTextWithUppercase(text);
		text = preProcessing.extractPunctuation(text);
		List<String> nounsAndAdjectives = preProcessing.preProcessing(text);
		Set<String> tokenSet = new HashSet<String>();
		tokenSet.addAll(nounsAndAdjectives);
		return preProcessing.tokensToString(tokenSet);
		
	}

}
