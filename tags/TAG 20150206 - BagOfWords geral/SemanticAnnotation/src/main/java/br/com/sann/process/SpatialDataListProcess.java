package br.com.sann.process;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.sann.domain.SpatialData;
import br.com.sann.domain.Sumary;
import br.com.sann.main.Main;
import br.com.sann.service.processing.text.BagOfWords;

public class SpatialDataListProcess extends ParentProcess{
	
	public void execute(List<SpatialData> spatialDataList) {
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		
		Logger log = Logger.getLogger(Main.class);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		log.info("Iniciando o processamento...");
		log.info("Data Inicial: " + df.format(new Date()));
			
		log.info("Inicio da consulta das classes e categorias na dbpedia que contenham relev�ncia com os t�tulos...");
		
		PrintWriter similarity = null;
		try {
			DateFormat dformat = new SimpleDateFormat("YYYYMMddHHmm");
			String dateFormated = dformat.format(new Date());

			similarity = new PrintWriter(new FileWriter(new File("Similarity" + dateFormated + ".txt")));
			StringBuffer storeBagsOfWords = new StringBuffer();
			BagOfWords bw = null;
			Sumary sumary = new Sumary();
			
			int countGC = 0;
//			for(int i=0; i<10; i++) {
//				SpatialData spatialData = spatialDataList.get(i);
			for (SpatialData spatialData : spatialDataList) {
				String title = spatialData.getTitle();				
				bw = new BagOfWords(spatialData);
				storeBagsOfWords.append(bw.extractTextProperties());	
				log.info("[INICIO] In�cio do processamento para o t�tulo: " + title);
				executeSimilarity(spatialData, title, bw.extractWordList(storeBagsOfWords.toString()), 
					similarity, sumary);
				log.info("[FIM] Fim do processamento para o t�tulo: " + title);
					
				storeBagsOfWords = new StringBuffer();
				countGC++;
				if (countGC == 25) {
					System.gc();
					countGC = 0;
				}
			}			
			similarity.println(sumary.toString());			
			log.info("Finaliza��o da consulta das classes e categorias na dbpedia que contenham relev�ncia com os t�tulos.");
			
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

	