package br.com.sann.criteria.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.sann.criteria.dao.ServiceDAO;
import br.com.sann.criteria.util.JPAUtil;
import br.com.sann.domain.Service;

/**
 * Classe de persitência da entidade {@link Service}.
 * 
 * @author Hamon
 *
 */
public class ServiceDAOImpl implements ServiceDAO{

	@Override
	public List<Service> recoverAllServices() {

		EntityManager em = JPAUtil.getEntityManager();
		String jpql = "SELECT n FROM Service n WHERE n.processed = FALSE ORDER BY n.name";
		Query q = em.createQuery(jpql);
		List<Service> services = q.getResultList();
		return services;
		
	}
	
}
