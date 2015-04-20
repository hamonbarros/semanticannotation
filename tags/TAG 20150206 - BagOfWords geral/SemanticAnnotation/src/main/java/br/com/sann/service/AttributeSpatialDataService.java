package br.com.sann.service;

import java.util.List;

import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.domain.SpatialData;

/**
 * Interface de neg�cio relacionada com {@link AttributeSpatialData}.
 * 
 * @author Hamon
 *
 */
public interface AttributeSpatialDataService {
	
	/**
	 * Recupera todos os atributos dos servi�os cadastrados.
	 * 
	 * @return Os atributos cadastrados.
	 */
	public List<AttributeSpatialData> recoverAllAttributes();
	
	/**
	 * Recupera os atributos de um determinado feature type.
	 * 
	 * @param idSpatialData O id do feature type.
	 * @return Os atributos do feature type.
	 */
	public List<AttributeSpatialData> recoverAttributesBySpatialData(Integer idSpatialData);
	
	/**
	 * Insere a lista de atributos de um servi�o na base de dados.
	 * @param list A lista de atributos.
	 */
	void insertAttributesServiceList(List<SpatialData> list);
	
	/**
	 * Atualiza o atributo do servi�o.
	 * 
	 * @param attributeService O atributo do servi�o a ser atualizado.
	 */
	void updateAttributeService(AttributeSpatialData attributeService);
}
