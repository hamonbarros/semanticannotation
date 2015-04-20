package br.com.sann.ows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.sann.domain.Service;
import br.com.sann.domain.SpatialData;
import br.com.sann.ows.parser.ParserException;
import br.com.sann.ows.parser.WFSParser;
import br.com.sann.service.AttributeSpatialDataService;
import br.com.sann.service.FeatureService;
import br.com.sann.service.ServiceService;
import br.com.sann.service.impl.AttributeSpatialDataServiceImpl;
import br.com.sann.service.impl.FeatureServiceImpl;
import br.com.sann.service.impl.ServiceServiceImpl;


/**
 * Classe responsável por importar os atributos dos serviços WFS na base de dados.
 * 
 * @author Hamon
 */
public class ImportAttributesWFS {
	

	private static final String WFS = "wfs";
	public static Logger log;
	
	public void importFTAndAttributes() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		String infoLine = "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%";
		String infoInit = "Início do processo de importação dos atributos - " + df.format(new Date());
		log.info(infoLine);
		log.info(infoInit);
		System.out.println(infoLine);
		System.out.println(infoInit);
		ServiceService sericeService = new ServiceServiceImpl();
		AttributeSpatialDataService attributeService = new AttributeSpatialDataServiceImpl();
		
		for (Service service : sericeService.recoverAllServices()) {
			String urlService = service.getURL();
			if (urlService.toLowerCase().indexOf(WFS) > 0) {
				String infoExtract = "Extraindo os atributos do serviço: " + service.getId() + " - " + service.getName();
				log.info(infoExtract);
				System.out.println(infoExtract);
				WFSParser parser = new WFSParser();
				try {
					attributeService.insertAttributesServiceList(parser.getFeatureTypeAndAttributes(urlService, service));
				} catch (ParserException e) {
					log.equals(e.getMessage());
				}
			}
		}
		String infoFinish = "Fim do processo de importação dos atributos - " + df.format(new Date());
		log.info(infoFinish);
		log.info(infoLine);
		System.out.println(infoFinish);
		System.out.println(infoLine);
	}

	public void updateFeatureTypes() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		String infoLine = "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%";
		String infoInit = "Início do processo de atualização dos feature types - " + df.format(new Date());
		log.info(infoLine);
		log.info(infoInit);
		System.out.println(infoLine);
		System.out.println(infoInit);
		ServiceService sericeService = new ServiceServiceImpl();
		FeatureService featureService = new FeatureServiceImpl();
		
		for (Service service : sericeService.recoverAllServices()) {
			String urlService = service.getURL();
			if (urlService.toLowerCase().indexOf(WFS) > 0) {
				String infoExtract = "Extraindo os atributos do serviço: " + service.getId() + " - " + service.getName();
				log.info(infoExtract);
				System.out.println(infoExtract);
				WFSParser parser = new WFSParser();
				try {
					List<SpatialData> featureTypes = parser.getFeatureTypes(urlService, service);
					for (SpatialData feature : featureTypes) {
						List<SpatialData> ftPersistidas = featureService.recoverySpatialDataByTextInfo(
								feature.getName(), feature.getTitle(), feature.getTextDescription(), feature.getKeywords());
						for (SpatialData ftPersistida : ftPersistidas) {
							ftPersistida.setService(service);
						}
						featureService.updateSpatialDataList(ftPersistidas);
					}
				} catch (ParserException e) {
					log.equals(e.getMessage());
				}
			}
		}
		String infoFinish = "Fim do processo de importação dos atributos - " + df.format(new Date());
		log.info(infoFinish);
		log.info(infoLine);
		System.out.println(infoFinish);
		System.out.println(infoLine);
	}
	
	public static void main(String[] args) {
		
		log = Logger.getLogger(ImportAttributesWFS.class);
		ImportAttributesWFS importer = new ImportAttributesWFS();
		importer.importFTAndAttributes();
//		importer.updateFeatureTypes();
	}
}
