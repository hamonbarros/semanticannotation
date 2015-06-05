package br.com.sann.service;

import java.util.List;
import java.util.Set;

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
	public void saveSemanticAnnotations(Set<SemanticAnnotation> semanticAnnotations);
	
	/**
	 * Recupera as anota��es de um feature type.
	 * 
	 * @param idSpatialData
	 *            Id do feature type a ser recuperado.
	 * @return As anota��es sem�nticas de um feature type.
	 */
	public List<SemanticAnnotation> recoveryAnnotations(Integer idSpatialData);
	
	/**
	 * Remove as anota��es de um feature type.
	 * 
	 * @param idSpatialData
	 *            Id do feature type cujas anota��es ser�o removidas.
	 */
	public void removeAnnotations(Integer idSpatialData);

	/**
	 * Persiste as anota��es sem�nticas entre o featureType e o conceitos.
	 * @param featureType O feature type.
	 * @param concepts Os conceitos associados.
	 */
	public void saveSemanticAnnotations(SpatialData featureType, Set<String> concepts);
	
}
