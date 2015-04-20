package br.com.sann.criteria.dao.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.sann.criteria.dao.SemanticAnnotationAttributeDAO;
import br.com.sann.criteria.dao.SemanticAnnotationDAO;
import br.com.sann.criteria.util.JPAUtil;
import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.domain.SemanticAnnotation;
import br.com.sann.domain.SemanticAnnotationAttribute;

/**
 * Classe de persitência da entidade {@link SemanticAnnotationAttribute}.
 * 
 * @author Hamon
 * 
 */
public class SemanticAnnotationAttributeDAOImpl implements SemanticAnnotationAttributeDAO {

	@Override
	public void saveSemanticAnnotationsAttrs(Set<SemanticAnnotationAttribute> semanticAnnotations) {
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		for (SemanticAnnotationAttribute semanticAnnotation : semanticAnnotations) {
			em.merge(semanticAnnotation);
			em.flush();
		}
		em.getTransaction().commit();
		JPAUtil.closeEntityManager();
	}
	
	@Override
	public List<Object[]> executeQuery(String query) {
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = query;
		Query q = em.createQuery(jpql);
		return q.getResultList();
	}

	@Override
	public List<SemanticAnnotationAttribute> recoveryAnnotations(Integer idAttribute) {
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM SemanticAnnotationAttribute n WHERE n.attributeService.id = :idAttribute";
		Query q = em.createQuery(jpql);
		q.setParameter("idAttribute", idAttribute);		
		List<SemanticAnnotationAttribute> sann = q.getResultList();
		em.close();
		JPAUtil.closeEntityManager();
		return sann;
	}

}
