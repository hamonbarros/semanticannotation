package br.com.sann.criteria.dao;

import java.util.List;

import br.com.sann.domain.SpatialData;

/**
 * Interface de persit�ncia da entidade SpatialData.
 * 
 * @author Hamon
 *
 */
public interface SpatialDataDAO {
	
	/**
	 * Recupera todos os SpatialData cadastrados.
	 * 
	 * @return Os SpatialData cadastrados.
	 */
	public List<SpatialData> recoverAllSpatialData();
	
	/**
	 * Recupera o SpatialData a partir do seu t�tulo.
	 * 
	 * @param title O t�tulo a ser pesquisado.
	 * @return O SpatialData referente ao t�tulo.
	 */
	public SpatialData recoverSpatialDataByTitle(String title);
}
