package br.com.sann.service.ontology;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.log4j.Logger;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.service.OntologyConceptService;
import br.com.sann.service.impl.OntologyConceptServiceImpl;
import br.com.sann.service.processing.text.PreProcessingText;

import com.hp.hpl.jena.util.FileManager;

public class NTReader {

	
	private static final int SKIP_INVALID_CHAR = 3;
	private static final char END_RELATION = '>';
	private static final char END_SUBJECT = '>';
	
	public static Logger log;

	public static void main(String[] args) {
		
		log = Logger.getLogger(NTReader.class);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		log.info("Iniciando o processamento...");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		log.info("Data Inicial: " + df.format(new Date()));
		
		OntologyConceptService service = new OntologyConceptServiceImpl();
		PreProcessingText preProcessingText = new PreProcessingText();
		
		InputStream in = FileManager.get().open("C:/yago_en.nt");
		if (in == null) {
		    throw new IllegalArgumentException("File not found");
		}
		Scanner sc = new Scanner(in);
		int count = 0;
		while (sc.hasNextLine()) {
			count++;
			String line = sc.nextLine();
			System.out.println(line);
			log.info(line);
			char[] charArray = line.toCharArray();
			if (charArray != null && charArray.length > 0) {			
				int endSubject = line.indexOf(END_SUBJECT);
				String subject = line.substring(1, endSubject);
				String subLine = line.substring(endSubject + SKIP_INVALID_CHAR);
				int endRelation = subLine.indexOf(END_RELATION);
				String relation = subLine.substring(0, endRelation);
				String predicate = subLine.substring(endRelation + SKIP_INVALID_CHAR, 
						subLine.length() - SKIP_INVALID_CHAR);
				
				OntologyConcept concept = new OntologyConcept();
				concept.setConcept(subject);
				concept.setConceptName(predicate);
				concept.setNormalizedName(preProcessingText.normalizeText(predicate));
				try {					
					if (service.recoveryOntologyByURI(subject) == null) {					
						service.saveOntologyConcept(concept);
					}
				} catch (Exception e) {
					log.error(e.getMessage() + " " + e.getCause());
				}
				
				System.out.println("Sujeito: "+ subject);
				System.out.println("Relacao: " + relation);
				System.out.println("Predicado: " + predicate);
				System.out.println("-------------------------------------------------------------");
				log.info("Sujeito: "+ subject);
				log.info("Relacao: " + relation);
				log.info("Predicado: " + predicate);
				log.info("-------------------------------------------------------------");
			}

			System.out.println("Conceitos persistidos: " + count);
			log.info("Fim do processamento!");
			log.info("Data Final: " + df.format(new Date()));
			log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

			
		}
	}
}
