package br.com.sann.criteria.dao.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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

	@Override
	public List<SemanticAnnotation> recoveryAnnotations(Integer idSpatialData) {
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM SemanticAnnotation n WHERE n.featureType.id = :idSpatialData";
		Query q = em.createQuery(jpql);
		q.setParameter("idSpatialData", idSpatialData);		
		List<SemanticAnnotation> sann = q.getResultList();
		em.close();
		JPAUtil.closeEntityManager();
		return sann;
	}

}
