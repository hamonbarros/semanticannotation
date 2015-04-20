package br.com.sann.service.impl;

import java.util.List;
import java.util.Set;

import br.com.sann.criteria.dao.SemanticAnnotationAttributeDAO;
import br.com.sann.criteria.dao.impl.SemanticAnnotationAttributeDAOImpl;
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

	/**
	 * Construtor padrão.
	 */
	public SemanticAnnotationAttrServiceImpl() {
		
		dao = new SemanticAnnotationAttributeDAOImpl();
	}
	
	@Override
	public void saveSemanticAnnotationsAttrs(Set<SemanticAnnotationAttribute> semanticAnnotations) {
		dao.saveSemanticAnnotationsAttrs(semanticAnnotations);		
	}

	@Override
	public List<Object[]> executeQuery(String query) {
		return dao.executeQuery(query);
	}

	@Override
	public List<SemanticAnnotationAttribute> recoveryAnnotations(
			Integer idAttribute) {
		return dao.recoveryAnnotations(idAttribute);
	}

}
