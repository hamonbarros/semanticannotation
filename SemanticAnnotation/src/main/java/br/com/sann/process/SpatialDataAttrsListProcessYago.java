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
import br.com.sann.domain.Extractor;
import br.com.sann.domain.SpatialData;
import br.com.sann.domain.Sumary;
import br.com.sann.main.MainAnnotationFTAttrs;
import br.com.sann.service.impl.AttributeSpatialDataServiceImpl;
import br.com.sann.service.processing.text.BagOfWords;
import br.com.sann.service.processing.text.PreProcessingText;

public class SpatialDataAttrsListProcessYago extends ParentProcess{
	
	public void execute(List<SpatialData> spatialDataList) {
		
		Logger log = Logger.getLogger(MainAnnotationFTAttrs.class);
		log.info("[INICIO] Inicio da execução do processo de anotação semântica...");

		PreProcessingText preprocessing = PreProcessingText.getInstance();
		AttributeSpatialDataServiceImpl attributeSDService = new AttributeSpatialDataServiceImpl();
		AttributeListProcess attributeProcess = new AttributeListProcess();	
		
		PrintWriter resultSannFTAttrs = null;
		try {
			DateFormat dformat = new SimpleDateFormat("YYYYMMddHHmm");
			String dateFormated = dformat.format(new Date());

			resultSannFTAttrs = new PrintWriter(new FileWriter(new File("ResultSannFTAttrs" + dateFormated + ".txt")));
			StringBuffer storeBagsOfWords = new StringBuffer();
			BagOfWords bw = null;
			Sumary sumary = new Sumary();
			
			int countGC = 0;
			for (SpatialData spatialData : spatialDataList) {
				String title = spatialData.getTitle();			
				title = preprocessing.extractUnderline(title);
				title = preprocessing.tokenizingTextWithUppercase(title);
				title = preprocessing.extractWordsDefault(title);
				bw = new BagOfWords(spatialData);
				storeBagsOfWords.append(bw.extractTextProperties());	
				log.info("[INICIO] Início do processamento para o título: " + title);
				String bagOfWords = bw.extractWordList(storeBagsOfWords.toString()) + title;				
				log.info("[INICIO] Início da anotação semântica via LOD");
				List<Extractor> extractorList = executeSimilarityYago(spatialData, title, bagOfWords, resultSannFTAttrs, sumary);
				log.info("[FIM] Fim da anotação semântica via LOD");
				if (!spatialData.getAnnotated()) {
					resultSannFTAttrs.println("------ NÃO ENCONTROU CONCEITOS VIA LOD! -----");
					log.info("-------->>>>>>> Não encontrou conceitos via LOD!");
					log.info("[INICIO] Início da anotação semântica via casamento de Strings");
					executeSannWords(extractorList, spatialData, title, resultSannFTAttrs, sumary);
					if (!spatialData.getAnnotated()) {
						log.info("RESULTADO: NÃO ANOTADO!");
					} else {
						log.info("RESULTADO: ANOTADO!");
					}
					log.info("[FIM] Fim da anotação semântica via casamento de Strings");
				}
				log.info("[INICIO] Início da anotação semântica dos atributos");
				List<AttributeSpatialData> attributes = attributeSDService
						.recoverAttributesBySpatialData(spatialData.getId()); 				
				attributeProcess.execute(attributes);
				log.info("[FIM] Fim da anotação semântica dos atributos");
				log.info("[FIM] Fim do processamento para o título: " + title);
					
				storeBagsOfWords = new StringBuffer();
				countGC++;
				if (countGC == 50) {
					System.gc();
					countGC = 0;
				}
			}			
			resultSannFTAttrs.println(sumary.toString());						
			resultSannFTAttrs.flush();
			resultSannFTAttrs.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("[FIM] Fim da execução do processo de anotação semântica.");
	}	
	
}

	