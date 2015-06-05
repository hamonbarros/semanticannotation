package br.com.sann.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.sann.criteria.dao.OntologyConceptDAO;
import br.com.sann.criteria.dao.SemanticAnnotationAttributeDAO;
import br.com.sann.criteria.dao.impl.OntologyConceptDAOImpl;
import br.com.sann.criteria.dao.impl.SemanticAnnotationAttributeDAOImpl;
import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SemanticAnnotationAttribute;
import br.com.sann.service.SemanticAnnotationAttrService;

/**
 * Classe de negócio relacionado com anotações semânticas dos atributos.
 * 
 * @author Hamon
 *
 */
public class SemanticAnnotationAttrServiceImpl implements SemanticAnnotationAttrService{
	
	private SemanticAnnotationAttributeDAO dao;
	private OntologyConceptDAO conceptDao;
	
	/**
	 * Construtor padrão.
	 */
	public SemanticAnnotationAttrServiceImpl() {
		
		dao = new SemanticAnnotationAttributeDAOImpl();
		conceptDao = new OntologyConceptDAOImpl();
	}
	
	public void saveSemanticAnnotationsAttrs(Set<SemanticAnnotationAttribute> semanticAnnotations) {
		dao.saveSemanticAnnotationsAttrs(semanticAnnotations);		
	}

	public List<Object[]> executeQuery(String query) {
		return dao.executeQuery(query);
	}

	public List<SemanticAnnotationAttribute> recoveryAnnotations(
			Integer idAttribute) {
		return dao.recoveryAnnotations(idAttribute);
	}

	public void removeAnnotations(Integer idAttribute) {
		dao.removeAnnotations(idAttribute);		
	}
	
	public void saveSemanticAnnotations(AttributeSpatialData attr, Set<String> concepts) {
		Set<SemanticAnnotationAttribute> sannAttrs = new HashSet<SemanticAnnotationAttribute>();
		for (String concept : concepts) {
			OntologyConcept ontologyConcept = conceptDao.recoveryOntologyByURI(concept);
			SemanticAnnotationAttribute sann = new SemanticAnnotationAttribute();
			sann.setAttributeService(attr);
			sann.setOntologyConcept(ontologyConcept);
			sannAttrs.add(sann);
		}
		dao.saveSemanticAnnotationsAttrs(sannAttrs);
	}

}
