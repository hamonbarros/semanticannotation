package br.com.sann.criteria.dao;

import java.util.Set;

import br.com.sann.domain.SemanticAnnotation;

/**
 * Interface de persitência da entidade SemanticAnnotation.
 * 
 * @author Hamon
 *
 */
public interface SemanticAnnotationDAO {
	
	/**
	 * Persiste as anotações semânticas passadas como parâmetro.
	 * 
	 * @param semanticAnnotations As anotações semânticas a serem persistidos.
	 */
	public void saveSemanticAnnotations(Set<SemanticAnnotation> semanticAnnotations);
	
}
