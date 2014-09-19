package br.com.sann.service.processing.text;

import java.util.List;

import br.com.sann.domain.Service;
import br.com.sann.domain.SpatialData;

/**
 * Classe utilit�ria para a montagem das BagOfWords contendo os atributos
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
	 * @param spatialData A entidade cujos atributos textuais ser�o explorados.
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
	 * Retorna os nomes e os adjetivos exitentes na BagOfWords constru�da
	 * com os atributos textuais do SpatialData.
	 * 
	 * @return A lista de substantivos e adjetivos da BagOfWords.
	 */
	public String extractWordList() {
		
		PreProcessingText preProcessing = new PreProcessingText();
		List<String> nounsAndAdjectives = preProcessing.preProcessing(extractTextProperties());
		return preProcessing.tokensToString(nounsAndAdjectives);
	}

}