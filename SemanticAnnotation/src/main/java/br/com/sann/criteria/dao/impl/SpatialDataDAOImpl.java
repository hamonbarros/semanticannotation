package br.com.sann.criteria.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.sann.criteria.dao.SpatialDataDAO;
import br.com.sann.criteria.util.JPAUtil;
import br.com.sann.domain.SpatialData;

/**
 * Classe de persitência da entidade SpatialData.
 * 
 * @author Hamon
 *
 */
public class SpatialDataDAOImpl implements SpatialDataDAO{

	public List<SpatialData> recoverAllSpatialData() {
		
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM SpatialData n ORDER BY n.title";
		Query q = em.createQuery(jpql);
		List<SpatialData> spatialDatas = q.getResultList();
		return spatialDatas;
	}

	@Override
	public SpatialData recoverSpatialDataByTitle(String title) {
		
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM SpatialData n WHERE n.title LIKE :title";
		Query q = em.createQuery(jpql);
		q.setParameter("title", title);
		
		List<SpatialData> spatialDatas = q.getResultList();
		em.close();
		
		if (spatialDatas!= null && !spatialDatas.isEmpty()) {			
			return spatialDatas.get(0);
		}
		return null;
	}

	
}
