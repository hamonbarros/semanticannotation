package br.com.sann.service.impl;

import java.util.List;

import br.com.sann.criteria.dao.ServiceDAO;
import br.com.sann.criteria.dao.impl.ServiceDAOImpl;
import br.com.sann.domain.Service;
import br.com.sann.service.ServiceService;

/**
 * Classe de neg�cio relacionado com Service.
 * 
 * @author Hamon
 *
 */
public class ServiceServiceImpl implements ServiceService{
	
	private ServiceDAO dao;
	
	/**
	 * Construtor padr�o.
	 */
	public ServiceServiceImpl() {
		
		dao = new ServiceDAOImpl();
	}

	public List<Service> recoverAllServices() {
		
		return dao.recoverAllServices();
	}

}
