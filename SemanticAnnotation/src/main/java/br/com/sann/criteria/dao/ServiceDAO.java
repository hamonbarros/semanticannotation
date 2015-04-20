package br.com.sann.criteria.dao;

import java.util.List;

import br.com.sann.domain.Service;

/**
 * Interface de persit�ncia da entidade {@link Service}.
 * 
 * @author Hamon
 *
 */
public interface ServiceDAO {
	
	/**
	 * Recupera todos os Services cadastrados.
	 * 
	 * @return Os Services cadastrados.
	 */
	public List<Service> recoverAllServices();
	
}
