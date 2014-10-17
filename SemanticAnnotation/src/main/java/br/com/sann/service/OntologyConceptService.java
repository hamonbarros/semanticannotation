package br.com.sann.service;

import java.util.List;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SpatialData;

/**
 * Interface de negócio relacionada com conceitos de ontologia.
 * 
 * @author Hamon
 *
 */
public interface OntologyConceptService {
	
	/**
	 * Recupera todos os conceitos de ontologias cadastrados.
	 * 
	 * @return Os conceitos de ontologias cadastrados.
	 */
	public List<OntologyConcept> recoverAllOntologyConcept();
	
	/**
	 * Persiste os conceitos de ontologias passados como parâmetro.
	 * 
	 * @param concepts Os conceitos a serem persistidos.
	 */
	public void saveOntologyConcepts(List<OntologyConcept> concepts);

}
