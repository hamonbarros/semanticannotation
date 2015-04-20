package br.com.sann.service;

import java.util.List;
import java.util.Set;

import br.com.sann.domain.SemanticAnnotation;

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
	public void saveSemanticAnnotations(Set<SemanticAnnotation> semanticAnnotations);
	
	/**
	 * Recupera as anota��es de um feature type.
	 * 
	 * @param idSpatialData
	 *            Id do feature type a ser recuperado.
	 * @return As anota��es sem�nticas de um feature type.
	 */
	public List<SemanticAnnotation> recoveryAnnotations(Integer idSpatialData);

}
