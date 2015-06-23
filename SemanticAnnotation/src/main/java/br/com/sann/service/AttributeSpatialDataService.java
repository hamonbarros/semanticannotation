package br.com.sann.service;

import java.util.List;

import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.domain.SpatialData;
import br.com.sann.performance.test.ExpectedResult;

/**
 * Interface de negócio relacionada com {@link AttributeSpatialData}.
 * 
 * @author Hamon
 *
 */
public interface AttributeSpatialDataService {
	
	/**
	 * Recupera todos os atributos dos serviços cadastrados.
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
	 * Insere a lista de atributos de um serviço na base de dados.
	 * @param list A lista de atributos.
	 */
	void insertAttributesServiceList(List<SpatialData> list);
	
	/**
	 * Atualiza o atributo do serviço.
	 * 
	 * @param attributeService O atributo do serviço a ser atualizado.
	 */
	void updateAttributeService(AttributeSpatialData attributeService);

	/**
	 * Recupera os atributos a partir do nome.
	 * 
	 * @param nameAttr
	 *            O nome dos atributos a serem recuperados.
	 * @return Os AttributeSpatialData solicitados.
	 */
	List<AttributeSpatialData> recoverySpatialDataByTitle(String nameAttr);
}
