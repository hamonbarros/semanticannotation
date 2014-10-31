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
	 * Recupera o SpatialData a partir do seu título.
	 * 
	 * @param title O título a ser pesquisado.
	 * @return O SpatialData referente ao título.
	 */
	public SpatialData recoverSpatialDataByTitle(String title);
}
