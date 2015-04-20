package br.com.sann.process;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.sann.attribute.process.AttributeListProcess;
import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.domain.SpatialData;
import br.com.sann.domain.Sumary;
import br.com.sann.main.Main;
import br.com.sann.service.impl.AttributeSpatialDataServiceImpl;
import br.com.sann.service.processing.text.BagOfWords;
import br.com.sann.service.processing.text.PreProcessingText;

public class SpatialDataAttrsListProcessYago extends ParentProcess{
	
	public void execute(List<SpatialData> spatialDataList) {
		
		PreProcessingText preprocessing = PreProcessingText.getInstance();
		AttributeSpatialDataServiceImpl attributeSDService = new AttributeSpatialDataServiceImpl();
		AttributeListProcess attributeProcess = new AttributeListProcess();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		
		Logger log = Logger.getLogger(Main.class);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		log.info("Iniciando o processamento...");
		log.info("Data Inicial: " + df.format(new Date()));
			
		log.info("Inicio da consulta das classes e categorias na dbpedia que contenham relevância com os títulos...");
		
		PrintWriter similarity = null;
		try {
			DateFormat dformat = new SimpleDateFormat("YYYYMMddHHmm");
			String dateFormated = dformat.format(new Date());

			similarity = new PrintWriter(new FileWriter(new File("Similarity" + dateFormated + ".txt")));
			StringBuffer storeBagsOfWords = new StringBuffer();
			BagOfWords bw = null;
			Sumary sumary = new Sumary();
			
			int countGC = 0;
//			for(int i=0; i<1; i++) {
//				SpatialData spatialData = spatialDataList.get(i);
			for (SpatialData spatialData : spatialDataList) {
				String title = spatialData.getTitle();			
				title = preprocessing.extractUnderline(title);
				title = preprocessing.tokenizingTextWithUppercase(title);
				bw = new BagOfWords(spatialData);
				storeBagsOfWords.append(bw.extractTextProperties());	
				log.info("[INICIO] Início do processamento para o título: " + title);
				String bagOfWords = bw.extractWordList(storeBagsOfWords.toString());
				executeSimilarityYago(spatialData, title, bagOfWords, similarity, sumary);
				if (!spatialData.getAnnotated()) {
					executeSimilarity(spatialData, title, bagOfWords, similarity, sumary);
				}
				// Lista os atributos persistidos
				List<AttributeSpatialData> attributes = attributeSDService
						.recoverAttributesBySpatialData(spatialData.getId()); 				
				// Pesquisa os conceitos da ontologia que representam os atributos
				attributeProcess.execute(attributes);
				log.info("[FIM] Fim do processamento para o título: " + title);
					
				storeBagsOfWords = new StringBuffer();
				countGC++;
				if (countGC == 50) {
					System.gc();
					countGC = 0;
				}
			}			
			similarity.println(sumary.toString());			
			log.info("Finalização da consulta das classes e categorias na dbpedia que contenham relevância com os títulos.");
			
			similarity.flush();
			similarity.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log.info("Fim do processamento!");
		log.info("Data Final: " + df.format(new Date()));
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

	}	
	
}

	