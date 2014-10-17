package br.com.sann.criteria.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.sann.criteria.dao.OntologyConceptDAO;
import br.com.sann.criteria.dao.SpatialDataDAO;
import br.com.sann.criteria.util.JPAUtil;
import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SpatialData;

/**
 * Classe de persitência da entidade OntologyConcept.
 * 
 * @author Hamon
 *
 */
public class OntologyConceptDAOImpl implements OntologyConceptDAO{

	@Override
	public List<OntologyConcept> recoverAllOntologyConcept() {
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM OntologyConcept n ORDER BY n.conceptName";
		Query q = em.createQuery(jpql);
		List<OntologyConcept> concepts = q.getResultList();
		return concepts;
	}

	@Override
	public void saveOntologyConcepts(List<OntologyConcept> concepts) {
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		for (OntologyConcept ontologyConcept : concepts) {			
			em.merge(ontologyConcept);		
			em.flush();
		}
		em.getTransaction().commit();
		JPAUtil.closeEntityManager();
	}

	
}
