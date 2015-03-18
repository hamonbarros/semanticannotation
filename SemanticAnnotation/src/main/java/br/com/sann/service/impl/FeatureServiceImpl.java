package br.com.sann.service.impl;

import java.util.List;

import br.com.sann.criteria.dao.SpatialDataDAO;
import br.com.sann.criteria.dao.impl.SpatialDataDAOImpl;
import br.com.sann.domain.SpatialData;
import br.com.sann.service.FeatureService;

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

	@Override
	public String recoverBagOfWordsByTitle(String title) {
		
		return dao.recoverBagOfWordsByTitle(title);
	}

	@Override
	public void updateSpatialDataList(List<SpatialData> list) {
		for (SpatialData spatialData : list) {
			spatialData.setAnnotated(Boolean.TRUE);
		}
		dao.updateSpatialDataList(list);
		
	}

	@Override
	public List<SpatialData> recoverySpatialDataByIDs(String ids) {
		
		return dao.recoverySpatialDataByIDs(ids);
	}

	@Override
	public void updateSpatialData(SpatialData spatialData) {
		spatialData.setAnnotated(Boolean.TRUE);
		dao.updateSpatialData(spatialData);
		
	}

}
