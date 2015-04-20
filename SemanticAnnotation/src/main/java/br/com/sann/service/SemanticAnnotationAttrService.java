package br.com.sann.service;

import java.util.List;
import java.util.Set;

import br.com.sann.domain.SemanticAnnotation;
import br.com.sann.domain.SemanticAnnotationAttribute;

/**
 * Interface de neg�cio relacionada com anota��es sem�nticas dos atributos .
 * 
 * @author Hamon
 *
 */
public interface SemanticAnnotationAttrService {
	

	/**
	 * Persiste as anota��es sem�nticas dos atributos passadas como par�metro.
	 * 
	 * @param semanticAnnotations As anota��es sem�nticas a serem persistidos.
	 */
	public void saveSemanticAnnotationsAttrs(Set<SemanticAnnotationAttribute> semanticAnnotations);
	
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

}
