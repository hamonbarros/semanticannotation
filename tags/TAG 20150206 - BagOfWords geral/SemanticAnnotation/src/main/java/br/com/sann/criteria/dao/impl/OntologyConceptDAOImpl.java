package br.com.sann.criteria.dao.impl;

import java.util.ArrayList;
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
public class OntologyConceptDAOImpl implements OntologyConceptDAO {

	@Override
	public List<OntologyConcept> recoverAllOntologyConcept() {
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM OntologyConcept n ORDER BY n.conceptName";
		Query q = em.createQuery(jpql);
		List<OntologyConcept> concepts = q.getResultList();
		JPAUtil.closeEntityManager();
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
		em.close();
		JPAUtil.closeEntityManager();
	}

	@Override
	public List<OntologyConcept> recoveryOntolgyConceptByIds(String[] idsOntology) {

		EntityManager em = JPAUtil.getEntityManager();
		List<OntologyConcept> concepts = new ArrayList<OntologyConcept>();
		for (String id : idsOntology) {
			OntologyConcept concept = em.find(OntologyConcept.class, Integer.parseInt(id.trim()));
			if (concept != null) {
				concepts.add(concept);
			}
		}
		em.close();
		JPAUtil.closeEntityManager();
		return concepts;
	}

	@Override
	public List<OntologyConcept> recoveryOntolgyConceptByTerm(String term) {
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM OntologyConcept n " +
					  "WHERE UPPER(n.conceptName) = UPPER('"+term+"') OR " +
					  		"UPPER(n.normalizedName) = UPPER('"+term+"')";
		Query q = em.createQuery(jpql);
		List<OntologyConcept> concepts = q.getResultList();
		em.close();
		JPAUtil.closeEntityManager();
		return concepts;
	}

	@Override
	public void saveOntologyConcept(OntologyConcept concept) {
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		em.merge(concept);
		em.flush();
		em.getTransaction().commit();
		em.close();
		JPAUtil.closeEntityManager();	
	}

	@Override
	public OntologyConcept recoveryOntologyByURI(String uri) {
		
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM OntologyConcept n " +
					  "WHERE concept like :uri";
		Query q = em.createQuery(jpql);
		q.setParameter("uri", uri);
		List<OntologyConcept> concepts = q.getResultList();
		em.close();
		JPAUtil.closeEntityManager();
		if (concepts != null && !concepts.isEmpty()) {
			return concepts.get(0);
		}
		return null;
	}

}
