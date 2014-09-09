package br.com.sann.service.feature;

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

}
