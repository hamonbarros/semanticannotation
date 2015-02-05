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
	 * Recupera a bag of words constru�da a partir dos SpatialData com um 
	 * determinado t�tulo.
	 * 
	 * @param title O t�tulo a ser pesquisado.
	 * @return A bag of words constu�da com as informa��es textuais do SpatialData.
	 */
	public String recoverBagOfWordsByTitle(String title);

	/**
	 * Atualiza a lista de spatialdata na base de dados.
	 * @param list A lista a ser atualizada.
	 */
	void updateSpatialDataList(List<SpatialData> list);
}
