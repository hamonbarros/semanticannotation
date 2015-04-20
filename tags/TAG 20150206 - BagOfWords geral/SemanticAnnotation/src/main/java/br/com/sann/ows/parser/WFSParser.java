package br.com.sann.ows.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.type.PropertyDescriptor;

import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.domain.Service;
import br.com.sann.domain.SpatialData;

/**
 * Classe para ler o retorno de um serviço WFS.
 * 
 * @author Hamon
 *
 */
public class WFSParser {

	private static final String GET_CAPABILITIES = "?service=WFS&request=GetCapabilities&version=1.0.0";
	private DataStore dataStore;
	
	protected String extractUrlService(String urlServiceWfs) {
		if (urlServiceWfs == null) {
			return null;
		}
		Integer index = urlServiceWfs.indexOf("?");
		if (index > 0 && index < urlServiceWfs.length()) {
			return urlServiceWfs.substring(0, index);
		}
		return urlServiceWfs; 
	}
	
	public List<SpatialData> getFeatureTypeAndAttributes(String urlServiceWfs, Service service) throws ParserException {
		
		List<SpatialData> result = new ArrayList<SpatialData>();
		
		try {
			String ulrService = extractUrlService(urlServiceWfs) + GET_CAPABILITIES;		
			System.out.println(ulrService);
			Map params = new HashMap();
			params.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", ulrService);
			dataStore = DataStoreFinder.getDataStore(params);		
			result.addAll(recoveryFTAndAttributes(service));
		} catch (Exception e) {
			throw new ParserException("Não foi possível recuperar os atributos do serviço " + urlServiceWfs);
		}
		
		return result;
	}
	
	public List<SpatialData> getFeatureTypes(String urlServiceWfs, Service service) throws ParserException {
		
		List<SpatialData> result = new ArrayList<SpatialData>();
		
		try {
			String ulrService = extractUrlService(urlServiceWfs) + GET_CAPABILITIES;		
			System.out.println(ulrService);
			Map params = new HashMap();
			params.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", ulrService);
			dataStore = DataStoreFinder.getDataStore(params);		
			result.addAll(recoveryFeatureTypes(service));
		} catch (Exception e) {
			throw new ParserException("Não foi possível recuperar os atributos do serviço " + urlServiceWfs);
		}
		
		return result;
	}
	
	/**
	 * Popula a entidade do feature type com as informações gerais.
	 * 
	 * @param nameFeature
	 *            O nome da feature type.
	 * @return A feature type populada.
	 * @throws IOException 
	 */
	private SpatialData loadFeatureType(String nameFeature) throws IOException {
		FeatureSource featureSource = dataStore.getFeatureSource(nameFeature);
		SpatialData featureType = new SpatialData();
		featureType.setName(nameFeature);
		featureType.setTitle(featureSource.getInfo().getTitle());
		String keywords = "";
		if (featureSource.getInfo().getKeywords() != null) {			
			for(String keyword : featureSource.getInfo().getKeywords()) {
				keywords += keyword + " ";
			}
		}
		featureType.setKeywords(keywords.trim());
		featureType.setTextDescription(featureSource.getInfo().getDescription());
		featureType.setAnnotated(false);
		return featureType;
	}
	
	/**
	 * Recupera os atributos de um serviço.
	 * 
	 * @param service
	 *            O serviço.
	 * @param dataStore
	 *            O dataStore.
	 * @return A lista de feature types do serviço.
	 * @throws IOException Exceção de indisponibilidade do serviço.
	 */
	private List<SpatialData> recoveryFTAndAttributes (Service service) 
		throws IOException {
		List<SpatialData> result = new ArrayList<SpatialData>();
		String[] typeNames = dataStore.getTypeNames();
		for (String name : typeNames) {
			try {
				SpatialData featureType = loadFeatureType(name);
				featureType.setService(service);
				List<AttributeSpatialData> attributtes  = new ArrayList<AttributeSpatialData>();
				FeatureCollection features = dataStore.getFeatureSource(name).getFeatures();
	            org.opengis.feature.type.FeatureType schema = features.getSchema();
	            
	            Iterator<PropertyDescriptor> it = schema.getDescriptors().iterator();
	            while(it.hasNext()){
	                  PropertyDescriptor desc = it.next();
	                  AttributeSpatialData attribute = new AttributeSpatialData();
	                  attribute.setName(desc.getName().toString());
	                  attribute.setType(desc.getType().toString());
	                  attribute.setSpatialData(featureType);
	                  attribute.setAnnotated(false);
	                  attributtes.add(attribute);
	            }
	            featureType.setAttributesService(attributtes);
	            result.add(featureType);
			} catch (Exception ex) {
				System.err.println("Não foi possível recuperar os atributos do feature type " + name);
				continue;
			}
		}
		return result;
	}
	
	/**
	 * Recupera os feature types de um serviço.
	 * 
	 * @param service
	 *            O serviço.
	 * @param dataStore
	 *            O dataStore.
	 * @return A lista de feature types do serviço.
	 * @throws IOException Exceção de indisponibilidade do serviço.
	 */
	private List<SpatialData> recoveryFeatureTypes (Service service) 
		throws IOException {
		List<SpatialData> result = new ArrayList<SpatialData>();
		String[] typeNames = dataStore.getTypeNames();
		for (String name : typeNames) {
			try {
				SpatialData featureType = loadFeatureType(name);
				featureType.setService(service);
	            result.add(featureType);
			} catch (Exception ex) {
				System.err.println("Não foi possível recuperar os atributos do feature type " + name);
				continue;
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) throws IOException {

		String getCapabilities = "http://www.geoservicos.inde.gov.br/geoserver/ows?service=WFS&request=GetCapabilities&version=1.0.0";
		
		WFSParser wfsParser = new WFSParser();
		try {
			List<SpatialData> fts = wfsParser.getFeatureTypeAndAttributes(getCapabilities, new Service());
			for (SpatialData ft : fts) {
				System.out.println(ft);
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
	}

}
