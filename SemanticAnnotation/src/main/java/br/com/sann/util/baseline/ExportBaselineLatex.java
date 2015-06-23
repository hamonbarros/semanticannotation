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

public class ExportBaselineLatex {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Início da extração da baseline.");
		DateFormat dformat = new SimpleDateFormat("YYYYMMddHHmm");
		String dateFormated = dformat.format(new Date());

		OntologyConceptService conceptService = new OntologyConceptServiceImpl();
		
		PrintWriter extractBaseline = new PrintWriter(new FileWriter(
				new File("Baseline_export_" + dateFormated + ".txt")));			
		
		Scanner scanner = new Scanner(new FileReader("Baseline_FT.txt")).useDelimiter("\\n");
		String line = scanner.next();
		while (scanner.hasNext()) {
			  line = scanner.next();
			  String[] lineSplit = line.split("\\|");
			  String title = lineSplit[0];
			  String[] idsOntology = lineSplit[1].split(",");
			  List<OntologyConcept> relevantConcepts = conceptService.recoveryOntolgyConceptByIds(idsOntology);
			  extractBaseline.print(" \\multirow{" + relevantConcepts.size() + "}{*}{" + title + "}");
			  for (OntologyConcept concept : relevantConcepts) {		
				  extractBaseline.print(" &" + concept.getConceptName() + " &" + concept.getConcept() + " \\\\");
			  }
			  extractBaseline.println("\\hline");
			  extractBaseline.flush();
		}
		extractBaseline.close();
		System.out.println("Fim da extração da baseline.");
	}
}
