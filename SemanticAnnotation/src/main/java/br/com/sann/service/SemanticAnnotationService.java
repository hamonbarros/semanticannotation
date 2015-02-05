package br.com.sann.service;

import java.util.List;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SemanticAnnotation;
import br.com.sann.domain.SpatialData;

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
	public void saveSemanticAnnotations(List<SemanticAnnotation> semanticAnnotations);

}
