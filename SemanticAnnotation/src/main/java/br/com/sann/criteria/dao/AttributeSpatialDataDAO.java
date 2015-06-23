package br.com.sann.criteria.dao;

import java.util.Collection;
import java.util.List;

import br.com.sann.domain.AttributeSpatialData;

/**
 * Interface de persitência da entidade {@link AttributeSpatialData}.
 * 
 * @author Hamon
 *
 */
public interface AttributeSpatialDataDAO {
	
	/**
	 * Recupera todos os AttributeService cadastrados.
	 * 
	 * @return Os AttributeService cadastrados.
	 */
	public List<AttributeSpatialData> recoverAllAttributeService();
	
	/**
	 * Recupera os atributos de um determinado feature type.
	 * 
	 * @param idSpatialData O id do feature type.
	 * @return Os atributos do feature type.
	 */
	public List<AttributeSpatialData> recoverAttributesBySpatialData(Integer idSpatialData);
	
	/**
	 * Insere a lista de atributos de um serviço na base de dados.
	 * 
	 * @param attributes A lista de atributos.
	 */
	void insertAttributesServiceList(Collection<AttributeSpatialData> attributes);
	
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
