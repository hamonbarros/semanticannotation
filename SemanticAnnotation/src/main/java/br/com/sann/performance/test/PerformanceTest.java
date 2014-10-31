package br.com.sann.performance.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.sann.domain.Extractor;
import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SpatialData;
import br.com.sann.main.Main;
import br.com.sann.service.FeatureService;
import br.com.sann.service.OntologyConceptService;
import br.com.sann.service.impl.FeatureServiceImpl;
import br.com.sann.service.impl.OntologyConceptServiceImpl;
import br.com.sann.service.processing.text.BagOfWords;

public class PerformanceTest {

	private OntologyConceptService conceptService;
	private FeatureService featureService;
	
	public PerformanceTest() {
		conceptService = new OntologyConceptServiceImpl();
		featureService = new FeatureServiceImpl();
	}
	
	public List<ExpectedResult> populatedExpectedResult() {
		
		List<ExpectedResult> expectedResult = new ArrayList<ExpectedResult>();
		
		Logger log = Logger.getLogger(PerformanceTest.class);
		log.info("Realizando a leitura dos conceitos ontológicos ... ");
		OntologyConceptService conceptService = new OntologyConceptServiceImpl();
		List<OntologyConcept> ontologyConcepts = conceptService.recoverAllOntologyConcept();
		log.info("Leitura dos conceitos ontológicos realizada com sucesso!");
		PrintWriter result = null;
		
		try {
			
			DateFormat df = new SimpleDateFormat("YYYYMMddHHmm");
			String dateFormated = df.format(new Date());
			result = new PrintWriter(new FileWriter(new File("Result_" + dateFormated + ".txt")));
			
			List<OntologyConcept> relevantConceptsGeneral = new ArrayList<OntologyConcept>();
			List<String> retrievedConceptsGeneral = new ArrayList<String>();
			List<String> retrievedRelevantConceptsGeneral = new ArrayList<String>();
			
			Scanner scanner = new Scanner(new FileReader("Dados de Teste.txt"))
				.useDelimiter("\\n");
			String line = scanner.next();
			while (scanner.hasNext()) {
				  line = scanner.next();
				  String[] lineSplit = line.split("\\|");
				  String title = lineSplit[0];
				  String[] idsOntology = lineSplit[1].split(",");
				  List<OntologyConcept> relevantConcepts = recoveryOntolgyConcept(idsOntology);
				  
				  ExpectedResult eResult = new ExpectedResult();
				  eResult.setTitle(title);
				  eResult.setRelevantConcepts(relevantConcepts);
				  eResult.setSpatialData(featureService.recoverSpatialDataByTitle(title));
				  extractRetrievedConcepts(eResult, ontologyConcepts);
				  extractRetrievedRelevantConcepts(eResult);
				  expectedResult.add(eResult);		
				  
				  relevantConceptsGeneral.addAll(eResult.getRelevantConcepts());
				  retrievedConceptsGeneral.addAll(eResult.getRetrievedConcepts());
				  retrievedRelevantConceptsGeneral.addAll(eResult.getRelevantRetrievedConcepts());
				  
				  result.println("---------------------------------------------------");
				  result.println("Título: " + title);
				  result.println("Conceitos relevantes: " + eResult.getConcepts(relevantConcepts));
				  result.println("Conceitos recuperados: " + eResult.getRetrievedConcepts());
				  result.println("Conceitos relevantes recuperados: " + eResult.getRelevantRetrievedConcepts());				  
				  result.println("---------------------------------------------------");
			}
			
			extractMetrics(relevantConceptsGeneral, retrievedConceptsGeneral, retrievedRelevantConceptsGeneral, result);
			
			result.flush();
			result.close();
			
		} catch (FileNotFoundException e) {
			log.error("Arquivo não encontrado.");
		} catch (IOException e) {
			log.error("IOExpection");
		}
		return expectedResult;
	}
	
	private List<OntologyConcept> recoveryOntolgyConcept(String[] idsOntology) {
		
		return conceptService.recoveryOntolgyConceptByIds(idsOntology);
	}
	
	private void extractRetrievedConcepts(ExpectedResult eResult, List<OntologyConcept> ontologyConcepts) {
		
		BagOfWords bw = new BagOfWords(eResult.getSpatialData());
		StringBuffer storeBagsOfWords = new StringBuffer();
		storeBagsOfWords.append(bw.extractTextProperties());
		try {
			Set<String> retrievedConcepts = Main.executeSimilarity(eResult.getTitle(),
					bw.extractWordList(storeBagsOfWords.toString()), ontologyConcepts);
			eResult.setRetrievedConcepts(retrievedConcepts);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	private void extractMetrics (List<OntologyConcept> relevantConcepts, List<String> retrievedConcepts, 
			List<String> retrievedRelevantConcepts, PrintWriter result) {
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
	}
	
	public static void main(String[] args) {
		
		PerformanceTest performanceTest = new PerformanceTest();
		performanceTest.populatedExpectedResult();
	}
}
