package br.com.sann.criteria.dao.impl;

import java.util.Set;

import javax.persistence.EntityManager;

import br.com.sann.criteria.dao.SemanticAnnotationDAO;
import br.com.sann.criteria.util.JPAUtil;
import br.com.sann.domain.SemanticAnnotation;

/**
 * Classe de persitência da entidade SemanticAnnotation.
 * 
 * @author Hamon
 * 
 */
public class SemanticAnnotationDAOImpl implements SemanticAnnotationDAO {

	@Override
	public void saveSemanticAnnotations(Set<SemanticAnnotation> semanticAnnotations) {
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		for (SemanticAnnotation semanticAnnotation : semanticAnnotations) {
			em.merge(semanticAnnotation);
			em.flush();
		}
		em.getTransaction().commit();
		JPAUtil.closeEntityManager();
	}

}
