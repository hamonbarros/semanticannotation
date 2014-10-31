package br.com.sann.criteria.dao;

import java.util.List;

import br.com.sann.domain.SpatialData;

/**
 * Interface de persitência da entidade SpatialData.
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
	 * Recupera o SpatialData a partir do seu título.
	 * 
	 * @param title O título a ser pesquisado.
	 * @return O SpatialData referente ao título.
	 */
	public SpatialData recoverSpatialDataByTitle(String title);
}
