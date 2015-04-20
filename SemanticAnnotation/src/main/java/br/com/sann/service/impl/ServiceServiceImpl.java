package br.com.sann.service.impl;

import java.util.List;

import br.com.sann.criteria.dao.ServiceDAO;
import br.com.sann.criteria.dao.SpatialDataDAO;
import br.com.sann.criteria.dao.impl.ServiceDAOImpl;
import br.com.sann.criteria.dao.impl.SpatialDataDAOImpl;
import br.com.sann.domain.Service;
import br.com.sann.domain.SpatialData;
import br.com.sann.service.FeatureService;
import br.com.sann.service.ServiceService;

/**
 * Classe de negócio relacionado com Service.
 * 
 * @author Hamon
 *
 */
public class ServiceServiceImpl implements ServiceService{
	
	private ServiceDAO dao;
	
	/**
	 * Construtor padrão.
	 */
	public ServiceServiceImpl() {
		
		dao = new ServiceDAOImpl();
	}

	@Override
	public List<Service> recoverAllServices() {
		
		return dao.recoverAllServices();
	}

}
