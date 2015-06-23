package br.com.sann.criteria.dao.impl;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.sann.criteria.dao.AttributeSpatialDataDAO;
import br.com.sann.criteria.util.JPAUtil;
import br.com.sann.domain.AttributeSpatialData;

/**
 * Classe de persitência da entidade {@link AttributeSpatialData}.
 * 
 * @author Hamon
 *
 */
public class AttributeSpatialDataDAOImpl implements AttributeSpatialDataDAO{

	public List<AttributeSpatialData> recoverAllAttributeService() {
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM AttributeSpatialData n WHERE n.annotated = FALSE ORDER BY n.name";
		Query q = em.createQuery(jpql);
		List<AttributeSpatialData> attributes = q.getResultList();
		return attributes;
	}
	
	public List<AttributeSpatialData> recoverAttributesBySpatialData(
			Integer idSpatialData) {
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM AttributeSpatialData n WHERE n.spatialData.id = " + idSpatialData;
		Query q = em.createQuery(jpql);
		List<AttributeSpatialData> attributes = q.getResultList();
		em.close();
		return attributes;
	}

	public void insertAttributesServiceList(Collection<AttributeSpatialData> attributes) {
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		for (AttributeSpatialData attribute : attributes) {
			em.merge(attribute);
			em.flush();
		}
		em.getTransaction().commit();
		em.close();
	}
	
	public void updateAttributeService(AttributeSpatialData attributeService) {
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		em.merge(attributeService);
		em.flush();
		em.getTransaction().commit();
		em.close();		
	}
	
	public List<AttributeSpatialData> recoverySpatialDataByTitle(String nameAttr) {
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM AttributeSpatialData n " +
				"      WHERE UPPER(n.name) LIKE :name ";
		Query q = em.createQuery(jpql);
		q.setParameter("name", nameAttr);

		List<AttributeSpatialData> attrs = q.getResultList();
		em.close();
		return attrs;
	}
	
}
