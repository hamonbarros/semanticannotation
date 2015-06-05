package br.com.sann.performance.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SemanticAnnotation;
import br.com.sann.domain.SpatialData;
import br.com.sann.main.Main;
import br.com.sann.service.FeatureService;
import br.com.sann.service.OntologyConceptService;
import br.com.sann.service.SemanticAnnotationService;
import br.com.sann.service.impl.FeatureServiceImpl;
import br.com.sann.service.impl.OntologyConceptServiceImpl;
import br.com.sann.service.impl.SemanticAnnotationServiceImpl;

public class ExtractResultPosAnnotation {

	private OntologyConceptService conceptService;
	private FeatureService featureService;
	private SemanticAnnotationService sannService;
	public static Logger log;
	
	public ExtractResultPosAnnotation() {
		conceptService = new OntologyConceptServiceImpl();
		featureService = new FeatureServiceImpl();
		sannService = new SemanticAnnotationServiceImpl();
	}
	
	public List<ExpectedResult> populatedExpectedResult() {
		
		List<ExpectedResult> expectedResult = new ArrayList<ExpectedResult>();

		PrintWriter result = null;
		
		try {
			
			DateFormat df = new SimpleDateFormat("YYYYMMddHHmm");
			String dateFormated = df.format(new Date());
			result = new PrintWriter(new FileWriter(new File("Result_WFS_" + dateFormated + ".txt")));
			
			List<OntologyConcept> relevantConceptsGeneral = new ArrayList<OntologyConcept>();
			List<String> retrievedConceptsGeneral = new ArrayList<String>();
			List<String> retrievedRelevantConceptsGeneral = new ArrayList<String>();
			
			Scanner scanner = new Scanner(new FileReader("Baseline_FT.txt")).useDelimiter("\\n");
			String line = scanner.next();
			int countConcepts = 0;
			int countAnnotatedConcepts = 0;
			while (scanner.hasNext()) {
				  line = scanner.next();
				  String[] lineSplit = line.split("\\|");
				  String title = lineSplit[0];
				  log.info("[INICIO] Início do processamento para o título: " + title);
				  String[] idsOntology = lineSplit[1].split(",");
				  List<OntologyConcept> relevantConcepts = recoveryOntolgyConcept(idsOntology);
				  
				  ExpectedResult eResult = new ExpectedResult();
				  eResult.setTitle(title);
				  eResult.setRelevantConcepts(relevantConcepts);
				  
				  SpatialData featureType = featureService.recoverySpatialDataByTitle(title).get(0);
				  eResult.convertAnnotationType(featureType.getTypeAnnotation());
				  List<SemanticAnnotation> sanns = sannService.recoveryAnnotations(featureType.getId());
				  eResult.setRetrievedConcepts(extractRetrievedConcepts(sanns));
				  extractRetrievedRelevantConcepts(eResult);
				  expectedResult.add(eResult);		
				  
				  relevantConceptsGeneral.addAll(eResult.getRelevantConcepts());
				  retrievedConceptsGeneral.addAll(eResult.getRetrievedConcepts());
				  retrievedRelevantConceptsGeneral.addAll(eResult.getRelevantRetrievedConcepts());
				  
				  countConcepts++;
				  if (!eResult.getRetrievedConcepts().isEmpty()) {
					  countAnnotatedConcepts++;
				  }
				  
				  result.println("---------------------------------------------------");
				  result.println("Título: " + title + " - Tipo de Anotação: " + eResult.getAnnotationType());
				  result.println("Conceitos relevantes: " + eResult.getConceptNames(relevantConcepts));
				  result.println("Conceitos recuperados: " + extractRetrievedConceptNames(sanns));
				  result.println("Conceitos relevantes recuperados: " + eResult.getRelevantRetrievedConcepts());	
				  result.println();
				  extractMetrics(eResult.getRelevantConcepts(), eResult.getRetrievedConcepts(), eResult.getRelevantRetrievedConcepts(), result);
				  result.println("---------------------------------------------------");
				  result.flush();
				  
				  log.info("[FIM] Fim do processamento para o título: " + title);
			}
			
			extractMetrics(relevantConceptsGeneral, retrievedConceptsGeneral, retrievedRelevantConceptsGeneral, result);
			
			double percentual = 0.0;
			if (countConcepts > 0) {
				percentual = (double) countAnnotatedConcepts/countConcepts;
			}
			result.println("Percentual anotado: " + String.format(Locale.ENGLISH, "%.2f", percentual));
			log.info("Percentual anotado: " + String.format(Locale.ENGLISH, "%.2f", percentual));
			
			result.flush();
			result.close();
			
		} catch (FileNotFoundException e) {
			log.error("Arquivo não encontrado.");
		} catch (IOException e) {
			log.error("IOExpection");
		}
		return expectedResult;
	}
	
	private Set<String> extractRetrievedConcepts(List<SemanticAnnotation> sanns) {

		Set<String> retrievedConcepts = new HashSet<String>();
		for (SemanticAnnotation sann : sanns) {
			retrievedConcepts.add(sann.getConceptid().getConcept());
		}
		return retrievedConcepts;
	}

	private Set<String> extractRetrievedConceptNames(List<SemanticAnnotation> sanns) {

		Set<String> retrievedConcepts = new HashSet<String>();
		for (SemanticAnnotation sann : sanns) {
			retrievedConcepts.add(sann.getConceptid().getConceptName() + "-" + sann.getConceptid().getId());
		}
		return retrievedConcepts;
	}
	
	private List<OntologyConcept> recoveryOntolgyConcept(String[] idsOntology) {
		
		return conceptService.recoveryOntolgyConceptByIds(idsOntology);
	}
	
	private void extractRetrievedRelevantConcepts(ExpectedResult eResult) {
		Set<String> retrievedRelevantConcepts = new HashSet<String>();
		for (OntologyConcept ontologyConcept : eResult.getRelevantConcepts()) {
			for (String retrievedConcept : eResult.getRetrievedConcepts()) {
				if (ontologyConcept.getConcept().equals(retrievedConcept)) {
					retrievedRelevantConcepts.add(retrievedConcept);
				}
			}
		}
		eResult.setRelevantRetrievedConcepts(retrievedRelevantConcepts);
	}
	
	private void extractMetrics (Collection<OntologyConcept> relevantConcepts, Collection<String> retrievedConcepts, 
			Collection<String> retrievedRelevantConcepts, PrintWriter result) {
		Double precision = 0.0;
		if (retrievedConcepts.size() != 0) {
			precision = Double.parseDouble(retrievedRelevantConcepts.size()+"")/Double.parseDouble(retrievedConcepts.size()+"");
		}
		Double cobertura = 0.0;
		if (relevantConcepts.size() != 0) {
			cobertura = Double.parseDouble(retrievedRelevantConcepts.size()+"")/Double.parseDouble(relevantConcepts.size()+"");
		}
		result.println("Precisão: " + precision);
		result.println("Cobertura: " + cobertura);
		
		log.info("Precisão: " + precision);
		log.info("Cobertura: " + cobertura);
	}
	
	public static void main(String[] args) {
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		
		log = Logger.getLogger(Main.class);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		log.info("Iniciando o processamento...");
		log.info("Data Inicial: " + df.format(new Date()));
		
		ExtractResultPosAnnotation performanceTest = new ExtractResultPosAnnotation();
		performanceTest.populatedExpectedResult();
		
		log.info("Fim do processamento!");
		log.info("Data Final: " + df.format(new Date()));
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	}
}
