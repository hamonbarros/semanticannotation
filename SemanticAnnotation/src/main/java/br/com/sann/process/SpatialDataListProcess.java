package br.com.sann.process;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SpatialData;
import br.com.sann.domain.Sumary;
import br.com.sann.main.Main;
import br.com.sann.service.OntologyConceptService;
import br.com.sann.service.impl.OntologyConceptServiceImpl;
import br.com.sann.service.processing.text.BagOfWords;

public class SpatialDataListProcess extends ParentProcess{
	
	public void execute(List<SpatialData> spatialDataList) {
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		
		Logger log = Logger.getLogger(Main.class);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		log.info("Iniciando o processamento...");
		log.info("Data Inicial: " + df.format(new Date()));
		
		log.info("Realizando a leitura dos conceitos ontológicos ... ");
		OntologyConceptService conceptService = new OntologyConceptServiceImpl();
		List<OntologyConcept> concepts = conceptService.recoverAllOntologyConcept();
		log.info("Leitura dos conceitos ontológicos realizada com sucesso!");
		
		log.info("Inicio da consulta das classes e categorias na dbpedia que contenham relevância com os títulos...");
		
		PrintWriter similarity = null;
		try {
			DateFormat dformat = new SimpleDateFormat("YYYYMMddHHmm");
			String dateFormated = dformat.format(new Date());

			similarity = new PrintWriter(new FileWriter(new File("Similarity" + dateFormated + ".txt")));
			String previousTitle = "";
			StringBuffer storeBagsOfWords = new StringBuffer();
			BagOfWords bw = null;
			Sumary sumary = new Sumary();
			
			for (SpatialData spatialData : spatialDataList) {
				String title = spatialData.getTitle();				
				bw = new BagOfWords(spatialData);
				// Tratamento necessário para o ínicio do processo. Quando a storeBagsOfWords ainda está vazia.
				if (title.equals(previousTitle)) {					
					storeBagsOfWords.append(" ");
					storeBagsOfWords.append(bw.extractTextProperties());					
				} else {					
					if (previousTitle.equals("")) {
						storeBagsOfWords.append(bw.extractTextProperties());
						previousTitle = title;
						continue;						
					}					
					log.info("[INICIO] Início do processamento para o título: " + previousTitle);
					executeSimilarity(previousTitle, bw.extractWordList(storeBagsOfWords.toString()), 
							similarity, sumary);
					log.info("[FIM] Fim do processamento para o título: " + previousTitle);
					
					// Cria uma nova instância quando o título é diferente do anterior.
					storeBagsOfWords = new StringBuffer();
					storeBagsOfWords.append(bw.extractTextProperties());
					previousTitle = title;	
				}								
			}			
			if (!previousTitle.isEmpty() && bw != null) {			
				log.info("[INICIO] Início do processamento para o título: " + previousTitle);
				executeSimilarity(previousTitle, bw.extractWordList(storeBagsOfWords.toString()), 
						similarity, sumary);
				log.info("[FIM] Fim do processamento para o título: " + previousTitle);
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

	