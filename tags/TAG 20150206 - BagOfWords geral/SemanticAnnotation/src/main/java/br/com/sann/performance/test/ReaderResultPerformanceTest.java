package br.com.sann.performance.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.service.impl.OntologyConceptServiceImpl;

public class ReaderResultPerformanceTest {
	
	public static void main(String[] args) {
		
		try {
			Scanner sc = new Scanner(new FileInputStream(new File("Result_Yago_201503090131.txt")));
			while (sc.hasNextLine()) {				
				// Linha tracejada
				System.out.println(sc.nextLine());
				// Título
				System.out.println(sc.nextLine());
				// Conceitos Relevantes
				String conceitosRelevantes = sc.nextLine();
				conceitosRelevantes = conceitosRelevantes.replace("Conceitos relevantes: [", "");
				conceitosRelevantes = conceitosRelevantes.replace("]", "");
				System.out.println();
				// Conceitos Recuperados
				String conceitosRecuperados = sc.nextLine();
				conceitosRecuperados = conceitosRecuperados.replace("Conceitos recuperados: [", "");
				conceitosRecuperados = conceitosRecuperados.replace("]", "");
				System.out.println("CONCEITOS RECUPERADOS NÃO RELEVANTES");
				recoveryIdsDifferentConcepts(conceitosRecuperados, conceitosRelevantes);
				// Conceitos Relevantes Recuperados
				String concRelevRecuperados = sc.nextLine();
				concRelevRecuperados = concRelevRecuperados.replace("Conceitos relevantes recuperados: [", "");
				concRelevRecuperados = concRelevRecuperados.replace("]", "");
				// Linha em branco
				sc.nextLine();
				// Precisão
				sc.nextLine();
				// Cobertura
				sc.nextLine();
				// Linha tracejada
				System.out.println(sc.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	private static void recoveryIdsDifferentConcepts(String concepts1, String concepts2) {	
		OntologyConceptServiceImpl ontologyService = new OntologyConceptServiceImpl();
		String[] concepts1Array = concepts1.split(", ");
		String[] concepts2Array = concepts2.split(", ");
		List<String> differentConcepts = new ArrayList<String>();
		for (String concept1 : concepts1Array) {
			boolean found = false;
			for (String concept2 : concepts2Array) {
				if (concept1.equals(concept2)) {
					found = true;
					break;
				}
			}
			if (!found) {
				differentConcepts.add(concept1);
			}
		}
		String idsConcepts = "";
		for (String concept : differentConcepts) {
			OntologyConcept ontologyConcept = ontologyService.recoveryOntologyByURI(concept);
			idsConcepts += ontologyConcept.getId() + ",";
		}
		System.out.println(idsConcepts);
	}

}
