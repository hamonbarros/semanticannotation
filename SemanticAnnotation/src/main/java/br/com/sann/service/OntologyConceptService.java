package br.com.sann.service;

import java.util.List;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SpatialData;

/**
 * Interface de neg�cio relacionada com conceitos de ontologia.
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
	 * Persiste os conceitos de ontologias passados como par�metro.
	 * 
	 * @param concepts Os conceitos a serem persistidos.
	 */
	public void saveOntologyConcepts(List<OntologyConcept> concepts);

	/**
	 * Recupera os conceitos de ontologia atrav�s dos ids.
	 * 
	 * @param idsOntology Os ids dos conceitos a serem recuperados. 
	 */
	public List<OntologyConcept> recoveryOntolgyConceptByIds(String[] idsOntology);
	
	/**
	 * Recupera os conceitos a partir de um tema.
	 * 
	 * @param term O tema a ser consultado. 
	 */
	public List<OntologyConcept> recoveryOntolgyConceptByTerm(String term);
}
