package br.com.sann.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.sann.criteria.dao.OntologyConceptDAO;
import br.com.sann.criteria.dao.SemanticAnnotationDAO;
import br.com.sann.criteria.dao.impl.OntologyConceptDAOImpl;
import br.com.sann.criteria.dao.impl.SemanticAnnotationDAOImpl;
import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SemanticAnnotation;
import br.com.sann.domain.SpatialData;
import br.com.sann.service.SemanticAnnotationService;

/**
 * Classe de negócio relacionado com anotações semânticas.
 * 
 * @author Hamon
 *
 */
public class SemanticAnnotationServiceImpl implements SemanticAnnotationService{
	
	private SemanticAnnotationDAO dao;
	private OntologyConceptDAO conceptDao;
	
	/**
	 * Construtor padrão.
	 */
	public SemanticAnnotationServiceImpl() {
		
		dao = new SemanticAnnotationDAOImpl();
		conceptDao = new OntologyConceptDAOImpl();
	}
	
	public void saveSemanticAnnotations(Set<SemanticAnnotation> semanticAnnotations) {
		dao.saveSemanticAnnotations(semanticAnnotations);
		
	}

	public List<SemanticAnnotation> recoveryAnnotations(Integer idSpatialData) {
		return dao.recoveryAnnotations(idSpatialData);
	}

	public void removeAnnotations(Integer idSpatialData) {
		dao.removeAnnotations(idSpatialData);	
	}

	public void saveSemanticAnnotations(SpatialData featureType, Set<String> concepts) {
		Set<SemanticAnnotation> sanns = new HashSet<SemanticAnnotation>();
		for (String concept : concepts) {
			OntologyConcept ontologyConcept = conceptDao.recoveryOntologyByURI(concept);
			SemanticAnnotation sann = new SemanticAnnotation(featureType, ontologyConcept);
			sanns.add(sann);
		}
		dao.saveSemanticAnnotations(sanns);
	}

}
