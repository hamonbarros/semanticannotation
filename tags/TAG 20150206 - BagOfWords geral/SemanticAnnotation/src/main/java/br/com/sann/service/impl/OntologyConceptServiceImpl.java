package br.com.sann.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.sann.criteria.dao.OntologyConceptDAO;
import br.com.sann.criteria.dao.impl.OntologyConceptDAOImpl;
import br.com.sann.domain.OntologyConcept;
import br.com.sann.service.OntologyConceptService;

/**
 * Classe de negócio relacionado com conceitos de ontologia.
 * 
 * @author Hamon
 *
 */
public class OntologyConceptServiceImpl implements OntologyConceptService{
	
	private OntologyConceptDAO dao;
	
	/**
	 * Construtor padrão.
	 */
	public OntologyConceptServiceImpl() {
		
		dao = new OntologyConceptDAOImpl();
	}

	public List<OntologyConcept> recoverAllOntologyConcept() {
		
		return dao.recoverAllOntologyConcept();
	}

	public void saveOntologyConcepts(List<OntologyConcept> concepts) {
		
		dao.saveOntologyConcepts(concepts);
	}

	public List<OntologyConcept> recoveryOntolgyConceptByIds(String[] idsOntology) {
		
		return dao.recoveryOntolgyConceptByIds(idsOntology);
	}

	public List<OntologyConcept> recoveryOntolgyConceptByTerm(String term) {
		
		return dao.recoveryOntolgyConceptByTerm(term);
	}

	public void saveOntologyConcept(OntologyConcept concept) {
		
		dao.saveOntologyConcept(concept);		
	}

	public OntologyConcept recoveryOntologyByURI(String uri) {
		
		return dao.recoveryOntologyByURI(uri);
	}

	public Collection<OntologyConcept> recoveryOntologiesByURIs(
			Set<String> urls) {
		return dao.recoveryOntologiesByURIs(urls);
	}

}
