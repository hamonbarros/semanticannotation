package br.com.sann.criteria.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.sann.criteria.dao.SpatialDataDAO;
import br.com.sann.criteria.util.JPAUtil;
import br.com.sann.domain.SpatialData;
import br.com.sann.service.processing.text.BagOfWords;

/**
 * Classe de persitência da entidade SpatialData.
 * 
 * @author Hamon
 *
 */
public class SpatialDataDAOImpl implements SpatialDataDAO{

	public List<SpatialData> recoverAllSpatialData() {
		
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM SpatialData n WHERE n.annotated = FALSE ORDER BY n.title";
		Query q = em.createQuery(jpql);
		List<SpatialData> spatialDatas = q.getResultList();
		return spatialDatas;
	}

	@Override
	public String recoverBagOfWordsByTitle(String title) {
		
		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM SpatialData n WHERE n.title LIKE :title";
		Query q = em.createQuery(jpql);
		q.setParameter("title", title);
		
		List<SpatialData> spatialDatas = q.getResultList();
		em.close();
		
		if (spatialDatas!= null && !spatialDatas.isEmpty()) {	
			
			StringBuffer storeBagsOfWords = new StringBuffer();	
			BagOfWords bw = null;
			for (SpatialData spatialData : spatialDatas) {
				bw = new BagOfWords(spatialData);
				storeBagsOfWords.append(bw.extractTextProperties());
				storeBagsOfWords.append(" ");
			}			
			return bw.extractWordList(storeBagsOfWords.toString());
		}
		return "";
	}

	@Override
	public void updateSpatialDataList(List<SpatialData> list) {
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		for (SpatialData spatialData : list) {
			em.merge(spatialData);
			em.flush();
		}
		em.getTransaction().commit();
		em.close();
	}

	@Override
	public List<SpatialData> recoverySpatialDataByIDs(String ids) {
		
		if (ids != null && !ids.isEmpty()) {
			String[] idSplit = ids.split(",");
			List<Integer> idsInteger = new ArrayList<Integer>();
			for (String id : idSplit) {
				idsInteger.add(Integer.valueOf(id));
			}
			
			EntityManager em = JPAUtil.getEntityManager();
			String jpql = "SELECT s FROM SpatialData s WHERE s.id in (:ids)";
			Query q = em.createQuery(jpql);
			q.setParameter("ids", idsInteger);
			List<SpatialData> spatialDatas = q.getResultList();
			return spatialDatas;				
		}
		return null;
	}
	
}
