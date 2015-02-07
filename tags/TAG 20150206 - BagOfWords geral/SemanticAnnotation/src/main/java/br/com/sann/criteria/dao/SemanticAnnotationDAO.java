package br.com.sann.criteria.dao;

import java.util.Set;

import br.com.sann.domain.SemanticAnnotation;

/**
 * Interface de persit�ncia da entidade SemanticAnnotation.
 * 
 * @author Hamon
 *
 */
public interface SemanticAnnotationDAO {
	
	/**
	 * Persiste as anota��es sem�nticas passadas como par�metro.
	 * 
	 * @param semanticAnnotations As anota��es sem�nticas a serem persistidos.
	 */
	public void saveSemanticAnnotations(Set<SemanticAnnotation> semanticAnnotations);
	
}
