package br.com.sann.main;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.sann.domain.SpatialData;
import br.com.sann.process.SpatialDataAttrsListProcessYago;
import br.com.sann.service.FeatureService;
import br.com.sann.service.impl.FeatureServiceImpl;

public class MainAnnotationFTAttrs {	

	public static Logger log;
	/**
	 * Método principal.
	 * 
	 * @param args Os argumentos de entrada.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		log = Logger.getLogger(MainAnnotationFTAttrs.class);
		log.info("Realizando a leitura das feature types... ");
		FeatureService service = new FeatureServiceImpl();
		List<SpatialData> spatialDataList = service.recoverAllSpatialDataNotAnnotated();
//		String ids = "1082";
//		List<SpatialData> spatialDataList = service.recoverySpatialDataByIDs(ids);
		log.info("Leitura das feature types realizada com sucesso!");
		
//		SpatialDataListProcess process = new SpatialDataListProcess();
		SpatialDataAttrsListProcessYago process = new SpatialDataAttrsListProcessYago();
		process.execute(spatialDataList);

	}
}
