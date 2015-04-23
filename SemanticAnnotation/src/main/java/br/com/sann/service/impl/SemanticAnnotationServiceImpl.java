package br.com.sann.service.impl;

import java.util.List;
import java.util.Set;

import br.com.sann.criteria.dao.SemanticAnnotationDAO;
import br.com.sann.criteria.dao.impl.SemanticAnnotationDAOImpl;
import br.com.sann.domain.SemanticAnnotation;
import br.com.sann.service.SemanticAnnotationService;

/**
 * Classe de neg�cio relacionado com anota��es sem�nticas.
 * 
 * @author Hamon
 *
 */
public class SemanticAnnotationServiceImpl implements SemanticAnnotationService{
	
	private SemanticAnnotationDAO dao;
	
	/**
	 * Construtor padr�o.
	 */
	public SemanticAnnotationServiceImpl() {
		
		dao = new SemanticAnnotationDAOImpl();
	}
	
	public void saveSemanticAnnotations(Set<SemanticAnnotation> semanticAnnotations) {
		dao.saveSemanticAnnotations(semanticAnnotations);
		
	}

	public List<SemanticAnnotation> recoveryAnnotations(Integer idSpatialData) {
		return dao.recoveryAnnotations(idSpatialData);
	}

}
