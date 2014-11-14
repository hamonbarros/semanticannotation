package br.com.sann.service;

import java.util.List;

import br.com.sann.domain.SpatialData;

/**
 * Interface de neg�cio relacionada com Feature Types.
 * 
 * @author Hamon
 *
 */
public interface FeatureService {
	
	/**
	 * Recupera todos os SpatialData cadastrados.
	 * 
	 * @return Os SpatialData cadastrados.
	 */
	public List<SpatialData> recoverAllSpatialData();

	/**
	 * Recupera a bag of words constru�da a partir dos SpatialData com um 
	 * determinado t�tulo.
	 * 
	 * @param title O t�tulo a ser pesquisado.
	 * @return A bag of words constu�da com as informa��es textuais do SpatialData.
	 */
	public String recoverBagOfWordsByTitle(String title);
}
