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

	public List<SpatialData> recoverAllSpatialData() {
		
		return dao.recoverAllSpatialData();
	}
	
	public List<SpatialData> recoverAllSpatialDataNotAnnotated() {
		
		return dao.recoverAllSpatialDataNotAnnotated();
	}

	public String recoverBagOfWordsByTitle(String title) {
		
		return dao.recoverBagOfWordsByTitle(title);
	}
	
	public void updateSpatialDataList(List<SpatialData> list) {
		dao.updateSpatialDataList(list);
		
	}

	public List<SpatialData> recoverySpatialDataByIDs(String ids) {
		
		return dao.recoverySpatialDataByIDs(ids);
	}

	public void updateSpatialData(SpatialData spatialData) {		
		dao.updateSpatialData(spatialData);
		
	}

	public List<SpatialData> recoverySpatialDataByTextInfo(String name,
			String title, String textDescription, String keywords) {
		return dao.recoverySpatialDataByTextInfo(name, title, textDescription, keywords);
	}
	
	public List<SpatialData> recoverySpatialDataByTitle(String title) {
		return dao.recoverySpatialDataByTitle(title);
	}

}
