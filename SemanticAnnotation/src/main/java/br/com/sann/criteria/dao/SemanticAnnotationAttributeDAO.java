package br.com.sann.criteria.dao;

import java.util.List;
import java.util.Set;

import br.com.sann.domain.SemanticAnnotationAttribute;

/**
 * Interface de persitência da entidade {@link SemanticAnnotationAttribute}.
 * 
 * @author Hamon
 *
 */
public interface SemanticAnnotationAttributeDAO {
	
	/**
	 * Persiste as anotações semânticas dos atributos passadas como parâmetro.
	 * 
	 * @param semanticAnnotations
	 *            As anotações semânticas a serem persistidos.
	 */
	void saveSemanticAnnotationsAttrs(Set<SemanticAnnotationAttribute> semanticAnnotations);
	
	/**
	 * Executa uma consulta.
	 * 
	 * @param query
	 *            A consulta a ser executada.
	 * @return A lista de anotações.
	 */
	List<Object[]> executeQuery(String query);
	
	/**
	 * Recupera as anotações de um atributo de um feature type.
	 * 
	 * @param idAttribute
	 *            Id do atributo a ser recuperado.
	 * @return As anotações semânticas de um atributo.
	 */
	public List<SemanticAnnotationAttribute> recoveryAnnotations(Integer idAttribute);

	/**
	 * Remove as anotações semânticas de um determinado atributo.
	 * 
	 * @param idAttribute
	 *            Id do atributo cujas anotações serão removidas.
	 */
	public void removeAnnotations(Integer idAttribute);
	
}
