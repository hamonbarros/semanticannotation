package br.com.sann.service;

import java.util.List;

import br.com.sann.domain.Service;

/**
 * Interface de negócio relacionada com Service.
 * 
 * @author Hamon
 *
 */
public interface ServiceService {
	
	/**
	 * Recupera todos os Services cadastrados.
	 * 
	 * @return Os Services cadastrados.
	 */
	public List<Service> recoverAllServices();
	
}
