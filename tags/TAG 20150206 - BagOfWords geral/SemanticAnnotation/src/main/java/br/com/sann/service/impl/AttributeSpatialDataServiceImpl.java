package br.com.sann.service.impl;

import java.util.List;

import br.com.sann.criteria.dao.AttributeSpatialDataDAO;
import br.com.sann.criteria.dao.SpatialDataDAO;
import br.com.sann.criteria.dao.impl.AttributeSpatialDataDAOImpl;
import br.com.sann.criteria.dao.impl.SpatialDataDAOImpl;
import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.domain.SpatialData;
import br.com.sann.service.AttributeSpatialDataService;

/**
 * Classe de negócio relacionado com {@link AttributeSpatialData}.
 * 
 * @author Hamon
 *
 */
public class AttributeSpatialDataServiceImpl implements AttributeSpatialDataService{
	
	private AttributeSpatialDataDAO dao;
	private SpatialDataDAO spatialDataDao;
	
	/**
	 * Construtor padrão.
	 */
	public AttributeSpatialDataServiceImpl() {
		
		dao = new AttributeSpatialDataDAOImpl();
		spatialDataDao = new SpatialDataDAOImpl();
	}

	@Override
	public List<AttributeSpatialData> recoverAllAttributes() {

		return dao.recoverAllAttributeService();
	}

	@Override
	public void insertAttributesServiceList(List<SpatialData> list) {
		for (SpatialData spatialData : list) {
			spatialDataDao.insertSpatialData(spatialData);			
			dao.insertAttributesServiceList(spatialData.getAttributesService());		
		}
	}

	@Override
	public void updateAttributeService(AttributeSpatialData attributeService) {
		dao.updateAttributeService(attributeService);
	}

	@Override
	public List<AttributeSpatialData> recoverAttributesBySpatialData(
			Integer idSpatialData) {
		return dao.recoverAttributesBySpatialData(idSpatialData);
	}

}
