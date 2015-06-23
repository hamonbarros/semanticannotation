package br.com.sann.util.baseline;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.service.OntologyConceptService;
import br.com.sann.service.impl.OntologyConceptServiceImpl;

public class ExportBaseline {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Início da extração da baseline.");
		DateFormat dformat = new SimpleDateFormat("YYYYMMddHHmm");
		String dateFormated = dformat.format(new Date());

		OntologyConceptService conceptService = new OntologyConceptServiceImpl();
		
		PrintWriter extractBaseline = new PrintWriter(new FileWriter(
				new File("Baseline_export_" + dateFormated + ".csv")));
		extractBaseline.println("TITULO,NOME_CONCEITO,CONCEITO");			
		
		Scanner scanner = new Scanner(new FileReader("Baseline_Atributos.txt")).useDelimiter("\\n");
		String line = scanner.next();
		while (scanner.hasNext()) {
			  line = scanner.next();
			  String[] lineSplit = line.split("\\|");
			  String title = lineSplit[0];
			  String[] idsOntology = lineSplit[1].split(",");
			  List<OntologyConcept> relevantConcepts = conceptService.recoveryOntolgyConceptByIds(idsOntology);
			  for (OntologyConcept concept : relevantConcepts) {				
				  extractBaseline.println(title + "," + concept.getConceptName() + "," + concept.getConcept());
			  }
			  extractBaseline.flush();
		}
		extractBaseline.close();
		System.out.println("Fim da extração da baseline.");
	}
}
