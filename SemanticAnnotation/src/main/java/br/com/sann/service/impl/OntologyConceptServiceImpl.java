package br.com.sann.service.impl;

import java.util.Collection;
import java.util.List;

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

	@Override
	public List<OntologyConcept> recoverAllOntologyConcept() {
		
		return dao.recoverAllOntologyConcept();
	}

	@Override
	public void saveOntologyConcepts(List<OntologyConcept> concepts) {
		
		dao.saveOntologyConcepts(concepts);
	}

	@Override
	public List<OntologyConcept> recoveryOntolgyConceptByIds(String[] idsOntology) {
		
		return dao.recoveryOntolgyConceptByIds(idsOntology);
	}

	@Override
	public List<OntologyConcept> recoveryOntolgyConceptByTerm(String term) {
		
		return dao.recoveryOntolgyConceptByTerm(term);
	}

	@Override
	public void saveOntologyConcept(OntologyConcept concept) {
		
		dao.saveOntologyConcept(concept);		
	}

	@Override
	public OntologyConcept recoveryOntologyByURI(String uri) {
		
		return dao.recoveryOntologyByURI(uri);
	}

	@Override
	public Collection<OntologyConcept> recoveryOntologiesByURIs(
			List<String> urls) {
		return dao.recoveryOntologiesByURIs(urls);
	}

}
