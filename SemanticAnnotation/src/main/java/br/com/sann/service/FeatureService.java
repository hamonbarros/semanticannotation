package br.com.sann.service;

import java.util.List;

import br.com.sann.domain.SpatialData;

/**
 * Interface de negócio relacionada com Feature Types.
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
	 * Recupera a bag of words construída a partir dos SpatialData com um 
	 * determinado título.
	 * 
	 * @param title O título a ser pesquisado.
	 * @return A bag of words constuída com as informações textuais do SpatialData.
	 */
	public String recoverBagOfWordsByTitle(String title);
}
