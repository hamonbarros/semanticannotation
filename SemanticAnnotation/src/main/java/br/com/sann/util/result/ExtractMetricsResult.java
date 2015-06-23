package br.com.sann.util.result;

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

public class ExtractMetricsResult {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Início da extração das métricas.");
		DateFormat dformat = new SimpleDateFormat("YYYYMMddHHmm");
		String dateFormated = dformat.format(new Date());

		PrintWriter extractMetrics = new PrintWriter(new FileWriter(
				new File("Extract_Metrics_Attrs_" + dateFormated + ".csv")));
		extractMetrics.println("TITULO,PRECISAO,COBERTURA");			
		
		Scanner scanner = new Scanner(new FileReader("Result_WFS_Attr_201506042333.txt")).useDelimiter("\\n");
		
		while (scanner.hasNext()) {
			  scanner.next();
			  String line = scanner.next();
			  if (line.startsWith("Cobertura:")) {
				  break;
			  }
			  String[] lineSplit = line.split(":");
			  String text = lineSplit[1];
			  String title = text.split("\\-")[0].trim();
			  scanner.next();
			  scanner.next();
			  scanner.next();
			  scanner.next();
			  String precisionLine = scanner.next();
			  String precision = precisionLine.split("\\:")[1].trim();
			  String recallLine = scanner.next();
			  String recall = recallLine.split("\\:")[1].trim();
			  scanner.next();
			  extractMetrics.println(title + "," + precision + "," + recall);
		}
		extractMetrics.flush();
		extractMetrics.close();
		System.out.println("Fim da extração das métricas.");
	}

}
