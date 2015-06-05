package br.com.sann.criteria.dao;

import java.util.List;
import java.util.Set;

import br.com.sann.domain.SemanticAnnotationAttribute;

/**
 * Interface de persit�ncia da entidade {@link SemanticAnnotationAttribute}.
 * 
 * @author Hamon
 *
 */
public interface SemanticAnnotationAttributeDAO {
	
	/**
	 * Persiste as anota��es sem�nticas dos atributos passadas como par�metro.
	 * 
	 * @param semanticAnnotations
	 *            As anota��es sem�nticas a serem persistidos.
	 */
	void saveSemanticAnnotationsAttrs(Set<SemanticAnnotationAttribute> semanticAnnotations);
	
	/**
	 * Executa uma consulta.
	 * 
	 * @param query
	 *            A consulta a ser executada.
	 * @return A lista de anota��es.
	 */
	List<Object[]> executeQuery(String query);
	
	/**
	 * Recupera as anota��es de um atributo de um feature type.
	 * 
	 * @param idAttribute
	 *            Id do atributo a ser recuperado.
	 * @return As anota��es sem�nticas de um atributo.
	 */
	public List<SemanticAnnotationAttribute> recoveryAnnotations(Integer idAttribute);

	/**
	 * Remove as anota��es sem�nticas de um determinado atributo.
	 * 
	 * @param idAttribute
	 *            Id do atributo cujas anota��es ser�o removidas.
	 */
	public void removeAnnotations(Integer idAttribute);
	
}
