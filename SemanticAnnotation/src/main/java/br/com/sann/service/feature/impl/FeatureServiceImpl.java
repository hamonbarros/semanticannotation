package br.com.sann.service.feature.impl;

import java.util.List;

import br.com.sann.criteria.dao.SpatialDataDAO;
import br.com.sann.criteria.dao.impl.SpatialDataDAOImpl;
import br.com.sann.domain.SpatialData;
import br.com.sann.service.feature.FeatureService;

/**
 * Classe de negócio relacionado com Feature Types.
 * 
 * @author Hamon
 *
 */
public class FeatureServiceImpl implements FeatureService{
	
	private SpatialDataDAO dao;
	
	/**
	 * Construtor padrão.
	 */
	public FeatureServiceImpl() {
		
		dao = new SpatialDataDAOImpl();
	}

	@Override
	public List<SpatialData> recoverAllSpatialData() {
		
		return dao.recoverAllSpatialData();
	}

}
