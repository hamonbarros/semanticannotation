package br.com.sann.service;

import java.util.List;
import java.util.Set;

import br.com.sann.domain.SemanticAnnotation;

/**
 * Interface de negócio relacionada com anotações semânticas.
 * 
 * @author Hamon
 *
 */
public interface SemanticAnnotationService {
	

	/**
	 * Persiste as anotações semânticas passadas como parâmetro.
	 * 
	 * @param semanticAnnotations As anotações semânticas a serem persistidos.
	 */
	public void saveSemanticAnnotations(Set<SemanticAnnotation> semanticAnnotations);
	
	/**
	 * Recupera as anotações de um feature type.
	 * 
	 * @param idSpatialData
	 *            Id do feature type a ser recuperado.
	 * @return As anotações semânticas de um feature type.
	 */
	public List<SemanticAnnotation> recoveryAnnotations(Integer idSpatialData);

}
