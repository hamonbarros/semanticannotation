package br.com.sann.service;

import java.util.List;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SemanticAnnotation;
import br.com.sann.domain.SpatialData;

/**
 * Interface de neg�cio relacionada com anota��es sem�nticas.
 * 
 * @author Hamon
 *
 */
public interface SemanticAnnotationService {
	

	/**
	 * Persiste as anota��es sem�nticas passadas como par�metro.
	 * 
	 * @param semanticAnnotations As anota��es sem�nticas a serem persistidos.
	 */
	public void saveSemanticAnnotations(List<SemanticAnnotation> semanticAnnotations);

}
