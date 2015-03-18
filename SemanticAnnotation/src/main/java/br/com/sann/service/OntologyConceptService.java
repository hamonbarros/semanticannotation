package br.com.sann.service;

import java.util.Collection;
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
	
	/**
	 * Persiste um conceito de ontologia passado como par�metro.
	 * 
	 * @param concept O conceito a ser persistido.
	 */
	public void saveOntologyConcept(OntologyConcept concept);
	
	/**
	 * Recupera um conceito ontologico a partir do sua URI.
	 * @param uri URI do conceito.
	 * @return O conceito ontol�gico ou null, caso n�o seja encontrado.
	 */
	public OntologyConcept recoveryOntologyByURI(String uri);

	/**
	 * Recupera os conceitos ontológicos correspondentes as URL's passadas
	 * @param urls As urls do conceitos.
	 * @return Os conceitos ontológicos.
	 */
	public Collection<OntologyConcept> recoveryOntologiesByURIs(List<String> urls);
}
