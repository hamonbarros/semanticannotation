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

	/**
	 * Recupera os conceitos de ontologia através dos ids.
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
	
	/**
	 * Persiste um conceito de ontologia passado como parâmetro.
	 * 
	 * @param concept O conceito a ser persistido.
	 */
	public void saveOntologyConcept(OntologyConcept concept);
	
	/**
	 * Recupera um conceito ontologico a partir do sua URI.
	 * @param uri URI do conceito.
	 * @return O conceito ontológico ou null, caso não seja encontrado.
	 */
	public OntologyConcept recoveryOntologyByURI(String uri);
}
