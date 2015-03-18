package br.com.sann.service.impl;

import java.util.Set;

import br.com.sann.criteria.dao.SemanticAnnotationDAO;
import br.com.sann.criteria.dao.impl.SemanticAnnotationDAOImpl;
import br.com.sann.domain.SemanticAnnotation;

/**
 * Classe de neg�cio relacionado com anota��es sem�nticas.
 * 
 * @author Hamon
 *
 */
public class SemanticAnnotationServiceImpl implements SemanticAnnotationDAO{
	
	private SemanticAnnotationDAO dao;
	
	/**
	 * Construtor padrão.
	 */
	public SemanticAnnotationServiceImpl() {
		
		dao = new SemanticAnnotationDAOImpl();
	}

	@Override
	public void saveSemanticAnnotations(Set<SemanticAnnotation> semanticAnnotations) {
		
		dao.saveSemanticAnnotations(semanticAnnotations);
	}

}
